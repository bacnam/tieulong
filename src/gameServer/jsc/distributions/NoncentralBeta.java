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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoncentralBeta
/*     */   extends AbstractContinuousDistribution
/*     */ {
/*     */   static final int ITRMAX = 200;
/*     */   private double p;
/*     */   private double q;
/*     */   private double lambda;
/*     */   private double emhl;
/*     */   private double halfLambda;
/*     */   private double logHalfLambda;
/*     */   private double logP;
/*     */   private ChiSquared Cq;
/*     */   private NoncentralChiSquared Cp;
/*     */   private double beta;
/*     */   private double logGammaQ;
/*     */   private Beta centralBeta;
/*     */   
/*     */   public NoncentralBeta(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  51 */     super(0.0D, 1.0D, false);
/*     */ 
/*     */ 
/*     */     
/*  55 */     setParameters(paramDouble1, paramDouble2, paramDouble3);
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
/*     */   private double betanc(double paramDouble) {
/*  75 */     double d4, d1 = this.p, d3 = this.q;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     double d8 = Beta.incompleteBeta(paramDouble, d1, d3, this.beta);
/*  82 */     double d5 = Math.exp(d1 * Math.log(paramDouble) + d3 * Math.log(1.0D - paramDouble) - this.beta - this.logP);
/*     */     
/*  84 */     double d6 = this.emhl;
/*  85 */     double d9 = 0.0D;
/*  86 */     double d2 = d6 * d8;
/*  87 */     double d7 = 1.0D - d6;
/*  88 */     double d10 = d2;
/*     */ 
/*     */     
/*     */     do {
/*  92 */       d9++;
/*     */       
/*  94 */       d8 -= d5;
/*  95 */       d5 = paramDouble * (d1 + d3 + d9 - 1.0D) * d5 / (d1 + d9);
/*  96 */       d6 = d6 * this.halfLambda / d9;
/*  97 */       d7 -= d6;
/*  98 */       d2 = d8 * d6;
/*  99 */       d10 += d2;
/* 100 */       d4 = (d8 - d5) * d7;
/* 101 */     } while (d9 < 200.0D && d4 > this.tolerance);
/* 102 */     if (d4 > this.tolerance)
/* 103 */       throw new RuntimeException("Cannot calculate cdf to required accuracy."); 
/* 104 */     return d10;
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
/* 121 */     if (this.lambda == 0.0D) return this.centralBeta.cdf(paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     double d1 = this.p;
/* 127 */     double d2 = this.q;
/*     */     
/* 129 */     double d18 = 0.0D;
/*     */     
/* 131 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/* 132 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 133 */     if (paramDouble == 0.0D || paramDouble == 1.0D) return paramDouble;
/*     */     
/* 135 */     double d16 = this.halfLambda;
/* 136 */     byte b1 = 0;
/* 137 */     if (this.lambda < 54.0D) return betanc(paramDouble); 
/* 138 */     int i = (int)Maths.truncate(d16 + 0.5D);
/* 139 */     int m = (int)(i - 5.0D * Math.sqrt(i));
/* 140 */     int n = (int)(i + 5.0D * Math.sqrt(i));
/* 141 */     double d10 = -d16 + i * this.logHalfLambda - Maths.logGamma(i + 1.0D);
/* 142 */     double d8 = Math.exp(d10);
/* 143 */     double d9 = d8;
/* 144 */     double d15 = d8;
/*     */ 
/*     */     
/* 147 */     double d17 = Maths.lnB(d1 + i, d2);
/* 148 */     double d12 = (d1 + i) * Math.log(paramDouble) + d2 * Math.log(1.0D - paramDouble) - Math.log(d1 + i) - d17;
/* 149 */     double d4 = Math.exp(d12);
/* 150 */     double d3 = d4;
/* 151 */     double d5 = Beta.incompleteBeta(paramDouble, d1 + i, d2, d17);
/* 152 */     double d6 = d5;
/* 153 */     b1++;
/* 154 */     double d14 = d8 * d5;
/* 155 */     int j = i;
/*     */ 
/*     */ 
/*     */     
/* 159 */     while (j >= m && d8 >= this.tolerance) {
/*     */ 
/*     */ 
/*     */       
/* 163 */       d8 = d8 * j / d16;
/* 164 */       b1++;
/* 165 */       d4 = (d1 + j) / paramDouble * (d1 + d2 + j - 1.0D) * d4;
/* 166 */       j--;
/* 167 */       d5 += d4;
/* 168 */       d15 += d8;
/* 169 */       d14 += d8 * d5;
/*     */     } 
/*     */     
/* 172 */     double d13 = Maths.logGamma(d1 + d2) - Maths.logGamma(d1 + 1.0D) - Maths.logGamma(d2);
/* 173 */     double d11 = d1 * Math.log(paramDouble) + d2 * Math.log(1.0D - paramDouble);
/*     */     
/* 175 */     for (byte b2 = 1; b2 <= j; b2++) {
/*     */       
/* 177 */       int i1 = b2 - 1;
/* 178 */       d18 += Math.exp(d13 + d11 + i1 * Math.log(paramDouble));
/* 179 */       double d = Math.log(d1 + d2 + i1) - Math.log(d1 + 1.0D + i1) + d13;
/* 180 */       d13 = d;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (j <= 0) j = 1; 
/* 186 */     double d7 = (1.0D - Gamma.incompleteGamma(d16, j)) * (d5 + d18);
/* 187 */     d8 = d9;
/* 188 */     d5 = d6;
/* 189 */     d4 = d3;
/* 190 */     int k = i;
/*     */     while (true) {
/* 192 */       double d = d7 + (1.0D - d15) * d5;
/* 193 */       if (d < this.tolerance || k >= n) return d14; 
/* 194 */       k++;
/* 195 */       b1++;
/*     */       
/* 197 */       d8 = d8 * d16 / k;
/* 198 */       d15 += d8;
/* 199 */       d5 -= d4;
/* 200 */       d4 = paramDouble * (d1 + d2 + k - 1.0D) / (d1 + k) * d4;
/* 201 */       d14 += d8 * d5;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLambda() {
/* 210 */     return this.lambda;
/*     */   }
/*     */   public double getP() {
/* 213 */     return this.p;
/*     */   }
/*     */   public double getQ() {
/* 216 */     return this.q;
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
/* 230 */     if (this.lambda == 0.0D) {
/* 231 */       return this.centralBeta.inverseCdf(paramDouble);
/*     */     }
/* 233 */     return super.inverseCdf(paramDouble);
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
/* 245 */     if (this.lambda == 0.0D) return this.centralBeta.mean(); 
/* 246 */     double d = 0.0D;
/*     */     
/* 248 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 250 */       double d1 = Math.exp(-this.halfLambda + b * this.logHalfLambda - Maths.logGamma((b + 1)) + Math.log(this.p + b) - Math.log(this.p + b + this.q));
/* 251 */       d += d1;
/*     */       
/* 253 */       if (Math.abs(d1) < this.tolerance * d) return d; 
/*     */     } 
/* 255 */     throw new RuntimeException("Cannot calculate mean to required accuracy.");
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
/*     */   public Normal normalApproximation() {
/* 267 */     double d1 = this.halfLambda;
/* 268 */     double d2 = d1 * d1;
/* 269 */     double d3 = d1 * d2;
/* 270 */     double d4 = d1 * d3;
/* 271 */     double d5 = this.p + this.q;
/* 272 */     double d6 = d5 * d5;
/* 273 */     double d7 = this.q * this.q;
/* 274 */     double d8 = d5 + d1;
/* 275 */     double d9 = 1.0D - this.q / d8 * (1.0D + this.lambda / 2.0D * d8 * d8);
/* 276 */     double d10 = d8 * (d8 + 1.0D) + d1;
/* 277 */     double d11 = (d5 + d5 + 1.0D) * (d5 + d5 + 1.0D) + 1.0D;
/* 278 */     double d12 = 3.0D * d6 + 5.0D * d5 + 2.0D;
/* 279 */     double d13 = d6 * (d5 + 1.0D) + d12 * d1 + (3.0D * d5 + 4.0D) * d2 + d3;
/* 280 */     double d14 = (3.0D * d5 + 1.0D) * (9.0D * d5 + 17.0D) + 2.0D * d5 * (3.0D * d5 + 2.0D) * (3.0D * d5 + 4.0D) + 15.0D;
/* 281 */     double d15 = 54.0D * d6 + 162.0D * d5 + 130.0D;
/* 282 */     double d16 = 6.0D * (6.0D * d5 + 11.0D);
/* 283 */     double d17 = d1 * (d12 * d12 + 2.0D * d14 * d1 + d15 * d2 + d16 * d3 + 9.0D * d4);
/* 284 */     double d18 = d1 * d7 / d8 * d8 * d8 * d8;
/* 285 */     double d19 = this.q / d10 * (1.0D + d1 * (this.lambda * this.lambda + 3.0D * this.lambda + d11) / d10 * d10) - d7 / d13 * (1.0D + d17 / d13 * d13);
/* 286 */     return new Normal(d9, Math.sqrt(d18 + d19));
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
/* 300 */     if (this.lambda == 0.0D) return this.centralBeta.pdf(paramDouble); 
/* 301 */     if ((paramDouble > 0.0D && paramDouble < 1.0D) || (paramDouble == 0.0D && this.p >= 1.0D) || (paramDouble == 1.0D && this.q >= 1.0D)) {
/*     */       
/* 303 */       if (paramDouble == 0.0D)
/* 304 */         return (this.p == 1.0D) ? this.q : 0.0D; 
/* 305 */       if (paramDouble == 1.0D) {
/* 306 */         return (this.q == 1.0D) ? this.p : 0.0D;
/*     */       }
/*     */ 
/*     */       
/* 310 */       double d1 = 0.0D;
/*     */       
/* 312 */       double d2 = -this.halfLambda + (this.q - 1.0D) * Math.log(1.0D - paramDouble) - this.logGammaQ;
/* 313 */       double d3 = Math.log(paramDouble);
/* 314 */       for (byte b = 0; b < 'Ϩ'; b++) {
/*     */         
/* 316 */         double d = Math.exp(d2 + b * this.logHalfLambda - Maths.logGamma((b + 1)) + (this.p + b - 1.0D) * d3 + Maths.logGamma(this.p + this.q + b) - Maths.logGamma(this.p + b));
/*     */         
/* 318 */         d1 += d;
/*     */         
/* 320 */         if (Math.abs(d) < this.tolerance * d1) return d1; 
/*     */       } 
/* 322 */       throw new RuntimeException("Cannot calculate pdf to required accuracy.");
/*     */     } 
/*     */ 
/*     */     
/* 326 */     throw new IllegalArgumentException("Invalid variate-value.");
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/* 331 */     if (this.lambda == 0.0D) return this.centralBeta.random(); 
/* 332 */     double d = this.Cp.random();
/* 333 */     return d / (d + this.Cq.random());
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
/*     */   public void setParameters(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 368 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/* 369 */       throw new IllegalArgumentException("Invalid shape parameter."); 
/* 370 */     if (paramDouble3 < 0.0D)
/* 371 */       throw new IllegalArgumentException("Invalid noncentrality parameter."); 
/* 372 */     this.p = paramDouble1;
/* 373 */     this.q = paramDouble2;
/* 374 */     this.lambda = paramDouble3;
/*     */     
/* 376 */     setOpen((paramDouble1 < 1.0D || paramDouble2 < 1.0D));
/*     */     
/* 378 */     if (paramDouble3 == 0.0D) {
/* 379 */       this.centralBeta = new Beta(paramDouble1, paramDouble2);
/*     */     } else {
/*     */       
/* 382 */       this.centralBeta = null;
/* 383 */       this.halfLambda = 0.5D * paramDouble3;
/* 384 */       this.logHalfLambda = Math.log(this.halfLambda);
/* 385 */       this.Cp = new NoncentralChiSquared(paramDouble1 + paramDouble1, paramDouble3);
/* 386 */       this.Cq = new ChiSquared(paramDouble2 + paramDouble2);
/* 387 */       this.Cq.setSeed(this.rand.nextLong());
/*     */       
/* 389 */       this.logP = Math.log(paramDouble1);
/*     */       
/* 391 */       this.logGammaQ = Maths.logGamma(paramDouble2);
/* 392 */       this.beta = Maths.logGamma(paramDouble1) + this.logGammaQ - Maths.logGamma(paramDouble1 + paramDouble2);
/* 393 */       this.emhl = Math.exp(-this.halfLambda);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 399 */     this.rand.setSeed(paramLong);
/* 400 */     if (this.lambda == 0.0D) {
/* 401 */       this.centralBeta.setSeed(this.rand.nextLong());
/*     */     } else {
/*     */       
/* 404 */       this.Cp.setSeed(this.rand.nextLong());
/* 405 */       this.Cq.setSeed(this.rand.nextLong());
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
/*     */   public String toString() {
/* 440 */     return new String("Noncentral beta distribution: p = " + this.p + ", q = " + this.q + ", lambda = " + this.lambda + ".");
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
/* 451 */     if (this.lambda == 0.0D) return this.centralBeta.variance();
/*     */ 
/*     */ 
/*     */     
/* 455 */     double d1 = 0.0D;
/*     */     
/* 457 */     double d2 = mean();
/* 458 */     for (byte b = 0; b < 'Ϩ'; b++) {
/*     */       
/* 460 */       double d = Math.exp(-this.halfLambda + b * this.logHalfLambda - Maths.logGamma((b + 1)) + Math.log(this.p + b) + Math.log(this.p + b + 1.0D) - Math.log(this.p + b + this.q + 1.0D) - Math.log(this.p + b + this.q));
/*     */       
/* 462 */       d1 += d;
/*     */       
/* 464 */       if (Math.abs(d) < this.tolerance * d1) return d1 - d2 * d2; 
/*     */     } 
/* 466 */     throw new RuntimeException("Cannot calculate variance to required accuracy.");
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 552 */       char c = '✐';
/* 553 */       NoncentralBeta noncentralBeta = new NoncentralBeta(5.0D, 1.5D, 4.0D);
/* 554 */       noncentralBeta.setTolerance(1.0E-11D);
/* 555 */       double[] arrayOfDouble = new double[c];
/* 556 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 558 */         arrayOfDouble[b] = noncentralBeta.random();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 563 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, noncentralBeta, H1.NOT_EQUAL, true);
/* 564 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/NoncentralBeta.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */