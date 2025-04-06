/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
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
/*    */ class ManualCleanupSoftKeyCachedStore
/*    */   extends KeyTransformingCachedStore
/*    */   implements Vacuumable
/*    */ {
/* 42 */   ReferenceQueue queue = new ReferenceQueue();
/*    */   
/*    */   public ManualCleanupSoftKeyCachedStore(CachedStore.Manager paramManager) {
/* 45 */     super(paramManager);
/*    */   }
/*    */   protected Object toUserKey(Object paramObject) {
/* 48 */     return ((SoftKey)paramObject).get();
/*    */   }
/*    */   protected Object toCacheFetchKey(Object paramObject) {
/* 51 */     return new SoftKey(paramObject, null);
/*    */   }
/*    */   protected Object toCachePutKey(Object paramObject) {
/* 54 */     return new SoftKey(paramObject, this.queue);
/*    */   }
/*    */   
/*    */   public void vacuum() throws CachedStoreException {
/*    */     SoftKey softKey;
/* 59 */     while ((softKey = (SoftKey)this.queue.poll()) != null)
/*    */     {
/*    */       
/* 62 */       removeByTransformedKey(softKey);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/ManualCleanupSoftKeyCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */