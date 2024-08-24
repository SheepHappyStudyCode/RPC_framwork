package com.yupi.yurpc.proxy;

import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.fault.retry.RetryStrategy;
import com.yupi.yurpc.fault.retry.RetryStrategyFactory;
import com.yupi.yurpc.fault.tolerant.TolerantStrategy;
import com.yupi.yurpc.fault.tolerant.TolerantStrategyFactory;
import com.yupi.yurpc.loadbalancer.LoadBalancer;
import com.yupi.yurpc.loadbalancer.LoadBalancerFactory;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.model.ServiceMetaInfo;
import com.yupi.yurpc.registry.Registry;
import com.yupi.yurpc.registry.RegistryFactory;
import com.yupi.yurpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // 寻找服务
        RpcConfig rpcConfig = RpcConfig.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        List<ServiceMetaInfo> serviceMetaInfoList = registry.discoveryServices(String.format("%s:%s", rpcRequest.getServiceName(), rpcRequest.getServiceVersion()));
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            throw new RuntimeException("no service found");
        }

        // 负载均衡
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("method", rpcRequest.getMethodName());
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        RpcResponse response;
        try {
            // 发送 TCP 请求
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            response = retryStrategy.retry(() ->
                VertxTcpClient.doRequest(rpcRequest, serviceMetaInfo)
            );

        } catch (Exception e) {
            e.printStackTrace();
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            response = tolerantStrategy.doTolerant(null, e);        }

        return response.getData();
    }
}





