package com.mchange.v1.cachedstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class SimpleWritableCachedStore
        implements WritableCachedStore {
    private static final Object REMOVE_TOKEN = new Object();

    TweakableCachedStore readOnlyCache;

    WritableCachedStore.Manager manager;
    HashMap writeCache = new HashMap<Object, Object>();

    Set failedWrites = null;

    SimpleWritableCachedStore(TweakableCachedStore paramTweakableCachedStore, WritableCachedStore.Manager paramManager) {
        this.readOnlyCache = paramTweakableCachedStore;
        this.manager = paramManager;
    }

    public Object find(Object paramObject) throws CachedStoreException {
        Object object = this.writeCache.get(paramObject);
        if (object == null)
            object = this.readOnlyCache.find(paramObject);
        return (object == REMOVE_TOKEN) ? null : object;
    }

    public void write(Object paramObject1, Object paramObject2) {
        this.writeCache.put(paramObject1, paramObject2);
    }

    public void remove(Object paramObject) {
        write(paramObject, REMOVE_TOKEN);
    }

    public void flushWrites() throws CacheFlushException {
        HashMap hashMap = (HashMap) this.writeCache.clone();
        for (Object object : hashMap.keySet()) {

            Object object1 = hashMap.get(object);

            try {
                if (object1 == REMOVE_TOKEN) {
                    this.manager.removeFromStorage(object);
                } else {
                    this.manager.writeToStorage(object, object1);
                }

                try {
                    this.readOnlyCache.setCachedValue(object, object1);
                    this.writeCache.remove(object);
                    if (this.failedWrites != null) {

                        this.failedWrites.remove(object);
                        if (this.failedWrites.size() == 0) {
                            this.failedWrites = null;
                        }
                    }
                } catch (CachedStoreException cachedStoreException) {

                    throw new CachedStoreError("SimpleWritableCachedStore: Internal cache is broken!");
                }

            } catch (Exception exception) {

                if (this.failedWrites == null)
                    this.failedWrites = new HashSet();
                this.failedWrites.add(object);
            }
        }

        if (this.failedWrites != null)
            throw new CacheFlushException("Some keys failed to write!");
    }

    public Set getFailedWrites() {
        return (this.failedWrites == null) ? null : Collections.unmodifiableSet(this.failedWrites);
    }

    public void clearPendingWrites() {
        this.writeCache.clear();
        this.failedWrites = null;
    }

    public void reset() throws CachedStoreException {
        this.writeCache.clear();
        this.readOnlyCache.reset();
        this.failedWrites = null;
    }

    public void sync() throws CachedStoreException {
        flushWrites();
        reset();
    }
}

