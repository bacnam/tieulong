package com.mchange.io;

import java.io.IOException;

public interface IOStringObjectMap {
  Object get(String paramString) throws IOException;
  
  void put(String paramString, Object paramObject) throws IOException;
  
  boolean putNoReplace(String paramString, Object paramObject) throws IOException;
  
  boolean remove(String paramString) throws IOException;
  
  boolean containsKey(String paramString) throws IOException;
  
  IOStringEnumeration keys() throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/IOStringObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */