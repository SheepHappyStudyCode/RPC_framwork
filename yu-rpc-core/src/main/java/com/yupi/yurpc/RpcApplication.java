package com.yupi.yurpc;


import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    void init(){
        RpcConfig rpcConfig = RpcConfig.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        RegistryFactory.getInstance(registryConfig.getRegistry());
        log.info("注冊中心启动，配置为{}", registryConfig);
    }
}

