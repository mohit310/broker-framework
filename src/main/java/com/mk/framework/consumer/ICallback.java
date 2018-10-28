package com.mk.framework.consumer;

import com.mk.framework.data.IBrokerObject;

public interface ICallback<T> {
    <T extends IBrokerObject> void receive(T data);
}
