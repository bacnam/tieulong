package com.mchange.util;

import com.mchange.io.IOByteArrayEnumeration;
import com.mchange.io.IOByteArrayMap;

public interface ByteArrayMap extends IOByteArrayMap {
  byte[] get(byte[] paramArrayOfbyte);
  
  void put(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  boolean putNoReplace(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  boolean remove(byte[] paramArrayOfbyte);
  
  boolean containsKey(byte[] paramArrayOfbyte);
  
  IOByteArrayEnumeration keys();
  
  ByteArrayEnumeration mkeys();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/ByteArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */