/*     */ package jsc.util;
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
/*     */ public class Rank
/*     */ {
/*     */   private int n;
/*     */   private int s;
/*     */   private int t;
/*     */   private final int[] r;
/*     */   private final double[] rank;
/*     */   
/*     */   public Rank(double[] paramArrayOfdouble, double paramDouble) {
/*  46 */     this(paramArrayOfdouble.length, paramArrayOfdouble, paramDouble);
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
/*     */   public Rank(int paramInt, double[] paramArrayOfdouble, double paramDouble) {
/*  74 */     if (paramInt < 1)
/*  75 */       throw new IllegalArgumentException("No data to rank"); 
/*  76 */     this.n = paramInt;
/*     */ 
/*     */     
/*  79 */     double[] arrayOfDouble1 = new double[paramInt];
/*  80 */     this.r = new int[paramInt]; int i;
/*  81 */     for (i = 0; i < paramInt; ) { this.r[i] = i; i++; }
/*     */     
/*  83 */     this.s = 0; this.t = 0;
/*     */ 
/*     */     
/*  86 */     double[] arrayOfDouble2 = new double[paramInt];
/*  87 */     for (i = 0; i < paramInt; ) { arrayOfDouble2[i] = paramArrayOfdouble[i]; i++; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     Sort.sort(arrayOfDouble2, this.r, 0, paramInt - 1, true);
/*     */     
/*  95 */     arrayOfDouble1[paramInt - 1] = (paramInt - 1); paramInt--;
/*  96 */     for (i = 0; i < paramInt; i++) {
/*     */       
/*  98 */       if (Math.abs(arrayOfDouble2[i] - arrayOfDouble2[i + 1]) > paramDouble) {
/*  99 */         arrayOfDouble1[i] = i;
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 107 */         byte b1 = 1;
/* 108 */         for (int j = i + 1; j < paramInt; ) {
/*     */           
/* 110 */           if (Math.abs(arrayOfDouble2[j] - arrayOfDouble2[j + 1]) <= paramDouble) {
/* 111 */             b1++;
/*     */             j++;
/*     */           } 
/*     */           break;
/*     */         } 
/* 116 */         double d = i + 0.5D * b1;
/* 117 */         for (byte b2 = 0; b2 <= b1; ) { arrayOfDouble1[i + b2] = d; b2++; }
/* 118 */          int k = b1 * (b1 + 1);
/* 119 */         this.s += k;
/* 120 */         this.t += k * (b1 + 2);
/* 121 */         i += b1;
/*     */       } 
/*     */     } 
/* 124 */     paramInt++;
/* 125 */     this.rank = new double[paramInt];
/*     */     
/* 127 */     for (byte b = 0; b < paramInt; ) { this.rank[this.r[b]] = arrayOfDouble1[b] + 1.0D; b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCorrectionFactor1() {
/* 138 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCorrectionFactor2() {
/* 146 */     return this.s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 153 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRank(int paramInt) {
/* 161 */     return this.rank[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getRanks() {
/* 168 */     return this.rank;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSortIndex(int paramInt) {
/* 177 */     return this.r[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getSortIndexes() {
/* 185 */     return this.r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTies() {
/* 193 */     return (this.t > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 203 */       double[] arrayOfDouble1 = { 5.0D, 3.0D, 0.0D, 7.0D, 5.0D, 8.0D, 9.0D, 1.0D, 5.0D, 4.0D };
/* 204 */       Rank rank = new Rank(arrayOfDouble1, 0.0D);
/* 205 */       double[] arrayOfDouble2 = rank.getRanks();
/* 206 */       int[] arrayOfInt = rank.getSortIndexes(); byte b;
/* 207 */       for (b = 0; b < rank.getN(); b++)
/* 208 */         System.out.println("Rank of " + arrayOfDouble1[b] + " is " + arrayOfDouble2[b]); 
/* 209 */       for (b = 0; b < rank.getN(); b++)
/* 210 */         System.out.println("Rank " + b + ": " + arrayOfDouble2[arrayOfInt[b]]); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Rank.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */