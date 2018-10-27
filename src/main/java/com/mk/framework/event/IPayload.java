
package com.mk.framework.event;

public interface IPayload<T> {
    void setData(T var1);

    T getData();
}
