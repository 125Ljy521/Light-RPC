package com.light.rpc.registry;

import com.light.rpc.core.Registry.ServiceRegistry;
import com.light.rpc.core.loadbalance.LoadBalancer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

public class RemoteServiceRegistry implements ServiceRegistry {
    private static final String REGISTRY_HOST = "127.0.0.1";
    private static final int REGISTRY_PORT = 9999;
    private final LoadBalancer loadBalancer;

    public RemoteServiceRegistry() {
        this.loadBalancer = ServiceLoader.load(LoadBalancer.class).findFirst()
                .orElseThrow(() -> new RuntimeException("未找到负载均衡实现"));
    }

    @Override
    public void register(String serviceName, InetSocketAddress address) {
        try (Socket socket = new Socket(REGISTRY_HOST, REGISTRY_PORT);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String registerMsg = String.format("register:%s:%s:%d\n", serviceName, address.getHostString(), address.getPort());
            writer.write(registerMsg);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("服务注册失败", e);
        }
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        try (Socket socket = new Socket(REGISTRY_HOST, REGISTRY_PORT);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String request = "lookup:" + serviceName + "\n";
            writer.write(request);
            writer.flush();

            String responseLine = reader.readLine();
            if (responseLine == null || responseLine.equals("not_found")) {
                throw new RuntimeException("找不到服务地址：" + serviceName);
            }

            // 把字符串地址转化成 InetSocketAddress 列表
            String[] addressArray = responseLine.split(",");
            List<InetSocketAddress> addressList = new ArrayList<>();
            for (String addr : addressArray) {
                String[] hostPort = addr.split(":");
                addressList.add(new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1])));
            }

            // 使用负载均衡策略选择地址
            return loadBalancer.select(serviceName, addressList);

        } catch (IOException e) {
            throw new RuntimeException("服务发现失败", e);
        }
    }
}
