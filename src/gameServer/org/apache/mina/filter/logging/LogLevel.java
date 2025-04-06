/*    */ package org.apache.mina.filter.logging;
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
/*    */ public enum LogLevel
/*    */ {
/* 34 */   TRACE(5),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   DEBUG(4),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   INFO(3),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   WARN(2),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   ERROR(1),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   NONE(0);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int level;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   LogLevel(int level) {
/* 70 */     this.level = level;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLevel() {
/* 77 */     return this.level;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/logging/LogLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */