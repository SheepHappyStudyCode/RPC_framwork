package com.yupi.yurpc;

import com.yupi.yurpc.loadbalancer.ConsistentHashLoadBalancer;
import com.yupi.yurpc.loadbalancer.LoadBalancer;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.ServiceMetaInfo;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancerTest {

    @Test
    public void testConsistentHashLoadBalancer() {
        LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "changeUsername");

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("127.0.0.1");
        serviceMetaInfo1.setServicePort(8080);

        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("127.0.0.1");
        serviceMetaInfo2.setServicePort(8081);

        ServiceMetaInfo serviceMetaInfo3 = new ServiceMetaInfo();
        serviceMetaInfo3.setServiceName("myService");
        serviceMetaInfo3.setServiceVersion("1.0");
        serviceMetaInfo3.setServiceHost("128.0.0.1");
        serviceMetaInfo3.setServicePort(8080);

        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2, serviceMetaInfo3);
        RpcRequest rpcRequest = new RpcRequest();

        rpcRequest.setMethodName("changeUsername");
        rpcRequest.setArgs(new Object[]{1, 2, 3});

        requestParams = new HashMap<>();
        requestParams.put("rpcRequest", rpcRequest);
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo.getServiceAddress());

        rpcRequest.setArgs(new Object[]{"ssf", "sfsf33"});
        requestParams.put("rpcRequest", rpcRequest);
        serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo.getServiceAddress());


    }
}
