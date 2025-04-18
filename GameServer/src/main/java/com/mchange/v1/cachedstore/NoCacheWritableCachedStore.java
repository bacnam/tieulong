package com.mchange.v1.cachedstore;

import java.util.Collections;
import java.util.Set;

class NoCacheWritableCachedStore
implements WritableCachedStore, Autoflushing
{
WritableCachedStore.Manager mgr;

NoCacheWritableCachedStore(WritableCachedStore.Manager paramManager) {
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

public void write(Object paramObject1, Object paramObject2) throws CachedStoreException {
try {
this.mgr.writeToStorage(paramObject1, paramObject2);
} catch (Exception exception) {

exception.printStackTrace();
throw CachedStoreUtils.toCachedStoreException(exception);
} 
}

public void remove(Object paramObject) throws CachedStoreException {
try {
this.mgr.removeFromStorage(paramObject);
} catch (Exception exception) {

exception.printStackTrace();
throw CachedStoreUtils.toCachedStoreException(exception);
} 
}

public void flushWrites() throws CacheFlushException {}

public Set getFailedWrites() throws CachedStoreException {
return Collections.EMPTY_SET;
}

public void clearPendingWrites() throws CachedStoreException {}

public void sync() throws CachedStoreException {}
}

