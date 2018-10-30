package com.mk.framework.broker;

import com.google.protobuf.ByteString;
import com.mk.framework.consumer.ICallback;
import com.mk.framework.context.ApplicationBrokerObject;
import com.mk.framework.context.ApplicationProto;
import com.mk.framework.data.IBrokerObject;
import com.mk.framework.event.DefaultEvent;
import com.mk.framework.event.DefaultPayLoad;
import com.mk.framework.event.IEvent;
import com.mk.framework.event.IPayload;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
            IBrokerObject iBrokerObject = event.getBody().getData();
            Class className = iBrokerObject.getClass();
            byte[] appProto = getApplicationProto(event.getId(), className, iBrokerObject);
            levelDB.put(id.getBytes(), appProto);
        }
        sendToSubscribers(event, subscribers);
    }

    private byte[] getApplicationProto(String id, Class className, IBrokerObject iBrokerObject) {
        try {
            ApplicationProto.ApplicationData.Builder builder = ApplicationProto.ApplicationData.newBuilder();
            builder.setKey(id);
            builder.setClassname(className.getCanonicalName());
            builder.setIbrokerobjectBytes(ByteString.copyFrom(iBrokerObject.serialize()));
            return builder.build().toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            ByteString keyByteString = ByteString.copyFrom(key);
            String keyStr = keyByteString.toString(Charset.defaultCharset());
            byte[] value = iterator.peekNext().getValue();
            IEvent data = (IEvent) getEventFromKS(keyStr, value);
            System.out.println(keyStr + ":" + data);
            sendToSubscribers(data, subscribers);
            iterator.next();
        }
    }

    private Object getEventFromKS(String timeStamp, byte[] value) {
        try {
            ApplicationProto.ApplicationData applicationData = ApplicationProto.ApplicationData.parseFrom(value);
            String id = applicationData.getKey();
            String className = applicationData.getClassname();
            IBrokerObject brokerObjectInstance = (IBrokerObject) Class.forName(className).newInstance();
            IBrokerObject newBrokerObject = (IBrokerObject) brokerObjectInstance.deserialize(applicationData.getIbrokerobjectBytes().toByteArray());
            IEvent event = new DefaultEvent(id, Long.valueOf(timeStamp));
            DefaultPayLoad payLoad = new DefaultPayLoad();
            payLoad.setData(newBrokerObject);
            event.setBody(payLoad);
            return event;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
