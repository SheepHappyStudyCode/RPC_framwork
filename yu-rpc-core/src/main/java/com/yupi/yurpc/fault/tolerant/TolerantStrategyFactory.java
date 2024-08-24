package com.yupi.yurpc.fault.tolerant;

import com.yupi.yurpc.spi.SpiLoader;

public class TolerantStrategyFactory {
    static{
        SpiLoader.load(TolerantStrategy.class);
    }

//    private static final TolerantStrategy DEFAULT_SERIALIZER = new JdkTolerantStrategy();

    public static  TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
