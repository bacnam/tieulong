package com.mchange.util;

public interface IntObjectMap {
  Object get(int paramInt);

  void put(int paramInt, Object paramObject);

  boolean putNoReplace(int paramInt, Object paramObject);

  Object remove(int paramInt);

  boolean containsInt(int paramInt);

  int getSize();

  void clear();

  IntEnumeration ints();
}

