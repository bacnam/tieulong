/*     */ package com.mchange.lang;
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
/*     */ 
/*     */ 
/*     */ public final class FloatUtils
/*     */ {
/*     */   static final boolean DEBUG = true;
/*     */   private static FParser fParser;
/*     */   
/*     */   static {
/*     */     try {
/*  51 */       fParser = new J12FParser();
/*  52 */       fParser.parseFloat("0.1");
/*     */     }
/*  54 */     catch (NoSuchMethodError noSuchMethodError) {
/*     */ 
/*     */       
/*  57 */       System.err.println("com.mchange.lang.FloatUtils: reconfiguring for Java 1.1 environment");
/*  58 */       fParser = new J11FParser();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] byteArrayFromFloat(float paramFloat) {
/*  64 */     int i = Float.floatToIntBits(paramFloat);
/*  65 */     return IntegerUtils.byteArrayFromInt(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float floatFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/*  70 */     int i = IntegerUtils.intFromByteArray(paramArrayOfbyte, paramInt);
/*  71 */     return Float.intBitsToFloat(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float parseFloat(String paramString, float paramFloat) {
/*  76 */     if (paramString == null) {
/*  77 */       return paramFloat;
/*     */     }
/*     */     try {
/*  80 */       return fParser.parseFloat(paramString);
/*  81 */     } catch (NumberFormatException numberFormatException) {
/*  82 */       return paramFloat;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float parseFloat(String paramString) throws NumberFormatException {
/*  89 */     return fParser.parseFloat(paramString);
/*     */   }
/*     */   
/*     */   public static String floatToString(float paramFloat, int paramInt) {
/*  93 */     boolean bool = (paramFloat < 0.0F) ? true : false;
/*  94 */     paramFloat = bool ? -paramFloat : paramFloat;
/*     */     
/*  96 */     long l = Math.round(paramFloat * Math.pow(10.0D, -paramInt));
/*     */     
/*  98 */     String str = String.valueOf(l);
/*  99 */     if (l == 0L) {
/* 100 */       return str;
/*     */     }
/* 102 */     int i = str.length();
/* 103 */     int j = i + paramInt;
/*     */     
/* 105 */     StringBuffer stringBuffer = new StringBuffer(32);
/* 106 */     if (bool) stringBuffer.append('-');
/*     */     
/* 108 */     if (j <= 0) {
/*     */       
/* 110 */       stringBuffer.append("0.");
/* 111 */       for (byte b = 0; b < -j; b++)
/* 112 */         stringBuffer.append('0'); 
/* 113 */       stringBuffer.append(str);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 119 */       stringBuffer.append(str.substring(0, Math.min(j, i)));
/* 120 */       if (j < i) {
/*     */         
/* 122 */         stringBuffer.append('.');
/* 123 */         stringBuffer.append(str.substring(j));
/*     */       }
/* 125 */       else if (j > i) {
/*     */         byte b; int k;
/* 127 */         for (b = 0, k = j - i; b < k; b++)
/* 128 */           stringBuffer.append('0'); 
/*     */       } 
/*     */     } 
/* 131 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   static interface FParser
/*     */   {
/*     */     float parseFloat(String param1String) throws NumberFormatException;
/*     */   }
/*     */   
/*     */   static class J12FParser
/*     */     implements FParser {
/*     */     public float parseFloat(String param1String) throws NumberFormatException {
/* 142 */       return Float.parseFloat(param1String);
/*     */     }
/*     */   }
/*     */   
/*     */   static class J11FParser implements FParser {
/*     */     public float parseFloat(String param1String) throws NumberFormatException {
/* 148 */       return (new Float(param1String)).floatValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/FloatUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */