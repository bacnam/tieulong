package javolution.util;

import javolution.lang.Realtime;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.table.sorted.AtomicSortedTableImpl;
import javolution.util.internal.table.sorted.FastSortedTableImpl;
import javolution.util.internal.table.sorted.SharedSortedTableImpl;
import javolution.util.internal.table.sorted.UnmodifiableSortedTableImpl;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public class FastSortedTable<E>
        extends FastTable<E> {
    private static final long serialVersionUID = 1536L;

    public FastSortedTable() {
        this(Equalities.STANDARD);
    }

    public FastSortedTable(Equality<? super E> comparator) {
        super((TableService<E>) new FastSortedTableImpl(comparator));
    }

    protected FastSortedTable(SortedTableService<E> service) {
        super((TableService<E>) service);
    }

    public FastSortedTable<E> atomic() {
        return new FastSortedTable((SortedTableService<E>) new AtomicSortedTableImpl((TableService) service()));
    }

    public FastSortedTable<E> shared() {
        return new FastSortedTable((SortedTableService<E>) new SharedSortedTableImpl(service()));
    }

    public FastSortedTable<E> unmodifiable() {
        return new FastSortedTable((SortedTableService<E>) new UnmodifiableSortedTableImpl(service()));
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean contains(Object obj) {
        return service().contains(obj);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean remove(Object obj) {
        return service().remove(obj);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public int indexOf(Object obj) {
        return service().indexOf(obj);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public boolean addIfAbsent(E element) {
        return service().addIfAbsent(element);
    }

    @Realtime(limit = Realtime.Limit.LOG_N)
    public int positionOf(E element) {
        return service().positionOf(element);
    }

    public FastSortedTable<E> addAll(E... elements) {
        return (FastSortedTable<E>) super.addAll(elements);
    }

    public FastSortedTable<E> addAll(FastCollection<? extends E> that) {
        return (FastSortedTable<E>) super.addAll(that);
    }

    protected SortedTableService<E> service() {
        return (SortedTableService<E>) super.service();
    }
}

