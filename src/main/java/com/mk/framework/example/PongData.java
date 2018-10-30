package com.mk.framework.example;

import com.mk.framework.data.IBrokerObject;

public class PongData implements IBrokerObject {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PongData{" +
                "data='" + data + '\'' +
                '}';
    }

    @Override
    public byte[] serialize() {
        Example.PongData.Builder newBuilder = Example.PongData.newBuilder();
        newBuilder.setData(data);
        return newBuilder.build().toByteArray();
    }

    @Override
    public IBrokerObject deserialize(byte[] data) {
        try {
            Example.PongData pongData = Example.PongData.parseFrom(data);
            PongData pongDataJava = new PongData();
            pongDataJava.setData(pongData.getData());
            return pongDataJava;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
