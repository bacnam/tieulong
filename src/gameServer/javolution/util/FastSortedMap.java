/*     */ package javolution.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.map.sorted.AtomicSortedMapImpl;
/*     */ import javolution.util.internal.map.sorted.FastSortedMapImpl;
/*     */ import javolution.util.internal.map.sorted.SharedSortedMapImpl;
/*     */ import javolution.util.internal.map.sorted.UnmodifiableSortedMapImpl;
/*     */ import javolution.util.service.MapService;
/*     */ import javolution.util.service.SortedMapService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastSortedMap<K, V>
/*     */   extends FastMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   public FastSortedMap() {
/*  43 */     this(Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap(Equality<? super K> keyComparator) {
/*  51 */     this(keyComparator, Equalities.STANDARD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
/*  60 */     super((MapService<K, V>)new FastSortedMapImpl(keyComparator, valueComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastSortedMap(SortedMapService<K, V> service) {
/*  67 */     super((MapService<K, V>)service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> atomic() {
/*  76 */     return new FastSortedMap((SortedMapService<K, V>)new AtomicSortedMapImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> shared() {
/*  81 */     return new FastSortedMap((SortedMapService<K, V>)new SharedSortedMapImpl(service()));
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> unmodifiable() {
/*  86 */     return new FastSortedMap((SortedMapService<K, V>)new UnmodifiableSortedMapImpl(service()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedSet<Map.Entry<K, V>> entrySet() {
/*  92 */     return new FastSortedSet<Map.Entry<K, V>>(service().entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public FastSortedSet<K> keySet() {
/*  97 */     return new FastSortedSet<K>(service().keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> subMap(K fromKey, K toKey) {
/* 103 */     return new FastSortedMap(service().subMap(fromKey, toKey));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> headMap(K toKey) {
/* 109 */     return new FastSortedMap(service().subMap(firstKey(), toKey));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> tailMap(K fromKey) {
/* 115 */     return new FastSortedMap(service().subMap(fromKey, lastKey()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean containsKey(Object key) {
/* 125 */     return super.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public V get(Object key) {
/* 131 */     return super.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public V put(K key, V value) {
/* 137 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public V remove(Object key) {
/* 143 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public V putIfAbsent(K key, V value) {
/* 149 */     return super.putIfAbsent(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean remove(Object key, Object value) {
/* 155 */     return super.remove(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public boolean replace(K key, V oldValue, V newValue) {
/* 161 */     return super.replace(key, oldValue, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LOG_N)
/*     */   public V replace(K key, V value) {
/* 167 */     return super.replace(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 177 */     return (K)service().firstKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 183 */     return (K)service().lastKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 189 */     return (Comparator<? super K>)keySet().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastSortedMap<K, V> putAll(FastMap<? extends K, ? extends V> that) {
/* 198 */     return (FastSortedMap<K, V>)super.putAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMapService<K, V> service() {
/* 203 */     return (SortedMapService<K, V>)super.service();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */