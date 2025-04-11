package org.apache.http.nio.util;

import org.apache.http.annotation.Immutable;

import java.nio.ByteBuffer;

@Immutable
public class HeapByteBufferAllocator
        implements ByteBufferAllocator {
    public static final HeapByteBufferAllocator INSTANCE = new HeapByteBufferAllocator();

    public ByteBuffer allocate(int size) {
        return ByteBuffer.allocate(size);
    }
}

