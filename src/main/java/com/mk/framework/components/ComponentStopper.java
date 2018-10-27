
package com.mk.framework.components;

import com.mk.framework.base.IComponent;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.IContext;

import java.util.Iterator;
import java.util.List;

public class ComponentStopper implements IComponent {
    public ComponentStopper() {
    }

    public void init(IContext context) {
        context.addSubscriber("stop", new ICallback() {
            public void receive(Object data) {
                if (data instanceof List) {
                    List<IComponent> components = (List) data;
                    Iterator var3 = components.iterator();

                    while (var3.hasNext()) {
                        IComponent component = (IComponent) var3.next();
                        System.out.println("Stopping:" + component);
                        component.stop();
                    }
                }

            }
        });
    }

    public void start() {
    }

    public void stop() {
    }
}
