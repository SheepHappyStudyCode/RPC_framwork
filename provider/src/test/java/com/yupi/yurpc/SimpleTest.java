package com.yupi.yurpc;

import org.junit.Test;

public class SimpleTest {
    @Test
    public void test() throws Exception {
        Thread t = new Thread(() -> {
            System.out.println("start new thread!");
        });
        t.start(); // 启动新线程

    }
}
