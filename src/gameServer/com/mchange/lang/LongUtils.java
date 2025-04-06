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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongUtils
/*    */ {
/*    */   public static long longFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 45 */     long l = 0L;
/* 46 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 56L;
/* 47 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 48L;
/* 48 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 2]) << 40L;
/* 49 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 3]) << 32L;
/* 50 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 4]) << 24L;
/* 51 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 5]) << 16L;
/* 52 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 6]) << 8L;
/* 53 */     l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 7]) << 0L;
/* 54 */     return l;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] byteArrayFromLong(long paramLong) {
/* 59 */     byte[] arrayOfByte = new byte[8];
/* 60 */     longIntoByteArray(paramLong, 0, arrayOfByte);
/* 61 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void longIntoByteArray(long paramLong, int paramInt, byte[] paramArrayOfbyte) {
/* 66 */     paramArrayOfbyte[paramInt + 0] = (byte)(int)(paramLong >>> 56L & 0xFFL);
/* 67 */     paramArrayOfbyte[paramInt + 1] = (byte)(int)(paramLong >>> 48L & 0xFFL);
/* 68 */     paramArrayOfbyte[paramInt + 2] = (byte)(int)(paramLong >>> 40L & 0xFFL);
/* 69 */     paramArrayOfbyte[paramInt + 3] = (byte)(int)(paramLong >>> 32L & 0xFFL);
/* 70 */     paramArrayOfbyte[paramInt + 4] = (byte)(int)(paramLong >>> 24L & 0xFFL);
/* 71 */     paramArrayOfbyte[paramInt + 5] = (byte)(int)(paramLong >>> 16L & 0xFFL);
/* 72 */     paramArrayOfbyte[paramInt + 6] = (byte)(int)(paramLong >>> 8L & 0xFFL);
/* 73 */     paramArrayOfbyte[paramInt + 7] = (byte)(int)(paramLong >>> 0L & 0xFFL);
/*    */   }
/*    */   
/*    */   public static int fullHashLong(long paramLong) {
/* 77 */     return hashLong(paramLong);
/*    */   }
/*    */   public static int hashLong(long paramLong) {
/* 80 */     return (int)paramLong ^ (int)(paramLong >>> 32L);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/LongUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */