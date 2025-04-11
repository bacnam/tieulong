package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingSet<E>
        extends ForwardingCollection<E>
        implements Set<E> {
    public boolean equals(@Nullable Object object) {
        return (object == this || delegate().equals(object));
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    @Beta
    protected boolean standardEquals(@Nullable Object object) {
        return Sets.equalsImpl(this, object);
    }

    @Beta
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }

    protected abstract Set<E> delegate();
}

