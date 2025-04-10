package javolution.util.internal.collection;

import java.util.Iterator;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.function.Function;
import javolution.util.service.CollectionService;

public class MappedCollectionImpl<E, R>
extends CollectionView<R>
{
private static final long serialVersionUID = 1536L;
protected final Function<? super E, ? extends R> function;

private class IteratorImpl
implements Iterator<R>
{
private final Iterator<E> targetIterator = MappedCollectionImpl.this.target().iterator();

public boolean hasNext() {
return this.targetIterator.hasNext();
}

public R next() {
return (R)MappedCollectionImpl.this.function.apply(this.targetIterator.next());
}

public void remove() {
this.targetIterator.remove();
}
}

public MappedCollectionImpl(CollectionService<E> target, Function<? super E, ? extends R> function) {
super(target);
this.function = function;
}

public boolean add(R element) {
throw new UnsupportedOperationException("New elements cannot be added to mapped views");
}

public void clear() {
target().clear();
}

public Equality<? super R> comparator() {
return Equalities.STANDARD;
}

public boolean isEmpty() {
return target().isEmpty();
}

public Iterator<R> iterator() {
return new IteratorImpl();
}

public int size() {
return target().size();
}
}

