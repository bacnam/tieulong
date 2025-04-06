/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.StringTokenizer;
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
/*    */ public final class StringTokenizerUtils
/*    */ {
/*    */   public static String[] tokenizeToArray(String paramString1, String paramString2, boolean paramBoolean) {
/* 44 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2, paramBoolean);
/* 45 */     String[] arrayOfString = new String[stringTokenizer.countTokens()];
/* 46 */     for (byte b = 0; stringTokenizer.hasMoreTokens(); b++)
/* 47 */       arrayOfString[b] = stringTokenizer.nextToken(); 
/* 48 */     return arrayOfString;
/*    */   }
/*    */   
/*    */   public static String[] tokenizeToArray(String paramString1, String paramString2) {
/* 52 */     return tokenizeToArray(paramString1, paramString2, false);
/*    */   }
/*    */   public static String[] tokenizeToArray(String paramString) {
/* 55 */     return tokenizeToArray(paramString, " \t\r\n");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/StringTokenizerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */