/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
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
/*    */ public class DBHelper
/*    */ {
/*    */   public static void closeConnection(Connection connection) {
/* 27 */     if (connection != null) {
/*    */       try {
/* 29 */         connection.close();
/* 30 */       } catch (SQLException sqle) {}
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeStatement(Statement statement) {
/* 38 */     if (statement != null)
/*    */       try {
/* 40 */         statement.close();
/* 41 */       } catch (SQLException sqle) {} 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/db/DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */