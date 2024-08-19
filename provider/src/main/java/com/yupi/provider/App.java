package com.yupi.provider;

import com.yupi.common.service.UserService;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.server.HttpServer;
import com.yupi.yurpc.server.VertxHttpServer;

/**
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
        System.out.println("启动成功");

    }
}
