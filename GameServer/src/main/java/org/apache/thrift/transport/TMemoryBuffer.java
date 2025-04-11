package org.apache.thrift.transport;

import org.apache.thrift.TByteArrayOutputStream;

import java.io.UnsupportedEncodingException;

public class TMemoryBuffer extends TTransport {

    private TByteArrayOutputStream arr_;
    private int pos_;

    public TMemoryBuffer(int size) {
        arr_ = new TByteArrayOutputStream(size);
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public int read(byte[] buf, int off, int len) {
        byte[] src = arr_.get();
        int amtToRead = (len > arr_.len() - pos_ ? arr_.len() - pos_ : len);
        if (amtToRead > 0) {
            System.arraycopy(src, pos_, buf, off, amtToRead);
            pos_ += amtToRead;
        }
        return amtToRead;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        arr_.write(buf, off, len);
    }

    public String toString(String enc) throws UnsupportedEncodingException {
        return arr_.toString(enc);
    }

    public String inspect() {
        String buf = "";
        byte[] bytes = arr_.toByteArray();
        for (int i = 0; i < bytes.length; i++) {
            buf += (pos_ == i ? "==>" : "") + Integer.toHexString(bytes[i] & 0xff) + " ";
        }
        return buf;
    }

    public int length() {
        return arr_.size();
    }

    public byte[] getArray() {
        return arr_.get();
    }
}

