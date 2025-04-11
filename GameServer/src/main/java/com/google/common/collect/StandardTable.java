package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

@GwtCompatible
class StandardTable<R, C, V>
        implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0L;
    final Map<R, Map<C, V>> backingMap;
    final Supplier<? extends Map<C, V>> factory;
    private transient CellSet cellSet;
    private transient RowKeySet rowKeySet;
    private transient Set<C> columnKeySet;
    private transient Values values;
    private transient RowMap rowMap;
    private transient ColumnMap columnMap;

    StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
        this.backingMap = backingMap;
        this.factory = factory;
    }

    static <K, V> Iterator<K> keyIteratorImpl(Map<K, V> map) {
        final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
        return new Iterator<K>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public K next() {
                return (K) ((Map.Entry) entryIterator.next()).getKey();
            }

            public void remove() {
                entryIterator.remove();
            }
        };
    }

    static <K, V> Iterator<V> valueIteratorImpl(Map<K, V> map) {
        final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
        return new Iterator<V>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public V next() {
                return (V) ((Map.Entry) entryIterator.next()).getValue();
            }

            public void remove() {
                entryIterator.remove();
            }
        };
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return false;
        }
        Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
        return (map != null && Maps.safeContainsKey(map, columnKey));
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        if (columnKey == null) {
            return false;
        }
        for (Map<C, V> map : this.backingMap.values()) {
            if (Maps.safeContainsKey(map, columnKey)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
    }

    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        }
        for (Map<C, V> map : this.backingMap.values()) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return null;
        }
        Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
        return (map == null) ? null : Maps.<V>safeGet(map, columnKey);
    }

    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    public int size() {
        int size = 0;
        for (Map<C, V> map : this.backingMap.values()) {
            size += map.size();
        }
        return size;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Table) {
            Table<?, ?, ?> other = (Table<?, ?, ?>) obj;
            return cellSet().equals(other.cellSet());
        }
        return false;
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public String toString() {
        return rowMap().toString();
    }

    public void clear() {
        this.backingMap.clear();
    }

    private Map<C, V> getOrCreate(R rowKey) {
        Map<C, V> map = this.backingMap.get(rowKey);
        if (map == null) {
            map = (Map<C, V>) this.factory.get();
            this.backingMap.put(rowKey, map);
        }
        return map;
    }

    public V put(R rowKey, C columnKey, V value) {
        Preconditions.checkNotNull(rowKey);
        Preconditions.checkNotNull(columnKey);
        Preconditions.checkNotNull(value);
        return getOrCreate(rowKey).put(columnKey, value);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return null;
        }
        Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
        if (map == null) {
            return null;
        }
        V value = map.remove(columnKey);
        if (map.isEmpty()) {
            this.backingMap.remove(rowKey);
        }
        return value;
    }

    private Map<R, V> removeColumn(Object column) {
        Map<R, V> output = new LinkedHashMap<R, V>();
        Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<R, Map<C, V>> entry = iterator.next();
            V value = (V) ((Map) entry.getValue()).remove(column);
            if (value != null) {
                output.put(entry.getKey(), value);
                if (((Map) entry.getValue()).isEmpty()) {
                    iterator.remove();
                }
            }
        }
        return output;
    }

    private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
        return (value != null && value.equals(get(rowKey, columnKey)));
    }

    private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
        if (containsMapping(rowKey, columnKey, value)) {
            remove(rowKey, columnKey);
            return true;
        }
        return false;
    }

    public Set<Table.Cell<R, C, V>> cellSet() {
        CellSet result = this.cellSet;
        return (result == null) ? (this.cellSet = new CellSet()) : result;
    }

    public Map<C, V> row(R rowKey) {
        return new Row(rowKey);
    }

    public Map<R, V> column(C columnKey) {
        return new Column(columnKey);
    }

    public Set<R> rowKeySet() {
        Set<R> result = this.rowKeySet;
        return (result == null) ? (this.rowKeySet = new RowKeySet()) : result;
    }

    public Set<C> columnKeySet() {
        Set<C> result = this.columnKeySet;
        return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
    }

    Iterator<C> createColumnKeyIterator() {
        return new ColumnKeyIterator();
    }

    public Collection<V> values() {
        Values result = this.values;
        return (result == null) ? (this.values = new Values()) : result;
    }

    public Map<R, Map<C, V>> rowMap() {
        RowMap result = this.rowMap;
        return (result == null) ? (this.rowMap = new RowMap()) : result;
    }

    public Map<C, Map<R, V>> columnMap() {
        ColumnMap result = this.columnMap;
        return (result == null) ? (this.columnMap = new ColumnMap()) : result;
    }

    private abstract class TableCollection<T>
            extends AbstractCollection<T> {
        private TableCollection() {
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }

    private abstract class TableSet<T>
            extends AbstractSet<T> {
        private TableSet() {
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }

    private class CellSet extends TableSet<Table.Cell<R, C, V>> {
        private CellSet() {
        }

        public Iterator<Table.Cell<R, C, V>> iterator() {
            return new StandardTable.CellIterator();
        }

        public int size() {
            return StandardTable.this.size();
        }

        public boolean contains(Object obj) {
            if (obj instanceof Table.Cell) {
                Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                return StandardTable.this.containsMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }

            return false;
        }

        public boolean remove(Object obj) {
            if (obj instanceof Table.Cell) {
                Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                return StandardTable.this.removeMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }

            return false;
        }
    }

    private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
        final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();

        Map.Entry<R, Map<C, V>> rowEntry;
        Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();

        private CellIterator() {
        }

        public boolean hasNext() {
            return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
        }

        public Table.Cell<R, C, V> next() {
            if (!this.columnIterator.hasNext()) {
                this.rowEntry = this.rowIterator.next();
                this.columnIterator = ((Map<C, V>) this.rowEntry.getValue()).entrySet().iterator();
            }
            Map.Entry<C, V> columnEntry = this.columnIterator.next();
            return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
        }

        public void remove() {
            this.columnIterator.remove();
            if (((Map) this.rowEntry.getValue()).isEmpty())
                this.rowIterator.remove();
        }
    }

    class Row extends AbstractMap<C, V> {
        final R rowKey;
        Map<C, V> backingRowMap;
        Set<C> keySet;
        Set<Map.Entry<C, V>> entrySet;
        Row(R rowKey) {
            this.rowKey = (R) Preconditions.checkNotNull(rowKey);
        }

        Map<C, V> backingRowMap() {
            return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this.backingRowMap = computeBackingRowMap()) : this.backingRowMap;
        }

        Map<C, V> computeBackingRowMap() {
            return (Map<C, V>) StandardTable.this.backingMap.get(this.rowKey);
        }

        void maintainEmptyInvariant() {
            if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
                StandardTable.this.backingMap.remove(this.rowKey);
                this.backingRowMap = null;
            }
        }

        public boolean containsKey(Object key) {
            Map<C, V> backingRowMap = backingRowMap();
            return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
        }

        public V get(Object key) {
            Map<C, V> backingRowMap = backingRowMap();
            return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
        }

        public V put(C key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
                return this.backingRowMap.put(key, value);
            }
            return StandardTable.this.put(this.rowKey, key, value);
        }

        public V remove(Object key) {
            try {
                Map<C, V> backingRowMap = backingRowMap();
                if (backingRowMap == null) {
                    return null;
                }
                V result = backingRowMap.remove(key);
                maintainEmptyInvariant();
                return result;
            } catch (ClassCastException e) {
                return null;
            }
        }

        public void clear() {
            Map<C, V> backingRowMap = backingRowMap();
            if (backingRowMap != null) {
                backingRowMap.clear();
            }
            maintainEmptyInvariant();
        }

        public Set<C> keySet() {
            Set<C> result = this.keySet;
            if (result == null) {
                return this.keySet = new Maps.KeySet<C, V>() {
                    Map<C, V> map() {
                        return StandardTable.Row.this;
                    }
                };
            }
            return result;
        }

        public Set<Map.Entry<C, V>> entrySet() {
            Set<Map.Entry<C, V>> result = this.entrySet;
            if (result == null) {
                return this.entrySet = new RowEntrySet();
            }
            return result;
        }

        private class RowEntrySet extends Maps.EntrySet<C, V> {
            private RowEntrySet() {
            }

            Map<C, V> map() {
                return StandardTable.Row.this;
            }

            public int size() {
                Map<C, V> map = StandardTable.Row.this.backingRowMap();
                return (map == null) ? 0 : map.size();
            }

            public Iterator<Map.Entry<C, V>> iterator() {
                Map<C, V> map = StandardTable.Row.this.backingRowMap();
                if (map == null) {
                    return Iterators.emptyModifiableIterator();
                }
                final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
                return new Iterator<Map.Entry<C, V>>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Map.Entry<C, V> next() {
                        final Map.Entry<C, V> entry = iterator.next();
                        return new ForwardingMapEntry<C, V>() {
                            protected Map.Entry<C, V> delegate() {
                                return entry;
                            }

                            public V setValue(V value) {
                                return super.setValue((V) Preconditions.checkNotNull(value));
                            }

                            public boolean equals(Object object) {
                                return standardEquals(object);
                            }
                        };
                    }

                    public void remove() {
                        iterator.remove();
                        StandardTable.Row.this.maintainEmptyInvariant();
                    }
                };
            }
        }
    }

    private class Column extends Maps.ImprovedAbstractMap<R, V> {
        final C columnKey;
        Values columnValues;
        KeySet keySet;

        Column(C columnKey) {
            this.columnKey = (C) Preconditions.checkNotNull(columnKey);
        }

        public V put(R key, V value) {
            return StandardTable.this.put(key, this.columnKey, value);
        }

        public V get(Object key) {
            return (V) StandardTable.this.get(key, this.columnKey);
        }

        public boolean containsKey(Object key) {
            return StandardTable.this.contains(key, this.columnKey);
        }

        public V remove(Object key) {
            return (V) StandardTable.this.remove(key, this.columnKey);
        }

        public Set<Map.Entry<R, V>> createEntrySet() {
            return new EntrySet();
        }

        public Collection<V> values() {
            Values result = this.columnValues;
            return (result == null) ? (this.columnValues = new Values()) : result;
        }

        boolean removePredicate(Predicate<? super Map.Entry<R, V>> predicate) {
            boolean changed = false;
            Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<R, Map<C, V>> entry = iterator.next();
                Map<C, V> map = entry.getValue();
                V value = map.get(this.columnKey);
                if (value != null && predicate.apply(new ImmutableEntry<Object, V>(entry.getKey(), value))) {

                    map.remove(this.columnKey);
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public Set<R> keySet() {
            KeySet result = this.keySet;
            return (result == null) ? (this.keySet = new KeySet()) : result;
        }

        class EntrySet extends AbstractSet<Map.Entry<R, V>> {
            public Iterator<Map.Entry<R, V>> iterator() {
                return new StandardTable.Column.EntrySetIterator();
            }

            public int size() {
                int size = 0;
                for (Map<C, V> map : (Iterable<Map<C, V>>) StandardTable.this.backingMap.values()) {
                    if (map.containsKey(StandardTable.Column.this.columnKey)) {
                        size++;
                    }
                }
                return size;
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
            }

            public void clear() {
                Predicate<Map.Entry<R, V>> predicate = Predicates.alwaysTrue();
                StandardTable.Column.this.removePredicate(predicate);
            }

            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
                }
                return false;
            }

            public boolean remove(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
                }
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                boolean changed = false;
                for (Object obj : c) {
                    changed |= remove(obj);
                }
                return changed;
            }

            public boolean retainAll(Collection<?> c) {
                return StandardTable.Column.this.removePredicate(Predicates.not(Predicates.in(c)));
            }
        }

        class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
            final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();

            protected Map.Entry<R, V> computeNext() {
                while (this.iterator.hasNext()) {
                    final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
                    if (((Map) entry.getValue()).containsKey(StandardTable.Column.this.columnKey)) {
                        return new AbstractMapEntry<R, V>() {
                            public R getKey() {
                                return (R) entry.getKey();
                            }

                            public V getValue() {
                                return (V) ((Map) entry.getValue()).get(StandardTable.Column.this.columnKey);
                            }

                            public V setValue(V value) {
                                return ((Map<C, V>) entry.getValue()).put(StandardTable.Column.this.columnKey, (V) Preconditions.checkNotNull(value));
                            }
                        };
                    }
                }
                return endOfData();
            }
        }

        class KeySet extends AbstractSet<R> {
            public Iterator<R> iterator() {
                return StandardTable.keyIteratorImpl(StandardTable.Column.this);
            }

            public int size() {
                return StandardTable.Column.this.entrySet().size();
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
            }

            public boolean contains(Object obj) {
                return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
            }

            public boolean remove(Object obj) {
                return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
            }

            public void clear() {
                StandardTable.Column.this.entrySet().clear();
            }

            public boolean removeAll(Collection<?> c) {
                boolean changed = false;
                for (Object obj : c) {
                    changed |= remove(obj);
                }
                return changed;
            }

            public boolean retainAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>() {
                    public boolean apply(Map.Entry<R, V> entry) {
                        return !c.contains(entry.getKey());
                    }
                };
                return StandardTable.Column.this.removePredicate(predicate);
            }
        }

        class Values extends AbstractCollection<V> {
            public Iterator<V> iterator() {
                return StandardTable.valueIteratorImpl(StandardTable.Column.this);
            }

            public int size() {
                return StandardTable.Column.this.entrySet().size();
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
            }

            public void clear() {
                StandardTable.Column.this.entrySet().clear();
            }

            public boolean remove(Object obj) {
                if (obj == null) {
                    return false;
                }
                Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
                while (iterator.hasNext()) {
                    Map<C, V> map = iterator.next();
                    if (map.entrySet().remove(new ImmutableEntry<C, Object>(StandardTable.Column.this.columnKey, obj))) {

                        if (map.isEmpty()) {
                            iterator.remove();
                        }
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>() {
                    public boolean apply(Map.Entry<R, V> entry) {
                        return c.contains(entry.getValue());
                    }
                };
                return StandardTable.Column.this.removePredicate(predicate);
            }

            public boolean retainAll(final Collection<?> c) {
                Preconditions.checkNotNull(c);
                Predicate<Map.Entry<R, V>> predicate = new Predicate<Map.Entry<R, V>>() {
                    public boolean apply(Map.Entry<R, V> entry) {
                        return !c.contains(entry.getValue());
                    }
                };
                return StandardTable.Column.this.removePredicate(predicate);
            }
        }
    }

    class RowKeySet extends TableSet<R> {
        public Iterator<R> iterator() {
            return StandardTable.keyIteratorImpl(StandardTable.this.rowMap());
        }

        public int size() {
            return StandardTable.this.backingMap.size();
        }

        public boolean contains(Object obj) {
            return StandardTable.this.containsRow(obj);
        }

        public boolean remove(Object obj) {
            return (obj != null && StandardTable.this.backingMap.remove(obj) != null);
        }
    }

    private class ColumnKeySet extends TableSet<C> {
        private ColumnKeySet() {
        }

        public Iterator<C> iterator() {
            return StandardTable.this.createColumnKeyIterator();
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public boolean remove(Object obj) {
            if (obj == null) {
                return false;
            }
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = iterator.next();
                if (map.keySet().remove(obj)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean removeAll(Collection<?> c) {
            Preconditions.checkNotNull(c);
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = iterator.next();

                if (Iterators.removeAll(map.keySet().iterator(), c)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean retainAll(Collection<?> c) {
            Preconditions.checkNotNull(c);
            boolean changed = false;
            Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                Map<C, V> map = iterator.next();
                if (map.keySet().retainAll(c)) {
                    changed = true;
                    if (map.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
            return changed;
        }

        public boolean contains(Object obj) {
            if (obj == null) {
                return false;
            }
            for (Map<C, V> map : (Iterable<Map<C, V>>) StandardTable.this.backingMap.values()) {
                if (map.containsKey(obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class ColumnKeyIterator
            extends AbstractIterator<C> {
        final Map<C, V> seen = (Map<C, V>) StandardTable.this.factory.get();
        final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
        Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();

        private ColumnKeyIterator() {
        }

        protected C computeNext() {
            while (true) {
                while (this.entryIterator.hasNext()) {
                    Map.Entry<C, V> entry = this.entryIterator.next();
                    if (!this.seen.containsKey(entry.getKey())) {
                        this.seen.put(entry.getKey(), entry.getValue());
                        return entry.getKey();
                    }
                }
                if (this.mapIterator.hasNext()) {
                    this.entryIterator = ((Map<C, V>) this.mapIterator.next()).entrySet().iterator();
                    continue;
                }
                break;
            }
            return endOfData();
        }
    }

    private class Values extends TableCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            final Iterator<Table.Cell<R, C, V>> cellIterator = StandardTable.this.cellSet().iterator();
            return new Iterator<V>() {
                public boolean hasNext() {
                    return cellIterator.hasNext();
                }

                public V next() {
                    return (V) ((Table.Cell) cellIterator.next()).getValue();
                }

                public void remove() {
                    cellIterator.remove();
                }
            };
        }

        public int size() {
            return StandardTable.this.size();
        }
    }

    class RowMap extends Maps.ImprovedAbstractMap<R, Map<C, V>> {
        public boolean containsKey(Object key) {
            return StandardTable.this.containsRow(key);
        }

        public Map<C, V> get(Object key) {
            return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
        }

        public Set<R> keySet() {
            return StandardTable.this.rowKeySet();
        }

        public Map<C, V> remove(Object key) {
            return (key == null) ? null : (Map<C, V>) StandardTable.this.backingMap.remove(key);
        }

        protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
            return new EntrySet();
        }

        class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
            public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
                return new StandardTable.RowMap.EntryIterator();
            }

            public int size() {
                return StandardTable.this.backingMap.size();
            }

            public boolean contains(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    return (entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
                }

                return false;
            }

            public boolean remove(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    return (entry.getKey() != null && entry.getValue() instanceof Map && StandardTable.this.backingMap.entrySet().remove(entry));
                }

                return false;
            }
        }

        class EntryIterator implements Iterator<Map.Entry<R, Map<C, V>>> {
            final Iterator<R> delegate = StandardTable.this.backingMap.keySet().iterator();

            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            public Map.Entry<R, Map<C, V>> next() {
                R rowKey = this.delegate.next();
                return new ImmutableEntry<R, Map<C, V>>(rowKey, StandardTable.this.row(rowKey));
            }

            public void remove() {
                this.delegate.remove();
            }
        }
    }

    private class ColumnMap extends Maps.ImprovedAbstractMap<C, Map<R, V>> {
        ColumnMapValues columnMapValues;

        private ColumnMap() {
        }

        public Map<R, V> get(Object key) {
            return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
        }

        public boolean containsKey(Object key) {
            return StandardTable.this.containsColumn(key);
        }

        public Map<R, V> remove(Object key) {
            return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
        }

        public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
            return new ColumnMapEntrySet();
        }

        public Set<C> keySet() {
            return StandardTable.this.columnKeySet();
        }

        public Collection<Map<R, V>> values() {
            ColumnMapValues result = this.columnMapValues;
            return (result == null) ? (this.columnMapValues = new ColumnMapValues()) : result;
        }

        class ColumnMapEntrySet
                extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
            public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
                final Iterator<C> columnIterator = StandardTable.this.columnKeySet().iterator();
                return new UnmodifiableIterator<Map.Entry<C, Map<R, V>>>() {
                    public boolean hasNext() {
                        return columnIterator.hasNext();
                    }

                    public Map.Entry<C, Map<R, V>> next() {
                        C columnKey = columnIterator.next();
                        return new ImmutableEntry<C, Map<R, V>>(columnKey, StandardTable.this.column(columnKey));
                    }
                };
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }

            public boolean contains(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    if (StandardTable.this.containsColumn(entry.getKey())) {

                        C columnKey = (C) entry.getKey();
                        return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
                    }
                }
                return false;
            }

            public boolean remove(Object obj) {
                if (contains(obj)) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                    StandardTable.this.removeColumn(entry.getKey());
                    return true;
                }
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                boolean changed = false;
                for (Object obj : c) {
                    changed |= remove(obj);
                }
                return changed;
            }

            public boolean retainAll(Collection<?> c) {
                boolean changed = false;
                for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
                    if (!c.contains(new ImmutableEntry<C, Object>(columnKey, StandardTable.this.column(columnKey)))) {

                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }
        }

        private class ColumnMapValues extends StandardTable<R, C, V>.TableCollection<Map<R, V>> {
            private ColumnMapValues() {
            }

            public Iterator<Map<R, V>> iterator() {
                return StandardTable.valueIteratorImpl(StandardTable.ColumnMap.this);
            }

            public boolean remove(Object obj) {
                for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
                    if (((Map) entry.getValue()).equals(obj)) {
                        StandardTable.this.removeColumn(entry.getKey());
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> c) {
                Preconditions.checkNotNull(c);
                boolean changed = false;
                for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
                    if (c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }

            public boolean retainAll(Collection<?> c) {
                Preconditions.checkNotNull(c);
                boolean changed = false;
                for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
                    if (!c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }
        }
    }
}

