/*     */ package com.mchange.v1.cachedstore;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CachedStoreFactory
/*     */ {
/*     */   public static TweakableCachedStore createNoCleanupCachedStore(CachedStore.Manager paramManager) {
/*  53 */     return new NoCleanupCachedStore(paramManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TweakableCachedStore createSoftValueCachedStore(CachedStore.Manager paramManager) {
/*  60 */     return new SoftReferenceCachedStore(paramManager);
/*     */   }
/*     */   
/*     */   public static TweakableCachedStore createSynchronousCleanupSoftKeyCachedStore(CachedStore.Manager paramManager) {
/*  64 */     final ManualCleanupSoftKeyCachedStore inner = new ManualCleanupSoftKeyCachedStore(paramManager);
/*  65 */     InvocationHandler invocationHandler = new InvocationHandler()
/*     */       {
/*     */         
/*     */         public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
/*     */         {
/*  70 */           inner.vacuum();
/*  71 */           return param1Method.invoke(inner, param1ArrayOfObject);
/*     */         }
/*     */       };
/*  74 */     return (TweakableCachedStore)Proxy.newProxyInstance(CachedStoreFactory.class.getClassLoader(), new Class[] { TweakableCachedStore.class }, invocationHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TweakableCachedStore createNoCacheCachedStore(CachedStore.Manager paramManager) {
/*  83 */     return new NoCacheCachedStore(paramManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WritableCachedStore createDefaultWritableCachedStore(WritableCachedStore.Manager paramManager) {
/*  91 */     TweakableCachedStore tweakableCachedStore = createSynchronousCleanupSoftKeyCachedStore(paramManager);
/*  92 */     return new SimpleWritableCachedStore(tweakableCachedStore, paramManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public static WritableCachedStore cacheWritesOnlyWritableCachedStore(WritableCachedStore.Manager paramManager) {
/*  97 */     TweakableCachedStore tweakableCachedStore = createNoCacheCachedStore(paramManager);
/*  98 */     return new SimpleWritableCachedStore(tweakableCachedStore, paramManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WritableCachedStore createNoCacheWritableCachedStore(WritableCachedStore.Manager paramManager) {
/* 109 */     return new NoCacheWritableCachedStore(paramManager);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/CachedStoreFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */