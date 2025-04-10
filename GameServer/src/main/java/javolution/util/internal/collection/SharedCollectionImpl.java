package javolution.util.internal.collection;

import java.util.Collection;
import java.util.Iterator;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

public class SharedCollectionImpl<E>
extends CollectionView<E>
{
private static final long serialVersionUID = 1536L;
protected ReadWriteLockImpl lock;

private class IteratorImpl
implements Iterator<E>
{
private E next;
private final Iterator<E> targetIterator;

public IteratorImpl() {
SharedCollectionImpl.this.lock.readLock.lock();
try {
this.targetIterator = SharedCollectionImpl.this.cloneTarget().iterator();
} finally {
SharedCollectionImpl.this.lock.readLock.unlock();
} 
}

public boolean hasNext() {
return this.targetIterator.hasNext();
}

public E next() {
this.next = this.targetIterator.next();
return this.next;
}

public void remove() {
if (this.next == null) throw new IllegalStateException(); 
SharedCollectionImpl.this.remove(this.next);
this.next = null;
}
}

public SharedCollectionImpl(CollectionService<E> target) {
this(target, new ReadWriteLockImpl());
}

public SharedCollectionImpl(CollectionService<E> target, ReadWriteLockImpl lock) {
super(target);
this.lock = lock;
}

public boolean add(E element) {
this.lock.writeLock.lock();
try {
return target().add(element);
} finally {
this.lock.writeLock.unlock();
} 
}

public boolean addAll(Collection<? extends E> c) {
this.lock.writeLock.lock();
try {
return target().addAll(c);
} finally {
this.lock.writeLock.unlock();
} 
}

public void clear() {
this.lock.writeLock.lock();
try {
target().clear();
} finally {
this.lock.writeLock.unlock();
} 
}

public SharedCollectionImpl<E> clone() {
this.lock.readLock.lock();
try {
SharedCollectionImpl<E> copy = (SharedCollectionImpl<E>)super.clone();

copy.lock = new ReadWriteLockImpl();
return copy;
} finally {
this.lock.readLock.unlock();
} 
}

public Equality<? super E> comparator() {
return target().comparator();
}

public boolean contains(Object o) {
this.lock.readLock.lock();
try {
return target().contains(o);
} finally {
this.lock.readLock.unlock();
} 
}

public boolean containsAll(Collection<?> c) {
this.lock.readLock.lock();
try {
return target().containsAll(c);
} finally {
this.lock.readLock.unlock();
} 
}

public boolean equals(Object o) {
this.lock.readLock.lock();
try {
return target().equals(o);
} finally {
this.lock.readLock.unlock();
} 
}

public int hashCode() {
this.lock.readLock.lock();
try {
return target().hashCode();
} finally {
this.lock.readLock.unlock();
} 
}

public boolean isEmpty() {
this.lock.readLock.lock();
try {
return target().isEmpty();
} finally {
this.lock.readLock.unlock();
} 
}

public Iterator<E> iterator() {
return new IteratorImpl();
}

public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
this.lock.readLock.lock();
try {
target().perform(action, view);
} finally {
this.lock.readLock.unlock();
} 
}

public boolean remove(Object o) {
this.lock.writeLock.lock();
try {
return target().remove(o);
} finally {
this.lock.writeLock.unlock();
} 
}

public boolean removeAll(Collection<?> c) {
this.lock.writeLock.lock();
try {
return target().removeAll(c);
} finally {
this.lock.writeLock.unlock();
} 
}

public boolean retainAll(Collection<?> c) {
this.lock.writeLock.lock();
try {
return target().retainAll(c);
} finally {
this.lock.writeLock.unlock();
} 
}

public int size() {
this.lock.readLock.lock();
try {
return target().size();
} finally {
this.lock.readLock.unlock();
} 
}

public CollectionService<E>[] split(int n) {
CollectionService[] arrayOfCollectionService1;
this.lock.readLock.lock();
try {
arrayOfCollectionService1 = (CollectionService[])target().split(n);
} finally {
this.lock.readLock.unlock();
} 
CollectionService[] arrayOfCollectionService2 = new CollectionService[arrayOfCollectionService1.length];
for (int i = 0; i < arrayOfCollectionService1.length; i++) {
arrayOfCollectionService2[i] = new SharedCollectionImpl(arrayOfCollectionService1[i], this.lock);
}
return (CollectionService<E>[])arrayOfCollectionService2;
}

public CollectionService<E> threadSafe() {
return this;
}

public Object[] toArray() {
this.lock.readLock.lock();
try {
return target().toArray();
} finally {
this.lock.readLock.unlock();
} 
}

public <T> T[] toArray(T[] a) {
this.lock.readLock.lock();
try {
return (T[])target().toArray((Object[])a);
} finally {
this.lock.readLock.unlock();
} 
}

protected CollectionService<E> cloneTarget() {
try {
return target().clone();
} catch (CloneNotSupportedException e) {
throw new Error("Cannot happen since target is Cloneable.");
} 
}
}

