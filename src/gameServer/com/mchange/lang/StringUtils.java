/*    */ package com.mchange.lang;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
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
/*    */ public final class StringUtils
/*    */ {
/* 45 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*    */   
/*    */   public static String normalString(String paramString) {
/* 48 */     return nonEmptyTrimmedOrNull(paramString);
/*    */   }
/*    */   public static boolean nonEmptyString(String paramString) {
/* 51 */     return (paramString != null && paramString.length() > 0);
/*    */   }
/*    */   public static boolean nonWhitespaceString(String paramString) {
/* 54 */     return (paramString != null && paramString.trim().length() > 0);
/*    */   }
/*    */   public static String nonEmptyOrNull(String paramString) {
/* 57 */     return nonEmptyString(paramString) ? paramString : null;
/*    */   }
/*    */   public static String nonNullOrBlank(String paramString) {
/* 60 */     return (paramString != null) ? paramString : "";
/*    */   }
/*    */   
/*    */   public static String nonEmptyTrimmedOrNull(String paramString) {
/* 64 */     String str = paramString;
/* 65 */     if (str != null) {
/*    */       
/* 67 */       str = str.trim();
/* 68 */       str = (str.length() > 0) ? str : null;
/*    */     } 
/* 70 */     return str;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] getUTF8Bytes(String paramString) {
/*    */     try {
/* 76 */       return paramString.getBytes("UTF8");
/* 77 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*    */       
/* 79 */       unsupportedEncodingException.printStackTrace();
/* 80 */       throw new InternalError("UTF8 is an unsupported encoding?!?");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */