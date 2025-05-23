package com.mchange.v1.cachedstore;

import com.mchange.lang.PotentiallySecondary;
import com.mchange.v1.lang.Synchronizer;

public final class CachedStoreUtils
{
static final boolean DEBUG = true;

public static CachedStore synchronizedCachedStore(CachedStore paramCachedStore) {
return (CachedStore)Synchronizer.createSynchronizedWrapper(paramCachedStore);
}
public static TweakableCachedStore synchronizedTweakableCachedStore(TweakableCachedStore paramTweakableCachedStore) {
return (TweakableCachedStore)Synchronizer.createSynchronizedWrapper(paramTweakableCachedStore);
}
public static WritableCachedStore synchronizedWritableCachedStore(WritableCachedStore paramWritableCachedStore) {
return (WritableCachedStore)Synchronizer.createSynchronizedWrapper(paramWritableCachedStore);
}

public static CachedStore untweakableCachedStore(final TweakableCachedStore orig) {
return new CachedStore()
{
public Object find(Object param1Object) throws CachedStoreException {
return orig.find(param1Object);
}
public void reset() throws CachedStoreException {
orig.reset();
}
};
}

static CachedStoreException toCachedStoreException(Throwable paramThrowable) {
paramThrowable.printStackTrace();

if (paramThrowable instanceof CachedStoreException)
return (CachedStoreException)paramThrowable; 
if (paramThrowable instanceof PotentiallySecondary) {

Throwable throwable = ((PotentiallySecondary)paramThrowable).getNestedThrowable();
if (throwable instanceof CachedStoreException)
return (CachedStoreException)throwable; 
} 
return new CachedStoreException(paramThrowable);
}

static CacheFlushException toCacheFlushException(Throwable paramThrowable) {
paramThrowable.printStackTrace();

if (paramThrowable instanceof CacheFlushException) {
return (CacheFlushException)paramThrowable;
}
return new CacheFlushException(paramThrowable);
}
}

