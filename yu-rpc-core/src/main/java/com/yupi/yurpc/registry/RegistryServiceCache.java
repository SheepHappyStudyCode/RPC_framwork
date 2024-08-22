package com.yupi.yurpc.registry;

import com.yupi.yurpc.model.ServiceMetaInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RegistryServiceCache {
    Map<String, List<ServiceMetaInfo>> map =  new HashMap<>();

    public void writeCache(String serviceKey, List<ServiceMetaInfo> serviceMetaInfoList) {
        map.put(serviceKey, serviceMetaInfoList);
    }

    public List<ServiceMetaInfo> readCache(String serviceKey) {
        return map.get(serviceKey);
    }

    public void clearCache(String serviceKey) {
        map.put(serviceKey, null);
    }
}
