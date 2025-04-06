/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
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
/*     */ public class CopyOnWriteMap<K, V>
/*     */   implements Map<K, V>, Cloneable
/*     */ {
/*     */   private volatile Map<K, V> internalMap;
/*     */   
/*     */   public CopyOnWriteMap() {
/*  46 */     this.internalMap = new HashMap<K, V>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CopyOnWriteMap(int initialCapacity) {
/*  56 */     this.internalMap = new HashMap<K, V>(initialCapacity);
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
/*     */   public CopyOnWriteMap(Map<K, V> data) {
/*  69 */     this.internalMap = new HashMap<K, V>(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/*  78 */     synchronized (this) {
/*  79 */       Map<K, V> newMap = new HashMap<K, V>(this.internalMap);
/*  80 */       V val = newMap.put(key, value);
/*  81 */       this.internalMap = newMap;
/*  82 */       return val;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/*  93 */     synchronized (this) {
/*  94 */       Map<K, V> newMap = new HashMap<K, V>(this.internalMap);
/*  95 */       V val = newMap.remove(key);
/*  96 */       this.internalMap = newMap;
/*  97 */       return val;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> newData) {
/* 108 */     synchronized (this) {
/* 109 */       Map<K, V> newMap = new HashMap<K, V>(this.internalMap);
/* 110 */       newMap.putAll(newData);
/* 111 */       this.internalMap = newMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 121 */     synchronized (this) {
/* 122 */       this.internalMap = new HashMap<K, V>();
/*     */     } 
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
/*     */   public int size() {
/* 136 */     return this.internalMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 145 */     return this.internalMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 155 */     return this.internalMap.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 165 */     return this.internalMap.containsValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 175 */     return this.internalMap.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 182 */     return this.internalMap.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 189 */     return this.internalMap.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 196 */     return this.internalMap.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 202 */       return super.clone();
/* 203 */     } catch (CloneNotSupportedException e) {
/* 204 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/CopyOnWriteMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */