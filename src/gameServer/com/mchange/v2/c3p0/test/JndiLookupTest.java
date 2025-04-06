/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import javax.naming.InitialContext;
/*    */ import javax.sql.ConnectionPoolDataSource;
/*    */ import javax.sql.DataSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JndiLookupTest
/*    */ {
/*    */   public static void main(String[] argv) {
/*    */     try {
/* 49 */       String dmds_name = null;
/* 50 */       String cpds_name = null;
/* 51 */       String pbds_name = null;
/*    */       
/* 53 */       if (argv.length == 3) {
/*    */         
/* 55 */         dmds_name = argv[0];
/* 56 */         cpds_name = argv[1];
/* 57 */         pbds_name = argv[2];
/*    */       } else {
/*    */         
/* 60 */         usage();
/*    */       } 
/* 62 */       InitialContext ctx = new InitialContext();
/* 63 */       DataSource dmds = (DataSource)ctx.lookup(dmds_name);
/* 64 */       dmds.getConnection().close();
/* 65 */       System.out.println("DriverManagerDataSource " + dmds_name + " sucessfully looked up and checked.");
/*    */       
/* 67 */       ConnectionPoolDataSource cpds = (ConnectionPoolDataSource)ctx.lookup(cpds_name);
/* 68 */       cpds.getPooledConnection().close();
/* 69 */       System.out.println("ConnectionPoolDataSource " + cpds_name + " sucessfully looked up and checked.");
/*    */       
/* 71 */       DataSource pbds = (DataSource)ctx.lookup(pbds_name);
/* 72 */       pbds.getConnection().close();
/* 73 */       System.out.println("PoolBackedDataSource " + pbds_name + " sucessfully looked up and checked.");
/*    */     
/*    */     }
/* 76 */     catch (Exception e) {
/* 77 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void usage() {
/* 82 */     System.err.println("java " + JndiLookupTest.class.getName() + " \\");
/*    */     
/* 84 */     System.err.println("\t<dmds_name> <wcpds_name> <wpbds_name>");
/* 85 */     System.exit(-1);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/JndiLookupTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */