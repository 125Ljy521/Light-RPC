package com.light.rpc.core.protocol;

import com.light.rpc.core.RpcRequest;
import com.light.rpc.core.RpcResponse;
import com.light.rpc.core.serialize.Serializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RpcDecoder {
    private final Serializer serializer;

    public RpcDecoder(Serializer serializer) {
        this.serializer = serializer;
    }

    public RpcMessage decode(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        //读取并验证魔数
        int magic = dis.readInt();
        if(magic != RpcConstants.MAGIC_NUMBER) {
            throw new IOException("非法魔数，非Light-RPC协议包！");
        }

        //读取协议字段
        byte version = dis.readByte();
        byte type = dis.readByte();
        Long requestId = dis.readLong();
        int length = dis.readInt();

        //读取正文
        byte[] dataBytes = new byte[length];
        dis.readFully(dataBytes); //阻塞直到读满 length 字节

        //反序列化正文为请求/响应
        Object data;
        try{
            if(type == RpcConstants.REQUEST_TYPE) {
                data = serializer.deserialize(dataBytes, RpcRequest.class);
            } else if (type == RpcConstants.RESPONSE_TYPE) {
                data = serializer.deserialize(dataBytes, RpcResponse.class);
            } else {
                throw new IOException("未知的消息类型" + type);
            }
        } catch (Exception e) {
            throw new IOException("反序列化失败", e);
        }

        //封装为 RpcMessage
        RpcMessage message = new RpcMessage();
        message.setMessageType(type);
        message.setRequestId(requestId);
        message.setData(data);
        return message;
    }

}
