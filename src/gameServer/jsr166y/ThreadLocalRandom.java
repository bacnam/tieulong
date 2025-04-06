/*     */ package jsr166y;
/*     */ 
/*     */ import java.util.Random;
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
/*     */ public class ThreadLocalRandom
/*     */   extends Random
/*     */ {
/*     */   private static final long multiplier = 25214903917L;
/*     */   private static final long addend = 11L;
/*     */   private static final long mask = 281474976710655L;
/*     */   private long rnd;
/*     */   boolean initialized;
/*     */   private long pad0;
/*     */   private long pad1;
/*     */   private long pad2;
/*     */   private long pad3;
/*     */   private long pad4;
/*     */   private long pad5;
/*     */   private long pad6;
/*     */   private long pad7;
/*     */   
/*  63 */   private static final ThreadLocal<ThreadLocalRandom> localRandom = new ThreadLocal<ThreadLocalRandom>()
/*     */     {
/*     */       protected ThreadLocalRandom initialValue() {
/*  66 */         return new ThreadLocalRandom();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -5851777807851030925L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThreadLocalRandom current() {
/*  86 */     return localRandom.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/*  96 */     if (this.initialized)
/*  97 */       throw new UnsupportedOperationException(); 
/*  98 */     this.initialized = true;
/*  99 */     this.rnd = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
/*     */   }
/*     */   
/*     */   protected int next(int bits) {
/* 103 */     this.rnd = this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
/* 104 */     return (int)(this.rnd >>> 48 - bits);
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
/*     */   
/*     */   public int nextInt(int least, int bound) {
/* 118 */     if (least >= bound)
/* 119 */       throw new IllegalArgumentException(); 
/* 120 */     return nextInt(bound - least) + least;
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
/*     */   public long nextLong(long n) {
/* 133 */     if (n <= 0L) {
/* 134 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     long offset = 0L;
/* 141 */     while (n >= 2147483647L) {
/* 142 */       int bits = next(2);
/* 143 */       long half = n >>> 1L;
/* 144 */       long nextn = ((bits & 0x2) == 0) ? half : (n - half);
/* 145 */       if ((bits & 0x1) == 0)
/* 146 */         offset += n - nextn; 
/* 147 */       n = nextn;
/*     */     } 
/* 149 */     return offset + nextInt((int)n);
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
/*     */   
/*     */   public long nextLong(long least, long bound) {
/* 163 */     if (least >= bound)
/* 164 */       throw new IllegalArgumentException(); 
/* 165 */     return nextLong(bound - least) + least;
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
/*     */   public double nextDouble(double n) {
/* 178 */     if (n <= 0.0D)
/* 179 */       throw new IllegalArgumentException("n must be positive"); 
/* 180 */     return nextDouble() * n;
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
/*     */   
/*     */   public double nextDouble(double least, double bound) {
/* 194 */     if (least >= bound)
/* 195 */       throw new IllegalArgumentException(); 
/* 196 */     return nextDouble() * (bound - least) + least;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsr166y/ThreadLocalRandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */