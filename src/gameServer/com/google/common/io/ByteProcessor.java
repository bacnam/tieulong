package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
public interface ByteProcessor<T> {
  boolean processBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  T getResult();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/io/ByteProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */