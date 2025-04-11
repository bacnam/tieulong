package org.apache.http.nio.util;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class SharedInputBuffer
        extends ExpandableBuffer
        implements ContentInputBuffer {
    private final ReentrantLock lock;
    private final Condition condition;
    private volatile IOControl ioctrl;
    private volatile boolean shutdown = false;
    private volatile boolean endOfStream = false;

    @Deprecated
    public SharedInputBuffer(int buffersize, IOControl ioctrl, ByteBufferAllocator allocator) {
        super(buffersize, allocator);
        this.ioctrl = ioctrl;
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public SharedInputBuffer(int buffersize, ByteBufferAllocator allocator) {
        super(buffersize, allocator);
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    public SharedInputBuffer(int buffersize) {
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

    @Deprecated
    public int consumeContent(ContentDecoder decoder) throws IOException {
        return consumeContent(decoder, (IOControl) null);
    }

    public int consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        if (this.shutdown) {
            return -1;
        }
        this.lock.lock();
        try {
            if (ioctrl != null) {
                this.ioctrl = ioctrl;
            }
            setInputMode();
            int totalRead = 0;
            int bytesRead;
            while ((bytesRead = decoder.read(this.buffer)) > 0) {
                totalRead += bytesRead;
            }
            if (bytesRead == -1 || decoder.isCompleted()) {
                this.endOfStream = true;
            }
            if (!this.buffer.hasRemaining() &&
                    this.ioctrl != null) {
                this.ioctrl.suspendInput();
            }

            this.condition.signalAll();

            if (totalRead > 0) {
                return totalRead;
            }
            if (this.endOfStream) {
                return -1;
            }
            return 0;
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

    protected void waitForData() throws IOException {
        this.lock.lock();
        try {
            while (true) {
                try {
                    if (!super.hasData() && !this.endOfStream) {
                        if (this.shutdown) {
                            throw new InterruptedIOException("Input operation aborted");
                        }
                        if (this.ioctrl != null) {
                            this.ioctrl.requestInput();
                        }
                        this.condition.await();
                        continue;
                    }
                } catch (InterruptedException ex) {
                    throw new IOException("Interrupted while waiting for more data");
                }
                break;
            }
        } finally {
            this.lock.unlock();
        }

    }

    public void close() {
        if (this.shutdown) {
            return;
        }
        this.endOfStream = true;
        this.lock.lock();
        try {
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
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

    protected boolean isShutdown() {
        return this.shutdown;
    }

    protected boolean isEndOfStream() {
        return (this.shutdown || (!hasData() && this.endOfStream));
    }

    public int read() throws IOException {
        if (this.shutdown) {
            return -1;
        }
        this.lock.lock();
        try {
            if (!hasData()) {
                waitForData();
            }
            if (isEndOfStream()) {
                return -1;
            }
            return this.buffer.get() & 0xFF;
        } finally {
            this.lock.unlock();
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.shutdown) {
            return -1;
        }
        if (b == null) {
            return 0;
        }
        this.lock.lock();
        try {
            if (!hasData()) {
                waitForData();
            }
            if (isEndOfStream()) {
                return -1;
            }
            setOutputMode();
            int chunk = len;
            if (chunk > this.buffer.remaining()) {
                chunk = this.buffer.remaining();
            }
            this.buffer.get(b, off, chunk);
            return chunk;
        } finally {
            this.lock.unlock();
        }
    }

    public int read(byte[] b) throws IOException {
        if (this.shutdown) {
            return -1;
        }
        if (b == null) {
            return 0;
        }
        return read(b, 0, b.length);
    }
}

