/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v1.db.sql.ResultSetUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.DataSources;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Random;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LoadPoolBackedDataSource
/*     */ {
/*     */   static final int NUM_THREADS = 100;
/*     */   static final int ITERATIONS_PER_THREAD = 1000;
/*     */   static DataSource ds;
/*     */   
/*     */   public static void main(String[] argv) {
/*  54 */     if (argv.length > 0) {
/*     */       
/*  56 */       System.err.println(LoadPoolBackedDataSource.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");
/*     */       
/*     */       return;
/*     */     } 
/*  60 */     String jdbc_url = null;
/*  61 */     String username = null;
/*  62 */     String password = null;
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
/*     */     try {
/*  87 */       DataSource ds_unpooled = DataSources.unpooledDataSource();
/*  88 */       ds = DataSources.pooledDataSource(ds_unpooled);
/*     */       
/*  90 */       Connection connection1 = null;
/*  91 */       Statement statement1 = null;
/*     */ 
/*     */       
/*     */       try {
/*  95 */         connection1 = ds.getConnection();
/*  96 */         statement1 = connection1.createStatement();
/*  97 */         statement1.executeUpdate("CREATE TABLE testpbds ( a varchar(16), b varchar(16) )");
/*  98 */         System.err.println("LoadPoolBackedDataSource -- TEST SCHEMA CREATED");
/*     */       }
/* 100 */       catch (SQLException e) {
/*     */         
/* 102 */         e.printStackTrace();
/* 103 */         System.err.println("relation testpbds already exists, or something bad happened.");
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 108 */         StatementUtils.attemptClose(statement1);
/* 109 */         ConnectionUtils.attemptClose(connection1);
/*     */       } 
/*     */       
/* 112 */       Thread[] threads = new Thread[100]; int i;
/* 113 */       for (i = 0; i < 100; i++) {
/*     */         
/* 115 */         Thread t = new ChurnThread(i);
/* 116 */         threads[i] = t;
/* 117 */         t.start();
/* 118 */         System.out.println("THREAD MADE [" + i + "]");
/* 119 */         Thread.sleep(500L);
/*     */       } 
/* 121 */       for (i = 0; i < 100; i++) {
/* 122 */         threads[i].join();
/*     */       }
/* 124 */     } catch (Exception e) {
/* 125 */       e.printStackTrace();
/*     */     } finally {
/*     */       
/* 128 */       Connection con = null;
/* 129 */       Statement stmt = null;
/*     */ 
/*     */       
/*     */       try {
/* 133 */         con = ds.getConnection();
/* 134 */         stmt = con.createStatement();
/* 135 */         stmt.executeUpdate("DROP TABLE testpbds");
/* 136 */         System.err.println("LoadPoolBackedDataSource -- TEST SCHEMA DROPPED");
/*     */       }
/* 138 */       catch (Exception e) {
/*     */         
/* 140 */         e.printStackTrace();
/*     */       }
/*     */       finally {
/*     */         
/* 144 */         StatementUtils.attemptClose(stmt);
/* 145 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static class ChurnThread
/*     */     extends Thread {
/* 152 */     Random random = new Random();
/*     */     
/*     */     int num;
/*     */     
/*     */     public ChurnThread(int num) {
/* 157 */       this.num = num;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 163 */         for (int i = 0; i < 1000; i++)
/*     */         {
/* 165 */           Connection con = null;
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
/*     */         }
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
/*     */       }
/* 197 */       catch (Exception e) {
/* 198 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static void executeInsert(Connection con, Random random) throws SQLException {
/* 204 */     Statement stmt = null;
/*     */     
/*     */     try {
/* 207 */       stmt = con.createStatement();
/* 208 */       stmt.executeUpdate("INSERT INTO testpbds VALUES ('" + random.nextInt() + "', '" + random.nextInt() + "')");
/*     */ 
/*     */       
/* 211 */       System.out.println("INSERTION");
/*     */     }
/*     */     finally {
/*     */       
/* 215 */       StatementUtils.attemptClose(stmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void executeDelete(Connection con) throws SQLException {
/* 221 */     Statement stmt = null;
/*     */     
/*     */     try {
/* 224 */       stmt = con.createStatement();
/* 225 */       stmt.executeUpdate("DELETE FROM testpbds;");
/* 226 */       System.out.println("DELETION");
/*     */     }
/*     */     finally {
/*     */       
/* 230 */       StatementUtils.attemptClose(stmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void executeSelect(Connection con) throws SQLException {
/* 236 */     long l = System.currentTimeMillis();
/* 237 */     Statement stmt = null;
/* 238 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 241 */       stmt = con.createStatement();
/* 242 */       rs = stmt.executeQuery("SELECT count(*) FROM testpbds");
/* 243 */       rs.next();
/* 244 */       System.out.println("SELECT [count=" + rs.getInt(1) + ", time=" + (System.currentTimeMillis() - l) + " msecs]");
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 249 */       ResultSetUtils.attemptClose(rs);
/* 250 */       StatementUtils.attemptClose(stmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage() {
/* 256 */     System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + LoadPoolBackedDataSource.class.getName() + " <jdbc_url> [<username> <password>]");
/*     */ 
/*     */ 
/*     */     
/* 260 */     System.exit(-1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/LoadPoolBackedDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */