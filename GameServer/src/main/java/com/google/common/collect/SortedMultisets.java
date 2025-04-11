package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import java.util.*;

@GwtCompatible
final class SortedMultisets {
    private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getElement();
    }

    static abstract class ElementSet<E>
            extends Multisets.ElementSet<E>
            implements SortedSet<E> {
        public Comparator<? super E> comparator() {
            return multiset().comparator();
        }

        public SortedSet<E> subSet(E fromElement, E toElement) {
            return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
        }

        public SortedSet<E> headSet(E toElement) {
            return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
        }

        public SortedSet<E> tailSet(E fromElement) {
            return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
        }

        public E first() {
            return SortedMultisets.getElementOrThrow(multiset().firstEntry());
        }

        abstract SortedMultiset<E> multiset();

        public E last() {
            return SortedMultisets.getElementOrThrow(multiset().lastEntry());
        }
    }

    static abstract class DescendingMultiset<E>
            extends ForwardingMultiset<E>
            implements SortedMultiset<E> {
        private transient Comparator<? super E> comparator;

        private transient SortedSet<E> elementSet;
        private transient Set<Multiset.Entry<E>> entrySet;

        public Comparator<? super E> comparator() {
            Comparator<? super E> result = this.comparator;
            if (result == null) {
                return this.comparator = Ordering.from(forwardMultiset().comparator()).<Object>reverse();
            }

            return result;
        }

        public SortedSet<E> elementSet() {
            SortedSet<E> result = this.elementSet;
            if (result == null) {
                return this.elementSet = new SortedMultisets.ElementSet<E>() {
                    SortedMultiset<E> multiset() {
                        return SortedMultisets.DescendingMultiset.this;
                    }
                };
            }
            return result;
        }

        public Multiset.Entry<E> pollFirstEntry() {
            return forwardMultiset().pollLastEntry();
        }

        public Multiset.Entry<E> pollLastEntry() {
            return forwardMultiset().pollFirstEntry();
        }

        public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) {
            return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset();
        }

        public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
            return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset();
        }

        public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) {
            return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset();
        }

        protected Multiset<E> delegate() {
            return forwardMultiset();
        }

        public SortedMultiset<E> descendingMultiset() {
            return forwardMultiset();
        }

        public Multiset.Entry<E> firstEntry() {
            return forwardMultiset().lastEntry();
        }

        public Multiset.Entry<E> lastEntry() {
            return forwardMultiset().firstEntry();
        }

        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> result = this.entrySet;
            return (result == null) ? (this.entrySet = createEntrySet()) : result;
        }

        Set<Multiset.Entry<E>> createEntrySet() {
            return new Multisets.EntrySet<E>() {
                Multiset<E> multiset() {
                    return SortedMultisets.DescendingMultiset.this;
                }

                public Iterator<Multiset.Entry<E>> iterator() {
                    return SortedMultisets.DescendingMultiset.this.entryIterator();
                }

                public int size() {
                    return SortedMultisets.DescendingMultiset.this.forwardMultiset().entrySet().size();
                }
            };
        }

        public Iterator<E> iterator() {
            return Multisets.iteratorImpl(this);
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray((Object[]) array);
        }

        public String toString() {
            return entrySet().toString();
        }

        abstract SortedMultiset<E> forwardMultiset();

        abstract Iterator<Multiset.Entry<E>> entryIterator();
    }
}

