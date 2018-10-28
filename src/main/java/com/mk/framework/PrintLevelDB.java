package com.mk.framework;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.*;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class PrintLevelDB {

    public static void main(String[] args) {
        DB levelDB = null;
        Options options = new Options().cacheSize(100_000).createIfMissing(true).maxOpenFiles(1024).blockSize(4092).writeBufferSize(4092);
        try {
            levelDB = factory.open(new File("broker-messages"), options);
            DBIterator iterator = levelDB.iterator();
            while (iterator.hasNext()) {
                byte[] key = iterator.peekNext().getKey();
                byte[] value = iterator.peekNext().getValue();
                IBrokerObject data = (IBrokerObject) getJavaObject(value);
                System.out.println(new String(key) + ":" + data);
                iterator.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Object getJavaObject(byte[] serializedData) {
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
