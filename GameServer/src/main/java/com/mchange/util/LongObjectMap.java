package com.mchange.util;

public interface LongObjectMap {
  Object get(long paramLong);

  void put(long paramLong, Object paramObject);

  boolean putNoReplace(long paramLong, Object paramObject);

  Object remove(long paramLong);

  boolean containsLong(long paramLong);

  long getSize();
}

