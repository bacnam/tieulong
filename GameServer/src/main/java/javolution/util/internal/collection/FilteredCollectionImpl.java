package javolution.util.internal.collection;

import javolution.util.function.Equality;
import javolution.util.function.Predicate;
import javolution.util.service.CollectionService;

import java.util.Iterator;

public class FilteredCollectionImpl<E>
        extends CollectionView<E> {
    private static final long serialVersionUID = 1536L;
    protected final Predicate<? super E> filter;

    public FilteredCollectionImpl(CollectionService<E> target, Predicate<? super E> filter) {
        super(target);
        this.filter = filter;
    }

    public boolean add(E element) {
        if (!this.filter.test(element)) return false;
        return target().add(element);
    }

    public Equality<? super E> comparator() {
        return target().comparator();
    }

    public boolean contains(Object o) {
        if (!this.filter.test(o)) return false;
        return target().contains(o);
    }

    public Iterator<E> iterator() {
        return new IteratorImpl(this.filter);
    }

    public boolean remove(Object o) {
        if (!this.filter.test(o)) return false;
        return target().remove(o);
    }

    private class IteratorImpl
            implements Iterator<E> {
        private final Predicate<? super E> filter;
        private final Iterator<E> targetIterator;
        private boolean ahead;
        private E next;

        public IteratorImpl(Predicate<? super E> filter) {
            this.filter = filter;
            this.targetIterator = FilteredCollectionImpl.this.target().iterator();
        }

        public boolean hasNext() {
            if (this.ahead) return true;
            while (this.targetIterator.hasNext()) {
                this.next = this.targetIterator.next();
                if (this.filter.test(this.next)) {
                    this.ahead = true;
                    return true;
                }
            }
            return false;
        }

        public E next() {
            hasNext();
            this.ahead = false;
            return this.next;
        }

        public void remove() {
            this.targetIterator.remove();
        }
    }
}

