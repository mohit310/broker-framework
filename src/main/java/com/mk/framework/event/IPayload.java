
package com.mk.framework.event;

import com.mk.framework.data.IBrokerObject;

import java.io.Serializable;

public interface IPayload<T> extends Serializable {
    <T extends IBrokerObject> void setData(T data);

    <T extends IBrokerObject> T getData();
}
