package com.yupi.provider;

import com.yupi.common.model.User;
import com.yupi.common.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User changeUsername(User user) {
        System.out.println(user.getUsername());
        user.setUsername("newUsername");
        return user;
    }
}
