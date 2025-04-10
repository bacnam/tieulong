package javolution.util.internal.set.sorted;

import java.util.Comparator;
import java.util.SortedSet;
import javolution.util.internal.set.UnmodifiableSetImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;
import javolution.util.service.SortedSetService;

public class UnmodifiableSortedSetImpl<E>
extends UnmodifiableSetImpl<E>
implements SortedSetService<E>
{
private static final long serialVersionUID = 1536L;

public UnmodifiableSortedSetImpl(SetService<E> target) {
super(target);
}

public E first() {
return (E)target().first();
}

public SortedSetService<E> headSet(E toElement) {
return new SubSortedSetImpl<E>(this, null, toElement);
}

public E last() {
return (E)target().last();
}

public SortedSetService<E> subSet(E fromElement, E toElement) {
return new SubSortedSetImpl<E>(this, fromElement, toElement);
}

public SortedSetService<E> tailSet(E fromElement) {
return new SubSortedSetImpl<E>(this, fromElement, null);
}

public SortedSetService<E> threadSafe() {
return this;
}

protected SortedSetService<E> target() {
return (SortedSetService<E>)super.target();
}
}

