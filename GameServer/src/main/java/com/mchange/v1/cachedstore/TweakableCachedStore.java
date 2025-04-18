package com.mchange.v1.cachedstore;

import java.util.Iterator;

public interface TweakableCachedStore extends CachedStore {
  Object getCachedValue(Object paramObject) throws CachedStoreException;

  void removeFromCache(Object paramObject) throws CachedStoreException;

  void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException;

  Iterator cachedKeys() throws CachedStoreException;
}

