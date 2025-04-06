/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSourceFactory;
/*     */ import com.mchange.v2.c3p0.PoolBackedDataSourceFactory;
/*     */ import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
/*     */ import javax.naming.InitialContext;
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
/*     */ public final class JndiBindTest
/*     */ {
/*     */   public static void main(String[] argv) {
/*     */     try {
/*  48 */       String driverClass = null;
/*  49 */       String jdbc_url = null;
/*  50 */       String username = null;
/*  51 */       String password = null;
/*  52 */       String dmds_name = null;
/*  53 */       String cpds_name = null;
/*  54 */       String pbds_name = null;
/*     */       
/*  56 */       if (argv.length == 7) {
/*     */         
/*  58 */         driverClass = argv[0];
/*  59 */         jdbc_url = argv[1];
/*  60 */         username = argv[2];
/*  61 */         password = argv[3];
/*  62 */         dmds_name = argv[4];
/*  63 */         cpds_name = argv[5];
/*  64 */         pbds_name = argv[6];
/*     */       }
/*  66 */       else if (argv.length == 5) {
/*     */         
/*  68 */         driverClass = argv[0];
/*  69 */         jdbc_url = argv[1];
/*  70 */         username = null;
/*  71 */         password = null;
/*  72 */         dmds_name = argv[2];
/*  73 */         cpds_name = argv[3];
/*  74 */         pbds_name = argv[4];
/*     */       } else {
/*     */         
/*  77 */         usage();
/*     */       } 
/*  79 */       if (!jdbc_url.startsWith("jdbc:")) {
/*  80 */         usage();
/*     */       }
/*  82 */       DataSource dmds = DriverManagerDataSourceFactory.create(driverClass, jdbc_url, username, password);
/*     */ 
/*     */ 
/*     */       
/*  86 */       WrapperConnectionPoolDataSource cpds = new WrapperConnectionPoolDataSource();
/*  87 */       cpds.setNestedDataSource(dmds);
/*  88 */       DataSource pbds = PoolBackedDataSourceFactory.create(driverClass, jdbc_url, username, password);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       InitialContext ctx = new InitialContext();
/*  94 */       ctx.rebind(dmds_name, dmds);
/*  95 */       System.out.println("DriverManagerDataSource bounds as " + dmds_name);
/*  96 */       ctx.rebind(cpds_name, cpds);
/*  97 */       System.out.println("ConnectionPoolDataSource bounds as " + cpds_name);
/*  98 */       ctx.rebind(pbds_name, pbds);
/*  99 */       System.out.println("PoolDataSource bounds as " + pbds_name);
/*     */     }
/* 101 */     catch (Exception e) {
/* 102 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void usage() {
/* 107 */     System.err.println("java " + JndiBindTest.class.getName() + " \\");
/* 108 */     System.err.println("\t<jdbc_driver_class> \\");
/* 109 */     System.err.println("\t<jdbc_url> [<username> <password>] \\");
/* 110 */     System.err.println("\t<dmds_name> <cpds_name> <pbds_name>");
/* 111 */     System.exit(-1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/JndiBindTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */