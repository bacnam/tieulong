/*    */ package javolution.util.internal.map.sorted;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedMap;
/*    */ import javolution.util.internal.map.AtomicMapImpl;
/*    */ import javolution.util.service.MapService;
/*    */ import javolution.util.service.SetService;
/*    */ import javolution.util.service.SortedMapService;
/*    */ import javolution.util.service.SortedSetService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicSortedMapImpl<K, V>
/*    */   extends AtomicMapImpl<K, V>
/*    */   implements SortedMapService<K, V>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public AtomicSortedMapImpl(SortedMapService<K, V> target) {
/* 26 */     super((MapService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super K> comparator() {
/* 31 */     return (Comparator<? super K>)target().keyComparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<Map.Entry<K, V>> entrySet() {
/* 36 */     return (new SubSortedMapImpl<K, V>(this, null, null)).entrySet();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public K firstKey() {
/* 42 */     return (K)targetView().firstKey();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> headMap(K toKey) {
/* 48 */     return new SubSortedMapImpl<K, V>(this, null, toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<K> keySet() {
/* 53 */     return (new SubSortedMapImpl<K, Object>(this, null, null)).keySet();
/*    */   }
/*    */ 
/*    */   
/*    */   public K lastKey() {
/* 58 */     return (K)targetView().lastKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> subMap(K fromKey, K toKey) {
/* 63 */     return new SubSortedMapImpl<K, V>(this, fromKey, toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> tailMap(K fromKey) {
/* 68 */     return new SubSortedMapImpl<K, V>(this, fromKey, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> threadSafe() {
/* 73 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedMapService<K, V> targetView() {
/* 78 */     return (SortedMapService<K, V>)super.targetView();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/AtomicSortedMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */