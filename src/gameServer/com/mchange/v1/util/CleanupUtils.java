/*    */ package com.mchange.v1.util;
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
/*    */ public final class CleanupUtils
/*    */ {
/*    */   public static void attemptClose(Statement paramStatement) {
/*    */     try {
/* 48 */       if (paramStatement != null) paramStatement.close(); 
/* 49 */     } catch (SQLException sQLException) {
/* 50 */       sQLException.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public static void attemptClose(Connection paramConnection) {
/*    */     try {
/* 55 */       if (paramConnection != null) paramConnection.close(); 
/* 56 */     } catch (SQLException sQLException) {
/* 57 */       sQLException.printStackTrace();
/*    */     } 
/*    */   }
/*    */   public static void attemptRollback(Connection paramConnection) {
/*    */     try {
/* 62 */       if (paramConnection != null) paramConnection.rollback(); 
/* 63 */     } catch (SQLException sQLException) {
/* 64 */       sQLException.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/CleanupUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */