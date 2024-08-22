package com.yupi.consumer;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.proxy.ServiceProxyFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        RpcApplication.init();

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setUsername("张三");
        user.setDesc("我是张三，但我遵纪守法");

        for (int i = 0; i < 3; i++) {
            User newUser = userService.changeUsername(user);
            if(newUser != null){
                System.out.println("调用成功");
                System.out.println(newUser.getUsername());

            }
        }





    }
}
