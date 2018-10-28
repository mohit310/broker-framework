package com.mk.framework.event;

import java.io.Serializable;

public interface IEvent<T> extends Serializable {

    String getId();

    long getTimestamp();

    void setBody(IPayload<T> var1);

    IPayload<T> getBody();
}
