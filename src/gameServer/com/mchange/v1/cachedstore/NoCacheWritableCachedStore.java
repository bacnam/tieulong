/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NoCacheWritableCachedStore
/*    */   implements WritableCachedStore, Autoflushing
/*    */ {
/*    */   WritableCachedStore.Manager mgr;
/*    */   
/*    */   NoCacheWritableCachedStore(WritableCachedStore.Manager paramManager) {
/* 46 */     this.mgr = paramManager;
/*    */   }
/*    */   public Object find(Object paramObject) throws CachedStoreException {
/*    */     try {
/* 50 */       return this.mgr.recreateFromKey(paramObject);
/* 51 */     } catch (Exception exception) {
/*    */       
/* 53 */       exception.printStackTrace();
/* 54 */       throw CachedStoreUtils.toCachedStoreException(exception);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {}
/*    */   
/*    */   public void write(Object paramObject1, Object paramObject2) throws CachedStoreException {
/*    */     try {
/* 63 */       this.mgr.writeToStorage(paramObject1, paramObject2);
/* 64 */     } catch (Exception exception) {
/*    */       
/* 66 */       exception.printStackTrace();
/* 67 */       throw CachedStoreUtils.toCachedStoreException(exception);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void remove(Object paramObject) throws CachedStoreException {
/*    */     try {
/* 73 */       this.mgr.removeFromStorage(paramObject);
/* 74 */     } catch (Exception exception) {
/*    */       
/* 76 */       exception.printStackTrace();
/* 77 */       throw CachedStoreUtils.toCachedStoreException(exception);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void flushWrites() throws CacheFlushException {}
/*    */   
/*    */   public Set getFailedWrites() throws CachedStoreException {
/* 85 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   
/*    */   public void clearPendingWrites() throws CachedStoreException {}
/*    */   
/*    */   public void sync() throws CachedStoreException {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/NoCacheWritableCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */