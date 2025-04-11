package javolution.util.internal.set.sorted;

import javolution.util.internal.set.SharedSetImpl;
import javolution.util.service.SetService;
import javolution.util.service.SortedSetService;

public class SharedSortedSetImpl<E>
        extends SharedSetImpl<E>
        implements SortedSetService<E> {
    private static final long serialVersionUID = 1536L;

    public SharedSortedSetImpl(SetService<E> target) {
        super(target);
    }

    public E first() {
        this.lock.readLock.lock();
        try {
            return (E) target().first();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public SortedSetService<E> headSet(E toElement) {
        return new SubSortedSetImpl<E>(this, null, toElement);
    }

    public E last() {
        this.lock.readLock.lock();
        try {
            return (E) target().last();
        } finally {
            this.lock.readLock.unlock();
        }
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
        return (SortedSetService<E>) super.target();
    }
}

