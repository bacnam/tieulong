/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.tests.H1;
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
/*     */ public class Weibull
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double LOGC;
/*     */   private double LOGB;
/*     */   private double RECIPC;
/*     */   private double scale;
/*     */   private double shape;
/*     */   
/*     */   public Weibull(double paramDouble1, double paramDouble2) {
/*  34 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/*  35 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  36 */     this.scale = paramDouble1;
/*  37 */     this.shape = paramDouble2;
/*  38 */     this.LOGC = Math.log(paramDouble2);
/*  39 */     this.LOGB = Math.log(paramDouble1);
/*  40 */     this.RECIPC = 1.0D / paramDouble2;
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
/*  52 */     if (paramDouble < 0.0D)
/*  53 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  54 */     return 1.0D - Math.exp(-Math.pow(paramDouble / this.scale, this.shape));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  62 */     return this.scale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getShape() {
/*  69 */     return this.shape;
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
/*     */   public double inverseCdf(double paramDouble) {
/*  81 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  82 */       throw new IllegalArgumentException("Invalid probability."); 
/*  83 */     if (paramDouble == 0.0D) return 0.0D;
/*     */     
/*  85 */     return this.scale * Math.pow(Math.log(1.0D / (1.0D - paramDouble)), this.RECIPC);
/*     */   }
/*     */   public double mean() {
/*  88 */     return Math.exp(this.LOGB + Maths.logGamma(1.0D + this.RECIPC));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/*  99 */     if (paramDouble < 0.0D)
/* 100 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 101 */     double d1 = Math.log(paramDouble);
/* 102 */     double d2 = this.shape * (d1 - this.LOGB);
/* 103 */     return Math.exp(this.LOGC - d1 + d2 - Math.exp(d2));
/*     */   }
/*     */   
/*     */   public double random() {
/* 107 */     return this.scale * Math.pow(-Math.log(1.0D - this.rand.nextDouble()), this.RECIPC);
/*     */   } public String toString() {
/* 109 */     return new String("Weibull distribution: scale = " + this.scale + ", shape = " + this.shape + ".");
/*     */   }
/*     */   
/*     */   public double variance() {
/* 113 */     return Math.exp(this.LOGB + this.LOGB + Maths.logGamma(1.0D + this.RECIPC + this.RECIPC)) - Math.exp(2.0D * Maths.logGamma(1.0D + this.RECIPC));
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
/* 137 */       char c = '✐';
/*     */       
/* 139 */       Weibull weibull = new Weibull(5.0D, 0.5D);
/* 140 */       double[] arrayOfDouble = new double[c];
/* 141 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 143 */         arrayOfDouble[b] = weibull.random();
/*     */       }
/*     */ 
/*     */       
/* 147 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, weibull, H1.NOT_EQUAL, true);
/* 148 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Weibull.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */