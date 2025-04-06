/*    */ package javolution.util.internal.map;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.service.MapService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnmodifiableMapImpl<K, V>
/*    */   extends MapView<K, V>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<Map.Entry<K, V>>
/*    */   {
/* 23 */     private final Iterator<Map.Entry<K, V>> targetIterator = UnmodifiableMapImpl.this.target().iterator();
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 28 */       return this.targetIterator.hasNext();
/*    */     }
/*    */ 
/*    */     
/*    */     public Map.Entry<K, V> next() {
/* 33 */       return this.targetIterator.next();
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 38 */       throw new UnsupportedOperationException("Read-Only Map.");
/*    */     }
/*    */     
/*    */     private IteratorImpl() {}
/*    */   }
/*    */   
/*    */   public UnmodifiableMapImpl(MapService<K, V> target) {
/* 45 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 50 */     throw new UnsupportedOperationException("Unmodifiable");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsKey(Object key) {
/* 55 */     return target().containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(Object key) {
/* 60 */     return (V)target().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Map.Entry<K, V>> iterator() {
/* 65 */     return new IteratorImpl();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super K> keyComparator() {
/* 70 */     return target().keyComparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public V put(K key, V value) {
/* 75 */     throw new UnsupportedOperationException("Unmodifiable");
/*    */   }
/*    */ 
/*    */   
/*    */   public V remove(Object key) {
/* 80 */     throw new UnsupportedOperationException("Unmodifiable");
/*    */   }
/*    */ 
/*    */   
/*    */   public MapService<K, V> threadSafe() {
/* 85 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super V> valueComparator() {
/* 90 */     return target().valueComparator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/UnmodifiableMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */