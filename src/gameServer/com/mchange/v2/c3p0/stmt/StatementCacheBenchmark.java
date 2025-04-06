/*     */ package com.mchange.v2.c3p0.stmt;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSourceFactory;
/*     */ import com.mchange.v2.c3p0.PoolBackedDataSourceFactory;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ public final class StatementCacheBenchmark
/*     */ {
/*     */   static final String EMPTY_TABLE_CREATE = "CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))";
/*     */   static final String EMPTY_TABLE_SELECT = "SELECT * FROM emptyyukyuk";
/*     */   static final String EMPTY_TABLE_DROP = "DROP TABLE emptyyukyuk";
/*     */   static final String EMPTY_TABLE_CONDITIONAL_SELECT = "SELECT * FROM emptyyukyuk where a = ?";
/*     */   static final int NUM_ITERATIONS = 2000;
/*     */   
/*     */   public static void main(String[] argv) {
/*  56 */     DataSource ds_unpooled = null;
/*  57 */     DataSource ds_pooled = null;
/*     */ 
/*     */     
/*     */     try {
/*  61 */       String jdbc_url = null;
/*  62 */       String username = null;
/*  63 */       String password = null;
/*  64 */       if (argv.length == 3) {
/*     */         
/*  66 */         jdbc_url = argv[0];
/*  67 */         username = argv[1];
/*  68 */         password = argv[2];
/*     */       }
/*  70 */       else if (argv.length == 1) {
/*     */         
/*  72 */         jdbc_url = argv[0];
/*  73 */         username = null;
/*  74 */         password = null;
/*     */       } else {
/*     */         
/*  77 */         usage();
/*     */       } 
/*  79 */       if (!jdbc_url.startsWith("jdbc:")) {
/*  80 */         usage();
/*     */       }
/*  82 */       ds_unpooled = DriverManagerDataSourceFactory.create(jdbc_url, username, password);
/*  83 */       ds_pooled = PoolBackedDataSourceFactory.create(jdbc_url, username, password, 5, 20, 5, 0, 100);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       create(ds_pooled);
/*     */       
/*  95 */       perform(ds_pooled, "pooled");
/*  96 */       perform(ds_unpooled, "unpooled");
/*     */     }
/*  98 */     catch (Exception e) {
/*  99 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 102 */         drop(ds_pooled);
/* 103 */       } catch (Exception e) {
/* 104 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void perform(DataSource ds, String name) throws SQLException {
/* 111 */     Connection c = null;
/* 112 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 115 */       c = ds.getConnection();
/* 116 */       long start = System.currentTimeMillis();
/* 117 */       for (int i = 0; i < 2000; i++) {
/*     */         
/* 119 */         PreparedStatement test = c.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");
/*     */         
/* 121 */         test.close();
/*     */       } 
/* 123 */       long end = System.currentTimeMillis();
/* 124 */       System.err.println(name + " --> " + ((float)(end - start) / 2000.0F) + " [" + 'ߐ' + " iterations]");
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 130 */       StatementUtils.attemptClose(ps);
/* 131 */       ConnectionUtils.attemptClose(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage() {
/* 137 */     System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + StatementCacheBenchmark.class.getName() + " <jdbc_url> [<username> <password>]");
/*     */ 
/*     */ 
/*     */     
/* 141 */     System.exit(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void create(DataSource ds) throws SQLException {
/* 147 */     System.err.println("Creating test schema.");
/* 148 */     Connection con = null;
/* 149 */     PreparedStatement ps1 = null;
/*     */     
/*     */     try {
/* 152 */       con = ds.getConnection();
/* 153 */       ps1 = con.prepareStatement("CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))");
/* 154 */       ps1.executeUpdate();
/* 155 */       System.err.println("Test schema created.");
/*     */     }
/*     */     finally {
/*     */       
/* 159 */       StatementUtils.attemptClose(ps1);
/* 160 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void drop(DataSource ds) throws SQLException {
/* 167 */     Connection con = null;
/* 168 */     PreparedStatement ps1 = null;
/*     */     
/*     */     try {
/* 171 */       con = ds.getConnection();
/* 172 */       ps1 = con.prepareStatement("DROP TABLE emptyyukyuk");
/* 173 */       ps1.executeUpdate();
/*     */     }
/*     */     finally {
/*     */       
/* 177 */       StatementUtils.attemptClose(ps1);
/* 178 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/* 180 */     System.err.println("Test schema dropped.");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/stmt/StatementCacheBenchmark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */