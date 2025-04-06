/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public final class ConnectionDispersionTest
/*     */ {
/*     */   private static final int DELAY_TIME = 120000;
/*     */   private static final int NUM_THREADS = 600;
/*  53 */   private static final Integer ZERO = new Integer(0);
/*     */   
/*     */   private static boolean should_go = false;
/*     */   
/*     */   private static DataSource cpds;
/*     */   
/*  59 */   private static int ready_count = 0;
/*     */   
/*     */   private static synchronized void setDataSource(DataSource ds) {
/*  62 */     cpds = ds;
/*     */   }
/*     */   private static synchronized DataSource getDataSource() {
/*  65 */     return cpds;
/*     */   }
/*     */   private static synchronized int ready() {
/*  68 */     return ++ready_count;
/*     */   }
/*     */   private static synchronized boolean isReady() {
/*  71 */     return (ready_count == 600);
/*     */   }
/*     */   
/*     */   private static synchronized void start() {
/*  75 */     should_go = true;
/*  76 */     ConnectionDispersionTest.class.notifyAll();
/*     */   }
/*     */ 
/*     */   
/*     */   private static synchronized void stop() {
/*  81 */     should_go = false;
/*  82 */     ConnectionDispersionTest.class.notifyAll();
/*     */   }
/*     */   
/*     */   private static synchronized boolean shouldGo() {
/*  86 */     return should_go;
/*     */   }
/*     */   
/*     */   public static void main(String[] argv) {
/*  90 */     String jdbc_url = null;
/*  91 */     String username = null;
/*  92 */     String password = null;
/*  93 */     if (argv.length == 3) {
/*     */       
/*  95 */       jdbc_url = argv[0];
/*  96 */       username = argv[1];
/*  97 */       password = argv[2];
/*     */     }
/*  99 */     else if (argv.length == 1) {
/*     */       
/* 101 */       jdbc_url = argv[0];
/* 102 */       username = null;
/* 103 */       password = null;
/*     */     } else {
/*     */       
/* 106 */       usage();
/*     */     } 
/* 108 */     if (!jdbc_url.startsWith("jdbc:")) {
/* 109 */       usage();
/*     */     }
/*     */     
/*     */     try {
/* 113 */       ComboPooledDataSource ds = new ComboPooledDataSource();
/* 114 */       ds.setJdbcUrl(jdbc_url);
/* 115 */       ds.setUser(username);
/* 116 */       ds.setPassword(password);
/* 117 */       setDataSource((DataSource)ds);
/*     */ 
/*     */       
/* 120 */       ds.getConnection().close();
/*     */       
/* 122 */       System.err.println("Generating thread list...");
/* 123 */       List<Thread> threads = new ArrayList(600); int i;
/* 124 */       for (i = 0; i < 600; i++) {
/*     */         
/* 126 */         Thread t = new CompeteThread();
/* 127 */         t.start();
/* 128 */         threads.add(t);
/* 129 */         Thread.currentThread(); Thread.yield();
/*     */       } 
/* 131 */       System.err.println("Thread list generated.");
/*     */       
/* 133 */       synchronized (ConnectionDispersionTest.class) {
/* 134 */         for (; !isReady(); ConnectionDispersionTest.class.wait());
/*     */       } 
/* 136 */       System.err.println("Starting the race.");
/* 137 */       start();
/*     */       
/* 139 */       System.err.println("Sleeping 120.0 seconds to let the race run");
/*     */       
/* 141 */       Thread.sleep(120000L);
/* 142 */       System.err.println("Stopping the race.");
/* 143 */       stop();
/*     */       
/* 145 */       System.err.println("Waiting for Threads to complete.");
/* 146 */       for (i = 0; i < 600; i++) {
/* 147 */         ((Thread)threads.get(i)).join();
/*     */       }
/* 149 */       Map<Object, Object> outcomeMap = new TreeMap<Object, Object>();
/* 150 */       for (int j = 0; j < 600; j++) {
/*     */         
/* 152 */         Integer outcome = new Integer(((CompeteThread)threads.get(j)).getCount());
/* 153 */         Integer old = (Integer)outcomeMap.get(outcome);
/* 154 */         if (old == null)
/* 155 */           old = ZERO; 
/* 156 */         outcomeMap.put(outcome, new Integer(old.intValue() + 1));
/*     */       } 
/*     */       
/* 159 */       int last = 0;
/* 160 */       for (Iterator<Integer> ii = outcomeMap.keySet().iterator(); ii.hasNext(); )
/*     */       {
/* 162 */         Integer outcome = ii.next();
/* 163 */         Integer count = (Integer)outcomeMap.get(outcome);
/* 164 */         int oc = outcome.intValue();
/* 165 */         int c = count.intValue();
/* 166 */         for (; last < oc; last++)
/* 167 */           System.out.println(String.valueOf(10000 + last).substring(1) + ": "); 
/* 168 */         last++;
/* 169 */         System.out.print(String.valueOf(10000 + oc).substring(1) + ": ");
/*     */ 
/*     */         
/* 172 */         for (int k = 0; k < c; k++)
/* 173 */           System.out.print('*'); 
/* 174 */         System.out.println();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 186 */     catch (Exception e) {
/* 187 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   static class CompeteThread extends Thread {
/*     */     DataSource ds;
/*     */     int count;
/*     */     
/*     */     synchronized void increment() {
/* 196 */       this.count++;
/*     */     }
/*     */     synchronized int getCount() {
/* 199 */       return this.count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 205 */         this.ds = ConnectionDispersionTest.getDataSource();
/* 206 */         synchronized (ConnectionDispersionTest.class) {
/*     */ 
/*     */ 
/*     */           
/* 210 */           ConnectionDispersionTest.ready();
/* 211 */           ConnectionDispersionTest.class.wait();
/*     */         } 
/*     */ 
/*     */         
/* 215 */         while (ConnectionDispersionTest.shouldGo()) {
/*     */           
/* 217 */           Connection c = null;
/* 218 */           ResultSet rs = null;
/*     */           
/*     */           try {
/* 221 */             c = this.ds.getConnection();
/* 222 */             increment();
/* 223 */             rs = c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 228 */           catch (SQLException e) {
/* 229 */             e.printStackTrace();
/*     */           } finally {
/*     */             try {
/* 232 */               if (rs != null) rs.close(); 
/* 233 */             } catch (Exception e) {
/* 234 */               e.printStackTrace();
/*     */             }  try {
/* 236 */               if (c != null) c.close(); 
/* 237 */             } catch (Exception e) {
/* 238 */               e.printStackTrace();
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/* 243 */       } catch (Exception e) {
/* 244 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static void usage() {
/* 250 */     System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + ConnectionDispersionTest.class.getName() + " <jdbc_url> [<username> <password>]");
/*     */ 
/*     */ 
/*     */     
/* 254 */     System.exit(-1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/ConnectionDispersionTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */