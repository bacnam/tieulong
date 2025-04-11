package javolution.util.internal.table;

import javolution.util.internal.collection.SharedCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.TableService;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class SharedTableImpl<E>
        extends SharedCollectionImpl<E>
        implements TableService<E> {
    private static final long serialVersionUID = 1536L;

    public SharedTableImpl(TableService<E> target) {
        super((CollectionService) target);
    }

    public void add(int index, E element) {
        this.lock.writeLock.lock();
        try {
            target().add(index, element);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        this.lock.writeLock.lock();
        try {
            return target().addAll(index, c);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public void addFirst(E element) {
        this.lock.writeLock.lock();
        try {
            target().addFirst(element);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public void addLast(E element) {
        this.lock.writeLock.lock();
        try {
            target().addLast(element);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public Iterator<E> descendingIterator() {
        return (new ReversedTableImpl<E>(this)).iterator();
    }

    public E element() {
        return getFirst();
    }

    public E get(int index) {
        this.lock.readLock.lock();
        try {
            return (E) target().get(index);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public E getFirst() {
        this.lock.readLock.lock();
        try {
            return (E) target().getFirst();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public E getLast() {
        this.lock.readLock.lock();
        try {
            return (E) target().getLast();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public int indexOf(Object element) {
        this.lock.readLock.lock();
        try {
            return target().indexOf(element);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public ListIterator<E> iterator() {
        return target().listIterator(0);
    }

    public int lastIndexOf(Object element) {
        this.lock.readLock.lock();
        try {
            return target().lastIndexOf(element);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public ListIterator<E> listIterator() {
        return target().listIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        return new TableIteratorImpl<E>(this, index);
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public boolean offerFirst(E e) {
        this.lock.writeLock.lock();
        try {
            return target().offerFirst(e);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean offerLast(E e) {
        this.lock.writeLock.lock();
        try {
            return target().offerLast(e);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        this.lock.readLock.lock();
        try {
            return (E) target().peekFirst();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public E peekLast() {
        this.lock.readLock.lock();
        try {
            return (E) target().peekLast();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        this.lock.writeLock.lock();
        try {
            return (E) target().pollFirst();
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E pollLast() {
        this.lock.writeLock.lock();
        try {
            return (E) target().pollLast();
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E pop() {
        return removeFirst();
    }

    public void push(E e) {
        addFirst(e);
    }

    public E remove() {
        return removeFirst();
    }

    public E remove(int index) {
        this.lock.writeLock.lock();
        try {
            return (E) target().remove(index);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E removeFirst() {
        this.lock.writeLock.lock();
        try {
            return (E) target().removeFirst();
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean removeFirstOccurrence(Object o) {
        this.lock.writeLock.lock();
        try {
            return target().removeFirstOccurrence(o);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E removeLast() {
        this.lock.writeLock.lock();
        try {
            return (E) target().removeLast();
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean removeLastOccurrence(Object o) {
        this.lock.writeLock.lock();
        try {
            return target().removeLastOccurrence(o);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public E set(int index, E element) {
        this.lock.writeLock.lock();
        try {
            return (E) target().set(index, element);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public CollectionService<E>[] split(int n) {
        return SubTableImpl.splitOf(this, n);
    }

    public TableService<E> subList(int fromIndex, int toIndex) {
        return new SubTableImpl<E>(this, fromIndex, toIndex);
    }

    public TableService<E> threadSafe() {
        return this;
    }

    protected TableService<E> target() {
        return (TableService<E>) super.target();
    }
}

