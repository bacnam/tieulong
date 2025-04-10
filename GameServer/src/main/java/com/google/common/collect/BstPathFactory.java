package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface BstPathFactory<N extends BstNode<?, N>, P extends BstPath<N, P>> {
  P extension(P paramP, BstSide paramBstSide);

  P initialPath(N paramN);
}

