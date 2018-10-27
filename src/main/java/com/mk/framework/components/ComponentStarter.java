
package com.mk.framework.components;

import com.mk.framework.base.IComponent;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.IContext;

public class ComponentStarter implements IComponent {

    public void init(IContext context) {
        context.addSubscriber("start", new ICallback() {
            public void receive(Object data) {
                if (data instanceof IComponent) {
                    IComponent component = (IComponent) data;
                    System.out.println("Starting:" + data);
                    component.start();
                }

            }
        });
    }

    public void start() {
    }

    public void stop() {
    }
}
