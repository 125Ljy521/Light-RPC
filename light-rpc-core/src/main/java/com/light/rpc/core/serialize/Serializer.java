package com.light.rpc.core.serialize;

public interface Serializer {
    <T> byte[] serialize(T obj) throws Exception;
    <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;
}
