package com.light.rpc.server;

import com.light.rpc.api.HelloService;
import com.light.rpc.api.UserService;
import com.light.rpc.core.Registry.ServiceRegistry;
import com.light.rpc.registry.RemoteServiceRegistry;
import com.light.rpc.server.impl.HelloServiceImpl;
import com.light.rpc.server.impl.UserServiceImpl;

import java.net.InetSocketAddress;

public class ServerApplication {
    public static void main(String[] args) {
        // 注册到注册中心（远程注册中心通信）
        ServiceRegistry registry = new RemoteServiceRegistry();
        registry.register(HelloService.class.getName(),new InetSocketAddress("127.0.0.1",8080));
        registry.register(UserService.class.getName(),new InetSocketAddress("127.0.0.1",8080));

        //启动RpcServer，监听端口，等待客户端来调用
        RpcServer server = new RpcServer();
        server.registerService(HelloService.class, new HelloServiceImpl());
        server.registerService(UserService.class, new UserServiceImpl());
        server.start(8080);
    }
}
