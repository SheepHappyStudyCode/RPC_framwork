package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.spi.SpiLoader;

public class RetryStrategyFactory {
    static{
        SpiLoader.load(RetryStrategy.class);
    }

//    private static final RetryStrategy DEFAULT_SERIALIZER = new JdkRetryStrategy();

    public static  RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }
}
