package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;

@GwtCompatible
abstract class BstNodeFactory<N extends BstNode<?, N>> {
    public abstract N createNode(N paramN1, @Nullable N paramN2, @Nullable N paramN3);

    public final N createLeaf(N source) {
        return createNode(source, null, null);
    }
}

