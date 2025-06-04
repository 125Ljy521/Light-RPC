package com.light.rpc.core.protocol;

import com.light.rpc.core.serialize.Serializer;

import java.nio.ByteBuffer;

public class RpcEncoder {
    private final Serializer serializer;

    public RpcEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    public byte[] encode(RpcMessage message) throws Exception {
        byte[] bodyBytes = serializer.serialize(message.getData());

        // 分配足够大小的 ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(4 + 1 + 1 + 8 + 4 + bodyBytes.length);

        //写入头部字段
        buffer.putInt(RpcConstants.MAGIC_NUMBER);
        buffer.put(RpcConstants.VERSION);
        buffer.put(message.getMessageType());
        buffer.putLong(message.getRequestId());
        buffer.putInt(bodyBytes.length);

        //写入正文
        buffer.put(bodyBytes);

        //输出为 byte[]
        return buffer.array();
    }
}
