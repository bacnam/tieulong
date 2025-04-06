/*     */ package jsc.onesample;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.Poisson;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
/*     */ import jsc.util.Rank;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WilcoxonTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   static final int SMALL_SAMPLE_SIZE = 499;
/*  31 */   static final double LOG2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final H1 alternative;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int n1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final double T;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double s2Sum;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final double SP;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double[] signedRanks;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble1, H1 paramH1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2) {
/*  67 */     this.alternative = paramH1;
/*  68 */     int i = paramArrayOfdouble.length;
/*     */ 
/*     */     
/*  71 */     double d1 = 0.0D;
/*  72 */     double d2 = 0.0D;
/*     */     
/*  74 */     this.n1 = 0;
/*  75 */     int[] arrayOfInt = new int[i];
/*  76 */     double[] arrayOfDouble = new double[i];
/*     */     
/*     */     byte b;
/*  79 */     for (b = 0; b < i; b++) {
/*     */       
/*  81 */       double d = paramArrayOfdouble[b] - paramDouble1;
/*  82 */       if (paramBoolean1 || Math.abs(d) > paramDouble2) {
/*  83 */         arrayOfDouble[this.n1] = Math.abs(d);
/*     */         
/*  85 */         arrayOfInt[this.n1] = (d >= 0.0D) ? 1 : -1;
/*  86 */         this.n1++;
/*     */       } 
/*  88 */     }  if (this.n1 < 1) {
/*  89 */       throw new IllegalArgumentException("No non-zero differences.");
/*     */     }
/*     */     
/*  92 */     Rank rank = new Rank(this.n1, arrayOfDouble, paramDouble2);
/*     */ 
/*     */     
/*  95 */     this.signedRanks = new double[this.n1];
/*     */ 
/*     */ 
/*     */     
/*  99 */     this.s2Sum = 0.0D;
/* 100 */     for (b = 0; b < this.n1; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 108 */       double d = rank.getRank(b);
/* 109 */       this.s2Sum += d * d;
/* 110 */       if (arrayOfInt[b] > 0) {
/*     */         
/* 112 */         d1 += d;
/* 113 */         this.signedRanks[b] = d;
/*     */       }
/*     */       else {
/*     */         
/* 117 */         d2 += d;
/* 118 */         this.signedRanks[b] = -d;
/*     */       } 
/*     */     } 
/* 121 */     if (paramH1 == H1.LESS_THAN) {
/* 122 */       this.T = d1;
/* 123 */     } else if (paramH1 == H1.GREATER_THAN) {
/* 124 */       this.T = d2;
/*     */     } else {
/* 126 */       this.T = Math.min(d1, d2);
/*     */     } 
/*     */     
/* 129 */     if (paramBoolean2) {
/* 130 */       this.SP = approxSP();
/*     */     } else {
/* 132 */       this.SP = exactSP();
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
/*     */   public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1, boolean paramBoolean) {
/* 148 */     this(paramArrayOfdouble, paramDouble, paramH1, 0.0D, paramBoolean, (paramArrayOfdouble.length > 499));
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
/*     */   public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
/* 163 */     this(paramArrayOfdouble, paramDouble, paramH1, 0.0D, false, (paramArrayOfdouble.length > 499));
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
/*     */   public WilcoxonTest(double[] paramArrayOfdouble, double paramDouble) {
/* 178 */     this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL, 0.0D, false, (paramArrayOfdouble.length > 499));
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
/*     */   public WilcoxonTest(PairedData paramPairedData, H1 paramH1, double paramDouble, boolean paramBoolean1, boolean paramBoolean2) {
/* 198 */     this(paramPairedData.differences(), 0.0D, paramH1, paramDouble, paramBoolean1, paramBoolean2);
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
/*     */   public WilcoxonTest(PairedData paramPairedData, H1 paramH1, boolean paramBoolean) {
/* 213 */     this(paramPairedData.differences(), 0.0D, paramH1, 0.0D, paramBoolean, (paramPairedData.getN() > 24));
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
/*     */   public WilcoxonTest(PairedData paramPairedData, H1 paramH1) {
/* 228 */     this(paramPairedData.differences(), 0.0D, paramH1, 0.0D, false, (paramPairedData.getN() > 24));
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
/*     */   public WilcoxonTest(PairedData paramPairedData) {
/* 243 */     this(paramPairedData.differences(), 0.0D, H1.NOT_EQUAL, 0.0D, false, (paramPairedData.getN() > 24));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double approxSP() {
/* 253 */     double d = getZ();
/*     */     
/* 255 */     if (this.alternative == H1.NOT_EQUAL) {
/* 256 */       return 2.0D * Normal.standardTailProb(d, (d > 0.0D));
/*     */     }
/* 258 */     return Normal.standardTailProb(d, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double exactSP() {
/* 269 */     FisherSymmetryTest fisherSymmetryTest = new FisherSymmetryTest(this.signedRanks, this.alternative);
/* 270 */     return fisherSymmetryTest.getSP();
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
/*     */   public int getN() {
/* 315 */     return this.n1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getSignedRanks() {
/* 324 */     return this.signedRanks;
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
/*     */   public double getSP() {
/* 341 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 348 */     return this.T;
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
/*     */   public double getZ() {
/* 360 */     double d = this.T - 0.25D * this.n1 * (this.n1 + 1.0D);
/* 361 */     return (d - ((d < 0.0D) ? -0.5D : 0.5D)) / Math.sqrt(0.25D * this.s2Sum);
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
/* 403 */       char c = 'Ǵ';
/* 404 */       double[] arrayOfDouble1 = new double[c];
/* 405 */       double[] arrayOfDouble2 = new double[c];
/*     */       
/* 407 */       Poisson poisson1 = new Poisson(1.0D);
/* 408 */       poisson1.setSeed(100L);
/*     */       
/* 410 */       Poisson poisson2 = new Poisson(1.5D);
/* 411 */       poisson2.setSeed(200L); byte b;
/* 412 */       for (b = 0; b < c; ) { arrayOfDouble1[b] = poisson1.random(); b++; }
/* 413 */        for (b = 0; b < c; ) { arrayOfDouble2[b] = poisson2.random(); b++; }
/* 414 */        PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
/* 415 */       WilcoxonTest wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
/* 416 */       System.out.println("T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP() + " Approx SP=" + wilcoxonTest.approxSP());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 421 */       double[] arrayOfDouble3 = { -2.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 4.0D, 4.0D, 1.0D, 1.0D, 5.0D, 3.0D, 5.0D, 3.0D, -1.0D, 1.0D, -1.0D, 5.0D, 8.0D, 2.0D, 2.0D, 2.0D, -3.0D, -2.0D, 1.0D, 4.0D, 8.0D, 2.0D, 3.0D, -1.0D };
/*     */       
/* 423 */       double d1 = 0.0D;
/* 424 */       double d2 = 0.0D;
/* 425 */       boolean bool1 = false;
/* 426 */       boolean bool2 = false;
/* 427 */       System.out.println("Normal approx: " + bool1);
/* 428 */       System.out.println("Include zeros: " + bool2);
/* 429 */       wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.NOT_EQUAL, d2, bool2, bool1);
/* 430 */       System.out.println("H1:m <> " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());
/* 431 */       wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.LESS_THAN, d2, bool2, bool1);
/* 432 */       System.out.println("H1:m < " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());
/* 433 */       wilcoxonTest = new WilcoxonTest(arrayOfDouble3, d1, H1.GREATER_THAN, d2, bool2, bool1);
/* 434 */       System.out.println("H1:m > " + d1 + " N=" + wilcoxonTest.getN() + " T=" + wilcoxonTest.getTestStatistic() + " SP=" + wilcoxonTest.getSP());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 450 */       double[] arrayOfDouble4 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
/* 451 */       double[] arrayOfDouble5 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
/* 452 */       pairedData = new PairedData(arrayOfDouble4, arrayOfDouble5);
/* 453 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
/* 454 */       System.out.println("H1: averages not equal: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
/* 455 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.LESS_THAN);
/* 456 */       System.out.println("H1: average A < average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
/* 457 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
/* 458 */       System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
/*     */ 
/*     */       
/* 461 */       double[] arrayOfDouble6 = { 17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D };
/* 462 */       double[] arrayOfDouble7 = { 13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D };
/* 463 */       pairedData = new PairedData(arrayOfDouble6, arrayOfDouble7);
/* 464 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
/* 465 */       System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
/*     */ 
/*     */       
/* 468 */       double[] arrayOfDouble8 = { 20.1D, 19.5D, 19.0D, 21.1D, 23.1D, 22.6D, 18.9D, 22.8D, 27.1D, 19.8D, 21.7D, 18.9D, 20.4D };
/* 469 */       double[] arrayOfDouble9 = { 21.2D, 18.7D, 19.0D, 20.8D, 19.9D, 21.4D, 17.9D, 23.1D, 24.3D, 18.5D, 20.3D, 18.7D, 19.4D };
/* 470 */       pairedData = new PairedData(arrayOfDouble8, arrayOfDouble9);
/* 471 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.GREATER_THAN);
/* 472 */       System.out.println("H1: average A > average B: T = " + wilcoxonTest.getTestStatistic() + " SP = " + wilcoxonTest.getSP());
/*     */ 
/*     */       
/* 475 */       double[] arrayOfDouble10 = { 82.0D, 69.0D, 73.0D, 43.0D, 58.0D, 56.0D, 76.0D, 65.0D };
/*     */       
/* 477 */       double[] arrayOfDouble11 = { 63.0D, 42.0D, 74.0D, 37.0D, 51.0D, 43.0D, 80.0D, 62.0D };
/* 478 */       pairedData = new PairedData(arrayOfDouble10, arrayOfDouble11);
/* 479 */       wilcoxonTest = new WilcoxonTest(pairedData, H1.NOT_EQUAL, 0.0D, false, false);
/* 480 */       System.out.println("H1:A <> B: T=" + wilcoxonTest.getTestStatistic() + " z=" + wilcoxonTest.getZ() + " SP=" + wilcoxonTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/WilcoxonTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */