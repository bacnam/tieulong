package javolution.util.internal.table;

import javolution.util.function.Equality;
import javolution.util.internal.collection.CollectionView;
import javolution.util.service.CollectionService;
import javolution.util.service.TableService;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public abstract class TableView<E>
        extends CollectionView<E>
        implements TableService<E> {
    private static final long serialVersionUID = 1536L;

    public TableView(TableService<E> target) {
        super((CollectionService) target);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return subList(index, index).addAll(c);
    }

    public void addFirst(E element) {
        add(0, element);
    }

    public void addLast(E element) {
        add(size(), element);
    }

    public final boolean contains(Object o) {
        return (indexOf(o) >= 0);
    }

    public Iterator<E> descendingIterator() {
        return (new ReversedTableImpl<E>(this)).iterator();
    }

    public final E element() {
        return getFirst();
    }

    public E getFirst() {
        if (size() == 0) emptyError();
        return get(0);
    }

    public E getLast() {
        if (size() == 0) emptyError();
        return get(size() - 1);
    }

    public int indexOf(Object o) {
        Equality<Object> cmp = comparator();
        for (int i = 0, n = size(); i < n; i++) {
            if (cmp.areEqual(o, get(i))) return i;
        }
        return -1;
    }

    public final boolean isEmpty() {
        return (size() == 0);
    }

    public Iterator<E> iterator() {
        return listIterator(0);
    }

    public int lastIndexOf(Object o) {
        Equality<Object> cmp = comparator();
        for (int i = size() - 1; i >= 0; i--) {
            if (cmp.areEqual(o, get(i))) return i;
        }
        return -1;
    }

    public final ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        return new TableIteratorImpl<E>(this, index);
    }

    public final boolean offer(E e) {
        return offerLast(e);
    }

    public final boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    public final boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    public final E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        return (size() == 0) ? null : getFirst();
    }

    public E peekLast() {
        return (size() == 0) ? null : getLast();
    }

    public final E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        return (size() == 0) ? null : removeFirst();
    }

    public E pollLast() {
        return (size() == 0) ? null : removeLast();
    }

    public final E pop() {
        return removeFirst();
    }

    public final void push(E e) {
        addFirst(e);
    }

    public final E remove() {
        return removeFirst();
    }

    public final boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    public E removeFirst() {
        if (size() == 0) emptyError();
        return remove(0);
    }

    public boolean removeFirstOccurrence(Object o) {
        int i = indexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    public E removeLast() {
        if (size() == 0) emptyError();
        return remove(size() - 1);
    }

    public boolean removeLastOccurrence(Object o) {
        int i = lastIndexOf(o);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    public CollectionService<E>[] split(int n) {
        return SubTableImpl.splitOf(this, n);
    }

    public TableService<E> subList(int fromIndex, int toIndex) {
        return new SubTableImpl<E>(this, fromIndex, toIndex);
    }

    public TableService<E> threadSafe() {
        return new SharedTableImpl<E>(this);
    }

    protected void emptyError() {
        throw new NoSuchElementException("Empty Table");
    }

    protected void indexError(int index) {
        throw new IndexOutOfBoundsException("index: " + index + ", size: " + size());
    }

    protected TableService<E> target() {
        return (TableService<E>) super.target();
    }

    public abstract void add(int paramInt, E paramE);

    public abstract void clear();

    public abstract E get(int paramInt);

    public abstract E remove(int paramInt);

    public abstract E set(int paramInt, E paramE);

    public abstract int size();
}

