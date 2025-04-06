package com.mchange.v1.cachedstore;

public interface CachedStore {
  Object find(Object paramObject) throws CachedStoreException;
  
  void reset() throws CachedStoreException;
  
  public static interface Manager {
    boolean isDirty(Object param1Object1, Object param1Object2) throws Exception;
    
    Object recreateFromKey(Object param1Object) throws Exception;
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/CachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */