package com.mk.framework.event;

import com.mk.framework.IBrokerObject;

public class DefaultPayLoad implements IPayload {
    private IBrokerObject data;

    @Override
    public void setData(IBrokerObject data) {
        this.data = data;
    }

    @Override
    public IBrokerObject getData() {
        return this.data;
    }
}
