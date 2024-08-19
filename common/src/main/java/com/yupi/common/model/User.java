package com.yupi.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String username;

    private String desc;
}
