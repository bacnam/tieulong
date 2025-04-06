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
/*     */ 
/*     */ 
/*     */ public class Geometric
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double p;
/*     */   private double LOGQ;
/*     */   private double LOGP;
/*     */   
/*     */   public Geometric(double paramDouble) {
/*  30 */     setP(paramDouble);
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
/*  41 */     if (paramDouble < 0.0D)
/*  42 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  43 */     return 1.0D - Math.pow(1.0D - this.p, paramDouble + 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getP() {
/*  51 */     return this.p;
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
/*  64 */     if (paramDouble < 0.0D || paramDouble >= 1.0D)
/*  65 */       throw new IllegalArgumentException("Invalid probability."); 
/*  66 */     return Math.round(Math.log(1.0D - paramDouble) / this.LOGQ - 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/*  74 */     return true;
/*     */   } public double mean() {
/*  76 */     return this.p / (1.0D - this.p);
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
/*  87 */     if (paramDouble < 0.0D)
/*  88 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  89 */     return this.p * Math.pow(1.0D - this.p, paramDouble);
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
/*     */   public double random() {
/* 103 */     return Math.max(0.0D, Math.ceil(Math.log(1.0D - this.rand.nextDouble()) / this.LOGQ - 1.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setP(double paramDouble) {
/* 114 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 115 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 116 */     this.p = paramDouble;
/* 117 */     this.LOGQ = Math.log(1.0D - paramDouble);
/* 118 */     this.LOGP = Math.log(paramDouble);
/*     */   }
/*     */   public String toString() {
/* 121 */     return new String("Geometric distribution: p = " + this.p + ".");
/*     */   } public double variance() {
/* 123 */     return this.p / (1.0D - this.p) * (1.0D - this.p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 133 */       double d = 0.25D;
/* 134 */       Geometric geometric = new Geometric(d);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       char c = '✐';
/* 143 */       geometric = new Geometric(0.25D);
/*     */ 
/*     */       
/* 146 */       int[] arrayOfInt = new int[c];
/* 147 */       for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)geometric.random(); b++; }
/*     */       
/* 149 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), geometric, 0);
/* 150 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 151 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Geometric.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */