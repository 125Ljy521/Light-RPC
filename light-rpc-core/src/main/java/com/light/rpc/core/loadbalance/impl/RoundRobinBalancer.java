package com.light.rpc.core.loadbalance.impl;

import com.light.rpc.core.loadbalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinBalancer implements LoadBalancer {
    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public InetSocketAddress select(String serviceName, List<InetSocketAddress> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            throw new RuntimeException("地址列表为空，无法负载均衡");
        }

        counters.putIfAbsent(serviceName, new AtomicInteger(0));
        int index = Math.abs(counters.get(serviceName).getAndIncrement()) % addresses.size();
        return addresses.get(index);
    }
}
