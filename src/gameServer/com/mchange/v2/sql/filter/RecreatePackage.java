/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import com.mchange.v2.codegen.intfc.DelegatorGenerator;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileWriter;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
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
/*     */ public final class RecreatePackage
/*     */ {
/*  47 */   static final Class[] intfcs = new Class[] { Connection.class, ResultSet.class, DatabaseMetaData.class, Statement.class, PreparedStatement.class, CallableStatement.class, DataSource.class };
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
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/*  63 */       DelegatorGenerator delegatorGenerator = new DelegatorGenerator();
/*  64 */       String str1 = RecreatePackage.class.getName();
/*  65 */       String str2 = str1.substring(0, str1.lastIndexOf('.'));
/*  66 */       for (byte b = 0; b < intfcs.length; b++)
/*     */       {
/*  68 */         Class clazz = intfcs[b];
/*  69 */         String str3 = ClassUtils.simpleClassName(clazz);
/*  70 */         String str4 = "Filter" + str3;
/*  71 */         String str5 = "SynchronizedFilter" + str3;
/*     */         
/*  73 */         BufferedWriter bufferedWriter = null;
/*     */         
/*     */         try {
/*  76 */           bufferedWriter = new BufferedWriter(new FileWriter(str4 + ".java"));
/*  77 */           delegatorGenerator.setMethodModifiers(1);
/*  78 */           delegatorGenerator.writeDelegator(clazz, str2 + '.' + str4, bufferedWriter);
/*  79 */           System.err.println(str4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */         finally {}
/*     */ 
/*     */ 
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
/*     */ 
/*     */     
/*     */     }
/* 103 */     catch (Exception exception) {
/* 104 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/RecreatePackage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */