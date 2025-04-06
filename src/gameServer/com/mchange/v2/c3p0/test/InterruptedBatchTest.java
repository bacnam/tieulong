/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import com.mchange.v2.c3p0.DataSources;
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Timestamp;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InterruptedBatchTest
/*     */ {
/*  44 */   static DataSource ds_unpooled = null;
/*  45 */   static DataSource ds_pooled = null;
/*     */ 
/*     */   
/*     */   public static void main(String[] argv) {
/*  49 */     if (argv.length > 0) {
/*     */       
/*  51 */       System.err.println(C3P0BenchmarkApp.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  58 */       ds_unpooled = (DataSource)new DriverManagerDataSource();
/*  59 */       ComboPooledDataSource cpds = new ComboPooledDataSource();
/*  60 */       ds_pooled = (DataSource)cpds;
/*     */       
/*  62 */       attemptSetupTable();
/*     */       
/*  64 */       performTransaction(true);
/*  65 */       performTransaction(false);
/*     */       
/*  67 */       checkCount();
/*     */     }
/*  69 */     catch (Throwable t) {
/*     */       
/*  71 */       System.err.print("Aborting tests on Throwable -- ");
/*  72 */       t.printStackTrace();
/*  73 */       if (t instanceof Error) {
/*  74 */         throw (Error)t;
/*     */       }
/*     */     } finally {
/*     */       try {
/*  78 */         DataSources.destroy(ds_pooled);
/*  79 */       } catch (Exception e) {
/*  80 */         e.printStackTrace();
/*     */       }  try {
/*  82 */         DataSources.destroy(ds_unpooled);
/*  83 */       } catch (Exception e) {
/*  84 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void performTransaction(boolean throwAnException) throws SQLException {
/*  90 */     Connection con = null;
/*  91 */     PreparedStatement prepStat = null;
/*     */ 
/*     */     
/*     */     try {
/*  95 */       con = ds_pooled.getConnection();
/*  96 */       con.setAutoCommit(false);
/*     */       
/*  98 */       prepStat = con.prepareStatement("INSERT INTO CG_TAROPT_LOG(CO_ID, ENTDATE, CS_SEQNO, DESCRIPTION) VALUES (?,?,?,?)");
/*     */       
/* 100 */       prepStat.setLong(1, -665L);
/* 101 */       prepStat.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
/* 102 */       prepStat.setInt(3, 1);
/* 103 */       prepStat.setString(4, "time: " + System.currentTimeMillis());
/*     */       
/* 105 */       prepStat.addBatch();
/*     */       
/* 107 */       if (throwAnException) {
/* 108 */         throw new NullPointerException("my exception");
/*     */       }
/* 110 */       prepStat.executeBatch();
/*     */       
/* 112 */       con.commit();
/*     */     }
/* 114 */     catch (Exception e) {
/*     */       
/* 116 */       System.out.println("exception caught (NPE expected): ");
/* 117 */       e.printStackTrace();
/*     */     } finally {
/*     */ 
/*     */       
/* 121 */       try { if (prepStat != null) prepStat.close();  } catch (Exception e) { e.printStackTrace(); }
/* 122 */        try { con.close(); } catch (Exception e) { e.printStackTrace(); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void attemptSetupTable() throws Exception {
/* 128 */     Connection con = null;
/* 129 */     Statement stmt = null;
/*     */     
/*     */     try {
/* 132 */       con = ds_pooled.getConnection();
/* 133 */       stmt = con.createStatement();
/*     */       
/*     */       try {
/* 136 */         stmt.executeUpdate("CREATE TABLE CG_TAROPT_LOG ( CO_ID INTEGER, ENTDATE TIMESTAMP, CS_SEQNO INTEGER, DESCRIPTION VARCHAR(32) )");
/*     */       }
/* 138 */       catch (SQLException e) {
/*     */         
/* 140 */         System.err.println("Table already constructed?");
/* 141 */         e.printStackTrace();
/*     */       } 
/*     */       
/* 144 */       stmt.executeUpdate("DELETE FROM CG_TAROPT_LOG");
/*     */     } finally {
/*     */ 
/*     */       
/* 148 */       try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
/* 149 */        try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void checkCount() throws Exception {
/* 155 */     Connection con = null;
/* 156 */     Statement stmt = null;
/* 157 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 160 */       con = ds_pooled.getConnection();
/* 161 */       stmt = con.createStatement();
/*     */       
/* 163 */       rs = stmt.executeQuery("SELECT COUNT(*) FROM CG_TAROPT_LOG");
/* 164 */       rs.next();
/* 165 */       System.out.println(rs.getInt(1) + " rows found. (one row expected.)");
/*     */     } finally {
/*     */ 
/*     */       
/* 169 */       try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
/* 170 */        try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
/*     */     
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/InterruptedBatchTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */