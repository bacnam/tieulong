package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public interface Constraint<E> {
  E checkElement(E paramE);
  
  String toString();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/Constraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */