/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.goodnessfit.ChiSquaredFitTest;
/*     */ import jsc.util.Maths;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hypergeometric
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   static final int MBIG = 3000;
/*  27 */   static final double ELIMIT = Math.log(Double.MIN_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final double SCALE = 1.0E300D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int sampleSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int populationSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int markedItemsCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private double P;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hypergeometric(int paramInt1, int paramInt2, int paramInt3) {
/*  57 */     super(Math.max(0, paramInt1 - paramInt2 + paramInt3), Math.min(paramInt1, paramInt3));
/*  58 */     if (paramInt3 < 0 || paramInt2 < paramInt3 || paramInt1 < 0 || paramInt1 > paramInt2) {
/*  59 */       throw new IllegalArgumentException("Invalid distribution parameter.");
/*     */     }
/*  61 */     this.sampleSize = paramInt1;
/*  62 */     this.populationSize = paramInt2;
/*  63 */     this.markedItemsCount = paramInt3;
/*  64 */     this.P = paramInt3 / paramInt2;
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
/*     */   public double cdf(double paramDouble) {
/*  85 */     return chyper(false, this.sampleSize, this.populationSize, this.markedItemsCount, (int)paramDouble);
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
/*     */   private double chyper(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 101 */     boolean bool = true;
/* 102 */     double d3 = 0.0D;
/*     */     
/* 104 */     int i = paramInt1 + 1;
/* 105 */     int j = paramInt4 + 1;
/* 106 */     int k = paramInt2 + 1;
/* 107 */     int m = paramInt3 + 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     if (j < 1 || i - j > k - m)
/* 113 */       throw new IllegalArgumentException("Invalid variate-value " + j); 
/* 114 */     if (!paramBoolean) d3 = 1.0D; 
/* 115 */     if (j > m || j > i) {
/* 116 */       throw new IllegalArgumentException("Invalid variate-value " + j);
/*     */     }
/* 118 */     d3 = 1.0D;
/* 119 */     if (i == 1 || i == k || m == 1 || m == k) return d3;
/*     */     
/* 121 */     double d1 = paramInt3 / (paramInt2 - paramInt3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     if (Math.min(i - 1, k - i) > Math.min(m - 1, k - m)) {
/* 145 */       int i3 = i; i = m; m = i3;
/* 146 */     }  if (k - i < i - 1) {
/*     */       
/* 148 */       bool = !bool ? true : false;
/* 149 */       j = m - j + 1;
/* 150 */       i = k - i + 1;
/*     */     } 
/*     */     
/* 153 */     if (paramInt2 > 3000) {
/*     */ 
/*     */       
/* 156 */       d1 = Maths.logFactorial(paramInt3) - Maths.logFactorial(paramInt2) + Maths.logFactorial((paramInt2 - paramInt1)) + Maths.logFactorial(paramInt1) + Maths.logFactorial((paramInt2 - paramInt3)) - Maths.logFactorial(paramInt4) - Maths.logFactorial((paramInt3 - paramInt4)) - Maths.logFactorial((paramInt1 - paramInt4)) - Maths.logFactorial((paramInt2 - paramInt3 - paramInt1 + paramInt4));
/*     */ 
/*     */ 
/*     */       
/* 160 */       d3 = 0.0D;
/* 161 */       if (d1 >= ELIMIT) d3 = Math.exp(d1);
/*     */     
/*     */     } else {
/*     */       int i3;
/* 165 */       for (i3 = 1; i3 <= j - 1; i3++)
/*     */       {
/* 167 */         d3 *= (i - i3) * (m - i3) / (j - i3) * (k - i3);
/*     */       }
/*     */       
/* 170 */       if (j != i) {
/*     */         
/* 172 */         int i4 = k - m + j;
/* 173 */         for (i3 = j; i3 <= i - 1; i3++) {
/* 174 */           d3 *= (i4 - i3) / (k - i3);
/*     */         }
/*     */       } 
/*     */     } 
/* 178 */     if (paramBoolean) return d3;
/*     */     
/* 180 */     if (d3 == 0.0D) {
/*     */       
/* 182 */       if (paramInt2 <= 3000) d1 = Maths.logFactorial(paramInt3) - Maths.logFactorial(paramInt2) + Maths.logFactorial(paramInt1) + Maths.logFactorial((paramInt2 - paramInt3)) - Maths.logFactorial(paramInt4) - Maths.logFactorial((paramInt3 - paramInt4)) - Maths.logFactorial((paramInt1 - paramInt4)) - Maths.logFactorial((paramInt2 - paramInt3 - paramInt1 + paramInt4)) + Maths.logFactorial((paramInt2 - paramInt1));
/*     */ 
/*     */ 
/*     */       
/* 186 */       d1 += Math.log(1.0E300D);
/*     */ 
/*     */ 
/*     */       
/* 190 */       d1 = Math.exp(d1);
/*     */     }
/*     */     else {
/*     */       
/* 194 */       d1 = d3 * 1.0E300D;
/*     */     } 
/* 196 */     double d2 = 0.0D;
/* 197 */     int n = m - j;
/* 198 */     int i1 = i - j;
/* 199 */     int i2 = k - m - i1 + 1;
/* 200 */     if (j <= i1) {
/*     */       
/* 202 */       for (byte b = 1; b <= j - 1; b++)
/*     */       {
/* 204 */         d1 *= (j - b) * (i2 - b) / (n + b) * (i1 + b);
/*     */         
/* 206 */         d2 += d1;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 213 */       bool = !bool ? true : false;
/* 214 */       for (byte b = 0; b <= i1 - 1; b++) {
/*     */         
/* 216 */         d1 *= (n - b) * (i1 - b) / (j + b) * (i2 + b);
/*     */         
/* 218 */         d2 += d1;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 223 */     d3 = bool ? (d3 + d2 / 1.0E300D) : (1.0D - d2 / 1.0E300D);
/*     */ 
/*     */     
/* 226 */     if (d3 > 1.0D) return 1.0D; 
/* 227 */     if (d3 < 0.0D) return 0.0D; 
/* 228 */     return d3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMarkedItemsCount() {
/* 237 */     return this.markedItemsCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPopulationSize() {
/* 244 */     return this.populationSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSampleSize() {
/* 251 */     return this.sampleSize;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 442 */     return this.sampleSize * this.P;
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
/*     */   public double pdf(double paramDouble) {
/* 458 */     return chyper(true, this.sampleSize, this.populationSize, this.markedItemsCount, (int)paramDouble);
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
/*     */   public double random() {
/*     */     int i;
/* 489 */     double d3, d1 = 0.0D;
/* 490 */     double d2 = this.populationSize;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 495 */     if (this.sampleSize < this.markedItemsCount) {
/* 496 */       i = this.sampleSize; d3 = this.markedItemsCount;
/*     */     } else {
/* 498 */       i = this.markedItemsCount; d3 = this.sampleSize;
/*     */     } 
/* 500 */     for (byte b = 1; b <= i; b++) {
/*     */       
/* 502 */       double d = d3 / d2;
/* 503 */       boolean bool = (this.rand.nextDouble() < d) ? true : false;
/* 504 */       d2--;
/*     */       
/* 506 */       if (bool) { d1++; d3--; }
/*     */       
/* 508 */       if (d3 == 0.0D)
/*     */         break; 
/* 510 */     }  return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 517 */     return new String("Hypergeometric distribution: sample size = " + this.sampleSize + ", population size = " + this.populationSize + ", marked items count = " + this.markedItemsCount + ".");
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
/*     */   public double upperTailProb(double paramDouble) {
/* 533 */     return 1.0D - cdf(((int)paramDouble - 1));
/*     */   }
/*     */   public double variance() {
/* 536 */     return this.sampleSize * this.P * (1.0D - this.P) * (this.populationSize - this.sampleSize) / (this.populationSize - 1.0D);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 556 */       char c2 = 'ߐ', c1 = 'Ϩ', c3 = 'Ϩ';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 580 */       int i = 1000000;
/* 581 */       Hypergeometric hypergeometric = new Hypergeometric(16, 32, 11);
/*     */ 
/*     */ 
/*     */       
/* 585 */       System.out.println(hypergeometric.toString());
/*     */       
/* 587 */       int[] arrayOfInt = new int[i];
/*     */       
/* 589 */       long l1 = System.currentTimeMillis();
/* 590 */       for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)hypergeometric.random(); b++; }
/* 591 */        long l2 = System.currentTimeMillis();
/* 592 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/*     */ 
/*     */       
/* 595 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), hypergeometric, 0);
/* 596 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 597 */       System.out.println("s = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Hypergeometric.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */