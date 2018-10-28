
package com.mk.framework.event;

import com.mk.framework.IBrokerObject;

public interface IPayload<T> {
    <T extends IBrokerObject> void setData(T data);

    <T extends IBrokerObject> T getData();
}
