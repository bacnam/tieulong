package javolution.util.internal.set;

import javolution.util.function.Predicate;
import javolution.util.internal.collection.FilteredCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public class FilteredSetImpl<E>
extends FilteredCollectionImpl<E>
implements SetService<E>
{
private static final long serialVersionUID = 1536L;

public FilteredSetImpl(SetService<E> target, Predicate<? super E> filter) {
super((CollectionService)target, filter);
}

public SetService<E> threadSafe() {
return new SharedSetImpl<E>(this);
}
}

