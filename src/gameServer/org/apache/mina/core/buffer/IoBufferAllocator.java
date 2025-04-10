package org.apache.mina.core.buffer;

import java.nio.ByteBuffer;

public interface IoBufferAllocator {
  IoBuffer allocate(int paramInt, boolean paramBoolean);

  ByteBuffer allocateNioBuffer(int paramInt, boolean paramBoolean);

  IoBuffer wrap(ByteBuffer paramByteBuffer);

  void dispose();
}

