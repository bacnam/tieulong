/*     */ package jsc.distributions;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ public class Beta
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private static final double ACU63 = 5.0E-17D;
/*     */   private static final double ACU109 = 1.0E-28D;
/*     */   private static final double LOWER = 1.0E-4D;
/*     */   private static final double UPPER = 0.9999D;
/*     */   private static final double CONST1 = 2.30753D;
/*     */   private static final double CONST2 = 0.27061D;
/*     */   private static final double CONST3 = 0.99229D;
/*     */   private static final double CONST4 = 0.04481D;
/*     */   private double p;
/*     */   private double q;
/*     */   private double logB;
/*     */   private static final int GR = 0;
/*     */   private static final int OS = 1;
/*     */   private static final int AS134 = 2;
/*     */   private static final int ATKINSON79_5_2 = 52;
/*     */   private static final double TOL = 1.0E-14D;
/*     */   private int randomMethod;
/*     */   private boolean reverseOrder = false;
/*     */   private int n;
/*     */   private double am1;
/*     */   private double bm1;
/*     */   private double arecip;
/*     */   private double brecip;
/*     */   private double t;
/*     */   private double r;
/*     */   private Gamma Gp;
/*     */   private Gamma Gq;
/*     */   private double pStar;
/*     */   private double qStar;
/*     */   private double s1;
/*     */   private double s2;
/*     */   
/*     */   public Beta(double paramDouble1, double paramDouble2) {
/*  72 */     setShape(paramDouble1, paramDouble2);
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
/*     */   public double cdf(double paramDouble) {
/*  84 */     return incompleteBeta(paramDouble, this.p, this.q, this.logB);
/*     */   }
/*     */   
/*     */   public double getP() {
/*  88 */     return this.p;
/*     */   }
/*     */   public double getQ() {
/*  91 */     return this.q;
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
/*     */   public static double incompleteBeta(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/*     */     boolean bool;
/* 116 */     double d3, d5, d9, d10 = paramDouble1;
/*     */ 
/*     */     
/* 119 */     if (paramDouble2 <= 0.0D || paramDouble3 <= 0.0D || paramDouble1 < 0.0D || paramDouble1 > 1.0D) {
/* 120 */       throw new IllegalArgumentException("Invalid argument of incomplete beta function ratio.");
/*     */     }
/* 122 */     if (paramDouble1 == 0.0D || paramDouble1 == 1.0D) return d10;
/*     */ 
/*     */     
/* 125 */     double d4 = paramDouble2 + paramDouble3;
/* 126 */     double d2 = 1.0D - paramDouble1;
/* 127 */     if (paramDouble2 >= d4 * paramDouble1) {
/*     */       
/* 129 */       d9 = paramDouble1;
/* 130 */       d3 = paramDouble2;
/* 131 */       d5 = paramDouble3;
/* 132 */       bool = false;
/*     */     }
/*     */     else {
/*     */       
/* 136 */       d9 = d2;
/* 137 */       d2 = paramDouble1;
/* 138 */       d3 = paramDouble3;
/* 139 */       d5 = paramDouble2;
/* 140 */       bool = true;
/*     */     } 
/* 142 */     double d8 = 1.0D;
/* 143 */     double d1 = 1.0D;
/* 144 */     d10 = 1.0D;
/* 145 */     int i = (int)(d5 + d2 * d4);
/*     */ 
/*     */     
/* 148 */     double d6 = d9 / d2;
/* 149 */     double d7 = d5 - d1;
/* 150 */     if (i == 0) d6 = d9; 
/*     */     while (true) {
/* 152 */       d8 = d8 * d7 * d6 / (d3 + d1);
/* 153 */       d10 += d8;
/* 154 */       d7 = Math.abs(d8);
/* 155 */       if (d7 <= 5.0E-17D && d7 <= 5.0E-17D * d10)
/* 156 */         break;  d1++;
/* 157 */       i--;
/* 158 */       if (i >= 0) {
/*     */         
/* 160 */         d7 = d5 - d1;
/* 161 */         if (i == 0) d6 = d9; 
/*     */         continue;
/*     */       } 
/* 164 */       d7 = d4; d4++;
/*     */     } 
/*     */ 
/*     */     
/* 168 */     d10 = d10 * Math.exp(d3 * Math.log(d9) + (d5 - 1.0D) * Math.log(d2) - paramDouble4) / d3;
/* 169 */     if (bool) d10 = 1.0D - d10; 
/* 170 */     return d10;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 185 */     return inverseIncompleteBeta(this.p, this.q, this.logB, paramDouble);
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
/*     */   public static double inverseIncompleteBeta(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/*     */     boolean bool;
/* 216 */     double d1, d2, d4, d10 = paramDouble4;
/*     */ 
/*     */     
/* 219 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D || paramDouble4 < 0.0D || paramDouble4 > 1.0D) {
/* 220 */       throw new IllegalArgumentException("Invalid argument of inverse of incomplete beta function ratio.");
/*     */     }
/* 222 */     if (paramDouble4 == 0.0D || paramDouble4 == 1.0D) return paramDouble4;
/*     */ 
/*     */     
/* 225 */     if (paramDouble4 <= 0.5D) {
/*     */       
/* 227 */       d1 = paramDouble4;
/* 228 */       d2 = paramDouble1;
/* 229 */       d4 = paramDouble2;
/* 230 */       bool = false;
/*     */     }
/*     */     else {
/*     */       
/* 234 */       d1 = 1.0D - paramDouble4;
/* 235 */       d2 = paramDouble2;
/* 236 */       d4 = paramDouble1;
/* 237 */       bool = true;
/*     */     } 
/*     */ 
/*     */     
/* 241 */     double d5 = Math.sqrt(-Math.log(d1 * d1));
/* 242 */     double d8 = d5 - (2.30753D + 0.27061D * d5) / (1.0D + (0.99229D + 0.04481D * d5) * d5);
/* 243 */     if (d2 > 1.0D && d4 > 1.0D) {
/*     */       
/* 245 */       d5 = (d8 * d8 - 3.0D) / 6.0D;
/* 246 */       double d12 = 1.0D / (d2 + d2 - 1.0D);
/* 247 */       double d13 = 1.0D / (d4 + d4 - 1.0D);
/* 248 */       double d11 = 2.0D / (d12 + d13);
/* 249 */       double d14 = d8 * Math.sqrt(d11 + d5) / d11 - (d13 - d12) * (d5 + 0.8333333333333334D - 2.0D / 3.0D * d11);
/* 250 */       d10 = d2 / (d2 + d4 * Math.exp(d14 + d14));
/*     */     }
/*     */     else {
/*     */       
/* 254 */       d5 = d4 + d4;
/* 255 */       double d = 1.0D / 9.0D * d4;
/* 256 */       d = d5 * Math.pow(1.0D - d + d8 * Math.sqrt(d), 3.0D);
/* 257 */       if (d <= 0.0D) {
/*     */         
/* 259 */         d10 = 1.0D - Math.exp((Math.log((1.0D - d1) * d4) + paramDouble3) / d4);
/*     */       } else {
/*     */         
/* 262 */         d = (4.0D * d2 + d5 - 2.0D) / d;
/* 263 */         if (d <= 1.0D) {
/*     */           
/* 265 */           d10 = Math.exp((Math.log(d1 * d2) + paramDouble3) / d2);
/*     */         } else {
/* 267 */           d10 = 1.0D - 2.0D / (d + 1.0D);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 272 */     d5 = 1.0D - d2;
/* 273 */     double d7 = 1.0D - d4;
/* 274 */     double d9 = 0.0D;
/* 275 */     double d6 = 1.0D;
/* 276 */     double d3 = 1.0D;
/* 277 */     if (d10 < 1.0E-4D) d10 = 1.0E-4D; 
/* 278 */     if (d10 > 0.9999D) d10 = 0.9999D;
/*     */ 
/*     */     
/*     */     label65: while (true) {
/* 282 */       d8 = incompleteBeta(d10, d2, d4, paramDouble3);
/* 283 */       d8 = (d8 - d1) * Math.exp(paramDouble3 + d5 * Math.log(d10) + d7 * Math.log(1.0D - d10));
/* 284 */       if (d8 * d9 <= 0.0D) d3 = d6; 
/* 285 */       double d = 1.0D;
/*     */ 
/*     */       
/*     */       while (true) {
/* 289 */         double d11 = d * d8;
/* 290 */         d6 = d11 * d11;
/* 291 */         if (d6 >= d3) {
/* 292 */           d /= 3.0D; continue;
/* 293 */         }  double d12 = d10 - d11;
/* 294 */         if (d12 >= 0.0D && d12 <= 1.0D) {
/*     */           
/* 296 */           if (d3 <= 1.0E-28D || d8 * d8 <= 1.0E-28D)
/* 297 */             return (bool == true) ? (1.0D - d10) : d10; 
/* 298 */           if (d12 == 0.0D || d12 == 1.0D) {
/* 299 */             d /= 3.0D; continue;
/* 300 */           }  if (d12 == d10)
/* 301 */             return (bool == true) ? (1.0D - d10) : d10; 
/* 302 */           d10 = d12;
/* 303 */           d9 = d8;
/*     */           
/*     */           continue label65;
/*     */         } 
/* 307 */         d /= 3.0D;
/*     */       } 
/*     */       break;
/*     */     } 
/*     */   }
/*     */   public double mean() {
/* 313 */     return this.p / (this.p + this.q);
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
/*     */   public double pdf(double paramDouble) {
/* 329 */     if ((paramDouble > 0.0D && paramDouble < 1.0D) || (paramDouble == 0.0D && this.p >= 1.0D) || (paramDouble == 1.0D && this.q >= 1.0D)) {
/*     */       
/* 331 */       if (paramDouble == 0.0D)
/* 332 */         return (this.p == 1.0D) ? this.q : 0.0D; 
/* 333 */       if (paramDouble == 1.0D) {
/* 334 */         return (this.q == 1.0D) ? this.p : 0.0D;
/*     */       }
/* 336 */       return Math.exp((this.p - 1.0D) * Math.log(paramDouble) + (this.q - 1.0D) * Math.log(1.0D - paramDouble) - this.logB);
/*     */     } 
/*     */     
/* 339 */     throw new IllegalArgumentException("Invalid variate-value.");
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
/*     */   private double random52() {
/*     */     while (true) {
/* 387 */       double d2 = this.rand.nextDouble();
/* 388 */       double d3 = this.rand.nextDouble();
/* 389 */       if (d2 > this.r) {
/*     */         
/* 391 */         double d6 = 1.0D - (1.0D - this.t) * Math.pow((1.0D - d2) / (1.0D - this.r), 1.0D / this.qStar);
/*     */         
/* 393 */         double d5 = Math.pow(d6, this.p - 1.0D);
/* 394 */         if (this.s2 * d3 <= d5) return d6;
/*     */         
/*     */         continue;
/*     */       } 
/* 398 */       double d4 = this.t * Math.pow(d2 / this.r, 1.0D / this.pStar);
/*     */       
/* 400 */       double d1 = Math.pow(1.0D - d4, this.q - 1.0D);
/* 401 */       if (this.s1 * d3 <= d1) return d4;
/*     */     
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
/*     */   public double random() {
/* 425 */     if (this.randomMethod == 0) {
/*     */       
/* 427 */       double d1 = this.Gp.random();
/* 428 */       double d2 = d1 + this.Gq.random();
/*     */       
/* 430 */       if (d2 == 0.0D) return 1.0D;
/*     */       
/* 432 */       return d1 / d2;
/*     */     } 
/*     */     
/* 435 */     if (this.randomMethod == 2)
/*     */     {
/*     */       while (true) {
/*     */ 
/*     */         
/* 440 */         double d2 = this.rand.nextDouble();
/* 441 */         double d3 = -Math.log(1.0D - this.rand.nextDouble());
/* 442 */         if (d2 > this.r) {
/*     */           
/* 444 */           double d = 1.0D - (1.0D - this.t) * Math.pow((1.0D - d2) / (1.0D - this.r), this.brecip);
/* 445 */           if (-this.am1 * Math.log(d / this.t) <= d3) return this.reverseOrder ? (1.0D - d) : d;
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */         
/* 453 */         double d1 = this.t * Math.pow(d2 / this.r, this.arecip);
/* 454 */         if (-this.bm1 * Math.log(1.0D - d1) <= d3) return this.reverseOrder ? (1.0D - d1) : d1;
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (this.randomMethod == 52) return random52();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     double[] arrayOfDouble = new double[this.n];
/* 467 */     for (byte b = 0; b < this.n; ) { arrayOfDouble[b] = this.rand.nextDouble(); b++; }
/*     */     
/* 469 */     Arrays.sort(arrayOfDouble);
/* 470 */     return arrayOfDouble[(int)this.p - 1];
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
/*     */   private void tOpt(double paramDouble1, double paramDouble2) {
/*     */     double d1, d2, d6;
/* 484 */     if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
/* 485 */       throw new IllegalArgumentException("Alpha out of range."); 
/* 486 */     if (paramDouble2 <= 1.0D)
/* 487 */       throw new IllegalArgumentException("Beta out of range."); 
/* 488 */     this.am1 = paramDouble1 - 1.0D;
/* 489 */     double d5 = this.am1 / (this.am1 - paramDouble2);
/* 490 */     if (1.0D - d5 * d5 >= 1.0D)
/* 491 */       throw new IllegalArgumentException("Alpha too near 1 or beta too large."); 
/* 492 */     this.bm1 = paramDouble2 - 1.0D;
/* 493 */     this.arecip = 1.0D / paramDouble1;
/* 494 */     this.brecip = 1.0D / paramDouble2;
/*     */ 
/*     */     
/* 497 */     double d7 = 0.0D;
/* 498 */     double d3 = this.am1;
/* 499 */     double d8 = 1.0D;
/* 500 */     double d4 = paramDouble2;
/*     */ 
/*     */     
/*     */     while (true) {
/* 504 */       d6 = (d7 * d4 - d8 * d3) / (d4 - d3);
/* 505 */       d1 = 1.0D - d6;
/* 506 */       d2 = paramDouble2 * d6;
/*     */       
/* 508 */       double d = d2 + Math.pow(d1, this.bm1) * (this.am1 * d1 - d2);
/* 509 */       if (Math.abs(d) < 1.0E-14D)
/* 510 */         break;  if (d * d3 < 0.0D) {
/*     */         
/* 512 */         d8 = d7;
/* 513 */         d4 = d3;
/* 514 */         d7 = d6;
/* 515 */         d3 = d;
/*     */         
/*     */         continue;
/*     */       } 
/* 519 */       d7 = d6;
/* 520 */       d3 = d;
/* 521 */       d4 /= 1.0D;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 526 */     this.t = d6;
/* 527 */     this.r = d2 / (d2 + paramDouble1 * Math.pow(d1, paramDouble2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 533 */     this.rand.setSeed(paramLong);
/* 534 */     if (this.randomMethod == 0) {
/*     */       
/* 536 */       this.Gp.setSeed(this.rand.nextLong());
/* 537 */       this.Gq.setSeed(this.rand.nextLong() + 1L);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double S(double paramDouble1, double paramDouble2) {
/* 544 */     double d1 = paramDouble1 - 1.0D;
/* 545 */     double d2 = paramDouble2 - 1.0D;
/* 546 */     double d3 = paramDouble1 + paramDouble2 - 2.0D;
/* 547 */     return Math.pow(d1, d1) * Math.pow(d2, d2) / Math.pow(d3, d3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShape(double paramDouble1, double paramDouble2) {
/* 558 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/* 559 */       throw new IllegalArgumentException("Invalid shape parameter."); 
/* 560 */     this.p = paramDouble1;
/* 561 */     this.q = paramDouble2;
/* 562 */     this.logB = Maths.lnB(paramDouble1, paramDouble2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 567 */     if (paramDouble1 == Math.floor(paramDouble1) && paramDouble2 == Math.floor(paramDouble2) && paramDouble1 + paramDouble2 < 10.0D) {
/* 568 */       this.randomMethod = 1; this.n = (int)(paramDouble1 + paramDouble2 - 1.0D);
/*     */     }
/* 570 */     else if (paramDouble1 < 1.0D && paramDouble2 > 1.0D) {
/* 571 */       this.randomMethod = 2; this.reverseOrder = false; tOpt(paramDouble1, paramDouble2);
/* 572 */     } else if (paramDouble1 > 1.0D && paramDouble2 < 1.0D) {
/* 573 */       this.randomMethod = 2; this.reverseOrder = true; tOpt(paramDouble2, paramDouble1);
/* 574 */     } else if (paramDouble1 < 1.0D && paramDouble2 < 1.0D) {
/*     */       
/* 576 */       this.randomMethod = 52;
/* 577 */       this.pStar = paramDouble1; this.qStar = paramDouble2;
/* 578 */       this.t = Math.sqrt(paramDouble1 * (1.0D - paramDouble1)) / (Math.sqrt(paramDouble1 * (1.0D - paramDouble1)) + Math.sqrt(paramDouble2 * (1.0D - paramDouble2)));
/* 579 */       this.s1 = Math.pow(1.0D - this.t, paramDouble2 - 1.0D);
/* 580 */       this.s2 = Math.pow(this.t, paramDouble1 - 1.0D);
/* 581 */       this.r = paramDouble2 * this.t / (paramDouble2 * this.t + paramDouble1 * (1.0D - this.t));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 610 */       this.randomMethod = 0;
/* 611 */       this.Gp = new Gamma(paramDouble1, 1.0D);
/* 612 */       this.Gq = new Gamma(paramDouble2, 1.0D);
/* 613 */       this.Gq.setSeed(this.rand.nextLong());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 618 */     return new String("Beta distribution: p = " + this.p + ", q = " + this.q + ".");
/*     */   } public double variance() {
/* 620 */     return this.p * this.q / (this.p + this.q + 1.0D) * (this.p + this.q) * (this.p + this.q);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 640 */       int i = 10000000;
/* 641 */       double d1 = 50.1D;
/* 642 */       double d2 = 50.1D;
/* 643 */       Beta beta = new Beta(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 658 */       long l1 = System.currentTimeMillis();
/* 659 */       for (byte b = 0; b < i; ) { beta.random(); b++; }
/* 660 */        long l2 = System.currentTimeMillis();
/* 661 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Beta.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */