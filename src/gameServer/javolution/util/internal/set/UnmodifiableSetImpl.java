package javolution.util.internal.set;

import javolution.util.internal.collection.UnmodifiableCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public class UnmodifiableSetImpl<E>
extends UnmodifiableCollectionImpl<E>
implements SetService<E>
{
private static final long serialVersionUID = 1536L;

public UnmodifiableSetImpl(SetService<E> target) {
super((CollectionService)target);
}

public SetService<E> threadSafe() {
return this;
}
}

