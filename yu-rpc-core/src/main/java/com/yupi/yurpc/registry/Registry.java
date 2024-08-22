package com.yupi.yurpc.registry;

import com.yupi.yurpc.config.RegistryConfig;
import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {
    void init(RegistryConfig registryConfig);

    void destroy();

    void register(ServiceMetaInfo serviceMetaInfo);

    void unRegister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> discoveryServices(String serviceKey);
}
