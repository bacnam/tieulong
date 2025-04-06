package com.mchange.io;

import java.io.IOException;

public interface IOByteArrayEnumeration extends IOEnumeration {
  byte[] nextBytes() throws IOException;
  
  boolean hasMoreBytes() throws IOException;
  
  Object nextElement() throws IOException;
  
  boolean hasMoreElements() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOByteArrayEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */