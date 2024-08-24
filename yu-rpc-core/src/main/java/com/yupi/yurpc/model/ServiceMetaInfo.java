package com.yupi.yurpc.model;

import lombok.Data;

@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 版本号
     */
    private String serviceVersion = "1.0";

    private String serviceHost;

    private Integer servicePort;

    private String serviceGroup = "default";

    public String getServiceKey() {
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    public String getServiceAddress(){
        return String.format("%s:%s", serviceHost, servicePort);
    }

    public String getServiceNodeKey(){
        return String.format("%s/%s", getServiceKey(), getServiceAddress());
    }
}
