package com.mchange.util;

import com.mchange.io.IOStringEnumeration;

public interface StringEnumeration extends MEnumeration, IOStringEnumeration {
  boolean hasMoreStrings();
  
  String nextString();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/StringEnumeration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */