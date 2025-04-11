package org.apache.mina.handler.stream;

import org.apache.mina.core.buffer.IoBuffer;

import java.io.IOException;
import java.io.InputStream;

class IoSessionInputStream
        extends InputStream {
    private final Object mutex = new Object();

    private final IoBuffer buf;

    private volatile boolean closed;

    private volatile boolean released;

    private IOException exception;

    public IoSessionInputStream() {
        this.buf = IoBuffer.allocate(16);
        this.buf.setAutoExpand(true);
        this.buf.limit(0);
    }

    public int available() {
        if (this.released) {
            return 0;
        }

        synchronized (this.mutex) {
            return this.buf.remaining();
        }
    }

    public void close() {
        if (this.closed) {
            return;
        }

        synchronized (this.mutex) {
            this.closed = true;
            releaseBuffer();

            this.mutex.notifyAll();
        }
    }

    public int read() throws IOException {
        synchronized (this.mutex) {
            if (!waitForData()) {
                return -1;
            }

            return this.buf.get() & 0xFF;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        synchronized (this.mutex) {
            int readBytes;
            if (!waitForData()) {
                return -1;
            }

            if (len > this.buf.remaining()) {
                readBytes = this.buf.remaining();
            } else {
                readBytes = len;
            }

            this.buf.get(b, off, readBytes);

            return readBytes;
        }
    }

    private boolean waitForData() throws IOException {
        if (this.released) {
            return false;
        }

        synchronized (this.mutex) {
            while (!this.released && this.buf.remaining() == 0 && this.exception == null) {
                try {
                    this.mutex.wait();
                } catch (InterruptedException e) {
                    IOException ioe = new IOException("Interrupted while waiting for more data");
                    ioe.initCause(e);
                    throw ioe;
                }
            }
        }

        if (this.exception != null) {
            releaseBuffer();
            throw this.exception;
        }

        if (this.closed && this.buf.remaining() == 0) {
            releaseBuffer();

            return false;
        }

        return true;
    }

    private void releaseBuffer() {
        if (this.released) {
            return;
        }

        this.released = true;
    }

    public void write(IoBuffer src) {
        synchronized (this.mutex) {
            if (this.closed) {
                return;
            }

            if (this.buf.hasRemaining()) {
                this.buf.compact();
                this.buf.put(src);
                this.buf.flip();
            } else {
                this.buf.clear();
                this.buf.put(src);
                this.buf.flip();
                this.mutex.notifyAll();
            }
        }
    }

    public void throwException(IOException e) {
        synchronized (this.mutex) {
            if (this.exception == null) {
                this.exception = e;

                this.mutex.notifyAll();
            }
        }
    }
}

