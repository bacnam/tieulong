/*    */ package ch.qos.logback.core.db.dialect;
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
/*    */ public class MySQLDialect
/*    */   implements SQLDialect
/*    */ {
/*    */   public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
/*    */   
/*    */   public String getSelectInsertId() {
/* 26 */     return "SELECT LAST_INSERT_ID()";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/db/dialect/MySQLDialect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */