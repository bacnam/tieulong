package javolution.util.internal.collection;

import java.util.Iterator;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

public class SequentialCollectionImpl<E>
extends CollectionView<E>
{
private static final long serialVersionUID = 1536L;

public SequentialCollectionImpl(CollectionService<E> target) {
super(target);
}

public boolean add(E e) {
return target().add(e);
}

public void clear() {
target().clear();
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
return target().iterator();
}

public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
action.accept(view);
}

public boolean remove(Object obj) {
return target().remove(obj);
}

public int size() {
return target().size();
}

public void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
action.accept(view);
}
}

