/*     */ package jsc.distributions;
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
/*     */ public class HotellingPabstS
/*     */   extends RankSumOfSquares
/*     */ {
/*     */   public HotellingPabstS(int paramInt) {
/*  37 */     super(2, paramInt);
/*     */   } public double mean() {
/*  39 */     return this.k * ((this.k * this.k) - 1.0D) / 6.0D;
/*     */   } public double variance() {
/*  41 */     return mean() * mean() / (this.k - 1.0D);
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
/*     */   public static double lowerTailProb(int paramInt1, int paramInt2, boolean paramBoolean) {
/*  55 */     return upperTailProb(paramInt1, (int)(paramInt1 * ((paramInt1 * paramInt1) - 1.0D) / 3.0D - paramInt2), paramBoolean);
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
/*     */   public static double upperTailProb(int paramInt1, int paramInt2, boolean paramBoolean) {
/*  75 */     double d = 1.0D;
/*     */ 
/*     */     
/*  78 */     if (paramInt1 <= 1) {
/*  79 */       throw new IllegalArgumentException("Less than two observations.");
/*     */     }
/*  81 */     if (paramInt2 <= 0) return d; 
/*  82 */     d = 0.0D;
/*  83 */     if (paramInt2 > paramInt1 * (paramInt1 * paramInt1 - 1) / 3) return d; 
/*  84 */     int i = paramInt2;
/*  85 */     if (i != 2 * i / 2) i++;
/*     */     
/*  87 */     if (paramBoolean) {
/*     */       
/*  89 */       double d1 = 1.0D / paramInt1;
/*  90 */       double d2 = (6.0D * (i - 1.0D) * d1 / (1.0D / d1 * d1 - 1.0D) - 1.0D) * Math.sqrt(1.0D / d1 - 1.0D);
/*     */       
/*  92 */       double d3 = d2 * d2;
/*  93 */       double d4 = d2 * d1 * (0.2274D + d1 * (0.2531D + 0.1745D * d1) + d3 * (-0.0758D + d1 * (0.1033D + 0.3932D * d1) - d3 * d1 * (0.0879D + 0.0151D * d1 - d3 * (0.0072D - 0.0831D * d1 + d3 * d1 * (0.0131D - 4.6E-4D * d3)))));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       d = d4 / Math.exp(d3 / 2.0D) + Normal.standardTailProb(d2, true);
/*  99 */       if (d < 0.0D) d = 0.0D; 
/* 100 */       if (d > 1.0D) d = 1.0D; 
/* 101 */       return d;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     int[] arrayOfInt = new int[paramInt1 + 1];
/* 108 */     int j = 1; byte b1;
/* 109 */     for (b1 = 1; b1 <= paramInt1; b1++) {
/* 110 */       j *= b1; arrayOfInt[b1] = b1;
/* 111 */     }  d = 1.0D / j;
/* 112 */     if (i == paramInt1 * (paramInt1 * paramInt1 - 1) / 3) return d; 
/* 113 */     byte b2 = 0;
/* 114 */     for (byte b3 = 1; b3 <= j; b3++) {
/*     */       
/* 116 */       int k = 0;
/* 117 */       for (b1 = 1; b1 <= paramInt1; b1++)
/* 118 */         k += (b1 - arrayOfInt[b1]) * (b1 - arrayOfInt[b1]); 
/* 119 */       if (i <= k) b2++; 
/* 120 */       int m = paramInt1;
/*     */       do {
/* 122 */         int n = arrayOfInt[1];
/* 123 */         int i1 = m - 1;
/* 124 */         for (b1 = 1; b1 <= i1; b1++)
/* 125 */           arrayOfInt[b1] = arrayOfInt[b1 + 1]; 
/* 126 */         arrayOfInt[m] = n;
/* 127 */         if (arrayOfInt[m] != m || m == 2)
/* 128 */           break;  m--;
/*     */       }
/* 130 */       while (b3 != j);
/*     */     } 
/*     */     
/* 133 */     return b2 / j;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 137 */     return new String("Hotelling-Pabst distribution: n = " + this.k + ".");
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 151 */       byte b2 = 6;
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
/* 188 */       System.out.println("Discrete distribution");
/* 189 */       HotellingPabstS hotellingPabstS = new HotellingPabstS(4);
/* 190 */       for (byte b1 = 0; b1 < hotellingPabstS.getValueCount(); b1++)
/* 191 */         System.out.println("P( X = " + hotellingPabstS.getValue(b1) + " ) = " + hotellingPabstS.getProb(b1)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/HotellingPabstS.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */