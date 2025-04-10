package org.apache.http.nio.util;

import java.nio.ByteBuffer;

public interface ByteBufferAllocator {
  ByteBuffer allocate(int paramInt);
}

