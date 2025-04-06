/*    */ package com.mchange.lang;
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
/*    */ public final class IntegerUtils
/*    */ {
/*    */   public static final long UNSIGNED_MAX_VALUE = -1L;
/*    */   
/*    */   public static int parseInt(String paramString, int paramInt) {
/* 44 */     if (paramString == null) {
/* 45 */       return paramInt;
/*    */     }
/*    */     try {
/* 48 */       return Integer.parseInt(paramString);
/* 49 */     } catch (NumberFormatException numberFormatException) {
/* 50 */       return paramInt;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int parseInt(String paramString, int paramInt1, int paramInt2) {
/* 55 */     if (paramString == null) {
/* 56 */       return paramInt2;
/*    */     }
/*    */     try {
/* 59 */       return Integer.parseInt(paramString, paramInt1);
/* 60 */     } catch (NumberFormatException numberFormatException) {
/* 61 */       return paramInt2;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 66 */     int i = 0;
/* 67 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 24;
/* 68 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 16;
/* 69 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 2]) << 8;
/* 70 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 3]) << 0;
/* 71 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] byteArrayFromInt(int paramInt) {
/* 76 */     byte[] arrayOfByte = new byte[4];
/* 77 */     intIntoByteArray(paramInt, 0, arrayOfByte);
/* 78 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void intIntoByteArray(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
/* 83 */     paramArrayOfbyte[paramInt2 + 0] = (byte)(paramInt1 >>> 24 & 0xFF);
/* 84 */     paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 16 & 0xFF);
/* 85 */     paramArrayOfbyte[paramInt2 + 2] = (byte)(paramInt1 >>> 8 & 0xFF);
/* 86 */     paramArrayOfbyte[paramInt2 + 3] = (byte)(paramInt1 >>> 0 & 0xFF);
/*    */   }
/*    */   
/*    */   public static long toUnsigned(int paramInt) {
/* 90 */     return (paramInt < 0) ? (0L + paramInt) : paramInt;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/IntegerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */