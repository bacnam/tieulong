package com.google.common.collect;

import com.google.common.annotations.Beta;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

@Deprecated
@Beta
public final class EvictionListeners {
    @Deprecated
    public static <K, V> MapEvictionListener<K, V> asynchronous(final MapEvictionListener<K, V> listener, final Executor executor) {
        return new MapEvictionListener<K, V>() {
            public void onEviction(@Nullable final K key, @Nullable final V value) {
                executor.execute(new Runnable() {
                    public void run() {
                        listener.onEviction(key, value);
                    }
                });
            }
        };
    }
}

