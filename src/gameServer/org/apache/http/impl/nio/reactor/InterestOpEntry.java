/*    */ package org.apache.http.impl.nio.reactor;
/*    */ 
/*    */ import java.nio.channels.SelectionKey;
/*    */ import org.apache.http.util.Args;
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
/*    */ class InterestOpEntry
/*    */ {
/*    */   private final SelectionKey key;
/*    */   private final int eventMask;
/*    */   
/*    */   public InterestOpEntry(SelectionKey key, int eventMask) {
/* 47 */     Args.notNull(key, "Selection key");
/* 48 */     this.key = key;
/* 49 */     this.eventMask = eventMask;
/*    */   }
/*    */   
/*    */   public SelectionKey getSelectionKey() {
/* 53 */     return this.key;
/*    */   }
/*    */   
/*    */   public int getEventMask() {
/* 57 */     return this.eventMask;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 62 */     if (this == obj) {
/* 63 */       return true;
/*    */     }
/* 65 */     if (obj instanceof InterestOpEntry) {
/* 66 */       InterestOpEntry that = (InterestOpEntry)obj;
/* 67 */       return this.key.equals(that.key);
/*    */     } 
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 75 */     return this.key.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/InterestOpEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */