package com.light.rpc.core.loadbalance.impl;

import com.light.rpc.core.loadbalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();
    @Override
    public InetSocketAddress select(String serviceName, List<InetSocketAddress> addresses) {
        if(addresses == null || addresses.isEmpty()) {
            throw new RuntimeException("地址列表为空，无法负载均衡");
        }
        return addresses.get(random.nextInt(addresses.size()));
    }
}
