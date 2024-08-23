package com.yupi.yurpc.proxy;

import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        // 构造请求
        RpcRequest rpcRequest = new RpcRequest().builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .serviceVersion(RpcConstant.DEFAULT_SERVICE_VERSION).
                build();

        try {
            // 发送请求
            RpcConfig rpcConfig = RpcConfig.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            List<ServiceMetaInfo> serviceMetaInfoList = registry.discoveryServices(String.format("%s:%s", rpcRequest.getServiceName(), rpcRequest.getServiceVersion()));
            if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
                throw new RuntimeException("no service found");
            }
            // todo 从服务列表挑选一个服务
            ServiceMetaInfo serviceMetaInfo = serviceMetaInfoList.get(0);


            // 发送 TCP 请求
            RpcResponse response = VertxTcpClient.doRequest(rpcRequest, serviceMetaInfo);
            return response.getData();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}





