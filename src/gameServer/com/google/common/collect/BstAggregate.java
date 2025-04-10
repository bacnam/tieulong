package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
interface BstAggregate<N extends BstNode<?, N>> {
  int treeValue(@Nullable N paramN);

  int entryValue(N paramN);
}

