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
/*     */ public final class OneThreadRepeatedInsertOrQueryTest
/*     */ {
/*     */   static final String INSERT_STMT = "INSERT INTO testpbds VALUES ( ? , ? )";
/*     */   static final String SELECT_STMT = "SELECT count(*) FROM testpbds";
/*  49 */   static Random random = new Random();
/*     */   
/*     */   static DataSource ds;
/*     */   
/*     */   public static void main(String[] argv) {
/*  54 */     String jdbc_url = null;
/*  55 */     String username = null;
/*  56 */     String password = null;
/*  57 */     if (argv.length == 3) {
/*     */       
/*  59 */       jdbc_url = argv[0];
/*  60 */       username = argv[1];
/*  61 */       password = argv[2];
/*     */     }
/*  63 */     else if (argv.length == 1) {
/*     */       
/*  65 */       jdbc_url = argv[0];
/*  66 */       username = null;
/*  67 */       password = null;
/*     */     } else {
/*     */       
/*  70 */       usage();
/*     */     } 
/*  72 */     if (!jdbc_url.startsWith("jdbc:")) {
/*  73 */       usage();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  78 */       DataSource ds_unpooled = DataSources.unpooledDataSource(jdbc_url, username, password);
/*  79 */       ds = DataSources.pooledDataSource(ds_unpooled);
/*     */       
/*  81 */       Connection con = null;
/*  82 */       Statement stmt = null;
/*     */ 
/*     */       
/*     */       try {
/*  86 */         con = ds.getConnection();
/*  87 */         stmt = con.createStatement();
/*  88 */         stmt.executeUpdate("CREATE TABLE testpbds ( a varchar(16), b varchar(16) )");
/*     */       }
/*  90 */       catch (SQLException e) {
/*     */         
/*  92 */         e.printStackTrace();
/*  93 */         System.err.println("relation testpbds already exists, or something bad happened.");
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/*  98 */         StatementUtils.attemptClose(stmt);
/*  99 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */ 
/*     */       
/*     */       while (true) {
/* 104 */         con = null;
/*     */         
/*     */         try {
/* 107 */           con = ds.getConnection();
/* 108 */           boolean select = random.nextBoolean();
/* 109 */           if (select) {
/* 110 */             executeSelect(con);
/*     */           } else {
/* 112 */             executeInsert(con);
/*     */           } 
/* 114 */         } catch (Exception e) {
/* 115 */           e.printStackTrace();
/*     */         } finally {
/* 117 */           ConnectionUtils.attemptClose(con);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 122 */     } catch (Exception e) {
/* 123 */       e.printStackTrace();
/*     */       return;
/*     */     } 
/*     */   }
/*     */   static void executeInsert(Connection con) throws SQLException {
/* 128 */     PreparedStatement pstmt = null;
/*     */     
/*     */     try {
/* 131 */       pstmt = con.prepareStatement("INSERT INTO testpbds VALUES ( ? , ? )");
/* 132 */       pstmt.setInt(1, random.nextInt());
/* 133 */       pstmt.setInt(2, random.nextInt());
/* 134 */       pstmt.executeUpdate();
/* 135 */       System.out.println("INSERTION");
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */       
/* 143 */       StatementUtils.attemptClose(pstmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void executeSelect(Connection con) throws SQLException {
/* 149 */     long l = System.currentTimeMillis();
/* 150 */     PreparedStatement pstmt = null;
/* 151 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 154 */       pstmt = con.prepareStatement("SELECT count(*) FROM testpbds");
/* 155 */       rs = pstmt.executeQuery();
/* 156 */       rs.next();
/* 157 */       System.out.println("SELECT [count=" + rs.getInt(1) + ", time=" + (System.currentTimeMillis() - l) + " msecs]");
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 162 */       ResultSetUtils.attemptClose(rs);
/* 163 */       StatementUtils.attemptClose(pstmt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage() {
/* 169 */     System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + OneThreadRepeatedInsertOrQueryTest.class.getName() + " <jdbc_url> [<username> <password>]");
/*     */ 
/*     */ 
/*     */     
/* 173 */     System.exit(-1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/OneThreadRepeatedInsertOrQueryTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */