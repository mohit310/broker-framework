package com.mk.framework.broker;

import com.mk.framework.consumer.ICallback;
import com.mk.framework.event.IEvent;
import java.util.List;
import java.util.Map;

public interface IBroker<T> {
    <T extends IEvent> void publish(T var1, Map<String, List<ICallback>> var2);

    void stop();
}
