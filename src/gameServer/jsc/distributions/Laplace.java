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
/*     */ public class Laplace
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double mean;
/*     */   private double scale;
/*     */   
/*     */   public Laplace() {
/*  21 */     this(0.0D, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Laplace(double paramDouble1, double paramDouble2) {
/*  32 */     if (paramDouble2 <= 0.0D)
/*  33 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  34 */     this.mean = paramDouble1;
/*  35 */     this.scale = paramDouble2;
/*     */   }
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/*  40 */     if (paramDouble >= this.mean) {
/*  41 */       return 0.5D + 0.5D * (1.0D - Math.exp((this.mean - paramDouble) / this.scale));
/*     */     }
/*  43 */     return 0.5D - 0.5D * (1.0D - Math.exp((paramDouble - this.mean) / this.scale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  51 */     return this.scale;
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
/*  64 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  65 */       throw new IllegalArgumentException("Invalid probability."); 
/*  66 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  67 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  68 */     if (paramDouble > 0.5D) {
/*  69 */       return this.mean - this.scale * Math.log(2.0D - paramDouble - paramDouble);
/*     */     }
/*  71 */     return this.mean + this.scale * Math.log(paramDouble + paramDouble);
/*     */   }
/*     */   public double mean() {
/*  74 */     return this.mean;
/*     */   } public double pdf(double paramDouble) {
/*  76 */     return 0.5D * Math.exp(-Math.abs(paramDouble - this.mean) / this.scale) / this.scale;
/*     */   }
/*     */   public double random() {
/*  79 */     return this.mean - this.scale * Math.log((1.0D - this.rand.nextDouble()) / (1.0D - this.rand.nextDouble()));
/*     */   }
/*     */   public String toString() {
/*  82 */     return new String("Laplace distribution: mean = " + this.mean + ", scale = " + this.scale + ".");
/*     */   } public double variance() {
/*  84 */     return 2.0D * this.scale * this.scale;
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
/*  95 */       double d1 = 5.0D;
/*  96 */       double d2 = 10.0D;
/*  97 */       Laplace laplace = new Laplace(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       char c = '✐';
/* 106 */       laplace = new Laplace(5.0D, 10.0D);
/* 107 */       double[] arrayOfDouble = new double[c];
/* 108 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = laplace.random(); b++; }
/*     */ 
/*     */       
/* 111 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, laplace, H1.NOT_EQUAL, false);
/* 112 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Laplace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */