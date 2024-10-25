package com.yupi.yurpc.proxy;

import com.yupi.yurpc.config.RpcConfig;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 * 用于生成代理对象
 */
public class ServiceProxyFactory{

    public static <T> T getProxy(Class<T> serviceClass){
        // 如果是mock模式，则返回mock代理对象
        if(RpcConfig.getRpcConfig().getMock()){
            return (T) Proxy.newProxyInstance(
                    serviceClass.getClassLoader(),
                    new Class[]{serviceClass},
                    new MockServiceProxy());
        }

        // 否则返回普通代理对象
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
