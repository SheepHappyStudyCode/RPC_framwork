package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.spi.SpiLoader;

public class LoadBalancerFactory {
    static{
        SpiLoader.load(LoadBalancer.class);
    }

//    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static  LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
