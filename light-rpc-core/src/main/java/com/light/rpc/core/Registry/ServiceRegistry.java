package com.light.rpc.core.Registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    void register(String serviceName, InetSocketAddress address);
    InetSocketAddress lookup(String serviceName);
}
