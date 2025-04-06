/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ class NoCleanupCachedStore
/*    */   implements TweakableCachedStore
/*    */ {
/*    */   static final boolean DEBUG = true;
/* 46 */   protected Map cache = new HashMap<Object, Object>();
/*    */   
/*    */   CachedStore.Manager manager;
/*    */   
/*    */   public NoCleanupCachedStore(CachedStore.Manager paramManager) {
/* 51 */     this.manager = paramManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object find(Object paramObject) throws CachedStoreException {
/*    */     try {
/* 60 */       Object object = getCachedValue(paramObject);
/* 61 */       if (object == null || this.manager.isDirty(paramObject, object)) {
/*    */         
/* 63 */         object = this.manager.recreateFromKey(paramObject);
/* 64 */         if (object != null)
/* 65 */           setCachedValue(paramObject, object); 
/*    */       } 
/* 67 */       return object;
/*    */     }
/* 69 */     catch (CachedStoreException cachedStoreException) {
/* 70 */       throw cachedStoreException;
/* 71 */     } catch (Exception exception) {
/*    */ 
/*    */       
/* 74 */       exception.printStackTrace();
/* 75 */       throw new CachedStoreException(exception);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCachedValue(Object paramObject) {
/* 81 */     return this.cache.get(paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeFromCache(Object paramObject) throws CachedStoreException {
/* 86 */     this.cache.remove(paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
/* 91 */     this.cache.put(paramObject1, paramObject2);
/*    */   }
/*    */   
/*    */   public Iterator cachedKeys() throws CachedStoreException {
/* 95 */     return this.cache.keySet().iterator();
/*    */   }
/*    */   public void reset() {
/* 98 */     this.cache.clear();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/NoCleanupCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */