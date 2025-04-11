package javolution.util;

import javolution.lang.Realtime;
import javolution.util.function.Consumer;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.table.*;
import javolution.util.service.TableService;

import java.util.*;

public class FastTable<E>
        extends FastCollection<E>
        implements List<E>, Deque<E>, RandomAccess {
    private static final long serialVersionUID = 1536L;
    private final TableService<E> service;

    public FastTable() {
        this(Equalities.STANDARD);
    }

    public FastTable(Equality<? super E> comparator) {
        this.service = (TableService<E>) new FastTableImpl(comparator);
    }

    protected FastTable(TableService<E> service) {
        this.service = service;
    }

    public FastTable<E> atomic() {
        return new FastTable((TableService<E>) new AtomicTableImpl(this.service));
    }

    public FastTable<E> reversed() {
        return new FastTable((TableService<E>) new ReversedTableImpl(this.service));
    }

    public FastTable<E> shared() {
        return new FastTable((TableService<E>) new SharedTableImpl(this.service));
    }

    public FastTable<E> unmodifiable() {
        return new FastTable((TableService<E>) new UnmodifiableTableImpl(this.service));
    }

    public FastTable<E> subTable(int fromIndex, int toIndex) {
        return new FastTable((TableService<E>) new SubTableImpl(this.service, fromIndex, toIndex));
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean isEmpty() {
        return this.service.isEmpty();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public int size() {
        return this.service.size();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void clear() {
        this.service.clear();
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public void add(int index, E element) {
        this.service.add(index, element);
    }

    @Realtime(limit = Realtime.Limit.N_LOG_N)
    public boolean addAll(int index, Collection<? extends E> elements) {
        return this.service.addAll(index, elements);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public E remove(int index) {
        return (E) this.service.remove(index);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E get(int index) {
        return (E) this.service.get(index);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E set(int index, E element) {
        return (E) this.service.set(index, element);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public int indexOf(Object element) {
        return this.service.indexOf(element);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public int lastIndexOf(Object element) {
        return this.service.lastIndexOf(element);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public ListIterator<E> listIterator() {
        return this.service.listIterator();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public ListIterator<E> listIterator(int index) {
        return this.service.listIterator(index);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void addFirst(E element) {
        this.service.addFirst(element);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void addLast(E element) {
        this.service.addLast(element);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E getFirst() {
        return (E) this.service.getFirst();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E getLast() {
        return (E) this.service.getLast();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E peekFirst() {
        return (E) this.service.peekFirst();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E peekLast() {
        return (E) this.service.peekLast();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E pollFirst() {
        return (E) this.service.pollFirst();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E pollLast() {
        return (E) this.service.pollLast();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E removeFirst() {
        return (E) this.service.removeFirst();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E removeLast() {
        return (E) this.service.removeLast();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean offerFirst(E e) {
        return this.service.offerFirst(e);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean offerLast(E e) {
        return this.service.offerLast(e);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public boolean removeFirstOccurrence(Object o) {
        return this.service.removeFirstOccurrence(o);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public boolean removeLastOccurrence(Object o) {
        return this.service.removeLastOccurrence(o);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean offer(E e) {
        return this.service.offer(e);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E remove() {
        return (E) this.service.remove();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E poll() {
        return (E) this.service.poll();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E element() {
        return (E) this.service.element();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E peek() {
        return (E) this.service.peek();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void push(E e) {
        this.service.push(e);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public E pop() {
        return (E) this.service.pop();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public Iterator<E> descendingIterator() {
        return this.service.descendingIterator();
    }

    @Realtime(limit = Realtime.Limit.N_SQUARE)
    public void sort() {
        update((Consumer) new Consumer<TableService<E>>() {
            public void accept(TableService<E> table) {
                QuickSort<E> qs = new QuickSort(table, (Comparator) table.comparator());
                qs.sort();
            }
        });
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public FastTable<E> addAll(E... elements) {
        return (FastTable<E>) super.addAll(elements);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public FastTable<E> addAll(FastCollection<? extends E> that) {
        return (FastTable<E>) super.addAll(that);
    }

    @Deprecated
    public FastTable<E> subList(int fromIndex, int toIndex) {
        return subTable(fromIndex, toIndex);
    }

    protected TableService<E> service() {
        return this.service;
    }
}

