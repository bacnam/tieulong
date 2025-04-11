package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.List;

@GwtCompatible
abstract class TransformedImmutableList<D, E>
        extends ImmutableList<E> {
    private final transient ImmutableList<D> backingList;

    TransformedImmutableList(ImmutableList<D> backingList) {
        this.backingList = (ImmutableList<D>) Preconditions.checkNotNull(backingList);
    }

    public int indexOf(@Nullable Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = 0; i < size(); i++) {
            if (get(i).equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(@Nullable Object object) {
        if (object == null) {
            return -1;
        }
        for (int i = size() - 1; i >= 0; i--) {
            if (get(i).equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public E get(int index) {
        return transform(this.backingList.get(index));
    }

    public UnmodifiableListIterator<E> listIterator(int index) {
        return new AbstractIndexedListIterator<E>(size(), index) {
            protected E get(int index) {
                return (E) TransformedImmutableList.this.get(index);
            }
        };
    }

    public int size() {
        return this.backingList.size();
    }

    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        return new TransformedView(this.backingList.subList(fromIndex, toIndex));
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof List) {
            List<?> list = (List) obj;
            return (size() == list.size() && Iterators.elementsEqual(iterator(), list.iterator()));
        }

        return false;
    }

    public int hashCode() {
        int hashCode = 1;
        for (E e : this) {
            hashCode = 31 * hashCode + ((e == null) ? 0 : e.hashCode());
        }
        return hashCode;
    }

    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] array) {
        return ObjectArrays.toArrayImpl(this, array);
    }

    boolean isPartialView() {
        return true;
    }

    abstract E transform(D paramD);

    private class TransformedView
            extends TransformedImmutableList<D, E> {
        TransformedView(ImmutableList<D> backingList) {
            super(backingList);
        }

        E transform(D d) {
            return TransformedImmutableList.this.transform(d);
        }
    }
}

