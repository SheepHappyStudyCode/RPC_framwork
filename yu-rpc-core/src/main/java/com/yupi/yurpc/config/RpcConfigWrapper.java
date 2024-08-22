package com.yupi.yurpc.config;

import lombok.Data;

/**
 * @description: RpcConfig的包装类，方便从 yml 文件读取配置
 */
@Data
public class RpcConfigWrapper {
    private RpcConfig rpc;
    private RegistryConfig register;
}