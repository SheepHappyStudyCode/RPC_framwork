package com.yupi.yurpc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;


@Slf4j
public class ConfigTest {
    @Test
    public void testYml() throws FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml");
        Yaml yaml = new Yaml();
        try {

            Map map = (Map ) yaml.load(inputStream);
            Map rpcConfigMap = (Map)map.get("rpc");
            ObjectMapper objectMapper = new ObjectMapper();
            RpcConfig rpcConfig = objectMapper.convertValue(rpcConfigMap, RpcConfig.class);
            System.out.println(rpcConfig);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testRpcConfig() throws FileNotFoundException {
        RpcConfig rpcConfig = RpcConfig.getRpcConfig();
        System.out.println(rpcConfig);

    }

    @Test
    public void simpleTest(){
        String s = "/rpc/";
        s.split("/");
        System.out.println(s.split("/"));

    }
}
