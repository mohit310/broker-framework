package com.mk.framework.example;

import com.mk.framework.data.IBrokerObject;

public class PingData implements IBrokerObject {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PingData{" +
                "data='" + data + '\'' +
                '}';
    }

    @Override
    public byte[] serialize() {
        Example.PingData.Builder newBuilder = Example.PingData.newBuilder();
        newBuilder.setData(data);
        return newBuilder.build().toByteArray();
    }

    @Override
    public IBrokerObject deserialize(byte[] data) {
        try {
            Example.PingData pingData = Example.PingData.parseFrom(data);
            PingData pingDataJava = new PingData();
            pingDataJava.setData(pingData.getData());
            return pingDataJava;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
