/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.combinatorics.Enumerator;
/*     */ import jsc.combinatorics.MultiSetPermutations;
/*     */ import jsc.combinatorics.Selection;
/*     */ import jsc.distributions.MannWhitneyU;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.statistics.PermutableStatistic;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.PermutationTest;
/*     */ import jsc.tests.SignificanceTest;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Maths;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MannWhitneyTest
/*     */   implements PermutableStatistic, SignificanceTest
/*     */ {
/*     */   static final int SMALL_SAMPLE_SIZE = 20;
/*     */   final int N;
/*     */   final int nAB;
/*     */   final H1 alternative;
/*     */   final int nA;
/*     */   final int nB;
/*     */   double RA;
/*     */   double RB;
/*     */   final Rank ranks;
/*     */   final double U;
/*     */   final double SP;
/*     */   final double tolerance;
/*     */   private final double[] originalRanks;
/*     */   private double[] permutedRanksA;
/*     */   
/*     */   public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, double paramDouble, boolean paramBoolean) {
/*  80 */     this.alternative = paramH1;
/*  81 */     this.tolerance = paramDouble;
/*  82 */     this.nA = paramArrayOfdouble1.length;
/*  83 */     this.nB = paramArrayOfdouble2.length;
/*  84 */     if (this.nA < 2 || this.nB < 2)
/*  85 */       throw new IllegalArgumentException("Less than two data values."); 
/*  86 */     this.nAB = this.nA * this.nB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     double[] arrayOfDouble1 = new double[this.nA];
/*  94 */     double[] arrayOfDouble2 = new double[this.nB];
/*  95 */     this.permutedRanksA = new double[this.nA];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.N = this.nA + this.nB;
/*     */ 
/*     */ 
/*     */     
/* 104 */     this.ranks = new Rank(Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1), paramDouble);
/*     */     
/* 106 */     this.originalRanks = this.ranks.getRanks();
/*     */ 
/*     */     
/* 109 */     this.RA = 0.0D; int i;
/* 110 */     for (i = 0; i < this.nA; i++) {
/* 111 */       double d = this.ranks.getRank(i); this.RA += d; arrayOfDouble1[i] = d;
/*     */     } 
/*     */     
/* 114 */     this.RB = 0.0D;
/* 115 */     for (byte b = 0; i < this.N; b++, i++) {
/* 116 */       double d = this.ranks.getRank(i); this.RB += d; arrayOfDouble2[b] = d;
/*     */     } 
/* 118 */     double d1 = this.RA - 0.5D * this.nA * (this.nA + 1.0D);
/* 119 */     double d2 = (this.nA * this.nB) - d1;
/* 120 */     if (paramH1 == H1.NOT_EQUAL) {
/* 121 */       this.U = Math.min(d1, d2);
/* 122 */     } else if (paramH1 == H1.LESS_THAN) {
/* 123 */       this.U = d1;
/*     */     } else {
/* 125 */       this.U = d2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (paramBoolean) {
/* 131 */       this.SP = approxSP();
/*     */     } else {
/* 133 */       this.SP = exactSP();
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
/*     */   public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
/* 155 */     this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, 0.0D, (paramArrayOfdouble1.length > 20 || paramArrayOfdouble2.length > 20));
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
/*     */   public MannWhitneyTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 169 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, 0.0D, (paramArrayOfdouble1.length > 20 || paramArrayOfdouble2.length > 20));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double approxSP() {
/* 180 */     double d = getZ();
/* 181 */     if (this.alternative == H1.NOT_EQUAL) {
/* 182 */       return 2.0D * Normal.standardTailProb(d, (d > 0.0D));
/*     */     }
/* 184 */     return Normal.standardTailProb(d, false);
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
/*     */   public double exactSP() {
/* 201 */     if (this.ranks.hasTies()) {
/*     */       
/* 203 */       PermutationTest permutationTest = new PermutationTest(this, Tail.LOWER, (this.nA * this.nB > 169), 10000, 1.0E-5D);
/* 204 */       return permutationTest.getSP();
/*     */     } 
/*     */ 
/*     */     
/* 208 */     MannWhitneyU mannWhitneyU = new MannWhitneyU(this.nA, this.nB);
/* 209 */     double d = mannWhitneyU.cdf(Math.ceil(this.U));
/*     */ 
/*     */ 
/*     */     
/* 213 */     if (this.alternative == H1.NOT_EQUAL) {
/* 214 */       return (d <= 0.5D) ? (2.0D * d) : (2.0D * (1.0D - d));
/*     */     }
/* 216 */     return d;
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
/*     */   public int getCorrectionFactor() {
/* 242 */     return this.ranks.getCorrectionFactor1();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumerator getEnumerator() {
/* 252 */     int[] arrayOfInt = new int[2];
/* 253 */     arrayOfInt[0] = this.nA; arrayOfInt[1] = this.nB;
/* 254 */     return (Enumerator)new MultiSetPermutations(arrayOfInt);
/*     */   }
/*     */   public int getN() {
/* 257 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rank getRanks() {
/* 267 */     return this.ranks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRankSumA() {
/* 275 */     return this.RA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRankSumB() {
/* 283 */     return this.RB;
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
/*     */   public double getSP() {
/* 306 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatistic() {
/* 313 */     return this.U;
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
/*     */   public double getTestStatistic() {
/* 333 */     return this.U;
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
/* 345 */     int i = this.ranks.getCorrectionFactor1();
/* 346 */     double d1 = this.N;
/* 347 */     double d2 = (d1 * d1 * d1 - d1 - i) / 12.0D;
/*     */     
/* 349 */     double d3 = this.U - 0.5D * this.nAB;
/*     */     
/* 351 */     return (d3 - ((d3 < 0.0D) ? -0.5D : 0.5D)) / Math.sqrt(this.nAB / d1 * (d1 - 1.0D) * d2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double permuteStatistic(Selection paramSelection) {
/* 359 */     byte b1 = 0;
/*     */     
/* 361 */     int[] arrayOfInt = paramSelection.toIntArray();
/*     */     
/* 363 */     for (byte b2 = 0; b2 < this.N; b2++) {
/*     */       
/* 365 */       if (arrayOfInt[b2] == 1) {
/* 366 */         this.permutedRanksA[b1] = this.originalRanks[b2]; b1++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     return resampleStatistic(this.permutedRanksA);
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
/*     */   public double resampleStatistic(double[] paramArrayOfdouble) {
/* 408 */     double d1, d2 = 0.0D;
/* 409 */     for (byte b = 0; b < this.nA; ) { d2 += paramArrayOfdouble[b]; b++; }
/*     */     
/* 411 */     double d3 = d2 - 0.5D * this.nA * (this.nA + 1.0D);
/* 412 */     double d4 = (this.nA * this.nB) - d3;
/*     */     
/* 414 */     if (this.alternative == H1.NOT_EQUAL) {
/* 415 */       d1 = Math.min(d3, d4);
/* 416 */     } else if (this.alternative == H1.LESS_THAN) {
/* 417 */       d1 = d3;
/*     */     } else {
/* 419 */       d1 = d4;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 424 */     return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int sizeA() {
/* 432 */     return this.nA;
/*     */   } public int sizeB() {
/* 434 */     return this.nB;
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
/* 445 */       double[] arrayOfDouble1 = { 90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D };
/* 446 */       double[] arrayOfDouble2 = { 62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D };
/*     */       
/* 448 */       double[] arrayOfDouble3 = { 13.0D, 12.0D, 12.0D, 10.0D, 10.0D, 10.0D, 10.0D, 9.0D, 8.0D, 8.0D, 7.0D, 7.0D, 7.0D, 7.0D, 7.0D, 6.0D };
/* 449 */       double[] arrayOfDouble4 = { 17.0D, 16.0D, 15.0D, 15.0D, 15.0D, 14.0D, 14.0D, 14.0D, 13.0D, 13.0D, 13.0D, 12.0D, 12.0D, 12.0D, 12.0D, 11.0D, 11.0D, 10.0D, 10.0D, 10.0D, 8.0D, 8.0D, 6.0D };
/*     */       
/* 451 */       double[] arrayOfDouble5 = { 78.0D, 64.0D, 75.0D, 45.0D, 82.0D };
/* 452 */       double[] arrayOfDouble6 = { 110.0D, 70.0D, 53.0D, 51.0D };
/*     */       
/* 454 */       double[] arrayOfDouble7 = { 3.0D, 7.0D, 15.0D, 10.0D, 4.0D, 6.0D, 4.0D, 7.0D };
/* 455 */       double[] arrayOfDouble8 = { 19.0D, 11.0D, 36.0D, 8.0D, 25.0D, 23.0D, 38.0D, 14.0D, 17.0D, 41.0D, 25.0D, 21.0D };
/*     */       
/* 457 */       double[] arrayOfDouble9 = { 3.0D, 7.0D, 15.0D, 10.0D, 4.0D, 6.0D, 4.0D, 7.0D };
/* 458 */       double[] arrayOfDouble10 = { 19.0D, 11.0D, 36.0D, 7.0D, 25.0D, 23.0D, 38.0D, 15.0D, 17.0D, 41.0D, 25.0D, 21.0D };
/*     */       
/* 460 */       double[] arrayOfDouble11 = { 13.0D, 6.0D, 12.0D, 7.0D, 12.0D, 7.0D, 10.0D, 7.0D, 10.0D, 7.0D, 16.0D, 7.0D, 10.0D, 8.0D, 9.0D, 8.0D };
/* 461 */       double[] arrayOfDouble12 = { 17.0D, 6.0D, 10.0D, 8.0D, 15.0D, 8.0D, 15.0D, 10.0D, 15.0D, 10.0D, 14.0D, 10.0D, 14.0D, 11.0D, 14.0D, 11.0D, 13.0D, 12.0D, 13.0D, 12.0D, 13.0D, 12.0D, 12.0D };
/*     */ 
/*     */ 
/*     */       
/* 465 */       String[] arrayOfString = { "Minitab Ref. Manual, p.18-9", "Siegel,S.(1956), pp.121-125", "Siegel,S.(1956), pp.118-120.", "Neave & Worthington (1988), pp.109-113.", "Neave & Worthington (1988), p.116.", "NAG" };
/*     */       
/* 467 */       double[][] arrayOfDouble13 = { arrayOfDouble1, arrayOfDouble3, arrayOfDouble5, arrayOfDouble7, arrayOfDouble9, arrayOfDouble11 };
/* 468 */       double[][] arrayOfDouble14 = { arrayOfDouble2, arrayOfDouble4, arrayOfDouble6, arrayOfDouble8, arrayOfDouble10, arrayOfDouble12 };
/* 469 */       boolean bool = false;
/* 470 */       for (byte b = 0; b < arrayOfDouble13.length; b++) {
/*     */         
/* 472 */         System.out.println(arrayOfString[b]);
/* 473 */         MannWhitneyTest mannWhitneyTest1 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.NOT_EQUAL, 0.0D, bool);
/* 474 */         System.out.println("H1:A <> B: U=" + mannWhitneyTest1.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest1.getSP(), 4) + " Ra=" + mannWhitneyTest1.getRankSumA() + " Rb=" + mannWhitneyTest1.getRankSumB() + " t=" + mannWhitneyTest1.getCorrectionFactor());
/*     */         
/* 476 */         MannWhitneyTest mannWhitneyTest2 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.LESS_THAN, 0.0D, bool);
/* 477 */         System.out.println("H1:A < B: U=" + mannWhitneyTest2.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest2.getSP(), 4) + " Ra=" + mannWhitneyTest2.getRankSumA() + " Rb=" + mannWhitneyTest2.getRankSumB() + " t=" + mannWhitneyTest2.getCorrectionFactor());
/*     */         
/* 479 */         MannWhitneyTest mannWhitneyTest3 = new MannWhitneyTest(arrayOfDouble13[b], arrayOfDouble14[b], H1.GREATER_THAN, 0.0D, bool);
/* 480 */         System.out.println("H1:A > B: U=" + mannWhitneyTest3.getTestStatistic() + " z=" + Maths.round(mannWhitneyTest1.getZ(), 4) + " SP=" + Maths.round(mannWhitneyTest3.getSP(), 4) + " Ra=" + mannWhitneyTest3.getRankSumA() + " Rb=" + mannWhitneyTest3.getRankSumB() + " t=" + mannWhitneyTest3.getCorrectionFactor());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/MannWhitneyTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */