package org.apache.thrift;

import java.io.ByteArrayOutputStream;

public class TByteArrayOutputStream extends ByteArrayOutputStream {
    public TByteArrayOutputStream(int size) {
        super(size);
    }

    public TByteArrayOutputStream() {
        super();
    }

    public byte[] get() {
        return buf;
    }

    public int len() {
        return count;
    }
}
