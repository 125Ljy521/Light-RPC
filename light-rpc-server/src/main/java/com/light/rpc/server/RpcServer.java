// src/main/java/com/light/rpc/server/RpcServer.java
package com.light.rpc.server;

import com.light.rpc.core.RpcRequest;
import com.light.rpc.core.RpcResponse;
import com.light.rpc.core.protocol.RpcConstants;
import com.light.rpc.core.protocol.RpcDecoder;
import com.light.rpc.core.protocol.RpcEncoder;
import com.light.rpc.core.protocol.RpcMessage;
import com.light.rpc.core.serialize.Serializer;
import com.light.rpc.core.serialize.impl.JdkSerializer;
import com.light.rpc.core.serialize.impl.JsonSerializer;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RpcServer {
    private final Map<String, Object> serviceMap = new HashMap<String, Object>();
    private final Serializer serializer = new JsonSerializer(); // 后续可以换
    private final RpcEncoder encoder = new RpcEncoder(serializer);
    private final RpcDecoder decoder = new RpcDecoder(serializer);

    /**
     * 注册服务到服务表中
     */
    public void registerService(Class<?> interfaceClass, Object impl) {
        serviceMap.put(interfaceClass.getName(), impl);
        System.out.println("✅ 服务注册成功：" + interfaceClass.getName());
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("🚀 Light-RPC Server started at port " + port);

            while (true) {
                try(Socket socket = serverSocket.accept();
                    InputStream in  = socket.getInputStream();
                    OutputStream out = socket.getOutputStream()
                ) {
                    // 1. 解码请求
                    RpcMessage requestMsg = decoder.decode(in);
                    RpcRequest request = (RpcRequest) requestMsg.getData();

                    // 2. 根据接口名获取服务实例
                    Object service = serviceMap.get(request.getInterfaceName());
                    if (service == null) {
                        throw new RuntimeException("未找到服务：" + request.getInterfaceName());
                    }

                    // 3. 反射调用方法
                    Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
                    Object result = method.invoke(service, request.getArgs());

                    // 4. 构造响应
                    RpcResponse response = RpcResponse.success(result);
                    RpcMessage responseMsg = new RpcMessage();
                    responseMsg.setData(response);
                    responseMsg.setRequestId(requestMsg.getRequestId());
                    responseMsg.setMessageType(RpcConstants.RESPONSE_TYPE);

                    // 5. 编码并发送
                    byte[] bytes = encoder.encode(responseMsg);
                    out.write(bytes);
                    out.flush();
                } catch (Exception e) {
                    System.err.println("❌ 服务端处理失败：" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("❌ 启动服务失败：" + e.getMessage());;
        }
    }
}