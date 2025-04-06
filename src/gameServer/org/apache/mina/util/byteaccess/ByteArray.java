package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public interface ByteArray extends IoAbsoluteReader, IoAbsoluteWriter {
  int first();
  
  int last();
  
  ByteOrder order();
  
  void order(ByteOrder paramByteOrder);
  
  void free();
  
  Iterable<IoBuffer> getIoBuffers();
  
  IoBuffer getSingleIoBuffer();
  
  boolean equals(Object paramObject);
  
  byte get(int paramInt);
  
  void get(int paramInt, IoBuffer paramIoBuffer);
  
  int getInt(int paramInt);
  
  Cursor cursor();
  
  Cursor cursor(int paramInt);
  
  public static interface Cursor extends IoRelativeReader, IoRelativeWriter {
    int getIndex();
    
    void setIndex(int param1Int);
    
    int getRemaining();
    
    boolean hasRemaining();
    
    byte get();
    
    void get(IoBuffer param1IoBuffer);
    
    int getInt();
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/ByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */