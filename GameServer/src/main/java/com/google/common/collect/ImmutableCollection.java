package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E>
        implements Collection<E>, Serializable {
    static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection();

    private transient ImmutableList<E> asList;

    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] other) {
        return ObjectArrays.toArrayImpl(this, other);
    }

    public boolean contains(@Nullable Object object) {
        return (object != null && Iterators.contains(iterator(), object));
    }

    public boolean containsAll(Collection<?> targets) {
        return Collections2.containsAllImpl(this, targets);
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public String toString() {
        return Collections2.toStringImpl(this);
    }

    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public final boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public final boolean addAll(Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }

    public final boolean removeAll(Collection<?> oldElements) {
        throw new UnsupportedOperationException();
    }

    public final boolean retainAll(Collection<?> elementsToKeep) {
        throw new UnsupportedOperationException();
    }

    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> list = this.asList;
        return (list == null) ? (this.asList = createAsList()) : list;
    }

    ImmutableList<E> createAsList() {
        switch (size()) {
            case 0:
                return ImmutableList.of();
            case 1:
                return ImmutableList.of(iterator().next());
        }
        return new ImmutableAsList<E>(toArray(), this);
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public abstract UnmodifiableIterator<E> iterator();

    abstract boolean isPartialView();

    private static class EmptyImmutableCollection
            extends ImmutableCollection<Object> {
        private static final Object[] EMPTY_ARRAY = new Object[0];

        private EmptyImmutableCollection() {
        }

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean contains(@Nullable Object object) {
            return false;
        }

        public UnmodifiableIterator<Object> iterator() {
            return Iterators.EMPTY_ITERATOR;
        }

        public Object[] toArray() {
            return EMPTY_ARRAY;
        }

        public <T> T[] toArray(T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }

        ImmutableList<Object> createAsList() {
            return ImmutableList.of();
        }

        boolean isPartialView() {
            return false;
        }
    }

    private static class ArrayImmutableCollection<E>
            extends ImmutableCollection<E> {
        private final E[] elements;

        ArrayImmutableCollection(E[] elements) {
            this.elements = elements;
        }

        public int size() {
            return this.elements.length;
        }

        public boolean isEmpty() {
            return false;
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.forArray(this.elements);
        }

        ImmutableList<E> createAsList() {
            return (this.elements.length == 1) ? new SingletonImmutableList<E>(this.elements[0]) : new RegularImmutableList<E>((Object[]) this.elements);
        }

        boolean isPartialView() {
            return false;
        }
    }

    private static class SerializedForm
            implements Serializable {
        private static final long serialVersionUID = 0L;
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return (this.elements.length == 0) ? ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION : new ImmutableCollection.ArrayImmutableCollection(Platform.clone(this.elements));
        }
    }

    public static abstract class Builder<E> {
        public abstract Builder<E> add(E param1E);

        public Builder<E> add(E... elements) {
            for (E element : elements) {
                add(element);
            }
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> elements) {
            for (E element : elements) {
                add(element);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> elements) {
            while (elements.hasNext()) {
                add(elements.next());
            }
            return this;
        }

        public abstract ImmutableCollection<E> build();
    }
}

