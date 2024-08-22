package com.yupi.yurpc.config;

import lombok.Data;

@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://127.0.0.1:2379";

    private String username;

    private String password;

    /**
     * 超时时间 （单位：毫秒）
     */
    private Long timeout = 10000L;
}
