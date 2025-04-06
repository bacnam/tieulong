/*     */ package com.mchange.lang;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
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
/*     */ public final class ByteUtils
/*     */ {
/*     */   public static final short UNSIGNED_MAX_VALUE = 255;
/*     */   
/*     */   public static short toUnsigned(byte paramByte) {
/*  47 */     return (short)((paramByte < 0) ? (256 + paramByte) : paramByte);
/*     */   }
/*     */   
/*     */   public static String toHexAscii(byte paramByte) {
/*  51 */     StringWriter stringWriter = new StringWriter(2);
/*  52 */     addHexAscii(paramByte, stringWriter);
/*  53 */     return stringWriter.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toHexAscii(byte[] paramArrayOfbyte) {
/*  58 */     int i = paramArrayOfbyte.length;
/*  59 */     StringWriter stringWriter = new StringWriter(i * 2);
/*  60 */     for (byte b = 0; b < i; b++)
/*  61 */       addHexAscii(paramArrayOfbyte[b], stringWriter); 
/*  62 */     return stringWriter.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fromHexAscii(String paramString) throws NumberFormatException {
/*     */     try {
/*  69 */       int i = paramString.length();
/*  70 */       if (i % 2 != 0) {
/*  71 */         throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
/*     */       }
/*  73 */       int j = i / 2;
/*  74 */       byte[] arrayOfByte = new byte[j];
/*  75 */       byte b = 0;
/*  76 */       StringReader stringReader = new StringReader(paramString);
/*  77 */       while (b < j) {
/*     */         
/*  79 */         int k = 16 * fromHexDigit(stringReader.read()) + fromHexDigit(stringReader.read());
/*  80 */         arrayOfByte[b++] = (byte)k;
/*     */       } 
/*  82 */       return arrayOfByte;
/*     */     }
/*  84 */     catch (IOException iOException) {
/*  85 */       throw new InternalError("IOException reading from StringReader?!?!");
/*     */     } 
/*     */   }
/*     */   
/*     */   static void addHexAscii(byte paramByte, StringWriter paramStringWriter) {
/*  90 */     short s = toUnsigned(paramByte);
/*  91 */     int i = s / 16;
/*  92 */     int j = s % 16;
/*  93 */     paramStringWriter.write(toHexDigit(i));
/*  94 */     paramStringWriter.write(toHexDigit(j));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fromHexDigit(int paramInt) throws NumberFormatException {
/*  99 */     if (paramInt >= 48 && paramInt < 58)
/* 100 */       return paramInt - 48; 
/* 101 */     if (paramInt >= 65 && paramInt < 71)
/* 102 */       return paramInt - 55; 
/* 103 */     if (paramInt >= 97 && paramInt < 103) {
/* 104 */       return paramInt - 87;
/*     */     }
/* 106 */     throw new NumberFormatException((39 + paramInt) + "' is not a valid hexadecimal digit.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char toHexDigit(int paramInt) {
/*     */     char c;
/* 115 */     if (paramInt <= 9) { c = (char)(paramInt + 48); }
/* 116 */     else { c = (char)(paramInt + 55); }
/*     */     
/* 118 */     return c;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/ByteUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */