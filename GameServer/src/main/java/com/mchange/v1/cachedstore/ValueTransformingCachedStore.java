package com.mchange.v1.cachedstore;

abstract class ValueTransformingCachedStore
extends NoCleanupCachedStore
{
protected ValueTransformingCachedStore(CachedStore.Manager paramManager) {
super(paramManager);
}
public Object getCachedValue(Object paramObject) {
return toUserValue(this.cache.get(paramObject));
}

public void removeFromCache(Object paramObject) throws CachedStoreException {
this.cache.remove(paramObject);
}

public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
this.cache.put(paramObject1, toCacheValue(paramObject2));
}
protected Object toUserValue(Object paramObject) {
return paramObject;
}
protected Object toCacheValue(Object paramObject) {
return paramObject;
}
}

