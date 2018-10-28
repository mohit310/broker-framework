package com.mk.framework.consumer;

import com.mk.framework.IBrokerObject;

public interface ICallback<T> {
    <T extends IBrokerObject> void receive(T data);
}
