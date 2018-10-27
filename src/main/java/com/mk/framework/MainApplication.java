package com.mk.framework;

import com.mk.framework.base.IComponent;
import com.mk.framework.context.ApplicationContext;
import com.mk.framework.context.IContext;
import com.mk.framework.example.Ping;
import com.mk.framework.example.Pong;

import java.util.ArrayList;
import java.util.List;

public class MainApplication {
    public MainApplication() {
    }

    public static void main(String[] args) {
        List<IComponent> components = new ArrayList();
        Ping ping = new Ping();
        Pong pong = new Pong();
        components.add(ping);
        components.add(pong);
        IContext context = new ApplicationContext(components);
        context.start();

        try {
            Thread.sleep(4000L);
        } catch (InterruptedException var6) {
            var6.printStackTrace();
        }

        context.stop();
    }
}
