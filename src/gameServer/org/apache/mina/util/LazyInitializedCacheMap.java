/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public class LazyInitializedCacheMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   private ConcurrentMap<K, LazyInitializer<V>> cache;
/*     */   
/*     */   public class NoopInitializer
/*     */     extends LazyInitializer<V>
/*     */   {
/*     */     private V value;
/*     */     
/*     */     public NoopInitializer(V value) {
/*  54 */       this.value = value;
/*     */     }
/*     */     
/*     */     public V init() {
/*  58 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LazyInitializedCacheMap() {
/*  67 */     this.cache = new ConcurrentHashMap<K, LazyInitializer<V>>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LazyInitializedCacheMap(ConcurrentHashMap<K, LazyInitializer<V>> map) {
/*  75 */     this.cache = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  82 */     LazyInitializer<V> c = this.cache.get(key);
/*  83 */     if (c != null) {
/*  84 */       return c.get();
/*     */     }
/*     */     
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/*  94 */     LazyInitializer<V> c = this.cache.remove(key);
/*  95 */     if (c != null) {
/*  96 */       return c.get();
/*     */     }
/*     */     
/*  99 */     return null;
/*     */   }
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
/*     */   public V putIfAbsent(K key, LazyInitializer<V> value) {
/* 120 */     LazyInitializer<V> v = this.cache.get(key);
/* 121 */     if (v == null) {
/* 122 */       v = this.cache.putIfAbsent(key, value);
/* 123 */       if (v == null) {
/* 124 */         return value.get();
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return v.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 135 */     LazyInitializer<V> c = this.cache.put(key, new NoopInitializer(value));
/* 136 */     if (c != null) {
/* 137 */       return c.get();
/*     */     }
/*     */     
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 148 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 156 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 164 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/* 171 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 172 */       this.cache.put(e.getKey(), new NoopInitializer(e.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<LazyInitializer<V>> getValues() {
/* 180 */     return this.cache.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 187 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 194 */     return this.cache.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 201 */     return this.cache.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 208 */     return this.cache.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 215 */     return this.cache.size();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/LazyInitializedCacheMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */