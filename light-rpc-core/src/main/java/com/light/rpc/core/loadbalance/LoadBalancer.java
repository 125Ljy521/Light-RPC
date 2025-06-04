package com.light.rpc.core.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {
    InetSocketAddress select(String serviceName, List<InetSocketAddress> addresses);
}
