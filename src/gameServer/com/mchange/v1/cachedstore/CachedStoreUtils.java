/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import com.mchange.lang.PotentiallySecondary;
/*    */ import com.mchange.v1.lang.Synchronizer;
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
/*    */ public final class CachedStoreUtils
/*    */ {
/*    */   static final boolean DEBUG = true;
/*    */   
/*    */   public static CachedStore synchronizedCachedStore(CachedStore paramCachedStore) {
/* 46 */     return (CachedStore)Synchronizer.createSynchronizedWrapper(paramCachedStore);
/*    */   }
/*    */   public static TweakableCachedStore synchronizedTweakableCachedStore(TweakableCachedStore paramTweakableCachedStore) {
/* 49 */     return (TweakableCachedStore)Synchronizer.createSynchronizedWrapper(paramTweakableCachedStore);
/*    */   }
/*    */   public static WritableCachedStore synchronizedWritableCachedStore(WritableCachedStore paramWritableCachedStore) {
/* 52 */     return (WritableCachedStore)Synchronizer.createSynchronizedWrapper(paramWritableCachedStore);
/*    */   }
/*    */   
/*    */   public static CachedStore untweakableCachedStore(final TweakableCachedStore orig) {
/* 56 */     return new CachedStore()
/*    */       {
/*    */         public Object find(Object param1Object) throws CachedStoreException {
/* 59 */           return orig.find(param1Object);
/*    */         }
/*    */         public void reset() throws CachedStoreException {
/* 62 */           orig.reset();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static CachedStoreException toCachedStoreException(Throwable paramThrowable) {
/* 68 */     paramThrowable.printStackTrace();
/*    */     
/* 70 */     if (paramThrowable instanceof CachedStoreException)
/* 71 */       return (CachedStoreException)paramThrowable; 
/* 72 */     if (paramThrowable instanceof PotentiallySecondary) {
/*    */       
/* 74 */       Throwable throwable = ((PotentiallySecondary)paramThrowable).getNestedThrowable();
/* 75 */       if (throwable instanceof CachedStoreException)
/* 76 */         return (CachedStoreException)throwable; 
/*    */     } 
/* 78 */     return new CachedStoreException(paramThrowable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static CacheFlushException toCacheFlushException(Throwable paramThrowable) {
/* 84 */     paramThrowable.printStackTrace();
/*    */     
/* 86 */     if (paramThrowable instanceof CacheFlushException) {
/* 87 */       return (CacheFlushException)paramThrowable;
/*    */     }
/* 89 */     return new CacheFlushException(paramThrowable);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/CachedStoreUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */