package com.google.common.collect;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;

@Deprecated
@Beta
public interface MapEvictionListener<K, V> {
    void onEviction(@Nullable K paramK, @Nullable V paramV);
}

