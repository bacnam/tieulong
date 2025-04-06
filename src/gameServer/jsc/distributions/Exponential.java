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
/*     */ 
/*     */ public class Exponential
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double mean;
/*     */   
/*     */   public Exponential(double paramDouble) {
/*  30 */     setMean(paramDouble);
/*     */   }
/*     */   public Exponential() {
/*  33 */     this(1.0D);
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
/*  44 */     if (paramDouble < 0.0D)
/*  45 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  46 */     return 1.0D - Math.exp(-paramDouble / this.mean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumPdf() {
/*  54 */     return 1.0D / this.mean;
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
/*  68 */     if (paramDouble == 0.0D) return 0.0D; 
/*  69 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  70 */     return -this.mean * Math.log(1.0D - paramDouble);
/*     */   }
/*     */   public double mean() {
/*  73 */     return this.mean;
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
/*  84 */     if (paramDouble < 0.0D)
/*  85 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  86 */     return Math.exp(-paramDouble / this.mean) / this.mean;
/*     */   }
/*     */   public double random() {
/*  89 */     return -this.mean * Math.log(1.0D - this.rand.nextDouble());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double sd() {
/*  96 */     return this.mean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMean(double paramDouble) {
/* 106 */     if (paramDouble <= 0.0D)
/* 107 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 108 */     this.mean = paramDouble;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 112 */     return new String("Exponential distribution: mean = " + this.mean + ".");
/*     */   } public double variance() {
/* 114 */     return this.mean * this.mean;
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
/* 125 */       double d = 2.0D;
/* 126 */       Exponential exponential = new Exponential(d);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       int i = 100000;
/* 135 */       double[] arrayOfDouble = new double[i];
/* 136 */       for (byte b = 0; b < i; ) { arrayOfDouble[b] = exponential.random(); b++; }
/*     */ 
/*     */       
/* 139 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, exponential, H1.NOT_EQUAL, false);
/* 140 */       System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Exponential.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */