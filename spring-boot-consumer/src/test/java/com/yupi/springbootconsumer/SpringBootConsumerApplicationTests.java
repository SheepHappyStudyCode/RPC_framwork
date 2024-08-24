package com.yupi.springbootconsumer;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;
import com.yupi.yurpc.springboot.starter.annotation.RpcReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootConsumerApplicationTests {

    @RpcReference
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("张三");
        user.setDesc("我是遵纪守法的张三");
        User res = userService.changeUsername(user);
        if(res != null){
            System.out.println("调用成功");
            System.out.println(res);
        }


    }

}
