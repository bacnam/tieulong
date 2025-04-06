package com.mchange.util;

import com.mchange.io.IOByteArrayEnumeration;

public interface ByteArrayEnumeration extends MEnumeration, IOByteArrayEnumeration {
  byte[] nextBytes();
  
  boolean hasMoreBytes();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/ByteArrayEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */