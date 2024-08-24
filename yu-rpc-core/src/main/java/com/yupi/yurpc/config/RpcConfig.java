package com.yupi.yurpc.config;

import com.yupi.yurpc.constant.SerializerKeys;
import com.yupi.yurpc.fault.retry.RetryStrategyKeys;
import com.yupi.yurpc.fault.tolerant.TolerantStrategyKeys;
import com.yupi.yurpc.utils.ConfigUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RpcConfig {
    // 使用volatile关键字确保多线程环境下的可见性和有序性
    private static volatile RpcConfig rpcConfig;

    private static void init(){
        RpcConfig newRpcConfig = ConfigUtils.loadConfig(null);
        log.info("init config: " + newRpcConfig);
        rpcConfig = newRpcConfig;

    }

    private static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
    }

    // 提供一个全局访问点
    public static RpcConfig getRpcConfig() {
        // 第一次检查，不加锁
        if (rpcConfig == null) {
            // 同步块，确保只有一个线程可以进入
            synchronized (RpcConfig.class) {
                // 第二次检查，加锁
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }


    private String name = "yu-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8081;

    /**
     * 是否模拟调用
     */
    private Boolean mock = false;

    private String serializer = SerializerKeys.JDK;

    private String loadBalancer = "random";

    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;

    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    private RegistryConfig registryConfig = new RegistryConfig();
}
