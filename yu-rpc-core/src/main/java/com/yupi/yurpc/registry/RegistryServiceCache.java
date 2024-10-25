package com.yupi.yurpc.registry;

import com.yupi.yurpc.model.ServiceMetaInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RegistryServiceCache {
    // 服务名 =》 服务信息列表
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
