package com.yupi.yurpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.serializer.Serializer;
import com.yupi.yurpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcConfig.getRpcConfig().getSerializer());
//        JdkSerializer serializer = new JdkSerializer();

        // 构造请求
        RpcRequest rpcRequest = new RpcRequest().builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .serviceVersion(RpcConstant.DEFAULT_SERVICE_VERSION).
                build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
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
            String serverHost = serviceMetaInfo.getServiceHost();
            Integer serverPort = serviceMetaInfo.getServicePort();
            String serverUrl = String.format("http://%s:%s", serverHost, serverPort);

            try(HttpResponse httpResponse = HttpRequest.post(serverUrl)
                    .body(bodyBytes)
                    .execute()){
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
