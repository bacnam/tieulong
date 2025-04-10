package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
interface BstModifier<K, N extends BstNode<K, N>> {
  BstModificationResult<N> modify(K paramK, @Nullable N paramN);
}

