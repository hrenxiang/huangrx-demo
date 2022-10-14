package com.huangrx.cloud.gateway.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

/**
 * @author hrenxiang
 * @since 2022-10-14 17:19:24
 */
public class DataBufferWrapper {
    private byte[] data;
    private DataBufferFactory factory;

    public DataBufferWrapper() {
    }

    public DataBufferWrapper(byte[] data, DataBufferFactory factory) {
        this.data = data;
        this.factory = factory;
    }

    public byte[] getData() {
        return data;
    }

    public DataBufferFactory getFactory() {
        return factory;
    }

    public DataBuffer newDataBuffer() {
        if (factory == null) {
            return null;
        }

        return factory.wrap(data);
    }

    public Boolean clear() {
        data = null;
        factory = null;

        return Boolean.TRUE;
    }
}