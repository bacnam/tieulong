package org.apache.mina.util;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashSet<E>
        extends MapBackedSet<E> {
    private static final long serialVersionUID = 8518578988740277828L;

    public ConcurrentHashSet() {
        super(new ConcurrentHashMap<E, Boolean>());
    }

    public ConcurrentHashSet(Collection<E> c) {
        super(new ConcurrentHashMap<E, Boolean>(), c);
    }

    public boolean add(E o) {
        Boolean answer = ((ConcurrentMap<E, Boolean>) this.map).putIfAbsent(o, Boolean.TRUE);
        return (answer == null);
    }
}

