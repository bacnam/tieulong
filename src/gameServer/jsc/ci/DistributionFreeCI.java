/*     */ package jsc.ci;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DistributionFreeCI
/*     */   extends AbstractConfidenceInterval
/*     */ {
/*     */   public static final int ONE_SAMPLE = 0;
/*     */   public static final int PAIRED_SAMPLE = 1;
/*     */   public static final int TWO_SAMPLE_SHIFT = 2;
/*     */   public static final int TWO_SAMPLE_RATIO = 3;
/*     */   static final int LOOP_MAX = 1000000;
/*     */   protected int d;
/*     */   protected double achievedConfidence;
/*     */   
/*     */   public DistributionFreeCI(double paramDouble) {
/*  68 */     super(paramDouble);
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
/*     */   protected void computeInterval(int paramInt1, int paramInt2, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*     */     int j, m;
/*  89 */     if (paramInt1 < 0 || paramInt1 > 3) {
/*  90 */       throw new IllegalArgumentException("Invalid problem type.");
/*     */     }
/*     */     
/*  93 */     if (paramInt1 == 0 && Arrays.isConstant(paramArrayOfdouble1)) {
/*     */       
/*  95 */       this.lowerLimit = paramArrayOfdouble1[0];
/*  96 */       this.upperLimit = this.lowerLimit;
/*     */       return;
/*     */     } 
/*  99 */     if ((paramInt1 == 2 || paramInt1 == 3) && Arrays.isConstant(paramArrayOfdouble1) && Arrays.isConstant(paramArrayOfdouble2))
/*     */     {
/*     */       
/* 102 */       throw new IllegalArgumentException("Constant data.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     int i = paramArrayOfdouble1.length;
/*     */     
/* 113 */     if (paramInt1 == 0) {
/* 114 */       j = i;
/*     */     } else {
/* 116 */       j = paramArrayOfdouble2.length;
/*     */     } 
/* 118 */     if (paramInt1 == 1 && j != i) {
/* 119 */       throw new IllegalArgumentException("Arrays not equal length.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     double d = 0.0D;
/*     */ 
/*     */     
/* 128 */     int n = i * j;
/* 129 */     int i1 = Math.max(i, j);
/*     */ 
/*     */     
/* 132 */     int[] arrayOfInt = new int[i1 + 1];
/* 133 */     double[] arrayOfDouble1 = new double[i1 + 1];
/* 134 */     double[] arrayOfDouble2 = new double[i1 + 1];
/*     */ 
/*     */ 
/*     */     
/* 138 */     int k = i; byte b;
/* 139 */     for (b = 1; b <= k; ) { arrayOfDouble1[b] = paramArrayOfdouble1[b - 1]; b++; }
/*     */     
/* 141 */     if (paramInt1 == 0 || paramInt1 == 1) {
/*     */       
/* 143 */       if (paramInt1 == 1) {
/*     */         
/* 145 */         m = j;
/* 146 */         for (b = 1; b <= m; ) { arrayOfDouble2[b] = paramArrayOfdouble2[b - 1]; b++; }
/* 147 */          for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] - arrayOfDouble2[b]; b++; }
/*     */       
/*     */       } else {
/* 150 */         m = k;
/*     */       } 
/*     */       
/* 153 */       Sort.sort(arrayOfDouble1, null, 1, k, true);
/* 154 */       if (arrayOfDouble1[1] <= 0.0D) {
/*     */         
/* 156 */         d = Math.abs(arrayOfDouble1[1]);
/* 157 */         for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] + d; b++; }
/*     */       
/*     */       } 
/* 160 */       for (b = 1; b <= k; ) { arrayOfDouble2[b] = -arrayOfDouble1[b]; b++; }
/*     */     
/*     */     } else {
/*     */       
/* 164 */       m = j;
/* 165 */       for (b = 1; b <= m; ) { arrayOfDouble2[b] = paramArrayOfdouble2[b - 1]; b++; }
/*     */ 
/*     */       
/* 168 */       Sort.sort(arrayOfDouble1, null, 1, k, true);
/* 169 */       Sort.sort(arrayOfDouble2, null, 1, m, false);
/* 170 */       if (paramInt1 == 3) {
/*     */         
/* 172 */         if (arrayOfDouble1[1] <= 0.0D || arrayOfDouble2[m] <= 0.0D)
/* 173 */           throw new IllegalArgumentException("Data not greater than zero."); 
/* 174 */         for (b = 1; b <= k; ) { arrayOfDouble1[b] = Math.log(arrayOfDouble1[b]); b++; }
/* 175 */          for (b = 1; b <= m; ) { arrayOfDouble2[b] = Math.log(arrayOfDouble2[b]); b++; }
/*     */       
/*     */       } 
/* 178 */       if (arrayOfDouble1[1] <= 0.0D || arrayOfDouble2[m] <= 0.0D) {
/*     */         
/* 180 */         if (arrayOfDouble1[1] < 0.0D && arrayOfDouble2[m] > 0.0D)
/* 181 */           d = Math.abs(arrayOfDouble1[1]); 
/* 182 */         if (arrayOfDouble1[1] > 0.0D && arrayOfDouble2[m] < 0.0D)
/* 183 */           d = Math.abs(arrayOfDouble2[m]); 
/* 184 */         if (arrayOfDouble1[1] < arrayOfDouble2[m]) {
/* 185 */           d = Math.abs(arrayOfDouble1[1]);
/*     */         }
/*     */         
/* 188 */         for (b = 1; b <= k; ) { arrayOfDouble1[b] = arrayOfDouble1[b] + d; b++; }
/* 189 */          for (b = 1; b <= m; ) { arrayOfDouble2[b] = arrayOfDouble2[b] + d; b++; }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 194 */     Double[] arrayOfDouble = diff(paramInt1, paramInt2, k, arrayOfDouble1, m, arrayOfDouble2, n, arrayOfInt);
/*     */     
/* 196 */     this.lowerLimit = arrayOfDouble[paramInt2].doubleValue();
/*     */ 
/*     */     
/* 199 */     if (paramInt1 == 0 || paramInt1 == 1) {
/* 200 */       this.lowerLimit = this.lowerLimit / 2.0D - d;
/*     */     }
/* 202 */     if (paramInt1 == 3) {
/* 203 */       this.lowerLimit = Math.exp(this.lowerLimit);
/*     */     }
/*     */ 
/*     */     
/* 207 */     Sort.sort(arrayOfDouble1, null, 1, k, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     Sort.sort(arrayOfDouble2, null, 1, m, true);
/* 216 */     arrayOfDouble = diff(paramInt1, paramInt2, m, arrayOfDouble2, k, arrayOfDouble1, n, arrayOfInt);
/*     */     
/* 218 */     this.upperLimit = -arrayOfDouble[paramInt2].doubleValue();
/*     */ 
/*     */     
/* 221 */     if (paramInt1 == 0 || paramInt1 == 1) {
/*     */ 
/*     */       
/* 224 */       this.upperLimit = this.upperLimit / 2.0D - d;
/*     */       
/*     */       return;
/*     */     } 
/* 228 */     if (paramInt1 == 3) {
/* 229 */       this.upperLimit = Math.exp(this.upperLimit);
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
/*     */   private Double[] diff(int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfdouble1, int paramInt4, double[] paramArrayOfdouble2, int paramInt5, int[] paramArrayOfint) {
/* 254 */     Vector vector = new Vector(2 + 2 * paramInt2, 5);
/*     */     
/* 256 */     vector.add(new Double(0.0D));
/* 257 */     byte b1 = 0;
/* 258 */     double d2 = 0.0D;
/*     */ 
/*     */     
/* 261 */     if (paramInt1 == 0 || paramInt1 == 1)
/* 262 */     { for (byte b = 1; b <= paramInt3; ) { paramArrayOfint[b] = b; b++; }
/*     */        }
/* 264 */     else { for (byte b = 1; b <= paramInt4; ) { paramArrayOfint[b] = 1; b++; }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     double d1 = paramInt2 * Math.abs(paramArrayOfdouble1[paramInt3] - paramArrayOfdouble1[1] + paramArrayOfdouble2[1] - paramArrayOfdouble2[paramInt4]) / paramInt5;
/*     */ 
/*     */ 
/*     */     
/* 278 */     byte b2 = 0;
/*     */     label75: while (true) {
/* 280 */       d2 += d1;
/* 281 */       double d = paramArrayOfdouble1[1] + d2;
/* 282 */       int k = paramArrayOfint[1];
/* 283 */       boolean bool1 = false; int i;
/* 284 */       for (i = k; i <= paramInt3; i++) {
/*     */         
/* 286 */         if (paramArrayOfdouble1[i] > d) { bool1 = true; break; }
/* 287 */          b1++;
/*     */ 
/*     */         
/* 290 */         vector.add(new Double(paramArrayOfdouble1[i] - paramArrayOfdouble2[1]));
/*     */       } 
/* 292 */       paramArrayOfint[1] = bool1 ? i : (paramInt3 + 1);
/* 293 */       int j = 2;
/*     */       
/* 295 */       boolean bool2 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 301 */         if (++b2 > 1000000)
/* 302 */           throw new IllegalArgumentException("Cannot calculate confidence interval."); 
/* 303 */         if (!bool2) j = i; 
/* 304 */         bool2 = false;
/*     */ 
/*     */         
/* 307 */         for (int m = j; m <= paramInt4; m++) {
/*     */           
/* 309 */           double d3 = d2 - paramArrayOfdouble2[1] + paramArrayOfdouble2[m];
/* 310 */           if (d3 <= 0.0D)
/* 311 */             break;  d = paramArrayOfdouble1[1] + d3;
/* 312 */           k = paramArrayOfint[m];
/* 313 */           bool1 = false;
/* 314 */           for (i = k; i <= paramInt3; i++) {
/*     */             
/* 316 */             if (paramArrayOfdouble1[i] > d) {
/*     */               
/* 318 */               bool1 = true; break;
/* 319 */             }  b1++;
/*     */ 
/*     */             
/* 322 */             vector.add(new Double(paramArrayOfdouble1[i] - paramArrayOfdouble2[m]));
/*     */           } 
/* 324 */           paramArrayOfint[m] = bool1 ? i : (paramInt3 + 1);
/*     */         } 
/*     */         
/* 327 */         if (b1 > paramInt2 + 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 332 */           Double[] arrayOfDouble = vector.<Double>toArray(new Double[1]);
/* 333 */           Sort.sort(arrayOfDouble, null, 1, b1, true);
/* 334 */           return arrayOfDouble;
/*     */         } 
/*     */         
/* 337 */         if (paramArrayOfint[1] <= paramInt3)
/*     */           continue label75; 
/* 339 */         d2 += d1;
/* 340 */         boolean bool = false;
/* 341 */         for (i = j; i <= paramInt4; i++) {
/* 342 */           if (paramArrayOfint[i] <= paramInt3) { bool = true; break; } 
/* 343 */         }  if (!bool) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 348 */           Double[] arrayOfDouble = vector.<Double>toArray(new Double[1]);
/* 349 */           Sort.sort(arrayOfDouble, null, 1, b1, true);
/* 350 */           return arrayOfDouble;
/*     */         } 
/*     */         
/* 353 */         if (!bool) {
/*     */           continue label75;
/*     */         }
/*     */       } 
/*     */       break;
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
/*     */   public double getAchievedConfidence() {
/* 369 */     return this.achievedConfidence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getD() {
/* 378 */     return this.d;
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
/*     */   public static double[] differences(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 394 */     int i = paramArrayOfdouble1.length;
/* 395 */     int j = paramArrayOfdouble2.length;
/* 396 */     double[] arrayOfDouble = new double[i * j];
/* 397 */     byte b1 = 0;
/* 398 */     for (byte b2 = 0; b2 < i; b2++) {
/* 399 */       for (byte b = 0; b < j; b++)
/* 400 */         arrayOfDouble[b1++] = paramArrayOfdouble1[b2] - paramArrayOfdouble2[b]; 
/* 401 */     }  Arrays.sort(arrayOfDouble);
/* 402 */     return arrayOfDouble;
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
/*     */   public static double[] walshAverages(double[] paramArrayOfdouble) {
/* 418 */     int i = paramArrayOfdouble.length;
/* 419 */     double[] arrayOfDouble = new double[i * (i + 1) / 2];
/* 420 */     byte b1 = 0;
/* 421 */     for (byte b2 = 0; b2 < i; b2++) {
/* 422 */       for (byte b = b2; b < i; b++)
/* 423 */         arrayOfDouble[b1++] = (paramArrayOfdouble[b2] + paramArrayOfdouble[b]) / 2.0D; 
/* 424 */     }  Arrays.sort(arrayOfDouble);
/* 425 */     return arrayOfDouble;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 429 */     return new String(super.toString() + ". Achieved confidence = " + this.achievedConfidence);
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
/* 440 */       double[] arrayOfDouble1 = { -1.0D, -2.3D, 0.8D, -0.1D, -2.0D, -0.9D, 0.3D, -2.4D, 0.5D, -2.5D, 1.3D, -2.1D };
/* 441 */       double[] arrayOfDouble2 = DistributionFreeCI.walshAverages(arrayOfDouble1);
/* 442 */       System.out.print("\nWalsh averages: "); byte b;
/* 443 */       for (b = 0; b < arrayOfDouble2.length; ) { System.out.print(arrayOfDouble2[b] + ","); b++; }
/*     */       
/* 445 */       double[] arrayOfDouble3 = { 122.0D, 127.0D, 110.0D, 115.0D, 132.0D, 131.0D, 105.0D, 124.0D, 112.0D, 123.0D };
/* 446 */       double[] arrayOfDouble4 = { 104.0D, 103.0D, 121.0D, 114.0D, 95.0D, 102.0D, 119.0D, 108.0D, 130.0D, 109.0D, 99.0D, 113.0D };
/* 447 */       double[] arrayOfDouble5 = DistributionFreeCI.differences(arrayOfDouble3, arrayOfDouble4);
/* 448 */       System.out.print("\nDifferences: ");
/* 449 */       for (b = 0; b < arrayOfDouble5.length; ) { System.out.print(arrayOfDouble5[b] + ","); b++; }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/ci/DistributionFreeCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */