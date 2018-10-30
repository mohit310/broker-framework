package com.mk.framework.event;

public interface IEvent<T> {

    String getId();

    long getTimestamp();

    void setBody(IPayload<T> var1);

    IPayload<T> getBody();
}
