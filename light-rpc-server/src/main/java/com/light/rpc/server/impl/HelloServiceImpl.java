package com.light.rpc.server.impl;

import com.light.rpc.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好，" + name + "！来自 Light-RPC 服务端";
    }
}
