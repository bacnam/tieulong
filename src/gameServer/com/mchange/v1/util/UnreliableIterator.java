package com.mchange.v1.util;

public interface UnreliableIterator extends UIterator {
  boolean hasNext() throws UnreliableIteratorException;
  
  Object next() throws UnreliableIteratorException;
  
  void remove() throws UnreliableIteratorException;
  
  void close() throws UnreliableIteratorException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/UnreliableIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */