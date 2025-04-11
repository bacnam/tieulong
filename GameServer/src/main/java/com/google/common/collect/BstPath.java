package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

@GwtCompatible
abstract class BstPath<N extends BstNode<?, N>, P extends BstPath<N, P>> {
    private final N tip;
    @Nullable
    private final P prefix;

    BstPath(N tip, @Nullable P prefix) {
        this.tip = (N) Preconditions.checkNotNull(tip);
        this.prefix = prefix;
    }

    public final N getTip() {
        return this.tip;
    }

    public final boolean hasPrefix() {
        return (this.prefix != null);
    }

    @Nullable
    public final P prefixOrNull() {
        return this.prefix;
    }

    public final P getPrefix() {
        Preconditions.checkState(hasPrefix());
        return this.prefix;
    }
}

