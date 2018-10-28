package com.mk.framework.example;

import com.mk.framework.IBrokerObject;
import com.mk.framework.base.AbstractComponent;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.IContext;

public class Ping extends AbstractComponent {

    public Ping() {
    }

    @Override
    public void subscribe(IContext context) {
        context.addSubscriber("pong", new ICallback() {
            public void receive(IBrokerObject dataObject) {
                PongData pongMessage = (PongData) dataObject;
                System.out.println("RECEIVED pong: " + pongMessage.getData());
            }
        });
    }

    public void start() {
        System.out.println("Starting ping");
        for (int i = 0; i < 10; ++i) {
            PingData dataObject = new PingData();
            dataObject.setData("ping" + i);
            this.context.send("ping", dataObject);

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }

    }

    public void stop() {
        System.out.println("Stopping ping");
    }
}
