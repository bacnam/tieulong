package com.mchange.v1.util;

public interface UIterator extends ClosableResource {
  boolean hasNext() throws Exception;

  Object next() throws Exception;

  void remove() throws Exception;

  void close() throws Exception;
}

