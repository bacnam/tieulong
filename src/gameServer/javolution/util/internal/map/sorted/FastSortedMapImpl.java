/*    */ package javolution.util.internal.map.sorted;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.internal.map.MapView;
/*    */ import javolution.util.internal.table.sorted.FastSortedTableImpl;
/*    */ import javolution.util.service.SortedMapService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FastSortedMapImpl<K, V>
/*    */   extends SortedMapView<K, V>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   private final Equality<? super K> keyComparator;
/* 23 */   private FastSortedTableImpl<Map.Entry<K, V>> entries = new FastSortedTableImpl((Equality)new MapView.EntryComparator(this));
/*    */   
/*    */   private final Equality<? super V> valueComparator;
/*    */ 
/*    */   
/*    */   public FastSortedMapImpl(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
/* 29 */     super((SortedMapService<K, V>)null);
/* 30 */     this.keyComparator = keyComparator;
/* 31 */     this.valueComparator = valueComparator;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean containsKey(Object key) {
/* 37 */     return this.entries.contains(new MapEntryImpl<Object, Object>(key, null));
/*    */   }
/*    */ 
/*    */   
/*    */   public K firstKey() {
/* 42 */     return (K)((Map.Entry)this.entries.getFirst()).getKey();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public V get(Object key) {
/* 48 */     int i = this.entries.indexOf(new MapEntryImpl<Object, Object>(key, null));
/* 49 */     return (i >= 0) ? (V)((Map.Entry)this.entries.get(i)).getValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Map.Entry<K, V>> iterator() {
/* 54 */     return this.entries.iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super K> keyComparator() {
/* 59 */     return this.keyComparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public K lastKey() {
/* 64 */     return (K)((Map.Entry)this.entries.getLast()).getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public V put(K key, V value) {
/* 69 */     MapEntryImpl<K, V> entry = new MapEntryImpl<K, V>(key, value);
/* 70 */     int i = this.entries.positionOf(entry);
/* 71 */     if (i < size()) {
/* 72 */       Map.Entry<K, V> e = (Map.Entry<K, V>)this.entries.get(i);
/* 73 */       if (keyComparator().areEqual(key, e.getKey())) {
/* 74 */         V previous = e.getValue();
/* 75 */         e.setValue(value);
/* 76 */         return previous;
/*    */       } 
/*    */     } 
/* 79 */     this.entries.add(i, entry);
/* 80 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public V remove(Object key) {
/* 86 */     int i = this.entries.indexOf(new MapEntryImpl<Object, Object>(key, null));
/* 87 */     if (i < 0) return null; 
/* 88 */     Map.Entry<K, V> e = (Map.Entry<K, V>)this.entries.get(i);
/* 89 */     V previous = e.getValue();
/* 90 */     this.entries.remove(i);
/* 91 */     return previous;
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super V> valueComparator() {
/* 96 */     return this.valueComparator;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/FastSortedMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */