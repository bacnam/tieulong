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
/*     */ public class Cauchy
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double location;
/*     */   private double scale;
/*     */   
/*     */   public Cauchy(double paramDouble1, double paramDouble2) {
/*  28 */     if (paramDouble2 <= 0.0D)
/*  29 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  30 */     this.location = paramDouble1;
/*  31 */     this.scale = paramDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cauchy() {
/*  38 */     this(0.0D, 1.0D);
/*     */   } public double cdf(double paramDouble) {
/*  40 */     return 0.5D + Math.atan((paramDouble - this.location) / this.scale) / Math.PI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLocation() {
/*  47 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  54 */     return this.scale;
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
/*  66 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  67 */       throw new IllegalArgumentException("Invalid probability."); 
/*  68 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  69 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  70 */     return this.location + this.scale * Math.tan(Math.PI * (paramDouble - 0.5D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/*  78 */     return Double.NaN;
/*     */   }
/*     */   
/*     */   public double pdf(double paramDouble) {
/*  82 */     double d = (paramDouble - this.location) / this.scale;
/*  83 */     return 1.0D / Math.PI * this.scale * (1.0D + d * d);
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/*     */     while (true) {
/*  89 */       double d = this.rand.nextGaussian(); if (d != 0.0D)
/*  90 */         return this.location + this.scale * this.rand.nextGaussian() / d; 
/*     */     } 
/*     */   }
/*     */   public String toString() {
/*  94 */     return new String("Cauchy distribution: location = " + this.location + ", scale = " + this.scale + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 101 */     return Double.NaN;
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
/* 112 */       double d1 = 2.0D;
/* 113 */       double d2 = 3.0D;
/* 114 */       Cauchy cauchy = new Cauchy(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       int i = 100000;
/* 123 */       cauchy = new Cauchy(2.0D, 3.0D);
/* 124 */       double[] arrayOfDouble = new double[i];
/* 125 */       for (byte b = 0; b < i; ) { arrayOfDouble[b] = cauchy.random(); b++; }
/*     */ 
/*     */       
/* 128 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, cauchy, H1.NOT_EQUAL, false);
/* 129 */       System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Cauchy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */