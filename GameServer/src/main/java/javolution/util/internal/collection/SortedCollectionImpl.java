package javolution.util.internal.collection;

import javolution.util.FastTable;
import javolution.util.function.Equality;
import javolution.util.internal.comparator.WrapperComparatorImpl;
import javolution.util.service.CollectionService;

import java.util.Comparator;
import java.util.Iterator;

public class SortedCollectionImpl<E>
        extends CollectionView<E> {
    private static final long serialVersionUID = 1536L;
    protected final Equality<E> comparator;

    public SortedCollectionImpl(CollectionService<E> target, Comparator<? super E> comparator) {
        super(target);
        this.comparator = (comparator instanceof Equality) ? (Equality) comparator : (Equality<E>) new WrapperComparatorImpl(comparator);
    }

    public boolean add(E e) {
        return target().add(e);
    }

    public void clear() {
        target().clear();
    }

    public Equality<? super E> comparator() {
        return this.comparator;
    }

    public boolean contains(Object obj) {
        return target().contains(obj);
    }

    public boolean isEmpty() {
        return target().isEmpty();
    }

    public Iterator<E> iterator() {
        return new IteratorImpl();
    }

    public boolean remove(Object obj) {
        return target().remove(obj);
    }

    public int size() {
        return target().size();
    }

    private class IteratorImpl
            implements Iterator<E> {
        private final Iterator<E> iterator;
        private E next;

        public IteratorImpl() {
            FastTable<E> sorted = new FastTable(SortedCollectionImpl.this.comparator);
            Iterator<E> it = SortedCollectionImpl.this.target().iterator();
            while (it.hasNext()) {
                sorted.add(it.next());
            }
            sorted.sort();
            this.iterator = sorted.iterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public E next() {
            this.next = this.iterator.next();
            return this.next;
        }

        public void remove() {
            if (this.next == null) throw new IllegalStateException();
            SortedCollectionImpl.this.target().remove(this.next);
            this.next = null;
        }
    }
}

