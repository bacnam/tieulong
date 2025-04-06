/*     */ package javolution.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javolution.lang.Immutable;
/*     */ import javolution.lang.Parallelizable;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.text.TextContext;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.map.AtomicMapImpl;
/*     */ import javolution.util.internal.map.FastMapImpl;
/*     */ import javolution.util.internal.map.ParallelMapImpl;
/*     */ import javolution.util.internal.map.SequentialMapImpl;
/*     */ import javolution.util.internal.map.SharedMapImpl;
/*     */ import javolution.util.internal.map.UnmodifiableMapImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.MapService;
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
/*     */ @Realtime
/*     */ public class FastMap<K, V>
/*     */   implements Map<K, V>, ConcurrentMap<K, V>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final MapService<K, V> service;
/*     */   
/*     */   public FastMap() {
/* 111 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastMap(Equality<? super K> keyEquality) {
/* 119 */     this(keyEquality, Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastMap(Equality<? super K> keyEquality, Equality<? super V> valueEquality) {
/* 128 */     this.service = (MapService<K, V>)new FastMapImpl(keyEquality, valueEquality);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastMap(MapService<K, V> service) {
/* 135 */     this.service = service;
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
/*     */   @Parallelizable(mutexFree = true, comment = "Except for write operations, all read operations are mutex-free.")
/*     */   public FastMap<K, V> atomic() {
/* 151 */     return new FastMap((MapService<K, V>)new AtomicMapImpl(this.service));
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
/*     */   @Parallelizable(mutexFree = false, comment = "Use multiple-readers/single-writer lock.")
/*     */   public FastMap<K, V> shared() {
/* 165 */     return new FastMap((MapService<K, V>)new SharedMapImpl(this.service));
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
/*     */   public FastMap<K, V> parallel() {
/* 179 */     return new FastMap((MapService<K, V>)new ParallelMapImpl(this.service));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastMap<K, V> sequential() {
/* 187 */     return new FastMap((MapService<K, V>)new SequentialMapImpl(this.service));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastMap<K, V> unmodifiable() {
/* 196 */     return new FastMap((MapService<K, V>)new UnmodifiableMapImpl(this.service));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSet<K> keySet() {
/* 207 */     return new FastSet<K>(this.service.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastCollection<V> values() {
/* 217 */     return new FastCollection<V>() {
/*     */         private static final long serialVersionUID = 1536L;
/* 219 */         private final CollectionService<V> serviceValues = FastMap.this.service.values();
/*     */ 
/*     */         
/*     */         protected CollectionService<V> service() {
/* 223 */           return this.serviceValues;
/*     */         }
/*     */       };
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
/*     */   public FastSet<Map.Entry<K, V>> entrySet() {
/* 237 */     return new FastSet<Map.Entry<K, V>>(this.service.entrySet());
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void perform(Consumer<? extends Map<K, V>> action) {
/* 259 */     service().perform(action, service());
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void update(Consumer<? extends Map<K, V>> action) {
/* 277 */     service().update(action, service());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public int size() {
/* 288 */     return this.service.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean isEmpty() {
/* 295 */     return this.service.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean containsKey(Object key) {
/* 302 */     return this.service.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean containsValue(Object value) {
/* 309 */     return this.service.containsValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public V get(Object key) {
/* 316 */     return (V)this.service.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public V put(K key, V value) {
/* 323 */     return (V)this.service.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 330 */     this.service.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public V remove(Object key) {
/* 337 */     return (V)this.service.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public void clear() {
/* 344 */     this.service.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public V putIfAbsent(K key, V value) {
/* 356 */     return (V)this.service.putIfAbsent(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean remove(Object key, Object value) {
/* 363 */     return this.service.remove(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public boolean replace(K key, V oldValue, V newValue) {
/* 370 */     return this.service.replace(key, oldValue, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.CONSTANT)
/*     */   public V replace(K key, V value) {
/* 377 */     return (V)this.service.replace(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastMap<K, V> putAll(FastMap<? extends K, ? extends V> that) {
/* 388 */     putAll(that);
/* 389 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Map<K, V>> Immutable<T> toImmutable() {
/* 399 */     return new Immutable<T>() {
/* 400 */         final T value = (T)FastMap.this.unmodifiable();
/*     */ 
/*     */ 
/*     */         
/*     */         public T value() {
/* 405 */           return this.value;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public String toString() {
/* 414 */     return TextContext.getFormat(FastCollection.class).format(entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MapService<K, V> service() {
/* 421 */     return this.service;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */