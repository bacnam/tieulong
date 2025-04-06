/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import com.mchange.v1.util.WrapperIterator;
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
/*    */ abstract class KeyValueTransformingCachedStore
/*    */   extends ValueTransformingCachedStore
/*    */ {
/*    */   protected KeyValueTransformingCachedStore(CachedStore.Manager paramManager) {
/* 44 */     super(paramManager);
/*    */   }
/*    */   public Object getCachedValue(Object paramObject) {
/* 47 */     return toUserValue(this.cache.get(toCacheFetchKey(paramObject)));
/*    */   }
/*    */   
/*    */   public void clearCachedValue(Object paramObject) throws CachedStoreException {
/* 51 */     this.cache.remove(toCacheFetchKey(paramObject));
/*    */   }
/*    */   
/*    */   public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
/* 55 */     this.cache.put(toCachePutKey(paramObject1), toCacheValue(paramObject2));
/*    */   }
/*    */   
/*    */   public Iterator cachedKeys() throws CachedStoreException {
/* 59 */     return (Iterator)new WrapperIterator(this.cache.keySet().iterator(), false)
/*    */       {
/*    */         public Object transformObject(Object param1Object)
/*    */         {
/* 63 */           Object object = KeyValueTransformingCachedStore.this.toUserKey(param1Object);
/* 64 */           return (object == null) ? SKIP_TOKEN : object;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected Object toUserKey(Object paramObject) {
/* 70 */     return paramObject;
/*    */   }
/*    */   protected Object toCacheFetchKey(Object paramObject) {
/* 73 */     return paramObject;
/*    */   }
/*    */   protected Object toCachePutKey(Object paramObject) {
/* 76 */     return paramObject;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/KeyValueTransformingCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */