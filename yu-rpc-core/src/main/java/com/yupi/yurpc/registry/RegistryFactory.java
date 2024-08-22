package com.yupi.yurpc.registry;

import com.yupi.yurpc.spi.SpiLoader;

public class RegistryFactory {
    static{
        SpiLoader.load(Registry.class);
    }

//    private static final Registry DEFAULT_Registry = new JdkRegistry();

    public static  Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class, key);
    }
}