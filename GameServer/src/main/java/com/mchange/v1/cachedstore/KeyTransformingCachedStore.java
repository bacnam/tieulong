package com.mchange.v1.cachedstore;

import com.mchange.v1.util.WrapperIterator;

import java.util.Iterator;

abstract class KeyTransformingCachedStore
        extends NoCleanupCachedStore {
    protected KeyTransformingCachedStore(CachedStore.Manager paramManager) {
        super(paramManager);
    }

    public Object getCachedValue(Object paramObject) {
        return this.cache.get(toCacheFetchKey(paramObject));
    }

    public void removeFromCache(Object paramObject) throws CachedStoreException {
        this.cache.remove(toCacheFetchKey(paramObject));
    }

    public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
        Object object = toCachePutKey(paramObject1);

        this.cache.put(object, paramObject2);
    }

    public Iterator cachedKeys() throws CachedStoreException {
        return (Iterator) new WrapperIterator(this.cache.keySet().iterator(), false) {
            public Object transformObject(Object param1Object) {
                Object object = KeyTransformingCachedStore.this.toUserKey(param1Object);
                return (object == null) ? SKIP_TOKEN : object;
            }
        };
    }

    protected Object toUserKey(Object paramObject) {
        return paramObject;
    }

    protected Object toCacheFetchKey(Object paramObject) {
        return toCachePutKey(paramObject);
    }

    protected Object toCachePutKey(Object paramObject) {
        return paramObject;
    }

    protected Object removeByTransformedKey(Object paramObject) {
        return this.cache.remove(paramObject);
    }
}

