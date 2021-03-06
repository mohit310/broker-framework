package com.mk.framework.context;

import com.mk.framework.data.IBrokerObject;

public class ApplicationBrokerObject implements IBrokerObject {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApplicationBrokerObject{" +
                "data='" + data + '\'' +
                '}';
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public IBrokerObject deserialize(byte[] data) {
        return null;
    }
}
