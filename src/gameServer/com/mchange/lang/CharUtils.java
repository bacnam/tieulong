/*    */ package com.mchange.lang;
/*    */ 
/*    */ import java.io.StringWriter;
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
/*    */ public final class CharUtils
/*    */ {
/*    */   public static int charFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 44 */     int i = 0;
/* 45 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 8;
/* 46 */     i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 0;
/* 47 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] byteArrayFromChar(char paramChar) {
/* 52 */     byte[] arrayOfByte = new byte[2];
/* 53 */     charIntoByteArray(paramChar, 0, arrayOfByte);
/* 54 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void charIntoByteArray(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
/* 59 */     paramArrayOfbyte[paramInt2 + 0] = (byte)(paramInt1 >>> 8 & 0xFF);
/* 60 */     paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 0 & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String toHexAscii(char paramChar) {
/* 65 */     StringWriter stringWriter = new StringWriter(4);
/* 66 */     ByteUtils.addHexAscii((byte)(paramChar >>> 8 & 0xFF), stringWriter);
/* 67 */     ByteUtils.addHexAscii((byte)(paramChar & 0xFF), stringWriter);
/* 68 */     return stringWriter.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static char[] fromHexAscii(String paramString) {
/* 73 */     int i = paramString.length();
/* 74 */     if (i % 4 != 0) {
/* 75 */       throw new NumberFormatException("Hex ascii must be exactly four digits per char.");
/*    */     }
/* 77 */     byte[] arrayOfByte = ByteUtils.fromHexAscii(paramString);
/* 78 */     int j = i / 4;
/* 79 */     char[] arrayOfChar = new char[j];
/* 80 */     for (byte b = 0; i < j; b++)
/* 81 */       arrayOfChar[b] = (char)charFromByteArray(arrayOfByte, b * 2); 
/* 82 */     return arrayOfChar;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/CharUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */