package javolution.util.internal.map;

import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.function.Function;
import javolution.util.internal.collection.MappedCollectionImpl;
import javolution.util.internal.set.MappedSetImpl;
import javolution.util.internal.set.SetView;
import javolution.util.service.CollectionService;
import javolution.util.service.MapService;
import javolution.util.service.SetService;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class MapView<K, V>
        implements MapService<K, V> {
    private static final long serialVersionUID = 1536L;
    private MapService<K, V> target;

    public MapView(MapService<K, V> target) {
        this.target = target;
    }

    public void clear() {
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            it.remove();
        }
    }

    public MapView<K, V> clone() {
        try {
            MapView<K, V> copy = (MapView<K, V>) super.clone();
            if (this.target != null) {
                copy.target = this.target.clone();
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new Error("Should not happen since target is cloneable");
        }
    }

    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    public SetService<Map.Entry<K, V>> entrySet() {
        return (SetService<Map.Entry<K, V>>) new EntrySet();
    }

    public boolean isEmpty() {
        return iterator().hasNext();
    }

    public SetService<K> keySet() {
        return (SetService<K>) new KeySet();
    }

    public void perform(Consumer<MapService<K, V>> action, MapService<K, V> view) {
        if (this.target == null) {
            action.accept(view);
        } else {
            this.target.perform(action, view);
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        Iterator<?> it = m.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> e = (Map.Entry<K, V>) it.next();
            put(e.getKey(), e.getValue());
        }
    }

    public V putIfAbsent(K key, V value) {
        if (!containsKey(key)) return put(key, value);
        return get(key);
    }

    public boolean remove(Object key, Object value) {
        if (containsKey(key) && get(key).equals(value)) {
            remove(key);
            return true;
        }
        return false;
    }

    public V replace(K key, V value) {
        if (containsKey(key))
            return put(key, value);
        return null;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        if (containsKey(key) && get(key).equals(oldValue)) {
            put(key, newValue);
            return true;
        }
        return false;
    }

    public int size() {
        int count = 0;
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            count++;
            it.next();
        }
        return count;
    }

    public MapService<K, V>[] split(int n) {
        if (this.target == null) return (MapService<K, V>[]) new MapService[]{this};
        MapService[] arrayOfMapService1 = (MapService[]) this.target.split(n);
        MapService[] arrayOfMapService2 = new MapService[arrayOfMapService1.length];
        for (int i = 0; i < arrayOfMapService1.length; i++) {
            MapView<K, V> copy = clone();
            copy.target = arrayOfMapService1[i];
            arrayOfMapService2[i] = copy;
        }
        return (MapService<K, V>[]) arrayOfMapService2;
    }

    public MapService<K, V> threadSafe() {
        return new SharedMapImpl<K, V>(this);
    }

    public void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
        if (this.target == null) {
            action.accept(view);
        } else {
            this.target.update(action, view);
        }
    }

    public CollectionService<V> values() {
        return (CollectionService<V>) new Values();
    }

    protected MapService<K, V> target() {
        return this.target;
    }

    public abstract boolean containsKey(Object paramObject);

    public abstract V get(Object paramObject);

    public abstract Iterator<Map.Entry<K, V>> iterator();

    public abstract Equality<? super K> keyComparator();

    public abstract V put(K paramK, V paramV);

    public abstract V remove(Object paramObject);

    public abstract Equality<? super V> valueComparator();

    protected class EntryComparator
            implements Equality<Map.Entry<K, V>>, Serializable {
        private static final long serialVersionUID = 1536L;

        public boolean areEqual(Map.Entry<K, V> left, Map.Entry<K, V> right) {
            return MapView.this.keyComparator().areEqual(left.getKey(), right.getKey());
        }

        public int compare(Map.Entry<K, V> left, Map.Entry<K, V> right) {
            return MapView.this.keyComparator().compare(left.getKey(), right.getKey());
        }

        public int hashCodeOf(Map.Entry<K, V> e) {
            return MapView.this.keyComparator().hashCodeOf(e.getKey());
        }
    }

    protected class EntrySet extends SetView<Map.Entry<K, V>> {
        private static final long serialVersionUID = 1536L;

        public EntrySet() {
            super(null);
        }

        public boolean add(Map.Entry<K, V> entry) {
            MapView.this.put(entry.getKey(), entry.getValue());
            return true;
        }

        public Equality<? super Map.Entry<K, V>> comparator() {
            return new MapView.EntryComparator();
        }

        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry<K, V> e = (Map.Entry<K, V>) obj;
                return contains(e.getKey());
            }
            return false;
        }

        public boolean isEmpty() {
            return MapView.this.isEmpty();
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return MapView.this.iterator();
        }

        public void perform(final Consumer<CollectionService<Map.Entry<K, V>>> action, final CollectionService<Map.Entry<K, V>> view) {
            Consumer<MapService<K, V>> mapAction = new Consumer<MapService<K, V>>() {
                public void accept(MapService<K, V> param) {
                    action.accept(view);
                }
            };
            MapView.this.perform(mapAction, MapView.this);
        }

        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry<K, V> e = (Map.Entry<K, V>) obj;
                if (!contains(e.getKey())) return false;
                MapView.this.remove(e.getKey());
                return true;
            }
            return false;
        }

        public int size() {
            return MapView.this.size();
        }

        public CollectionService<Map.Entry<K, V>>[] split(int n) {
            MapService[] arrayOfMapService = MapView.this.split(n);
            CollectionService[] arrayOfCollectionService = new CollectionService[arrayOfMapService.length];
            for (int i = 0; i < arrayOfCollectionService.length; i++) {
                arrayOfCollectionService[i] = (CollectionService) arrayOfMapService[i].entrySet();
            }
            return (CollectionService<Map.Entry<K, V>>[]) arrayOfCollectionService;
        }

        public void update(final Consumer<CollectionService<Map.Entry<K, V>>> action, final CollectionService<Map.Entry<K, V>> view) {
            Consumer<MapService<K, V>> mapAction = new Consumer<MapService<K, V>>() {
                public void accept(MapService<K, V> param) {
                    action.accept(view);
                }
            };
            MapView.this.update(mapAction, MapView.this);
        }
    }

    protected class KeySet
            extends MappedSetImpl<Map.Entry<K, V>, K> {
        private static final long serialVersionUID = 1536L;

        public KeySet() {
            super(MapView.this.entrySet(), new Function<Map.Entry<K, V>, K>(MapView.this) {
                public K apply(Map.Entry<K, V> e) {
                    return e.getKey();
                }
            });
        }

        public boolean add(K key) {
            if (MapView.this.containsKey(key)) return false;
            MapView.this.put(key, null);
            return true;
        }

        public Equality<? super K> comparator() {
            return MapView.this.keyComparator();
        }

        public boolean contains(Object obj) {
            return MapView.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            if (!MapView.this.containsKey(obj)) return false;
            MapView.this.remove(obj);
            return true;
        }
    }

    protected class Values
            extends MappedCollectionImpl<Map.Entry<K, V>, V> {
        private static final long serialVersionUID = 1536L;

        public Values() {
            super((CollectionService) MapView.this.entrySet(), new Function<Map.Entry<K, V>, V>(MapView.this) {
                public V apply(Map.Entry<K, V> e) {
                    return e.getValue();
                }
            });
        }

        public Equality<? super V> comparator() {
            return MapView.this.valueComparator();
        }
    }
}

