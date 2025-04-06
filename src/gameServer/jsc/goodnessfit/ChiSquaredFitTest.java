/*     */ package jsc.goodnessfit;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import jsc.descriptive.CategoricalTally;
/*     */ import jsc.descriptive.DoubleFrequencyTable;
/*     */ import jsc.descriptive.DoubleTally;
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.DiscreteUniform;
/*     */ import jsc.distributions.Distribution;
/*     */ import jsc.tests.SignificanceTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChiSquaredFitTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private int df;
/*     */   private int k;
/*     */   private int n;
/*     */   private int estimatedParasCount;
/*  27 */   private int smallExpectedFrequencyCount = 0;
/*     */ 
/*     */   
/*     */   private double chi;
/*     */ 
/*     */   
/*     */   private int[] O;
/*     */   
/*     */   private double[] E;
/*     */   
/*     */   private double[] probs;
/*     */   
/*     */   private double[] resids;
/*     */   
/*     */   private double SP;
/*     */ 
/*     */   
/*     */   public ChiSquaredFitTest(int[] paramArrayOfint) {
/*  45 */     this(paramArrayOfint, getUniformProbs(paramArrayOfint.length), 0);
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
/*     */   public ChiSquaredFitTest(int[] paramArrayOfint, double[] paramArrayOfdouble, int paramInt) {
/*  63 */     int i = paramArrayOfint.length;
/*  64 */     int j = 0;
/*  65 */     for (byte b = 0; b < i; ) { j += paramArrayOfint[b]; b++; }
/*  66 */      calculateChiSquared(j, i, paramArrayOfint, paramArrayOfdouble, paramInt);
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
/*     */   public ChiSquaredFitTest(CategoricalTally paramCategoricalTally) {
/*  79 */     this(paramCategoricalTally, getUniformProbs(paramCategoricalTally.getNumberOfBins()), 0);
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
/*     */   public ChiSquaredFitTest(CategoricalTally paramCategoricalTally, double[] paramArrayOfdouble, int paramInt) {
/*  99 */     calculateChiSquared(paramCategoricalTally.getN(), paramCategoricalTally.getNumberOfBins(), paramCategoricalTally.getFrequencies(), paramArrayOfdouble, paramInt);
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
/*     */   public ChiSquaredFitTest(Tally paramTally) {
/* 112 */     this(paramTally, (Distribution)new DiscreteUniform(paramTally.getMin(), paramTally.getMax()), 0);
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
/*     */   public ChiSquaredFitTest(Tally paramTally, Distribution paramDistribution, int paramInt) {
/* 130 */     int i = paramTally.getNumberOfBins();
/* 131 */     double[] arrayOfDouble = new double[i];
/* 132 */     for (byte b = 0; b < i; b++)
/* 133 */       arrayOfDouble[b] = paramDistribution.pdf(paramTally.getBinValue(b)); 
/* 134 */     calculateChiSquared(paramTally.getN(), i, paramTally.getFrequencies(), arrayOfDouble, paramInt);
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
/*     */   public ChiSquaredFitTest(DoubleTally paramDoubleTally, Distribution paramDistribution, int paramInt) {
/* 153 */     int i = paramDoubleTally.getValueCount();
/* 154 */     int[] arrayOfInt = new int[i];
/* 155 */     double[] arrayOfDouble = new double[i];
/* 156 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 158 */       arrayOfInt[b] = paramDoubleTally.getFrequency(b);
/* 159 */       arrayOfDouble[b] = paramDistribution.pdf(paramDoubleTally.getValue(b));
/*     */     } 
/* 161 */     calculateChiSquared(paramDoubleTally.getN(), i, arrayOfInt, arrayOfDouble, paramInt);
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
/*     */   public ChiSquaredFitTest(DoubleFrequencyTable paramDoubleFrequencyTable, Distribution paramDistribution, int paramInt) {
/* 184 */     int i = paramDoubleFrequencyTable.getNumberOfBins();
/* 185 */     double[] arrayOfDouble = new double[i];
/*     */ 
/*     */     
/* 188 */     double d = paramDistribution.cdf(paramDoubleFrequencyTable.getBoundary(0));
/* 189 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 191 */       double d1 = paramDistribution.cdf(paramDoubleFrequencyTable.getBoundary(1 + b));
/* 192 */       arrayOfDouble[b] = d1 - d;
/* 193 */       d = d1;
/*     */     } 
/* 195 */     calculateChiSquared(paramDoubleFrequencyTable.getN(), i, paramDoubleFrequencyTable.getFrequencies(), arrayOfDouble, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateChiSquared(int paramInt1, int paramInt2, int[] paramArrayOfint, double[] paramArrayOfdouble, int paramInt3) {
/* 202 */     if (paramInt3 < 0)
/* 203 */       throw new IllegalArgumentException("Negative number of estimated parameters."); 
/* 204 */     this.df = paramInt2 - paramInt3 - 1;
/* 205 */     if (this.df < 1) {
/* 206 */       throw new IllegalArgumentException("Zero degrees of freedom.");
/*     */     }
/* 208 */     this.n = paramInt1;
/* 209 */     this.k = paramInt2;
/* 210 */     this.O = paramArrayOfint;
/* 211 */     this.estimatedParasCount = paramInt3;
/* 212 */     this.probs = paramArrayOfdouble;
/*     */     
/* 214 */     this.E = new double[paramInt2];
/* 215 */     this.resids = new double[paramInt2];
/*     */ 
/*     */     
/* 218 */     this.chi = 0.0D;
/* 219 */     this.smallExpectedFrequencyCount = 0;
/* 220 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 222 */       if (paramArrayOfdouble[b] < 0.0D || paramArrayOfdouble[b] > 1.0D)
/* 223 */         throw new IllegalArgumentException("Invalid probability."); 
/* 224 */       this.E[b] = paramInt1 * paramArrayOfdouble[b];
/* 225 */       if (this.E[b] == 0.0D)
/* 226 */         throw new IllegalArgumentException("An expected frequency is zero."); 
/* 227 */       if (this.E[b] < 5.0D) this.smallExpectedFrequencyCount++; 
/* 228 */       this.resids[b] = (this.O[b] - this.E[b]) * (this.O[b] - this.E[b]) / this.E[b];
/* 229 */       this.chi += this.resids[b];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 234 */     this.SP = ChiSquared.upperTailProb(this.chi, this.df);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDegreesOfFreedom() {
/* 242 */     return this.df;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 250 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfBins() {
/* 258 */     return this.k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getExpectedFrequencies() {
/* 265 */     return this.E;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getExpectedFrequency(int paramInt) {
/* 273 */     return this.E[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getObservedFrequencies() {
/* 280 */     return this.O;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getObservedFrequency(int paramInt) {
/* 288 */     return this.O[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getResiduals() {
/* 295 */     return this.resids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getResidual(int paramInt) {
/* 303 */     return this.resids[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSmallExpectedFrequencyCount() {
/* 312 */     return this.smallExpectedFrequencyCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 319 */     return this.chi;
/*     */   }
/*     */   
/*     */   private static double[] getUniformProbs(int paramInt) {
/* 323 */     double[] arrayOfDouble = new double[paramInt];
/* 324 */     Arrays.fill(arrayOfDouble, 1.0D / paramInt);
/* 325 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 333 */     return this.SP;
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
/*     */   public boolean poolBins() {
/*     */     int i;
/*     */     do {
/* 355 */       i = this.k;
/* 356 */       if (this.df == 1) return false; 
/* 357 */       for (byte b = 0; b < this.k - 1; b++) {
/*     */         
/* 359 */         if (this.E[b] < 5.0D) {
/*     */           
/* 361 */           double d = this.E[b];
/* 362 */           byte b1 = b; int j;
/* 363 */           for (j = b1 + 1; j < this.k; j++) {
/*     */             
/* 365 */             d += this.E[j];
/* 366 */             if (d >= 5.0D)
/*     */               break; 
/*     */           } 
/* 369 */           if (j == this.k) j--; 
/* 370 */           poolBins(b1, j);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 376 */       if (this.E[this.k - 1] >= 5.0D || this.k <= 2) continue;  poolBins(this.k - 2, this.k - 1);
/*     */     }
/* 378 */     while (i != this.k && this.k > 2);
/*     */     
/* 380 */     return (this.smallExpectedFrequencyCount == 0);
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
/*     */   public double poolBins(int paramInt1, int paramInt2) {
/* 439 */     int j = paramInt2 - paramInt1;
/* 440 */     int k = this.k - j;
/* 441 */     int m = 0;
/* 442 */     double d = 0.0D;
/* 443 */     int[] arrayOfInt = new int[k];
/* 444 */     double[] arrayOfDouble = new double[k];
/*     */     
/* 446 */     if (j < 1 || paramInt1 < 0 || paramInt2 >= this.k)
/* 447 */       throw new IllegalArgumentException("Invalid pool indexes."); 
/*     */     int i;
/* 449 */     for (i = 0; i < paramInt1; ) { arrayOfDouble[i] = this.probs[i]; arrayOfInt[i] = this.O[i]; i++; }
/* 450 */      for (i = paramInt1; i <= paramInt2; ) { d += this.probs[i]; m += this.O[i]; i++; }
/* 451 */      arrayOfDouble[paramInt1] = d; arrayOfInt[paramInt1] = m;
/* 452 */     for (i = paramInt1 + 1; i < k; ) { arrayOfDouble[i] = this.probs[i + j]; arrayOfInt[i] = this.O[i + j]; i++; }
/*     */     
/* 454 */     calculateChiSquared(this.n, k, arrayOfInt, arrayOfDouble, this.estimatedParasCount);
/* 455 */     return this.n * d;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 504 */       int[] arrayOfInt = { 1, 2, 3, 2, 1, 3, 4, 2, 1 };
/* 505 */       double[] arrayOfDouble = { 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D };
/* 506 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(arrayOfInt, arrayOfDouble, 0);
/* 507 */       chiSquaredFitTest.poolBins();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 516 */       for (byte b = 0; b < chiSquaredFitTest.getNumberOfBins(); b++) {
/* 517 */         System.out.println("O = " + chiSquaredFitTest.getObservedFrequency(b) + " E = " + chiSquaredFitTest.getExpectedFrequency(b) + " resid = " + chiSquaredFitTest.getResidual(b));
/*     */       }
/* 519 */       System.out.println("Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/goodnessfit/ChiSquaredFitTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */