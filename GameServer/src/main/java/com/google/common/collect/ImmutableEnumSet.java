package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableEnumSet<E extends Enum<E>>
        extends ImmutableSet<E> {
    private final transient EnumSet<E> delegate;
    private transient int hashCode;

    ImmutableEnumSet(EnumSet<E> delegate) {
        this.delegate = delegate;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean contains(Object object) {
        return this.delegate.contains(object);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.delegate.containsAll(collection);
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public Object[] toArray() {
        return this.delegate.toArray();
    }

    public <T> T[] toArray(T[] array) {
        return (T[]) this.delegate.toArray((Object[]) array);
    }

    public boolean equals(Object object) {
        return (object == this || this.delegate.equals(object));
    }

    public int hashCode() {
        int result = this.hashCode;
        return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
    }

    public String toString() {
        return this.delegate.toString();
    }

    Object writeReplace() {
        return new EnumSerializedForm<E>(this.delegate);
    }

    private static class EnumSerializedForm<E extends Enum<E>>
            implements Serializable {
        private static final long serialVersionUID = 0L;
        final EnumSet<E> delegate;

        EnumSerializedForm(EnumSet<E> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumSet<Enum>(this.delegate.clone());
        }
    }
}

