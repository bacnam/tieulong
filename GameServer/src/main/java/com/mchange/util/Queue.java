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

