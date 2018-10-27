//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mk.framework.context;

import com.mk.framework.consumer.ICallback;

public interface IContext<V> {
    void send(String var1, V var2);

    void addSubscriber(String var1, ICallback<V> var2);

    void start();

    void stop();
}
