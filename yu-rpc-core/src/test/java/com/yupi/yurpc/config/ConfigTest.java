package com.yupi.yurpc.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
public class ConfigTest {
    @Test
    public void testYml() throws FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml");
        Yaml yaml = new Yaml();
        try {

            RpcConfigWrapper configWrapper = yaml.loadAs(inputStream, RpcConfigWrapper.class);
            RpcConfig rpcConfig = configWrapper.getRpc();
            System.out.println(configWrapper);
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
