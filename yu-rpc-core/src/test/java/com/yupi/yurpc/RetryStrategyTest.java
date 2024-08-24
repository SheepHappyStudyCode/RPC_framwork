package com.yupi.yurpc;

import com.yupi.yurpc.fault.retry.FixedIntervalRetryStrategy;
import com.yupi.yurpc.fault.retry.RetryStrategy;
import org.junit.Test;

public class RetryStrategyTest {

    @Test
    public void testNoRetry() {
        RetryStrategy retryStratety = new FixedIntervalRetryStrategy();
        try {
            retryStratety.retry(() ->{
                System.out.println("任务进行");
                throw new RuntimeException("模拟失败");
            });
            System.out.println("no retry");
        } catch (Exception e) {
            System.out.println("重试失败");
            throw new RuntimeException(e);
        }

    }
}
