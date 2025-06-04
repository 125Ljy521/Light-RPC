package com.light.rpc.registry;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServer {
    private static final Map<String, List<String>> serviceMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("🚀 注册中心启动成功，监听端口 9999");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handle(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("❌ 注册中心启动失败：" + e.getMessage());
        }
    }

    private static void handle(Socket socket) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ){
            String line = reader.readLine();
            if (line == null) {return;}

            if(line.startsWith("register:")){
                // register:com.light.rpc.api.HelloService:127.0.0.1:8080
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    String serviceName = parts[1];
                    String address = parts[2] + ":" + parts[3];

                    serviceMap.putIfAbsent(serviceName, new ArrayList<>());
                    List<String> addresses = serviceMap.get(serviceName);
                    if (!addresses.contains(address)) {
                        addresses.add(address);
                    }

                    writer.write("ok\n");
                    writer.flush();
                    System.out.println("✅ 注册成功：" + serviceName + " -> " + address);
                }
            } else if(line.startsWith("lookup:")){
                String serviceName = line.substring("lookup:".length());
                List<String> addresses = serviceMap.get(serviceName);
                if (addresses != null && !addresses.isEmpty()) {
                    writer.write(String.join(",", addresses) + "\n");
                } else {
                    writer.write("not_found\n");
                }
                writer.flush();
            }

        } catch (IOException e){
            System.err.println("❌ 注册中心处理失败：" + e.getMessage());
        }
    }
}
