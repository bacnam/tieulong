/*     */ package jsc.contingencytables;
/*     */ 
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.ExtendedHypergeometric;
/*     */ import jsc.distributions.Hypergeometric;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FishersExactTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private double chiSquared;
/*     */   private double p1;
/*     */   private double p1x;
/*     */   private double midP;
/*     */   private double SP;
/*     */   private int testStatistic;
/*     */   
/*     */   public FishersExactTest(ContingencyTable2x2 paramContingencyTable2x2) {
/*  69 */     this(paramContingencyTable2x2, H1.NOT_EQUAL);
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
/*     */   public FishersExactTest(ContingencyTable2x2 paramContingencyTable2x2, H1 paramH1) {
/*     */     int n, i1, i2;
/*     */     double d2, d3;
/*     */     this.p1x = 0.0D;
/*  86 */     Hypergeometric hypergeometric = null;
/*     */     
/*  88 */     int i = paramContingencyTable2x2.getFrequency(0, 0);
/*  89 */     int j = paramContingencyTable2x2.getFrequency(0, 1);
/*  90 */     int k = paramContingencyTable2x2.getFrequency(1, 0);
/*  91 */     int m = paramContingencyTable2x2.getFrequency(1, 1);
/*  92 */     int i5 = i + j + k + m;
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (i * m > j * k) {
/*     */       
/*  98 */       if (j < k) {
/*  99 */         n = j; i1 = i; i2 = m; int i7 = k;
/*     */       } else {
/* 101 */         n = k; i1 = m; i2 = i; int i7 = j;
/*     */       }
/*     */     
/*     */     }
/* 105 */     else if (i < m) {
/* 106 */       n = i; i1 = j; i2 = k; int i7 = m;
/*     */     } else {
/* 108 */       n = m; i1 = k; i2 = j; int i7 = i;
/*     */     } 
/*     */     
/* 111 */     this.testStatistic = n;
/* 112 */     int i3 = n + i1;
/* 113 */     int i4 = n + i2;
/*     */ 
/*     */     
/* 116 */     double d4 = -1.0D;
/*     */     
/* 118 */     if (i5 == 0)
/*     */     {
/* 120 */       throw new IllegalArgumentException("All frequencies are zero.");
/*     */     }
/*     */     
/* 123 */     hypergeometric = new Hypergeometric(i3, i5, i4);
/* 124 */     this.p1 = hypergeometric.cdf(n);
/* 125 */     this.midP = 0.5D * hypergeometric.pdf(n) + hypergeometric.cdf((n - 1));
/*     */ 
/*     */     
/* 128 */     if (n > 0) {
/* 129 */       d3 = hypergeometric.pdf(n);
/*     */     } else {
/* 131 */       d3 = this.p1;
/*     */     } 
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
/* 143 */     int i6 = Math.min(n + i1, n + i2);
/* 144 */     while (i6 >= n + 1) {
/*     */ 
/*     */ 
/*     */       
/* 148 */       if (hypergeometric.pdf(i6) - d3 > 1.0E-16D) {
/*     */         
/* 150 */         d4 = hypergeometric.cdf(i6);
/*     */         break;
/*     */       } 
/* 153 */       i6--;
/*     */     } 
/*     */     
/* 156 */     if (d4 < 0.0D) {
/* 157 */       d2 = 1.0D; this.p1x = 1.0D - this.p1;
/*     */     } else {
/* 159 */       this.p1x = 1.0D - d4; d2 = this.p1 + this.p1x;
/*     */     } 
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
/* 177 */     double d1 = Math.abs(i * m - j * k) - 0.5D * i5;
/*     */     
/* 179 */     this.chiSquared = i5 * d1 * d1 / (i + j) * (k + m) * (i + k) * (j + m);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (paramH1 == H1.NOT_EQUAL) {
/* 186 */       this.SP = Math.min(d2, 1.0D);
/*     */     } else {
/*     */       
/* 189 */       ExtendedHypergeometric extendedHypergeometric = new ExtendedHypergeometric(i + k, j + m, i + j, 1.0D);
/*     */       
/* 191 */       if (paramH1 == H1.LESS_THAN) {
/* 192 */         this.SP = extendedHypergeometric.cdf(i);
/*     */       } else {
/* 194 */         this.SP = 1.0D - extendedHypergeometric.cdf((i - 1));
/*     */       } 
/*     */     } 
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
/*     */   public double getApproxSP() {
/* 249 */     return ChiSquared.upperTailProb(this.chiSquared, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getChiSquared() {
/* 258 */     return this.chiSquared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOneTailedMidP() {
/* 268 */     return this.midP;
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
/*     */   public double getOneTailedSP() {
/* 288 */     return this.p1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOppositeTailProb() {
/* 296 */     return this.p1x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 304 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 311 */     return this.testStatistic;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 366 */       byte b1 = 9;
/* 367 */       ContingencyTable2x2[] arrayOfContingencyTable2x2 = new ContingencyTable2x2[b1];
/* 368 */       arrayOfContingencyTable2x2[0] = new ContingencyTable2x2(3, 1, 1, 3);
/* 369 */       arrayOfContingencyTable2x2[1] = new ContingencyTable2x2(8, 2, 3, 5);
/* 370 */       arrayOfContingencyTable2x2[2] = new ContingencyTable2x2(2, 6, 18, 14);
/* 371 */       arrayOfContingencyTable2x2[3] = new ContingencyTable2x2(2, 3, 4, 5);
/* 372 */       arrayOfContingencyTable2x2[4] = new ContingencyTable2x2(8, 1, 4, 5);
/*     */       
/* 374 */       arrayOfContingencyTable2x2[5] = new ContingencyTable2x2(100, 210, 310, 410);
/* 375 */       arrayOfContingencyTable2x2[6] = new ContingencyTable2x2(200, 410, 620, 820);
/* 376 */       arrayOfContingencyTable2x2[7] = new ContingencyTable2x2(400, 410, 420, 420);
/* 377 */       arrayOfContingencyTable2x2[8] = new ContingencyTable2x2(1000, 2101, 3104, 4105);
/* 378 */       for (byte b2 = 0; b2 < b1; b2++) {
/*     */ 
/*     */         
/* 381 */         FishersExactTest fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2]);
/* 382 */         System.out.println("\n    One tail = " + fishersExactTest.getOneTailedSP());
/* 383 */         System.out.println("      opp.tail = " + fishersExactTest.getOppositeTailProb());
/* 384 */         System.out.println("            SP = " + fishersExactTest.getSP());
/*     */         
/* 386 */         System.out.println("One tail mid-P = " + fishersExactTest.getOneTailedMidP());
/*     */         
/* 388 */         System.out.println("     Approx SP = " + fishersExactTest.getApproxSP());
/* 389 */         fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2], H1.LESS_THAN);
/* 390 */         System.out.println("\"Less than\" SP = " + fishersExactTest.getSP());
/* 391 */         fishersExactTest = new FishersExactTest(arrayOfContingencyTable2x2[b2], H1.GREATER_THAN);
/* 392 */         System.out.println("\"Greater than\" SP = " + fishersExactTest.getSP());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/contingencytables/FishersExactTest.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */