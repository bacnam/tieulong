package com.mchange.v1.util;

public interface UnreliableIterator extends UIterator {
  boolean hasNext() throws UnreliableIteratorException;

  Object next() throws UnreliableIteratorException;

  void remove() throws UnreliableIteratorException;

  void close() throws UnreliableIteratorException;
}

