package org.apache.mina.util;

import java.util.Collection;
import java.util.IdentityHashMap;

public class IdentityHashSet<E>
        extends MapBackedSet<E> {
    private static final long serialVersionUID = 6948202189467167147L;

    public IdentityHashSet() {
        super(new IdentityHashMap<E, Boolean>());
    }

    public IdentityHashSet(int expectedMaxSize) {
        super(new IdentityHashMap<E, Boolean>(expectedMaxSize));
    }

    public IdentityHashSet(Collection<E> c) {
        super(new IdentityHashMap<E, Boolean>(), c);
    }
}

