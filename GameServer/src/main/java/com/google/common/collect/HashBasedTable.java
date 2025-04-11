package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@GwtCompatible(serializable = true)
@Beta
public class HashBasedTable<R, C, V>
        extends StandardTable<R, C, V> {
    private static final long serialVersionUID = 0L;

    HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) {
        super(backingMap, factory);
    }

    public static <R, C, V> HashBasedTable<R, C, V> create() {
        return new HashBasedTable<R, C, V>(new HashMap<R, Map<C, V>>(), new Factory<C, V>(0));
    }

    public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
        Preconditions.checkArgument((expectedCellsPerRow >= 0));
        Map<R, Map<C, V>> backingMap = Maps.newHashMapWithExpectedSize(expectedRows);

        return new HashBasedTable<R, C, V>(backingMap, new Factory<C, V>(expectedCellsPerRow));
    }

    public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
        HashBasedTable<R, C, V> result = create();
        result.putAll(table);
        return result;
    }

    public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
        return super.contains(rowKey, columnKey);
    }

    public boolean containsColumn(@Nullable Object columnKey) {
        return super.containsColumn(columnKey);
    }

    public boolean containsRow(@Nullable Object rowKey) {
        return super.containsRow(rowKey);
    }

    public boolean containsValue(@Nullable Object value) {
        return super.containsValue(value);
    }

    public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
        return super.get(rowKey, columnKey);
    }

    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
        return super.remove(rowKey, columnKey);
    }

    private static class Factory<C, V>
            implements Supplier<Map<C, V>>, Serializable {
        private static final long serialVersionUID = 0L;
        final int expectedSize;

        Factory(int expectedSize) {
            this.expectedSize = expectedSize;
        }

        public Map<C, V> get() {
            return Maps.newHashMapWithExpectedSize(this.expectedSize);
        }
    }
}

