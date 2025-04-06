/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*  46 */   private static final ImmutableBiMap<Object, Object> EMPTY_IMMUTABLE_BIMAP = new EmptyBiMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of() {
/*  55 */     return (ImmutableBiMap)EMPTY_IMMUTABLE_BIMAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
/*  62 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  71 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  81 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  92 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 103 */     return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 114 */     return new Builder<K, V>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder<K, V> put(K key, V value) {
/* 150 */       super.put(key, value);
/* 151 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 162 */       super.putAll(map);
/* 163 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> build() {
/* 172 */       ImmutableMap<K, V> map = super.build();
/* 173 */       if (map.isEmpty()) {
/* 174 */         return ImmutableBiMap.of();
/*     */       }
/* 176 */       return new RegularImmutableBiMap<K, V>(map);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 195 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 197 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */ 
/*     */       
/* 200 */       if (!bimap.isPartialView()) {
/* 201 */         return bimap;
/*     */       }
/*     */     } 
/*     */     
/* 205 */     if (map.isEmpty()) {
/* 206 */       return of();
/*     */     }
/*     */     
/* 209 */     ImmutableMap<K, V> immutableMap = ImmutableMap.copyOf(map);
/* 210 */     return new RegularImmutableBiMap<K, V>(immutableMap);
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
/*     */   public boolean containsKey(@Nullable Object key) {
/* 227 */     return delegate().containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 231 */     return inverse().containsKey(value);
/*     */   }
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 235 */     return delegate().entrySet();
/*     */   }
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 239 */     return delegate().get(key);
/*     */   }
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 243 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<V> values() {
/* 251 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V forcePut(K key, V value) {
/* 261 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 265 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 270 */     return delegate().size();
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 274 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 278 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 282 */     return delegate().toString();
/*     */   }
/*     */   
/*     */   static class EmptyBiMap
/*     */     extends ImmutableBiMap<Object, Object>
/*     */   {
/*     */     ImmutableMap<Object, Object> delegate() {
/* 289 */       return ImmutableMap.of();
/*     */     }
/*     */     public ImmutableBiMap<Object, Object> inverse() {
/* 292 */       return this;
/*     */     }
/*     */     boolean isPartialView() {
/* 295 */       return false;
/*     */     }
/*     */     Object readResolve() {
/* 298 */       return ImmutableBiMap.EMPTY_IMMUTABLE_BIMAP;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableBiMap<?, ?> bimap) {
/* 313 */       super(bimap);
/*     */     }
/*     */     Object readResolve() {
/* 316 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
/* 317 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 323 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   abstract ImmutableMap<K, V> delegate();
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/ImmutableBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */