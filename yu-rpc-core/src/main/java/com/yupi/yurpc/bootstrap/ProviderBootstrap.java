package com.yupi.yurpc.bootstrap;

import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.model.ServiceRegisterInfo;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.Server;
import com.yupi.yurpc.server.tcp.VertxTcpServer;

import java.util.List;

public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList) {
        for (ServiceRegisterInfo serviceRegisterInfo : serviceRegisterInfoList) {
            // 在本地注册服务
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            RpcApplication.init();
            RpcConfig rpcConfig = RpcConfig.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            registry.register(serviceMetaInfo);

            Server server = new VertxTcpServer();
            server.doStart(rpcConfig.getServerPort());
            System.out.println("启动成功");
        }

    }
}
