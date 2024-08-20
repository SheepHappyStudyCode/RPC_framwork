package com.yupi.yurpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        log.info("mock invoke {}", method);
        // 根据返回值类型获得默认的返回值
        Class<?> returnType = method.getReturnType();
        // 返回默认值
        return getDefaultValue(returnType);

    }


    private Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return 0;
            }
            if (type == long.class) {
                return 0L;
            }
            if (type == short.class) {
                return (short) 0;
            }
            if (type == byte.class) {
                return (byte) 0;
            }
            if (type == boolean.class) {
                return false;
            }
            if (type == float.class) {
                return 0.0f;
            }
            if (type == double.class) {
                return 0.0d;
            }
            if (type == char.class) {
                return '空';
            }
        }

        return null;
    }

}
