package com.yupi.yurpc.utils;

import cn.hutool.core.util.StrUtil;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.config.RpcConfigWrapper;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class ConfigUtils {

    public static RpcConfig loadConfig(String environment){
        StringBuilder configFileStringBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileStringBuilder.append("-").append(environment);
        }
        configFileStringBuilder.append(".yml");

        Yaml yaml = new Yaml();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileStringBuilder.toString());
//            InputStream input = new RpcApplication().getClass().getClassLoader().getResourceAsStream(configFileStringBuilder.toString());
            RpcConfigWrapper configWrapper = yaml.loadAs(input, RpcConfigWrapper.class);
            RpcConfig rpcConfig = configWrapper.getRpc();
            return rpcConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RpcConfig();
    }



//    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment){
//        StringBuilder configFileStringBuilder = new StringBuilder("application");
//        if(StrUtil.isNotBlank(environment)){
//            configFileStringBuilder.append("-").append(environment);
//        }
//        configFileStringBuilder.append(".properties");
//        Props props = new Props(configFileStringBuilder.toString());
//        return props.toBean(tClass, prefix);
//    }
//
//    public static <T> T loadConfig(Class<T> tClass, String prefix){
//        return loadConfig(tClass, prefix, null);
//    }
}
