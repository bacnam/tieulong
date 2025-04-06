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
/*     */ public class Pareto
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double location;
/*     */   private double shape;
/*     */   private double RECIPC;
/*     */   private double CAC;
/*     */   
/*     */   public Pareto(double paramDouble1, double paramDouble2) {
/*  29 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/*  30 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  31 */     this.location = paramDouble1;
/*  32 */     this.shape = paramDouble2;
/*  33 */     this.RECIPC = 1.0D / paramDouble2;
/*  34 */     this.CAC = paramDouble2 * Math.pow(paramDouble1, paramDouble2);
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
/*  46 */     if (paramDouble < this.location)
/*  47 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  48 */     return 1.0D - Math.pow(this.location / paramDouble, this.shape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLocation() {
/*  56 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getShape() {
/*  63 */     return this.shape;
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
/*  75 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  76 */       throw new IllegalArgumentException("Invalid probability."); 
/*  77 */     if (paramDouble == 0.0D) return this.location; 
/*  78 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  79 */     return this.location / Math.pow(1.0D - paramDouble, this.RECIPC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/*  89 */     return (this.shape > 1.0D) ? (this.shape * this.location / (this.shape - 1.0D)) : Double.NaN;
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
/* 100 */     if (paramDouble < this.location)
/* 101 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 102 */     return this.CAC / Math.pow(paramDouble, this.shape + 1.0D);
/*     */   }
/*     */   
/*     */   public double random() {
/* 106 */     return this.location / Math.pow(1.0D - this.rand.nextDouble(), this.RECIPC);
/*     */   }
/*     */   public String toString() {
/* 109 */     return new String("Pareto distribution: location = " + this.location + ", shape = " + this.shape + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 119 */     return (this.shape > 2.0D) ? (this.shape * this.location * this.location / (this.shape - 1.0D) * (this.shape - 1.0D) * (this.shape - 2.0D)) : Double.NaN;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 139 */       char c = '✐';
/* 140 */       Pareto pareto = new Pareto(1.0D, 2.0D);
/* 141 */       double[] arrayOfDouble = new double[c];
/* 142 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 144 */         arrayOfDouble[b] = pareto.random();
/*     */       }
/*     */ 
/*     */       
/* 148 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, pareto, H1.NOT_EQUAL, true);
/* 149 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Pareto.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */