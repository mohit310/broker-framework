package com.mk.framework.context;

import com.mk.framework.base.IComponent;
import com.mk.framework.broker.DefaultBroker;
import com.mk.framework.broker.IBroker;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.event.DefaultEvent;
import com.mk.framework.event.DefaultPayLoad;
import com.mk.framework.event.IEvent;
import com.mk.framework.event.IPayload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ApplicationContext implements IContext {
    private final IBroker broker;
    private final Map<String, List<ICallback>> subscribers = new ConcurrentHashMap();
    private List<IComponent> components = new ArrayList();

    public ApplicationContext(List<IComponent> components) {
        this.components.addAll(components);
        this.broker = new DefaultBroker();
        this.initializeComponents();
    }

    public void start() {
        send("start", "starting components");
    }

    public void stop() {
        send("stop", "stopping components");
        broker.stop();
    }

    private void initializeComponents() {
        System.out.println("init");
        for (IComponent component : components) {
            component.init(this);
        }

    }

    public void send(String messageTopic, Object data) {
        IEvent<IPayload> event = this.createEvent(messageTopic, data);
        broker.publish(event, this.subscribers);
    }

    private IEvent<IPayload> createEvent(String messageTopic, Object data) {
        IPayload payload = new DefaultPayLoad();
        payload.setData(data);
        IEvent<IPayload> event = new DefaultEvent(messageTopic);
        Map<String, Object> eventHeaders = new HashMap();
        eventHeaders.put("TIMESTAMP", LocalDateTime.now());
        event.setHeader(eventHeaders);
        event.setBody(payload);
        return event;
    }

    public void addSubscriber(String id, ICallback callback) {
        List<ICallback> callbacks = (List) this.subscribers.get(id);
        if (callbacks == null) {
            callbacks = new CopyOnWriteArrayList();
            subscribers.put(id, callbacks);
        }

        if (!((List) callbacks).contains(callback)) {
            ((List) callbacks).add(callback);
        }

    }
}
