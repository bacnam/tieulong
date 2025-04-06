/*    */ package javolution.util.internal.map.sorted;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class MapEntryImpl<K, V>
/*    */   implements Map.Entry<K, V>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   K key;
/*    */   V value;
/*    */   
/*    */   public MapEntryImpl(K key, V value) {
/* 24 */     this.key = key;
/* 25 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public K getKey() {
/* 30 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public V getValue() {
/* 35 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public V setValue(V value) {
/* 40 */     V oldValue = this.value;
/* 41 */     this.value = value;
/* 42 */     return oldValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/map/sorted/MapEntryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */