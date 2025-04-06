/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v1.db.sql.ResultSetUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.DataSources;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ public final class PSLoadPoolBackedDataSource
/*     */ {
/*     */   static final String INSERT_STMT = "INSERT INTO testpbds VALUES ( ? , ? )";
/*     */   static final String SELECT_STMT = "SELECT count(*) FROM testpbds";
/*     */   static final String DELETE_STMT = "DELETE FROM testpbds";
/*     */   static DataSource ds;
/*     */   
/*     */   public static void main(String[] argv) {
/*  54 */     if (argv.length > 0) {
/*     */       
/*  56 */       System.err.println(PSLoadPoolBackedDataSource.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  61 */     String jdbc_url = null;
/*  62 */     String username = null;
/*  63 */     String password = null;
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
/*     */     try {
/*  90 */       DataSource ds_unpooled = DataSources.unpooledDataSource();
/*  91 */       ds = DataSources.pooledDataSource(ds_unpooled);
/*     */ 
/*     */ 
/*     */       
/*  95 */       Connection con = null;
/*  96 */       Statement stmt = null;
/*     */ 
/*     */       
/*     */       try {
/* 100 */         con = ds_unpooled.getConnection();
/* 101 */         stmt = con.createStatement();
/* 102 */         stmt.executeUpdate("CREATE TABLE testpbds ( a varchar(16), b varchar(16) )");
/*     */       }
/* 104 */       catch (SQLException e) {
/*     */         
/* 106 */         e.printStackTrace();
/* 107 */         System.err.println("relation testpbds already exists, or something bad happened.");
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 112 */         StatementUtils.attemptClose(stmt);
/* 113 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */ 
/*     */       
/* 117 */       for (int i = 0; i < 100; i++)
/*     */       {
/* 119 */         Thread t = new ChurnThread();
/* 120 */         t.start();
/* 121 */         System.out.println("THREAD MADE [" + i + "]");
/* 122 */         Thread.sleep(500L);
/*     */       }
/*     */     
/*     */     }
/* 126 */     catch (Exception e) {
/* 127 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   static class ChurnThread extends Thread {
/* 132 */     Random random = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/*     */         while (true) {
/* 141 */           Connection con = null;
/*     */           
/*     */           try {
/* 144 */             con = PSLoadPoolBackedDataSource.ds.getConnection();
/* 145 */             int select = this.random.nextInt(3);
/* 146 */             switch (select) {
/*     */               
/*     */               case 0:
/* 149 */                 PSLoadPoolBackedDataSource.executeSelect(con, this.random);
/*     */                 break;
/*     */               case 1:
/* 152 */                 PSLoadPoolBackedDataSource.executeInsert(con, this.random);
/*     */                 break;
/*     */               case 2:
/* 155 */                 PSLoadPoolBackedDataSource.executeDelete(con, this.random);
/*     */                 break;
/*     */             } 
/*     */ 
/*     */           
/* 160 */           } catch (Exception e) {
/* 161 */             e.printStackTrace();
/*     */           } finally {
/* 163 */             ConnectionUtils.attemptClose(con);
/*     */           } 
/*     */           
/* 166 */           Thread.sleep(1L);
/*     */         }
/*     */       
/* 169 */       } catch (Exception e) {
/* 170 */         e.printStackTrace();
/*     */         return;
/*     */       } 
/*     */     } }
/*     */   
/*     */   static void executeInsert(Connection con, Random random) throws SQLException {
/* 176 */     PreparedStatement pstmt = null;
/*     */     
/*     */     try {
/* 179 */       pstmt = con.prepareStatement("INSERT INTO testpbds VALUES ( ? , ? )");
/* 180 */       pstmt.setInt(1, random.nextInt());
/* 181 */       pstmt.setInt(2, random.nextInt());
/* 182 */       pstmt.executeUpdate();
/* 183 */       System.out.println("INSERTION");
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */       
/* 191 */       StatementUtils.attemptClose(pstmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void executeSelect(Connection con, Random random) throws SQLException {
/* 197 */     long l = System.currentTimeMillis();
/* 198 */     PreparedStatement pstmt = null;
/* 199 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 202 */       pstmt = con.prepareStatement("SELECT count(*) FROM testpbds");
/* 203 */       rs = pstmt.executeQuery();
/* 204 */       rs.next();
/* 205 */       System.out.println("SELECT [count=" + rs.getInt(1) + ", time=" + (System.currentTimeMillis() - l) + " msecs]");
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 210 */       ResultSetUtils.attemptClose(rs);
/* 211 */       StatementUtils.attemptClose(pstmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void executeDelete(Connection con, Random random) throws SQLException {
/* 217 */     PreparedStatement pstmt = null;
/* 218 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 221 */       pstmt = con.prepareStatement("DELETE FROM testpbds");
/* 222 */       int deleted = pstmt.executeUpdate();
/* 223 */       System.out.println("DELETE [" + deleted + " rows]");
/*     */     }
/*     */     finally {
/*     */       
/* 227 */       ResultSetUtils.attemptClose(rs);
/* 228 */       StatementUtils.attemptClose(pstmt);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/PSLoadPoolBackedDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */