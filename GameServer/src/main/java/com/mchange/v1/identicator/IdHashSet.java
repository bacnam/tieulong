package com.mchange.v1.identicator;

import com.mchange.v1.util.WrapperIterator;

import java.util.*;

public class IdHashSet
        extends AbstractSet
        implements Set {
    HashSet inner;
    Identicator id;

    private IdHashSet(HashSet paramHashSet, Identicator paramIdenticator) {
        this.inner = paramHashSet;
        this.id = paramIdenticator;
    }

    public IdHashSet(Identicator paramIdenticator) {
        this(new HashSet(), paramIdenticator);
    }

    public IdHashSet(Collection paramCollection, Identicator paramIdenticator) {
        this(new HashSet(2 * paramCollection.size()), paramIdenticator);
    }

    public IdHashSet(int paramInt, float paramFloat, Identicator paramIdenticator) {
        this(new HashSet(paramInt, paramFloat), paramIdenticator);
    }

    public IdHashSet(int paramInt, Identicator paramIdenticator) {
        this(new HashSet(paramInt, 0.75F), paramIdenticator);
    }

    public Iterator iterator() {
        return (Iterator) new WrapperIterator(this.inner.iterator(), true) {
            protected Object transformObject(Object param1Object) {
                IdHashKey idHashKey = (IdHashKey) param1Object;
                return idHashKey.getKeyObj();
            }
        };
    }

    public int size() {
        return this.inner.size();
    }

    public boolean contains(Object paramObject) {
        return this.inner.contains(createKey(paramObject));
    }

    public boolean add(Object paramObject) {
        return this.inner.add(createKey(paramObject));
    }

    public boolean remove(Object paramObject) {
        return this.inner.remove(createKey(paramObject));
    }

    public void clear() {
        this.inner.clear();
    }

    private IdHashKey createKey(Object paramObject) {
        return new StrongIdHashKey(paramObject, this.id);
    }
}

