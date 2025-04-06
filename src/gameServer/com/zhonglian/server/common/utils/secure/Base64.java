/*     */ package com.zhonglian.server.common.utils.secure;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ public class Base64 {
/*   6 */   private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
/*   7 */       'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 
/*   8 */       'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*   9 */   private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  10 */       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, 
/*  11 */       -1, -1, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 
/*  12 */       30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(byte[] data) {
/*  21 */     StringBuffer sb = new StringBuffer();
/*  22 */     int len = data.length;
/*  23 */     int i = 0;
/*     */     
/*  25 */     while (i < len) {
/*  26 */       int b1 = data[i++] & 0xFF;
/*  27 */       if (i == len) {
/*  28 */         sb.append(base64EncodeChars[b1 >>> 2]);
/*  29 */         sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
/*  30 */         sb.append("==");
/*     */         break;
/*     */       } 
/*  33 */       int b2 = data[i++] & 0xFF;
/*  34 */       if (i == len) {
/*  35 */         sb.append(base64EncodeChars[b1 >>> 2]);
/*  36 */         sb.append(base64EncodeChars[(b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4]);
/*  37 */         sb.append(base64EncodeChars[(b2 & 0xF) << 2]);
/*  38 */         sb.append("=");
/*     */         break;
/*     */       } 
/*  41 */       int b3 = data[i++] & 0xFF;
/*  42 */       sb.append(base64EncodeChars[b1 >>> 2]);
/*  43 */       sb.append(base64EncodeChars[(b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4]);
/*  44 */       sb.append(base64EncodeChars[(b2 & 0xF) << 2 | (b3 & 0xC0) >>> 6]);
/*  45 */       sb.append(base64EncodeChars[b3 & 0x3F]);
/*     */     } 
/*  47 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(String str) {
/*     */     try {
/*  58 */       return decodePrivate(str);
/*  59 */     } catch (UnsupportedEncodingException e) {
/*  60 */       e.printStackTrace();
/*     */       
/*  62 */       return new byte[0];
/*     */     } 
/*     */   }
/*     */   private static byte[] decodePrivate(String str) throws UnsupportedEncodingException {
/*  66 */     StringBuffer sb = new StringBuffer();
/*  67 */     byte[] data = null;
/*  68 */     data = str.getBytes("US-ASCII");
/*  69 */     int len = data.length;
/*  70 */     int i = 0;
/*     */     
/*  72 */     while (i < len) {
/*     */       int b1, b2, b3, b4;
/*     */       do {
/*  75 */         b1 = base64DecodeChars[data[i++]];
/*  76 */       } while (i < len && b1 == -1);
/*  77 */       if (b1 == -1) {
/*     */         break;
/*     */       }
/*     */       do {
/*  81 */         b2 = base64DecodeChars[data[i++]];
/*  82 */       } while (i < len && b2 == -1);
/*  83 */       if (b2 == -1)
/*     */         break; 
/*  85 */       sb.append((char)(b1 << 2 | (b2 & 0x30) >>> 4));
/*     */       
/*     */       do {
/*  88 */         b3 = data[i++];
/*  89 */         if (b3 == 61)
/*  90 */           return sb.toString().getBytes("iso8859-1"); 
/*  91 */         b3 = base64DecodeChars[b3];
/*  92 */       } while (i < len && b3 == -1);
/*  93 */       if (b3 == -1)
/*     */         break; 
/*  95 */       sb.append((char)((b2 & 0xF) << 4 | (b3 & 0x3C) >>> 2));
/*     */       
/*     */       do {
/*  98 */         b4 = data[i++];
/*  99 */         if (b4 == 61)
/* 100 */           return sb.toString().getBytes("iso8859-1"); 
/* 101 */         b4 = base64DecodeChars[b4];
/* 102 */       } while (i < len && b4 == -1);
/* 103 */       if (b4 == -1)
/*     */         break; 
/* 105 */       sb.append((char)((b3 & 0x3) << 6 | b4));
/*     */     } 
/* 107 */     return sb.toString().getBytes("iso8859-1");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/Base64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */