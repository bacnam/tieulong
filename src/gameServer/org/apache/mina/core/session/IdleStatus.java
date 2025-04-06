/*    */ package org.apache.mina.core.session;
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
/*    */ public class IdleStatus
/*    */ {
/* 41 */   public static final IdleStatus READER_IDLE = new IdleStatus("reader idle");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final IdleStatus WRITER_IDLE = new IdleStatus("writer idle");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static final IdleStatus BOTH_IDLE = new IdleStatus("both idle");
/*    */ 
/*    */   
/*    */   private final String strValue;
/*    */ 
/*    */ 
/*    */   
/*    */   private IdleStatus(String strValue) {
/* 59 */     this.strValue = strValue;
/*    */   }
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
/*    */   public String toString() {
/* 72 */     return this.strValue;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IdleStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */