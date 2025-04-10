package javolution.util.internal.table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javolution.util.FastCollection;
import javolution.util.function.Equality;
import javolution.util.internal.collection.CollectionView;
import javolution.util.service.CollectionService;
import javolution.util.service.TableService;

public class FastTableImpl<E>
extends TableView<E>
{
private static final long serialVersionUID = 1536L;
private transient int capacity;
private final Equality<? super E> comparator;
private transient FractalTableImpl fractal;
private transient int size;

private class IteratorImpl
implements Iterator<E> {
private int currentIndex = -1;

private int nextIndex;

public boolean hasNext() {
return (this.nextIndex < FastTableImpl.this.size);
}

public E next() {
if (this.nextIndex >= FastTableImpl.this.size) throw new NoSuchElementException(); 
this.currentIndex = this.nextIndex++;
return (E)FastTableImpl.this.fractal.get(this.currentIndex);
}

public void remove() {
if (this.currentIndex < 0) throw new IllegalStateException(); 
FastTableImpl.this.remove(this.currentIndex);
this.nextIndex--;
this.currentIndex = -1;
}

private IteratorImpl() {}
}

public FastTableImpl(Equality<? super E> comparator) {
super((TableService<E>)null);
this.comparator = comparator;
}

public boolean add(E element) {
addLast(element);
return true;
}

public void add(int index, E element) {
if (index < 0 || index > this.size) indexError(index); 
checkUpsize();
if (index >= this.size >> 1) {
this.fractal.shiftRight(element, index, this.size - index);
} else {
this.fractal.shiftLeft(element, index - 1, index);
this.fractal.offset--;
} 
this.size++;
}

public void addFirst(E element) {
checkUpsize();
this.fractal.offset--;
this.fractal.set(0, element);
this.size++;
}

public void addLast(E element) {
checkUpsize();
this.fractal.set(this.size++, element);
}

public void clear() {
this.fractal = null;
this.capacity = 0;
this.size = 0;
}

public FastTableImpl<E> clone() {
FastTableImpl<E> copy = new FastTableImpl(comparator());
copy.addAll((FastCollection)this);
return copy;
}

public Equality<? super E> comparator() {
return this.comparator;
}

public E get(int index) {
if (index < 0 && index >= this.size) indexError(index); 
return (E)this.fractal.get(index);
}

public E getFirst() {
if (this.size == 0) emptyError(); 
return get(0);
}

public E getLast() {
if (this.size == 0) emptyError(); 
return get(this.size - 1);
}

public Iterator<E> iterator() {
return new IteratorImpl();
}

public E remove(int index) {
if (index < 0 || index >= this.size) indexError(index); 
E removed = (E)this.fractal.get(index);
if (index >= this.size >> 1) {
this.fractal.shiftLeft(null, this.size - 1, this.size - index - 1);
} else {
this.fractal.shiftRight(null, 0, index);
this.fractal.offset++;
} 
this.size--;
return removed;
}

public E removeFirst() {
if (this.size == 0) emptyError(); 
E first = (E)this.fractal.set(0, null);
this.fractal.offset++;
this.size--;
return first;
}

public E removeLast() {
if (this.size == 0) emptyError(); 
E last = (E)this.fractal.set(--this.size, null);
return last;
}

public E set(int index, E element) {
if (index < 0 && index >= this.size) indexError(index); 
return (E)this.fractal.set(index, element);
}

public int size() {
return this.size;
}

private void checkUpsize() {
if (this.size >= this.capacity) upsize();

}

private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
s.defaultReadObject();
int n = s.readInt();
for (int i = 0; i < n; i++)
addLast((E)s.readObject()); 
}

private void upsize() {
this.fractal = (this.fractal == null) ? new FractalTableImpl() : this.fractal.upsize();
this.capacity = this.fractal.capacity();
}

private void writeObject(ObjectOutputStream s) throws IOException {
s.defaultWriteObject();
s.writeInt(this.size);
for (int i = 0; i < this.size; i++)
s.writeObject(this.fractal.get(i)); 
}
}

