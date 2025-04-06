/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public class PitmanTwoSampleTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   static final double PTOL = 0.001D;
/*     */   private double cv;
/*     */   private double T;
/*     */   private double SP;
/*     */   private H1 alternative;
/*     */   
/*     */   public PitmanTwoSampleTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1) {
/*     */     double d;
/*  50 */     int i = paramArrayOfdouble1.length;
/*  51 */     int j = paramArrayOfdouble2.length;
/*  52 */     int k = i + j;
/*  53 */     this.alternative = paramH1;
/*     */ 
/*     */     
/*  56 */     int[] arrayOfInt = new int[k];
/*     */     
/*  58 */     if (Arrays.isIntegers(paramArrayOfdouble1) && Arrays.isIntegers(paramArrayOfdouble2)) {
/*     */       
/*  60 */       int[] arrayOfInt1 = new int[k]; byte b;
/*  61 */       for (b = 0; b < i; ) { arrayOfInt1[b] = (int)paramArrayOfdouble1[b]; arrayOfInt[b] = 1; b++; }
/*  62 */        for (b = 0; b < j; ) { arrayOfInt1[b + i] = (int)paramArrayOfdouble2[b]; arrayOfInt[b + i] = 2; b++; }
/*  63 */        d = pitmanProbInt(arrayOfInt1, arrayOfInt, k);
/*     */     }
/*     */     else {
/*     */       
/*  67 */       double[] arrayOfDouble = new double[k]; byte b;
/*  68 */       for (b = 0; b < i; ) { arrayOfDouble[b] = paramArrayOfdouble1[b]; arrayOfInt[b] = 1; b++; }
/*  69 */        for (b = 0; b < j; ) { arrayOfDouble[b + i] = paramArrayOfdouble2[b]; arrayOfInt[b + i] = 2; b++; }
/*  70 */        d = pitmanProb(arrayOfDouble, arrayOfInt, k, 0.001D);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     this.SP = d;
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
/*     */   public PitmanTwoSampleTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  99 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double pitmanProb(double[] paramArrayOfdouble, int[] paramArrayOfint, int paramInt, double paramDouble) {
/*     */     double d17;
/* 109 */     byte b1 = 0, b2 = 0; int j = 0;
/* 110 */     double d1 = 0.0D, d2 = 0.0D, d8 = 0.0D, d9 = 0.0D;
/*     */     
/* 112 */     int m = paramInt;
/*     */     
/* 114 */     int[] arrayOfInt1 = new int[paramInt];
/*     */     
/* 116 */     int[] arrayOfInt2 = new int[paramInt];
/*     */     
/*     */     byte b3;
/*     */     
/* 120 */     for (b3 = 0; b3 < paramInt; b3++) {
/* 121 */       if (b3 == 0) {
/* 122 */         d8 = paramArrayOfdouble[b3];
/* 123 */         d9 = paramArrayOfdouble[b3];
/*     */       }
/*     */       else {
/*     */         
/* 127 */         d8 = (paramArrayOfdouble[b3] > d8) ? d8 : paramArrayOfdouble[b3];
/* 128 */         d9 = (paramArrayOfdouble[b3] > d9) ? paramArrayOfdouble[b3] : d9;
/*     */       } 
/* 130 */       if (paramArrayOfint[b3] == 1) {
/*     */         
/* 132 */         b1++;
/* 133 */         d1 += paramArrayOfdouble[b3];
/*     */       }
/*     */       else {
/*     */         
/* 137 */         b2++;
/* 138 */         d2 += paramArrayOfdouble[b3];
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     double d10 = d1 / b1;
/* 143 */     double d11 = d2 / b2;
/* 144 */     boolean bool2 = (d10 > d11) ? true : true;
/* 145 */     for (b3 = 0; b3 < paramInt; ) { paramArrayOfdouble[b3] = paramArrayOfdouble[b3] - d8; b3++; }
/* 146 */      boolean bool1 = (b1 < b2) ? true : true;
/* 147 */     d1 -= b1 * d8;
/* 148 */     d2 -= b2 * d8;
/* 149 */     d9 -= d8;
/*     */     
/* 151 */     if (bool2 != bool1) {
/*     */       
/* 153 */       for (b3 = 0; b3 < paramInt; ) { paramArrayOfdouble[b3] = d9 - paramArrayOfdouble[b3]; b3++; }
/* 154 */        d1 = b1 * d9 - d1;
/* 155 */       d2 = b2 * d9 - d2;
/*     */     } 
/* 157 */     double d4 = (bool1 == true) ? d1 : d2;
/* 158 */     this.T = d4;
/* 159 */     byte b5 = (b1 < b2) ? b1 : b2;
/*     */     
/* 161 */     double d14 = b5;
/* 162 */     double d15 = paramInt; double d12, d13;
/* 163 */     for (d12 = 0.0D, d13 = 0.0D, b3 = 0; b3 < paramInt; b3++) {
/* 164 */       d12 += paramArrayOfdouble[b3];
/* 165 */       d13 += paramArrayOfdouble[b3] * paramArrayOfdouble[b3];
/* 166 */       for (int n = b3 + 1; n < paramInt; ) { d13 -= 2.0D * paramArrayOfdouble[b3] * paramArrayOfdouble[n] / (d15 - 1.0D); n++; }
/*     */     
/* 168 */     }  d12 = d14 / d15 * d12;
/* 169 */     d13 = d14 / d15 * (1.0D - d14 / d15) * d13;
/* 170 */     double d16 = Math.sqrt(d14 / d15 * (1.0D - d14 / d15) * (d15 + (d12 - d4) * (d12 - d4) * (d15 + 1.0D) / d15 * d13) / 12.0D);
/* 171 */     double d6 = d12 * (d12 - d4) / paramDouble * d13;
/* 172 */     double d7 = 2.0D / paramDouble;
/* 173 */     int i = (int)(d16 * ((d6 > d7) ? d6 : d7));
/*     */     
/* 175 */     double d3 = i / d4;
/* 176 */     for (b3 = 0; b3 < paramInt; b3++) {
/* 177 */       arrayOfInt2[b3] = (int)(paramArrayOfdouble[b3] * d3 + 0.5D);
/* 178 */       if (paramArrayOfint[b3] == bool1) j += arrayOfInt2[b3]; 
/*     */     } 
/* 180 */     i = j;
/*     */     
/* 182 */     m = (i + 2) * b5;
/*     */     
/* 184 */     double[] arrayOfDouble = new double[m];
/*     */ 
/*     */     
/* 187 */     Arrays.sort(arrayOfInt2);
/* 188 */     for (b3 = 0; b3 < paramInt; b3++) {
/* 189 */       arrayOfInt2[b3] = (arrayOfInt2[b3] < i + 1) ? arrayOfInt2[b3] : (i + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 194 */     for (b3 = 0; b3 < b5; ) { arrayOfInt1[b3] = b3 * (i + 2); b3++; }
/* 195 */      arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[0]] = arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[0]] + 1.0D;
/*     */ 
/*     */     
/* 198 */     int k = arrayOfInt2[0];
/* 199 */     for (byte b4 = 1; b4 < paramInt; b4++) {
/*     */       
/* 201 */       byte b7 = (b5 + b4 - paramInt > 1) ? (b5 + b4 - paramInt) : 1;
/* 202 */       int n = k + arrayOfInt2[b4];
/* 203 */       for (byte b6 = (b4 > b5 - 1) ? (b5 - 1) : b4; b6 >= b7; b6--) {
/*     */         
/* 205 */         if (n > i) for (int i3 = i - arrayOfInt2[b4] + 1; i3 <= i + 1; i3++)
/*     */           {
/* 207 */             arrayOfDouble[arrayOfInt1[b6] + i + 1] = arrayOfDouble[arrayOfInt1[b6] + i + 1] + arrayOfDouble[arrayOfInt1[b6 - 1] + i3];
/*     */           } 
/* 209 */         int i1 = (i > n) ? n : i;
/* 210 */         for (int i2 = arrayOfInt2[b4]; i2 <= i1; i2++)
/*     */         {
/* 212 */           arrayOfDouble[arrayOfInt1[b6] + i2] = arrayOfDouble[arrayOfInt1[b6] + i2] + arrayOfDouble[arrayOfInt1[b6 - 1] + i2 - arrayOfInt2[b4]];
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 217 */       arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[b4]] = arrayOfDouble[arrayOfInt1[0] + arrayOfInt2[b4]] + 1.0D;
/*     */       
/* 219 */       k = n;
/*     */     } 
/*     */     
/* 222 */     double d5 = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     arrayOfInt2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     if ((this.alternative == H1.LESS_THAN && bool2 == true) || (this.alternative == H1.GREATER_THAN && bool2 == 2)) {
/*     */ 
/*     */       
/* 242 */       for (b3 = 0; b3 <= i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
/* 243 */        d17 = d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1]);
/*     */     }
/* 245 */     else if (this.alternative == H1.NOT_EQUAL) {
/*     */       
/* 247 */       for (b3 = 0; b3 <= i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
/* 248 */        d17 = 2.0D * d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1]);
/*     */     }
/*     */     else {
/*     */       
/* 252 */       for (b3 = 0; b3 < i; ) { d5 += arrayOfDouble[arrayOfInt1[b5 - 1] + b3]; b3++; }
/* 253 */        d17 = 1.0D - d5 / (d5 + arrayOfDouble[arrayOfInt1[b5 - 1] + i + 1] + arrayOfDouble[arrayOfInt1[b5 - 1] + i]);
/*     */     } 
/* 255 */     this.cv = 100.0D * arrayOfDouble[arrayOfInt1[b5 - 1] + i] * d16 / d5;
/* 256 */     return d17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double pitmanProbInt(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt) {
/*     */     double d4;
/* 265 */     int i = 0, j = 0;
/* 266 */     byte b1 = 0, b2 = 0;
/*     */ 
/*     */     
/* 269 */     int m = 0, n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     int i3 = paramInt;
/*     */     
/* 277 */     int[] arrayOfInt = new int[paramInt];
/*     */     
/*     */     byte b3;
/* 280 */     for (b3 = 0; b3 < paramInt; b3++) {
/* 281 */       if (b3 == 0) {
/* 282 */         i = paramArrayOfint1[b3];
/* 283 */         j = paramArrayOfint1[b3];
/*     */       }
/*     */       else {
/*     */         
/* 287 */         i = (paramArrayOfint1[b3] > i) ? i : paramArrayOfint1[b3];
/* 288 */         j = (paramArrayOfint1[b3] > j) ? paramArrayOfint1[b3] : j;
/*     */       } 
/* 290 */       if (paramArrayOfint2[b3] == 1) {
/*     */         
/* 292 */         b1++;
/* 293 */         m += paramArrayOfint1[b3];
/*     */       }
/*     */       else {
/*     */         
/* 297 */         b2++;
/* 298 */         n += paramArrayOfint1[b3];
/*     */       } 
/*     */     } 
/*     */     
/* 302 */     double d1 = (m / b1);
/* 303 */     double d2 = (n / b2);
/* 304 */     boolean bool1 = (d1 > d2) ? true : true;
/*     */     
/* 306 */     for (b3 = 0; b3 < paramInt; ) { paramArrayOfint1[b3] = paramArrayOfint1[b3] - i; b3++; }
/* 307 */      boolean bool2 = (b1 < b2) ? true : true;
/*     */     
/* 309 */     m -= b1 * i;
/* 310 */     n -= b2 * i;
/* 311 */     j -= i;
/*     */     
/* 313 */     if (bool1 != bool2) {
/*     */       
/* 315 */       for (b3 = 0; b3 < paramInt; ) { paramArrayOfint1[b3] = j - paramArrayOfint1[b3]; b3++; }
/* 316 */        m = b1 * j - m;
/* 317 */       n = b2 * j - n;
/*     */     } 
/*     */     
/* 320 */     int i2 = (bool2 == true) ? m : n;
/*     */ 
/*     */     
/* 323 */     this.T = i2;
/* 324 */     byte b5 = (b1 < b2) ? b1 : b2;
/*     */ 
/*     */     
/* 327 */     int i1 = i2;
/*     */ 
/*     */     
/* 330 */     Arrays.sort(paramArrayOfint1);
/* 331 */     for (b3 = 0; b3 < paramInt; b3++) {
/* 332 */       paramArrayOfint1[b3] = (paramArrayOfint1[b3] < i1 + 1) ? paramArrayOfint1[b3] : (i1 + 1);
/*     */     }
/*     */ 
/*     */     
/* 336 */     i3 = (i1 + 2) * b5;
/*     */     
/* 338 */     double[] arrayOfDouble = new double[i3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     for (b3 = 0; b3 < b5; ) { arrayOfInt[b3] = b3 * (i1 + 2); b3++; }
/* 344 */      arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[0]] = arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[0]] + 1.0D;
/*     */ 
/*     */     
/* 347 */     int k = paramArrayOfint1[0];
/* 348 */     for (byte b4 = 1; b4 < paramInt; b4++) {
/*     */       
/* 350 */       byte b7 = (b5 + b4 - paramInt > 1) ? (b5 + b4 - paramInt) : 1;
/* 351 */       int i4 = k + paramArrayOfint1[b4];
/* 352 */       for (byte b6 = (b4 > b5 - 1) ? (b5 - 1) : b4; b6 >= b7; b6--) {
/*     */         
/* 354 */         if (i4 > i1) for (int i7 = i1 - paramArrayOfint1[b4] + 1; i7 <= i1 + 1; i7++)
/*     */           {
/* 356 */             arrayOfDouble[arrayOfInt[b6] + i1 + 1] = arrayOfDouble[arrayOfInt[b6] + i1 + 1] + arrayOfDouble[arrayOfInt[b6 - 1] + i7];
/*     */           } 
/* 358 */         int i5 = (i1 > i4) ? i4 : i1;
/* 359 */         for (int i6 = paramArrayOfint1[b4]; i6 <= i5; i6++)
/*     */         {
/* 361 */           arrayOfDouble[arrayOfInt[b6] + i6] = arrayOfDouble[arrayOfInt[b6] + i6] + arrayOfDouble[arrayOfInt[b6 - 1] + i6 - paramArrayOfint1[b4]];
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 366 */       arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[b4]] = arrayOfDouble[arrayOfInt[0] + paramArrayOfint1[b4]] + 1.0D;
/*     */       
/* 368 */       k = i4;
/*     */     } 
/*     */     
/* 371 */     double d3 = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     d3 = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     if ((this.alternative == H1.LESS_THAN && bool1 == true) || (this.alternative == H1.GREATER_THAN && bool1 == 2)) {
/*     */ 
/*     */       
/* 392 */       for (b3 = 0; b3 <= i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
/* 393 */        d4 = d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1]);
/*     */     }
/* 395 */     else if (this.alternative == H1.NOT_EQUAL) {
/*     */ 
/*     */ 
/*     */       
/* 399 */       for (b3 = 0; b3 <= i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
/* 400 */        d4 = 2.0D * d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1]);
/*     */     }
/*     */     else {
/*     */       
/* 404 */       for (b3 = 0; b3 < i1; ) { d3 += arrayOfDouble[arrayOfInt[b5 - 1] + b3]; b3++; }
/* 405 */        d4 = 1.0D - d3 / (d3 + arrayOfDouble[arrayOfInt[b5 - 1] + i1 + 1] + arrayOfDouble[arrayOfInt[b5 - 1] + i1]);
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
/* 417 */     return d4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCV() {
/* 427 */     return this.cv;
/*     */   } public double getSP() {
/* 429 */     return this.SP;
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
/*     */   public double getTestStatistic() {
/* 445 */     return this.T;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 468 */       double[] arrayOfDouble1 = { 90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D };
/* 469 */       double[] arrayOfDouble2 = { 62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 480 */       H1[] arrayOfH1 = { H1.LESS_THAN, H1.NOT_EQUAL, H1.GREATER_THAN };
/* 481 */       Tail[] arrayOfTail = { Tail.LOWER, Tail.TWO, Tail.UPPER };
/* 482 */       for (byte b = 0; b < 3; b++) {
/*     */         
/* 484 */         PitmanTwoSampleTest pitmanTwoSampleTest = new PitmanTwoSampleTest(arrayOfDouble2, arrayOfDouble1, arrayOfH1[b]);
/* 485 */         System.out.println("     Pitman: SP = " + pitmanTwoSampleTest.getSP() + " T = " + pitmanTwoSampleTest.getTestStatistic() + " CV = " + pitmanTwoSampleTest.getCV());
/* 486 */         TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble2, arrayOfDouble1, arrayOfH1[b], true);
/* 487 */         PermutationTest permutationTest = new PermutationTest(twoSampleTtest, arrayOfTail[b], false, 0, 0.0D, null);
/* 488 */         System.out.println("Permutation: SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
/* 489 */         System.out.println("     t-test: SP = " + twoSampleTtest.getSP() + " t = " + twoSampleTtest.getTestStatistic());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/PitmanTwoSampleTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */