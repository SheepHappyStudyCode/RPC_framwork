package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 轮询负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer{
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()){
            return null;
        }

        return serviceMetaInfoList.get(random.nextInt(serviceMetaInfoList.size()));
    }
}
