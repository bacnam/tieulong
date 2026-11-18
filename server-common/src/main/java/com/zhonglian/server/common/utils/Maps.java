package com.zhonglian.server.common.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Maps {
    public static <K, V> Map<K, V> list2Map(Function<? super V, ? extends K> keyMapper, List<V> list) {
        return list.stream().collect(Collectors.toMap(keyMapper, p -> (V)p));
    }

    public static <K, V> Map<K, V> newMap() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> Map<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> Map<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }
}
