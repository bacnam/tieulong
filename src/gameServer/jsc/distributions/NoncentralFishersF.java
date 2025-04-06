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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoncentralFishersF
/*     */   extends AbstractContinuousDistribution
/*     */ {
/*     */   static final int ITRMAX = 200;
/*     */   private double df1;
/*     */   private double df2;
/*     */   private double lambda;
/*     */   private double logHalfLambda;
/*     */   private double ratio;
/*     */   private double logRatio;
/*     */   private double logGammaHalfV;
/*     */   private FishersF centralF;
/*     */   private NoncentralBeta B;
/*     */   private NoncentralChiSquared chiSquaredU;
/*     */   private ChiSquared chiSquaredV;
/*     */   
/*     */   public NoncentralFishersF(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  46 */     super(0.0D, Double.POSITIVE_INFINITY, false);
/*     */ 
/*     */     
/*  49 */     setParameters(paramDouble1, paramDouble2, paramDouble3);
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
/*  61 */     if (paramDouble < 0.0D)
/*  62 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  63 */     if (paramDouble == 0.0D) return 0.0D; 
/*  64 */     if (this.lambda == 0.0D) return this.centralF.cdf(paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     return this.B.cdf(this.df1 * paramDouble / (this.df1 * paramDouble + this.df2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf1() {
/*  80 */     return this.df1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf2() {
/*  87 */     return this.df2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLambda() {
/*  94 */     return this.lambda;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 108 */     if (this.lambda == 0.0D) {
/* 109 */       return this.centralF.inverseCdf(paramDouble);
/*     */     }
/* 111 */     return super.inverseCdf(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 120 */     return (this.df2 > 2.0D) ? (this.df2 * (this.df1 + this.lambda) / this.df1 * (this.df2 - 2.0D)) : Double.NaN;
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
/*     */   public double pdf(double paramDouble) {
/* 134 */     if (this.lambda == 0.0D) return this.centralF.pdf(paramDouble); 
/* 135 */     if (paramDouble < 0.0D)
/* 136 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 137 */     if (paramDouble == 0.0D) return 0.0D;
/*     */ 
/*     */ 
/*     */     
/* 141 */     double d1 = 0.0D;
/*     */     
/* 143 */     double d2 = Math.log(paramDouble);
/* 144 */     double d3 = 0.5D * this.lambda;
/* 145 */     double d4 = 0.5D * this.df1;
/* 146 */     double d5 = 0.5D * (this.df1 + this.df2);
/* 147 */     double d6 = Math.log(1.0D + this.ratio * paramDouble);
/* 148 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 150 */       double d = Math.exp(b * this.logHalfLambda - d3 + (d4 + b) * this.logRatio + Maths.logGamma(d5 + b) + (d4 + b - 1.0D) * d2 - Maths.logGamma((b + 1)) - Maths.logGamma(d4 + b) - this.logGammaHalfV - (b + d5) * d6);
/*     */ 
/*     */       
/* 153 */       d1 += d;
/*     */       
/* 155 */       if (Math.abs(d) < this.tolerance * d1) return d1;
/*     */     
/*     */     } 
/*     */     
/* 159 */     throw new RuntimeException("Cannot calculate pdf to required accuracy.");
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/* 164 */     if (this.lambda == 0.0D) return this.centralF.random(); 
/* 165 */     return this.df2 * this.chiSquaredU.random() / this.df1 * this.chiSquaredV.random();
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
/*     */   public void setParameters(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 226 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/* 227 */       throw new IllegalArgumentException("Invalid \"degrees of freedom\" parameter."); 
/* 228 */     if (paramDouble3 < 0.0D)
/* 229 */       throw new IllegalArgumentException("Invalid noncentrality parameter."); 
/* 230 */     this.df1 = paramDouble1;
/* 231 */     this.df2 = paramDouble2;
/* 232 */     this.lambda = paramDouble3;
/* 233 */     this.ratio = paramDouble1 / paramDouble2;
/* 234 */     this.logRatio = Math.log(this.ratio);
/* 235 */     this.logGammaHalfV = Maths.logGamma(0.5D * paramDouble2);
/* 236 */     if (paramDouble3 == 0.0D) {
/*     */       
/* 238 */       this.centralF = new FishersF(paramDouble1, paramDouble2);
/* 239 */       this.chiSquaredU = null;
/* 240 */       this.chiSquaredV = null;
/* 241 */       this.B = null;
/*     */     }
/*     */     else {
/*     */       
/* 245 */       this.centralF = null;
/* 246 */       this.chiSquaredU = new NoncentralChiSquared(paramDouble1, paramDouble3);
/* 247 */       this.chiSquaredV = new ChiSquared(paramDouble2);
/* 248 */       this.chiSquaredV.setSeed(this.rand.nextLong());
/* 249 */       this.logHalfLambda = Math.log(0.5D * paramDouble3);
/* 250 */       this.B = new NoncentralBeta(0.5D * paramDouble1, 0.5D * paramDouble2, paramDouble3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 256 */     this.rand.setSeed(paramLong);
/* 257 */     if (this.lambda == 0.0D) {
/* 258 */       this.centralF.setSeed(this.rand.nextLong());
/*     */     } else {
/*     */       
/* 261 */       this.chiSquaredU.setSeed(this.rand.nextLong());
/* 262 */       this.chiSquaredV.setSeed(this.rand.nextLong() + 1L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 267 */     return new String("Noncentral Fisher's F distribution: df1 = " + this.df1 + ", df2 = " + this.df2 + ", lambda = " + this.lambda + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 276 */     return (this.df2 > 4.0D) ? (2.0D / this.ratio * this.ratio * ((this.df1 + this.lambda) * (this.df1 + this.lambda) + (this.df1 + this.lambda + this.lambda) * (this.df2 - 2.0D) / (this.df2 - 2.0D) * (this.df2 - 2.0D) * (this.df2 - 4.0D))) : Double.NaN;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 307 */       char c = '✐';
/*     */       
/* 309 */       NoncentralFishersF noncentralFishersF = new NoncentralFishersF(8.0D, 30.0D, 36.0D);
/* 310 */       noncentralFishersF.setTolerance(1.0E-11D);
/* 311 */       double[] arrayOfDouble = new double[c];
/* 312 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 314 */         arrayOfDouble[b] = noncentralFishersF.random();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 319 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralFishersF, H1.NOT_EQUAL, true);
/* 320 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/NoncentralFishersF.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */