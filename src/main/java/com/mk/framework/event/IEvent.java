package com.mk.framework.event;

import java.util.Map;

public interface IEvent<T> {
    String getId();

    long getTimestamp();

    void setHeader(Map<String, Object> var1);

    Map<String, Object> getHeader();

    void setBody(IPayload<T> var1);

    IPayload<T> getBody();
}
