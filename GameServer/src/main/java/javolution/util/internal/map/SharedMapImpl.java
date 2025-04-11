package javolution.util.internal.map;

import javolution.util.function.Equality;
import javolution.util.internal.collection.ReadWriteLockImpl;
import javolution.util.service.MapService;

import java.util.Iterator;
import java.util.Map;

public class SharedMapImpl<K, V>
        extends MapView<K, V> {
    private static final long serialVersionUID = 1536L;
    protected ReadWriteLockImpl lock;
    protected transient Thread updatingThread;

    public SharedMapImpl(MapService<K, V> target) {
        this(target, new ReadWriteLockImpl());
    }

    public SharedMapImpl(MapService<K, V> target, ReadWriteLockImpl lock) {
        super(target);
        this.lock = lock;
    }

    public void clear() {
        this.lock.writeLock.lock();
        try {
            target().clear();
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean containsKey(Object key) {
        this.lock.readLock.lock();
        try {
            return target().containsKey(key);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public boolean containsValue(Object value) {
        this.lock.readLock.lock();
        try {
            return target().containsValue(value);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public V get(Object key) {
        this.lock.readLock.lock();
        try {
            return (V) target().get(key);
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public boolean isEmpty() {
        this.lock.readLock.lock();
        try {
            return target().isEmpty();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return new IteratorImpl();
    }

    public Equality<? super K> keyComparator() {
        return target().keyComparator();
    }

    public V put(K key, V value) {
        this.lock.writeLock.lock();
        try {
            return (V) target().put(key, value);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        this.lock.writeLock.lock();
        try {
            target().putAll(m);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public V putIfAbsent(K key, V value) {
        this.lock.writeLock.lock();
        try {
            return (V) target().putIfAbsent(key, value);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public V remove(Object key) {
        this.lock.writeLock.lock();
        try {
            return (V) target().remove(key);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean remove(Object key, Object value) {
        this.lock.writeLock.lock();
        try {
            return target().remove(key, value);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public V replace(K key, V value) {
        this.lock.writeLock.lock();
        try {
            return (V) target().replace(key, value);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public boolean replace(K key, V oldValue, V newValue) {
        this.lock.writeLock.lock();
        try {
            return target().replace(key, oldValue, newValue);
        } finally {
            this.lock.writeLock.unlock();
        }
    }

    public int size() {
        this.lock.readLock.lock();
        try {
            return target().size();
        } finally {
            this.lock.readLock.unlock();
        }
    }

    public MapService<K, V>[] split(int n) {
        MapService[] arrayOfMapService1;
        this.lock.readLock.lock();
        try {
            arrayOfMapService1 = (MapService[]) target().split(n);
        } finally {
            this.lock.readLock.unlock();
        }
        MapService[] arrayOfMapService2 = new MapService[arrayOfMapService1.length];
        for (int i = 0; i < arrayOfMapService1.length; i++) {
            arrayOfMapService2[i] = new SharedMapImpl(arrayOfMapService1[i], this.lock);
        }
        return (MapService<K, V>[]) arrayOfMapService2;
    }

    public MapService<K, V> threadSafe() {
        return this;
    }

    public Equality<? super V> valueComparator() {
        return target().valueComparator();
    }

    protected MapService<K, V> cloneTarget() {
        try {
            return target().clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Cannot happen since target is Cloneable.");
        }
    }

    private class IteratorImpl
            implements Iterator<Map.Entry<K, V>> {
        private final Iterator<Map.Entry<K, V>> targetIterator;
        private Map.Entry<K, V> next;

        public IteratorImpl() {
            SharedMapImpl.this.lock.readLock.lock();
            try {
                this.targetIterator = SharedMapImpl.this.cloneTarget().entrySet().iterator();
            } finally {
                SharedMapImpl.this.lock.readLock.unlock();
            }
        }

        public boolean hasNext() {
            return this.targetIterator.hasNext();
        }

        public Map.Entry<K, V> next() {
            this.next = this.targetIterator.next();
            return this.next;
        }

        public void remove() {
            if (this.next == null) throw new IllegalStateException();
            SharedMapImpl.this.remove(this.next.getKey());
            this.next = null;
        }
    }
}

