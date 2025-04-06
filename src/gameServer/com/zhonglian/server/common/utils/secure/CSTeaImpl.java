/*     */ package com.zhonglian.server.common.utils.secure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CSTeaImpl
/*     */ {
/*  11 */   private int DELTA = -1640531527;
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
/*     */   public byte[] encrypt(byte[] content, int offset, int[] key, int times) {
/*  24 */     int[] tempInt = byteToInt(content, offset);
/*  25 */     int y = tempInt[0], z = tempInt[1], sum = 0;
/*  26 */     int a = key[0], b = key[1], c = key[2], d = key[3];
/*     */     
/*  28 */     for (int i = 0; i < times; i++) {
/*  29 */       sum += this.DELTA;
/*  30 */       y += (z << 4) + a ^ z + sum ^ (z >> 5) + b;
/*  31 */       z += (y << 4) + c ^ y + sum ^ (y >> 5) + d;
/*     */     } 
/*  33 */     tempInt[0] = y;
/*  34 */     tempInt[1] = z;
/*  35 */     return intToByte(tempInt, 0);
/*     */   }
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
/*     */   public byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times) {
/*  48 */     int[] tempInt = byteToInt(encryptContent, offset);
/*  49 */     int y = tempInt[0], z = tempInt[1], sum = 0;
/*  50 */     int a = key[0], b = key[1], c = key[2], d = key[3];
/*  51 */     if (times == 32) {
/*  52 */       sum = -957401312;
/*  53 */     } else if (times == 16) {
/*  54 */       sum = -478700656;
/*     */     } else {
/*  56 */       sum = this.DELTA * times;
/*     */     } 
/*     */     
/*  59 */     for (int i = 0; i < times; i++) {
/*  60 */       z -= (y << 4) + c ^ y + sum ^ (y >> 5) + d;
/*  61 */       y -= (z << 4) + a ^ z + sum ^ (z >> 5) + b;
/*  62 */       sum -= this.DELTA;
/*     */     } 
/*  64 */     tempInt[0] = y;
/*  65 */     tempInt[1] = z;
/*     */     
/*  67 */     return intToByte(tempInt, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] byteToInt(byte[] content, int offset) {
/*  79 */     int[] result = new int[content.length >> 2];
/*     */ 
/*     */     
/*  82 */     for (int i = 0, j = offset; j < content.length; i++, j += 4) {
/*  83 */       result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16 | content[j] << 24;
/*     */     }
/*     */     
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] intToByte(int[] content, int offset) {
/*  97 */     byte[] result = new byte[content.length << 2];
/*     */ 
/*     */     
/* 100 */     for (int i = 0, j = offset; j < result.length; i++, j += 4) {
/* 101 */       result[j + 3] = (byte)(content[i] & 0xFF);
/* 102 */       result[j + 2] = (byte)(content[i] >> 8 & 0xFF);
/* 103 */       result[j + 1] = (byte)(content[i] >> 16 & 0xFF);
/* 104 */       result[j] = (byte)(content[i] >> 24 & 0xFF);
/*     */     } 
/*     */     
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int transform(byte temp) {
/* 117 */     int tempInt = temp;
/* 118 */     if (tempInt < 0) {
/* 119 */       tempInt += 256;
/*     */     }
/* 121 */     return tempInt;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/CSTeaImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */