package com.yupi.yurpc;


import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    // 使用volatile关键字确保多线程环境下的可见性和有序性
    private static volatile RpcConfig rpcConfig;

    private static RpcConfig init(){
        RpcConfig newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        log.info("init config: " + newRpcConfig);
        return newRpcConfig;

    }

    private static RpcConfig init(RpcConfig newRpcConfig){
        return newRpcConfig;
    }

    // 提供一个全局访问点
    public static RpcConfig getRpcConfig() {
        // 第一次检查，不加锁
        if (rpcConfig == null) {
            // 同步块，确保只有一个线程可以进入
            synchronized (RpcConfig.class) {
                // 第二次检查，加锁
                if (rpcConfig == null) {
                    rpcConfig = init();
                }
            }
        }
        return rpcConfig;
    }
}

