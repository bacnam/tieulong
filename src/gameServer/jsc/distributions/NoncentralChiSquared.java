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
/*     */ public class NoncentralChiSquared
/*     */   extends AbstractContinuousDistribution
/*     */ {
/*     */   static final int ITRMAX = 100;
/*  25 */   static final double LOG2 = Math.log(2.0D);
/*     */ 
/*     */   
/*     */   private double delta;
/*     */   
/*     */   private double df;
/*     */   
/*     */   private double halfDf;
/*     */   
/*     */   private double logGammaHalfDf1;
/*     */   
/*     */   private double logDelta;
/*     */   
/*     */   private double sqrtDelta;
/*     */   
/*     */   private double expMhalfDelta;
/*     */   
/*     */   private ChiSquared chiSquared;
/*     */   
/*     */   private ChiSquared chiSquaredVm1;
/*     */ 
/*     */   
/*     */   public NoncentralChiSquared(double paramDouble1, double paramDouble2) {
/*  48 */     super(0.0D, Double.POSITIVE_INFINITY, true);
/*     */ 
/*     */     
/*  51 */     setParameters(paramDouble1, paramDouble2);
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
/*     */   public double cdf(double paramDouble) {
/*  68 */     if (paramDouble < 0.0D)
/*  69 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  70 */     if (paramDouble == 0.0D) return 0.0D; 
/*  71 */     if (this.delta == 0.0D) return this.chiSquared.cdf(paramDouble); 
/*  72 */     double d1 = this.df;
/*     */     
/*  74 */     double d2 = this.delta / 2.0D;
/*     */ 
/*     */     
/*  77 */     double d3 = 1.0D;
/*     */     
/*  79 */     double d4 = this.expMhalfDelta;
/*  80 */     double d5 = d4;
/*  81 */     double d6 = paramDouble / 2.0D;
/*  82 */     double d7 = this.halfDf;
/*  83 */     double d8 = Math.pow(d6, d7) * Math.exp(-d6) / this.logGammaHalfDf1;
/*  84 */     double d9 = d5 * d8;
/*  85 */     double d10 = d9;
/*     */ 
/*     */ 
/*     */     
/*  89 */     while (d1 + 2.0D * d3 - paramDouble <= 0.0D) {
/*     */ 
/*     */       
/*  92 */       d4 = d4 * d2 / d3;
/*  93 */       d5 += d4;
/*  94 */       d8 = d8 * paramDouble / (d1 + 2.0D * d3);
/*  95 */       d9 = d5 * d8;
/*  96 */       d10 += d9;
/*  97 */       d3++;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 103 */       double d = d8 * paramDouble / (d1 + 2.0D * d3 - paramDouble);
/* 104 */       if (d < this.tolerance) return d10;
/*     */       
/* 106 */       d4 = d4 * d2 / d3;
/* 107 */       d5 += d4;
/* 108 */       d8 = d8 * paramDouble / (d1 + 2.0D * d3);
/* 109 */       d9 = d5 * d8;
/* 110 */       d10 += d9;
/* 111 */       d3++;
/* 112 */       if (d3 > 100.0D) {
/* 113 */         throw new RuntimeException("Cannot calculate cdf to required accuracy.");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDelta() {
/* 121 */     return this.delta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf() {
/* 128 */     return this.df;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 150 */     if (this.delta == 0.0D) {
/* 151 */       return this.chiSquared.inverseCdf(paramDouble);
/*     */     }
/* 153 */     return super.inverseCdf(paramDouble);
/*     */   }
/*     */   public double mean() {
/* 156 */     return this.df + this.delta;
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
/* 168 */     if (paramDouble < 0.0D)
/* 169 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 170 */     if (this.delta == 0.0D) return this.chiSquared.pdf(paramDouble);
/*     */     
/* 172 */     if (paramDouble == 0.0D) return 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     double d1 = 0.0D;
/*     */     
/* 179 */     double d2 = -0.5D * (paramDouble + this.delta + this.df * LOG2);
/* 180 */     double d3 = Math.log(paramDouble);
/* 181 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 183 */       double d = Math.exp(d2 + b * this.logDelta + ((b - 1) + this.halfDf) * d3 - 2.0D * b * LOG2 - Maths.logGamma((b + 1)) - Maths.logGamma(this.halfDf + b));
/*     */       
/* 185 */       d1 += d;
/*     */       
/* 187 */       if (d < this.tolerance * d1) return d1; 
/*     */     } 
/* 189 */     throw new RuntimeException("Cannot calculate pdf to required accuracy.");
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
/*     */   public double random() {
/* 202 */     if (this.delta == 0.0D) return this.chiSquared.random();
/*     */     
/* 204 */     if (this.df > 1.0D && this.df == Math.floor(this.df)) {
/*     */       
/* 206 */       double d = this.rand.nextGaussian() + this.sqrtDelta;
/* 207 */       return d * d + this.chiSquaredVm1.random();
/*     */     } 
/* 209 */     return super.random();
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
/*     */   public void setParameters(double paramDouble1, double paramDouble2) {
/* 270 */     if (paramDouble2 < 0.0D)
/* 271 */       throw new IllegalArgumentException("Invalid noncentrality parameter."); 
/* 272 */     if (paramDouble1 < 0.0D || (paramDouble1 == 0.0D && paramDouble2 == 0.0D))
/* 273 */       throw new IllegalArgumentException("Invalid degrees of freedom."); 
/* 274 */     this.delta = paramDouble2;
/* 275 */     this.df = paramDouble1;
/* 276 */     if (paramDouble2 == 0.0D) {
/* 277 */       this.chiSquared = new ChiSquared(paramDouble1);
/*     */     } else {
/*     */       
/* 280 */       this.chiSquared = null;
/* 281 */       this.logDelta = Math.log(paramDouble2);
/* 282 */       this.sqrtDelta = Math.sqrt(paramDouble2);
/* 283 */       this.expMhalfDelta = Math.exp(-0.5D * paramDouble2);
/* 284 */       if (paramDouble1 > 1.0D && paramDouble1 == Math.floor(paramDouble1)) this.chiSquaredVm1 = new ChiSquared(paramDouble1 - 1.0D); 
/* 285 */       this.halfDf = 0.5D * paramDouble1;
/* 286 */       this.logGammaHalfDf1 = Math.exp(Maths.logGamma(this.halfDf + 1.0D));
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 291 */     return new String("Noncentral chi-squared distribution: df = " + this.df + ", delta = " + this.delta + ".");
/*     */   } public double variance() {
/* 293 */     return 2.0D * (this.df + this.delta + this.delta);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 317 */       char c = '✐';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 328 */       NoncentralChiSquared noncentralChiSquared = new NoncentralChiSquared(4.1D, 0.5D);
/*     */       
/* 330 */       double[] arrayOfDouble = new double[c];
/* 331 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 333 */         arrayOfDouble[b] = noncentralChiSquared.random();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 338 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralChiSquared, H1.NOT_EQUAL, true);
/* 339 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/NoncentralChiSquared.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */