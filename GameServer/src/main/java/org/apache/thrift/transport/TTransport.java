package org.apache.thrift.transport;

public abstract class TTransport {

    public abstract boolean isOpen();

    public boolean peek() {
        return isOpen();
    }

    public abstract void open()
            throws TTransportException;

    public abstract void close();

    public abstract int read(byte[] buf, int off, int len)
            throws TTransportException;

    public int readAll(byte[] buf, int off, int len)
            throws TTransportException {
        int got = 0;
        int ret = 0;
        while (got < len) {
            ret = read(buf, off + got, len - got);
            if (ret <= 0) {
                throw new TTransportException("Cannot read. Remote side has closed. Tried to read " + len + " bytes, but only got " + got + " bytes.");
            }
            got += ret;
        }
        return got;
    }

    public void write(byte[] buf) throws TTransportException {
        write(buf, 0, buf.length);
    }

    public abstract void write(byte[] buf, int off, int len)
            throws TTransportException;

    public void flush()
            throws TTransportException {
    }

    public byte[] getBuffer() {
        return null;
    }

    public int getBufferPosition() {
        return 0;
    }

    public int getBytesRemainingInBuffer() {
        return -1;
    }

    public void consumeBuffer(int len) {
    }
}
