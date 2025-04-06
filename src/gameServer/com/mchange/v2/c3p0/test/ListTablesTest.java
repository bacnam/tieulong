/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import com.mchange.v1.db.sql.ConnectionUtils;
/*    */ import com.mchange.v1.db.sql.ResultSetUtils;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DatabaseMetaData;
/*    */ import java.sql.ResultSet;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.sql.DataSource;
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
/*    */ public final class ListTablesTest
/*    */ {
/*    */   public static void main(String[] argv) {
/*    */     try {
/* 49 */       InitialContext ctx = new InitialContext();
/* 50 */       DataSource ds = (DataSource)ctx.lookup(argv[0]);
/* 51 */       System.err.println(ds.getClass());
/* 52 */       Connection con = null;
/* 53 */       ResultSet rs = null;
/*    */       
/*    */       try {
/* 56 */         con = ds.getConnection();
/* 57 */         DatabaseMetaData md = con.getMetaData();
/* 58 */         rs = md.getTables(null, null, "%", null);
/* 59 */         while (rs.next()) {
/* 60 */           System.out.println(rs.getString(3));
/*    */         }
/*    */       } finally {
/*    */         
/* 64 */         ResultSetUtils.attemptClose(rs);
/* 65 */         ConnectionUtils.attemptClose(con);
/*    */       }
/*    */     
/* 68 */     } catch (Exception e) {
/* 69 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/ListTablesTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */