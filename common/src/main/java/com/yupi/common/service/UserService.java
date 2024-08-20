package com.yupi.common.service;

import com.yupi.common.model.User;

public interface UserService {
    User changeUsername(User user);

    default int getNumber(User user){
        return 1;
    }
}
