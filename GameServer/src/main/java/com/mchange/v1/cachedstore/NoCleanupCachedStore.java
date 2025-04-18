package com.mchange.v1.cachedstore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class NoCleanupCachedStore
implements TweakableCachedStore
{
static final boolean DEBUG = true;
protected Map cache = new HashMap<Object, Object>();

CachedStore.Manager manager;

public NoCleanupCachedStore(CachedStore.Manager paramManager) {
this.manager = paramManager;
}

public Object find(Object paramObject) throws CachedStoreException {
try {
Object object = getCachedValue(paramObject);
if (object == null || this.manager.isDirty(paramObject, object)) {

object = this.manager.recreateFromKey(paramObject);
if (object != null)
setCachedValue(paramObject, object); 
} 
return object;
}
catch (CachedStoreException cachedStoreException) {
throw cachedStoreException;
} catch (Exception exception) {

exception.printStackTrace();
throw new CachedStoreException(exception);
} 
}

public Object getCachedValue(Object paramObject) {
return this.cache.get(paramObject);
}

public void removeFromCache(Object paramObject) throws CachedStoreException {
this.cache.remove(paramObject);
}

public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
this.cache.put(paramObject1, paramObject2);
}

public Iterator cachedKeys() throws CachedStoreException {
return this.cache.keySet().iterator();
}
public void reset() {
this.cache.clear();
}
}

