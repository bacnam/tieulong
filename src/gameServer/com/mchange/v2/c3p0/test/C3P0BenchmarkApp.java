/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v1.db.sql.ResultSetUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import com.mchange.v2.c3p0.DataSources;
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class C3P0BenchmarkApp
/*     */ {
/*     */   static final String EMPTY_TABLE_CREATE = "CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))";
/*     */   static final String EMPTY_TABLE_SELECT = "SELECT * FROM emptyyukyuk";
/*     */   static final String EMPTY_TABLE_DROP = "DROP TABLE emptyyukyuk";
/*     */   static final String EMPTY_TABLE_CONDITIONAL_SELECT = "SELECT * FROM emptyyukyuk where a = ?";
/*     */   static final String N_ENTRY_TABLE_CREATE = "CREATE TABLE n_entryyukyuk (a INTEGER)";
/*     */   static final String N_ENTRY_TABLE_INSERT = "INSERT INTO n_entryyukyuk VALUES ( ? )";
/*     */   static final String N_ENTRY_TABLE_SELECT = "SELECT * FROM n_entryyukyuk";
/*     */   static final String N_ENTRY_TABLE_DROP = "DROP TABLE n_entryyukyuk";
/*     */   static final int NUM_ITERATIONS = 2000;
/*     */   
/*     */   public static void main(String[] argv) {
/*     */     DriverManagerDataSource driverManagerDataSource;
/*     */     ComboPooledDataSource comboPooledDataSource;
/*  63 */     if (argv.length > 0) {
/*     */       
/*  65 */       System.err.println(C3P0BenchmarkApp.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  70 */     DataSource ds_unpooled = null;
/*  71 */     DataSource ds_pooled = null;
/*     */     
/*     */     try {
/*  74 */       driverManagerDataSource = new DriverManagerDataSource();
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
/*  90 */       ComboPooledDataSource cpds = new ComboPooledDataSource();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       comboPooledDataSource = cpds;
/*     */       
/*  98 */       comboPooledDataSource.getParentLogger().info("Pooled DataSource created.");
/*     */       
/* 100 */       create((DataSource)comboPooledDataSource);
/*     */       
/* 102 */       System.out.println("Please wait. Tests can be very slow.");
/* 103 */       List<ConnectionAcquisitionTest> l = new ArrayList();
/* 104 */       l.add(new ConnectionAcquisitionTest());
/* 105 */       l.add(new StatementCreateTest());
/* 106 */       l.add(new StatementEmptyTableSelectTest());
/*     */       
/* 108 */       l.add(new PreparedStatementEmptyTableSelectTest());
/* 109 */       l.add(new PreparedStatementAcquireTest());
/* 110 */       l.add(new ResultSetReadTest());
/* 111 */       l.add(new FiveThreadPSQueryTestTest());
/* 112 */       for (int i = 0, len = l.size(); i < len; i++) {
/* 113 */         ((Test)l.get(i)).perform((DataSource)driverManagerDataSource, (DataSource)comboPooledDataSource, 2000);
/*     */       }
/* 115 */     } catch (Throwable t) {
/*     */       
/* 117 */       System.err.print("Aborting tests on Throwable -- ");
/* 118 */       t.printStackTrace();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (t instanceof Error) {
/* 126 */         throw (Error)t;
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 132 */         drop((DataSource)comboPooledDataSource);
/* 133 */       } catch (Exception e) {
/* 134 */         e.printStackTrace();
/*     */       }  try {
/* 136 */         DataSources.destroy((DataSource)comboPooledDataSource);
/* 137 */       } catch (Exception e) {
/* 138 */         e.printStackTrace();
/*     */       }  try {
/* 140 */         DataSources.destroy((DataSource)driverManagerDataSource);
/* 141 */       } catch (Exception e) {
/* 142 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */   static void create(DataSource ds) throws SQLException {
/* 159 */     System.err.println("Creating test schema.");
/* 160 */     Connection con = null;
/* 161 */     PreparedStatement ps1 = null;
/* 162 */     PreparedStatement ps2 = null;
/* 163 */     PreparedStatement ps3 = null;
/*     */     
/*     */     try {
/* 166 */       con = ds.getConnection();
/* 167 */       ps1 = con.prepareStatement("CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))");
/* 168 */       ps2 = con.prepareStatement("CREATE TABLE n_entryyukyuk (a INTEGER)");
/* 169 */       ps3 = con.prepareStatement("INSERT INTO n_entryyukyuk VALUES ( ? )");
/*     */       
/* 171 */       ps1.executeUpdate();
/* 172 */       ps2.executeUpdate();
/*     */       
/* 174 */       for (int i = 0; i < 2000; i++) {
/*     */         
/* 176 */         ps3.setInt(1, i);
/* 177 */         ps3.executeUpdate();
/* 178 */         System.err.print('.');
/*     */       } 
/* 180 */       System.err.println();
/* 181 */       System.err.println("Test schema created.");
/*     */     }
/*     */     finally {
/*     */       
/* 185 */       StatementUtils.attemptClose(ps1);
/* 186 */       StatementUtils.attemptClose(ps2);
/* 187 */       StatementUtils.attemptClose(ps3);
/* 188 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void drop(DataSource ds) throws SQLException {
/* 195 */     Connection con = null;
/* 196 */     PreparedStatement ps1 = null;
/* 197 */     PreparedStatement ps2 = null;
/*     */     
/*     */     try {
/* 200 */       con = ds.getConnection();
/* 201 */       ps1 = con.prepareStatement("DROP TABLE emptyyukyuk");
/* 202 */       ps2 = con.prepareStatement("DROP TABLE n_entryyukyuk");
/*     */       
/* 204 */       ps1.executeUpdate();
/* 205 */       ps2.executeUpdate();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 210 */       System.err.println("Test schema dropped.");
/*     */     }
/*     */     finally {
/*     */       
/* 214 */       StatementUtils.attemptClose(ps1);
/* 215 */       StatementUtils.attemptClose(ps2);
/* 216 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/*     */   }
/*     */   
/*     */   static abstract class Test
/*     */   {
/*     */     String name;
/*     */     
/*     */     Test(String name) {
/* 225 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void perform(DataSource unpooled, DataSource pooled, int iterations) throws Exception {
/* 229 */       double msecs_unpooled = test(unpooled, iterations) / iterations;
/* 230 */       double msecs_pooled = test(pooled, iterations) / iterations;
/* 231 */       System.out.println(this.name + " [ " + iterations + " iterations ]:");
/* 232 */       System.out.println("\tunpooled: " + msecs_unpooled + " msecs");
/* 233 */       System.out.println("\t  pooled: " + msecs_pooled + " msecs");
/* 234 */       System.out.println("\tspeed-up factor: " + (msecs_unpooled / msecs_pooled) + " times");
/* 235 */       System.out.println("\tspeed-up absolute: " + (msecs_unpooled - msecs_pooled) + " msecs");
/*     */       
/* 237 */       System.out.println();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract long test(DataSource param1DataSource, int param1Int) throws Exception;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConnectionAcquisitionTest
/*     */     extends Test
/*     */   {
/*     */     ConnectionAcquisitionTest() {
/* 252 */       super("Connection Acquisition and Cleanup");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long test(DataSource ds, int n) throws Exception {
/* 259 */       long start = System.currentTimeMillis();
/* 260 */       for (int i = 0; i < n; i++)
/*     */       {
/* 262 */         Connection con = null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 269 */       long end = System.currentTimeMillis();
/* 270 */       return end - start;
/*     */     }
/*     */   }
/*     */   
/*     */   static class StatementCreateTest
/*     */     extends Test {
/*     */     StatementCreateTest() {
/* 277 */       super("Statement Creation and Cleanup");
/*     */     }
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 281 */       Connection con = null;
/*     */       
/*     */       try {
/* 284 */         con = ds.getConnection();
/* 285 */         return test(con, n);
/*     */       } finally {
/*     */         
/* 288 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long test(Connection con, int n) throws SQLException {
/* 297 */       Statement stmt = null;
/* 298 */       long start = System.currentTimeMillis();
/* 299 */       for (int i = 0; i < n; i++) {
/*     */ 
/*     */         
/* 302 */         try { stmt = con.createStatement();
/*     */           
/* 304 */           StatementUtils.attemptClose(stmt); } finally { StatementUtils.attemptClose(stmt); }
/*     */       
/* 306 */       }  long end = System.currentTimeMillis();
/* 307 */       return end - start;
/*     */     }
/*     */   }
/*     */   
/*     */   static class StatementEmptyTableSelectTest
/*     */     extends Test
/*     */   {
/*     */     StatementEmptyTableSelectTest() {
/* 315 */       super("Empty Table Statement Select (on a single Statement)");
/*     */     }
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 319 */       Connection con = null;
/* 320 */       Statement stmt = null;
/*     */       
/*     */       try {
/* 323 */         con = ds.getConnection();
/* 324 */         stmt = con.createStatement();
/*     */         
/* 326 */         return test(stmt, n);
/*     */       }
/*     */       finally {
/*     */         
/* 330 */         StatementUtils.attemptClose(stmt);
/* 331 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long test(Statement stmt, int n) throws SQLException {
/* 340 */       long start = System.currentTimeMillis();
/* 341 */       for (int i = 0; i < n; i++)
/* 342 */         stmt.executeQuery("SELECT * FROM emptyyukyuk").close(); 
/* 343 */       long end = System.currentTimeMillis();
/* 344 */       return end - start;
/*     */     }
/*     */   }
/*     */   
/*     */   static class DataBaseMetaDataListNonexistentTablesTest
/*     */     extends Test {
/*     */     DataBaseMetaDataListNonexistentTablesTest() {
/* 351 */       super("DataBaseMetaDataListNonexistentTablesTest");
/*     */     }
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 355 */       Connection con = null;
/* 356 */       Statement stmt = null;
/*     */       
/*     */       try {
/* 359 */         con = ds.getConnection();
/* 360 */         return test(con, n);
/*     */       }
/*     */       finally {
/*     */         
/* 364 */         StatementUtils.attemptClose(stmt);
/* 365 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     long test(Connection con, int n) throws SQLException {
/* 371 */       ResultSet rs = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 378 */         long start = System.currentTimeMillis();
/* 379 */         for (int i = 0; i < n; i++) {
/* 380 */           rs = con.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
/*     */         } 
/*     */ 
/*     */         
/* 384 */         long end = System.currentTimeMillis();
/* 385 */         return end - start;
/*     */       } finally {
/*     */         
/* 388 */         ResultSetUtils.attemptClose(rs);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class PreparedStatementAcquireTest extends Test {
/*     */     PreparedStatementAcquireTest() {
/* 395 */       super("Acquire and Cleanup a PreparedStatement (same statement, many times)");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 402 */       Connection con = null;
/* 403 */       PreparedStatement pstmt = null;
/*     */       
/*     */       try {
/* 406 */         con = ds.getConnection();
/* 407 */         long start = System.currentTimeMillis();
/* 408 */         for (int i = 0; i < n; i++) {
/*     */ 
/*     */           
/* 411 */           try { pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");
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
/* 427 */             StatementUtils.attemptClose(pstmt); } finally { StatementUtils.attemptClose(pstmt); }
/*     */         
/* 429 */         }  long end = System.currentTimeMillis();
/* 430 */         return end - start;
/*     */       } finally {
/*     */         
/* 433 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class PreparedStatementEmptyTableSelectTest extends Test {
/*     */     PreparedStatementEmptyTableSelectTest() {
/* 440 */       super("Empty Table PreparedStatement Select (on a single PreparedStatement)");
/*     */     }
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 444 */       Connection con = null;
/* 445 */       PreparedStatement pstmt = null;
/*     */       
/*     */       try {
/* 448 */         con = ds.getConnection();
/* 449 */         pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 457 */         return test(pstmt, n);
/*     */       }
/*     */       finally {
/*     */         
/* 461 */         StatementUtils.attemptClose(pstmt);
/* 462 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long test(PreparedStatement pstmt, int n) throws SQLException {
/* 471 */       long start = System.currentTimeMillis();
/* 472 */       for (int i = 0; i < n; i++)
/* 473 */         pstmt.executeQuery().close(); 
/* 474 */       long end = System.currentTimeMillis();
/* 475 */       return end - start;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ResultSetReadTest
/*     */     extends Test {
/*     */     ResultSetReadTest() {
/* 482 */       super("Reading one row / one entry from a result set");
/*     */     }
/*     */     
/*     */     protected long test(DataSource ds, int n) throws SQLException {
/* 486 */       if (n > 10000) {
/* 487 */         throw new IllegalArgumentException("10K max.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 492 */       Connection con = null;
/* 493 */       PreparedStatement pstmt = null;
/* 494 */       ResultSet rs = null;
/*     */ 
/*     */       
/*     */       try {
/* 498 */         con = ds.getConnection();
/* 499 */         pstmt = con.prepareStatement("SELECT * FROM n_entryyukyuk");
/* 500 */         rs = pstmt.executeQuery();
/*     */         
/* 502 */         long start = System.currentTimeMillis();
/* 503 */         for (int i = 0; i < n; i++) {
/*     */           
/* 505 */           if (!rs.next())
/* 506 */             System.err.println("huh?"); 
/* 507 */           rs.getInt(1);
/*     */         } 
/* 509 */         long end = System.currentTimeMillis();
/* 510 */         return end - start;
/*     */       }
/*     */       finally {
/*     */         
/* 514 */         ResultSetUtils.attemptClose(rs);
/* 515 */         StatementUtils.attemptClose(pstmt);
/* 516 */         ConnectionUtils.attemptClose(con);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class FiveThreadPSQueryTestTest
/*     */     extends Test
/*     */   {
/*     */     FiveThreadPSQueryTestTest() {
/* 528 */       super("Five threads getting a connection, executing a query, " + System.getProperty("line.separator") + "and retrieving results concurrently via a prepared statement (in a transaction).");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected long test(final DataSource ds, final int n) throws Exception {
/*     */       class QueryThread
/*     */         extends Thread
/*     */       {
/*     */         QueryThread(int num) {
/* 538 */           super("QueryThread-" + num);
/*     */         }
/*     */         
/*     */         public void run() {
/* 542 */           Connection con = null;
/* 543 */           PreparedStatement pstmt = null;
/* 544 */           ResultSet rs = null;
/*     */           
/* 546 */           for (int i = 0; i < n / 5; i++) {
/*     */ 
/*     */ 
/*     */             
/* 550 */             try { con = ds.getConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 557 */               con.setAutoCommit(false);
/*     */               
/* 559 */               pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 569 */               pstmt.setString(1, "boo");
/* 570 */               rs = pstmt.executeQuery();
/* 571 */               while (rs.next()) {
/* 572 */                 System.err.println("Huh?? Empty table has values?");
/*     */               }
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
/* 584 */               con.commit();
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
/* 599 */               ResultSetUtils.attemptClose(rs);
/* 600 */               StatementUtils.attemptClose(pstmt);
/* 601 */               ConnectionUtils.attemptClose(con);
/* 602 */               con = null; } catch (Exception e) { System.err.print("FiveThreadPSQueryTestTest exception -- "); e.printStackTrace(); try { if (con != null) con.rollback();  } catch (SQLException e2) { System.err.print("Rollback on exception failed! -- "); e2.printStackTrace(); }  } finally { ResultSetUtils.attemptClose(rs); StatementUtils.attemptClose(pstmt); ConnectionUtils.attemptClose(con); con = null; }
/*     */           
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 615 */       long start = System.currentTimeMillis();
/*     */       
/* 617 */       Thread[] ts = new Thread[5]; int i;
/* 618 */       for (i = 0; i < 5; i++) {
/*     */         
/* 620 */         ts[i] = new QueryThread(i);
/* 621 */         ts[i].start();
/*     */       } 
/* 623 */       for (i = 0; i < 5; i++) {
/* 624 */         ts[i].join();
/*     */       }
/* 626 */       return System.currentTimeMillis() - start;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/C3P0BenchmarkApp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */