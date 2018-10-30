package com.mk.framework.data;

public interface IBrokerObject {

    byte[] serialize();

    IBrokerObject deserialize(byte[] data);

}
