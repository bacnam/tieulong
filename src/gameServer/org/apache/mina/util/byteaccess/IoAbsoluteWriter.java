package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public interface IoAbsoluteWriter {
  int first();
  
  int last();
  
  ByteOrder order();
  
  void put(int paramInt, byte paramByte);
  
  void put(int paramInt, IoBuffer paramIoBuffer);
  
  void putShort(int paramInt, short paramShort);
  
  void putInt(int paramInt1, int paramInt2);
  
  void putLong(int paramInt, long paramLong);
  
  void putFloat(int paramInt, float paramFloat);
  
  void putDouble(int paramInt, double paramDouble);
  
  void putChar(int paramInt, char paramChar);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/IoAbsoluteWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */