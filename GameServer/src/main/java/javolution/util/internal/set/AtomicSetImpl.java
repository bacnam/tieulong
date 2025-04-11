package javolution.util.internal.set;

import javolution.util.internal.collection.AtomicCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public class AtomicSetImpl<E>
        extends AtomicCollectionImpl<E>
        implements SetService<E> {
    private static final long serialVersionUID = 1536L;

    public AtomicSetImpl(SetService<E> target) {
        super((CollectionService) target);
    }

    public SetService<E> threadSafe() {
        return this;
    }
}

