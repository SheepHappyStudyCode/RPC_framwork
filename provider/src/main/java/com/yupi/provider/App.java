package com.yupi.provider;

import com.yupi.common.service.UserService;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.Server;
import com.yupi.yurpc.server.tcp.VertxTcpServer;

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

        RpcApplication.init();
        RpcConfig rpcConfig = RpcConfig.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost("127.0.0.1");
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        registry.register(serviceMetaInfo);

        Server server = new VertxTcpServer();
        server.doStart(rpcConfig.getServerPort());
        System.out.println("启动成功");

    }
}
