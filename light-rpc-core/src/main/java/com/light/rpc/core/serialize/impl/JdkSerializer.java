package com.light.rpc.core.serialize.impl;

import com.light.rpc.core.serialize.Serializer;

import java.io.*;

public class JdkSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)
        ) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("JDK 序列化失败",e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais)
        ){
            return clazz.cast(ois.readObject());
        } catch (Exception e) {
            throw new RuntimeException("JDK 反序列化失败", e);
        }
    }
}
