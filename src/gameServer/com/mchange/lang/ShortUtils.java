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
/*    */ public class ShortUtils
/*    */ {
/*    */   public static final int UNSIGNED_MAX_VALUE = 65535;
/*    */   
/*    */   public static int shortFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 44 */     int i = 0;
/* 45 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 8;
/* 46 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 0;
/* 47 */     return (short)i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] byteArrayFromShort(short paramShort) {
/* 52 */     byte[] arrayOfByte = new byte[2];
/* 53 */     shortIntoByteArray(paramShort, 0, arrayOfByte);
/* 54 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void shortIntoByteArray(short paramShort, int paramInt, byte[] paramArrayOfbyte) {
/* 59 */     paramArrayOfbyte[paramInt + 0] = (byte)(paramShort >>> 8 & 0xFF);
/* 60 */     paramArrayOfbyte[paramInt + 1] = (byte)(paramShort >>> 0 & 0xFF);
/*    */   }
/*    */   
/*    */   public static int toUnsigned(short paramShort) {
/* 64 */     return (paramShort < 0) ? (65536 + paramShort) : paramShort;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/ShortUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */