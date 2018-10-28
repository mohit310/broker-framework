package com.mk.framework.example;

import com.mk.framework.data.IBrokerObject;
import com.mk.framework.base.AbstractComponent;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.IContext;

public class Pong extends AbstractComponent {

    public void subscribe(IContext context) {
        this.context = context;
        context.addSubscriber("ping", new ICallback() {
            public void receive(IBrokerObject data) {
                PingData pingDataObject = (PingData) data;
                System.out.println("RECEIVED ping: " + pingDataObject.getData());
            }
        });
    }

    public void start() {
        System.out.println("Starting pong");
        for (int i = 0; i < 10; ++i) {
            PongData pongData = new PongData();
            pongData.setData("pong" + i);
            this.context.send("pong", pongData);

            try {
                Thread.sleep(200L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }

    }

    public void stop() {
        System.out.println("Stopping pong");
    }
}
