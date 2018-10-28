package com.mk.framework.broker;

import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.ApplicationBrokerObject;
import com.mk.framework.event.IEvent;
import com.mk.framework.event.IPayload;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class DefaultBroker implements IBroker<IEvent<IPayload>> {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private DB levelDB;

    public DefaultBroker() {
        Options options = new Options().cacheSize(100_000).createIfMissing(true).maxOpenFiles(1024).blockSize(4092).writeBufferSize(4092);
        try {
            levelDB = factory.open(new File("broker-messages"), options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends IEvent> void publish(T event, Map<String, List<ICallback>> subscribers) {
        if (!(event.getBody().getData() instanceof ApplicationBrokerObject)) {
            String id = String.valueOf(System.currentTimeMillis());
            levelDB.put(id.getBytes(), getBytes(event));
        }
        sendToSubscribers(event, subscribers);
    }

    private <T extends IEvent> void sendToSubscribers(T event, Map<String, List<ICallback>> subscribers) {
        List<ICallback> callbacks = (List) subscribers.get(event.getId());
        if (callbacks != null) {
            callbacks.stream().forEach((c) -> {
                DefaultBroker.NotifyThread t = new DefaultBroker.NotifyThread(c, event);
                this.executorService.execute(t);
            });
        }
    }

    private byte[] getBytes(IEvent event) {
        byte[] dataBytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(event);
            out.flush();
            dataBytes = bos.toByteArray();
            return dataBytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return dataBytes;
    }

    public void stop() {
        try {
            levelDB.close();
            executorService.shutdown();
            boolean exited = executorService.awaitTermination(2, TimeUnit.SECONDS);
            if (!exited) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
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

    @Override
    public void replay(Map<String, List<ICallback>> subscribers) {
        DBIterator iterator = levelDB.iterator();
        while (iterator.hasNext()) {
            byte[] key = iterator.peekNext().getKey();
            byte[] value = iterator.peekNext().getValue();
            IEvent data = (IEvent) getJavaObject(value);
            System.out.println(new String(key) + ":" + data);
            sendToSubscribers(data, subscribers);
            iterator.next();
        }
    }

    private Object getJavaObject(byte[] serializedData) {
        Object o = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return o;
    }
}
