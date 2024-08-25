package com.yupi.yurpc.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.yurpc.config.RpcConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigUtils {

    public static RpcConfig loadConfig(String environment){
        StringBuilder configFileStringBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileStringBuilder.append("-").append(environment);
        }
        configFileStringBuilder.append(".yml");

        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileStringBuilder.toString());
            Map map = (Map ) yaml.load(inputStream);
            Map rpcConfigMap = (Map)map.get("rpc");
            ObjectMapper objectMapper = new ObjectMapper();
            RpcConfig rpcConfig = objectMapper.convertValue(rpcConfigMap, RpcConfig.class);
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
