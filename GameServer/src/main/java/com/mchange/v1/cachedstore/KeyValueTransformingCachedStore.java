package com.mchange.v1.cachedstore;

import com.mchange.v1.util.WrapperIterator;

import java.util.Iterator;

abstract class KeyValueTransformingCachedStore
        extends ValueTransformingCachedStore {
    protected KeyValueTransformingCachedStore(CachedStore.Manager paramManager) {
        super(paramManager);
    }

    public Object getCachedValue(Object paramObject) {
        return toUserValue(this.cache.get(toCacheFetchKey(paramObject)));
    }

    public void clearCachedValue(Object paramObject) throws CachedStoreException {
        this.cache.remove(toCacheFetchKey(paramObject));
    }

    public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
        this.cache.put(toCachePutKey(paramObject1), toCacheValue(paramObject2));
    }

    public Iterator cachedKeys() throws CachedStoreException {
        return (Iterator) new WrapperIterator(this.cache.keySet().iterator(), false) {
            public Object transformObject(Object param1Object) {
                Object object = KeyValueTransformingCachedStore.this.toUserKey(param1Object);
                return (object == null) ? SKIP_TOKEN : object;
            }
        };
    }

    protected Object toUserKey(Object paramObject) {
        return paramObject;
    }

    protected Object toCacheFetchKey(Object paramObject) {
        return paramObject;
    }

    protected Object toCachePutKey(Object paramObject) {
        return paramObject;
    }
}

