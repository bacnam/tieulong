/*    */ package javolution.util.internal.map;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ 
/*    */ public final class MapEntryImpl<K, V>
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   int hash;
/*    */   K key;
/*    */   MapEntryImpl<K, V> next;
/*    */   MapEntryImpl<K, V> previous;
/*    */   V value;
/*    */   
/*    */   public K getKey() {
/* 26 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public V getValue() {
/* 31 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public V setValue(V value) {
/* 36 */     V oldValue = this.value;
/* 37 */     this.value = value;
/* 38 */     return oldValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/MapEntryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */