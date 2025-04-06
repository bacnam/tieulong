/*    */ package javolution.util.internal.map;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javolution.util.function.Consumer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SequentialMapImpl<K, V>
/*    */   extends MapView<K, V>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SequentialMapImpl(MapService<K, V> target) {
/* 25 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsKey(Object key) {
/* 30 */     return target().containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(Object key) {
/* 35 */     return (V)target().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Map.Entry<K, V>> iterator() {
/* 40 */     return target().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super K> keyComparator() {
/* 45 */     return target().keyComparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public void perform(Consumer<MapService<K, V>> action, MapService<K, V> view) {
/* 50 */     action.accept(view);
/*    */   }
/*    */ 
/*    */   
/*    */   public V put(K key, V value) {
/* 55 */     return (V)target().put(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public V remove(Object key) {
/* 60 */     return (V)target().remove(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
/* 65 */     action.accept(view);
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super V> valueComparator() {
/* 70 */     return target().valueComparator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/SequentialMapImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */