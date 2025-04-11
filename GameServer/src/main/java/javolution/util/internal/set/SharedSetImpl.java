package javolution.util.internal.set;

import javolution.util.internal.collection.SharedCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public class SharedSetImpl<E>
        extends SharedCollectionImpl<E>
        implements SetService<E> {
    private static final long serialVersionUID = 1536L;

    public SharedSetImpl(SetService<E> target) {
        super((CollectionService) target);
    }

    public SetService<E> threadSafe() {
        return this;
    }
}

