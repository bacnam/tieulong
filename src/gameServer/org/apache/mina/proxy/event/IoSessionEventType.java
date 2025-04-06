/*    */ package org.apache.mina.proxy.event;
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
/*    */ public enum IoSessionEventType
/*    */ {
/* 29 */   CREATED(1), OPENED(2), IDLE(3), CLOSED(4);
/*    */ 
/*    */   
/*    */   private final int id;
/*    */ 
/*    */ 
/*    */   
/*    */   IoSessionEventType(int id) {
/* 37 */     this.id = id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     switch (this) {
/*    */       case CREATED:
/* 56 */         return "- CREATED event -";
/*    */       case OPENED:
/* 58 */         return "- OPENED event -";
/*    */       case IDLE:
/* 60 */         return "- IDLE event -";
/*    */       case CLOSED:
/* 62 */         return "- CLOSED event -";
/*    */     } 
/* 64 */     return "- Event Id=" + this.id + " -";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/event/IoSessionEventType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */