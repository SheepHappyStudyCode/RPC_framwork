package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()){
            return null;
        }

        return serviceMetaInfoList.get(currentIndex.getAndIncrement() % serviceMetaInfoList.size());
    }
}
