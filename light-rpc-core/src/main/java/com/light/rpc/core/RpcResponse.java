package com.light.rpc.core;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object result;
    private String message;
    private boolean success;

    public RpcResponse() {}

    public RpcResponse(Object result, String message, boolean success) {
        this.result = result;
        this.message = message;
        this.success = success;
    }

    public static RpcResponse success(Object result) {
        return new RpcResponse(result, "调用成功", true);
    }
    public static RpcResponse fail(String message) {
        return new RpcResponse(null, message, false);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
