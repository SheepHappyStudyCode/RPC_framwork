package com.yupi.springbootprovider;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;
import com.yupi.yurpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User changeUsername(User user) {
        System.out.println(user.getUsername());
        user.setUsername("newUsername");
        return user;
    }
}
