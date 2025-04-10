package javolution.util.internal.table;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import javolution.util.service.TableService;

public final class TableIteratorImpl<E>
implements ListIterator<E>
{
private int currentIndex = -1;
private int end;
private int nextIndex;
private final TableService<E> table;

public TableIteratorImpl(TableService<E> table, int index) {
this.table = table;
this.nextIndex = index;
this.end = table.size();
}

public void add(E e) {
this.table.add(this.nextIndex++, e);
this.end++;
this.currentIndex = -1;
}

public boolean hasNext() {
return (this.nextIndex < this.end);
}

public boolean hasPrevious() {
return (this.nextIndex > 0);
}

public E next() {
if (this.nextIndex >= this.end) throw new NoSuchElementException(); 
this.currentIndex = this.nextIndex++;
return (E)this.table.get(this.currentIndex);
}

public int nextIndex() {
return this.nextIndex;
}

public E previous() {
if (this.nextIndex <= 0) throw new NoSuchElementException(); 
this.currentIndex = --this.nextIndex;
return (E)this.table.get(this.currentIndex);
}

public int previousIndex() {
return this.nextIndex - 1;
}

public void remove() {
if (this.currentIndex < 0) throw new IllegalStateException(); 
this.table.remove(this.currentIndex);
this.end--;
if (this.currentIndex < this.nextIndex) {
this.nextIndex--;
}
this.currentIndex = -1;
}

public void set(E e) {
if (this.currentIndex >= 0) {
this.table.set(this.currentIndex, e);
} else {
throw new IllegalStateException();
} 
}
}

