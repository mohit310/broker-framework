//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mk.framework.event;

import java.util.HashMap;
import java.util.Map;

public class DefaultEvent<T> implements IEvent<T> {
    private String id;
    private Map<String, Object> headers = new HashMap();
    private IPayload<T> payload;

    public DefaultEvent(String id) {
        this.id = id;
    }

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
}
