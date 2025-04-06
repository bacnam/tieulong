package com.mchange.io;

import java.io.IOException;

public interface IOEnumeration {
  boolean hasMoreElements() throws IOException;
  
  Object nextElement() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */