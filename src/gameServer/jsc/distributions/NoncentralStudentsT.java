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
/*     */ public class NoncentralStudentsT
/*     */   extends AbstractContinuousDistribution
/*     */ {
/*     */   static final int VMAX = 340;
/*     */   static final int ITRMAX = 1000;
/*  25 */   static final double R2PI = Math.sqrt(0.6366197723675814D);
/*  26 */   static final double ALNRPI = 0.5D * Math.log(Math.PI);
/*  27 */   static final double LGHALF = Maths.logGamma(0.5D);
/*     */   
/*     */   private double df;
/*     */   
/*     */   private double delta;
/*     */   
/*     */   private ChiSquared chiSquared;
/*     */   
/*     */   private Normal normalApprox;
/*     */   
/*     */   private StudentsT studentsT;
/*     */   
/*     */   private double logGammaHalfV;
/*     */   
/*     */   private double logSqrt2dv;
/*     */   
/*     */   private double constant;
/*     */   
/*     */   private double HD2;
/*     */   
/*     */   private double albeta;
/*     */   
/*     */   private double pConst;
/*     */ 
/*     */   
/*     */   public NoncentralStudentsT(double paramDouble1, double paramDouble2) {
/*  53 */     super(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
/*     */ 
/*     */     
/*  56 */     setParameters(paramDouble1, paramDouble2);
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
/*  73 */     if (this.delta == 0.0D) return this.studentsT.cdf(paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     double d1 = 0.0D;
/*  86 */     double d4 = paramDouble;
/*  87 */     double d2 = this.delta;
/*  88 */     boolean bool = false;
/*  89 */     if (paramDouble < 0.0D) {
/*     */       
/*  91 */       bool = true;
/*  92 */       d4 = -d4;
/*  93 */       d2 = -d2;
/*     */     } 
/*     */ 
/*     */     
/*  97 */     double d3 = 1.0D;
/*  98 */     double d6 = paramDouble * paramDouble;
/*  99 */     double d5 = d6 / (d6 + this.df);
/* 100 */     if (d5 > 0.0D) {
/*     */       
/* 102 */       double d9, d12 = d2 * d2;
/*     */       
/* 104 */       double d13 = this.pConst;
/*     */ 
/*     */ 
/*     */       
/* 108 */       double d14 = R2PI * d13 * d2;
/* 109 */       double d16 = 0.5D - d13;
/* 110 */       double d7 = 0.5D;
/* 111 */       double d8 = 0.5D * this.df;
/* 112 */       double d15 = Math.pow(1.0D - d5, d8);
/*     */       
/* 114 */       double d18 = Beta.incompleteBeta(d5, d7, d8, this.albeta);
/* 115 */       double d11 = 2.0D * d15 * Math.exp(d7 * Math.log(d5) - this.albeta);
/* 116 */       double d17 = 1.0D - d15;
/* 117 */       double d10 = d8 * d5 * d15;
/* 118 */       d1 = d13 * d18 + d14 * d17;
/*     */       
/*     */       do {
/* 121 */         d7++;
/* 122 */         d18 -= d11;
/* 123 */         d17 -= d10;
/* 124 */         d11 = d11 * d5 * (d7 + d8 - 1.0D) / d7;
/* 125 */         d10 = d10 * d5 * (d7 + d8 - 0.5D) / (d7 + 0.5D);
/* 126 */         double d = d3 + d3;
/* 127 */         d13 = d13 * d12 / d;
/* 128 */         d14 = d14 * d12 / (d + 1.0D);
/* 129 */         d16 -= d13;
/* 130 */         d3++;
/* 131 */         d1 = d1 + d13 * d18 + d14 * d17;
/* 132 */         d9 = 2.0D * d16 * (d18 - d11);
/* 133 */       } while (d9 > this.tolerance && d3 <= 1000.0D);
/*     */     } 
/*     */     
/* 136 */     if (d3 > 1000.0D)
/* 137 */       throw new RuntimeException("Cannot calculate cdf to required accuracy."); 
/* 138 */     d1 += Normal.standardTailProb(d2, true);
/* 139 */     if (bool) d1 = 1.0D - d1; 
/* 140 */     return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDelta() {
/* 148 */     return this.delta;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf() {
/* 155 */     return this.df;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 197 */     if (this.delta == 0.0D) {
/* 198 */       return this.studentsT.inverseCdf(paramDouble);
/*     */     }
/* 200 */     return super.inverseCdf(paramDouble);
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
/*     */   public double mean() {
/* 212 */     if (this.df <= 1.0D) return Double.NaN; 
/* 213 */     if (this.delta == 0.0D) return 0.0D; 
/* 214 */     return this.delta * Math.sqrt(0.5D * this.df) * Math.exp(Maths.logGamma(0.5D * (this.df - 1.0D)) - this.logGammaHalfV);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Normal normalApproximation() {
/* 225 */     return this.normalApprox;
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
/* 237 */     if (this.delta == 0.0D) return this.studentsT.pdf(paramDouble); 
/* 238 */     if (paramDouble == 0.0D) return Math.exp(-this.HD2 + Maths.logGamma(0.5D * (this.df + 1.0D))) / this.constant; 
/* 239 */     if (this.df > 340.0D) return this.normalApprox.pdf(paramDouble);
/*     */ 
/*     */ 
/*     */     
/* 243 */     double d1 = this.delta * paramDouble;
/* 244 */     boolean bool = (d1 < 0.0D) ? true : false;
/* 245 */     double d2 = 0.0D;
/* 246 */     double d3 = this.logSqrt2dv + Math.log(Math.abs(d1));
/* 247 */     double d4 = 0.5D * Math.log(1.0D + paramDouble * paramDouble / this.df);
/*     */     
/* 249 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 251 */       double d = Math.exp(b * d3 - this.HD2 + Maths.logGamma(0.5D * (this.df + b + 1.0D)) - Maths.logGamma((b + 1)) - (this.df + b + 1.0D) * d4);
/* 252 */       if (bool && b % 2 > 0) {
/* 253 */         d2 -= d;
/*     */       } else {
/* 255 */         d2 += d;
/* 256 */       }  if (Math.abs(d) < this.tolerance) return d2 / this.constant; 
/*     */     } 
/* 258 */     throw new RuntimeException("Cannot calculate pdf to required accuracy.");
/*     */   }
/*     */   
/*     */   public double random() {
/* 262 */     return (this.rand.nextGaussian() + this.delta) / Math.sqrt(this.chiSquared.random() / this.df);
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
/*     */   public void setParameters(double paramDouble1, double paramDouble2) {
/* 317 */     if (paramDouble1 <= 0.0D)
/* 318 */       throw new IllegalArgumentException("Invalid degrees of freedom."); 
/* 319 */     this.df = paramDouble1;
/* 320 */     this.delta = paramDouble2;
/* 321 */     if (paramDouble2 == 0.0D) {
/* 322 */       this.studentsT = new StudentsT(paramDouble1);
/*     */     } else {
/*     */       
/* 325 */       this.studentsT = null;
/* 326 */       this.HD2 = 0.5D * paramDouble2 * paramDouble2;
/* 327 */       this.pConst = 0.5D * Math.exp(-this.HD2);
/*     */       
/* 329 */       double d = 0.5D * paramDouble1;
/* 330 */       this.logGammaHalfV = Maths.logGamma(0.5D * paramDouble1);
/* 331 */       this.logSqrt2dv = Math.log(Math.sqrt(2.0D / paramDouble1));
/* 332 */       this.albeta = ALNRPI + this.logGammaHalfV - Maths.logGamma(0.5D * (paramDouble1 + 1.0D));
/* 333 */       this.constant = Math.exp(LGHALF + Maths.logGamma(0.5D * paramDouble1) + 0.5D * Math.log(paramDouble1));
/*     */     } 
/* 335 */     this.chiSquared = new ChiSquared(paramDouble1);
/* 336 */     this.chiSquared.setSeed(this.rand.nextLong());
/* 337 */     this.normalApprox = new Normal(paramDouble2, Math.sqrt(1.0D + paramDouble2 * paramDouble2 / (paramDouble1 + paramDouble1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 342 */     this.rand.setSeed(paramLong);
/* 343 */     this.chiSquared.setSeed(this.rand.nextLong() + 1L);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 347 */     return new String("Noncentral Student's t distribution: df = " + this.df + ", delta = " + this.delta + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 358 */     if (this.df <= 2.0D) return Double.NaN; 
/* 359 */     double d = mean();
/* 360 */     return (1.0D + this.delta * this.delta) * this.df / (this.df - 2.0D) - d * d;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 399 */       char c = '✐';
/*     */       
/* 401 */       NoncentralStudentsT noncentralStudentsT = new NoncentralStudentsT(100.0D, 10.0D);
/*     */       
/* 403 */       double[] arrayOfDouble = new double[c];
/* 404 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 406 */         arrayOfDouble[b] = noncentralStudentsT.random();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 411 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralStudentsT, H1.NOT_EQUAL, true);
/* 412 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/NoncentralStudentsT.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */