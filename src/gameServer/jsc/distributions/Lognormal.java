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
/*     */ public class Lognormal
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private Normal N;
/*     */   
/*     */   public Lognormal() {
/*  23 */     this(0.0D, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lognormal(double paramDouble1, double paramDouble2) {
/*  32 */     this.N = new Normal(paramDouble1, paramDouble2);
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
/*  43 */     if (paramDouble < 0.0D)
/*  44 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  45 */     if (paramDouble == 0.0D) return 0.0D; 
/*  46 */     return this.N.cdf(Math.log(paramDouble));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLocation() {
/*  54 */     return this.N.mean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  61 */     return this.N.sd();
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
/*  73 */     if (paramDouble == 0.0D) return 0.0D; 
/*  74 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  75 */     return Math.exp(this.N.inverseCdf(paramDouble));
/*     */   }
/*     */   public double mean() {
/*  78 */     return Math.exp(this.N.mean() + 0.5D * this.N.variance());
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
/*  89 */     if (paramDouble < 0.0D)
/*  90 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  91 */     if (paramDouble == 0.0D) return 0.0D; 
/*  92 */     return this.N.pdf(Math.log(paramDouble)) / paramDouble;
/*     */   }
/*     */   public double random() {
/*  95 */     return Math.exp(this.N.random());
/*     */   }
/*     */   public String toString() {
/*  98 */     return new String("Lognormal distribution: location = " + this.N.mean() + ", scale = " + this.N.sd() + ".");
/*     */   }
/*     */   
/*     */   public double variance() {
/* 102 */     double d = this.N.mean() + this.N.variance();
/* 103 */     return Math.exp(d + d) - Math.exp(this.N.mean() + d);
/*     */   }
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
/* 115 */       double d1 = 5.0D;
/* 116 */       double d2 = 0.6D;
/* 117 */       Lognormal lognormal = new Lognormal(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       char c = '✐';
/*     */ 
/*     */ 
/*     */       
/* 139 */       lognormal = new Lognormal(-10.0D, 99.0D);
/* 140 */       double[] arrayOfDouble = new double[c];
/* 141 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = lognormal.random(); b++; }
/*     */ 
/*     */       
/* 144 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, lognormal, H1.NOT_EQUAL, false);
/* 145 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Lognormal.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */