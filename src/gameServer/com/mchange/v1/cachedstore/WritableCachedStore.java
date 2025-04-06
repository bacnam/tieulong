package com.mchange.v1.cachedstore;

import java.util.Set;

public interface WritableCachedStore extends CachedStore {
  void write(Object paramObject1, Object paramObject2) throws CachedStoreException;
  
  void remove(Object paramObject) throws CachedStoreException;
  
  void flushWrites() throws CacheFlushException;
  
  Set getFailedWrites() throws CachedStoreException;
  
  void clearPendingWrites() throws CachedStoreException;
  
  void reset() throws CachedStoreException;
  
  void sync() throws CachedStoreException;
  
  public static interface Manager extends CachedStore.Manager {
    void writeToStorage(Object param1Object1, Object param1Object2) throws Exception;
    
    void removeFromStorage(Object param1Object) throws Exception;
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/WritableCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */