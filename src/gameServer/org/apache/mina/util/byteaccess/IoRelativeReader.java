package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public interface IoRelativeReader {
  int getRemaining();
  
  boolean hasRemaining();
  
  void skip(int paramInt);
  
  ByteArray slice(int paramInt);
  
  ByteOrder order();
  
  byte get();
  
  void get(IoBuffer paramIoBuffer);
  
  short getShort();
  
  int getInt();
  
  long getLong();
  
  float getFloat();
  
  double getDouble();
  
  char getChar();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/IoRelativeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */