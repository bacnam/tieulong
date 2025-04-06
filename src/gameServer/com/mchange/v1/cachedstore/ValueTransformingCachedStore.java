/*    */ package com.mchange.v1.cachedstore;
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
/*    */ abstract class ValueTransformingCachedStore
/*    */   extends NoCleanupCachedStore
/*    */ {
/*    */   protected ValueTransformingCachedStore(CachedStore.Manager paramManager) {
/* 41 */     super(paramManager);
/*    */   }
/*    */   public Object getCachedValue(Object paramObject) {
/* 44 */     return toUserValue(this.cache.get(paramObject));
/*    */   }
/*    */   
/*    */   public void removeFromCache(Object paramObject) throws CachedStoreException {
/* 48 */     this.cache.remove(paramObject);
/*    */   }
/*    */   
/*    */   public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
/* 52 */     this.cache.put(paramObject1, toCacheValue(paramObject2));
/*    */   }
/*    */   protected Object toUserValue(Object paramObject) {
/* 55 */     return paramObject;
/*    */   }
/*    */   protected Object toCacheValue(Object paramObject) {
/* 58 */     return paramObject;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/ValueTransformingCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */