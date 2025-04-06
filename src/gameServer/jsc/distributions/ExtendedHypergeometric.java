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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedHypergeometric
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   private int n1;
/*     */   private int n2;
/*     */   private int m;
/*     */   private int mode;
/*     */   private int ll;
/*     */   private int uu;
/*     */   private double mean;
/*     */   private double variance;
/*     */   private double psi;
/*     */   private int lStar;
/*     */   private int uStar;
/*     */   private double[] prob;
/*     */   
/*     */   public ExtendedHypergeometric(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2) {
/*  90 */     super(Math.max(0, paramInt3 - paramInt2), Math.min(paramInt1, paramInt3));
/*  91 */     setParameters(paramInt1, paramInt2, paramInt3, paramDouble1, paramDouble2);
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
/*     */   public ExtendedHypergeometric(int paramInt1, int paramInt2, int paramInt3, double paramDouble) {
/* 105 */     this(paramInt1, paramInt2, paramInt3, paramDouble, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/* 116 */     if (paramDouble < this.ll || paramDouble > this.uu)
/* 117 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 118 */     if (paramDouble < this.lStar) return 0.0D; 
/* 119 */     if (paramDouble >= this.uStar) return 1.0D; 
/* 120 */     double d = 0.0D;
/* 121 */     long l = this.lStar;
/* 122 */     while (l <= paramDouble) {
/*     */       
/* 124 */       d += this.prob[(int)(l - this.lStar)];
/* 125 */       l++;
/*     */     } 
/*     */     
/* 128 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectiveMaxX() {
/* 138 */     return this.uStar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectiveMinX() {
/* 147 */     return this.lStar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumPdf() {
/* 156 */     double d = 0.0D;
/* 157 */     for (byte b = 0; b < this.prob.length; b++) {
/*     */       
/* 159 */       if (this.prob[b] > d) d = this.prob[b]; 
/*     */     } 
/* 161 */     return d;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 188 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
/* 189 */     if (paramDouble == 0.0D) return Math.max(this.lStar - 1, this.ll); 
/* 190 */     if (paramDouble == 1.0D) return this.uStar; 
/* 191 */     long l = this.lStar;
/*     */ 
/*     */ 
/*     */     
/* 195 */     double d = this.prob[0];
/* 196 */     while (l < this.uStar && d < paramDouble) { l++; d += this.prob[(int)(l - this.lStar)]; }
/* 197 */      return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 207 */     return this.mean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int mode() {
/* 214 */     return this.mode;
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
/* 225 */     if (paramDouble < this.ll || paramDouble > this.uu)
/* 226 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 227 */     if (paramDouble < this.lStar || paramDouble > this.uStar) return 0.0D; 
/* 228 */     return this.prob[(int)paramDouble - this.lStar];
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/* 233 */     double d1 = 0.0D;
/* 234 */     double d2 = this.rand.nextDouble();
/* 235 */     long l = this.lStar;
/* 236 */     for (byte b = 0; b < this.prob.length; b++) {
/*     */       
/* 238 */       d1 += this.prob[b];
/* 239 */       if (d2 < d1) return l; 
/* 240 */       l++;
/*     */     } 
/* 242 */     return this.uStar;
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
/*     */   private double rFunction(int paramInt) {
/* 304 */     double d = paramInt;
/* 305 */     return (this.n1 - d + 1.0D) * (this.m - d + 1.0D) / d / ((this.n2 - this.m) + d) * this.psi;
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
/*     */   public void setParameters(int paramInt1, int paramInt2, int paramInt3, double paramDouble1, double paramDouble2) {
/* 322 */     this.ll = Math.max(0, paramInt3 - paramInt2);
/* 323 */     this.uu = Math.min(paramInt1, paramInt3);
/* 324 */     int i = paramInt1 + paramInt2;
/* 325 */     if (paramInt1 < 0 || paramInt2 < 0 || paramInt3 < 0 || paramInt3 > i || paramDouble1 <= 0.0D)
/* 326 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 327 */     this.minValue = this.ll;
/* 328 */     this.maxValue = this.uu;
/* 329 */     this.n1 = paramInt1;
/* 330 */     this.n2 = paramInt2;
/* 331 */     this.m = paramInt3;
/* 332 */     this.psi = paramDouble1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     double d1 = paramDouble1 - 1.0D;
/* 341 */     double d2 = -((paramInt3 + paramInt1 + 2) * paramDouble1 + paramInt2 - paramInt3);
/* 342 */     double d3 = paramDouble1 * (paramInt1 + 1) * (paramInt3 + 1);
/* 343 */     double d4 = d2 + ((d2 < 0.0D) ? -1 : true) * Math.sqrt(d2 * d2 - 4.0D * d1 * d3);
/* 344 */     d4 = -d4 / 2.0D;
/* 345 */     this.mode = (int)Maths.truncate(d3 / d4);
/* 346 */     if (this.uu < this.mode || this.mode < this.ll) {
/* 347 */       this.mode = (int)Maths.truncate(d4 / d1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     double[] arrayOfDouble = new double[this.uu + 2];
/*     */ 
/*     */ 
/*     */     
/* 358 */     arrayOfDouble[this.mode] = 1.0D;
/* 359 */     double d5 = 0.1D * paramDouble2; int j;
/* 360 */     for (j = this.mode + 1; j <= this.uu; j++) {
/*     */       
/* 362 */       double d = rFunction(j);
/* 363 */       arrayOfDouble[j] = arrayOfDouble[j - 1] * d;
/* 364 */       if (arrayOfDouble[j] <= d5 && d < 0.8333333333333333D)
/*     */         break; 
/* 366 */     }  this.uStar = Math.min(j, this.uu);
/* 367 */     for (j = this.mode - 1; j >= this.ll; j--) {
/*     */       
/* 369 */       double d = rFunction(j + 1);
/* 370 */       arrayOfDouble[j] = arrayOfDouble[j + 1] / d;
/* 371 */       if (arrayOfDouble[j] <= d5 && d > 1.2D)
/*     */         break; 
/* 373 */     }  this.lStar = Math.max(this.ll, j);
/*     */     
/* 375 */     int m = this.uStar - this.lStar + 1;
/* 376 */     this.prob = new double[m]; int k;
/* 377 */     for (j = this.lStar, k = 0; j <= this.uStar; ) { this.prob[k] = arrayOfDouble[j]; j++; k++; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 384 */     double d6 = 0.0D;
/*     */     
/* 386 */     for (j = 0; j < m; ) { d6 += this.prob[j]; j++; }
/* 387 */      if (d6 <= 0.0D) {
/* 388 */       throw new IllegalArgumentException("Zero probabilities.");
/*     */     }
/* 390 */     for (j = 0; j < m; ) { this.prob[j] = this.prob[j] / d6; j++; }
/*     */ 
/*     */     
/* 393 */     this.mean = 0.0D; this.variance = 0.0D;
/* 394 */     for (j = 0, k = this.lStar; k <= this.uStar; ) { this.mean += k * this.prob[j]; j++; k++; }
/* 395 */      for (j = 0, k = this.lStar; k <= this.uStar; ) { this.variance += (k - this.mean) * (k - this.mean) * this.prob[j]; j++; k++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 402 */     return new String("Extended hypergeometric distribution: n1 = " + this.n1 + ", n2 = " + this.n2 + ", m = " + this.m + ", psi = " + this.psi + ".");
/*     */   } public double variance() {
/* 404 */     return this.variance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 415 */       char c1 = 'Ǵ';
/* 416 */       char c2 = c1;
/* 417 */       char c3 = c1;
/* 418 */       char c4 = c1;
/*     */       
/* 420 */       double d1 = 6.0D;
/* 421 */       double d2 = 1.0E-14D;
/*     */ 
/*     */       
/* 424 */       ExtendedHypergeometric extendedHypergeometric = new ExtendedHypergeometric(500, 500, 500, 6.0D, 1.0E-14D);
/* 425 */       System.out.println(extendedHypergeometric.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 441 */       int i = 1000000;
/*     */       
/* 443 */       int[] arrayOfInt1 = new int[i];
/*     */       
/* 445 */       int[] arrayOfInt2 = { 50, 500, 5000, 50, 500, 5000 };
/* 446 */       double[] arrayOfDouble = { 1.5D, 1.5D, 1.5D, 6.0D, 6.0D, 6.0D };
/*     */       
/* 448 */       System.out.println("tolerance = " + d2); byte b;
/* 449 */       for (b = 0; b < arrayOfInt2.length; b++) {
/*     */         
/* 451 */         extendedHypergeometric.setParameters(arrayOfInt2[b], arrayOfInt2[b], arrayOfInt2[b], arrayOfDouble[b], d2);
/* 452 */         double d5 = 0.0D, d6 = 0.0D;
/*     */         
/* 454 */         long l1 = System.currentTimeMillis(); byte b1;
/* 455 */         for (b1 = 0; b1 < i; ) { arrayOfInt1[b1] = (int)extendedHypergeometric.random(); b1++; }
/* 456 */          long l2 = System.currentTimeMillis();
/* 457 */         for (b1 = 0; b1 < i; ) { d5 += arrayOfInt1[b1]; b1++; }
/* 458 */          double d3 = Maths.round(d5 / i, 4);
/* 459 */         for (b1 = 0; b1 < i; ) { d6 += (arrayOfInt1[b1] - d3) * (arrayOfInt1[b1] - d3); b1++; }
/* 460 */          double d4 = Maths.round(d6 / i, 4);
/* 461 */         System.out.println("m=" + arrayOfInt2[b] + " psi=" + arrayOfDouble[b] + " l=" + extendedHypergeometric.getEffectiveMinX() + " mode=" + extendedHypergeometric.mode() + " u=" + extendedHypergeometric.getEffectiveMaxX() + " " + ((l2 - l1) / 1000L) + " secs" + " mean=" + d3 + " var=" + d4);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 482 */       i = 10000;
/*     */ 
/*     */ 
/*     */       
/* 486 */       extendedHypergeometric = new ExtendedHypergeometric(50, 50, 90, 6.0D, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 493 */       int[] arrayOfInt3 = new int[i];
/* 494 */       for (b = 0; b < i; ) { arrayOfInt3[b] = (int)extendedHypergeometric.random(); b++; }
/*     */       
/* 496 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt3), extendedHypergeometric, 0);
/* 497 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 498 */       System.out.println("n = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/ExtendedHypergeometric.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */