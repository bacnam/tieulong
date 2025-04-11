package javolution.util.internal.table.sorted;

import javolution.util.internal.table.TableView;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public abstract class SortedTableView<E>
        extends TableView<E>
        implements SortedTableService<E> {
    private static final long serialVersionUID = 1536L;

    public SortedTableView(SortedTableService<E> target) {
        super((TableService) target);
    }

    public boolean addIfAbsent(E element) {
        if (!contains(element)) return add(element);
        return false;
    }

    public int indexOf(Object o) {
        int i = positionOf((E) o);
        if (i >= size() || !comparator().areEqual(o, get(i))) return -1;
        return i;
    }

    public int lastIndexOf(Object o) {
        int i = positionOf((E) o);
        int result = -1;
        while (i < size() && comparator().areEqual(o, get(i))) {
            result = i++;
        }
        return result;
    }

    public SortedTableService<E> threadSafe() {
        return new SharedSortedTableImpl<E>(this);
    }

    protected SortedTableService<E> target() {
        return (SortedTableService<E>) super.target();
    }

    public abstract int positionOf(E paramE);
}

