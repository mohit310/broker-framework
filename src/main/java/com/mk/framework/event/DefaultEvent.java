package com.mk.framework.event;

public class DefaultEvent<T> implements IEvent<T> {
    private String id;
    private long timestamp;
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

    @Override
    public String toString() {
        return "DefaultEvent{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", payload=" + payload +
                '}';
    }
}
