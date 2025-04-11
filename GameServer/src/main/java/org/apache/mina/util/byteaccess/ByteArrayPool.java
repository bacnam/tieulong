package org.apache.mina.util.byteaccess;

import org.apache.mina.core.buffer.IoBuffer;

import java.util.ArrayList;
import java.util.Stack;

public class ByteArrayPool
        implements ByteArrayFactory {
    private final int MAX_BITS = 32;
    private final boolean direct;
    private final int maxFreeBuffers;
    private final int maxFreeMemory;
    private boolean freed;
    private ArrayList<Stack<DirectBufferByteArray>> freeBuffers;
    private int freeBufferCount = 0;
    private long freeMemory = 0L;

    public ByteArrayPool(boolean direct, int maxFreeBuffers, int maxFreeMemory) {
        this.direct = direct;
        this.freeBuffers = new ArrayList<Stack<DirectBufferByteArray>>();
        for (int i = 0; i < 32; i++) {
            this.freeBuffers.add(new Stack<DirectBufferByteArray>());
        }
        this.maxFreeBuffers = maxFreeBuffers;
        this.maxFreeMemory = maxFreeMemory;
        this.freed = false;
    }

    public ByteArray create(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Buffer size must be at least 1: " + size);
        }
        int bits = bits(size);
        synchronized (this) {
            if (!((Stack) this.freeBuffers.get(bits)).isEmpty()) {
                DirectBufferByteArray directBufferByteArray = ((Stack<DirectBufferByteArray>) this.freeBuffers.get(bits)).pop();
                directBufferByteArray.setFreed(false);
                directBufferByteArray.getSingleIoBuffer().limit(size);
                return directBufferByteArray;
            }
        }

        int bbSize = 1 << bits;
        IoBuffer bb = IoBuffer.allocate(bbSize, this.direct);
        bb.limit(size);
        DirectBufferByteArray ba = new DirectBufferByteArray(bb);
        ba.setFreed(false);
        return ba;
    }

    private int bits(int index) {
        int bits = 0;
        while (1 << bits < index) {
            bits++;
        }
        return bits;
    }

    public void free() {
        synchronized (this) {
            if (this.freed) {
                throw new IllegalStateException("Already freed.");
            }
            this.freed = true;
            this.freeBuffers.clear();
            this.freeBuffers = null;
        }
    }

    private class DirectBufferByteArray
            extends BufferByteArray {
        private boolean freed;

        public DirectBufferByteArray(IoBuffer bb) {
            super(bb);
        }

        public void setFreed(boolean freed) {
            this.freed = freed;
        }

        public void free() {
            synchronized (this) {
                if (this.freed) {
                    throw new IllegalStateException("Already freed.");
                }
                this.freed = true;
            }
            int bits = ByteArrayPool.this.bits(last());
            synchronized (ByteArrayPool.this) {
                if (ByteArrayPool.this.freeBuffers != null && ByteArrayPool.this.freeBufferCount < ByteArrayPool.this.maxFreeBuffers && ByteArrayPool.this.freeMemory + last() <= ByteArrayPool.this.maxFreeMemory) {
                    ((Stack<DirectBufferByteArray>) ByteArrayPool.this.freeBuffers.get(bits)).push(this);
                    ByteArrayPool.this.freeBufferCount++;
                    ByteArrayPool.this.freeMemory += last();
                    return;
                }
            }
        }
    }
}

