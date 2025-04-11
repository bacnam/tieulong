package javolution.util.internal.set;

import javolution.util.function.Function;
import javolution.util.internal.collection.MappedCollectionImpl;
import javolution.util.service.CollectionService;
import javolution.util.service.SetService;

public abstract class MappedSetImpl<E, R>
        extends MappedCollectionImpl<E, R>
        implements SetService<R> {
    private static final long serialVersionUID = 1536L;

    public MappedSetImpl(SetService<E> target, Function<? super E, ? extends R> function) {
        super((CollectionService) target, function);
    }

    public SetService<R> threadSafe() {
        return new SharedSetImpl<R>(this);
    }

    public abstract boolean add(R paramR);

    public abstract boolean contains(Object paramObject);

    public abstract boolean remove(Object paramObject);
}

