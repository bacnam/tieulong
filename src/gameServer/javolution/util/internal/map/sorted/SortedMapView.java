/*     */ package javolution.util.internal.map.sorted;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import javolution.util.internal.map.MapView;
/*     */ import javolution.util.internal.set.sorted.SharedSortedSetImpl;
/*     */ import javolution.util.internal.set.sorted.SubSortedSetImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.MapService;
/*     */ import javolution.util.service.SetService;
/*     */ import javolution.util.service.SortedMapService;
/*     */ import javolution.util.service.SortedSetService;
/*     */ 
/*     */ public abstract class SortedMapView<K, V>
/*     */   extends MapView<K, V>
/*     */   implements SortedMapService<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   
/*     */   protected class EntrySortedSet
/*     */     extends MapView<K, V>.EntrySet
/*     */     implements SortedSetService<Map.Entry<K, V>> {
/*     */     protected EntrySortedSet() {
/*  27 */       super(SortedMapView.this);
/*     */     }
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public Map.Entry<K, V> first() {
/*  32 */       K key = (K)SortedMapView.this.firstKey();
/*  33 */       V value = (V)SortedMapView.this.get(key);
/*  34 */       return new MapEntryImpl<K, V>(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<Map.Entry<K, V>> headSet(Map.Entry<K, V> toElement) {
/*  39 */       return (SortedSetService<Map.Entry<K, V>>)new SubSortedSetImpl(this, null, toElement);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> last() {
/*  44 */       K key = (K)SortedMapView.this.lastKey();
/*  45 */       V value = (V)SortedMapView.this.get(key);
/*  46 */       return new MapEntryImpl<K, V>(key, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SortedSetService<Map.Entry<K, V>> subSet(Map.Entry<K, V> fromElement, Map.Entry<K, V> toElement) {
/*  53 */       return (SortedSetService<Map.Entry<K, V>>)new SubSortedSetImpl(this, fromElement, toElement);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<Map.Entry<K, V>> tailSet(Map.Entry<K, V> fromElement) {
/*  58 */       return (SortedSetService<Map.Entry<K, V>>)new SubSortedSetImpl(this, fromElement, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<Map.Entry<K, V>> threadSafe() {
/*  63 */       return (SortedSetService<Map.Entry<K, V>>)new SharedSortedSetImpl((SetService)this);
/*     */     } }
/*     */   
/*     */   protected class KeySortedSet extends MapView<K, V>.KeySet implements SortedSetService<K> {
/*     */     protected KeySortedSet() {
/*  68 */       super(SortedMapView.this);
/*     */     }
/*     */     private static final long serialVersionUID = 1536L;
/*     */     
/*     */     public K first() {
/*  73 */       return (K)SortedMapView.this.firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<K> headSet(K toElement) {
/*  78 */       return (SortedSetService<K>)new SubSortedSetImpl(this, null, toElement);
/*     */     }
/*     */ 
/*     */     
/*     */     public K last() {
/*  83 */       return (K)SortedMapView.this.lastKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<K> subSet(K fromElement, K toElement) {
/*  88 */       return (SortedSetService<K>)new SubSortedSetImpl(this, fromElement, toElement);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<K> tailSet(K fromElement) {
/*  93 */       return (SortedSetService<K>)new SubSortedSetImpl(this, fromElement, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSetService<K> threadSafe() {
/*  98 */       return (SortedSetService<K>)new SharedSortedSetImpl((SetService)this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMapView(SortedMapService<K, V> target) {
/* 108 */     super((MapService)target);
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 113 */     return (Comparator<? super K>)keyComparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSetService<Map.Entry<K, V>> entrySet() {
/* 118 */     return new EntrySortedSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMapService<K, V> headMap(K toKey) {
/* 126 */     return new SubSortedMapImpl<K, V>(this, firstKey(), toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSetService<K> keySet() {
/* 131 */     return new KeySortedSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMapService<K, V> subMap(K fromKey, K toKey) {
/* 139 */     return new SubSortedMapImpl<K, V>(this, fromKey, toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMapService<K, V> tailMap(K fromKey) {
/* 144 */     return new SubSortedMapImpl<K, V>(this, fromKey, lastKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMapService<K, V> threadSafe() {
/* 149 */     return new SharedSortedMapImpl<K, V>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMapService<K, V> target() {
/* 155 */     return (SortedMapService<K, V>)super.target();
/*     */   }
/*     */   
/*     */   public abstract K firstKey();
/*     */   
/*     */   public abstract K lastKey();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/SortedMapView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */