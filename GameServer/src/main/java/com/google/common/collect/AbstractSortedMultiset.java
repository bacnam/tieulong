package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

@GwtCompatible
abstract class AbstractSortedMultiset<E>
        extends AbstractMultiset<E>
        implements SortedMultiset<E> {
    final Comparator<? super E> comparator;
    private transient SortedMultiset<E> descendingMultiset;

    AbstractSortedMultiset(Comparator<? super E> comparator) {
        this.comparator = (Comparator<? super E>) Preconditions.checkNotNull(comparator);
    }

    public SortedSet<E> elementSet() {
        return (SortedSet<E>) super.elementSet();
    }

    SortedSet<E> createElementSet() {
        return new SortedMultisets.ElementSet<E>() {
            SortedMultiset<E> multiset() {
                return AbstractSortedMultiset.this;
            }
        };
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    public Multiset.Entry<E> firstEntry() {
        Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
        return entryIterator.hasNext() ? entryIterator.next() : null;
    }

    public Multiset.Entry<E> lastEntry() {
        Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
        return entryIterator.hasNext() ? entryIterator.next() : null;
    }

    public Multiset.Entry<E> pollFirstEntry() {
        Iterator<Multiset.Entry<E>> entryIterator = entryIterator();
        if (entryIterator.hasNext()) {
            Multiset.Entry<E> result = entryIterator.next();
            result = Multisets.immutableEntry(result.getElement(), result.getCount());
            entryIterator.remove();
            return result;
        }
        return null;
    }

    public Multiset.Entry<E> pollLastEntry() {
        Iterator<Multiset.Entry<E>> entryIterator = descendingEntryIterator();
        if (entryIterator.hasNext()) {
            Multiset.Entry<E> result = entryIterator.next();
            result = Multisets.immutableEntry(result.getElement(), result.getCount());
            entryIterator.remove();
            return result;
        }
        return null;
    }

    public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) {
        return tailMultiset(fromElement, fromBoundType).headMultiset(toElement, toBoundType);
    }

    Iterator<E> descendingIterator() {
        return Multisets.iteratorImpl(descendingMultiset());
    }

    public SortedMultiset<E> descendingMultiset() {
        SortedMultiset<E> result = this.descendingMultiset;
        return (result == null) ? (this.descendingMultiset = createDescendingMultiset()) : result;
    }

    SortedMultiset<E> createDescendingMultiset() {
        return new SortedMultisets.DescendingMultiset<E>() {
            SortedMultiset<E> forwardMultiset() {
                return AbstractSortedMultiset.this;
            }

            Iterator<Multiset.Entry<E>> entryIterator() {
                return AbstractSortedMultiset.this.descendingEntryIterator();
            }

            public Iterator<E> iterator() {
                return AbstractSortedMultiset.this.descendingIterator();
            }
        };
    }

    abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();
}

