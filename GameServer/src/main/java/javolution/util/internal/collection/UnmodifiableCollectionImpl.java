package javolution.util.internal.collection;

import java.util.Iterator;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

public class UnmodifiableCollectionImpl<E>
extends CollectionView<E>
{
private static final long serialVersionUID = 1536L;

private class IteratorImpl
implements Iterator<E>
{
private final Iterator<E> targetIterator = UnmodifiableCollectionImpl.this.target().iterator();

public boolean hasNext() {
return this.targetIterator.hasNext();
}

public E next() {
return this.targetIterator.next();
}

public void remove() {
throw new UnsupportedOperationException("Read-Only Collection.");
}

private IteratorImpl() {}
}

public UnmodifiableCollectionImpl(CollectionService<E> target) {
super(target);
}

public boolean add(E element) {
throw new UnsupportedOperationException("Read-Only Collection.");
}

public void clear() {
throw new UnsupportedOperationException("Read-Only Collection.");
}

public Equality<? super E> comparator() {
return target().comparator();
}

public boolean contains(Object obj) {
return target().contains(obj);
}

public boolean isEmpty() {
return target().isEmpty();
}

public Iterator<E> iterator() {
return new IteratorImpl();
}

public boolean remove(Object o) {
throw new UnsupportedOperationException("Read-Only Collection.");
}

public int size() {
return target().size();
}

public CollectionService<E> threadSafe() {
return this;
}
}

