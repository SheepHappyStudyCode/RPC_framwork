package com.yupi.yurpc.registry;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry{
    private Client client;

    private KV kvClient;

    private Watch watchClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 在注册中心已经注册的节点列表 (服务端使用）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 缓存
     */
    private final RegistryServiceCache localCache = new RegistryServiceCache();

    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 注册中心初始化
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        watchClient = client.getWatchClient();
        heartBeat();

        // 停机前自动执行
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    /**
     * 注册中心下线
     */
    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(key + "节点下线失败 ");
            }
        }

        if(kvClient != null) {
            kvClient.close();
        }

        if(watchClient != null) {
            watchClient.close();
        }

        if(client != null){
            client.close();
        }
    }

    /**
     * 注册服务 （服务端）
     * @param serviceMetaInfo
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo)  {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        long leaseId;
        try {
            // 创建一个 30 秒的租约
            leaseId = leaseClient.grant(30).get().getID();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        try {
            kvClient.put(key, value, putOption).get();
            localRegisterNodeKeySet.add(registerKey);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 服务注销 （服务端）
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8)).get();
            localRegisterNodeKeySet.remove(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务发现 （消费端）
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> discoveryServices(String serviceKey) {

        // 先查缓存
        List<ServiceMetaInfo> serviceMetaInfoList = localCache.readCache(serviceKey);
        if(CollectionUtil.isNotEmpty(serviceMetaInfoList)){
            return serviceMetaInfoList;
        }
        // 搜索前缀
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();

            // 解析服务信息
            serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        // 监听节点变化
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            // 写入缓存
            localCache.writeCache(serviceKey, serviceMetaInfoList);
            return serviceMetaInfoList;

        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    /**
     * 心跳检测 （服务端）
     */
    @Override
    public void heartBeat() {
        CronUtil.schedule("0/10 * * * * ? ", (Task) () -> {
                for(String registerKey : localRegisterNodeKeySet){
                    try{
                        List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(registerKey, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 节点已过期， 需要重启节点才能注册
                        if(CollectionUtil.isEmpty(keyValueList)){
                            continue;
                        }

                        KeyValue keyValue = keyValueList.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        throw new RuntimeException(registerKey + "续约失败 ");
                    }
                }
            });
        // 支持秒级定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    /**
     * 监听服务节点 （消费端）
     * 主要与缓存搭配使用
     * @param serviceNode
     */
    @Override
    public void watch(String serviceNode) {
        // 检查该节点是否已经在检测列表中
        boolean add = watchingKeySet.add(serviceNode);
        if(add){
            String serviceKey = serviceNode.split("/")[2];
            watchClient.watch(ByteSequence.from(serviceNode, StandardCharsets.UTF_8), (response) -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()){
                        case PUT:
                            // 更新缓存
                            //localCache.clearCache(serviceKey);
                            break;
                        case DELETE:
                            // 删除缓存
                            localCache.clearCache(serviceKey);
                            break;

                        // todo 其他事件
                            default:
                                break;
                    }
                }

            });
        }


    }
}


