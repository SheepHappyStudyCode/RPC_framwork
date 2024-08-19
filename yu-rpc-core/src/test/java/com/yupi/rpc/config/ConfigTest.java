package com.yupi.rpc.config;

import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.config.RpcConfigWrapper;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);

    }

    @Test
    public void simpleTest(){
        InputStream input = new RpcApplication().getClass().getClassLoader().getResourceAsStream("application.yml");
        try (Scanner scanner = new Scanner(input)) {
            String content = scanner.useDelimiter("\\A").next();
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
