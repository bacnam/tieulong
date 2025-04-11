package javolution.util.internal.map;

import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.MapService;

import java.util.Iterator;
import java.util.Map;

public class AtomicMapImpl<K, V>
        extends MapView<K, V> {
    private static final long serialVersionUID = 1536L;
    protected volatile MapService<K, V> immutable;
    protected transient Thread updatingThread;

    public AtomicMapImpl(MapService<K, V> target) {
        super(target);
        this.immutable = cloneTarget();
    }

    public synchronized void clear() {
        clear();
        if (!updateInProgress()) {
            this.immutable = cloneTarget();
        }
    }

    public synchronized AtomicMapImpl<K, V> clone() {
        AtomicMapImpl<K, V> copy = (AtomicMapImpl<K, V>) super.clone();
        copy.updatingThread = null;
        return copy;
    }

    public boolean containsKey(Object key) {
        return targetView().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return targetView().containsValue(value);
    }

    public V get(Object key) {
        return (V) targetView().get(key);
    }

    public boolean isEmpty() {
        return targetView().isEmpty();
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return new IteratorImpl();
    }

    public Equality<? super K> keyComparator() {
        return targetView().keyComparator();
    }

    public synchronized V put(K key, V value) {
        V v = (V) target().put(key, value);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return v;
    }

    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        target().putAll(m);
        if (!updateInProgress()) this.immutable = cloneTarget();

    }

    public synchronized V putIfAbsent(K key, V value) {
        V v = (V) target().putIfAbsent(key, value);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return v;
    }

    public synchronized V remove(Object key) {
        V v = (V) target().remove(key);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return v;
    }

    public synchronized boolean remove(Object key, Object value) {
        boolean changed = target().remove(key, value);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public synchronized V replace(K key, V value) {
        V v = (V) target().replace(key, value);
        if (!updateInProgress()) this.immutable = cloneTarget();
        return v;
    }

    public synchronized boolean replace(K key, V oldValue, V newValue) {
        boolean changed = target().replace(key, oldValue, newValue);
        if (changed && !updateInProgress()) this.immutable = cloneTarget();
        return changed;
    }

    public int size() {
        return targetView().size();
    }

    public MapService<K, V>[] split(int n) {
        return (MapService<K, V>[]) new MapService[]{this};
    }

    public MapService<K, V> threadSafe() {
        return this;
    }

    public synchronized void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
        this.updatingThread = Thread.currentThread();
        try {
            target().update(action, view);
        } finally {
            this.updatingThread = null;
            this.immutable = cloneTarget();
        }
    }

    public Equality<? super V> valueComparator() {
        return targetView().valueComparator();
    }

    protected MapService<K, V> cloneTarget() {
        try {
            return target().clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Cannot happen since target is Cloneable.");
        }
    }

    protected MapService<K, V> targetView() {
        return (this.updatingThread == null || this.updatingThread != Thread.currentThread()) ? this.immutable : target();
    }

    protected final boolean updateInProgress() {
        return (this.updatingThread == Thread.currentThread());
    }

    private class IteratorImpl
            implements Iterator<Map.Entry<K, V>> {
        private final Iterator<Map.Entry<K, V>> targetIterator = AtomicMapImpl.this.targetView().iterator();
        private Map.Entry<K, V> current;

        private IteratorImpl() {
        }

        public boolean hasNext() {
            return this.targetIterator.hasNext();
        }

        public Map.Entry<K, V> next() {
            this.current = this.targetIterator.next();
            return this.current;
        }

        public void remove() {
            if (this.current == null) throw new IllegalStateException();
            AtomicMapImpl.this.remove(this.current.getKey());
            this.current = null;
        }
    }
}

