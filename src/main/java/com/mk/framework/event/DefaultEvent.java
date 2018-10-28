package com.mk.framework.event;

import java.util.HashMap;
import java.util.Map;

public class DefaultEvent<T> implements IEvent<T> {
    private String id;
    private long timestamp;
    private Map<String, Object> headers = new HashMap();
    private IPayload<T> payload;

    public DefaultEvent() {
    }

    public DefaultEvent(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setHeader(Map<String, Object> header) {
        this.headers.putAll(header);
    }

    public Map<String, Object> getHeader() {
        return this.headers;
    }

    public void setBody(IPayload<T> payload) {
        this.payload = payload;
    }

    public IPayload<T> getBody() {
        return this.payload;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
