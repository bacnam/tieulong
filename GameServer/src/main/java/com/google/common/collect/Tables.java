package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
@Beta
public final class Tables {
    public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
        return new ImmutableCell<R, C, V>(rowKey, columnKey, value);
    }

    public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
        return (table instanceof TransposeTable) ? ((TransposeTable) table).original : new TransposeTable<C, R, V>(table);
    }

    public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
        Preconditions.checkArgument(backingMap.isEmpty());
        Preconditions.checkNotNull(factory);

        return new StandardTable<R, C, V>(backingMap, factory);
    }

    public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
        return new TransformedTable<R, C, V1, V2>(fromTable, function);
    }

    static final class ImmutableCell<R, C, V>
            extends AbstractCell<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0L;
        private final R rowKey;
        private final C columnKey;
        private final V value;

        ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.value = value;
        }

        public R getRowKey() {
            return this.rowKey;
        }

        public C getColumnKey() {
            return this.columnKey;
        }

        public V getValue() {
            return this.value;
        }
    }

    static abstract class AbstractCell<R, C, V>
            implements Table.Cell<R, C, V> {
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Table.Cell) {
                Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>) obj;
                return (Objects.equal(getRowKey(), other.getRowKey()) && Objects.equal(getColumnKey(), other.getColumnKey()) && Objects.equal(getValue(), other.getValue()));
            }

            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{getRowKey(), getColumnKey(), getValue()});
        }

        public String toString() {
            return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
        }
    }

    private static class TransposeTable<C, R, V>
            implements Table<C, R, V> {
        private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>() {
            public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell) {
                return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            }
        };
        final Table<R, C, V> original;
        CellSet cellSet;

        TransposeTable(Table<R, C, V> original) {
            this.original = (Table<R, C, V>) Preconditions.checkNotNull(original);
        }

        public void clear() {
            this.original.clear();
        }

        public Map<C, V> column(R columnKey) {
            return this.original.row(columnKey);
        }

        public Set<R> columnKeySet() {
            return this.original.rowKeySet();
        }

        public Map<R, Map<C, V>> columnMap() {
            return this.original.rowMap();
        }

        public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.contains(columnKey, rowKey);
        }

        public boolean containsColumn(@Nullable Object columnKey) {
            return this.original.containsRow(columnKey);
        }

        public boolean containsRow(@Nullable Object rowKey) {
            return this.original.containsColumn(rowKey);
        }

        public boolean containsValue(@Nullable Object value) {
            return this.original.containsValue(value);
        }

        public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.get(columnKey, rowKey);
        }

        public boolean isEmpty() {
            return this.original.isEmpty();
        }

        public V put(C rowKey, R columnKey, V value) {
            return this.original.put(columnKey, rowKey, value);
        }

        public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
            this.original.putAll(Tables.transpose(table));
        }

        public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
            return this.original.remove(columnKey, rowKey);
        }

        public Map<R, V> row(C rowKey) {
            return this.original.column(rowKey);
        }

        public Set<C> rowKeySet() {
            return this.original.columnKeySet();
        }

        public Map<C, Map<R, V>> rowMap() {
            return this.original.columnMap();
        }

        public int size() {
            return this.original.size();
        }

        public Collection<V> values() {
            return this.original.values();
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

        public Set<Table.Cell<C, R, V>> cellSet() {
            CellSet result = this.cellSet;
            return (result == null) ? (this.cellSet = new CellSet()) : result;
        }

        class CellSet
                extends Collections2.TransformedCollection<Table.Cell<R, C, V>, Table.Cell<C, R, V>>
                implements Set<Table.Cell<C, R, V>> {
            CellSet() {
                super(Tables.TransposeTable.this.original.cellSet(), (Function) Tables.TransposeTable.TRANSPOSE_CELL);
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set<?> os = (Set) obj;
                if (os.size() != size()) {
                    return false;
                }
                return containsAll(os);
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean contains(Object obj) {
                if (obj instanceof Table.Cell) {
                    Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                    return Tables.TransposeTable.this.original.cellSet().contains(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
                }

                return false;
            }

            public boolean remove(Object obj) {
                if (obj instanceof Table.Cell) {
                    Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                    return Tables.TransposeTable.this.original.cellSet().remove(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
                }

                return false;
            }
        }
    }

    private static class TransformedTable<R, C, V1, V2>
            implements Table<R, C, V2> {
        final Table<R, C, V1> fromTable;
        final Function<? super V1, V2> function;
        CellSet cellSet;
        Collection<V2> values;
        Map<R, Map<C, V2>> rowMap;
        Map<C, Map<R, V2>> columnMap;
        TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
            this.fromTable = (Table<R, C, V1>) Preconditions.checkNotNull(fromTable);
            this.function = (Function<? super V1, V2>) Preconditions.checkNotNull(function);
        }

        public boolean contains(Object rowKey, Object columnKey) {
            return this.fromTable.contains(rowKey, columnKey);
        }

        public boolean containsRow(Object rowKey) {
            return this.fromTable.containsRow(rowKey);
        }

        public boolean containsColumn(Object columnKey) {
            return this.fromTable.containsColumn(columnKey);
        }

        public boolean containsValue(Object value) {
            return values().contains(value);
        }

        public V2 get(Object rowKey, Object columnKey) {
            return contains(rowKey, columnKey) ? (V2) this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
        }

        public boolean isEmpty() {
            return this.fromTable.isEmpty();
        }

        public int size() {
            return this.fromTable.size();
        }

        public void clear() {
            this.fromTable.clear();
        }

        public V2 put(R rowKey, C columnKey, V2 value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
            throw new UnsupportedOperationException();
        }

        public V2 remove(Object rowKey, Object columnKey) {
            return contains(rowKey, columnKey) ? (V2) this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null;
        }

        public Map<C, V2> row(R rowKey) {
            return Maps.transformValues(this.fromTable.row(rowKey), this.function);
        }

        public Map<R, V2> column(C columnKey) {
            return Maps.transformValues(this.fromTable.column(columnKey), this.function);
        }

        Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
            return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>() {
                public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
                    return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), (V2) Tables.TransformedTable.this.function.apply(cell.getValue()));
                }
            };
        }

        public Set<Table.Cell<R, C, V2>> cellSet() {
            return (this.cellSet == null) ? (this.cellSet = new CellSet()) : this.cellSet;
        }

        public Set<R> rowKeySet() {
            return this.fromTable.rowKeySet();
        }

        public Set<C> columnKeySet() {
            return this.fromTable.columnKeySet();
        }

        public Collection<V2> values() {
            return (this.values == null) ? (this.values = Collections2.transform(this.fromTable.values(), this.function)) : this.values;
        }

        Map<R, Map<C, V2>> createRowMap() {
            Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>() {
                public Map<C, V2> apply(Map<C, V1> row) {
                    return Maps.transformValues(row, Tables.TransformedTable.this.function);
                }
            };
            return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
        }

        public Map<R, Map<C, V2>> rowMap() {
            return (this.rowMap == null) ? (this.rowMap = createRowMap()) : this.rowMap;
        }

        Map<C, Map<R, V2>> createColumnMap() {
            Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>() {
                public Map<R, V2> apply(Map<R, V1> column) {
                    return Maps.transformValues(column, Tables.TransformedTable.this.function);
                }
            };
            return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
        }

        public Map<C, Map<R, V2>> columnMap() {
            return (this.columnMap == null) ? (this.columnMap = createColumnMap()) : this.columnMap;
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

        class CellSet
                extends Collections2.TransformedCollection<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>
                implements Set<Table.Cell<R, C, V2>> {
            CellSet() {
                super(Tables.TransformedTable.this.fromTable.cellSet(), Tables.TransformedTable.this.cellFunction());
            }

            public boolean equals(Object obj) {
                return Sets.equalsImpl(this, obj);
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean contains(Object obj) {
                if (obj instanceof Table.Cell) {
                    Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                    if (!Objects.equal(cell.getValue(), Tables.TransformedTable.this.get(cell.getRowKey(), cell.getColumnKey()))) {
                        return false;
                    }
                    return (cell.getValue() != null || Tables.TransformedTable.this.fromTable.contains(cell.getRowKey(), cell.getColumnKey()));
                }

                return false;
            }

            public boolean remove(Object obj) {
                if (contains(obj)) {
                    Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) obj;
                    Tables.TransformedTable.this.fromTable.remove(cell.getRowKey(), cell.getColumnKey());
                    return true;
                }
                return false;
            }
        }
    }
}

