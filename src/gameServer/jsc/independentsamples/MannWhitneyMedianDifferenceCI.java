/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import jsc.ci.DistributionFreeCI;
/*     */ import jsc.distributions.MannWhitneyU;
/*     */ import jsc.distributions.Normal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MannWhitneyMedianDifferenceCI
/*     */   extends DistributionFreeCI
/*     */ {
/*     */   public static final int AUTO = 0;
/*     */   public static final int EXACT = 1;
/*     */   public static final int APPROX = 2;
/*     */   public static final int FAST_APPROX = 3;
/*     */   int method;
/*     */   static final int SMALL_SAMPLE_SIZE_PRODUCT = 400;
/*     */   static final int MEDIUM_SAMPLE_SIZE_PRODUCT = 10000;
/*     */   double[] xA;
/*     */   double[] xB;
/*     */   static final double TOL1 = 1.0E-16D;
/*     */   static final double TOL2 = 1.0E-16D;
/*     */   static final double TOL3 = 1.0E-15D;
/*     */   double dpoint;
/*     */   double x1;
/*     */   double fx1;
/*     */   double x2;
/*     */   double fx2;
/*     */   
/*     */   public MannWhitneyMedianDifferenceCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble, int paramInt) {
/* 114 */     super(paramDouble);
/*     */     
/* 116 */     int i = paramArrayOfdouble1.length;
/* 117 */     int j = paramArrayOfdouble2.length;
/*     */     
/* 119 */     if (paramInt == 0) {
/*     */       
/* 121 */       long l = (i * j);
/* 122 */       if (l <= 400L) { paramInt = 1; }
/* 123 */       else if (l <= 10000L) { paramInt = 2; }
/* 124 */       else { paramInt = 3; }
/*     */     
/* 126 */     }  this.method = paramInt;
/*     */     
/* 128 */     if (paramInt == 3) {
/* 129 */       rankCI(paramArrayOfdouble2, paramArrayOfdouble1, paramDouble);
/*     */     } else {
/*     */       double d;
/*     */       
/* 133 */       if (paramInt == 2) {
/*     */         
/* 135 */         double d1 = Normal.inverseStandardCdf(0.5D + 0.5D * paramDouble);
/*     */         
/* 137 */         this.d = (int)Math.round(0.5D * ((i * j) + 1.0D - d1 * Math.sqrt((i * j) * ((i + j) + 1.0D) / 3.0D)));
/* 138 */         Normal normal = MannWhitneyU.normalApproximation(i, j, 0);
/* 139 */         d = normal.cdf(this.d - 1.0D);
/*     */       }
/* 141 */       else if (paramInt == 1) {
/*     */         
/* 143 */         MannWhitneyU mannWhitneyU = new MannWhitneyU(i, j);
/*     */ 
/*     */ 
/*     */         
/* 147 */         d = 0.5D * (1.0D - paramDouble);
/* 148 */         long l = mannWhitneyU.getMinValue();
/*     */ 
/*     */         
/* 151 */         double d1 = mannWhitneyU.pdf(l);
/* 152 */         while (l < mannWhitneyU.getMaxValue() && d1 < d) { l++; d1 += mannWhitneyU.pdf(l); }
/*     */         
/* 154 */         l--;
/* 155 */         this.d = (int)(l + 1L);
/* 156 */         d = d1 - mannWhitneyU.pdf((l + 1L));
/*     */       } else {
/*     */         
/* 159 */         throw new IllegalArgumentException("Invalid method parameter.");
/*     */       } 
/*     */       
/* 162 */       this.achievedConfidence = 1.0D - 2.0D * d;
/* 163 */       computeInterval(2, this.d, paramArrayOfdouble1, paramArrayOfdouble2);
/* 164 */       this.xA = paramArrayOfdouble1; this.xB = paramArrayOfdouble2;
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
/*     */   public MannWhitneyMedianDifferenceCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
/* 180 */     this(paramArrayOfdouble1, paramArrayOfdouble2, paramDouble, 0);
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
/*     */   private void rankCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
/*     */     double d17;
/* 235 */     int i = paramArrayOfdouble1.length;
/* 236 */     int j = paramArrayOfdouble2.length;
/*     */ 
/*     */ 
/*     */     
/* 240 */     double[] arrayOfDouble1 = new double[1 + i];
/* 241 */     double[] arrayOfDouble2 = new double[1 + j];
/* 242 */     System.arraycopy(paramArrayOfdouble1, 0, arrayOfDouble1, 1, i);
/* 243 */     System.arraycopy(paramArrayOfdouble2, 0, arrayOfDouble2, 1, j);
/* 244 */     arrayOfDouble1[0] = Double.NEGATIVE_INFINITY;
/* 245 */     arrayOfDouble2[0] = Double.NEGATIVE_INFINITY;
/* 246 */     Arrays.sort(arrayOfDouble1);
/* 247 */     Arrays.sort(arrayOfDouble2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     this.dpoint = 0.0D;
/* 254 */     this.lowerLimit = 0.0D;
/* 255 */     this.upperLimit = 0.0D;
/* 256 */     boolean bool1 = false;
/* 257 */     double d1 = i;
/* 258 */     double d2 = j;
/*     */ 
/*     */ 
/*     */     
/* 262 */     if (i < 2 || j < 2) {
/* 263 */       throw new IllegalArgumentException("Too few data values.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     TrimmedMean trimmedMean1 = new TrimmedMean(this, arrayOfDouble1, i);
/* 271 */     double d3 = trimmedMean1.getMean();
/* 272 */     double d4 = trimmedMean1.getVariance();
/*     */     
/* 274 */     TrimmedMean trimmedMean2 = new TrimmedMean(this, arrayOfDouble2, j);
/* 275 */     double d5 = trimmedMean2.getMean();
/* 276 */     double d6 = trimmedMean2.getVariance();
/*     */     
/* 278 */     double d7 = d5 - d3;
/* 279 */     double d8 = d4 + d6;
/* 280 */     double d9 = Math.sqrt(d8);
/* 281 */     d9 = Math.max(d9, 1.0E-20D);
/* 282 */     double d10 = 2.0D;
/* 283 */     if (i < 10 || j < 10 || paramDouble > 0.96D) d10 = 3.0D; 
/* 284 */     double d11 = d7 - d10 * d9;
/* 285 */     double d12 = d7 + d10 * d9;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     double d13 = arrayOfDouble1[i] - arrayOfDouble1[1];
/* 296 */     double d14 = arrayOfDouble2[j] - arrayOfDouble2[1];
/* 297 */     double d15 = 1.0E-16D * Math.max(d13, d14);
/*     */     
/* 299 */     int n = i / 2;
/* 300 */     d13 = arrayOfDouble1[n];
/* 301 */     n = j / 2;
/* 302 */     d14 = arrayOfDouble2[n];
/*     */     
/* 304 */     double d16 = 1.0E-16D * Math.max(Math.max(d13, -d13), Math.max(d14, -d14));
/* 305 */     d15 = Math.max(d15, d16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     d16 = d9 * 1.0E-15D;
/* 314 */     d15 = Math.max(d15, d16);
/*     */     
/* 316 */     d15 = Math.max(d15, 1.0E-20D);
/*     */ 
/*     */     
/* 319 */     d16 = 10.0D * d15;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     boolean bool2 = true;
/*     */     
/* 327 */     double d18 = 0.0D;
/* 328 */     if (i * j % 2 == 0) {
/*     */       
/* 330 */       d17 = d1 * d2 / 2.0D - 0.5D;
/* 331 */       d18 = d17 + 1.0D;
/* 332 */       bool2 = false;
/*     */     } else {
/*     */       
/* 335 */       d17 = d1 * d2 / 2.0D;
/*     */     } 
/*     */     
/* 338 */     double d19 = (1.0D - paramDouble) / 2.0D;
/*     */ 
/*     */     
/* 341 */     double d20 = d1 * d2 / 2.0D;
/* 342 */     double d21 = d1 * d2 * (d1 + d2 + 1.0D) / 12.0D;
/* 343 */     double d22 = Math.sqrt(d21);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 350 */     double d23 = d22 * Normal.inverseStandardCdf(d19) + d20 - 0.5D;
/*     */ 
/*     */     
/* 353 */     int i1 = (int)d23;
/* 354 */     if (i1 < 0) i1 = 0; 
/* 355 */     d23 = i1;
/* 356 */     d13 = (d23 + 0.5D - d20) / d22;
/* 357 */     d19 = Normal.standardTailProb(d13, false);
/*     */     
/* 359 */     this.achievedConfidence = 1.0D - 2.0D * d19;
/*     */ 
/*     */     
/* 362 */     d23 += 0.5D;
/*     */     
/* 364 */     double d24 = d1 * d2 - d23;
/* 365 */     int k = (int)(d23 + 0.5D);
/* 366 */     int m = (int)(d24 + 0.5D);
/*     */ 
/*     */     
/* 369 */     this.d = k;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     double d25 = Math.abs(d13) * Math.sqrt(d1 * d2 * (d1 + d2)) / (d12 - d11) * Math.sqrt(3.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     brack(d11, d23, d25, arrayOfDouble1, i, arrayOfDouble2, j);
/* 383 */     this.lowerLimit = ill(d23, arrayOfDouble1, i, arrayOfDouble2, j, d16);
/*     */     
/* 385 */     brack(d12, d24, d25, arrayOfDouble1, i, arrayOfDouble2, j);
/* 386 */     this.upperLimit = ill(d24, arrayOfDouble1, i, arrayOfDouble2, j, d16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 392 */     if (this.upperLimit > this.lowerLimit + d16) d25 = (d12 - d11) / (this.upperLimit - this.lowerLimit) * d25;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 399 */     d7 = (this.lowerLimit + this.upperLimit) / 2.0D;
/* 400 */     brack(d7, d17, d25, arrayOfDouble1, i, arrayOfDouble2, j);
/* 401 */     this.dpoint = ill(d17, arrayOfDouble1, i, arrayOfDouble2, j, d15);
/*     */     
/* 403 */     if (bool2 == true) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 409 */     brack(this.dpoint, d18, d25, arrayOfDouble1, i, arrayOfDouble2, j);
/* 410 */     double d26 = ill(d18, arrayOfDouble1, i, arrayOfDouble2, j, d15);
/* 411 */     this.dpoint = (this.dpoint + d26) / 2.0D;
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
/*     */   private double ill(double paramDouble1, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2, double paramDouble2) {
/* 440 */     this.fx1 -= paramDouble1;
/* 441 */     this.fx2 -= paramDouble1;
/* 442 */     boolean bool = false;
/*     */     
/* 444 */     while (Math.abs(this.x2 - this.x1) >= paramDouble2) {
/*     */ 
/*     */ 
/*     */       
/* 448 */       double d1 = this.x2 - this.fx2 * (this.x2 - this.x1) / (this.fx2 - this.fx1);
/* 449 */       if (bool == true) d1 = (this.x1 + this.x2) / 2.0D; 
/* 450 */       bool = false;
/* 451 */       double d2 = fmann(d1, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
/* 452 */       d2 -= paramDouble1;
/*     */       
/* 454 */       if (d2 * this.fx2 <= 0.0D) {
/*     */         
/* 456 */         this.x1 = this.x2;
/* 457 */         this.fx1 = this.fx2;
/* 458 */         this.x2 = d1;
/* 459 */         this.fx2 = d2;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 464 */       this.x2 = d1;
/* 465 */       this.fx2 = d2;
/* 466 */       this.fx1 /= 2.0D;
/* 467 */       if (Math.abs(this.fx2) > Math.abs(this.fx1)) {
/*     */ 
/*     */         
/* 470 */         this.fx1 = 2.0D * this.fx1;
/* 471 */         bool = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 476 */     return (this.x1 + this.x2) / 2.0D;
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
/*     */   private void brack(double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2) {
/* 502 */     this.x1 = paramDouble1;
/* 503 */     this.fx1 = fmann(this.x1, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
/* 504 */     double d = 1.5D * (paramDouble2 - this.fx1) / paramDouble3;
/*     */     while (true) {
/* 506 */       this.x2 = this.x1 + d;
/* 507 */       this.fx2 = fmann(this.x2, paramArrayOfdouble1, paramInt1, paramArrayOfdouble2, paramInt2);
/* 508 */       if ((this.fx1 - paramDouble2) * (this.fx2 - paramDouble2) < 0.0D)
/* 509 */         return;  this.x1 = this.x2;
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
/*     */   
/*     */   private double fmann(double paramDouble, double[] paramArrayOfdouble1, int paramInt1, double[] paramArrayOfdouble2, int paramInt2) {
/* 545 */     double d = paramDouble;
/* 546 */     int i = paramInt1;
/* 547 */     int j = paramInt2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 552 */     int k = 0;
/* 553 */     int m = 0;
/*     */ 
/*     */ 
/*     */     
/* 557 */     for (byte b = 1; b <= i; b++) {
/*     */       
/* 559 */       double d1 = paramArrayOfdouble1[b] + d;
/*     */       
/* 561 */       while (d1 >= paramArrayOfdouble2[k + 1]) {
/*     */         
/* 563 */         k++;
/* 564 */         if (k >= j) {
/*     */ 
/*     */ 
/*     */           
/* 568 */           m += (i - b + 1) * j;
/* 569 */           return m;
/*     */         } 
/*     */       } 
/*     */       
/* 573 */       m += k;
/*     */     } 
/* 575 */     return m;
/*     */   }
/*     */ 
/*     */   
/*     */   class TrimmedMean
/*     */   {
/*     */     double zbar;
/*     */     
/*     */     double varzb;
/*     */     
/*     */     private final MannWhitneyMedianDifferenceCI this$0;
/*     */ 
/*     */     
/*     */     TrimmedMean(MannWhitneyMedianDifferenceCI this$0, double[] param1ArrayOfdouble, int param1Int) {
/* 589 */       this.this$0 = this$0;
/*     */       
/* 591 */       double d1 = 0.1D;
/* 592 */       double d2 = param1Int;
/*     */ 
/*     */ 
/*     */       
/* 596 */       int j = (int)(d1 * d2);
/* 597 */       int k = j + 1;
/* 598 */       int m = param1Int - j;
/*     */ 
/*     */       
/* 601 */       double d3 = 0.0D; int i;
/* 602 */       for (i = k; i <= m; ) { d3 += param1ArrayOfdouble[i]; i++; }
/* 603 */        double d4 = (param1Int - 2 * j);
/* 604 */       this.zbar = d3 / d4;
/*     */ 
/*     */       
/* 607 */       d3 = 0.0D;
/* 608 */       for (i = k; i <= m; ) { d3 += (param1ArrayOfdouble[i] - this.zbar) * (param1ArrayOfdouble[i] - this.zbar); i++; }
/*     */       
/* 610 */       if (j != 0) {
/*     */         
/* 612 */         double d = j;
/* 613 */         d3 += d * (param1ArrayOfdouble[k - 1] - this.zbar) * (param1ArrayOfdouble[k - 1] - this.zbar) + d * (param1ArrayOfdouble[m + 1] - this.zbar) * (param1ArrayOfdouble[m + 1] - this.zbar);
/*     */       } 
/*     */       
/* 616 */       this.varzb = d3 / d2 * d2;
/*     */     }
/* 618 */     double getMean() { return this.zbar; } double getVariance() {
/* 619 */       return this.varzb;
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
/*     */   public double getPointEstimate() {
/* 635 */     if (this.method == 3) {
/* 636 */       return this.dpoint;
/*     */     }
/* 638 */     return getPointEstimate(this.xA, this.xB);
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
/*     */   public static double getPointEstimate(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 653 */     double[] arrayOfDouble = DistributionFreeCI.differences(paramArrayOfdouble1, paramArrayOfdouble2);
/* 654 */     int i = arrayOfDouble.length;
/* 655 */     int j = i / 2;
/* 656 */     if (i % 2 == 0) {
/* 657 */       return (arrayOfDouble[j - 1] + arrayOfDouble[j]) / 2.0D;
/*     */     }
/* 659 */     return arrayOfDouble[j];
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 700 */       char c = 'È';
/* 701 */       int i = c + 1;
/*     */       
/* 703 */       double[] arrayOfDouble1 = new double[c];
/* 704 */       double[] arrayOfDouble2 = new double[i];
/* 705 */       Normal normal1 = new Normal(2.0D, 1.0D);
/* 706 */       Normal normal2 = new Normal(1.0D, 1.0D); byte b;
/* 707 */       for (b = 0; b < c; ) { arrayOfDouble1[b] = normal1.random(); b++; }
/* 708 */        for (b = 0; b < i; ) { arrayOfDouble2[b] = normal2.random(); b++; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 713 */       long l1 = System.currentTimeMillis();
/* 714 */       MannWhitneyMedianDifferenceCI mannWhitneyMedianDifferenceCI = new MannWhitneyMedianDifferenceCI(arrayOfDouble1, arrayOfDouble2, 0.9D, 3);
/* 715 */       long l2 = System.currentTimeMillis();
/* 716 */       System.out.println("n = " + c + " Time = " + ((l2 - l1) / 1000L) + " secs");
/* 717 */       System.out.println("CI=[" + mannWhitneyMedianDifferenceCI.getLowerLimit() + "," + mannWhitneyMedianDifferenceCI.getUpperLimit() + "]" + " d = " + mannWhitneyMedianDifferenceCI.getD() + " Point estimate = " + mannWhitneyMedianDifferenceCI.getPointEstimate() + " Achieved conf = " + mannWhitneyMedianDifferenceCI.getAchievedConfidence());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/MannWhitneyMedianDifferenceCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */