/*     */ package jsc.onesample;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.PermutationTest;
/*     */ import jsc.tests.SignificanceTest;
/*     */ import jsc.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FisherSymmetryTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   static final double PTOL = 0.001D;
/*     */   private boolean negsumSmaller;
/*     */   private double cv;
/*     */   private double T;
/*     */   private final double SP;
/*     */   private H1 alternative;
/*     */   
/*     */   public FisherSymmetryTest(double[] paramArrayOfdouble, H1 paramH1) {
/*     */     double d;
/*  50 */     this.alternative = paramH1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (Arrays.isIntegers(paramArrayOfdouble)) {
/*  57 */       d = fisherProbInt(paramArrayOfdouble);
/*     */     } else {
/*  59 */       d = fisherProb(paramArrayOfdouble, 0.001D);
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
/*  70 */     if (paramH1 == H1.NOT_EQUAL) {
/*  71 */       this.SP = d + d;
/*  72 */     } else if (paramH1 == H1.LESS_THAN) {
/*  73 */       this.SP = this.negsumSmaller ? (1.0D - d) : d;
/*     */     } else {
/*  75 */       this.SP = this.negsumSmaller ? d : (1.0D - d);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FisherSymmetryTest(double[] paramArrayOfdouble) {
/*  84 */     this(paramArrayOfdouble, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FisherSymmetryTest(PairedData paramPairedData, H1 paramH1) {
/*  93 */     this(paramPairedData.differences(), paramH1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FisherSymmetryTest(PairedData paramPairedData) {
/* 101 */     this(paramPairedData, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double fisherProb(double[] paramArrayOfdouble, double paramDouble) {
/*     */     double d3, d4, d11;
/* 112 */     int i = paramArrayOfdouble.length;
/*     */ 
/*     */     
/* 115 */     byte b1 = 0, b2 = 0; int k = 0, m = 0;
/*     */ 
/*     */     
/* 118 */     double d1 = 0.0D, d2 = 0.0D;
/*     */ 
/*     */     
/* 121 */     long l = i;
/*     */     
/* 123 */     int[] arrayOfInt = new int[i];
/*     */     
/* 125 */     boolean[] arrayOfBoolean = new boolean[i];
/*     */     byte b4;
/*     */     double d6, d7;
/* 128 */     for (b4 = 0, d6 = 0.0D, d7 = 0.0D; b4 < i; b4++) {
/* 129 */       d7 += paramArrayOfdouble[b4] * paramArrayOfdouble[b4];
/* 130 */       if (paramArrayOfdouble[b4] >= 0.0D) {
/*     */         
/* 132 */         b1++;
/* 133 */         d1 += paramArrayOfdouble[b4];
/* 134 */         arrayOfBoolean[b4] = true;
/*     */       }
/*     */       else {
/*     */         
/* 138 */         b2++;
/* 139 */         d2 -= paramArrayOfdouble[b4];
/* 140 */         paramArrayOfdouble[b4] = -paramArrayOfdouble[b4];
/* 141 */         arrayOfBoolean[b4] = false;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     d6 = (d1 + d2) / 2.0D;
/* 146 */     d7 /= 4.0D;
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (d1 < d2) {
/* 151 */       d11 = d1; byte b = b1; this.negsumSmaller = false;
/*     */     } else {
/* 153 */       d11 = d2; byte b = b2; this.negsumSmaller = true;
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
/* 168 */     double d5 = Math.sqrt((i - (d6 - d11) * (d6 - d11) / d7) / 48.0D);
/* 169 */     double d8 = d6 * (d6 - d11) / paramDouble * d7;
/* 170 */     double d9 = 2.0D / paramDouble;
/*     */     
/* 172 */     int j = (int)(d5 * ((d8 > d9) ? d8 : d9));
/*     */ 
/*     */     
/* 175 */     byte b3 = (b1 < b2) ? b1 : b2;
/*     */     
/* 177 */     if (b3 > 0) {
/* 178 */       d3 = j / d11;
/*     */     } else {
/* 180 */       d3 = 1.0D;
/*     */     } 
/* 182 */     for (b4 = 0; b4 < i; b4++) {
/* 183 */       arrayOfInt[b4] = (int)(paramArrayOfdouble[b4] * d3 + 0.5D);
/*     */ 
/*     */       
/* 186 */       if (arrayOfBoolean[b4]) { k += arrayOfInt[b4]; }
/*     */       else
/* 188 */       { m += arrayOfInt[b4]; }
/*     */     
/* 190 */     }  j = (k < m) ? k : m;
/*     */     
/* 192 */     l = (j + 2);
/*     */     
/* 194 */     double[] arrayOfDouble = new double[j + 2];
/*     */ 
/*     */ 
/*     */     
/* 198 */     Arrays.sort(arrayOfInt);
/* 199 */     for (b4 = 0; b4 < i; b4++) {
/* 200 */       arrayOfInt[b4] = (arrayOfInt[b4] < j + 1) ? arrayOfInt[b4] : (j + 1);
/*     */     }
/* 202 */     arrayOfDouble[0] = arrayOfDouble[0] + 1.0D;
/* 203 */     arrayOfDouble[arrayOfInt[0]] = arrayOfDouble[arrayOfInt[0]] + 1.0D;
/* 204 */     int n = arrayOfInt[0];
/* 205 */     for (byte b5 = 1; b5 < i; b5++) {
/*     */       
/* 207 */       int i1 = n + arrayOfInt[b5];
/* 208 */       if (i1 > j) {
/*     */         
/* 210 */         arrayOfDouble[j + 1] = arrayOfDouble[j + 1] * 2.0D;
/* 211 */         for (int i4 = j; i4 >= j - arrayOfInt[b5] + 1; ) { arrayOfDouble[j + 1] = arrayOfDouble[j + 1] + arrayOfDouble[i4]; i4--; }
/*     */       
/* 213 */       }  int i3 = (j > i1) ? i1 : j;
/* 214 */       for (int i2 = i3; i2 >= arrayOfInt[b5]; ) { arrayOfDouble[i2] = arrayOfDouble[i2] + arrayOfDouble[i2 - arrayOfInt[b5]]; i2--; }
/* 215 */        n = i1;
/*     */     } 
/*     */     
/* 218 */     double d10 = 0.0D;
/*     */     
/* 220 */     if ((this.alternative == H1.GREATER_THAN && !this.negsumSmaller) || (this.alternative == H1.LESS_THAN && this.negsumSmaller)) {
/*     */ 
/*     */       
/* 223 */       for (b4 = 0; b4 < j; ) { d10 += arrayOfDouble[b4]; b4++; }
/* 224 */        d4 = d10 / (d10 + arrayOfDouble[j + 1] + arrayOfDouble[j]);
/*     */     }
/*     */     else {
/*     */       
/* 228 */       for (b4 = 0; b4 <= j; ) { d10 += arrayOfDouble[b4]; b4++; }
/* 229 */        d4 = d10 / (d10 + arrayOfDouble[j + 1]);
/*     */     } 
/*     */     
/* 232 */     this.cv = 100.0D * arrayOfDouble[j] * d5 / d10;
/*     */ 
/*     */ 
/*     */     
/* 236 */     arrayOfDouble = null;
/* 237 */     arrayOfInt = null;
/* 238 */     arrayOfBoolean = null;
/*     */     
/* 240 */     this.T = d11;
/* 241 */     return d4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double fisherProbInt(double[] paramArrayOfdouble) {
/*     */     int m;
/*     */     double d1;
/* 253 */     int i = paramArrayOfdouble.length;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     int n = 0, i1 = 0;
/*     */     
/*     */     byte b2;
/*     */     
/* 262 */     for (b2 = 0; b2 < i; b2++) {
/* 263 */       if (paramArrayOfdouble[b2] >= 0.0D) {
/*     */         
/* 265 */         n = (int)(n + paramArrayOfdouble[b2]);
/*     */       }
/*     */       else {
/*     */         
/* 269 */         i1 = (int)(i1 - paramArrayOfdouble[b2]);
/* 270 */         paramArrayOfdouble[b2] = -paramArrayOfdouble[b2];
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 275 */     if (n < i1) {
/* 276 */       m = n; this.negsumSmaller = false;
/*     */     } else {
/* 278 */       m = i1; this.negsumSmaller = true;
/*     */     } 
/* 280 */     int k = m;
/*     */     
/* 282 */     long l = (k + 2);
/* 283 */     double[] arrayOfDouble = new double[k + 2];
/*     */ 
/*     */     
/* 286 */     Arrays.sort(paramArrayOfdouble);
/* 287 */     for (b2 = 0; b2 < i; b2++) {
/* 288 */       paramArrayOfdouble[b2] = (paramArrayOfdouble[b2] < (k + 1)) ? paramArrayOfdouble[b2] : (k + 1);
/*     */     }
/* 290 */     for (b2 = 0; b2 <= k + 1; ) { arrayOfDouble[b2] = 0.0D; b2++; }
/* 291 */      arrayOfDouble[0] = arrayOfDouble[0] + 1.0D;
/* 292 */     arrayOfDouble[(int)paramArrayOfdouble[0]] = arrayOfDouble[(int)paramArrayOfdouble[0]] + 1.0D;
/* 293 */     int j = (int)paramArrayOfdouble[0];
/* 294 */     for (byte b1 = 1; b1 < i; b1++) {
/*     */       
/* 296 */       int i2 = (int)(j + paramArrayOfdouble[b1]);
/* 297 */       if (i2 > k) {
/*     */         
/* 299 */         arrayOfDouble[k + 1] = 2.0D * arrayOfDouble[k + 1];
/* 300 */         for (int i5 = k; i5 >= k - paramArrayOfdouble[b1] + 1.0D; ) { arrayOfDouble[k + 1] = arrayOfDouble[k + 1] + arrayOfDouble[i5]; i5--; }
/*     */       
/* 302 */       }  int i4 = (k > i2) ? i2 : k;
/* 303 */       for (int i3 = i4; i3 >= paramArrayOfdouble[b1]; ) { arrayOfDouble[i3] = arrayOfDouble[i3] + arrayOfDouble[i3 - (int)paramArrayOfdouble[b1]]; i3--; }
/* 304 */        j = i2;
/*     */     } 
/*     */     
/* 307 */     double d2 = 0.0D;
/*     */ 
/*     */ 
/*     */     
/* 311 */     if ((this.alternative == H1.GREATER_THAN && !this.negsumSmaller) || (this.alternative == H1.LESS_THAN && this.negsumSmaller)) {
/*     */ 
/*     */       
/* 314 */       for (b2 = 0; b2 < k; ) { d2 += arrayOfDouble[b2]; b2++; }
/* 315 */        d1 = d2 / (d2 + arrayOfDouble[k + 1] + arrayOfDouble[k]);
/*     */     }
/*     */     else {
/*     */       
/* 319 */       for (b2 = 0; b2 <= k; ) { d2 += arrayOfDouble[b2]; b2++; }
/* 320 */        d1 = d2 / (d2 + arrayOfDouble[k + 1]);
/*     */     } 
/*     */     
/* 323 */     arrayOfDouble = null;
/* 324 */     this.T = m;
/* 325 */     return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCV() {
/* 335 */     return this.cv;
/*     */   } public double getSP() {
/* 337 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 346 */     return this.T;
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
/* 370 */       double[] arrayOfDouble1 = { -8.0D, -7.0D, -6.0D, -5.0D, -5.0D, -5.0D, -5.0D, -4.0D, -4.0D, -4.0D, -2.0D, -2.0D, -2.0D, -2.0D, -2.0D, -2.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 2.0D, 2.0D, 3.0D, 4.0D, 5.0D, 5.0D, 5.0D };
/*     */       
/* 372 */       FisherSymmetryTest fisherSymmetryTest = new FisherSymmetryTest(arrayOfDouble1, H1.LESS_THAN);
/* 373 */       System.out.println("Fisher symm: SP = " + fisherSymmetryTest.getSP() + " T = " + fisherSymmetryTest.getTestStatistic() + " CV = " + fisherSymmetryTest.getCV());
/*     */ 
/*     */       
/* 376 */       double[] arrayOfDouble2 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
/* 377 */       double[] arrayOfDouble3 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 392 */       PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);
/*     */ 
/*     */       
/* 395 */       H1[] arrayOfH1 = { H1.LESS_THAN, H1.NOT_EQUAL, H1.GREATER_THAN };
/* 396 */       Tail[] arrayOfTail = { Tail.LOWER, Tail.TWO, Tail.UPPER };
/* 397 */       for (byte b = 0; b < 3; b++) {
/*     */         
/* 399 */         fisherSymmetryTest = new FisherSymmetryTest(pairedData, arrayOfH1[b]);
/* 400 */         System.out.println("Fisher symm: SP = " + fisherSymmetryTest.getSP() + " T = " + fisherSymmetryTest.getTestStatistic() + " CV = " + fisherSymmetryTest.getCV());
/* 401 */         PairedTtest pairedTtest = new PairedTtest(pairedData, arrayOfH1[b]);
/* 402 */         PermutationTest permutationTest = new PermutationTest(pairedTtest, arrayOfTail[b], false, 0, 0.0D, null);
/* 403 */         System.out.println("Permutation: SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
/* 404 */         System.out.println("   Paired t: SP = " + pairedTtest.getSP() + " t = " + pairedTtest.getTestStatistic());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/FisherSymmetryTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */