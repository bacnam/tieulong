package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

@GwtCompatible
public final class Multisets {
    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
        if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset) {

            return (Multiset) multiset;
        }

        return new UnmodifiableMultiset<E>((Multiset<? extends E>) Preconditions.checkNotNull(multiset));
    }

    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
        return (Multiset<E>) Preconditions.checkNotNull(multiset);
    }

    public static <E> Multiset.Entry<E> immutableEntry(@Nullable E e, int n) {
        return new ImmutableEntry<E>(e, n);
    }

    static <E> Multiset<E> forSet(Set<E> set) {
        return new SetMultiset<E>(set);
    }

    static int inferDistinctElements(Iterable<?> elements) {
        if (elements instanceof Multiset) {
            return ((Multiset) elements).elementSet().size();
        }
        return 11;
    }

    public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset1);
        Preconditions.checkNotNull(multiset2);

        return new AbstractMultiset<E>() {
            public int count(Object element) {
                int count1 = multiset1.count(element);
                return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
            }

            Set<E> createElementSet() {
                return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
            }

            Iterator<Multiset.Entry<E>> entryIterator() {
                final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
                return new AbstractIterator() {
                    protected Multiset.Entry<E> computeNext() {
                        while (iterator1.hasNext()) {
                            Multiset.Entry<E> entry1 = iterator1.next();
                            E element = entry1.getElement();
                            int count = Math.min(entry1.getCount(), multiset2.count(element));
                            if (count > 0) {
                                return Multisets.immutableEntry(element, count);
                            }
                        }
                        return endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    @Beta
    public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
        Preconditions.checkNotNull(superMultiset);
        Preconditions.checkNotNull(subMultiset);
        for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
            int superCount = superMultiset.count(entry.getElement());
            if (superCount < entry.getCount()) {
                return false;
            }
        }
        return true;
    }

    @Beta
    public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
        return retainOccurrencesImpl(multisetToModify, multisetToRetain);
    }

    private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
        Preconditions.checkNotNull(multisetToModify);
        Preconditions.checkNotNull(occurrencesToRetain);

        Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            Multiset.Entry<E> entry = entryIterator.next();
            int retainCount = occurrencesToRetain.count(entry.getElement());
            if (retainCount == 0) {
                entryIterator.remove();
                changed = true;
                continue;
            }
            if (retainCount < entry.getCount()) {
                multisetToModify.setCount(entry.getElement(), retainCount);
                changed = true;
            }
        }
        return changed;
    }

    @Beta
    public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
        return removeOccurrencesImpl(multisetToModify, occurrencesToRemove);
    }

    private static <E> boolean removeOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRemove) {
        Preconditions.checkNotNull(multisetToModify);
        Preconditions.checkNotNull(occurrencesToRemove);

        boolean changed = false;
        Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Multiset.Entry<E> entry = entryIterator.next();
            int removeCount = occurrencesToRemove.count(entry.getElement());
            if (removeCount >= entry.getCount()) {
                entryIterator.remove();
                changed = true;
                continue;
            }
            if (removeCount > 0) {
                multisetToModify.remove(entry.getElement(), removeCount);
                changed = true;
            }
        }
        return changed;
    }

    static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
        if (object == multiset) {
            return true;
        }
        if (object instanceof Multiset) {
            Multiset<?> that = (Multiset) object;

            if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
                return false;
            }
            for (Multiset.Entry<?> entry : that.entrySet()) {
                if (multiset.count(entry.getElement()) != entry.getCount()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
        if (elements.isEmpty()) {
            return false;
        }
        if (elements instanceof Multiset) {
            Multiset<? extends E> that = cast(elements);
            for (Multiset.Entry<? extends E> entry : that.entrySet()) {
                self.add(entry.getElement(), entry.getCount());
            }
        } else {
            Iterators.addAll(self, elements.iterator());
        }
        return true;
    }

    static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
        Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset) elementsToRemove).elementSet() : elementsToRemove;

        return self.elementSet().removeAll(collection);
    }

    static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
        Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset) elementsToRetain).elementSet() : elementsToRetain;

        return self.elementSet().retainAll(collection);
    }

    static <E> int setCountImpl(Multiset<E> self, E element, int count) {
        checkNonnegative(count, "count");

        int oldCount = self.count(element);

        int delta = count - oldCount;
        if (delta > 0) {
            self.add(element, delta);
        } else if (delta < 0) {
            self.remove(element, -delta);
        }

        return oldCount;
    }

    static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
        checkNonnegative(oldCount, "oldCount");
        checkNonnegative(newCount, "newCount");

        if (self.count(element) == oldCount) {
            self.setCount(element, newCount);
            return true;
        }
        return false;
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
        return new MultisetIteratorImpl<E>(multiset, multiset.entrySet().iterator());
    }

    static int sizeImpl(Multiset<?> multiset) {
        long size = 0L;
        for (Multiset.Entry<?> entry : multiset.entrySet()) {
            size += entry.getCount();
        }
        return Ints.saturatedCast(size);
    }

    static void checkNonnegative(int count, String name) {
        Preconditions.checkArgument((count >= 0), "%s cannot be negative: %s", new Object[]{name, Integer.valueOf(count)});
    }

    static <T> Multiset<T> cast(Iterable<T> iterable) {
        return (Multiset<T>) iterable;
    }

    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0L;
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;
        UnmodifiableMultiset(Multiset<? extends E> delegate) {
            this.delegate = delegate;
        }

        protected Multiset<E> delegate() {
            return (Multiset) this.delegate;
        }

        Set<E> createElementSet() {
            return Collections.unmodifiableSet(this.delegate.elementSet());
        }

        public Set<E> elementSet() {
            Set<E> es = this.elementSet;
            return (es == null) ? (this.elementSet = createElementSet()) : es;
        }

        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> es = this.entrySet;
            return (es == null) ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
        }

        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator((Iterator) this.delegate.iterator());
        }

        public boolean add(E element) {
            throw new UnsupportedOperationException();
        }

        public int add(E element, int occurences) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> elementsToAdd) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object element) {
            throw new UnsupportedOperationException();
        }

        public int remove(Object element, int occurrences) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> elementsToRemove) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> elementsToRetain) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public int setCount(E element, int count) {
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E element, int oldCount, int newCount) {
            throw new UnsupportedOperationException();
        }
    }

    static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        private static final long serialVersionUID = 0L;
        @Nullable
        final E element;
        final int count;

        ImmutableEntry(@Nullable E element, int count) {
            this.element = element;
            this.count = count;
            Preconditions.checkArgument((count >= 0));
        }

        @Nullable
        public E getElement() {
            return this.element;
        }

        public int getCount() {
            return this.count;
        }
    }

    private static class SetMultiset<E> extends ForwardingCollection<E> implements Multiset<E>, Serializable {
        private static final long serialVersionUID = 0L;
        final Set<E> delegate;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;

        SetMultiset(Set<E> set) {
            this.delegate = (Set<E>) Preconditions.checkNotNull(set);
        }

        protected Set<E> delegate() {
            return this.delegate;
        }

        public int count(Object element) {
            return this.delegate.contains(element) ? 1 : 0;
        }

        public int add(E element, int occurrences) {
            throw new UnsupportedOperationException();
        }

        public int remove(Object element, int occurrences) {
            if (occurrences == 0) {
                return count(element);
            }
            Preconditions.checkArgument((occurrences > 0));
            return this.delegate.remove(element) ? 1 : 0;
        }

        public Set<E> elementSet() {
            Set<E> es = this.elementSet;
            return (es == null) ? (this.elementSet = new ElementSet()) : es;
        }

        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> es = this.entrySet;
            if (es == null) {
                es = this.entrySet = new Multisets.EntrySet<E>() {
                    Multiset<E> multiset() {
                        return Multisets.SetMultiset.this;
                    }

                    public Iterator<Multiset.Entry<E>> iterator() {
                        return Iterators.transform(Multisets.SetMultiset.this.delegate.iterator(), new Function<E, Multiset.Entry<E>>() {
                            public Multiset.Entry<E> apply(E elem) {
                                return Multisets.immutableEntry(elem, 1);
                            }
                        });
                    }

                    public int size() {
                        return Multisets.SetMultiset.this.delegate.size();
                    }
                };
            }
            return es;
        }

        public boolean add(E o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public int setCount(E element, int count) {
            Multisets.checkNonnegative(count, "count");

            if (count == count(element))
                return count;
            if (count == 0) {
                remove(element);
                return 1;
            }
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E element, int oldCount, int newCount) {
            return Multisets.setCountImpl(this, element, oldCount, newCount);
        }

        public boolean equals(@Nullable Object object) {
            if (object instanceof Multiset) {
                Multiset<?> that = (Multiset) object;
                return (size() == that.size() && this.delegate.equals(that.elementSet()));
            }
            return false;
        }

        public int hashCode() {
            int sum = 0;
            for (E e : this) {
                sum += ((e == null) ? 0 : e.hashCode()) ^ 0x1;
            }
            return sum;
        }

        class ElementSet
                extends ForwardingSet<E> {
            protected Set<E> delegate() {
                return Multisets.SetMultiset.this.delegate;
            }

            public boolean add(E o) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> c) {
                throw new UnsupportedOperationException();
            }
        }
    }

    static abstract class AbstractEntry<E>
            implements Multiset.Entry<E> {
        public boolean equals(@Nullable Object object) {
            if (object instanceof Multiset.Entry) {
                Multiset.Entry<?> that = (Multiset.Entry) object;
                return (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement()));
            }

            return false;
        }

        public int hashCode() {
            E e = getElement();
            return ((e == null) ? 0 : e.hashCode()) ^ getCount();
        }

        public String toString() {
            String text = String.valueOf(getElement());
            int n = getCount();
            return (n == 1) ? text : (text + " x " + n);
        }
    }

    static abstract class ElementSet<E>
            extends AbstractSet<E> {
        abstract Multiset<E> multiset();

        public void clear() {
            multiset().clear();
        }

        public boolean contains(Object o) {
            return multiset().contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return multiset().containsAll(c);
        }

        public boolean isEmpty() {
            return multiset().isEmpty();
        }

        public Iterator<E> iterator() {
            return Iterators.transform(multiset().entrySet().iterator(), new Function<Multiset.Entry<E>, E>() {
                public E apply(Multiset.Entry<E> entry) {
                    return entry.getElement();
                }
            });
        }

        public boolean remove(Object o) {
            int count = multiset().count(o);
            if (count > 0) {
                multiset().remove(o, count);
                return true;
            }
            return false;
        }

        public int size() {
            return multiset().entrySet().size();
        }
    }

    static abstract class EntrySet<E> extends AbstractSet<Multiset.Entry<E>> {
        abstract Multiset<E> multiset();

        public boolean contains(@Nullable Object o) {
            if (o instanceof Multiset.Entry) {

                Multiset.Entry<?> entry = (Multiset.Entry) o;
                if (entry.getCount() <= 0) {
                    return false;
                }
                int count = multiset().count(entry.getElement());
                return (count == entry.getCount());
            }

            return false;
        }

        public boolean remove(Object o) {
            return (contains(o) && multiset().elementSet().remove(((Multiset.Entry) o).getElement()));
        }

        public void clear() {
            multiset().clear();
        }
    }

    static final class MultisetIteratorImpl<E>
            implements Iterator<E> {
        private final Multiset<E> multiset;

        private final Iterator<Multiset.Entry<E>> entryIterator;
        private Multiset.Entry<E> currentEntry;
        private int laterCount;
        private int totalCount;
        private boolean canRemove;

        MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
            this.multiset = multiset;
            this.entryIterator = entryIterator;
        }

        public boolean hasNext() {
            return (this.laterCount > 0 || this.entryIterator.hasNext());
        }

        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.laterCount == 0) {
                this.currentEntry = this.entryIterator.next();
                this.totalCount = this.laterCount = this.currentEntry.getCount();
            }
            this.laterCount--;
            this.canRemove = true;
            return this.currentEntry.getElement();
        }

        public void remove() {
            Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");

            if (this.totalCount == 1) {
                this.entryIterator.remove();
            } else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            this.totalCount--;
            this.canRemove = false;
        }
    }
}

