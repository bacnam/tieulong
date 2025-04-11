package com.mysql.jdbc.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache
        extends LinkedHashMap<Object, Object> {
    private static final long serialVersionUID = 1L;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
        return (size() > this.maxElements);
    }
}

