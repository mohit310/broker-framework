package com.mk.framework.example;

import com.mk.framework.IBrokerObject;

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
}
