/*    */ package com.zhonglian.server.common.db;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.ResultSet;
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
/*    */ public class SQLUtil
/*    */ {
/*    */   public static Exception close(AutoCloseable rs) {
/*    */     try {
/* 23 */       if (rs != null) {
/* 24 */         rs.close();
/*    */       }
/* 26 */     } catch (Exception e) {
/* 27 */       return e;
/*    */     } 
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public static void close(ResultSet rs, Statement pstm, Connection con) {
/* 33 */     close(rs);
/* 34 */     close(pstm);
/* 35 */     close(con);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/SQLUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */