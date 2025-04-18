package org.apache.http.nio.util;

import java.nio.ByteBuffer;
import org.apache.http.annotation.Immutable;

@Immutable
public class HeapByteBufferAllocator
implements ByteBufferAllocator
{
public static final HeapByteBufferAllocator INSTANCE = new HeapByteBufferAllocator();

public ByteBuffer allocate(int size) {
return ByteBuffer.allocate(size);
}
}

