package com.light.rpc.client;

import com.light.rpc.api.HelloService;
import com.light.rpc.core.Registry.ServiceRegistry;
import com.light.rpc.registry.RemoteServiceRegistry;

public class ClientApplication {
    public static void main(String[] args) {
        // 远程查找服务地址的注册器
        ServiceRegistry registry = new RemoteServiceRegistry();

        // 传入注册器构造代理工厂
        RpcClientProxy proxy = new RpcClientProxy(registry);

        // 创建动态代理
        HelloService helloService = proxy.getProxy(HelloService.class);

        // 多次远程调用，看是否是不同端口响应
        for (int i = 1; i <= 10; i++) {
            String result = helloService.sayHello("用户" + i);
            System.out.println("第 " + i + " 次调用结果：" + result);
        }
    }
}