/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSource;
/*     */ import com.mchange.v2.c3p0.PoolBackedDataSource;
/*     */ import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
/*     */ import com.mchange.v2.naming.ReferenceableUtils;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.ConnectionPoolDataSource;
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
/*     */ public final class TestRefSerStuff
/*     */ {
/*     */   static void create(DataSource ds) throws SQLException {
/*  54 */     Connection con = null;
/*  55 */     Statement stmt = null;
/*     */     
/*     */     try {
/*  58 */       con = ds.getConnection();
/*  59 */       stmt = con.createStatement();
/*  60 */       stmt.executeUpdate("CREATE TABLE TRSS_TABLE ( a_col VARCHAR(16) )");
/*     */     }
/*     */     finally {
/*     */       
/*  64 */       StatementUtils.attemptClose(stmt);
/*  65 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void drop(DataSource ds) throws SQLException {
/*  71 */     Connection con = null;
/*  72 */     Statement stmt = null;
/*     */     
/*     */     try {
/*  75 */       con = ds.getConnection();
/*  76 */       stmt = con.createStatement();
/*  77 */       stmt.executeUpdate("DROP TABLE TRSS_TABLE");
/*     */     }
/*     */     finally {
/*     */       
/*  81 */       StatementUtils.attemptClose(stmt);
/*  82 */       ConnectionUtils.attemptClose(con);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void doSomething(DataSource ds) throws SQLException {
/*  88 */     Connection con = null;
/*  89 */     Statement stmt = null;
/*     */     
/*     */     try {
/*  92 */       con = ds.getConnection();
/*  93 */       stmt = con.createStatement();
/*  94 */       int i = stmt.executeUpdate("INSERT INTO TRSS_TABLE VALUES ('" + System.currentTimeMillis() + "')");
/*     */       
/*  96 */       if (i != 1) {
/*  97 */         throw new SQLException("Insert failed somehow strange!");
/*     */       }
/*     */     } finally {
/*     */       
/* 101 */       StatementUtils.attemptClose(stmt);
/* 102 */       ConnectionUtils.attemptClose(con);
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
/*     */   
/*     */   static void doTest(DataSource checkMe) throws Exception {
/* 119 */     doSomething(checkMe);
/* 120 */     System.err.println("\tcreated:   " + checkMe);
/* 121 */     DataSource afterSer = (DataSource)SerializableUtils.testSerializeDeserialize(checkMe);
/* 122 */     doSomething(afterSer);
/* 123 */     System.err.println("\tafter ser: " + afterSer);
/* 124 */     Reference ref = ((Referenceable)checkMe).getReference();
/*     */ 
/*     */     
/* 127 */     DataSource afterRef = (DataSource)ReferenceableUtils.referenceToObject(ref, null, null, null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     doSomething(afterRef);
/* 133 */     System.err.println("\tafter ref: " + afterRef);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] argv) {
/* 138 */     if (argv.length > 0) {
/*     */       
/* 140 */       System.err.println(TestRefSerStuff.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");
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
/*     */       return;
/*     */     } 
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
/* 170 */       DriverManagerDataSource dmds = new DriverManagerDataSource();
/*     */ 
/*     */       
/*     */       try {
/* 174 */         drop((DataSource)dmds);
/* 175 */       } catch (Exception e) {}
/*     */       
/* 177 */       create((DataSource)dmds);
/*     */       
/* 179 */       System.err.println("DriverManagerDataSource:");
/* 180 */       doTest((DataSource)dmds);
/*     */       
/* 182 */       WrapperConnectionPoolDataSource wcpds = new WrapperConnectionPoolDataSource();
/* 183 */       wcpds.setNestedDataSource((DataSource)dmds);
/* 184 */       PoolBackedDataSource pbds = new PoolBackedDataSource();
/* 185 */       pbds.setConnectionPoolDataSource((ConnectionPoolDataSource)wcpds);
/*     */       
/* 187 */       System.err.println("PoolBackedDataSource:");
/* 188 */       doTest((DataSource)pbds);
/*     */       
/* 190 */       ComboPooledDataSource cpds = new ComboPooledDataSource();
/* 191 */       doTest((DataSource)cpds);
/*     */     }
/* 193 */     catch (Exception e) {
/* 194 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/TestRefSerStuff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */