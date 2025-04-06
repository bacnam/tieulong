package com.mchange.io;

import java.io.IOException;

public interface IOStringEnumeration extends IOEnumeration {
  boolean hasMoreStrings() throws IOException;
  
  String nextString() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOStringEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */