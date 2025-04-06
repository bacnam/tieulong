/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.tests.H1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Normal
/*     */   extends AbstractDistribution
/*     */ {
/*  26 */   static final double SQRPI2 = Math.sqrt(6.283185307179586D);
/*     */   
/*     */   private double mean;
/*     */   
/*     */   private double sd;
/*     */ 
/*     */   
/*     */   public Normal() {
/*  34 */     this(0.0D, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Normal(double paramDouble1, double paramDouble2) {
/*  45 */     if (paramDouble2 <= 0.0D)
/*  46 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  47 */     this.mean = paramDouble1;
/*  48 */     this.sd = paramDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/*  59 */     return standardTailProb((paramDouble - this.mean) / this.sd, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumPdf() {
/*  66 */     return 1.0D / this.sd * SQRPI2;
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
/*  81 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  82 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  83 */     return this.mean + this.sd * inverseStandardCdf(paramDouble);
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
/*     */   public static double inverseStandardCdf(double paramDouble) {
/* 250 */     double d1, d2 = paramDouble - 0.5D;
/* 251 */     if (Math.abs(d2) <= 0.425D) {
/*     */       
/* 253 */       double d = 0.180625D - d2 * d2;
/* 254 */       d1 = d2 * (((((((2509.0809287301227D * d + 33430.57558358813D) * d + 67265.7709270087D) * d + 45921.95393154987D) * d + 13731.69376550946D) * d + 1971.5909503065513D) * d + 133.14166789178438D) * d + 3.3871328727963665D) / (((((((5226.495278852854D * d + 28729.085735721943D) * d + 39307.89580009271D) * d + 21213.794301586597D) * d + 5394.196021424751D) * d + 687.1870074920579D) * d + 42.31333070160091D) * d + 1.0D);
/*     */ 
/*     */ 
/*     */       
/* 258 */       return d1;
/*     */     } 
/*     */ 
/*     */     
/* 262 */     double d3 = (d2 < 0.0D) ? paramDouble : (1.0D - paramDouble);
/* 263 */     if (d3 <= 0.0D) {
/* 264 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/* 266 */     d3 = Math.sqrt(-Math.log(d3));
/*     */     
/* 268 */     if (d3 <= 5.0D) {
/*     */       
/* 270 */       d3 -= 1.6D;
/* 271 */       d1 = (((((((7.745450142783414E-4D * d3 + 0.022723844989269184D) * d3 + 0.2417807251774506D) * d3 + 1.2704582524523684D) * d3 + 3.6478483247632045D) * d3 + 5.769497221460691D) * d3 + 4.630337846156546D) * d3 + 1.4234371107496835D) / (((((((1.0507500716444169E-9D * d3 + 5.475938084995345E-4D) * d3 + 0.015198666563616457D) * d3 + 0.14810397642748008D) * d3 + 0.6897673349851D) * d3 + 1.6763848301838038D) * d3 + 2.053191626637759D) * d3 + 1.0D);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 278 */       d3 -= 5.0D;
/* 279 */       d1 = (((((((2.0103343992922881E-7D * d3 + 2.7115555687434876E-5D) * d3 + 0.0012426609473880784D) * d3 + 0.026532189526576124D) * d3 + 0.29656057182850487D) * d3 + 1.7848265399172913D) * d3 + 5.463784911164114D) * d3 + 6.657904643501103D) / (((((((2.0442631033899397E-15D * d3 + 1.421511758316446E-7D) * d3 + 1.8463183175100548E-5D) * d3 + 7.868691311456133E-4D) * d3 + 0.014875361290850615D) * d3 + 0.1369298809227358D) * d3 + 0.599832206555888D) * d3 + 1.0D);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     if (d2 < 0.0D) d1 = -d1; 
/* 286 */     return d1;
/*     */   }
/*     */   public double mean() {
/* 289 */     return this.mean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/* 299 */     double d = (paramDouble - this.mean) / this.sd;
/* 300 */     return Math.exp(-0.5D * d * d) / SQRPI2 * this.sd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double random() {
/* 311 */     return this.mean + this.sd * this.rand.nextGaussian();
/*     */   } public double sd() {
/* 313 */     return this.sd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMean(double paramDouble) {
/* 320 */     this.mean = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSd(double paramDouble) {
/* 330 */     if (paramDouble <= 0.0D)
/* 331 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 332 */     this.sd = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariance(double paramDouble) {
/* 343 */     if (paramDouble <= 0.0D)
/* 344 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 345 */     this.sd = Math.sqrt(paramDouble);
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
/*     */   public static double standardTailProb(double paramDouble, boolean paramBoolean) {
/*     */     double d1;
/* 393 */     boolean bool = paramBoolean;
/* 394 */     double d2 = paramDouble;
/* 395 */     if (d2 < 0.0D) { bool = !bool; d2 = -d2; }
/* 396 */      if (d2 <= 8.0D || (bool && d2 <= 37.0D)) {
/*     */       
/* 398 */       double d = 0.5D * d2 * d2;
/* 399 */       if (d2 >= 1.28D) {
/* 400 */         d1 = 0.398942280385D * Math.exp(-d) / (d2 - 3.8052E-8D + 1.00000615302D / (d2 + 3.98064794E-4D + 1.98615381364D / (d2 - 0.151679116635D + 5.29330324926D / (d2 + 4.8385912808D - 15.1508972451D / (d2 + 0.742380924027D + 30.789933034D / (d2 + 3.99019417011D))))));
/*     */       }
/*     */       else {
/*     */         
/* 404 */         d1 = 0.5D - d2 * (0.398942280444D - 0.399903438504D * d / (d + 5.75885480458D - 29.8213557808D / (d + 2.62433121679D + 48.6959930692D / (d + 5.92885724438D))));
/*     */       } 
/*     */     } else {
/*     */       
/* 408 */       d1 = 0.0D;
/* 409 */     }  if (!bool) d1 = 1.0D - d1; 
/* 410 */     return d1;
/*     */   }
/*     */   public String toString() {
/* 413 */     return new String("Normal distribution: mean = " + this.mean + ", sd = " + this.sd + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double upperTailProb(double paramDouble) {
/* 422 */     return standardTailProb((paramDouble - this.mean) / this.sd, true);
/*     */   } public double variance() {
/* 424 */     return this.sd * this.sd;
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
/* 447 */       char c = '✐';
/* 448 */       Normal normal = new Normal(-5.0D, 10.0D);
/* 449 */       double[] arrayOfDouble = new double[c];
/* 450 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 452 */         arrayOfDouble[b] = normal.random();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 457 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, normal, H1.NOT_EQUAL, true);
/* 458 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Normal.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */