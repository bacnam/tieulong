/*     */ package jsc.numerical;
/*     */ 
/*     */ import jsc.distributions.Beta;
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.PowerFunction;
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
/*     */ public class Integration
/*     */ {
/*     */   private static final int JMAX = 14;
/*     */   private static final int JMAXP = 15;
/*     */   private static final int K = 5;
/*     */   private static double dy;
/*  25 */   private static final double RT3 = Math.sqrt(3.0D);
/*  26 */   private static final double HAFRT3 = 0.5D * RT3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final double TWOPI = 6.283185307179586D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int M_MAX = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double esterr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int used;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double[] csxfrm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double clenshawCurtis(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
/*  68 */     int[] arrayOfInt = new int[9];
/*     */ 
/*     */ 
/*     */     
/*  72 */     double d1 = (paramDouble1 + paramDouble2) * 0.5D;
/*  73 */     double d2 = (paramDouble2 - paramDouble1) * 0.5D;
/*  74 */     int j = (int)Math.min(paramInt, 2.0D * Math.pow(3.0D, 9.0D)); int k;
/*  75 */     for (k = 1; k <= 8; ) { arrayOfInt[k] = 1; k++; }
/*     */     
/*  77 */     csxfrm = new double[j + 1];
/*     */ 
/*     */ 
/*     */     
/*  81 */     int i = 6;
/*     */     
/*  83 */     csxfrm[1] = paramFunction.function(paramDouble1);
/*  84 */     csxfrm[7] = paramFunction.function(paramDouble2);
/*  85 */     double d3 = d2 * RT3 * 0.5D;
/*  86 */     csxfrm[2] = paramFunction.function(d1 - d3);
/*  87 */     csxfrm[6] = paramFunction.function(d1 + d3);
/*  88 */     d3 = d2 * 0.5D;
/*  89 */     csxfrm[3] = paramFunction.function(d1 - d3);
/*  90 */     csxfrm[5] = paramFunction.function(d1 + d3);
/*  91 */     csxfrm[4] = paramFunction.function(d1);
/*     */ 
/*     */     
/*  94 */     double d5 = csxfrm[1] + csxfrm[7];
/*  95 */     double d6 = csxfrm[1] - csxfrm[7];
/*  96 */     double d7 = 2.0D * csxfrm[4];
/*  97 */     double d8 = csxfrm[2] + csxfrm[6];
/*  98 */     double d9 = (csxfrm[2] - csxfrm[6]) * RT3;
/*  99 */     double d10 = csxfrm[3] + csxfrm[5];
/* 100 */     double d11 = csxfrm[3] - csxfrm[5];
/* 101 */     double d12 = d5 + d10 + d10;
/* 102 */     double d13 = d8 + d8 + d7;
/* 103 */     double d14 = d6 + d11;
/* 104 */     double d15 = d5 - d10;
/* 105 */     double d16 = d8 - d7;
/* 106 */     csxfrm[1] = d12 + d13;
/* 107 */     csxfrm[2] = d14 + d9;
/* 108 */     csxfrm[3] = d15 + d16;
/* 109 */     csxfrm[4] = d6 - d11 - d11;
/* 110 */     csxfrm[5] = d15 - d16;
/* 111 */     csxfrm[6] = d14 - d9;
/* 112 */     csxfrm[7] = d12 - d13;
/* 113 */     used = 7;
/*     */ 
/*     */     
/* 116 */     double d4 = (d5 + d7 + d7) / 3.0D;
/*     */ 
/*     */     
/*     */     while (true) {
/* 120 */       int i2 = i - 3;
/* 121 */       double d18 = 0.5D * csxfrm[used] / (1.0D - (i * i));
/* 122 */       for (k = 1; k <= i2; k += 2) {
/*     */         
/* 124 */         int i5 = i - k;
/* 125 */         d18 += csxfrm[i5] / (i5 * (2 - i5));
/*     */       } 
/* 127 */       d18 += 0.5D * csxfrm[1];
/*     */ 
/*     */ 
/*     */       
/* 131 */       esterr = Math.abs(d4 * 3.0D - d18);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 139 */       if (Math.abs(d18) * paramDouble3 >= esterr || 3 * i + 1 > j) {
/*     */         
/* 141 */         double d = d2 * d18 / 0.5D * i;
/* 142 */         esterr = d2 * esterr / 0.5D * i;
/* 143 */         csxfrm = null;
/* 144 */         return d;
/*     */       } 
/*     */       
/* 147 */       d4 = d18;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 156 */       for (k = 2; k <= 8; k++)
/* 157 */         arrayOfInt[k - 1] = arrayOfInt[k]; 
/* 158 */       arrayOfInt[8] = 3 * arrayOfInt[7];
/* 159 */       k = used;
/* 160 */       double d17 = Math.PI / (3 * i); int i4;
/* 161 */       for (i4 = 1; i4 <= arrayOfInt[1]; i4++) {
/* 162 */         for (int i5 = i4; i5 <= arrayOfInt[2]; i5 += arrayOfInt[1]) {
/* 163 */           for (int i6 = i5; i6 <= arrayOfInt[3]; i6 += arrayOfInt[2]) {
/* 164 */             for (int i7 = i6; i7 <= arrayOfInt[4]; i7 += arrayOfInt[3])
/* 165 */             { for (int i8 = i7; i8 <= arrayOfInt[5]; i8 += arrayOfInt[4])
/* 166 */               { for (int i9 = i8; i9 <= arrayOfInt[6]; i9 += arrayOfInt[5])
/* 167 */                 { for (int i10 = i9; i10 <= arrayOfInt[7]; i10 += arrayOfInt[6])
/* 168 */                   { for (int i11 = i10; i11 <= arrayOfInt[8]; i11 += arrayOfInt[7])
/*     */                     
/* 170 */                     { double d = d17 * (3 * i11 - 2);
/* 171 */                       d3 = d2 * Math.cos(d);
/* 172 */                       d5 = paramFunction.function(d1 - d3);
/* 173 */                       d7 = paramFunction.function(d1 + d3);
/* 174 */                       d3 = d2 * Math.sin(d);
/* 175 */                       d6 = paramFunction.function(d1 + d3);
/* 176 */                       d8 = paramFunction.function(d1 - d3);
/* 177 */                       d9 = d5 + d7;
/* 178 */                       d10 = d6 + d8;
/* 179 */                       csxfrm[k + 1] = d9 + d10;
/* 180 */                       csxfrm[k + 2] = d5 - d7;
/* 181 */                       csxfrm[k + 3] = d9 - d10;
/* 182 */                       csxfrm[k + 4] = d6 - d8;
/* 183 */                       k += 4; }  }  }  }  } 
/*     */           } 
/*     */         } 
/* 186 */       }  int m = i + i;
/* 187 */       int i3 = 4;
/*     */       do {
/* 189 */         i4 = used + i3;
/* 190 */         int i5 = used + i3 + i3;
/* 191 */         int i6 = m - i3 - i3;
/* 192 */         r3pass(m, i3, i6, used, i4, i5);
/* 193 */         i3 *= 3;
/* 194 */       } while (i3 < i);
/*     */ 
/*     */ 
/*     */       
/* 198 */       d5 = csxfrm[1];
/* 199 */       d6 = csxfrm[used + 1];
/* 200 */       csxfrm[1] = d5 + d6 + d6;
/* 201 */       csxfrm[used + 1] = d5 - d6;
/* 202 */       d5 = csxfrm[i + 1];
/* 203 */       d6 = csxfrm[m + 2];
/* 204 */       csxfrm[i + 1] = d5 + d6;
/* 205 */       csxfrm[m + 2] = d5 - d6 - d6;
/*     */       
/* 207 */       int n = 3 * i;
/* 208 */       int i1 = i - 1;
/* 209 */       for (k = 1; k <= i1; k++) {
/*     */         
/* 211 */         i4 = i + k;
/* 212 */         int i5 = n - k;
/* 213 */         double d19 = d17 * k;
/* 214 */         double d20 = Math.cos(d19);
/* 215 */         double d21 = Math.sin(d19);
/* 216 */         d5 = d20 * csxfrm[i4 + 2] - d21 * csxfrm[i5 + 2];
/* 217 */         d6 = (d21 * csxfrm[i4 + 2] + d20 * csxfrm[i5 + 2]) * RT3;
/* 218 */         csxfrm[i4 + 2] = csxfrm[k + 1] - d5 - d6;
/* 219 */         csxfrm[i5 + 2] = csxfrm[k + 1] - d5 + d6;
/* 220 */         csxfrm[k + 1] = csxfrm[k + 1] + d5 + d5;
/*     */       } 
/*     */       
/* 223 */       d5 = csxfrm[m + 1];
/* 224 */       d6 = csxfrm[m + 2];
/* 225 */       for (k = 1; k <= i1; k++) {
/*     */         
/* 227 */         i4 = used + k;
/* 228 */         int i5 = m + k;
/* 229 */         csxfrm[i5] = csxfrm[i4];
/* 230 */         csxfrm[i4] = csxfrm[i5 + 2];
/*     */       } 
/* 232 */       csxfrm[n] = d5;
/* 233 */       csxfrm[n + 1] = d6;
/* 234 */       i = n;
/* 235 */       used = i + 1;
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
/*     */   private static void r3pass(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 250 */     int i = (paramInt2 - 1) / 2;
/* 251 */     int j = paramInt2 * 3;
/* 252 */     double d = 6.283185307179586D / j;
/*     */     int k;
/* 254 */     for (k = 1; k <= paramInt1; k += j) {
/*     */       
/* 256 */       double d1 = csxfrm[k + paramInt5] + csxfrm[k + paramInt6];
/* 257 */       double d2 = (csxfrm[k + paramInt5] - csxfrm[k + paramInt6]) * HAFRT3;
/* 258 */       csxfrm[k + paramInt5] = csxfrm[k + paramInt4] - d1 * 0.5D;
/* 259 */       csxfrm[k + paramInt6] = d2;
/* 260 */       csxfrm[k + paramInt4] = csxfrm[k + paramInt4] + d1;
/*     */     } 
/*     */     
/* 263 */     int m = paramInt2 / 2 + 1;
/* 264 */     for (k = m; k <= paramInt1; k += j) {
/*     */       
/* 266 */       double d1 = (csxfrm[k + paramInt5] + csxfrm[k + paramInt6]) * HAFRT3;
/* 267 */       double d2 = csxfrm[k + paramInt5] - csxfrm[k + paramInt6];
/* 268 */       csxfrm[k + paramInt5] = csxfrm[k + paramInt4] - d2;
/* 269 */       csxfrm[k + paramInt6] = d1;
/* 270 */       csxfrm[k + paramInt4] = csxfrm[k + paramInt4] + d2 * 0.5D;
/*     */     } 
/* 272 */     for (m = 1; m <= i; m++) {
/*     */       
/* 274 */       int i1 = m + 1;
/* 275 */       int i2 = paramInt2 - m + 1;
/*     */       
/* 277 */       double d1 = d * m;
/* 278 */       double d2 = Math.cos(d1);
/* 279 */       double d3 = Math.sin(d1);
/* 280 */       double d4 = d2 * d2 - d3 * d3;
/* 281 */       double d5 = 2.0D * d3 * d2;
/*     */       
/* 283 */       for (int n = i1; n <= paramInt1; n += j) {
/*     */         
/* 285 */         int i3 = n - i1 + i2;
/*     */         
/* 287 */         double d12 = csxfrm[n + paramInt4];
/* 288 */         double d15 = csxfrm[i3 + paramInt4];
/* 289 */         double d13 = d2 * csxfrm[n + paramInt5] - d3 * csxfrm[i3 + paramInt5];
/* 290 */         double d16 = d3 * csxfrm[n + paramInt5] + d2 * csxfrm[i3 + paramInt5];
/* 291 */         double d14 = d4 * csxfrm[n + paramInt6] - d5 * csxfrm[i3 + paramInt6];
/* 292 */         double d17 = d5 * csxfrm[n + paramInt6] + d4 * csxfrm[i3 + paramInt6];
/*     */         
/* 294 */         double d6 = d13 + d14;
/* 295 */         double d7 = (d13 - d14) * HAFRT3;
/* 296 */         double d8 = d12 - 0.5D * d6;
/* 297 */         double d9 = d16 + d17;
/* 298 */         double d10 = (d16 - d17) * HAFRT3;
/* 299 */         double d11 = d15 - 0.5D * d9;
/* 300 */         csxfrm[n + paramInt4] = d12 + d6;
/* 301 */         csxfrm[i3 + paramInt4] = d8 + d10;
/* 302 */         csxfrm[n + paramInt5] = d8 - d10;
/* 303 */         csxfrm[i3 + paramInt5] = d7 + d11;
/* 304 */         csxfrm[n + paramInt6] = d7 - d11;
/* 305 */         csxfrm[i3 + paramInt6] = d15 + d9;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double integrate(Function paramFunction, double paramDouble1, double paramDouble2, boolean paramBoolean, double paramDouble3, int paramInt) throws NumericalException {
/* 349 */     if (Double.isInfinite(paramDouble1)) {
/*     */       
/* 351 */       if (Double.isInfinite(paramDouble2))
/*     */       {
/* 353 */         return romberg(paramFunction, paramDouble1, 0.0D, paramDouble3, new ExtendedMidpointNegExpRule()) + romberg(paramFunction, 0.0D, paramDouble2, paramDouble3, new ExtendedMidpointPosExpRule());
/*     */       }
/*     */ 
/*     */       
/* 357 */       return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointNegExpRule());
/*     */     } 
/*     */ 
/*     */     
/* 361 */     if (Double.isInfinite(paramDouble2)) {
/* 362 */       return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointPosExpRule());
/*     */     }
/*     */     
/* 365 */     if (paramBoolean) {
/* 366 */       return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointRule());
/*     */     }
/* 368 */     return clenshawCurtis(paramFunction, paramDouble1, paramDouble2, paramDouble3, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double polint(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt, double paramDouble) throws NumericalException {
/* 379 */     int i = 1;
/*     */     
/* 381 */     double[] arrayOfDouble1 = new double[paramInt + 1];
/* 382 */     double[] arrayOfDouble2 = new double[paramInt + 1];
/*     */     
/* 384 */     double d1 = Math.abs(paramDouble - paramArrayOfdouble1[1]); byte b1;
/* 385 */     for (b1 = 1; b1 <= paramInt; b1++) {
/*     */       double d;
/* 387 */       if ((d = Math.abs(paramDouble - paramArrayOfdouble1[b1])) < d1) { i = b1; d1 = d; }
/* 388 */        arrayOfDouble1[b1] = paramArrayOfdouble2[b1];
/* 389 */       arrayOfDouble2[b1] = paramArrayOfdouble2[b1];
/*     */     } 
/* 391 */     double d2 = paramArrayOfdouble2[i--];
/* 392 */     for (byte b2 = 1; b2 < paramInt; b2++) {
/*     */       
/* 394 */       for (b1 = 1; b1 <= paramInt - b2; b1++) {
/*     */         
/* 396 */         double d4 = paramArrayOfdouble1[b1] - paramDouble;
/* 397 */         double d5 = paramArrayOfdouble1[b1 + b2] - paramDouble;
/* 398 */         double d6 = arrayOfDouble1[b1 + 1] - arrayOfDouble2[b1]; double d3;
/* 399 */         if ((d3 = d4 - d5) == 0.0D)
/* 400 */           throw new NumericalException("Cannot interpolate: identical x values"); 
/* 401 */         d3 = d6 / d3;
/* 402 */         arrayOfDouble2[b1] = d5 * d3;
/* 403 */         arrayOfDouble1[b1] = d4 * d3;
/*     */       } 
/* 405 */       d2 += dy = (2 * i < paramInt - b2) ? arrayOfDouble1[i + 1] : arrayOfDouble2[i--];
/*     */     } 
/* 407 */     return d2;
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
/*     */   public static double romberg(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
/* 427 */     int i = paramInt + 1;
/*     */     
/* 429 */     double[] arrayOfDouble1 = new double[i + 1];
/* 430 */     double[] arrayOfDouble2 = new double[i + 1];
/* 431 */     double[] arrayOfDouble3 = new double[6];
/* 432 */     double[] arrayOfDouble4 = new double[6];
/*     */     
/* 434 */     ExtendedTrapezoidalRule extendedTrapezoidalRule = new ExtendedTrapezoidalRule();
/* 435 */     arrayOfDouble2[1] = 1.0D;
/* 436 */     for (byte b = 1; b <= paramInt; b++) {
/*     */       
/* 438 */       arrayOfDouble1[b] = extendedTrapezoidalRule.getIntegral(paramFunction, paramDouble1, paramDouble2, b);
/*     */ 
/*     */       
/* 441 */       if (b >= 5) {
/*     */ 
/*     */         
/* 444 */         System.arraycopy(arrayOfDouble2, b - 5 + 1, arrayOfDouble4, 1, 5);
/* 445 */         System.arraycopy(arrayOfDouble1, b - 5 + 1, arrayOfDouble3, 1, 5);
/* 446 */         double d = polint(arrayOfDouble4, arrayOfDouble3, 5, 0.0D);
/*     */ 
/*     */         
/* 449 */         if (Math.abs(dy) <= paramDouble3 * Math.abs(d) / 300.0D) return d; 
/*     */       } 
/* 451 */       arrayOfDouble2[b + 1] = 0.25D * arrayOfDouble2[b];
/*     */     } 
/*     */     
/* 454 */     throw new NumericalException("Required accuracy not achieved in integration.");
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
/*     */   public static double romberg(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, IntegratingFunction paramIntegratingFunction) throws NumericalException {
/* 478 */     double d = 0.0D;
/* 479 */     double[] arrayOfDouble1 = new double[16];
/* 480 */     double[] arrayOfDouble2 = new double[16];
/* 481 */     double[] arrayOfDouble3 = new double[6];
/* 482 */     double[] arrayOfDouble4 = new double[6];
/*     */ 
/*     */     
/* 485 */     arrayOfDouble1[1] = 1.0D;
/*     */     
/* 487 */     for (byte b = 1; b <= 14; b++) {
/*     */ 
/*     */       
/* 490 */       try { arrayOfDouble2[b] = paramIntegratingFunction.getIntegral(paramFunction, paramDouble1, paramDouble2, b); }
/* 491 */       catch (NumericalException numericalException) { return d; }
/*     */ 
/*     */       
/* 494 */       if (b >= 5) {
/*     */ 
/*     */         
/* 497 */         System.arraycopy(arrayOfDouble1, b - 5 + 1, arrayOfDouble3, 1, 5);
/* 498 */         System.arraycopy(arrayOfDouble2, b - 5 + 1, arrayOfDouble4, 1, 5);
/* 499 */         d = polint(arrayOfDouble3, arrayOfDouble4, 5, 0.0D);
/*     */ 
/*     */         
/* 502 */         if (Math.abs(dy) <= paramDouble3 * Math.abs(d)) return d; 
/*     */       } 
/* 504 */       arrayOfDouble2[b + 1] = arrayOfDouble2[b];
/* 505 */       arrayOfDouble1[b + 1] = arrayOfDouble1[b] / 9.0D;
/*     */     } 
/*     */     
/* 508 */     throw new NumericalException("Required accuracy not achieved in integration.");
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
/*     */   static class Test
/*     */   {
/*     */     static int count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static void main(String[] param1ArrayOfString) {
/* 601 */       double d = 1.0E-6D;
/*     */       
/* 603 */       byte b1 = 9;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 609 */       for (byte b2 = 51; b2 <= 55; b2++) {
/*     */         
/* 611 */         Test50 test50 = new Test50(b2); 
/* 612 */         try { double d1 = Integration.clenshawCurtis(test50, test50.getA(), test50.getB(), d, 200);
/* 613 */           System.out.println("F(" + b2 + ") CC = " + Maths.roundSigFigs(d1, b1) + " " + count + " "); }
/* 614 */         catch (NumericalException numericalException) { System.out.println("F(" + b2 + ") CC " + numericalException.getMessage()); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 619 */         count = 0; 
/* 620 */         try { double d1 = Integration.romberg(test50, test50.getA(), test50.getB(), d, 20);
/* 621 */           System.out.println("F(" + b2 + ") R2 = " + Maths.roundSigFigs(d1, b1) + " " + count + " "); }
/* 622 */         catch (NumericalException numericalException) { System.out.println("F(" + b2 + ") R2 " + numericalException.getMessage()); }
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static class Test50
/*     */       implements Function
/*     */     {
/*     */       int i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 642 */       double p = 2.0D; double q = 4.0D;
/* 643 */       Beta B = new Beta(this.p, this.q);
/* 644 */       double b = 1.0D; double c = 2.0D;
/* 645 */       PowerFunction PF = new PowerFunction(this.b, this.c); public Test50(int param2Int) {
/* 646 */         this.i = param2Int; Integration.Test.count = 0;
/*     */       } public double function(double param2Double) {
/* 648 */         Integration.Test.count++;
/* 649 */         switch (this.i) { case 0:
/* 650 */             return Math.pow(param2Double, -5.0D);
/* 651 */           case 1: return 1.0D;
/* 652 */           case 2: return param2Double - 2.0D;
/* 653 */           case 3: return param2Double * param2Double - 2.0D * param2Double + 3.0D;
/* 654 */           case 4: return param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D;
/* 655 */           case 5: return param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D;
/* 656 */           case 6: return param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D;
/* 657 */           case 7: return param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D;
/* 658 */           case 8: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D;
/* 659 */           case 9: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D;
/* 660 */           case 10: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D;
/* 661 */           case 11: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D;
/* 662 */           case 12: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D;
/* 663 */           case 13: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D;
/* 664 */           case 14: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D;
/* 665 */           case 15: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D;
/* 666 */           case 16: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D;
/* 667 */           case 17: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D;
/* 668 */           case 18: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D;
/* 669 */           case 19: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D) + 19.0D;
/* 670 */           case 20: return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D) + 19.0D) - 20.0D;
/* 671 */           case 21: return Math.exp(param2Double);
/* 672 */           case 22: return Math.sin(Math.PI * param2Double);
/* 673 */           case 23: return Math.cos(param2Double);
/* 674 */           case 24: return param2Double / (Math.exp(param2Double) - 1.0D);
/* 675 */           case 25: return 1.0D / (1.0D + param2Double * param2Double);
/* 676 */           case 26: return 2.0D / (2.0D + Math.sin(31.41592653589793D * param2Double));
/* 677 */           case 27: return 1.0D / (1.0D + param2Double * param2Double * param2Double * param2Double);
/* 678 */           case 28: return 1.0D / (1.0D + Math.exp(param2Double));
/* 679 */           case 29: return param2Double * Math.sin(30.0D * param2Double) * Math.cos(param2Double);
/* 680 */           case 30: return param2Double * Math.sin(30.0D * param2Double) * Math.cos(50.0D * param2Double);
/* 681 */           case 31: return param2Double * Math.sin(30.0D * param2Double) / Math.sqrt(1.0D - param2Double * param2Double / 39.47841760435743D);
/* 682 */           case 32: return 0.46D * (Math.exp(param2Double) + Math.exp(-param2Double)) - Math.cos(param2Double);
/* 683 */           case 33: return 1.0D / (param2Double * param2Double * param2Double * param2Double + param2Double * param2Double + 0.9D);
/* 684 */           case 34: return Math.sqrt(98696.04401089359D - param2Double * param2Double) * Math.sin(param2Double);
/* 685 */           case 35: return 1.0D / (1.0D + param2Double);
/* 686 */           case 36: return Math.sqrt(param2Double);
/* 687 */           case 37: return Math.pow(param2Double, 0.25D);
/* 688 */           case 38: return Math.pow(param2Double, 0.125D);
/* 689 */           case 39: return Math.pow(param2Double, 0.0625D);
/* 690 */           case 40: return Math.sqrt(Math.abs(param2Double * param2Double - 0.25D));
/* 691 */           case 41: return Math.pow(param2Double, 1.5D);
/* 692 */           case 42: return Math.pow(Math.abs(param2Double * param2Double - 0.25D), 1.5D);
/* 693 */           case 43: return Math.pow(param2Double, 2.5D);
/* 694 */           case 44: return Math.pow(Math.abs(param2Double * param2Double - 0.25D), 2.5D);
/* 695 */           case 46: if (param2Double >= 0.0D && param2Double <= 0.333D) return param2Double; 
/* 696 */             if (param2Double > 0.333D && param2Double <= 0.667D) return param2Double + 1.0D; 
/* 697 */             if (param2Double > 0.667D && param2Double <= 1.0D) return param2Double + 2.0D; 
/* 698 */           case 47: if (param2Double > 0.49D && param2Double < 0.5D) return 0.0D; 
/* 699 */             return -1000.0D * (param2Double * param2Double - param2Double);
/* 700 */           case 48: if (param2Double >= 0.0D && param2Double <= 0.7182818284590451D) return 1.0D / (param2Double + 2.0D); 
/* 701 */             if (param2Double > 0.7182818284590451D && param2Double <= 1.0D) return 0.0D; 
/* 702 */           case 49: return 10000.0D * (param2Double - 0.1D) * (param2Double - 0.11D) * (param2Double - 0.12D) * (param2Double - 0.13D);
/* 703 */           case 50: return Math.sin(314.1592653589793D * param2Double);
/* 704 */           case 51: return Math.sqrt(1.0D - param2Double * param2Double);
/* 705 */           case 52: return this.B.pdf(param2Double);
/* 706 */           case 53: return param2Double * this.B.pdf(param2Double);
/* 707 */           case 54: return this.PF.pdf(param2Double);
/* 708 */           case 55: return param2Double * this.PF.pdf(param2Double); }
/*     */         
/* 710 */         return 0.0D;
/*     */       }
/*     */       public double getA() {
/* 713 */         if (this.i == 32 || this.i == 33) return -1.0D; 
/* 714 */         if (this.i == 0) return 0.01D; 
/* 715 */         return 0.0D;
/*     */       } public double getB() {
/* 717 */         if (this.i >= 29 && this.i <= 31) return 6.283185307179586D; 
/* 718 */         if (this.i == 34) return 314.1592653589793D; 
/* 719 */         if (this.i == 0) return 1.1D; 
/* 720 */         if (this.i == 54 || this.i == 55) return this.b; 
/* 721 */         return 1.0D;
/*     */       }
/*     */     }
/*     */     
/*     */     static class Func1 implements Function {
/*     */       public double function(double param2Double) {
/* 727 */         return Math.sqrt(1.0D - param2Double * param2Double);
/*     */       }
/*     */     }
/*     */     
/*     */     static class Func2 implements Function {
/*     */       public double function(double param2Double) {
/* 733 */         double d = 1.0D;
/* 734 */         if (param2Double < 0.0D) throw new IllegalArgumentException("Invalid variate-value."); 
/* 735 */         return Math.exp(-param2Double / d) / d;
/*     */       }
/*     */     }
/*     */     
/*     */     static class Func3 implements Function {
/* 740 */       ChiSquared D = new ChiSquared(4.0D); public double function(double param2Double) {
/* 741 */         return this.D.pdf(param2Double);
/*     */       } }
/*     */     
/*     */     static class Func4 implements Function {
/* 745 */       Normal D = new Normal(1.0D, 1.0D); public double function(double param2Double) {
/* 746 */         return this.D.pdf(param2Double);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/Integration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */