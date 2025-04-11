package javolution.util;

import javolution.lang.Realtime;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.function.Predicate;
import javolution.util.internal.map.FastMapImpl;
import javolution.util.internal.set.AtomicSetImpl;
import javolution.util.internal.set.FilteredSetImpl;
import javolution.util.internal.set.SharedSetImpl;
import javolution.util.internal.set.UnmodifiableSetImpl;
import javolution.util.service.SetService;

import java.util.Set;

public class FastSet<E>
        extends FastCollection<E>
        implements Set<E> {
    private static final long serialVersionUID = 1536L;
    private final SetService<E> service;

    public FastSet() {
        this(Equalities.STANDARD);
    }

    public FastSet(Equality<? super E> comparator) {
        this.service = (new FastMapImpl(comparator, Equalities.IDENTITY)).keySet();
    }

    protected FastSet(SetService<E> service) {
        this.service = service;
    }

    public FastSet<E> atomic() {
        return new FastSet((SetService<E>) new AtomicSetImpl(service()));
    }

    public FastSet<E> filtered(Predicate<? super E> filter) {
        return new FastSet((SetService<E>) new FilteredSetImpl(service(), filter));
    }

    public FastSet<E> shared() {
        return new FastSet((SetService<E>) new SharedSetImpl(service()));
    }

    public FastSet<E> unmodifiable() {
        return new FastSet((SetService<E>) new UnmodifiableSetImpl(service()));
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public int size() {
        return this.service.size();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void clear() {
        this.service.clear();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean contains(Object obj) {
        return this.service.contains(obj);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean remove(Object obj) {
        return this.service.remove(obj);
    }

    public FastSet<E> addAll(E... elements) {
        return (FastSet<E>) super.addAll(elements);
    }

    public FastSet<E> addAll(FastCollection<? extends E> that) {
        return (FastSet<E>) super.addAll(that);
    }

    protected SetService<E> service() {
        return this.service;
    }
}

