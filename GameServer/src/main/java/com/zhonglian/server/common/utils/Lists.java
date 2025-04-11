package com.zhonglian.server.common.utils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Lists {
    public static <E> List<E> newConcurrentList() {
        return new CopyOnWriteArrayList<>();
    }

    public static <E> List<E> newConcurrentList(Collection<? extends E> c) {
        return new CopyOnWriteArrayList<>(c);
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> ArrayList<E> newArrayList(Collection<? extends E> c) {
        return new ArrayList<>(c);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static <E> LinkedList<E> newLinkedList(Collection<? extends E> c) {
        return new LinkedList<>(c);
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<>();
    }

    public static <E> HashSet<E> newHashSet(Collection<? extends E> c) {
        return new HashSet<>(c);
    }
}

