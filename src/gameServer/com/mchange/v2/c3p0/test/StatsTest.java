/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ public final class StatsTest
/*     */ {
/*     */   static void display(ComboPooledDataSource cpds) throws Exception {
/*  48 */     System.err.println("numConnections: " + cpds.getNumConnections());
/*  49 */     System.err.println("numBusyConnections: " + cpds.getNumBusyConnections());
/*  50 */     System.err.println("numIdleConnections: " + cpds.getNumIdleConnections());
/*  51 */     System.err.println("numUnclosedOrphanedConnections: " + cpds.getNumUnclosedOrphanedConnections());
/*  52 */     System.err.println();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] argv) {
/*     */     try {
/*  59 */       ComboPooledDataSource cpds = new ComboPooledDataSource();
/*  60 */       cpds.setJdbcUrl(argv[0]);
/*  61 */       cpds.setUser(argv[1]);
/*  62 */       cpds.setPassword(argv[2]);
/*  63 */       cpds.setMinPoolSize(5);
/*  64 */       cpds.setAcquireIncrement(5);
/*  65 */       cpds.setMaxPoolSize(20);
/*     */       
/*  67 */       System.err.println("Initial...");
/*  68 */       display(cpds);
/*  69 */       Thread.sleep(2000L);
/*     */       
/*  71 */       HashSet<Connection> hs = new HashSet();
/*  72 */       for (int i = 0; i < 20; i++) {
/*     */         
/*  74 */         Connection c = cpds.getConnection();
/*  75 */         hs.add(c);
/*  76 */         System.err.println("Adding (" + (i + 1) + ") " + c);
/*  77 */         display(cpds);
/*  78 */         Thread.sleep(1000L);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       int count = 0;
/*  90 */       for (Iterator<Connection> ii = hs.iterator(); ii.hasNext(); ) {
/*     */         
/*  92 */         Connection c = ii.next();
/*  93 */         System.err.println("Removing " + ++count);
/*  94 */         ii.remove(); try {
/*  95 */           c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
/*  96 */         } catch (Exception e) {
/*     */           
/*  98 */           System.err.println(e);
/*  99 */           System.err.println();
/*     */           
/*     */           continue;
/*     */         } finally {
/* 103 */           c.close();
/* 104 */         }  Thread.sleep(2000L);
/* 105 */         display(cpds);
/*     */       } 
/*     */       
/* 108 */       System.err.println("Closing data source, \"forcing\" garbage collection, and sleeping for 5 seconds...");
/* 109 */       cpds.close();
/* 110 */       System.gc();
/* 111 */       System.err.println("Main Thread: Sleeping for five seconds!");
/* 112 */       Thread.sleep(5000L);
/*     */ 
/*     */       
/* 115 */       System.err.println("Bye!");
/*     */     }
/* 117 */     catch (Exception e) {
/* 118 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/StatsTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */