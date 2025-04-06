/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public class PoolUtil
/*     */ {
/*     */   public static String fillLogParams(String sql, Map<Object, Object> logParams) {
/*  42 */     StringBuilder result = new StringBuilder();
/*  43 */     Map<Object, Object> tmpLogParam = (logParams == null) ? new HashMap<Object, Object>() : logParams;
/*     */     
/*  45 */     Iterator<Object> it = tmpLogParam.values().iterator();
/*  46 */     boolean inQuote = false;
/*  47 */     boolean inQuote2 = false;
/*  48 */     char[] sqlChar = (sql != null) ? sql.toCharArray() : new char[0];
/*     */     
/*  50 */     for (int i = 0; i < sqlChar.length; i++) {
/*  51 */       if (sqlChar[i] == '\'') {
/*  52 */         inQuote = !inQuote;
/*     */       }
/*  54 */       if (sqlChar[i] == '"') {
/*  55 */         inQuote2 = !inQuote2;
/*     */       }
/*     */       
/*  58 */       if (sqlChar[i] == '?' && !inQuote && !inQuote2) {
/*  59 */         if (it.hasNext()) {
/*  60 */           result.append(prettyPrint(it.next()));
/*     */         } else {
/*  62 */           result.append('?');
/*     */         } 
/*     */       } else {
/*  65 */         result.append(sqlChar[i]);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  70 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String safePrint(Object... o) {
/*  78 */     StringBuilder sb = new StringBuilder();
/*  79 */     for (Object obj : o) {
/*  80 */       sb.append((obj != null) ? obj : "null");
/*     */     }
/*  82 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String prettyPrint(Object obj) {
/*  90 */     StringBuilder sb = new StringBuilder();
/*  91 */     if (obj == null) {
/*  92 */       sb.append("NULL");
/*  93 */     } else if (obj instanceof Blob) {
/*  94 */       sb.append(formatLogParam((Blob)obj));
/*  95 */     } else if (obj instanceof Clob) {
/*  96 */       sb.append(formatLogParam((Clob)obj));
/*  97 */     } else if (obj instanceof Ref) {
/*  98 */       sb.append(formatLogParam((Ref)obj));
/*  99 */     } else if (obj instanceof Array) {
/* 100 */       sb.append(formatLogParam((Array)obj));
/* 101 */     } else if (obj instanceof String) {
/* 102 */       sb.append("'" + obj.toString() + "'");
/*     */     } else {
/* 104 */       sb.append(obj.toString());
/*     */     } 
/* 106 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatLogParam(Blob obj) {
/* 114 */     String result = "";
/*     */     try {
/* 116 */       result = "(blob of length " + obj.length() + ")";
/* 117 */     } catch (SQLException e) {
/* 118 */       result = "(blob of unknown length)";
/*     */     } 
/* 120 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatLogParam(Clob obj) {
/* 128 */     String result = "";
/*     */     try {
/* 130 */       result = "(cblob of length " + obj.length() + ")";
/* 131 */     } catch (SQLException e) {
/* 132 */       result = "(cblob of unknown length)";
/*     */     } 
/* 134 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatLogParam(Array obj) {
/* 142 */     String result = "";
/*     */     try {
/* 144 */       result = "(array of type" + obj.getBaseTypeName().length() + ")";
/* 145 */     } catch (SQLException e) {
/* 146 */       result = "(array of unknown type)";
/*     */     } 
/* 148 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatLogParam(Ref obj) {
/* 156 */     String result = "";
/*     */     try {
/* 158 */       result = "(ref of type" + obj.getBaseTypeName().length() + ")";
/* 159 */     } catch (SQLException e) {
/* 160 */       result = "(ref of unknown type)";
/*     */     } 
/* 162 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stringifyException(Throwable t) {
/* 170 */     StringWriter sw = new StringWriter();
/* 171 */     PrintWriter pw = new PrintWriter(sw);
/* 172 */     t.printStackTrace(pw);
/* 173 */     String result = "";
/*     */     
/* 175 */     result = "------\r\n" + sw.toString() + "------\r\n";
/*     */     
/* 177 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/PoolUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */