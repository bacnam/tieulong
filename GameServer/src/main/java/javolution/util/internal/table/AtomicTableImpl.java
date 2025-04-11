package javolution.util.internal.table;

import javolution.util.internal.collection.AtomicCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.TableService;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class AtomicTableImpl<E>
        extends AtomicCollectionImpl<E>
        implements TableService<E> {
    private static final long serialVersionUID = 1536L;

    public AtomicTableImpl(TableService<E> target) {
        super((CollectionService) target);
    }

    public synchronized void add(int index, E element) {
        target().add(index, element);
        if (!updateInProgress()) this.immutable = cloneTarget();

    }

    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = target().addAll(index, c);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public synchronized void addFirst(E element) {
        target().addFirst(element);
        if (!updateInProgress()) this.immutable = cloneTarget();

    }

    public synchronized void addLast(E element) {
        target().addLast(element);
        if (!updateInProgress()) this.immutable = cloneTarget();

    }

    public Iterator<E> descendingIterator() {
        return (new ReversedTableImpl<E>(this)).iterator();
    }

    public E element() {
        return getFirst();
    }

    public E get(int index) {
        return (E) targetView().get(index);
    }

    public E getFirst() {
        return (E) targetView().getFirst();
    }

    public E getLast() {
        return (E) targetView().getLast();
    }

    public int indexOf(Object element) {
        return targetView().indexOf(element);
    }

    public ListIterator<E> iterator() {
        return listIterator(0);
    }

    public int lastIndexOf(Object element) {
        return targetView().lastIndexOf(element);
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        return new TableIteratorImpl<E>(this, index);
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public synchronized boolean offerFirst(E e) {
        boolean changed = target().offerFirst(e);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public synchronized boolean offerLast(E e) {
        boolean changed = target().offerLast(e);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        return (E) targetView().peekFirst();
    }

    public E peekLast() {
        return (E) targetView().peekLast();
    }

    public E poll() {
        return pollFirst();
    }

    public synchronized E pollFirst() {
        E e = (E) target().pollFirst();
        if (e != null && !updateInProgress()) this.immutable = cloneTarget();
        return e;
    }

    public synchronized E pollLast() {
        E e = (E) target().pollLast();
        if (e != null && !updateInProgress()) this.immutable = cloneTarget();
        return e;
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

    public synchronized E remove(int index) {
        E e = (E) target().remove(index);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return e;
    }

    public synchronized E removeFirst() {
        E e = (E) target().removeFirst();
        if (!updateInProgress()) this.immutable = cloneTarget();
        return e;
    }

    public synchronized boolean removeFirstOccurrence(Object o) {
        boolean changed = target().removeFirstOccurrence(o);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public synchronized E removeLast() {
        E e = (E) target().removeLast();
        if (!updateInProgress()) this.immutable = cloneTarget();
        return e;
    }

    public synchronized boolean removeLastOccurrence(Object o) {
        boolean changed = target().removeLastOccurrence(o);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public synchronized E set(int index, E element) {
        E e = (E) target().set(index, element);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return e;
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

    protected TableService<E> targetView() {
        return (TableService<E>) super.targetView();
    }

    protected TableService<E> target() {
        return (TableService<E>) super.target();
    }
}

