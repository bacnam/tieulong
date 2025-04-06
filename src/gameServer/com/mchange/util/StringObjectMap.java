package com.mchange.util;

import com.mchange.io.IOStringEnumeration;
import com.mchange.io.IOStringObjectMap;

public interface StringObjectMap extends IOStringObjectMap {
  Object get(String paramString);
  
  void put(String paramString, Object paramObject);
  
  boolean putNoReplace(String paramString, Object paramObject);
  
  boolean remove(String paramString);
  
  boolean containsKey(String paramString);
  
  IOStringEnumeration keys();
  
  StringEnumeration mkeys();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/StringObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */