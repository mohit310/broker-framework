
package com.mk.framework.context;

import com.mk.framework.consumer.ICallback;
import com.mk.framework.data.IBrokerObject;

public interface IContext<V> {
    <V extends IBrokerObject> void send(String messageId, V ibrokerObject);

    <V extends IBrokerObject> void addSubscriber(String messageId, ICallback<V> callback);

    void start();

    void stop();

    void replay();
}
