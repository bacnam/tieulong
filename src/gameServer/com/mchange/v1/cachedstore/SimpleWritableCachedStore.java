/*     */ package com.mchange.v1.cachedstore;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ class SimpleWritableCachedStore
/*     */   implements WritableCachedStore
/*     */ {
/*  46 */   private static final Object REMOVE_TOKEN = new Object();
/*     */   
/*     */   TweakableCachedStore readOnlyCache;
/*     */   
/*     */   WritableCachedStore.Manager manager;
/*  51 */   HashMap writeCache = new HashMap<Object, Object>();
/*     */   
/*  53 */   Set failedWrites = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SimpleWritableCachedStore(TweakableCachedStore paramTweakableCachedStore, WritableCachedStore.Manager paramManager) {
/*  59 */     this.readOnlyCache = paramTweakableCachedStore;
/*  60 */     this.manager = paramManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object find(Object paramObject) throws CachedStoreException {
/*  65 */     Object object = this.writeCache.get(paramObject);
/*  66 */     if (object == null)
/*  67 */       object = this.readOnlyCache.find(paramObject); 
/*  68 */     return (object == REMOVE_TOKEN) ? null : object;
/*     */   }
/*     */   
/*     */   public void write(Object paramObject1, Object paramObject2) {
/*  72 */     this.writeCache.put(paramObject1, paramObject2);
/*     */   }
/*     */   public void remove(Object paramObject) {
/*  75 */     write(paramObject, REMOVE_TOKEN);
/*     */   }
/*     */   
/*     */   public void flushWrites() throws CacheFlushException {
/*  79 */     HashMap hashMap = (HashMap)this.writeCache.clone();
/*  80 */     for (Object object : hashMap.keySet()) {
/*     */ 
/*     */       
/*  83 */       Object object1 = hashMap.get(object);
/*     */ 
/*     */       
/*     */       try {
/*  87 */         if (object1 == REMOVE_TOKEN) {
/*  88 */           this.manager.removeFromStorage(object);
/*     */         } else {
/*  90 */           this.manager.writeToStorage(object, object1);
/*     */         } 
/*     */         
/*     */         try {
/*  94 */           this.readOnlyCache.setCachedValue(object, object1);
/*  95 */           this.writeCache.remove(object);
/*  96 */           if (this.failedWrites != null) {
/*     */             
/*  98 */             this.failedWrites.remove(object);
/*  99 */             if (this.failedWrites.size() == 0) {
/* 100 */               this.failedWrites = null;
/*     */             }
/*     */           } 
/* 103 */         } catch (CachedStoreException cachedStoreException) {
/*     */           
/* 105 */           throw new CachedStoreError("SimpleWritableCachedStore: Internal cache is broken!");
/*     */         }
/*     */       
/*     */       }
/* 109 */       catch (Exception exception) {
/*     */         
/* 111 */         if (this.failedWrites == null)
/* 112 */           this.failedWrites = new HashSet(); 
/* 113 */         this.failedWrites.add(object);
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     if (this.failedWrites != null)
/* 118 */       throw new CacheFlushException("Some keys failed to write!"); 
/*     */   }
/*     */   
/*     */   public Set getFailedWrites() {
/* 122 */     return (this.failedWrites == null) ? null : Collections.unmodifiableSet(this.failedWrites);
/*     */   }
/*     */   
/*     */   public void clearPendingWrites() {
/* 126 */     this.writeCache.clear();
/* 127 */     this.failedWrites = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws CachedStoreException {
/* 132 */     this.writeCache.clear();
/* 133 */     this.readOnlyCache.reset();
/* 134 */     this.failedWrites = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sync() throws CachedStoreException {
/* 139 */     flushWrites();
/* 140 */     reset();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/SimpleWritableCachedStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */