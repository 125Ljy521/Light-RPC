package com.light.rpc.client;

import com.light.rpc.core.Registry.ServiceRegistry;
import com.light.rpc.core.RpcRequest;
import com.light.rpc.core.RpcResponse;
import com.light.rpc.core.protocol.RpcConstants;
import com.light.rpc.core.protocol.RpcDecoder;
import com.light.rpc.core.protocol.RpcEncoder;
import com.light.rpc.core.protocol.RpcMessage;
import com.light.rpc.core.serialize.Serializer;
import com.light.rpc.core.serialize.impl.JdkSerializer;
import com.light.rpc.core.serialize.impl.JsonSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClientProxy {
    private final ServiceRegistry registry;

    public RpcClientProxy(ServiceRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ClientInvocationHandler(registry, interfaceClass));
    }

    private static class ClientInvocationHandler implements InvocationHandler {
        private final ServiceRegistry registry;
        private final Class<?> interfaceClass;
        private final Serializer serializer = new JsonSerializer(); // 后续可以切换为 JSON
        private final RpcEncoder encoder = new RpcEncoder(serializer);
        private final RpcDecoder decoder = new RpcDecoder(serializer);

        public ClientInvocationHandler(ServiceRegistry registry, Class<?> interfaceClass) {
            this.registry = registry;
            this.interfaceClass = interfaceClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //1.构造 RpcRequest
            RpcRequest request = new RpcRequest();
            request.setMethodName(method.getName());
            request.setArgs(args);
            request.setInterfaceName(method.getDeclaringClass().getName());
            request.setParamTypes(method.getParameterTypes());

            // 2. 构造 RpcMessage
            RpcMessage message = new RpcMessage();
            message.setMessageType(RpcConstants.REQUEST_TYPE);
            message.setRequestId(System.currentTimeMillis()); // 简单唯一 ID
            message.setData(request);

            // 3. 编码
            byte[] bytes = encoder.encode(message);

            InetSocketAddress address = registry.lookup(interfaceClass.getName());

            // 4. 建立 Socket 连接并发送
            try (
                    Socket socket = new Socket(address.getHostName(), address.getPort());
                    OutputStream out = socket.getOutputStream();
                    InputStream in = socket.getInputStream();
            ) {
                out.write(bytes);
                out.flush();

                // 5. 接收并解码响应
                RpcMessage responseMsg = decoder.decode(in);
                RpcResponse response = (RpcResponse) responseMsg.getData();

                if (response.isSuccess()) {
                    return response.getResult();
                } else {
                    throw new RuntimeException("远程调用失败：" + response.getMessage());
                }
            }
        }
    }
}
