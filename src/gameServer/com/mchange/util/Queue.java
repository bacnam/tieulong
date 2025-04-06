package com.mchange.util;

import java.util.NoSuchElementException;

public interface Queue extends Cloneable {
  void enqueue(Object paramObject);
  
  Object dequeue() throws NoSuchElementException;
  
  Object peek() throws NoSuchElementException;
  
  boolean hasMoreElements();
  
  int size();
  
  Object clone();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/Queue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */