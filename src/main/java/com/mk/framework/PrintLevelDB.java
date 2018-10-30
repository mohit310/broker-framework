package com.mk.framework;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mk.framework.context.ApplicationProto;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

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
                ApplicationProto.ApplicationData applicationData = getAppDataObject(value);
                System.out.println(ByteString.copyFrom(key).toString(Charset.defaultCharset()) + ":\n" + applicationData);
                iterator.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (levelDB != null)
                try {
                    levelDB.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    private static ApplicationProto.ApplicationData getAppDataObject(byte[] value) {
        try {
            ApplicationProto.ApplicationData applicationData = ApplicationProto.ApplicationData.parseFrom(value);
            return applicationData;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
