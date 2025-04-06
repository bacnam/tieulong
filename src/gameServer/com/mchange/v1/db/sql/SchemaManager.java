/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import com.mchange.util.impl.CommandLineParserImpl;
/*    */ import com.mchange.v1.util.CleanupUtils;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
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
/*    */ public class SchemaManager
/*    */ {
/* 46 */   static final String[] VALID = new String[] { "create", "drop" };
/*    */ 
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/* 50 */     Connection connection = null;
/*    */     
/*    */     try {
/* 53 */       CommandLineParserImpl commandLineParserImpl = new CommandLineParserImpl(paramArrayOfString, VALID, null, null);
/* 54 */       boolean bool = commandLineParserImpl.checkSwitch("create");
/*    */       
/* 56 */       if (!commandLineParserImpl.checkArgv()) usage(); 
/* 57 */       if (!(bool ^ commandLineParserImpl.checkSwitch("drop"))) usage();
/*    */       
/* 59 */       String[] arrayOfString = commandLineParserImpl.findUnswitchedArgs();
/*    */       
/* 61 */       if (arrayOfString.length == 2) {
/* 62 */         connection = DriverManager.getConnection(arrayOfString[0]);
/* 63 */       } else if (arrayOfString.length == 4) {
/* 64 */         connection = DriverManager.getConnection(arrayOfString[0], arrayOfString[1], arrayOfString[2]);
/*    */       } else {
/* 66 */         usage();
/*    */       } 
/* 68 */       connection.setAutoCommit(false);
/*    */       
/* 70 */       Schema schema = (Schema)Class.forName(arrayOfString[arrayOfString.length - 1]).newInstance();
/* 71 */       if (bool)
/*    */       {
/* 73 */         schema.createSchema(connection);
/* 74 */         System.out.println("Schema created.");
/*    */       }
/*    */       else
/*    */       {
/* 78 */         schema.dropSchema(connection);
/* 79 */         System.out.println("Schema dropped.");
/*    */       }
/*    */     
/* 82 */     } catch (Exception exception) {
/* 83 */       exception.printStackTrace();
/*    */     } finally {
/* 85 */       CleanupUtils.attemptClose(connection);
/*    */     } 
/*    */   }
/*    */   
/*    */   static void usage() {
/* 90 */     System.err.println("java -Djdbc.drivers=<driverclass> com.mchange.v1.db.sql.SchemaManager [-create | -drop] <jdbc_url> [<user> <password>] <schemaclass>");
/*    */     
/* 92 */     System.exit(-1);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/SchemaManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */