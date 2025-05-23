package com.mchange.v1.cachedstore;

import com.mchange.v1.util.IteratorUtils;
import java.util.Iterator;

class NoCacheCachedStore
implements TweakableCachedStore
{
CachedStore.Manager mgr;

NoCacheCachedStore(CachedStore.Manager paramManager) {
this.mgr = paramManager;
}
public Object find(Object paramObject) throws CachedStoreException {
try {
return this.mgr.recreateFromKey(paramObject);
} catch (Exception exception) {

exception.printStackTrace();
throw CachedStoreUtils.toCachedStoreException(exception);
} 
}

public void reset() {}

public Object getCachedValue(Object paramObject) {
return null;
}

public void removeFromCache(Object paramObject) {}

public void setCachedValue(Object paramObject1, Object paramObject2) {}

public Iterator cachedKeys() {
return IteratorUtils.EMPTY_ITERATOR;
}
}

