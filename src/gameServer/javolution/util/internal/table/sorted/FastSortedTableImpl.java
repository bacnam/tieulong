package javolution.util.internal.table.sorted;

import javolution.util.function.Equality;
import javolution.util.internal.table.FastTableImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SortedTableService;
import javolution.util.service.TableService;

public class FastSortedTableImpl<E>
extends FastTableImpl<E>
implements SortedTableService<E>
{
private static final long serialVersionUID = 1536L;

public FastSortedTableImpl(Equality<? super E> comparator) {
super(comparator);
}

public boolean add(E element) {
add(positionOf(element), element);
return true;
}

public boolean addIfAbsent(E element) {
int i = positionOf(element);
if (i < size() && comparator().areEqual(element, get(i))) return false; 
add(i, element);
return true;
}

public int indexOf(Object element) {
int i = positionOf((E)element);
if (i >= size() || !comparator().areEqual(get(i), element)) return -1; 
return i;
}

public int positionOf(E element) {
return positionOf(element, 0, size());
}

public SortedTableService<E> threadSafe() {
return new SharedSortedTableImpl<E>(this);
}

private int positionOf(E element, int start, int length) {
if (length == 0) return start; 
int half = length >> 1;
return (comparator().compare(element, get(start + half)) <= 0) ? positionOf(element, start, half) : positionOf(element, start + half + 1, length - half - 1);
}
}

