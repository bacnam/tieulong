package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
interface BstBalancePolicy<N extends BstNode<?, N>> {
  N balance(BstNodeFactory<N> paramBstNodeFactory, N paramN1, @Nullable N paramN2, @Nullable N paramN3);

  @Nullable
  N combine(BstNodeFactory<N> paramBstNodeFactory, @Nullable N paramN1, @Nullable N paramN2);
}

