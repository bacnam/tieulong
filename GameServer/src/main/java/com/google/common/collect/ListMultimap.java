package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
    List<V> get(@Nullable K paramK);

    List<V> removeAll(@Nullable Object paramObject);

    List<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);

    Map<K, Collection<V>> asMap();

    boolean equals(@Nullable Object paramObject);
}

