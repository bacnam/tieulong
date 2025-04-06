package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public interface IoRelativeWriter {
  int getRemaining();
  
  boolean hasRemaining();
  
  void skip(int paramInt);
  
  ByteOrder order();
  
  void put(byte paramByte);
  
  void put(IoBuffer paramIoBuffer);
  
  void putShort(short paramShort);
  
  void putInt(int paramInt);
  
  void putLong(long paramLong);
  
  void putFloat(float paramFloat);
  
  void putDouble(double paramDouble);
  
  void putChar(char paramChar);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/IoRelativeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */