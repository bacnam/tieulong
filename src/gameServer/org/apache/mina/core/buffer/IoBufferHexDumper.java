/*     */ package org.apache.mina.core.buffer;
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
/*     */ class IoBufferHexDumper
/*     */ {
/*     */   private static final byte[] highDigits;
/*     */   private static final byte[] lowDigits;
/*     */   
/*     */   static {
/*  43 */     byte[] digits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*     */ 
/*     */     
/*  46 */     byte[] high = new byte[256];
/*  47 */     byte[] low = new byte[256];
/*     */     
/*  49 */     for (int i = 0; i < 256; i++) {
/*  50 */       high[i] = digits[i >>> 4];
/*  51 */       low[i] = digits[i & 0xF];
/*     */     } 
/*     */     
/*  54 */     highDigits = high;
/*  55 */     lowDigits = low;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHexdump(IoBuffer in, int lengthLimit) {
/*     */     int size;
/*  66 */     if (lengthLimit == 0) {
/*  67 */       throw new IllegalArgumentException("lengthLimit: " + lengthLimit + " (expected: 1+)");
/*     */     }
/*     */     
/*  70 */     boolean truncate = (in.remaining() > lengthLimit);
/*     */     
/*  72 */     if (truncate) {
/*  73 */       size = lengthLimit;
/*     */     } else {
/*  75 */       size = in.remaining();
/*     */     } 
/*     */     
/*  78 */     if (size == 0) {
/*  79 */       return "empty";
/*     */     }
/*     */     
/*  82 */     StringBuilder out = new StringBuilder(size * 3 + 3);
/*     */     
/*  84 */     int mark = in.position();
/*     */ 
/*     */     
/*  87 */     int byteValue = in.get() & 0xFF;
/*  88 */     out.append((char)highDigits[byteValue]);
/*  89 */     out.append((char)lowDigits[byteValue]);
/*  90 */     size--;
/*     */ 
/*     */     
/*  93 */     for (; size > 0; size--) {
/*  94 */       out.append(' ');
/*  95 */       byteValue = in.get() & 0xFF;
/*  96 */       out.append((char)highDigits[byteValue]);
/*  97 */       out.append((char)lowDigits[byteValue]);
/*     */     } 
/*     */     
/* 100 */     in.position(mark);
/*     */     
/* 102 */     if (truncate) {
/* 103 */       out.append("...");
/*     */     }
/*     */     
/* 106 */     return out.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/buffer/IoBufferHexDumper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */