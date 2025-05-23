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

