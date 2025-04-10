package javolution.util.internal.collection;

import java.util.Collection;
import java.util.Iterator;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

public class AtomicCollectionImpl<E>
extends CollectionView<E>
{
private static final long serialVersionUID = 1536L;
protected volatile CollectionService<E> immutable;
protected transient Thread updatingThread;

private class IteratorImpl
implements Iterator<E>
{
private E current;
private final Iterator<E> targetIterator = AtomicCollectionImpl.this.targetView().iterator();

public boolean hasNext() {
return this.targetIterator.hasNext();
}

public E next() {
this.current = this.targetIterator.next();
return this.current;
}

public void remove() {
if (this.current == null) throw new IllegalStateException(); 
AtomicCollectionImpl.this.remove(this.current);
this.current = null;
}
}

public AtomicCollectionImpl(CollectionService<E> target) {
super(target);
this.immutable = cloneTarget();
}

public synchronized boolean add(E element) {
boolean changed = target().add(element);
if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
return changed;
}

public synchronized boolean addAll(Collection<? extends E> c) {
boolean changed = target().addAll(c);
if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
return changed;
}

public synchronized void clear() {
clear();
if (!updateInProgress()) {
this.immutable = cloneTarget();
}
}

public synchronized AtomicCollectionImpl<E> clone() {
AtomicCollectionImpl<E> copy = (AtomicCollectionImpl<E>)super.clone();
copy.updatingThread = null;
return copy;
}

public Equality<? super E> comparator() {
return this.immutable.comparator();
}

public boolean contains(Object o) {
return targetView().contains(o);
}

public boolean containsAll(Collection<?> c) {
return targetView().containsAll(c);
}

public boolean equals(Object o) {
return targetView().equals(o);
}

public int hashCode() {
return targetView().hashCode();
}

public boolean isEmpty() {
return targetView().isEmpty();
}

public Iterator<E> iterator() {
return new IteratorImpl();
}

public synchronized boolean remove(Object o) {
boolean changed = target().remove(o);
if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
return changed;
}

public synchronized boolean removeAll(Collection<?> c) {
boolean changed = target().removeAll(c);
if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
return changed;
}

public synchronized boolean retainAll(Collection<?> c) {
boolean changed = target().retainAll(c);
if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
return changed;
}

public int size() {
return targetView().size();
}

public CollectionService<E>[] split(int n) {
return (CollectionService<E>[])new CollectionService[] { this };
}

public CollectionService<E> threadSafe() {
return this;
}

public Object[] toArray() {
return targetView().toArray();
}

public <T> T[] toArray(T[] a) {
return (T[])targetView().toArray((Object[])a);
}

public synchronized void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
this.updatingThread = Thread.currentThread();
try {
target().update(action, view);
} finally {
this.updatingThread = null;
this.immutable = cloneTarget();
} 
}

protected CollectionService<E> targetView() {
return (this.updatingThread == null || this.updatingThread != Thread.currentThread()) ? this.immutable : target();
}

protected CollectionService<E> cloneTarget() {
try {
return target().clone();
} catch (CloneNotSupportedException e) {
throw new Error("Cannot happen since target is Cloneable.");
} 
}

protected final boolean updateInProgress() {
return (this.updatingThread == Thread.currentThread());
}
}

