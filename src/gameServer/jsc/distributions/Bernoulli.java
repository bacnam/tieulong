/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.goodnessfit.ChiSquaredFitTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bernoulli
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   private double p;
/*     */   
/*     */   public Bernoulli(double paramDouble) {
/*  26 */     super(0L, 1L);
/*  27 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/*  28 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  29 */     this.p = paramDouble;
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
/*     */   public double cdf(double paramDouble) {
/*  42 */     if (paramDouble == 0.0D)
/*  43 */       return 1.0D - this.p; 
/*  44 */     if (paramDouble == 1.0D) {
/*  45 */       return 1.0D;
/*     */     }
/*  47 */     throw new IllegalArgumentException("Invalid variate-value.");
/*     */   }
/*     */   
/*     */   public double getP() {
/*  51 */     return this.p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/*  58 */     return this.p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double inverseCdf(double paramDouble) {
/*  69 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  70 */       throw new IllegalArgumentException("Invalid probability."); 
/*  71 */     return ((paramDouble <= 1.0D - this.p) ? false : true);
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
/*     */   public double pdf(double paramDouble) {
/*  90 */     if (paramDouble == 0.0D)
/*  91 */       return 1.0D - this.p; 
/*  92 */     if (paramDouble == 1.0D) {
/*  93 */       return this.p;
/*     */     }
/*  95 */     throw new IllegalArgumentException("Invalid variate-value.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double random() {
/* 104 */     return ((this.rand.nextDouble() < this.p) ? true : false);
/*     */   } public String toString() {
/* 106 */     return new String("Bernoulli distribution: p = " + this.p + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 113 */     return this.p * (1.0D - this.p);
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
/* 125 */       double d2 = 0.5D;
/* 126 */       double d1 = 0.7D;
/* 127 */       boolean bool = false;
/* 128 */       Bernoulli bernoulli = new Bernoulli(d1);
/* 129 */       System.out.println("Bernoulli distribution: p = " + d1);
/* 130 */       System.out.println("p.m.f. at X = " + bool + ") = " + bernoulli.pdf(bool));
/* 131 */       System.out.println("Probability(X <= " + bool + ") = " + bernoulli.cdf(bool));
/* 132 */       System.out.println("x such that Probability(X <= x) = " + d2 + ") = " + bernoulli.inverseCdf(d2));
/*     */       
/* 134 */       int i = 100000;
/* 135 */       int[] arrayOfInt = new int[i];
/* 136 */       for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)bernoulli.random(); b++; }
/*     */       
/* 138 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), bernoulli, 0);
/* 139 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 140 */       System.out.println("n = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Bernoulli.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */