/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*     */ import com.mchange.v2.c3p0.C3P0ProxyStatement;
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RawConnectionOpTest
/*     */ {
/*     */   public static void main(String[] argv) {
/*  49 */     ComboPooledDataSource cpds = null;
/*     */     
/*     */     try {
/*  52 */       String jdbc_url = null;
/*  53 */       String username = null;
/*  54 */       String password = null;
/*     */       
/*  56 */       if (argv.length == 3) {
/*     */         
/*  58 */         jdbc_url = argv[0];
/*  59 */         username = argv[1];
/*  60 */         password = argv[2];
/*     */       }
/*  62 */       else if (argv.length == 1) {
/*     */         
/*  64 */         jdbc_url = argv[0];
/*  65 */         username = null;
/*  66 */         password = null;
/*     */       } else {
/*     */         
/*  69 */         usage();
/*     */       } 
/*  71 */       if (!jdbc_url.startsWith("jdbc:")) {
/*  72 */         usage();
/*     */       }
/*  74 */       cpds = new ComboPooledDataSource();
/*  75 */       cpds.setJdbcUrl(jdbc_url);
/*  76 */       cpds.setUser(username);
/*  77 */       cpds.setPassword(password);
/*  78 */       cpds.setMaxPoolSize(10);
/*     */ 
/*     */       
/*  81 */       C3P0ProxyConnection conn = (C3P0ProxyConnection)cpds.getConnection();
/*  82 */       Method toStringMethod = Object.class.getMethod("toString", new Class[0]);
/*  83 */       Method identityHashCodeMethod = System.class.getMethod("identityHashCode", new Class[] { Object.class });
/*  84 */       System.out.println("rawConnection.toString() -> " + conn.rawConnectionOperation(toStringMethod, C3P0ProxyConnection.RAW_CONNECTION, new Object[0]));
/*     */       
/*  86 */       Integer ihc = (Integer)conn.rawConnectionOperation(identityHashCodeMethod, null, new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
/*  87 */       System.out.println("System.identityHashCode( rawConnection ) -> " + Integer.toHexString(ihc.intValue()));
/*     */       
/*  89 */       C3P0ProxyStatement stmt = (C3P0ProxyStatement)conn.createStatement();
/*  90 */       System.out.println("rawStatement.toString() -> " + stmt.rawStatementOperation(toStringMethod, C3P0ProxyStatement.RAW_STATEMENT, new Object[0]));
/*     */       
/*  92 */       Integer ihc2 = (Integer)stmt.rawStatementOperation(identityHashCodeMethod, null, new Object[] { C3P0ProxyStatement.RAW_STATEMENT });
/*  93 */       System.out.println("System.identityHashCode( rawStatement ) -> " + Integer.toHexString(ihc2.intValue()));
/*     */       
/*  95 */       conn.close();
/*     */       
/*  97 */       for (int i = 0; i < 10; i++)
/*     */       {
/*  99 */         C3P0ProxyConnection check = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 110 */     catch (Exception e) {
/* 111 */       e.printStackTrace();
/*     */     } finally {
/* 113 */       if (cpds != null) cpds.close(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void usage() {
/* 118 */     System.err.println("java " + RawConnectionOpTest.class.getName() + " \\");
/* 119 */     System.err.println("\t<jdbc_driver_class> \\");
/* 120 */     System.err.println("\t<jdbc_url> [<username> <password>]");
/* 121 */     System.exit(-1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/RawConnectionOpTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */