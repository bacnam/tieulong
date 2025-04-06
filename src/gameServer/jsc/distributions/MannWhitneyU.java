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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MannWhitneyU
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   public static final int MAX_PRODUCT = 10000;
/*     */   private int n;
/*     */   private int nA;
/*     */   private int nB;
/*     */   private double[] p;
/*     */   
/*     */   public MannWhitneyU(int paramInt1, int paramInt2) {
/*  45 */     super(0L, (paramInt1 * paramInt2));
/*     */ 
/*     */     
/*  48 */     if (this.maxValue > 10000L)
/*  49 */       throw new IllegalArgumentException("Cannot calculate exact distribution: try normal approximation."); 
/*  50 */     this.nA = paramInt1;
/*  51 */     this.nB = paramInt2;
/*  52 */     this.n = (int)Math.ceil(0.5D * this.maxValue);
/*  53 */     this.p = new double[1 + this.n];
/*  54 */     harding(false, paramInt1, paramInt2, this.n, this.p);
/*     */ 
/*     */     
/*  57 */     double d = Maths.logBinomialCoefficient((paramInt1 + paramInt2), paramInt1);
/*     */     
/*  59 */     for (byte b = 0; b <= this.n; b++) {
/*     */ 
/*     */ 
/*     */       
/*  63 */       this.p[b] = Math.exp(Math.log(this.p[b]) - d);
/*  64 */       if (Double.isNaN(this.p[b]))
/*     */       {
/*     */         
/*  67 */         throw new RuntimeException("Cannot calculate exact distribution: try normal approximation.");
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
/*     */   public long criticalValue(double paramDouble) {
/*  92 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
/*  93 */     long l = this.minValue;
/*     */ 
/*     */ 
/*     */     
/*  97 */     double d = pdf(l);
/*  98 */     while (l < this.maxValue && d < paramDouble) { l++; d += pdf(l); }
/*  99 */      if (Math.abs(d - paramDouble) < 1.0E-11D) return l; 
/* 100 */     return --l;
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
/*     */   public static void harding(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble) {
/* 130 */     if (paramInt1 < 1 || paramInt2 < 1) {
/* 131 */       throw new IllegalArgumentException("Sample size too small.");
/*     */     }
/* 133 */     if (!paramBoolean) {
/*     */       
/* 135 */       paramArrayOfdouble[0] = 1.0D;
/* 136 */       for (byte b1 = 1; b1 <= paramInt3; ) { paramArrayOfdouble[b1] = 0.0D; b1++; }
/*     */     
/*     */     } 
/* 139 */     if (paramInt2 + 1 <= paramInt3) {
/*     */       
/* 141 */       int j = Math.min(paramInt1 + paramInt2, paramInt3);
/* 142 */       for (int k = paramInt2 + 1; k <= j; k++) {
/* 143 */         for (int m = paramInt3; m >= k; m--)
/*     */         {
/* 145 */           paramArrayOfdouble[m] = paramArrayOfdouble[m] - paramArrayOfdouble[m - k];
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 151 */     int i = Math.min(paramInt1, paramInt3);
/* 152 */     for (byte b = 1; b <= i; b++) {
/* 153 */       for (byte b1 = b; b1 <= paramInt3; b1++)
/*     */       {
/* 155 */         paramArrayOfdouble[b1] = paramArrayOfdouble[b1] + paramArrayOfdouble[b1 - b];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double mean() {
/* 162 */     return 0.5D * this.nA * this.nB;
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
/*     */   public static Normal normalApproximation(int paramInt1, int paramInt2, int paramInt3) {
/* 177 */     double d1 = (paramInt1 + paramInt2);
/* 178 */     double d2 = (paramInt1 * paramInt2);
/* 179 */     double d3 = (d1 * d1 * d1 - d1 - paramInt3) / 12.0D;
/* 180 */     if (paramInt3 < 0 || d3 <= 0.0D) {
/* 181 */       throw new IllegalArgumentException("Invalid samples sizes or correction factor.");
/*     */     }
/* 183 */     double d4 = 0.5D * d2;
/* 184 */     double d5 = Math.sqrt(d2 / d1 * (d1 - 1.0D) * d3);
/* 185 */     return new Normal(d4, d5);
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
/*     */   public double pdf(double paramDouble) {
/* 197 */     int i = (int)paramDouble;
/* 198 */     if (i < this.minValue || i > this.maxValue)
/* 199 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 200 */     if (i <= this.n) {
/* 201 */       return this.p[i];
/*     */     }
/* 203 */     return this.p[(int)(this.maxValue - i)];
/*     */   }
/*     */   public String toString() {
/* 206 */     return new String("Mann-Whitney U distribution: nA = " + this.nA + ", nB = " + this.nB + ".");
/*     */   } public double variance() {
/* 208 */     return (this.nA * this.nB) * ((this.nA + this.nB) + 1.0D) / 12.0D;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 276 */       MannWhitneyU mannWhitneyU = new MannWhitneyU(50, 40);
/* 277 */       char c = '✐';
/* 278 */       int[] arrayOfInt = new int[c];
/* 279 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 281 */         arrayOfInt[b] = (int)mannWhitneyU.random();
/*     */       }
/*     */       
/* 284 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), mannWhitneyU, 0);
/* 285 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 286 */       System.out.println("n = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/MannWhitneyU.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */