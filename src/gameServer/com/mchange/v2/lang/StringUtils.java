/*     */ package com.mchange.v2.lang;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ public final class StringUtils
/*     */ {
/*     */   static final Pattern COMMA_SEP_TRIM_REGEX;
/*     */   static final Pattern COMMA_SEP_NO_TRIM_REGEX;
/*     */   
/*     */   static {
/*     */     try {
/*  53 */       COMMA_SEP_TRIM_REGEX = Pattern.compile("\\s*\\,\\s*");
/*  54 */       COMMA_SEP_NO_TRIM_REGEX = Pattern.compile("\\,");
/*     */     }
/*  56 */     catch (PatternSyntaxException patternSyntaxException) {
/*     */       
/*  58 */       patternSyntaxException.printStackTrace();
/*  59 */       throw new InternalError(patternSyntaxException.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  64 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*     */   public static String normalString(String paramString) {
/*  67 */     return nonEmptyTrimmedOrNull(paramString);
/*     */   }
/*     */   public static boolean nonEmptyString(String paramString) {
/*  70 */     return (paramString != null && paramString.length() > 0);
/*     */   }
/*     */   public static boolean nonWhitespaceString(String paramString) {
/*  73 */     return (paramString != null && paramString.trim().length() > 0);
/*     */   }
/*     */   public static String nonEmptyOrNull(String paramString) {
/*  76 */     return nonEmptyString(paramString) ? paramString : null;
/*     */   }
/*     */   public static String nonNullOrBlank(String paramString) {
/*  79 */     return (paramString != null) ? paramString : "";
/*     */   }
/*     */   
/*     */   public static String nonEmptyTrimmedOrNull(String paramString) {
/*  83 */     String str = paramString;
/*  84 */     if (str != null) {
/*     */       
/*  86 */       str = str.trim();
/*  87 */       str = (str.length() > 0) ? str : null;
/*     */     } 
/*  89 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getUTF8Bytes(String paramString) {
/*     */     try {
/*  95 */       return paramString.getBytes("UTF8");
/*  96 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/*  98 */       unsupportedEncodingException.printStackTrace();
/*  99 */       throw new InternalError("UTF8 is an unsupported encoding?!?");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] splitCommaSeparated(String paramString, boolean paramBoolean) {
/* 105 */     Pattern pattern = paramBoolean ? COMMA_SEP_TRIM_REGEX : COMMA_SEP_NO_TRIM_REGEX;
/* 106 */     return pattern.split(paramString);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */