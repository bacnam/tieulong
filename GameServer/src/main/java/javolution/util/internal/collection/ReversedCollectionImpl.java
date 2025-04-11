package javolution.util.internal.collection;

import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

import java.util.Iterator;

public class ReversedCollectionImpl<E>
        extends CollectionView<E> {
    private static final long serialVersionUID = 1536L;

    public ReversedCollectionImpl(CollectionService<E> target) {
        super(target);
    }

    public boolean add(E e) {
        return target().add(e);
    }

    public void clear() {
        target().clear();
    }

    public Equality<? super E> comparator() {
        return target().comparator();
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
        private final E[] elements = (E[]) new Object[ReversedCollectionImpl.this.size()];

        private int index = 0;

        public IteratorImpl() {
            Iterator<E> it = ReversedCollectionImpl.this.target().iterator();
            while (it.hasNext() && this.index < this.elements.length) {
                this.elements[this.index++] = it.next();
            }
        }

        public boolean hasNext() {
            return (this.index > 0);
        }

        public E next() {
            return this.elements[--this.index];
        }

        public void remove() {
            ReversedCollectionImpl.this.target().remove(this.elements[this.index]);
        }
    }
}

