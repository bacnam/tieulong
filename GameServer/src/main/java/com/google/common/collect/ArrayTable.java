package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

@Beta
public final class ArrayTable<R, C, V>
        implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0L;
    private final ImmutableList<R> rowList;
    private final ImmutableList<C> columnList;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final V[][] array;
    private transient CellSet cellSet;
    private transient ColumnMap columnMap;
    private transient RowMap rowMap;
    private transient Collection<V> values;

    private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
        this.rowList = ImmutableList.copyOf(rowKeys);
        this.columnList = ImmutableList.copyOf(columnKeys);
        Preconditions.checkArgument(!this.rowList.isEmpty());
        Preconditions.checkArgument(!this.columnList.isEmpty());

        ImmutableMap.Builder<R, Integer> rowBuilder = ImmutableMap.builder();
        for (int i = 0; i < this.rowList.size(); i++) {
            rowBuilder.put(this.rowList.get(i), Integer.valueOf(i));
        }
        this.rowKeyToIndex = rowBuilder.build();

        ImmutableMap.Builder<C, Integer> columnBuilder = ImmutableMap.builder();
        for (int j = 0; j < this.columnList.size(); j++) {
            columnBuilder.put(this.columnList.get(j), Integer.valueOf(j));
        }
        this.columnKeyToIndex = columnBuilder.build();

        V[][] tmpArray = (V[][]) new Object[this.rowList.size()][this.columnList.size()];

        this.array = tmpArray;
    }

    private ArrayTable(Table<R, C, V> table) {
        this(table.rowKeySet(), table.columnKeySet());
        putAll(table);
    }

    private ArrayTable(ArrayTable<R, C, V> table) {
        this.rowList = table.rowList;
        this.columnList = table.columnList;
        this.rowKeyToIndex = table.rowKeyToIndex;
        this.columnKeyToIndex = table.columnKeyToIndex;

        V[][] copy = (V[][]) new Object[this.rowList.size()][this.columnList.size()];
        this.array = copy;
        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length);
        }
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
        return new ArrayTable<R, C, V>(rowKeys, columnKeys);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
        return new ArrayTable<R, C, V>(table);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(ArrayTable<R, C, V> table) {
        return new ArrayTable<R, C, V>(table);
    }

    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }

    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }

    public V at(int rowIndex, int columnIndex) {
        return this.array[rowIndex][columnIndex];
    }

    public V set(int rowIndex, int columnIndex, @Nullable V value) {
        V oldValue = this.array[rowIndex][columnIndex];
        this.array[rowIndex][columnIndex] = value;
        return oldValue;
    }

    public V[][] toArray(Class<V> valueClass) {
        V[][] copy = (V[][]) Array.newInstance(valueClass, new int[]{this.rowList.size(), this.columnList.size()});

        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
        }
        return copy;
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    public void eraseAll() {
        for (V[] row : this.array) {
            Arrays.fill((Object[]) row, (Object) null);
        }
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        return (containsRow(rowKey) && containsColumn(columnKey));
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        return this.columnKeyToIndex.containsKey(columnKey);
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return this.rowKeyToIndex.containsKey(rowKey);
    }

    public boolean containsValue(@Nullable Object value) {
        for (V[] row : this.array) {
            for (V element : row) {
                if (Objects.equal(value, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return getIndexed(rowIndex, columnIndex);
    }

    private V getIndexed(Integer rowIndex, Integer columnIndex) {
        return (rowIndex == null || columnIndex == null) ? null : this.array[rowIndex.intValue()][columnIndex.intValue()];
    }

    public boolean isEmpty() {
        return false;
    }

    public V put(R rowKey, C columnKey, @Nullable V value) {
        Preconditions.checkNotNull(rowKey);
        Preconditions.checkNotNull(columnKey);
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", new Object[]{rowKey, this.rowList});
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", new Object[]{columnKey, this.columnList});

        return set(rowIndex.intValue(), columnIndex.intValue(), value);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    @Deprecated
    public V remove(Object rowKey, Object columnKey) {
        throw new UnsupportedOperationException();
    }

    public V erase(@Nullable Object rowKey, @Nullable Object columnKey) {
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        if (rowIndex == null || columnIndex == null) {
            return null;
        }
        return set(rowIndex.intValue(), columnIndex.intValue(), null);
    }

    public int size() {
        return this.rowList.size() * this.columnList.size();
    }

    public boolean equals(@Nullable Object obj) {
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

    public Set<Table.Cell<R, C, V>> cellSet() {
        CellSet set = this.cellSet;
        return (set == null) ? (this.cellSet = new CellSet()) : set;
    }

    public Map<R, V> column(C columnKey) {
        Preconditions.checkNotNull(columnKey);
        Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return (columnIndex == null) ? ImmutableMap.<R, V>of() : new Column(columnIndex.intValue());
    }

    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
    }

    public Map<C, Map<R, V>> columnMap() {
        ColumnMap map = this.columnMap;
        return (map == null) ? (this.columnMap = new ColumnMap()) : map;
    }

    public Map<C, V> row(R rowKey) {
        Preconditions.checkNotNull(rowKey);
        Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        return (rowIndex == null) ? ImmutableMap.<C, V>of() : new Row(rowIndex.intValue());
    }

    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
    }

    public Map<R, Map<C, V>> rowMap() {
        RowMap map = this.rowMap;
        return (map == null) ? (this.rowMap = new RowMap()) : map;
    }

    public Collection<V> values() {
        Collection<V> v = this.values;
        return (v == null) ? (this.values = new Values()) : v;
    }

    private class CellSet extends AbstractSet<Table.Cell<R, C, V>> {
        private CellSet() {
        }

        public Iterator<Table.Cell<R, C, V>> iterator() {
            return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size()) {
                protected Table.Cell<R, C, V> get(final int index) {
                    return new Tables.AbstractCell<R, C, V>() {
                        final int rowIndex = index / ArrayTable.this.columnList.size();
                        final int columnIndex = index % ArrayTable.this.columnList.size();

                        public R getRowKey() {
                            return (R) ArrayTable.this.rowList.get(this.rowIndex);
                        }

                        public C getColumnKey() {
                            return (C) ArrayTable.this.columnList.get(this.columnIndex);
                        }

                        public V getValue() {
                            return (V) ArrayTable.this.array[this.rowIndex][this.columnIndex];
                        }
                    };
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }

        public boolean contains(Object obj) {
            if (obj instanceof Table.Cell) {
                Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                Integer rowIndex = (Integer) ArrayTable.this.rowKeyToIndex.get(cell.getRowKey());
                Integer columnIndex = (Integer) ArrayTable.this.columnKeyToIndex.get(cell.getColumnKey());
                return (rowIndex != null && columnIndex != null && Objects.equal(ArrayTable.this.array[rowIndex.intValue()][columnIndex.intValue()], cell.getValue()));
            }

            return false;
        }
    }

    private class Column extends AbstractMap<R, V> {
        final int columnIndex;
        ArrayTable<R, C, V>.ColumnEntrySet entrySet;

        Column(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public Set<Map.Entry<R, V>> entrySet() {
            ArrayTable<R, C, V>.ColumnEntrySet set = this.entrySet;
            return (set == null) ? (this.entrySet = new ArrayTable.ColumnEntrySet(this.columnIndex)) : set;
        }

        public V get(Object rowKey) {
            Integer rowIndex = (Integer) ArrayTable.this.rowKeyToIndex.get(rowKey);
            return ArrayTable.this.getIndexed(rowIndex, Integer.valueOf(this.columnIndex));
        }

        public boolean containsKey(Object rowKey) {
            return ArrayTable.this.rowKeyToIndex.containsKey(rowKey);
        }

        public V put(R rowKey, V value) {
            Preconditions.checkNotNull(rowKey);
            Integer rowIndex = (Integer) ArrayTable.this.rowKeyToIndex.get(rowKey);
            Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", new Object[]{rowKey, ArrayTable.access$200(this.this$0)});
            return (V) ArrayTable.this.set(rowIndex.intValue(), this.columnIndex, value);
        }

        public Set<R> keySet() {
            return ArrayTable.this.rowKeySet();
        }
    }

    private class ColumnEntrySet extends AbstractSet<Map.Entry<R, V>> {
        final int columnIndex;

        ColumnEntrySet(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public Iterator<Map.Entry<R, V>> iterator() {
            return new AbstractIndexedListIterator<Map.Entry<R, V>>(size()) {
                protected Map.Entry<R, V> get(final int rowIndex) {
                    return new AbstractMapEntry<R, V>() {
                        public R getKey() {
                            return (R) ArrayTable.this.rowList.get(rowIndex);
                        }

                        public V getValue() {
                            return (V) ArrayTable.this.array[rowIndex][ArrayTable.ColumnEntrySet.this.columnIndex];
                        }

                        public V setValue(V value) {
                            return (V) ArrayTable.this.set(rowIndex, ArrayTable.ColumnEntrySet.this.columnIndex, value);
                        }
                    };
                }
            };
        }

        public int size() {
            return ArrayTable.this.rowList.size();
        }
    }

    private class ColumnMap extends AbstractMap<C, Map<R, V>> {
        transient ArrayTable<R, C, V>.ColumnMapEntrySet entrySet;

        private ColumnMap() {
        }

        public Set<Map.Entry<C, Map<R, V>>> entrySet() {
            ArrayTable<R, C, V>.ColumnMapEntrySet set = this.entrySet;
            return (set == null) ? (this.entrySet = new ArrayTable.ColumnMapEntrySet()) : set;
        }

        public Map<R, V> get(Object columnKey) {
            Integer columnIndex = (Integer) ArrayTable.this.columnKeyToIndex.get(columnKey);
            return (columnIndex == null) ? null : new ArrayTable.Column(columnIndex.intValue());
        }

        public boolean containsKey(Object columnKey) {
            return ArrayTable.this.containsColumn(columnKey);
        }

        public Set<C> keySet() {
            return ArrayTable.this.columnKeySet();
        }

        public Map<R, V> remove(Object columnKey) {
            throw new UnsupportedOperationException();
        }
    }

    private class ColumnMapEntrySet extends AbstractSet<Map.Entry<C, Map<R, V>>> {
        private ColumnMapEntrySet() {
        }

        public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
            return new AbstractIndexedListIterator<Map.Entry<C, Map<R, V>>>(size()) {
                protected Map.Entry<C, Map<R, V>> get(int index) {
                    return Maps.immutableEntry((C) ArrayTable.this.columnList.get(index), new ArrayTable.Column(index));
                }
            };
        }

        public int size() {
            return ArrayTable.this.columnList.size();
        }
    }

    private class Row extends AbstractMap<C, V> {
        final int rowIndex;
        ArrayTable<R, C, V>.RowEntrySet entrySet;

        Row(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public Set<Map.Entry<C, V>> entrySet() {
            ArrayTable<R, C, V>.RowEntrySet set = this.entrySet;
            return (set == null) ? (this.entrySet = new ArrayTable.RowEntrySet(this.rowIndex)) : set;
        }

        public V get(Object columnKey) {
            Integer columnIndex = (Integer) ArrayTable.this.columnKeyToIndex.get(columnKey);
            return ArrayTable.this.getIndexed(Integer.valueOf(this.rowIndex), columnIndex);
        }

        public boolean containsKey(Object columnKey) {
            return ArrayTable.this.containsColumn(columnKey);
        }

        public V put(C columnKey, V value) {
            Preconditions.checkNotNull(columnKey);
            Integer columnIndex = (Integer) ArrayTable.this.columnKeyToIndex.get(columnKey);
            Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", new Object[]{columnKey, ArrayTable.access$100(this.this$0)});

            return (V) ArrayTable.this.set(this.rowIndex, columnIndex.intValue(), value);
        }

        public Set<C> keySet() {
            return ArrayTable.this.columnKeySet();
        }
    }

    private class RowEntrySet extends AbstractSet<Map.Entry<C, V>> {
        final int rowIndex;

        RowEntrySet(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public Iterator<Map.Entry<C, V>> iterator() {
            return new AbstractIndexedListIterator<Map.Entry<C, V>>(size()) {
                protected Map.Entry<C, V> get(final int columnIndex) {
                    return new AbstractMapEntry<C, V>() {
                        public C getKey() {
                            return (C) ArrayTable.this.columnList.get(columnIndex);
                        }

                        public V getValue() {
                            return (V) ArrayTable.this.array[ArrayTable.RowEntrySet.this.rowIndex][columnIndex];
                        }

                        public V setValue(V value) {
                            return (V) ArrayTable.this.set(ArrayTable.RowEntrySet.this.rowIndex, columnIndex, value);
                        }
                    };
                }
            };
        }

        public int size() {
            return ArrayTable.this.columnList.size();
        }
    }

    private class RowMap extends AbstractMap<R, Map<C, V>> {
        transient ArrayTable<R, C, V>.RowMapEntrySet entrySet;

        private RowMap() {
        }

        public Set<Map.Entry<R, Map<C, V>>> entrySet() {
            ArrayTable<R, C, V>.RowMapEntrySet set = this.entrySet;
            return (set == null) ? (this.entrySet = new ArrayTable.RowMapEntrySet()) : set;
        }

        public Map<C, V> get(Object rowKey) {
            Integer rowIndex = (Integer) ArrayTable.this.rowKeyToIndex.get(rowKey);
            return (rowIndex == null) ? null : new ArrayTable.Row(rowIndex.intValue());
        }

        public boolean containsKey(Object rowKey) {
            return ArrayTable.this.containsRow(rowKey);
        }

        public Set<R> keySet() {
            return ArrayTable.this.rowKeySet();
        }

        public Map<C, V> remove(Object rowKey) {
            throw new UnsupportedOperationException();
        }
    }

    private class RowMapEntrySet extends AbstractSet<Map.Entry<R, Map<C, V>>> {
        private RowMapEntrySet() {
        }

        public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
            return new AbstractIndexedListIterator<Map.Entry<R, Map<C, V>>>(size()) {
                protected Map.Entry<R, Map<C, V>> get(int index) {
                    return Maps.immutableEntry((R) ArrayTable.this.rowList.get(index), new ArrayTable.Row(index));
                }
            };
        }

        public int size() {
            return ArrayTable.this.rowList.size();
        }
    }

    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            return new AbstractIndexedListIterator<V>(size()) {
                protected V get(int index) {
                    int rowIndex = index / ArrayTable.this.columnList.size();
                    int columnIndex = index % ArrayTable.this.columnList.size();
                    return (V) ArrayTable.this.array[rowIndex][columnIndex];
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }

        public boolean contains(Object value) {
            return ArrayTable.this.containsValue(value);
        }
    }

}

