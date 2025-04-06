package org.apache.mina.core.buffer;

import java.nio.ByteBuffer;

public interface IoBufferAllocator {
  IoBuffer allocate(int paramInt, boolean paramBoolean);
  
  ByteBuffer allocateNioBuffer(int paramInt, boolean paramBoolean);
  
  IoBuffer wrap(ByteBuffer paramByteBuffer);
  
  void dispose();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/IoBufferAllocator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */