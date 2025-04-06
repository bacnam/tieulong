/*     */ package com.mchange.v1.identicator;
/*     */ 
/*     */ import com.mchange.v1.util.WrapperIterator;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public final class IdWeakHashMap
/*     */   extends IdMap
/*     */   implements Map
/*     */ {
/*     */   ReferenceQueue rq;
/*     */   
/*     */   public IdWeakHashMap(Identicator paramIdenticator) {
/*  51 */     super(new HashMap<Object, Object>(), paramIdenticator);
/*  52 */     this.rq = new ReferenceQueue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  63 */     cleanCleared();
/*  64 */     return super.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*     */     try {
/*  70 */       return super.isEmpty();
/*     */     } finally {
/*  72 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object paramObject) {
/*     */     try {
/*  78 */       return super.containsKey(paramObject);
/*     */     } finally {
/*  80 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object paramObject) {
/*     */     try {
/*  86 */       return super.containsValue(paramObject);
/*     */     } finally {
/*  88 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object get(Object paramObject) {
/*     */     try {
/*  94 */       return super.get(paramObject);
/*     */     } finally {
/*  96 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object put(Object paramObject1, Object paramObject2) {
/*     */     try {
/* 102 */       return super.put(paramObject1, paramObject2);
/*     */     } finally {
/* 104 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object remove(Object paramObject) {
/*     */     try {
/* 110 */       return super.remove(paramObject);
/*     */     } finally {
/* 112 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> paramMap) {
/*     */     try {
/* 118 */       super.putAll(paramMap);
/*     */     } finally {
/* 120 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/*     */     try {
/* 126 */       super.clear();
/*     */     } finally {
/* 128 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set keySet() {
/*     */     try {
/* 134 */       return super.keySet();
/*     */     } finally {
/* 136 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection values() {
/*     */     try {
/* 142 */       return super.values();
/*     */     } finally {
/* 144 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set entrySet() {
/*     */     try {
/* 155 */       return new WeakUserEntrySet();
/*     */     } finally {
/* 157 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*     */     try {
/* 163 */       return super.equals(paramObject);
/*     */     } finally {
/* 165 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*     */     try {
/* 171 */       return super.hashCode();
/*     */     } finally {
/* 173 */       cleanCleared();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected IdHashKey createIdKey(Object paramObject) {
/* 178 */     return new WeakIdHashKey(paramObject, this.id, this.rq);
/*     */   }
/*     */   
/*     */   private void cleanCleared() {
/*     */     WeakIdHashKey.Ref ref;
/* 183 */     while ((ref = (WeakIdHashKey.Ref)this.rq.poll()) != null)
/* 184 */       removeIdHashKey(ref.getKey()); 
/*     */   }
/*     */   
/*     */   private final class WeakUserEntrySet
/*     */     extends AbstractSet {
/* 189 */     Set innerEntries = IdWeakHashMap.this.internalEntrySet();
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator iterator() {
/*     */       try {
/* 195 */         return (Iterator)new WrapperIterator(this.innerEntries.iterator(), true)
/*     */           {
/*     */             protected Object transformObject(Object param2Object)
/*     */             {
/* 199 */               Map.Entry entry = (Map.Entry)param2Object;
/* 200 */               final Object userKey = ((IdHashKey)entry.getKey()).getKeyObj();
/* 201 */               if (object == null) {
/* 202 */                 return WrapperIterator.SKIP_TOKEN;
/*     */               }
/* 204 */               return new IdMap.UserEntry(entry) {
/* 205 */                   Object preventRefClear = userKey;
/*     */                 };
/*     */             }
/*     */           };
/*     */       } finally {
/* 210 */         IdWeakHashMap.this.cleanCleared();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 220 */       IdWeakHashMap.this.cleanCleared();
/* 221 */       return this.innerEntries.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object param1Object) {
/*     */       try {
/* 228 */         if (param1Object instanceof Map.Entry) {
/*     */           
/* 230 */           Map.Entry entry = (Map.Entry)param1Object;
/* 231 */           return this.innerEntries.contains(IdWeakHashMap.this.createIdEntry(entry));
/*     */         } 
/*     */         
/* 234 */         return false;
/*     */       } finally {
/*     */         
/* 237 */         IdWeakHashMap.this.cleanCleared();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object param1Object) {
/*     */       try {
/* 244 */         if (param1Object instanceof Map.Entry) {
/*     */           
/* 246 */           Map.Entry entry = (Map.Entry)param1Object;
/* 247 */           return this.innerEntries.remove(IdWeakHashMap.this.createIdEntry(entry));
/*     */         } 
/*     */         
/* 250 */         return false;
/*     */       } finally {
/*     */         
/* 253 */         IdWeakHashMap.this.cleanCleared();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void clear() {
/*     */       try {
/* 259 */         IdWeakHashMap.this.inner.clear();
/*     */       } finally {
/* 261 */         IdWeakHashMap.this.cleanCleared();
/*     */       } 
/*     */     }
/*     */     
/*     */     private WeakUserEntrySet() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdWeakHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */