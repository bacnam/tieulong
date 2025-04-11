package org.apache.http.nio.util;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class SharedOutputBuffer
        extends ExpandableBuffer
        implements ContentOutputBuffer {
    private final ReentrantLock lock;
    private final Condition condition;
    private volatile IOControl ioctrl;
    private volatile boolean shutdown = false;
    private volatile boolean endOfStream = false;

    @Deprecated
    public SharedOutputBuffer(int buffersize, IOControl ioctrl, ByteBufferAllocator allocator) {
        super(buffersize, allocator);
        Args.notNull(ioctrl, "I/O content control");
        this.ioctrl = ioctrl;
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public SharedOutputBuffer(int buffersize, ByteBufferAllocator allocator) {
        super(buffersize, allocator);
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public SharedOutputBuffer(int buffersize) {
        this(buffersize, HeapByteBufferAllocator.INSTANCE);
    }

    public void reset() {
        if (this.shutdown) {
            return;
        }
        this.lock.lock();
        try {
            clear();
            this.endOfStream = false;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean hasData() {
        this.lock.lock();
        try {
            return super.hasData();
        } finally {
            this.lock.unlock();
        }
    }

    public int available() {
        this.lock.lock();
        try {
            return super.available();
        } finally {
            this.lock.unlock();
        }
    }

    public int capacity() {
        this.lock.lock();
        try {
            return super.capacity();
        } finally {
            this.lock.unlock();
        }
    }

    public int length() {
        this.lock.lock();
        try {
            return super.length();
        } finally {
            this.lock.unlock();
        }
    }

    @Deprecated
    public int produceContent(ContentEncoder encoder) throws IOException {
        return produceContent(encoder, (IOControl) null);
    }

    public int produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        if (this.shutdown) {
            return -1;
        }
        this.lock.lock();
        try {
            if (ioctrl != null) {
                this.ioctrl = ioctrl;
            }
            setOutputMode();
            int bytesWritten = 0;
            if (super.hasData()) {
                bytesWritten = encoder.write(this.buffer);
                if (encoder.isCompleted()) {
                    this.endOfStream = true;
                }
            }
            if (!super.hasData()) {

                if (this.endOfStream && !encoder.isCompleted()) {
                    encoder.complete();
                }
                if (!this.endOfStream) {
                    if (this.ioctrl != null) {
                        this.ioctrl.suspendOutput();
                    }
                }
            }
            this.condition.signalAll();
            return bytesWritten;
        } finally {
            this.lock.unlock();
        }
    }

    public void close() {
        shutdown();
    }

    public void shutdown() {
        if (this.shutdown) {
            return;
        }
        this.shutdown = true;
        this.lock.lock();
        try {
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            return;
        }
        int pos = off;
        this.lock.lock();
        try {
            Asserts.check((!this.shutdown && !this.endOfStream), "Buffer already closed for writing");
            setInputMode();
            int remaining = len;
            while (remaining > 0) {
                if (!this.buffer.hasRemaining()) {
                    flushContent();
                    setInputMode();
                }
                int chunk = Math.min(remaining, this.buffer.remaining());
                this.buffer.put(b, pos, chunk);
                remaining -= chunk;
                pos += chunk;
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void write(byte[] b) throws IOException {
        if (b == null) {
            return;
        }
        write(b, 0, b.length);
    }

    public void write(int b) throws IOException {
        this.lock.lock();
        try {
            Asserts.check((!this.shutdown && !this.endOfStream), "Buffer already closed for writing");
            setInputMode();
            if (!this.buffer.hasRemaining()) {
                flushContent();
                setInputMode();
            }
            this.buffer.put((byte) b);
        } finally {
            this.lock.unlock();
        }
    }

    public void flush() throws IOException {
    }

    private void flushContent() throws IOException {
        this.lock.lock();
        try {
            while (true) {
                try {
                    if (super.hasData()) {
                        if (this.shutdown) {
                            throw new InterruptedIOException("Output operation aborted");
                        }
                        if (this.ioctrl != null) {
                            this.ioctrl.requestOutput();
                        }
                        this.condition.await();
                        continue;
                    }
                } catch (InterruptedException ex) {
                    throw new IOException("Interrupted while flushing the content buffer");
                }
                break;
            }
        } finally {
            this.lock.unlock();
        }

    }

    public void writeCompleted() throws IOException {
        this.lock.lock();
        try {
            if (this.endOfStream) {
                return;
            }
            this.endOfStream = true;
            if (this.ioctrl != null) {
                this.ioctrl.requestOutput();
            }
        } finally {
            this.lock.unlock();
        }
    }
}

