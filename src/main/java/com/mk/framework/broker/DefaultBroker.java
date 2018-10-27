package com.mk.framework.broker;

import com.mk.framework.consumer.ICallback;
import com.mk.framework.event.IEvent;
import com.mk.framework.event.IPayload;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultBroker implements IBroker<IEvent<IPayload>> {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public DefaultBroker() {
    }

    public <T extends IEvent> void publish(T event, Map<String, List<ICallback>> subscribers) {
        List<ICallback> callbacks = (List) subscribers.get(event.getId());
        if (callbacks != null) {
            callbacks.stream().forEach((c) -> {
                DefaultBroker.NotifyThread t = new DefaultBroker.NotifyThread(c, event);
                this.executorService.execute(t);
            });
        }

    }

    public void stop() {
        try {
            executorService.shutdown();
            boolean exited = executorService.awaitTermination(2, TimeUnit.SECONDS);
            if (!exited) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class NotifyThread implements Runnable {
        private final ICallback callback;
        private final IEvent event;

        public NotifyThread(ICallback callback, IEvent event) {
            this.callback = callback;
            this.event = event;
        }

        public void run() {
            IPayload payload = this.event.getBody();
            this.callback.receive(payload.getData());
        }
    }
}
