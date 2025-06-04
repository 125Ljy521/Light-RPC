package com.light.rpc.core.protocol;

public class RpcMessage {
    private byte messageType; // 请求 or 响应
    private long requestId;
    private Object data; // 实际要传输的对象 ：RpcRequest or PrcResponse

    public RpcMessage() {
    }

    public RpcMessage(byte messageType, long requestId, Object data) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.data = data;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
