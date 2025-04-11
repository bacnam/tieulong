package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;

@Beta
@GwtCompatible
public abstract class AbstractLinkedIterator<T>
        extends UnmodifiableIterator<T> {
    private T nextOrNull;

    protected AbstractLinkedIterator(@Nullable T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }

    protected abstract T computeNext(T paramT);

    public final boolean hasNext() {
        return (this.nextOrNull != null);
    }

    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return this.nextOrNull;
        } finally {
            this.nextOrNull = computeNext(this.nextOrNull);
        }
    }
}

