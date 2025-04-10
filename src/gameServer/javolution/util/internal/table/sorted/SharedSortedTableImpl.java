package javolution.util.internal.table.sorted;

import javolution.util.internal.table.SharedTableImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public class SharedSortedTableImpl<E>
extends SharedTableImpl<E>
implements SortedTableService<E>
{
private static final long serialVersionUID = 1536L;

public SharedSortedTableImpl(SortedTableService<E> target) {
super((TableService)target);
}

public boolean addIfAbsent(E element) {
this.lock.writeLock.lock();
try {
return target().addIfAbsent(element);
} finally {
this.lock.writeLock.unlock();
} 
}

public int positionOf(E element) {
this.lock.readLock.lock();
try {
return target().positionOf(element);
} finally {
this.lock.readLock.unlock();
} 
}

public SortedTableService<E> threadSafe() {
return this;
}

protected SortedTableService<E> target() {
return (SortedTableService<E>)super.target();
}
}

