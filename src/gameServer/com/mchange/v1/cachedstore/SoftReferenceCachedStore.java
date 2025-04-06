/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
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
/*    */ class SoftReferenceCachedStore
/*    */   extends ValueTransformingCachedStore
/*    */ {
/*    */   public SoftReferenceCachedStore(CachedStore.Manager paramManager) {
/* 54 */     super(paramManager);
/*    */   }
/*    */   protected Object toUserValue(Object paramObject) {
/* 57 */     return (paramObject == null) ? null : ((SoftReference)paramObject).get();
/*    */   }
/*    */   protected Object toCacheValue(Object paramObject) {
/* 60 */     return (paramObject == null) ? null : new SoftReference(paramObject);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/SoftReferenceCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */