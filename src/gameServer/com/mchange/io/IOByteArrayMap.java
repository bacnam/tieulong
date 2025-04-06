package com.mchange.io;

import java.io.IOException;

public interface IOByteArrayMap {
  byte[] get(byte[] paramArrayOfbyte) throws IOException;
  
  void put(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException;
  
  boolean putNoReplace(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws IOException;
  
  boolean remove(byte[] paramArrayOfbyte) throws IOException;
  
  boolean containsKey(byte[] paramArrayOfbyte) throws IOException;
  
  IOByteArrayEnumeration keys() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOByteArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */