package com.light.rpc.server.impl;

import com.light.rpc.api.HelloService;

public class HelloServiceImpl8080 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "来自 8080 的响应：" + name;
    }
}