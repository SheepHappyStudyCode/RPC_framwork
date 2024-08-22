package com.yupi.yurpc.mock;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;
import com.yupi.yurpc.proxy.ServiceProxyFactory;
import org.junit.Test;

public class MockTest {

    @Test
    public void testMock(){
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setUsername("张三");
        user.setDesc("我是张三，但我遵纪守法");

        User newUser = userService.changeUsername(user);


    }
}
