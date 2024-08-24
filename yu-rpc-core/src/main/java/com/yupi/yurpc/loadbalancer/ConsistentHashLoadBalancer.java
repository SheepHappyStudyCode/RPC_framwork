package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 轮询负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{


    private final int VIRTUAL_NODE_COUNT = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()){
            return null;
        }

        if(serviceMetaInfoList.size() == 1){
            return serviceMetaInfoList.get(0);
        }

        // 构造哈希环
        TreeMap<BigInteger, ServiceMetaInfo> virtualNodes = new TreeMap<>();

        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                String virtualNodeName = serviceMetaInfo.getServiceAddress() + "#" + i;
                virtualNodes.put(getHash(virtualNodeName), serviceMetaInfo);
            }
        }

        BigInteger hash = getHash(requestParams);
        Map.Entry<BigInteger, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);

        if(entry == null){
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();

    }


    public BigInteger getHash(Object key)  {
        // 创建一个MessageDigest实例:
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 反复调用update输入数据:
        md.update(key.toString().getBytes());

        byte[] result = md.digest(); // 16 bytes: 68e109f0f40ca72a15e05cc22786f8e6
        return new BigInteger(result); // 68e109f0f40ca72a15e05cc22786f8e6
    }
}
