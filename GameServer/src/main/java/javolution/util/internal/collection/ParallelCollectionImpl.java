package javolution.util.internal.collection;

import javolution.context.ConcurrentContext;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

import java.util.Iterator;

public class ParallelCollectionImpl<E>
        extends CollectionView<E> {
    private static final long serialVersionUID = 1536L;

    public ParallelCollectionImpl(CollectionService<E> target) {
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

    public void perform(final Consumer<CollectionService<E>> action, CollectionService<E> view) {
        ConcurrentContext ctx = ConcurrentContext.enter();
        try {
            int concurrency = ctx.getConcurrency();
            CollectionService[] arrayOfCollectionService = (CollectionService[]) view.split(concurrency + 1);
            for (int i = 1; i < arrayOfCollectionService.length; i++) {
                final CollectionService<E> subView = arrayOfCollectionService[i];
                ctx.execute(new Runnable() {
                    public void run() {
                        ParallelCollectionImpl.this.target().perform(action, subView);
                    }
                });
            }
            target().perform(action, arrayOfCollectionService[0]);
        } finally {

            ctx.exit();
        }
    }

    public boolean remove(Object obj) {
        return target().remove(obj);
    }

    public int size() {
        return target().size();
    }

    public void update(final Consumer<CollectionService<E>> action, CollectionService<E> view) {
        ConcurrentContext ctx = ConcurrentContext.enter();
        try {
            int concurrency = ctx.getConcurrency();
            CollectionService[] arrayOfCollectionService = (CollectionService[]) view.threadSafe().split(concurrency + 1);

            for (int i = 1; i < arrayOfCollectionService.length; i++) {
                final CollectionService<E> subView = arrayOfCollectionService[i];
                ctx.execute(new Runnable() {
                    public void run() {
                        ParallelCollectionImpl.this.target().update(action, subView);
                    }
                });
            }
            target().perform(action, arrayOfCollectionService[0]);
        } finally {

            ctx.exit();
        }
    }
}

