/*     */ package jsc.onesample;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import jsc.ci.DistributionFreeCI;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.WilcoxonT;
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
/*     */ public class WilcoxonMedianCI
/*     */   extends DistributionFreeCI
/*     */ {
/*     */   protected static final int SMALL_SAMPLE_SIZE = 24;
/*     */   double[] x;
/*     */   
/*     */   public WilcoxonMedianCI(double[] paramArrayOfdouble, double paramDouble, boolean paramBoolean) {
/*  65 */     super(paramDouble);
/*     */     double d;
/*  67 */     int i = paramArrayOfdouble.length;
/*     */     
/*  69 */     if (paramBoolean) {
/*     */ 
/*     */       
/*  72 */       double d1 = Normal.inverseStandardCdf(0.5D + 0.5D * paramDouble);
/*     */       
/*  74 */       this.d = (int)Math.round(0.5D * (0.5D * i * (i + 1.0D) + 1.0D - d1 * Math.sqrt(i * (i + 1.0D) * (2.0D * i + 1.0D) / 6.0D)));
/*     */       
/*  76 */       Normal normal = WilcoxonT.normalApproximation(i);
/*  77 */       d = normal.cdf(this.d - 1.0D);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  82 */       WilcoxonT wilcoxonT = new WilcoxonT(i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       d = 0.5D * (1.0D - paramDouble);
/*  94 */       long l = wilcoxonT.getMinValue();
/*     */ 
/*     */       
/*  97 */       double d1 = wilcoxonT.pdf(l);
/*  98 */       while (l < wilcoxonT.getMaxValue() && d1 < d) { l++; d1 += wilcoxonT.pdf(l); }
/*  99 */        l--;
/* 100 */       this.d = (int)(l + 1L);
/* 101 */       d = d1 - wilcoxonT.pdf((l + 1L));
/*     */     } 
/*     */ 
/*     */     
/* 105 */     this.achievedConfidence = 1.0D - 2.0D * d;
/* 106 */     computeInterval(0, this.d, paramArrayOfdouble, null);
/* 107 */     this.x = paramArrayOfdouble;
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
/*     */   public WilcoxonMedianCI(double[] paramArrayOfdouble, double paramDouble) {
/* 122 */     this(paramArrayOfdouble, paramDouble, (paramArrayOfdouble.length > 24));
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
/*     */   public WilcoxonMedianCI(PairedData paramPairedData, double paramDouble, boolean paramBoolean) {
/* 139 */     this(paramPairedData.differences(), paramDouble, paramBoolean);
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
/*     */   public WilcoxonMedianCI(PairedData paramPairedData, double paramDouble) {
/* 156 */     this(paramPairedData.differences(), paramDouble, (paramPairedData.getN() > 24));
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
/*     */   public double hodgesLehmannEstimate() {
/* 173 */     return hodgesLehmannEstimate(this.x);
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
/*     */   public static double hodgesLehmannEstimate(double[] paramArrayOfdouble) {
/* 214 */     int k, i = paramArrayOfdouble.length;
/*     */ 
/*     */     
/* 217 */     double[] arrayOfDouble = new double[1 + i];
/* 218 */     System.arraycopy(paramArrayOfdouble, 0, arrayOfDouble, 1, i);
/* 219 */     arrayOfDouble[0] = Double.NEGATIVE_INFINITY;
/* 220 */     Arrays.sort(arrayOfDouble);
/*     */ 
/*     */     
/* 223 */     int m = 0;
/* 224 */     int[] arrayOfInt1 = new int[1 + i];
/* 225 */     int[] arrayOfInt2 = new int[1 + i];
/* 226 */     int[] arrayOfInt3 = new int[1 + i];
/*     */ 
/*     */     
/* 229 */     if (i <= 2) {
/*     */       
/* 231 */       if (i == 1) return arrayOfDouble[1]; 
/* 232 */       return (arrayOfDouble[1] + arrayOfDouble[2]) / 2.0D;
/*     */     } 
/*     */     
/* 235 */     Random random = new Random();
/*     */ 
/*     */     
/* 238 */     int i3 = i * (i + 1) / 2;
/* 239 */     int n = (i3 + 1) / 2;
/* 240 */     int i1 = (i3 + 2) / 2;
/*     */     
/*     */     byte b;
/* 243 */     for (b = 1; b <= i; ) { arrayOfInt1[b] = b; arrayOfInt2[b] = i; b++; }
/*     */     
/* 245 */     int j = i3;
/* 246 */     int i2 = 0;
/*     */ 
/*     */     
/* 249 */     int i4 = (i + 1) / 2;
/* 250 */     int i5 = (i + 2) / 2;
/* 251 */     double d3 = arrayOfDouble[i4] + arrayOfDouble[i5];
/* 252 */     boolean bool = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     label106: while (true) {
/* 258 */       if (!bool) {
/*     */         
/* 260 */         double d6 = arrayOfDouble[1] + arrayOfDouble[1];
/* 261 */         double d5 = arrayOfDouble[i] + arrayOfDouble[i];
/* 262 */         for (b = 1; b <= i; b++) {
/*     */ 
/*     */           
/* 265 */           if (arrayOfInt1[b] <= arrayOfInt2[b]) {
/* 266 */             int i6 = arrayOfInt1[b];
/*     */             
/* 268 */             d5 = Math.min(d5, arrayOfDouble[i6] + arrayOfDouble[b]);
/* 269 */             int i7 = arrayOfInt2[b];
/*     */             
/* 271 */             d6 = Math.max(d6, arrayOfDouble[i7] + arrayOfDouble[b]);
/*     */           } 
/* 273 */         }  d3 = (d6 + d5) / 2.0D;
/*     */         
/* 275 */         if (d3 <= d5 || d3 > d6) d3 = d6;
/*     */         
/* 277 */         if (d5 != d6 && j != 2) {
/* 278 */           bool = true;
/*     */         } else {
/*     */           
/* 281 */           return d3 / 2.0D;
/*     */         } 
/*     */       } 
/*     */       
/*     */       while (true) {
/* 286 */         if (!bool) {
/*     */ 
/*     */ 
/*     */           
/* 290 */           int i6 = random.nextInt(j - 1);
/*     */           
/* 292 */           for (b = 1; b <= i; b++) {
/*     */             
/* 294 */             m = b;
/* 295 */             if (i6 <= arrayOfInt2[b] - arrayOfInt1[b])
/* 296 */               break;  i6 = i6 - arrayOfInt2[b] + arrayOfInt1[b] - 1;
/*     */           } 
/*     */           
/* 299 */           int i7 = (arrayOfInt1[m] + arrayOfInt2[m]) / 2;
/* 300 */           d3 = arrayOfDouble[m] + arrayOfDouble[i7];
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 306 */         bool = false;
/* 307 */         m = i;
/*     */         
/* 309 */         k = 0;
/*     */         
/* 311 */         for (b = 1; b <= i; b++) {
/*     */           
/* 313 */           arrayOfInt3[b] = 0;
/*     */ 
/*     */           
/* 316 */           while (m >= b) {
/*     */             
/* 318 */             if (arrayOfDouble[b] + arrayOfDouble[m] < d3) {
/*     */ 
/*     */ 
/*     */               
/* 322 */               arrayOfInt3[b] = m - b + 1;
/*     */               
/* 324 */               k += arrayOfInt3[b]; break;
/*     */             } 
/*     */             m--;
/*     */           } 
/*     */         } 
/* 329 */         if (k == i2) {
/*     */           continue label106;
/*     */         }
/*     */         
/* 333 */         if (k == i1 - 1) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 339 */         if (k - n < 0)
/*     */         
/*     */         { 
/*     */           
/* 343 */           for (b = 1; b <= i; ) { arrayOfInt1[b] = b + arrayOfInt3[b]; b++; }
/*     */            }
/* 345 */         else { if (k - n == 0) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 350 */           for (b = 1; b <= i; ) { arrayOfInt2[b] = b + arrayOfInt3[b] - 1; b++; }
/*     */            }
/*     */ 
/*     */ 
/*     */         
/* 355 */         i2 = 0;
/* 356 */         j = 0;
/*     */         
/* 358 */         for (b = 1; b <= i; b++) {
/*     */           
/* 360 */           i2 = i2 + arrayOfInt1[b] - b;
/* 361 */           j = j + arrayOfInt2[b] - arrayOfInt1[b] + 1;
/*     */         } 
/*     */ 
/*     */         
/* 365 */         if (j <= 2) {
/*     */           continue label106;
/*     */         }
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 372 */     double d1 = arrayOfDouble[i] + arrayOfDouble[i];
/* 373 */     double d2 = arrayOfDouble[1] + arrayOfDouble[1];
/* 374 */     for (b = 1; b <= i; b++) {
/*     */       
/* 376 */       int i6 = arrayOfInt3[b];
/* 377 */       int i7 = b + i6;
/* 378 */       if (i6 > 0) d2 = Math.max(d2, arrayOfDouble[b] + arrayOfDouble[i7 - 1]); 
/* 379 */       i7 = b + i6;
/* 380 */       if (i6 < i - b + 1) d1 = Math.min(d1, arrayOfDouble[b] + arrayOfDouble[i7]); 
/*     */     } 
/* 382 */     double d4 = (d1 + d2) / 4.0D;
/*     */     
/* 384 */     if (n < i1) return d4; 
/* 385 */     if (k == n) d4 = d2 / 2.0D; 
/* 386 */     if (k == n - 1) d4 = d1 / 2.0D; 
/* 387 */     return d4;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 463 */       int i = 500;
/* 464 */       double[] arrayOfDouble1 = new double[i];
/*     */ 
/*     */       
/* 467 */       i = arrayOfDouble1.length;
/* 468 */       Normal normal = new Normal(2.0D, 1.0D);
/*     */       
/* 470 */       normal.setSeed(123L); byte b;
/* 471 */       for (b = 0; b < i; ) { arrayOfDouble1[b] = normal.random(); b++; }
/*     */ 
/*     */       
/* 474 */       long l1 = System.currentTimeMillis();
/*     */       
/* 476 */       WilcoxonMedianCI wilcoxonMedianCI = new WilcoxonMedianCI(arrayOfDouble1, 0.95D, true);
/* 477 */       long l2 = System.currentTimeMillis();
/* 478 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/* 479 */       System.out.println("CI = [" + wilcoxonMedianCI.getLowerLimit() + ", " + wilcoxonMedianCI.getUpperLimit() + "]" + " d = " + wilcoxonMedianCI.getD() + " Point estimate = " + wilcoxonMedianCI.hodgesLehmannEstimate() + " Achieved confidence = " + wilcoxonMedianCI.getAchievedConfidence());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 494 */       double[] arrayOfDouble2 = { 51.0D, 48.0D, 52.0D, 62.0D, 64.0D, 51.0D, 55.0D, 60.0D };
/* 495 */       double[] arrayOfDouble3 = { 46.0D, 45.0D, 53.0D, 48.0D, 57.0D, 55.0D, 44.0D, 50.0D };
/*     */ 
/*     */       
/* 498 */       PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);
/* 499 */       double[] arrayOfDouble4 = { 0.89D, 0.9D, 0.92D, 0.94D, 0.95D, 0.96D, 0.98D, 0.99D };
/* 500 */       System.out.println("************* Suntan lotions: n = 8 *******************");
/* 501 */       for (b = 0; b < arrayOfDouble4.length; b++) {
/*     */         
/* 503 */         System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
/* 504 */         wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], false);
/* 505 */         System.out.println("Exact CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 512 */       double[] arrayOfDouble5 = { 54.5D, 70.6D, 85.6D, 78.2D, 69.6D, 73.1D, 97.5D, 85.6D, 74.9D, 86.8D, 53.6D, 89.4D };
/* 513 */       double[] arrayOfDouble6 = { 55.5D, 72.9D, 84.8D, 78.3D, 71.6D, 74.0D, 97.2D, 88.0D, 74.4D, 89.3D, 52.3D, 91.5D };
/* 514 */       pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
/* 515 */       System.out.println("*************** Weight change data: n = 12 ***************");
/* 516 */       for (b = 0; b < arrayOfDouble4.length; b++) {
/*     */         
/* 518 */         System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
/* 519 */         wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], false);
/* 520 */         System.out.println(" Exact CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));
/*     */         
/* 522 */         wilcoxonMedianCI = new WilcoxonMedianCI(pairedData, arrayOfDouble4[b], true);
/* 523 */         System.out.println("Approx CI=[" + wilcoxonMedianCI.getLowerLimit() + "," + wilcoxonMedianCI.getUpperLimit() + "]" + " d=" + wilcoxonMedianCI.getD() + " Achieved conf.=" + Maths.round(wilcoxonMedianCI.getAchievedConfidence(), 3));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/WilcoxonMedianCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */