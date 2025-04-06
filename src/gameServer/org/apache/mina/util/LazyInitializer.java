/*    */ package org.apache.mina.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LazyInitializer<V>
/*    */ {
/*    */   private V value;
/*    */   
/*    */   public abstract V init();
/*    */   
/*    */   public V get() {
/* 50 */     if (this.value == null) {
/* 51 */       this.value = init();
/*    */     }
/*    */     
/* 54 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/LazyInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */