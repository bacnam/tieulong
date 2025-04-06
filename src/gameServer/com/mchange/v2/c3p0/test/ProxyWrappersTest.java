/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import com.mchange.v2.c3p0.ComboPooledDataSource;
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
/*    */ public final class ProxyWrappersTest
/*    */ {
/*    */   public static void main(String[] argv) {
/* 48 */     ComboPooledDataSource cpds = null;
/* 49 */     Connection c = null;
/*    */     
/*    */     try {
/* 52 */       cpds = new ComboPooledDataSource();
/* 53 */       cpds.setDriverClass("org.postgresql.Driver");
/* 54 */       cpds.setJdbcUrl("jdbc:postgresql://localhost/c3p0-test");
/* 55 */       cpds.setUser("swaldman");
/* 56 */       cpds.setPassword("test");
/* 57 */       cpds.setMinPoolSize(5);
/* 58 */       cpds.setAcquireIncrement(5);
/* 59 */       cpds.setMaxPoolSize(20);
/*    */       
/* 61 */       c = cpds.getConnection();
/* 62 */       c.setAutoCommit(false);
/* 63 */       Statement stmt = c.createStatement();
/* 64 */       stmt.executeUpdate("CREATE TABLE pwtest_table (col1 char(5), col2 char(5))");
/* 65 */       ResultSet rs = stmt.executeQuery("SELECT * FROM pwtest_table");
/* 66 */       System.err.println("rs: " + rs);
/* 67 */       System.err.println("rs.getStatement(): " + rs.getStatement());
/* 68 */       System.err.println("rs.getStatement().getConnection(): " + rs.getStatement().getConnection());
/*    */     }
/* 70 */     catch (Exception e) {
/* 71 */       e.printStackTrace();
/*    */     } finally {
/*    */       
/* 74 */       try { if (c != null) c.rollback();  }
/* 75 */       catch (Exception e) { e.printStackTrace(); }
/* 76 */        try { if (cpds != null) cpds.close();  }
/* 77 */       catch (Exception e) { e.printStackTrace(); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/ProxyWrappersTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */