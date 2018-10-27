package com.mk.framework.base;

import com.mk.framework.context.IContext;

public interface IComponent {
    void init(IContext context);

    void start();

    void stop();
}