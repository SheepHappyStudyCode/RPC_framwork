package com.yupi.consumer;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;
import com.yupi.yurpc.proxy.ServiceProxyFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setUsername("张三");
        user.setDesc("我是张三，但我遵纪守法");

        int res = userService.getNumber(user);

        System.out.println("res = " + res);





    }
}
