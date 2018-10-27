//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mk.framework.event;

public class DefaultPayLoad<T> implements IPayload<T> {
    private T data;

    public DefaultPayLoad() {
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}
