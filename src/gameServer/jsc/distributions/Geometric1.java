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
/*     */ public class Geometric1
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double p;
/*     */   private double LOGQ;
/*     */   private double LOGP;
/*     */   
/*     */   public Geometric1(double paramDouble) {
/*  26 */     setP(paramDouble);
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
/*  37 */     if (paramDouble < 1.0D)
/*  38 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  39 */     return 1.0D - Math.pow(1.0D - this.p, paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getP() {
/*  47 */     return this.p;
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
/*  60 */     if (paramDouble < 0.0D || paramDouble >= 1.0D)
/*  61 */       throw new IllegalArgumentException("Invalid probability."); 
/*  62 */     return Math.round(Math.log(1.0D - paramDouble) / this.LOGQ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/*  70 */     return true;
/*     */   } public double mean() {
/*  72 */     return 1.0D / this.p;
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
/*  83 */     if (paramDouble < 1.0D)
/*  84 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  85 */     return this.p * Math.pow(1.0D - this.p, paramDouble - 1.0D);
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
/*  99 */     return Math.max(0.0D, Math.ceil(Math.log(1.0D - this.rand.nextDouble()) / this.LOGQ));
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
/* 110 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 111 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 112 */     this.p = paramDouble;
/* 113 */     this.LOGQ = Math.log(1.0D - paramDouble);
/* 114 */     this.LOGP = Math.log(paramDouble);
/*     */   }
/*     */   public String toString() {
/* 117 */     return new String("Geometric distribution: p = " + this.p + ".");
/*     */   } public double variance() {
/* 119 */     return (1.0D - this.p) / this.p * this.p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 129 */       double d = 0.05D;
/* 130 */       Geometric1 geometric1 = new Geometric1(d);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 138 */       char c = '✐';
/*     */ 
/*     */       
/* 141 */       geometric1 = new Geometric1(0.001D);
/* 142 */       int[] arrayOfInt = new int[c];
/* 143 */       for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)geometric1.random(); b++; }
/*     */       
/* 145 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), geometric1, 0);
/* 146 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 147 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Geometric1.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */