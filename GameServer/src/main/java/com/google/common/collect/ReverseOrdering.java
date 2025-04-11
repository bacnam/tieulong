package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ReverseOrdering<T>
        extends Ordering<T>
        implements Serializable {
    private static final long serialVersionUID = 0L;
    final Ordering<? super T> forwardOrder;

    ReverseOrdering(Ordering<? super T> forwardOrder) {
        this.forwardOrder = (Ordering<? super T>) Preconditions.checkNotNull(forwardOrder);
    }

    public int compare(T a, T b) {
        return this.forwardOrder.compare(b, a);
    }

    public <S extends T> Ordering<S> reverse() {
        return (Ordering) this.forwardOrder;
    }

    public <E extends T> E min(E a, E b) {
        return this.forwardOrder.max(a, b);
    }

    public <E extends T> E min(E a, E b, E c, E... rest) {
        return this.forwardOrder.max(a, b, c, rest);
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return this.forwardOrder.max(iterable);
    }

    public <E extends T> E max(E a, E b) {
        return this.forwardOrder.min(a, b);
    }

    public <E extends T> E max(E a, E b, E c, E... rest) {
        return this.forwardOrder.min(a, b, c, rest);
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return this.forwardOrder.min(iterable);
    }

    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ReverseOrdering) {
            ReverseOrdering<?> that = (ReverseOrdering) object;
            return this.forwardOrder.equals(that.forwardOrder);
        }
        return false;
    }

    public String toString() {
        return this.forwardOrder + ".reverse()";
    }
}

