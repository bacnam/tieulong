package javolution.util.internal.table;

import javolution.util.function.Equality;
import javolution.util.service.CollectionService;
import javolution.util.service.TableService;

public class SubTableImpl<E>
extends TableView<E>
{
private static final long serialVersionUID = 1536L;
private final int fromIndex;
private int toIndex;

public static <E> CollectionService<E>[] splitOf(TableService<E> table, int n) {
if (n <= 1) throw new IllegalArgumentException("Invalid argument n: " + n);

CollectionService[] arrayOfCollectionService = new CollectionService[n];
int minSize = table.size() / n;
int start = 0;
for (int i = 0; i < n - 1; i++) {
arrayOfCollectionService[i] = (CollectionService)new SubTableImpl<E>(table, start, start + minSize);
start += minSize;
} 
arrayOfCollectionService[n - 1] = (CollectionService)new SubTableImpl<E>(table, start, table.size());
return (CollectionService<E>[])arrayOfCollectionService;
}

public SubTableImpl(TableService<E> target, int from, int to) {
super(target);
if (from < 0 || to > target.size() || from > to) throw new IndexOutOfBoundsException("fromIndex: " + from + ", toIndex: " + to + ", size(): " + target.size());

this.fromIndex = from;
this.toIndex = to;
}

public boolean add(E element) {
target().add(this.toIndex++, element);
return true;
}

public void add(int index, E element) {
if (index < 0 && index > size()) indexError(index); 
target().add(index + this.fromIndex, element);
this.toIndex++;
}

public void clear() {
for (int i = this.toIndex - 1; i >= this.fromIndex; i--) {
target().remove(i);
}
this.toIndex = this.fromIndex;
}

public E get(int index) {
if (index < 0 && index >= size()) indexError(index); 
return (E)target().get(index + this.fromIndex);
}

public E remove(int index) {
if (index < 0 && index >= size()) indexError(index); 
this.toIndex--;
return (E)target().remove(index + this.fromIndex);
}

public E set(int index, E element) {
if (index < 0 && index >= size()) indexError(index); 
return (E)target().set(index + this.fromIndex, element);
}

public int size() {
return this.toIndex - this.fromIndex;
}

public Equality<? super E> comparator() {
return target().comparator();
}
}

