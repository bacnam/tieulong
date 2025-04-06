/*     */ package com.zhonglian.server.common.utils;
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
/*     */ public final class ByteUtils
/*     */ {
/*     */   public static final short UNSIGNED_MAX_VALUE = 255;
/*     */   
/*     */   public static short toUnsigned(byte b) {
/*  46 */     return (short)((b < 0) ? (256 + b) : b);
/*     */   }
/*     */   
/*     */   public static String toHexAscii(byte b) {
/*  50 */     StringWriter sw = new StringWriter(2);
/*  51 */     addHexAscii(b, sw);
/*  52 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public static String toHexAscii(byte[] bytes) {
/*  56 */     int len = bytes.length;
/*  57 */     StringWriter sw = new StringWriter(len * 2);
/*  58 */     for (int i = 0; i < len; i++)
/*  59 */       addHexAscii(bytes[i], sw); 
/*  60 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public static byte[] fromHexAscii(String s) throws NumberFormatException {
/*     */     try {
/*  65 */       int len = s.length();
/*  66 */       if (len % 2 != 0) {
/*  67 */         throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
/*     */       }
/*  69 */       int out_len = len / 2;
/*  70 */       byte[] out = new byte[out_len];
/*  71 */       int i = 0;
/*  72 */       StringReader sr = new StringReader(s);
/*  73 */       while (i < out_len) {
/*  74 */         int val = 16 * fromHexDigit(sr.read()) + fromHexDigit(sr.read());
/*  75 */         out[i++] = (byte)val;
/*     */       } 
/*  77 */       return out;
/*  78 */     } catch (IOException e) {
/*  79 */       throw new InternalError("IOException reading from StringReader?!?!");
/*     */     } 
/*     */   }
/*     */   
/*     */   static void addHexAscii(byte b, StringWriter sw) {
/*  84 */     short ub = toUnsigned(b);
/*  85 */     int h1 = ub / 16;
/*  86 */     int h2 = ub % 16;
/*  87 */     sw.write(toHexDigit(h1));
/*  88 */     sw.write(toHexDigit(h2));
/*     */   }
/*     */   
/*     */   private static int fromHexDigit(int c) throws NumberFormatException {
/*  92 */     if (c >= 48 && c < 58)
/*  93 */       return c - 48; 
/*  94 */     if (c >= 65 && c < 71)
/*  95 */       return c - 55; 
/*  96 */     if (c >= 97 && c < 103) {
/*  97 */       return c - 87;
/*     */     }
/*  99 */     throw new NumberFormatException(String.valueOf(39 + c) + "' is not a valid hexadecimal digit.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char toHexDigit(int h) {
/*     */     char out;
/* 107 */     if (h <= 9) {
/* 108 */       out = (char)(h + 48);
/*     */     } else {
/* 110 */       out = (char)(h + 55);
/*     */     } 
/* 112 */     return out;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/ByteUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */