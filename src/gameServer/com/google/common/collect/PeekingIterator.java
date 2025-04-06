package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public interface PeekingIterator<E> extends Iterator<E> {
  E peek();
  
  E next();
  
  void remove();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/PeekingIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */