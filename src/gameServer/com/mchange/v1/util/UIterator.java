package com.mchange.v1.util;

public interface UIterator extends ClosableResource {
  boolean hasNext() throws Exception;
  
  Object next() throws Exception;
  
  void remove() throws Exception;
  
  void close() throws Exception;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/UIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */