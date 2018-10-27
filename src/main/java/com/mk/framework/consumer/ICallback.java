package com.mk.framework.consumer;

public interface ICallback<T> {
    void receive(T data);
}
