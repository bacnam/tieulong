package javolution.util;

import javolution.lang.Realtime;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.map.sorted.FastSortedMapImpl;
import javolution.util.internal.set.sorted.AtomicSortedSetImpl;
import javolution.util.internal.set.sorted.SharedSortedSetImpl;
import javolution.util.internal.set.sorted.UnmodifiableSortedSetImpl;
import javolution.util.service.SetService;
import javolution.util.service.SortedSetService;

import java.util.SortedSet;

public class FastSortedSet<E>
        extends FastSet<E>
        implements SortedSet<E> {
    private static final long serialVersionUID = 1536L;

    public FastSortedSet() {
        this(Equalities.STANDARD);
    }

    public FastSortedSet(Equality<? super E> comparator) {
        super((SetService<E>) (new FastSortedMapImpl(comparator, Equalities.IDENTITY)).keySet());
    }

    protected FastSortedSet(SortedSetService<E> service) {
        super((SetService<E>) service);
    }

    public FastSortedSet<E> atomic() {
        return new FastSortedSet((SortedSetService<E>) new AtomicSortedSetImpl(service()));
    }

    public FastSortedSet<E> shared() {
        return new FastSortedSet((SortedSetService<E>) new SharedSortedSetImpl((SetService) service()));
    }

    public FastSortedSet<E> unmodifiable() {
        return new FastSortedSet((SortedSetService<E>) new UnmodifiableSortedSetImpl((SetService) service()));
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean add(E e) {
        return super.add(e);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean contains(Object obj) {
        return super.contains(obj);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean remove(Object obj) {
        return super.remove(obj);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public FastSortedSet<E> subSet(E fromElement, E toElement) {
        return new FastSortedSet(service().subSet(fromElement, toElement));
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public FastSortedSet<E> headSet(E toElement) {
        return subSet(first(), toElement);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public FastSortedSet<E> tailSet(E fromElement) {
        return subSet(fromElement, last());
    }

    public E first() {
        return (E) service().first();
    }

    public E last() {
        return (E) service().last();
    }

    public FastSortedSet<E> addAll(E... elements) {
        return (FastSortedSet<E>) super.addAll(elements);
    }

    public FastSortedSet<E> addAll(FastCollection<? extends E> that) {
        return (FastSortedSet<E>) super.addAll(that);
    }

    protected SortedSetService<E> service() {
        return (SortedSetService<E>) super.service();
    }
}

