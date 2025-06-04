package com.light.rpc.core.serialize.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.rpc.core.serialize.Serializer;

public class JsonSerializer implements Serializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return objectMapper.readValue(bytes,clazz);
    }
}
