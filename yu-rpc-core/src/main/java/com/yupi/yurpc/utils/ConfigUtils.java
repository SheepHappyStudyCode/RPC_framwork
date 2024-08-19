package com.yupi.yurpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {

    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment){
        StringBuilder configFileStringBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileStringBuilder.append("-").append(environment);
        }
        configFileStringBuilder.append(".properties");
        Props props = new Props(configFileStringBuilder.toString());
        return props.toBean(tClass, prefix);
    }

    public static <T> T loadConfig(Class<T> tClass, String prefix){
        return loadConfig(tClass, prefix, null);
    }
}
