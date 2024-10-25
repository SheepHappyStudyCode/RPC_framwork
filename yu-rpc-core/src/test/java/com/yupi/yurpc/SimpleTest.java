package com.yupi.yurpc;

import org.junit.Test;

import java.lang.reflect.Method;

public class SimpleTest {
    @Test
    public void test() throws Exception {
        // 获取Integer.parseInt(String)方法，参数为String:
        Method m = Integer.class.getMethod("parseInt", String.class);
        // 调用该静态方法并获取结果:
        Integer n = (Integer) m.invoke(null, "12345");
        // 打印调用结果:
        System.out.println(n);
    }
}
