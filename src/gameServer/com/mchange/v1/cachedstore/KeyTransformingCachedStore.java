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
/*    */ abstract class KeyTransformingCachedStore
/*    */   extends NoCleanupCachedStore
/*    */ {
/*    */   protected KeyTransformingCachedStore(CachedStore.Manager paramManager) {
/* 44 */     super(paramManager);
/*    */   }
/*    */   public Object getCachedValue(Object paramObject) {
/* 47 */     return this.cache.get(toCacheFetchKey(paramObject));
/*    */   }
/*    */   
/*    */   public void removeFromCache(Object paramObject) throws CachedStoreException {
/* 51 */     this.cache.remove(toCacheFetchKey(paramObject));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCachedValue(Object paramObject1, Object paramObject2) throws CachedStoreException {
/* 57 */     Object object = toCachePutKey(paramObject1);
/*    */     
/* 59 */     this.cache.put(object, paramObject2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator cachedKeys() throws CachedStoreException {
/* 64 */     return (Iterator)new WrapperIterator(this.cache.keySet().iterator(), false)
/*    */       {
/*    */         public Object transformObject(Object param1Object)
/*    */         {
/* 68 */           Object object = KeyTransformingCachedStore.this.toUserKey(param1Object);
/* 69 */           return (object == null) ? SKIP_TOKEN : object;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected Object toUserKey(Object paramObject) {
/* 75 */     return paramObject;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object toCacheFetchKey(Object paramObject) {
/* 82 */     return toCachePutKey(paramObject);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object toCachePutKey(Object paramObject) {
/* 89 */     return paramObject;
/*    */   }
/*    */   protected Object removeByTransformedKey(Object paramObject) {
/* 92 */     return this.cache.remove(paramObject);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/KeyTransformingCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */