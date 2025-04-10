package javolution.util.internal.table.sorted;

import javolution.util.internal.table.UnmodifiableTableImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public class UnmodifiableSortedTableImpl<E>
extends UnmodifiableTableImpl<E>
implements SortedTableService<E>
{
private static final long serialVersionUID = 1536L;

public UnmodifiableSortedTableImpl(SortedTableService<E> target) {
super((TableService)target);
}

public boolean addIfAbsent(E element) {
throw new UnsupportedOperationException("Read-Only Collection.");
}

public int positionOf(E element) {
return target().positionOf(element);
}

public SortedTableService<E> threadSafe() {
return this;
}

protected SortedTableService<E> target() {
return (SortedTableService<E>)super.target();
}
}

