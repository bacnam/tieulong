package javolution.util.internal.set.sorted;

import javolution.util.function.Equality;
import javolution.util.service.SortedSetService;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SubSortedSetImpl<E>
        extends SortedSetView<E> {
    private static final long serialVersionUID = 1536L;
    private final E from;
    private final E to;

    public SubSortedSetImpl(SortedSetService<E> target, E from, E to) {
        super(target);
        if (from != null && to != null && comparator().compare(from, to) > 0) {
            throw new IllegalArgumentException("from: " + from + ", to: " + to);
        }
        this.from = from;
        this.to = to;
    }

    public boolean add(E e) {
        Equality<? super E> cmp = comparator();
        if (this.from != null && cmp.compare(e, this.from) < 0)
            throw new IllegalArgumentException("Element: " + e + " outside of this sub-set bounds");

        if (this.to != null && cmp.compare(e, this.to) >= 0)
            throw new IllegalArgumentException("Element: " + e + " outside of this sub-set bounds");

        return target().add(e);
    }

    public Equality<? super E> comparator() {
        return target().comparator();
    }

    public boolean contains(Object obj) {
        Equality<? super E> cmp = comparator();
        if (this.from != null && cmp.compare(obj, this.from) < 0) return false;
        if (this.to != null && cmp.compare(obj, this.to) >= 0) return false;
        return target().contains(obj);
    }

    public E first() {
        if (this.from == null) return (E) target().first();
        Iterator<E> it = iterator();
        if (!it.hasNext()) throw new NoSuchElementException();
        return it.next();
    }

    public boolean isEmpty() {
        return iterator().hasNext();
    }

    public Iterator<E> iterator() {
        return new IteratorImpl();
    }

    public E last() {
        if (this.to == null) return (E) target().last();
        Iterator<E> it = iterator();
        if (!it.hasNext()) throw new NoSuchElementException();
        E last = it.next();
        while (it.hasNext()) {
            last = it.next();
        }
        return last;
    }

    public boolean remove(Object obj) {
        Equality<? super E> cmp = comparator();
        if (this.from != null && cmp.compare(obj, this.from) < 0) return false;
        if (this.to != null && cmp.compare(obj, this.to) >= 0) return false;
        return target().remove(obj);
    }

    public int size() {
        int count = 0;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            count++;
            it.next();
        }
        return count;
    }

    private class IteratorImpl
            implements Iterator<E> {
        private final Equality<? super E> cmp = SubSortedSetImpl.this.comparator();
        private final Iterator<E> targetIterator = SubSortedSetImpl.this.target().iterator();
        private boolean ahead;
        private E next;

        private IteratorImpl() {
        }

        public boolean hasNext() {
            if (this.ahead) return true;
            while (this.targetIterator.hasNext()) {
                this.next = this.targetIterator.next();
                if (SubSortedSetImpl.this.from != null && this.cmp.compare(this.next, SubSortedSetImpl.this.from) < 0)
                    continue;
                if (SubSortedSetImpl.this.to != null && this.cmp.compare(this.next, SubSortedSetImpl.this.to) >= 0)
                    break;
                this.ahead = true;
                return true;
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

