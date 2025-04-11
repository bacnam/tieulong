package javolution.util.internal.map.sorted;

import javolution.util.internal.map.MapView;
import javolution.util.internal.set.sorted.SharedSortedSetImpl;
import javolution.util.internal.set.sorted.SubSortedSetImpl;
import javolution.util.service.MapService;
import javolution.util.service.SetService;
import javolution.util.service.SortedMapService;
import javolution.util.service.SortedSetService;

import java.util.Comparator;
import java.util.Map;

public abstract class SortedMapView<K, V>
        extends MapView<K, V>
        implements SortedMapService<K, V> {
    private static final long serialVersionUID = 1536L;

    public SortedMapView(SortedMapService<K, V> target) {
        super((MapService) target);
    }

    public Comparator<? super K> comparator() {
        return (Comparator<? super K>) keyComparator();
    }

    public SortedSetService<Map.Entry<K, V>> entrySet() {
        return new EntrySortedSet();
    }

    public SortedMapService<K, V> headMap(K toKey) {
        return new SubSortedMapImpl<K, V>(this, firstKey(), toKey);
    }

    public SortedSetService<K> keySet() {
        return new KeySortedSet();
    }

    public SortedMapService<K, V> subMap(K fromKey, K toKey) {
        return new SubSortedMapImpl<K, V>(this, fromKey, toKey);
    }

    public SortedMapService<K, V> tailMap(K fromKey) {
        return new SubSortedMapImpl<K, V>(this, fromKey, lastKey());
    }

    public SortedMapService<K, V> threadSafe() {
        return new SharedSortedMapImpl<K, V>(this);
    }

    protected SortedMapService<K, V> target() {
        return (SortedMapService<K, V>) super.target();
    }

    public abstract K firstKey();

    public abstract K lastKey();

    protected class EntrySortedSet
            extends MapView<K, V>.EntrySet
            implements SortedSetService<Map.Entry<K, V>> {
        private static final long serialVersionUID = 1536L;

        protected EntrySortedSet() {
            super(SortedMapView.this);
        }

        public Map.Entry<K, V> first() {
            K key = (K) SortedMapView.this.firstKey();
            V value = (V) SortedMapView.this.get(key);
            return new MapEntryImpl<K, V>(key, value);
        }

        public SortedSetService<Map.Entry<K, V>> headSet(Map.Entry<K, V> toElement) {
            return (SortedSetService<Map.Entry<K, V>>) new SubSortedSetImpl(this, null, toElement);
        }

        public Map.Entry<K, V> last() {
            K key = (K) SortedMapView.this.lastKey();
            V value = (V) SortedMapView.this.get(key);
            return new MapEntryImpl<K, V>(key, value);
        }

        public SortedSetService<Map.Entry<K, V>> subSet(Map.Entry<K, V> fromElement, Map.Entry<K, V> toElement) {
            return (SortedSetService<Map.Entry<K, V>>) new SubSortedSetImpl(this, fromElement, toElement);
        }

        public SortedSetService<Map.Entry<K, V>> tailSet(Map.Entry<K, V> fromElement) {
            return (SortedSetService<Map.Entry<K, V>>) new SubSortedSetImpl(this, fromElement, null);
        }

        public SortedSetService<Map.Entry<K, V>> threadSafe() {
            return (SortedSetService<Map.Entry<K, V>>) new SharedSortedSetImpl((SetService) this);
        }
    }

    protected class KeySortedSet extends MapView<K, V>.KeySet implements SortedSetService<K> {
        private static final long serialVersionUID = 1536L;

        protected KeySortedSet() {
            super(SortedMapView.this);
        }

        public K first() {
            return (K) SortedMapView.this.firstKey();
        }

        public SortedSetService<K> headSet(K toElement) {
            return (SortedSetService<K>) new SubSortedSetImpl(this, null, toElement);
        }

        public K last() {
            return (K) SortedMapView.this.lastKey();
        }

        public SortedSetService<K> subSet(K fromElement, K toElement) {
            return (SortedSetService<K>) new SubSortedSetImpl(this, fromElement, toElement);
        }

        public SortedSetService<K> tailSet(K fromElement) {
            return (SortedSetService<K>) new SubSortedSetImpl(this, fromElement, null);
        }

        public SortedSetService<K> threadSafe() {
            return (SortedSetService<K>) new SharedSortedSetImpl((SetService) this);
        }
    }
}

