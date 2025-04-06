/*    */ package javolution.util.internal.map.sorted;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedMap;
/*    */ import javolution.util.internal.map.SharedMapImpl;
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
/*    */ public class SharedSortedMapImpl<K, V>
/*    */   extends SharedMapImpl<K, V>
/*    */   implements SortedMapService<K, V>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SharedSortedMapImpl(SortedMapService<K, V> target) {
/* 26 */     super((MapService)target);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Comparator<? super K> comparator() {
/* 32 */     return (Comparator<? super K>)target().keyComparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<Map.Entry<K, V>> entrySet() {
/* 37 */     return (new SubSortedMapImpl<K, V>(this, null, null)).entrySet();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public K firstKey() {
/* 43 */     this.lock.readLock.lock();
/*    */     try {
/* 45 */       return (K)target().firstKey();
/*    */     } finally {
/* 47 */       this.lock.readLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> headMap(K toKey) {
/* 54 */     return new SubSortedMapImpl<K, V>(this, null, toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<K> keySet() {
/* 59 */     return (new SubSortedMapImpl<K, Object>(this, null, null)).keySet();
/*    */   }
/*    */ 
/*    */   
/*    */   public K lastKey() {
/* 64 */     this.lock.readLock.lock();
/*    */     try {
/* 66 */       return (K)target().lastKey();
/*    */     } finally {
/* 68 */       this.lock.readLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> subMap(K fromKey, K toKey) {
/* 74 */     return new SubSortedMapImpl<K, V>(this, fromKey, toKey);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> tailMap(K fromKey) {
/* 79 */     return new SubSortedMapImpl<K, V>(this, fromKey, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMapService<K, V> threadSafe() {
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedMapService<K, V> target() {
/* 89 */     return (SortedMapService<K, V>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/SharedSortedMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */