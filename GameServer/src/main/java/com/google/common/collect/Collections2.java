package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.*;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ");

    public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredCollection) {

            return ((FilteredCollection<E>) unfiltered).createCombined(predicate);
        }

        return new FilteredCollection<E>((Collection<E>) Preconditions.checkNotNull(unfiltered), (Predicate<? super E>) Preconditions.checkNotNull(predicate));
    }

    static boolean safeContains(Collection<?> collection, Object object) {
        try {
            return collection.contains(object);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
        return new TransformedCollection<F, T>(fromCollection, function);
    }

    static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
        Preconditions.checkNotNull(self);
        for (Object o : c) {
            if (!self.contains(o)) {
                return false;
            }
        }
        return true;
    }

    static String toStringImpl(final Collection<?> collection) {
        StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');

        STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>() {
            public Object apply(Object input) {
                return (input == collection) ? "(this Collection)" : input;
            }
        }));
        return sb.append(']').toString();
    }

    static StringBuilder newStringBuilderForCollection(int size) {
        Preconditions.checkArgument((size >= 0), "size must be non-negative");
        return new StringBuilder((int) Math.min(size * 8L, 1073741824L));
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection<T>) iterable;
    }

    static class FilteredCollection<E>
            implements Collection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;

        FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
            return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }

        public boolean add(E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }

        public boolean addAll(Collection<? extends E> collection) {
            for (E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll(collection);
        }

        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        public boolean contains(Object element) {
            try {
                E e = (E) element;

                return (this.predicate.apply(e) && this.unfiltered.contains(element));
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object element : collection) {
                if (!contains(element)) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEmpty() {
            return !Iterators.any(this.unfiltered.iterator(), this.predicate);
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        public boolean remove(Object element) {
            try {
                E e = (E) element;

                return (this.predicate.apply(e) && this.unfiltered.remove(element));
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }

        public boolean removeAll(final Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Predicate<E> combinedPredicate = new Predicate<E>() {
                public boolean apply(E input) {
                    return (Collections2.FilteredCollection.this.predicate.apply(input) && collection.contains(input));
                }
            };
            return Iterables.removeIf(this.unfiltered, combinedPredicate);
        }

        public boolean retainAll(final Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Predicate<E> combinedPredicate = new Predicate<E>() {
                public boolean apply(E input) {
                    return (Collections2.FilteredCollection.this.predicate.apply(input) && !collection.contains(input));
                }
            };
            return Iterables.removeIf(this.unfiltered, combinedPredicate);
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public Object[] toArray() {
            return Lists.<E>newArrayList(iterator()).toArray();
        }

        public <T> T[] toArray(T[] array) {
            return (T[]) Lists.<E>newArrayList(iterator()).toArray((Object[]) array);
        }

        public String toString() {
            return Iterators.toString(iterator());
        }
    }

    static class TransformedCollection<F, T>
            extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
            this.fromCollection = (Collection<F>) Preconditions.checkNotNull(fromCollection);
            this.function = (Function<? super F, ? extends T>) Preconditions.checkNotNull(function);
        }

        public void clear() {
            this.fromCollection.clear();
        }

        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        public int size() {
            return this.fromCollection.size();
        }
    }
}

