/*    */ package com.mchange.v2.c3p0.example;
/*    */ 
/*    */ import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import java.sql.Connection;
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
/*    */ public class InitSqlConnectionCustomizer
/*    */   extends AbstractConnectionCustomizer
/*    */ {
/* 44 */   static final MLogger logger = MLog.getLogger(InitSqlConnectionCustomizer.class);
/*    */   
/*    */   private String getInitSql(String parentDataSourceIdentityToken) {
/* 47 */     return (String)extensionsForToken(parentDataSourceIdentityToken).get("initSql");
/*    */   }
/*    */   
/*    */   public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {
/* 51 */     String initSql = getInitSql(parentDataSourceIdentityToken);
/* 52 */     if (initSql != null) {
/*    */       
/* 54 */       Statement stmt = null;
/*    */       
/*    */       try {
/* 57 */         stmt = c.createStatement();
/* 58 */         int num = stmt.executeUpdate(initSql);
/* 59 */         if (logger.isLoggable(MLevel.FINEST)) {
/* 60 */           logger.log(MLevel.FINEST, "Initialized checked-out Connection '" + c + "' with initSql '" + initSql + "'. Return value: " + num);
/*    */         }
/*    */       } finally {
/* 63 */         if (stmt != null) stmt.close(); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/example/InitSqlConnectionCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */