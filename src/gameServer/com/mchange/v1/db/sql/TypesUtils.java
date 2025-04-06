/*     */ package com.mchange.v1.db.sql;
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
/*     */ 
/*     */ public final class TypesUtils
/*     */ {
/*     */   public static String getNameForSqlTypeCode(int paramInt) throws UnsupportedTypeException {
/*  45 */     switch (paramInt) {
/*     */       
/*     */       case -7:
/*  48 */         return "BIT";
/*     */       case -6:
/*  50 */         return "TINYINT";
/*     */       case 5:
/*  52 */         return "SMALLINT";
/*     */       case 4:
/*  54 */         return "INTEGER";
/*     */       case -5:
/*  56 */         return "BIGINT";
/*     */       case 6:
/*  58 */         return "FLOAT";
/*     */       case 7:
/*  60 */         return "REAL";
/*     */       case 8:
/*  62 */         return "DOUBLE";
/*     */       case 2:
/*  64 */         return "NUMERIC";
/*     */       case 3:
/*  66 */         return "DECIMAL";
/*     */       case 1:
/*  68 */         return "CHAR";
/*     */       case 12:
/*  70 */         return "VARCHAR";
/*     */       case -1:
/*  72 */         return "LONGVARCHAR";
/*     */       case 91:
/*  74 */         return "DATE";
/*     */       case 92:
/*  76 */         return "TIME";
/*     */       case 93:
/*  78 */         return "TIMESTAMP";
/*     */       case -2:
/*  80 */         return "BINARY";
/*     */       case -3:
/*  82 */         return "VARBINARY";
/*     */       case -4:
/*  84 */         return "LONGVARBINARY";
/*     */       case 0:
/*  86 */         return "NULL";
/*     */       case 1111:
/*  88 */         throw new UnsupportedTypeException("Type OTHER cannot be represented as a String.");
/*     */       
/*     */       case 2000:
/*  91 */         throw new UnsupportedTypeException("Type JAVA_OBJECT cannot be represented as a String.");
/*     */       
/*     */       case 2006:
/*  94 */         return "REF";
/*     */       case 2002:
/*  96 */         return "STRUCT";
/*     */       case 2003:
/*  98 */         return "ARRAY";
/*     */       case 2004:
/* 100 */         return "BLOB";
/*     */       case 2005:
/* 102 */         return "CLOB";
/*     */     } 
/* 104 */     throw new UnsupportedTypeException("Type code: " + paramInt + " is unknown.");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/TypesUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */