package javolution.util.internal.table.sorted;

import javolution.util.internal.table.AtomicTableImpl;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public class AtomicSortedTableImpl<E>
        extends AtomicTableImpl<E>
        implements SortedTableService<E> {
    private static final long serialVersionUID = 1536L;

    public AtomicSortedTableImpl(TableService<E> target) {
        super(target);
    }

    public synchronized boolean addIfAbsent(E element) {
        boolean changed = target().addIfAbsent(element);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public int positionOf(E element) {
        return targetView().positionOf(element);
    }

    public SortedTableService<E> threadSafe() {
        return this;
    }

    protected SortedTableService<E> targetView() {
        return (SortedTableService<E>) super.targetView();
    }

    protected SortedTableService<E> target() {
        return (SortedTableService<E>) super.target();
    }
}

