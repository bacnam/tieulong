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
/*     */ public class Logistic
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double mean;
/*     */   private double scale;
/*     */   
/*     */   public Logistic() {
/*  19 */     this(0.0D, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logistic(double paramDouble1, double paramDouble2) {
/*  30 */     if (paramDouble2 <= 0.0D)
/*  31 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  32 */     this.mean = paramDouble1;
/*  33 */     this.scale = paramDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/*  39 */     if (paramDouble > this.mean) {
/*  40 */       return 1.0D / (1.0D + Math.exp(-(paramDouble - this.mean) / this.scale));
/*     */     }
/*  42 */     return 1.0D - 1.0D / (1.0D + Math.exp((paramDouble - this.mean) / this.scale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  50 */     return this.scale;
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
/*     */   public double inverseCdf(double paramDouble) {
/*  63 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  64 */       throw new IllegalArgumentException("Invalid probability."); 
/*  65 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  66 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  67 */     return this.mean + this.scale * Math.log(paramDouble / (1.0D - paramDouble));
/*     */   }
/*     */   public double mean() {
/*  70 */     return this.mean;
/*     */   }
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/*  75 */     double d = 1.0D + ((paramDouble > this.mean) ? Math.exp(-(paramDouble - this.mean) / this.scale) : Math.exp((paramDouble - this.mean) / this.scale));
/*  76 */     return (d - 1.0D) / this.scale * d * d;
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/*     */     while (true) {
/*  82 */       double d = this.rand.nextDouble(); if (d != 0.0D)
/*  83 */         return this.mean + this.scale * Math.log(d / (1.0D - d)); 
/*     */     } 
/*     */   }
/*     */   public String toString() {
/*  87 */     return new String("Laplace distribution: mean = " + this.mean + ", scale = " + this.scale + ".");
/*     */   } public double variance() {
/*  89 */     return 9.869604401089358D * this.scale * this.scale / 3.0D;
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
/* 100 */       double d1 = 2.0D;
/* 101 */       double d2 = 3.0D;
/* 102 */       Logistic logistic = new Logistic(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       char c = '✐';
/*     */       
/* 112 */       logistic = new Logistic(2.0D, 3.0D);
/* 113 */       double[] arrayOfDouble = new double[c];
/* 114 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = logistic.random(); b++; }
/*     */ 
/*     */       
/* 117 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, logistic, H1.NOT_EQUAL, false);
/* 118 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Logistic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */