package javolution.util.internal.collection;

import javolution.util.FastSet;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

import java.util.Iterator;

public class DistinctCollectionImpl<E>
        extends CollectionView<E> {
    private static final long serialVersionUID = 1536L;

    public DistinctCollectionImpl(CollectionService<E> target) {
        super(target);
    }

    public boolean add(E element) {
        if (target().contains(element)) return false;
        return target().add(element);
    }

    public void clear() {
        target().clear();
    }

    public Equality<? super E> comparator() {
        return target().comparator();
    }

    public boolean contains(Object o) {
        return target().contains(o);
    }

    public boolean isEmpty() {
        return target().isEmpty();
    }

    public Iterator<E> iterator() {
        return new IteratorImpl();
    }

    public boolean remove(Object o) {
        boolean changed = false;
        while (true) {
            if (!remove(o)) return changed;
            changed = true;
        }
    }

    private class IteratorImpl
            implements Iterator<E> {
        private final FastSet<E> iterated = new FastSet(DistinctCollectionImpl.this.comparator());
        private final Iterator<E> targetIterator = DistinctCollectionImpl.this.target().iterator();
        private boolean ahead;
        private E next;

        private IteratorImpl() {
        }

        public boolean hasNext() {
            if (this.ahead) return true;
            while (this.targetIterator.hasNext()) {
                this.next = this.targetIterator.next();
                if (!this.iterated.contains(this.next)) {
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

