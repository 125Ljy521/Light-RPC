package com.light.rpc.server;

import com.light.rpc.api.HelloService;
import com.light.rpc.core.Registry.ServiceRegistry;
import com.light.rpc.registry.RemoteServiceRegistry;
import com.light.rpc.server.impl.HelloServiceImpl8081;

import java.net.InetSocketAddress;

public class ServerApplication8081 {
    public static void main(String[] args) {
        ServiceRegistry registry = new RemoteServiceRegistry();
        registry.register(HelloService.class.getName(), new InetSocketAddress("127.0.0.1", 8081));

        RpcServer server = new RpcServer();
        server.registerService(HelloService.class, new HelloServiceImpl8081());
        server.start(8081);
    }
}
