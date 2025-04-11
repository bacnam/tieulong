package javolution.util.internal.map.sorted;

import javolution.util.internal.map.AtomicMapImpl;
import javolution.util.service.MapService;
import javolution.util.service.SortedMapService;
import javolution.util.service.SortedSetService;

import java.util.Comparator;
import java.util.Map;

public class AtomicSortedMapImpl<K, V>
        extends AtomicMapImpl<K, V>
        implements SortedMapService<K, V> {
    private static final long serialVersionUID = 1536L;

    public AtomicSortedMapImpl(SortedMapService<K, V> target) {
        super((MapService) target);
    }

    public Comparator<? super K> comparator() {
        return (Comparator<? super K>) target().keyComparator();
    }

    public SortedSetService<Map.Entry<K, V>> entrySet() {
        return (new SubSortedMapImpl<K, V>(this, null, null)).entrySet();
    }

    public K firstKey() {
        return (K) targetView().firstKey();
    }

    public SortedMapService<K, V> headMap(K toKey) {
        return new SubSortedMapImpl<K, V>(this, null, toKey);
    }

    public SortedSetService<K> keySet() {
        return (new SubSortedMapImpl<K, Object>(this, null, null)).keySet();
    }

    public K lastKey() {
        return (K) targetView().lastKey();
    }

    public SortedMapService<K, V> subMap(K fromKey, K toKey) {
        return new SubSortedMapImpl<K, V>(this, fromKey, toKey);
    }

    public SortedMapService<K, V> tailMap(K fromKey) {
        return new SubSortedMapImpl<K, V>(this, fromKey, null);
    }

    public SortedMapService<K, V> threadSafe() {
        return this;
    }

    protected SortedMapService<K, V> targetView() {
        return (SortedMapService<K, V>) super.targetView();
    }
}

