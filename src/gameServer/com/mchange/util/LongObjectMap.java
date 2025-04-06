package com.mchange.util;

public interface LongObjectMap {
  Object get(long paramLong);
  
  void put(long paramLong, Object paramObject);
  
  boolean putNoReplace(long paramLong, Object paramObject);
  
  Object remove(long paramLong);
  
  boolean containsLong(long paramLong);
  
  long getSize();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/LongObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */