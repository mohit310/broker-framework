package com.mk.framework.example;

import com.mk.framework.base.IComponent;
import com.mk.framework.context.ApplicationContext;
import com.mk.framework.context.IContext;

import java.util.ArrayList;
import java.util.List;

public class ReplayEventsApplication {
    public static void main(String[] args) {
        List<IComponent> components = new ArrayList();
        Ping ping = new Ping();
        Pong pong = new Pong();
        components.add(ping);
        components.add(pong);
        IContext context = new ApplicationContext(components);
        context.replay();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.stop();
    }
}
