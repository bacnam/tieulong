package com.mchange.v1.cachedstore;

import java.util.Iterator;

public interface TweakableCachedStore extends CachedStore {
  Object getCachedValue(Object paramObject) throws CachedStoreException;
  
  void removeFromCache(Object paramObject) throws CachedStoreException;
  
  void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException;
  
  Iterator cachedKeys() throws CachedStoreException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/TweakableCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */