package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.model.RpcResponse;

import java.util.concurrent.Callable;

public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse retry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
