package org.apache.http.nio.util;

import org.apache.http.annotation.Immutable;

import java.nio.ByteBuffer;

@Immutable
public class DirectByteBufferAllocator
        implements ByteBufferAllocator {
    public static final DirectByteBufferAllocator INSTANCE = new DirectByteBufferAllocator();

    public ByteBuffer allocate(int size) {
        return ByteBuffer.allocateDirect(size);
    }
}

