/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.combinatorics.Enumerator;
/*     */ import jsc.combinatorics.MultiSetPermutations;
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.goodnessfit.SampleDistributionFunction;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
/*     */ import jsc.util.Arrays;
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
/*     */ public class SmirnovTest
/*     */   extends PermutableTwoSampleStatistic
/*     */   implements SignificanceTest
/*     */ {
/*     */   private static final double EPS = 1.0E-6D;
/*     */   private static final double SMALL = 4.9E-324D;
/*  42 */   private static final double SMALL_N = Math.log(Double.MIN_VALUE);
/*  43 */   private static final double ALN2 = Math.log(2.0D);
/*     */ 
/*     */   
/*     */   private static final double CHKNUM = 1.0E64D;
/*     */ 
/*     */   
/*     */   private static final int ITERUP = 1000;
/*     */   
/*     */   private static final int SMALL_SAMPLE_SIZE = 5000;
/*     */   
/*     */   private final H1 alternative;
/*     */   
/*     */   final int nA;
/*     */   
/*     */   final int nB;
/*     */   
/*     */   private double D;
/*     */   
/*     */   private long Dstar;
/*     */   
/*     */   private final double SP;
/*     */   
/*     */   private SampleDistributionFunction sdfA;
/*     */   
/*     */   private SampleDistributionFunction sdfB;
/*     */ 
/*     */   
/*     */   public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean) {
/*  71 */     super(paramArrayOfdouble1, paramArrayOfdouble2);
/*  72 */     this.alternative = paramH1;
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.sdfA = new SampleDistributionFunction(paramArrayOfdouble1);
/*  77 */     this.sdfB = new SampleDistributionFunction(paramArrayOfdouble2);
/*  78 */     this.nA = this.sdfA.getN();
/*  79 */     this.nB = this.sdfB.getN();
/*  80 */     this.permutedSampleA = new double[this.nA];
/*  81 */     this.permutedSampleB = new double[this.nB];
/*  82 */     this.originalSample = Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1);
/*  83 */     this.N = this.nA + this.nB;
/*  84 */     this.Dstar = calculateTestStatistic(this.sdfA, this.sdfB);
/*  85 */     this.D = this.Dstar / (this.nA * this.nB);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     if (this.D == 0.0D) {
/*  93 */       this.SP = 1.0D;
/*     */ 
/*     */     
/*     */     }
/*  97 */     else if (paramBoolean) {
/*  98 */       this.SP = approxSP(this.nA, this.nB, this.D, paramH1);
/*     */     } else {
/* 100 */       this.SP = exactSP(this.nA, this.nB, this.D, paramH1);
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
/*     */   public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
/* 115 */     this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, (paramArrayOfdouble1.length > 5000 && paramArrayOfdouble2.length > 5000));
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
/*     */   public SmirnovTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 127 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, (paramArrayOfdouble1.length > 5000 && paramArrayOfdouble2.length > 5000));
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
/*     */   public static double approxSP(int paramInt1, int paramInt2, double paramDouble, H1 paramH1) {
/* 176 */     if (paramInt1 < 1 || paramInt2 < 1)
/* 177 */       throw new IllegalArgumentException("Invalid sample sizes."); 
/* 178 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/* 179 */       throw new IllegalArgumentException("Invalid D value."); 
/* 180 */     if (paramH1 == H1.NOT_EQUAL)
/*     */     {
/*     */       
/* 183 */       return probks(paramInt1, paramInt2, paramDouble);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     double d1 = (paramInt1 + paramInt2);
/* 191 */     double d2 = (paramInt1 * paramInt2);
/*     */ 
/*     */ 
/*     */     
/* 195 */     return ChiSquared.upperTailProb(4.0D * paramDouble * paramDouble * d2 / d1, 2.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long calculateTestStatistic(SampleDistributionFunction paramSampleDistributionFunction1, SampleDistributionFunction paramSampleDistributionFunction2) {
/* 201 */     byte b1 = 0, b2 = 0;
/* 202 */     double d1 = 0.0D, d2 = 0.0D;
/* 203 */     double d3 = 0.0D;
/*     */     
/* 205 */     int i = paramSampleDistributionFunction1.getN();
/* 206 */     int j = paramSampleDistributionFunction2.getN();
/*     */     
/* 208 */     while (b1 < i && b2 < j) {
/*     */       
/* 210 */       double d6, d4 = paramSampleDistributionFunction1.getOrderedX(b1);
/* 211 */       double d5 = paramSampleDistributionFunction2.getOrderedX(b2);
/*     */       
/* 213 */       if (d4 <= d5) d1 = paramSampleDistributionFunction1.getOrderedS(b1++); 
/* 214 */       if (d5 <= d4) d2 = paramSampleDistributionFunction2.getOrderedS(b2++);
/*     */       
/* 216 */       if (this.alternative == H1.GREATER_THAN) {
/*     */ 
/*     */         
/* 219 */         d6 = d2 - d1;
/* 220 */       } else if (this.alternative == H1.LESS_THAN) {
/*     */ 
/*     */         
/* 223 */         d6 = d1 - d2;
/*     */       } else {
/*     */         
/* 226 */         d6 = Math.abs(d2 - d1);
/*     */       } 
/*     */       
/* 229 */       if (d6 > d3) d3 = d6;
/*     */     
/*     */     } 
/* 232 */     return Math.round((i * j) * d3);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double exactSP(int paramInt1, int paramInt2, double paramDouble, H1 paramH1) {
/* 337 */     if (paramInt1 < 1 || paramInt2 < 1)
/* 338 */       throw new IllegalArgumentException("Invalid sample sizes."); 
/* 339 */     if (paramDouble < 0.0D || paramDouble > 1.0D) {
/* 340 */       throw new IllegalArgumentException("Invalid D value.");
/*     */     }
/*     */     
/* 343 */     return gsmirn(paramInt1, paramInt2, paramDouble, paramH1, Arrays.fill(paramInt1 + paramInt2 + 1, 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumerator getEnumerator() {
/* 354 */     int[] arrayOfInt = new int[2];
/* 355 */     arrayOfInt[0] = this.nA; arrayOfInt[1] = this.nB;
/* 356 */     return (Enumerator)new MultiSetPermutations(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SampleDistributionFunction getSdfA() {
/* 364 */     return this.sdfA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SampleDistributionFunction getSdfB() {
/* 371 */     return this.sdfB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 380 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatistic() {
/* 390 */     return this.Dstar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 397 */     return this.D;
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
/*     */   private static double gsmirn(int paramInt1, int paramInt2, double paramDouble, H1 paramH1, int[] paramArrayOfint) {
/* 443 */     int m = 0, i1 = 0, i2 = 0, i3 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 450 */     int i4 = paramInt1 + paramInt2;
/*     */ 
/*     */     
/* 453 */     double[] arrayOfDouble = new double[paramInt1 + 3];
/* 454 */     int j = 0;
/* 455 */     byte b = 0;
/*     */     do {
/* 457 */       b++;
/* 458 */       if (paramArrayOfint[b] <= 0)
/* 459 */         throw new IllegalArgumentException("Invalid number of observations."); 
/* 460 */       j += paramArrayOfint[b];
/* 461 */       if (j > i4)
/* 462 */         throw new IllegalArgumentException("Invalid number of observations."); 
/* 463 */     } while (j < i4);
/*     */ 
/*     */     
/* 466 */     double d1 = 1.0D;
/* 467 */     double d3 = paramDouble - 1.0E-6D;
/* 468 */     if (d3 <= 0.0D) return d1; 
/* 469 */     arrayOfDouble[1] = 1.0D;
/*     */ 
/*     */     
/* 472 */     double d2 = paramInt1 / i4;
/* 473 */     double d4 = d2 * d3 * paramInt2;
/*     */     
/* 475 */     boolean bool = true;
/* 476 */     j = 1;
/* 477 */     int i6 = paramArrayOfint[1];
/* 478 */     int k = 0;
/* 479 */     int n = 0;
/*     */ 
/*     */     
/* 482 */     int i = 1000;
/* 483 */     int i5 = 0;
/* 484 */     double d6 = 1.0D;
/*     */     
/* 486 */     for (b = 1; b <= i4 - 1; b++) {
/*     */       
/* 488 */       if (i6 == 1) {
/*     */ 
/*     */         
/* 491 */         double d = b * d2;
/*     */ 
/*     */         
/* 494 */         n = Math.min((int)(d + d4), Math.min(b, paramInt1));
/* 495 */         k = Math.max((int)(d - d4 + 1.0D), Math.max(b - paramInt2, 0));
/* 496 */         j++;
/* 497 */         i6 = paramArrayOfint[j];
/* 498 */         bool = true;
/*     */       }
/*     */       else {
/*     */         
/* 502 */         i6--;
/*     */ 
/*     */ 
/*     */         
/* 506 */         if (bool) {
/*     */           
/* 508 */           bool = false;
/* 509 */           int i12 = b + i6;
/*     */ 
/*     */ 
/*     */           
/* 513 */           double d = i12 * d2;
/*     */ 
/*     */ 
/*     */           
/* 517 */           int i11 = Math.min((int)(d + d4), Math.min(i12, paramInt1));
/* 518 */           int i10 = Math.max((int)(d - d4 + 1.0D), Math.max(i12 - paramInt2, 0));
/*     */ 
/*     */ 
/*     */           
/* 522 */           m = k;
/* 523 */           i1 = i11;
/* 524 */           i3 = i12 - i10;
/* 525 */           i2 = b - n - 1;
/*     */         } 
/*     */ 
/*     */         
/* 529 */         k = Math.max(m, b - i3);
/* 530 */         n = Math.min(i1, b - i2);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 535 */       if (paramH1 == H1.GREATER_THAN) { n = Math.min(paramInt1, b); }
/* 536 */       else if (paramH1 == H1.LESS_THAN) { k = Math.max(0, b - paramInt2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 542 */       int i8 = Math.max(1, k);
/* 543 */       int i9 = Math.min(b - 1, n);
/*     */       int i7;
/* 545 */       for (i7 = i9; i7 >= i8; i7--) {
/* 546 */         arrayOfDouble[i7 + 1] = arrayOfDouble[i7 + 1] + arrayOfDouble[i7];
/*     */       }
/*     */       
/* 549 */       i--;
/* 550 */       if (i <= 0) {
/*     */         
/* 552 */         double d = 0.0D;
/* 553 */         for (i7 = i8 + 1; i7 <= i9 + 1; i7++)
/* 554 */           d = Math.max(arrayOfDouble[i7], d); 
/* 555 */         if (d == 0.0D) return d1; 
/* 556 */         if (d > 1.0E64D) {
/*     */           
/* 558 */           for (i7 = i8 + 1; i7 <= i9 + 1; i7++)
/* 559 */             arrayOfDouble[i7] = arrayOfDouble[i7] * Double.MIN_VALUE; 
/* 560 */           i = 1000;
/* 561 */           i5++;
/* 562 */           d6 *= Double.MIN_VALUE;
/*     */         }
/*     */         else {
/*     */           
/* 566 */           i = (int)((-SMALL_N - Math.log(d)) / ALN2);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 572 */       if (k == 0) {
/* 573 */         arrayOfDouble[i8] = d6;
/*     */       } else {
/* 575 */         arrayOfDouble[i8] = 0.0D;
/*     */       } 
/* 577 */       if (n == b) {
/* 578 */         arrayOfDouble[i9 + 2] = d6;
/*     */       } else {
/* 580 */         arrayOfDouble[i9 + 2] = 0.0D;
/*     */       } 
/*     */     } 
/* 583 */     double d5 = arrayOfDouble[paramInt1 + 1] + arrayOfDouble[paramInt1];
/* 584 */     if (d5 == 0.0D) return d1;
/*     */ 
/*     */ 
/*     */     
/* 588 */     d1 = 1.0D - Math.exp(Maths.logFactorial(paramInt1) + Maths.logFactorial(paramInt2) + Math.log(d5) - i5 * SMALL_N - Maths.logFactorial(i4));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 593 */     if (d1 < 0.0D)
/* 594 */       throw new IllegalArgumentException("Invalid SP " + d1); 
/* 595 */     return d1;
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
/*     */   private static double probks(int paramInt1, int paramInt2, double paramDouble) {
/* 612 */     double d2 = 0.0D;
/* 613 */     double d3 = 2.0D;
/*     */     
/* 615 */     double d4 = 0.0D;
/* 616 */     double d5 = paramDouble * Math.sqrt((paramInt1 * paramInt2 / (paramInt1 + paramInt2)));
/*     */     
/* 618 */     double d1 = -2.0D * d5 * d5;
/* 619 */     for (byte b = 1; b <= 'Ϩ'; b++) {
/*     */       
/* 621 */       double d = d3 * Math.exp(d1 * b * b);
/* 622 */       d2 += d;
/* 623 */       if (Math.abs(d) <= 0.001D * d4 || Math.abs(d) < 1.0E-8D * d2) {
/*     */         
/* 625 */         if (d2 > 1.0D) { d2 = 1.0D; }
/* 626 */         else if (d2 < 0.0D) { d2 = 0.0D; }
/* 627 */          return d2;
/*     */       } 
/* 629 */       d3 = -d3;
/* 630 */       d4 = Math.abs(d);
/*     */     } 
/*     */     
/* 633 */     throw new RuntimeException("Cannot calculate approximate SP");
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
/*     */   public double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 650 */     SampleDistributionFunction sampleDistributionFunction1 = new SampleDistributionFunction(paramArrayOfdouble1);
/* 651 */     SampleDistributionFunction sampleDistributionFunction2 = new SampleDistributionFunction(paramArrayOfdouble2);
/* 652 */     return calculateTestStatistic(sampleDistributionFunction1, sampleDistributionFunction2);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 724 */       double d2 = 0.0D;
/* 725 */       H1 h1 = H1.NOT_EQUAL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 731 */       byte b1 = 11;
/* 732 */       byte b2 = 11;
/* 733 */       double[] arrayOfDouble1 = new double[b1];
/* 734 */       double[] arrayOfDouble2 = new double[b2];
/* 735 */       Normal normal1 = new Normal(0.0D, 1.0D);
/* 736 */       normal1.setSeed(100L);
/* 737 */       Normal normal2 = new Normal(1.0D, 1.0D);
/* 738 */       normal2.setSeed(200L);
/*     */       byte b3;
/* 740 */       for (b3 = 0; b3 < b1; ) { arrayOfDouble1[b3] = normal1.random(); b3++; }
/* 741 */        for (b3 = 0; b3 < b2; ) { arrayOfDouble2[b3] = normal2.random(); b3++; }
/* 742 */        Tail tail = Tail.TWO;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 747 */       b1 = 25; b2 = 25; d2 = 300.0D;
/* 748 */       double d1 = d2 / (b1 * b2);
/*     */       
/* 750 */       b1 = 100; b2 = 50; d1 = 0.36D;
/* 751 */       System.out.println("D* = " + d2 + " D = " + d1);
/* 752 */       long l1 = System.currentTimeMillis();
/* 753 */       System.out.println("Approx SP = " + SmirnovTest.approxSP(b1, b2, d1, h1));
/*     */       
/* 755 */       long l2 = System.currentTimeMillis();
/* 756 */       System.out.println("Time = " + (l2 - l1) + " millisecs");
/* 757 */       l1 = System.currentTimeMillis();
/* 758 */       System.out.println(" Exact SP = " + SmirnovTest.exactSP(b1, b2, d1, h1));
/*     */       
/* 760 */       l2 = System.currentTimeMillis();
/* 761 */       System.out.println("Time = " + (l2 - l1) + " millisecs");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/SmirnovTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */