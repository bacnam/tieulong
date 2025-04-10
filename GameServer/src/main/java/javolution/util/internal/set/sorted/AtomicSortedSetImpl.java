package javolution.util.internal.set.sorted;

import java.util.Comparator;
import java.util.SortedSet;
import javolution.util.internal.set.AtomicSetImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;
import javolution.util.service.SortedSetService;

public class AtomicSortedSetImpl<E>
extends AtomicSetImpl<E>
implements SortedSetService<E>
{
private static final long serialVersionUID = 1536L;

public AtomicSortedSetImpl(SortedSetService<E> target) {
super((SetService)target);
}

public E first() {
return (E)targetView().first();
}

public SortedSetService<E> headSet(E toElement) {
return new SubSortedSetImpl<E>(this, null, toElement);
}

public E last() {
return (E)targetView().last();
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

protected SortedSetService<E> targetView() {
return (SortedSetService<E>)super.targetView();
}
}

