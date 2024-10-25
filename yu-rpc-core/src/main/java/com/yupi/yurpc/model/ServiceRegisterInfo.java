package com.yupi.yurpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本地注册的服务信息
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo <T>{
    private String serviceName;

    private Class<? extends T> implClass;
}
