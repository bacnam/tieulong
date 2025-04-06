/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.combinatorics.MultiSetPermutation;
/*     */ import jsc.combinatorics.MultiSetPermutations;
/*     */ import jsc.datastructures.GroupedData;
/*     */ import jsc.distributions.Beta;
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.Gamma;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KruskalWallisTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   public static final int MAX_PERMUTATION_COUNT = 17153136;
/*     */   final int k;
/*     */   final int N;
/*     */   final double H;
/*     */   final int[] ns;
/*     */   final double[] R;
/*     */   final double[] z;
/*     */   final double SP;
/*     */   final double[] originalSample;
/*     */   private final double Rbar;
/*     */   private final double c;
/*     */   
/*     */   public KruskalWallisTest(GroupedData paramGroupedData, double paramDouble, boolean paramBoolean) {
/* 100 */     double d3 = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     this.k = paramGroupedData.getGroupCount();
/* 107 */     if (this.k < 2)
/* 108 */       throw new IllegalArgumentException("Less than two samples."); 
/* 109 */     this.N = paramGroupedData.getN();
/*     */ 
/*     */     
/* 112 */     this.ns = new int[this.k];
/* 113 */     this.R = new double[this.k];
/* 114 */     this.z = new double[this.k];
/*     */     
/* 116 */     double[] arrayOfDouble = paramGroupedData.getData();
/*     */ 
/*     */ 
/*     */     
/* 120 */     Rank rank = new Rank(arrayOfDouble, paramDouble);
/*     */     
/* 122 */     this.originalSample = rank.getRanks();
/* 123 */     int i = rank.getCorrectionFactor1();
/*     */     
/* 125 */     double d2 = this.N;
/* 126 */     double d1 = (i > 0) ? (1.0D - i / d2 * (d2 * d2 - 1.0D)) : 1.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     byte b2 = 0; byte b1;
/* 133 */     for (b1 = 0; b1 < this.k; b1++) {
/*     */       
/* 135 */       this.R[b1] = 0.0D;
/* 136 */       this.ns[b1] = paramGroupedData.getSize(b1);
/* 137 */       for (byte b = 0; b < this.ns[b1]; ) { this.R[b1] = this.R[b1] + rank.getRank(b2++); b++; }
/*     */     
/*     */     } 
/*     */     
/* 141 */     for (b1 = 0; b1 < this.k; ) { this.R[b1] = this.R[b1] / this.ns[b1]; b1++; }
/*     */ 
/*     */     
/* 144 */     this.Rbar = 0.5D * (d2 + 1.0D);
/* 145 */     this.c = 12.0D / d2 * (d2 + 1.0D);
/*     */     
/* 147 */     for (b1 = 0; b1 < this.k; b1++) {
/*     */       
/* 149 */       double d = this.R[b1] - this.Rbar;
/* 150 */       this.z[b1] = d / Math.sqrt((d2 + 1.0D) / 12.0D * (d2 / this.ns[b1] - 1.0D));
/* 151 */       d3 += this.ns[b1] * d * d;
/*     */     } 
/*     */ 
/*     */     
/* 155 */     if (d1 <= 0.0D)
/* 156 */       throw new IllegalArgumentException("Cannot calculate Kruskal-Wallis statistic."); 
/* 157 */     this.H = this.c * d3 / d1;
/*     */ 
/*     */     
/* 160 */     double d4 = Maths.multinomialCoefficient(this.ns);
/*     */     
/* 162 */     if (paramBoolean) {
/* 163 */       this.SP = gammaApproxSP(this.ns, this.H);
/*     */ 
/*     */     
/*     */     }
/* 167 */     else if (d4 <= 1.7153136E7D) {
/* 168 */       this.SP = exactSP();
/*     */     } else {
/* 170 */       this.SP = gammaApproxSP(this.ns, this.H);
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
/*     */   public KruskalWallisTest(GroupedData paramGroupedData) {
/* 185 */     this(paramGroupedData, 0.0D, false);
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
/*     */   public static double betaApproxSP(int[] paramArrayOfint, double paramDouble) {
/* 249 */     if (paramDouble < 0.0D)
/* 250 */       throw new IllegalArgumentException("Negative H."); 
/* 251 */     int i = paramArrayOfint.length;
/* 252 */     if (i < 2)
/* 253 */       throw new IllegalArgumentException("Less than 2 samples."); 
/* 254 */     int j = 0;
/* 255 */     double d1 = (i - 1);
/*     */     
/* 257 */     double d2 = 0.0D;
/* 258 */     double d3 = 0.0D;
/* 259 */     for (byte b = 0; b < i; b++) {
/*     */ 
/*     */       
/* 262 */       if (paramArrayOfint[b] < 1)
/* 263 */         throw new IllegalArgumentException("Less than one data value in a sample."); 
/* 264 */       d2 += 1.0D / paramArrayOfint[b];
/* 265 */       d3 += (paramArrayOfint[b] * paramArrayOfint[b] * paramArrayOfint[b]);
/* 266 */       j += paramArrayOfint[b];
/*     */     } 
/*     */ 
/*     */     
/* 270 */     double d4 = 2.0D * d1 - 2.0D * (3.0D * i * i - 6.0D * i + j * (2.0D * i * i - 6.0D * i + 1.0D)) / 5.0D * j * (j + 1.0D) - 1.2D * d2;
/*     */     
/* 272 */     double d5 = ((j * j * j) - d3) / j * (j + 1.0D);
/* 273 */     if (d4 <= 0.0D || d5 <= 0.0D)
/* 274 */       throw new IllegalArgumentException("Invalid sample sizes."); 
/* 275 */     double d6 = d1 * (d1 * (d5 - d1) - d4) / 0.5D * d5 * d4;
/* 276 */     double d7 = (d5 - d1) / d1 * d6;
/* 277 */     double d8 = 0.5D * d6;
/* 278 */     double d9 = 0.5D * d7;
/* 279 */     if (d8 <= 0.0D || d9 <= 0.0D)
/* 280 */       throw new IllegalArgumentException("Invalid sample sizes."); 
/* 281 */     double d10 = paramDouble / d5;
/* 282 */     if (d10 >= 1.0D) return 0.0D;
/*     */     
/* 284 */     return 1.0D - Beta.incompleteBeta(d10, d8, d9, Maths.lnB(d8, d9));
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
/*     */   public static double chiSquaredApproxSP(int paramInt, double paramDouble) {
/* 298 */     if (paramInt < 2)
/* 299 */       throw new IllegalArgumentException("Less than two samples."); 
/* 300 */     if (paramDouble < 0.0D)
/* 301 */       throw new IllegalArgumentException("Negative H."); 
/* 302 */     ChiSquared chiSquared = new ChiSquared((paramInt - 1));
/* 303 */     return 1.0D - chiSquared.cdf(paramDouble);
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
/*     */   public double exactSP() {
/* 330 */     MultiSetPermutations multiSetPermutations = new MultiSetPermutations(this.ns);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     double d = Maths.multinomialCoefficient(this.ns);
/*     */ 
/*     */     
/* 338 */     double[] arrayOfDouble1 = new double[this.N];
/* 339 */     int[] arrayOfInt = new int[this.k];
/* 340 */     double[] arrayOfDouble2 = new double[this.k];
/*     */     
/* 342 */     long l = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     while (multiSetPermutations.hasNext()) {
/*     */ 
/*     */       
/* 355 */       double d1 = 0.0D;
/*     */ 
/*     */       
/* 358 */       MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
/* 359 */       int[] arrayOfInt1 = multiSetPermutation.toIntArray();
/*     */       
/* 361 */       arrayOfInt[0] = 0; byte b1;
/* 362 */       for (b1 = 1; b1 < this.k; ) { arrayOfInt[b1] = arrayOfInt[b1 - 1] + this.ns[b1 - 1]; b1++; }
/*     */       
/* 364 */       for (b1 = 0; b1 < this.N; b1++) {
/*     */         
/* 366 */         int i = arrayOfInt1[b1] - 1;
/*     */         
/* 368 */         arrayOfDouble1[arrayOfInt[i]] = this.originalSample[b1];
/* 369 */         arrayOfInt[i] = arrayOfInt[i] + 1;
/*     */       } 
/*     */ 
/*     */       
/* 373 */       byte b2 = 0;
/* 374 */       for (b1 = 0; b1 < this.k; b1++) {
/*     */         
/* 376 */         arrayOfDouble2[b1] = 0.0D;
/* 377 */         for (byte b = 0; b < this.ns[b1]; ) { arrayOfDouble2[b1] = arrayOfDouble2[b1] + arrayOfDouble1[b2++]; b++; }
/*     */       
/*     */       } 
/*     */       
/* 381 */       for (b1 = 0; b1 < this.k; ) { arrayOfDouble2[b1] = arrayOfDouble2[b1] / this.ns[b1]; b1++; }
/*     */ 
/*     */ 
/*     */       
/* 385 */       for (b1 = 0; b1 < this.k; b1++) {
/*     */         
/* 387 */         double d3 = arrayOfDouble2[b1] - this.Rbar;
/*     */         
/* 389 */         d1 += this.ns[b1] * d3 * d3;
/*     */       } 
/*     */ 
/*     */       
/* 393 */       double d2 = this.c * d1;
/* 394 */       if (d2 >= this.H) l++;
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 399 */     arrayOfDouble1 = null;
/* 400 */     arrayOfInt = null;
/* 401 */     arrayOfDouble2 = null;
/*     */ 
/*     */     
/* 404 */     return l / d;
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
/*     */   public static double gammaApproxSP(int[] paramArrayOfint, double paramDouble) {
/* 428 */     int i = 0;
/* 429 */     int j = paramArrayOfint.length;
/* 430 */     if (j < 2)
/* 431 */       throw new IllegalArgumentException("Less than 2 samples."); 
/* 432 */     if (paramDouble < 0.0D)
/* 433 */       throw new IllegalArgumentException("Negative H."); 
/* 434 */     double d1 = (j - 1);
/* 435 */     double d2 = 0.0D;
/* 436 */     for (byte b = 0; b < j; b++) {
/*     */       
/* 438 */       i += paramArrayOfint[b];
/* 439 */       if (paramArrayOfint[b] < 1)
/* 440 */         throw new IllegalArgumentException("Less than one data value in a sample."); 
/* 441 */       d2 += 1.0D / paramArrayOfint[b];
/*     */     } 
/* 443 */     double d3 = 2.0D * d1 - 2.0D * (3.0D * j * j - 6.0D * j + i * (2.0D * j * j - 6.0D * j + 1.0D)) / 5.0D * i * (i + 1.0D) - 1.2D * d2;
/*     */     
/* 445 */     if (d3 <= 0.0D) {
/* 446 */       throw new IllegalArgumentException("Invalid sample sizes.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 462 */     return 1.0D - Gamma.incompleteGamma(paramDouble * d1 / d3, d1 * d1 / d3);
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
/*     */   public double getMeanRank(int paramInt) {
/* 483 */     return this.R[paramInt];
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
/*     */   public double getPermutationCount() {
/* 495 */     return Maths.multinomialCoefficient(this.ns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 502 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize(int paramInt) {
/* 510 */     return this.ns[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/* 517 */     return this.k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 524 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatistic() {
/* 531 */     return this.H;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 538 */     return this.H;
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
/*     */   public double getZ(int paramInt) {
/* 551 */     return this.z[paramInt];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 733 */       String[] arrayOfString = { "1", "1", "3", "1", "1", "3", "2", "2", "2", "1", "3", "2", "3", "2", "3", "3" };
/* 734 */       double[] arrayOfDouble = { 15.1D, 13.0D, 16.2D, 14.9D, 13.2D, 13.8D, 13.1D, 13.0D, 12.9D, 11.9D, 17.0D, 12.8D, 14.7D, 12.0D, 15.0D, 16.6D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 758 */       GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString);
/* 759 */       int i = groupedData.getGroupCount();
/* 760 */       int[] arrayOfInt = groupedData.getSizes();
/* 761 */       KruskalWallisTest kruskalWallisTest = new KruskalWallisTest(groupedData, 0.0D, true);
/* 762 */       for (byte b = 0; b < i; b++)
/* 763 */         System.out.println(groupedData.getLabel(b) + "\tn = " + groupedData.getSize(b) + "\tAve.rank = " + kruskalWallisTest.getMeanRank(b) + "\tz = " + kruskalWallisTest.getZ(b)); 
/* 764 */       double d1 = kruskalWallisTest.getTestStatistic();
/* 765 */       System.out.println("H = " + d1 + " " + (long)kruskalWallisTest.getPermutationCount() + " permutations");
/* 766 */       System.out.println("                   SP = " + kruskalWallisTest.getSP());
/* 767 */       System.out.println("       Beta approx SP = " + KruskalWallisTest.betaApproxSP(arrayOfInt, d1));
/* 768 */       System.out.println("      Gamma approx SP = " + KruskalWallisTest.gammaApproxSP(arrayOfInt, d1));
/* 769 */       System.out.println("Chi-squared approx SP = " + KruskalWallisTest.chiSquaredApproxSP(i, d1));
/*     */ 
/*     */       
/* 772 */       long l1 = System.currentTimeMillis();
/* 773 */       double d2 = kruskalWallisTest.exactSP();
/* 774 */       long l2 = System.currentTimeMillis();
/* 775 */       System.out.println("       Exact(test) SP = " + d2);
/* 776 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/KruskalWallisTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */