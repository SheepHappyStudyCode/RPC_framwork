package com.yupi.provider;

import com.yupi.common.service.UserService;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.HttpServer;
import com.yupi.yurpc.server.VertxHttpServer;

/**
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // 在本地注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        RpcConfig rpcConfig = RpcConfig.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost("127.0.0.1");
        serviceMetaInfo.setServicePort(8082);
        registry.register(serviceMetaInfo);

        HttpServer server = new VertxHttpServer();
        server.doStart(rpcConfig.getServerPort());
        System.out.println("启动成功");

    }
}
