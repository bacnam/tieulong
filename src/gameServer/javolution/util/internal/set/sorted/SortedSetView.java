package javolution.util.internal.set.sorted;

import java.util.Comparator;
import java.util.SortedSet;
import javolution.util.internal.set.SetView;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;
import javolution.util.service.SortedSetService;

public abstract class SortedSetView<E>
extends SetView<E>
implements SortedSetService<E>
{
private static final long serialVersionUID = 1536L;

public SortedSetView(SortedSetService<E> target) {
super((SetService)target);
}

public SortedSetService<E> headSet(E toElement) {
return new SubSortedSetImpl<E>(this, null, toElement);
}

public SortedSetService<E> subSet(E fromElement, E toElement) {
return new SubSortedSetImpl<E>(this, fromElement, toElement);
}

public SortedSetService<E> tailSet(E fromElement) {
return new SubSortedSetImpl<E>(this, fromElement, null);
}

public SortedSetService<E> threadSafe() {
return new SharedSortedSetImpl<E>((SetService<E>)this);
}

protected SortedSetService<E> target() {
return (SortedSetService<E>)super.target();
}

public abstract E first();

public abstract E last();
}

