/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.goodnessfit.ChiSquaredFitTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WilcoxonT
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   public static final int MAX_PRODUCT = 10000;
/*  23 */   static final double LOG2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */   
/*     */   private int n;
/*     */ 
/*     */   
/*     */   private int n2;
/*     */ 
/*     */   
/*     */   private double[] p;
/*     */ 
/*     */ 
/*     */   
/*     */   public WilcoxonT(int paramInt) {
/*  38 */     super(0L, (paramInt * (paramInt + 1) / 2));
/*     */ 
/*     */     
/*  41 */     if (paramInt < 1)
/*  42 */       throw new IllegalArgumentException("Sample size too small."); 
/*  43 */     if (this.maxValue > 10000L)
/*  44 */       throw new IllegalArgumentException("Cannot calculate exact distribution: try normal approximation."); 
/*  45 */     this.n = paramInt;
/*  46 */     this.n2 = (int)Math.ceil(0.5D * this.maxValue);
/*  47 */     this.p = new double[1 + this.n2];
/*     */     
/*  49 */     for (byte b = 0; b <= this.n2; b++)
/*     */     {
/*  51 */       this.p[b] = Math.exp(Math.log(wcount(paramInt, b)) - paramInt * LOG2);
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
/*     */   public long criticalValue(double paramDouble) {
/*  72 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
/*  73 */     long l = this.minValue;
/*     */ 
/*     */ 
/*     */     
/*  77 */     double d = pdf(l);
/*  78 */     while (l < this.maxValue && d < paramDouble) { l++; d += pdf(l); }
/*     */     
/*  80 */     return --l;
/*     */   }
/*     */   public double mean() {
/*  83 */     return 0.25D * this.n * (this.n + 1.0D);
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
/*     */   public static Normal normalApproximation(int paramInt) {
/*  96 */     if (paramInt < 1)
/*  97 */       throw new IllegalArgumentException("Sample size too small."); 
/*  98 */     double d1 = 0.25D * paramInt * (paramInt + 1.0D);
/*  99 */     double d2 = Math.sqrt(paramInt * (paramInt + 1.0D) / 24.0D * ((paramInt + paramInt) + 1.0D));
/* 100 */     return new Normal(d1, d2);
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
/* 112 */     int i = (int)paramDouble;
/* 113 */     if (i < this.minValue || i > this.maxValue)
/* 114 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 115 */     if (i <= this.n2) {
/* 116 */       return this.p[i];
/*     */     }
/* 118 */     return this.p[(int)(this.maxValue - i)];
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
/*     */   private int wcount(int paramInt1, int paramInt2) {
/* 133 */     if (paramInt1 == 0) {
/* 134 */       if (paramInt2 == 0) {
/* 135 */         return 1;
/*     */       }
/* 137 */       return 0;
/*     */     } 
/* 139 */     if (paramInt2 < 0) {
/* 140 */       return 0;
/*     */     }
/* 142 */     return wcount(paramInt1 - 1, paramInt2) + wcount(paramInt1 - 1, paramInt2 - paramInt1);
/*     */   }
/*     */   public String toString() {
/* 145 */     return new String("Wilcoxon T distribution: n = " + this.n + ".");
/*     */   } public double variance() {
/* 147 */     return this.n * (this.n + 1.0D) / 24.0D * ((this.n + this.n) + 1.0D);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 189 */       char c = '✐';
/*     */       
/* 191 */       WilcoxonT wilcoxonT = new WilcoxonT(25);
/* 192 */       int[] arrayOfInt = new int[c];
/* 193 */       for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)wilcoxonT.random(); b++; }
/*     */       
/* 195 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), wilcoxonT, 0);
/* 196 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 197 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/WilcoxonT.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */