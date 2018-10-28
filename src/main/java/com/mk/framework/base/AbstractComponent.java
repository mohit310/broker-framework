package com.mk.framework.base;

import com.mk.framework.IBrokerObject;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.IContext;

public abstract class AbstractComponent implements IComponent {
    protected IContext context;

    @Override
    public void init(IContext context) {
        this.context = context;
        context.addSubscriber("start", new ICallback() {
            @Override
            public void receive(IBrokerObject message) {
                AbstractComponent.this.start();
            }
        });
        context.addSubscriber("stop", new ICallback() {
            @Override
            public void receive(IBrokerObject message) {
                AbstractComponent.this.stop();
            }
        });
        subscribe(context);
    }

    public abstract void subscribe(IContext context);
}
