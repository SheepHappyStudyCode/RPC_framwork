package com.yupi.yurpc.server;

import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.model.RpcRequest;
import com.yupi.yurpc.model.RpcResponse;
import com.yupi.yurpc.registry.LocalRegistry;
import com.yupi.yurpc.serializer.Serializer;
import com.yupi.yurpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        // 打印日志
        System.out.println("receive request: " + request.method() + " " + request.uri());

        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcConfig.getRpcConfig().getSerializer());

        // 异步处理 http 请求
        request.bodyHandler(body ->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try{
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();

            // 如果请求为 null， 直接返回

            if(request == null){
                rpcResponse.setMessage("request is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }


            try {
                // 获取服务实现类
                Class<?>  implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setType(method.getReturnType());
                rpcResponse.setMessage("方法调用成功");

            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 响应

            doResponse(request, rpcResponse, serializer);
        });


    }

    private void doResponse(HttpServerRequest request, RpcResponse response, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");

        try {
            byte[] serialized = serializer.serialize(response);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }

    }
}
