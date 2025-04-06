/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import com.mchange.v1.util.IteratorUtils;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ class NoCacheCachedStore
/*    */   implements TweakableCachedStore
/*    */ {
/*    */   CachedStore.Manager mgr;
/*    */   
/*    */   NoCacheCachedStore(CachedStore.Manager paramManager) {
/* 47 */     this.mgr = paramManager;
/*    */   }
/*    */   public Object find(Object paramObject) throws CachedStoreException {
/*    */     try {
/* 51 */       return this.mgr.recreateFromKey(paramObject);
/* 52 */     } catch (Exception exception) {
/*    */       
/* 54 */       exception.printStackTrace();
/* 55 */       throw CachedStoreUtils.toCachedStoreException(exception);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {}
/*    */   
/*    */   public Object getCachedValue(Object paramObject) {
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeFromCache(Object paramObject) {}
/*    */   
/*    */   public void setCachedValue(Object paramObject1, Object paramObject2) {}
/*    */   
/*    */   public Iterator cachedKeys() {
/* 72 */     return IteratorUtils.EMPTY_ITERATOR;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/NoCacheCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */