package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Set;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableSet<E>
        extends ImmutableSet<E> {
    final transient E element;
    private transient Integer cachedHashCode;

    SingletonImmutableSet(E element) {
        this.element = (E) Preconditions.checkNotNull(element);
    }

    SingletonImmutableSet(E element, int hashCode) {
        this.element = element;
        this.cachedHashCode = Integer.valueOf(hashCode);
    }

    public int size() {
        return 1;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object target) {
        return this.element.equals(target);
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    boolean isPartialView() {
        return false;
    }

    public Object[] toArray() {
        return new Object[]{this.element};
    }

    public <T> T[] toArray(T[] array) {
        if (array.length == 0) {
            array = ObjectArrays.newArray(array, 1);
        } else if (array.length > 1) {
            array[1] = null;
        }

        T[] arrayOfT = array;
        arrayOfT[0] = (T) this.element;
        return array;
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Set) {
            Set<?> that = (Set) object;
            return (that.size() == 1 && this.element.equals(that.iterator().next()));
        }
        return false;
    }

    public final int hashCode() {
        Integer code = this.cachedHashCode;
        if (code == null) {
            return (this.cachedHashCode = Integer.valueOf(this.element.hashCode())).intValue();
        }
        return code.intValue();
    }

    boolean isHashCodeFast() {
        return false;
    }

    public String toString() {
        String elementToString = this.element.toString();
        return (new StringBuilder(elementToString.length() + 2)).append('[').append(elementToString).append(']').toString();
    }
}

