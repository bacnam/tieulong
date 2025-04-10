package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public interface Constraint<E> {
  E checkElement(E paramE);

  String toString();
}

