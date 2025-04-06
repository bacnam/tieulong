/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.tests.H1;
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
/*     */ public class FishersF
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private ChiSquared chiSquared1;
/*     */   private ChiSquared chiSquared2;
/*     */   private double df1;
/*     */   private double df2;
/*     */   private double lnB;
/*     */   
/*     */   public FishersF(double paramDouble1, double paramDouble2) {
/*  32 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/*  33 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  34 */     this.df1 = paramDouble1;
/*  35 */     this.df2 = paramDouble2;
/*  36 */     this.lnB = Maths.lnB(0.5D * paramDouble2, 0.5D * paramDouble1);
/*  37 */     this.chiSquared1 = new ChiSquared(paramDouble1);
/*  38 */     this.chiSquared2 = new ChiSquared(paramDouble2);
/*  39 */     this.chiSquared2.setSeed(this.rand.nextLong());
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
/*     */   public double cdf(double paramDouble) {
/*  51 */     if (paramDouble < 0.0D)
/*  52 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  53 */     if (paramDouble == 0.0D) return 0.0D; 
/*  54 */     return 1.0D - Beta.incompleteBeta(this.df2 / (this.df2 + this.df1 * paramDouble), 0.5D * this.df2, 0.5D * this.df1, this.lnB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf1() {
/*  62 */     return this.df1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf2() {
/*  69 */     return this.df2;
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
/*     */   public double inverseCdf(double paramDouble) {
/*  81 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  82 */       throw new IllegalArgumentException("Invalid probability."); 
/*  83 */     if (paramDouble == 0.0D) return 0.0D; 
/*  84 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  85 */     double d = Beta.inverseIncompleteBeta(0.5D * this.df2, 0.5D * this.df1, this.lnB, 1.0D - paramDouble);
/*  86 */     if (d == 0.0D) {
/*  87 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*  89 */     return this.df2 * (1.0D - d) / d * this.df1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/*  98 */     return (this.df2 > 2.0D) ? (this.df2 / (this.df2 - 2.0D)) : Double.NaN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/* 109 */     if (paramDouble < 0.0D)
/* 110 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 111 */     if (paramDouble == 0.0D) return 0.0D;
/*     */     
/* 113 */     return Math.exp(0.5D * (this.df1 * (Math.log(this.df1) - Math.log(this.df2)) + (this.df1 - 2.0D) * Math.log(paramDouble) - (this.df1 + this.df2) * Math.log(1.0D + this.df1 / this.df2 * paramDouble)) - this.lnB);
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/* 118 */     return this.df2 * this.chiSquared1.random() / this.df1 * this.chiSquared2.random();
/*     */   }
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 122 */     this.rand.setSeed(paramLong);
/* 123 */     this.chiSquared1.setSeed(this.rand.nextLong());
/* 124 */     this.chiSquared2.setSeed(this.rand.nextLong());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 128 */     return new String("Fisher's F distribution: df1 = " + this.df1 + ", df2 = " + this.df2 + ".");
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
/*     */   public static double upperTailProb(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 141 */     return Beta.incompleteBeta(paramDouble3 / (paramDouble3 + paramDouble2 * paramDouble1), 0.5D * paramDouble3, 0.5D * paramDouble2, Maths.lnB(0.5D * paramDouble3, 0.5D * paramDouble2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 150 */     return (this.df2 > 4.0D) ? (2.0D * this.df2 * this.df2 * (this.df1 + this.df2 - 2.0D) / this.df1 * (this.df2 - 2.0D) * (this.df2 - 2.0D) * (this.df2 - 4.0D)) : Double.NaN;
/*     */   }
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
/* 162 */       double d1 = 2.0D;
/* 163 */       double d2 = 4.0D;
/* 164 */       FishersF fishersF = new FishersF(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 172 */       char c = '✐';
/*     */ 
/*     */ 
/*     */       
/* 176 */       fishersF = new FishersF(100.0D, 1000.0D);
/* 177 */       double[] arrayOfDouble = new double[c];
/* 178 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = fishersF.random(); b++; }
/*     */ 
/*     */       
/* 181 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, fishersF, H1.NOT_EQUAL, false);
/* 182 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/FishersF.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */