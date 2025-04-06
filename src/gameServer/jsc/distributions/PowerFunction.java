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
/*     */ public class PowerFunction
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double scale;
/*     */   private double shape;
/*     */   private double BC;
/*     */   private double RECIPC;
/*     */   
/*     */   public PowerFunction(double paramDouble1, double paramDouble2) {
/*  29 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/*  30 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  31 */     this.scale = paramDouble1;
/*  32 */     this.shape = paramDouble2;
/*  33 */     this.RECIPC = 1.0D / paramDouble2;
/*  34 */     this.BC = Math.pow(paramDouble1, paramDouble2);
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
/*  46 */     if (paramDouble < 0.0D || paramDouble > this.scale)
/*  47 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  48 */     return Math.pow(paramDouble / this.scale, this.shape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  56 */     return this.scale;
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
/*  77 */     if (paramDouble == 0.0D) return 0.0D; 
/*  78 */     if (paramDouble == 1.0D) return this.scale; 
/*  79 */     return this.scale * Math.pow(paramDouble, this.RECIPC);
/*     */   }
/*     */   public double mean() {
/*  82 */     return this.scale * this.shape / (this.shape + 1.0D);
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
/*  93 */     if (paramDouble < 0.0D || paramDouble > this.scale)
/*  94 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  95 */     return this.shape * Math.pow(paramDouble, this.shape - 1.0D) / this.BC;
/*     */   }
/*     */   public double random() {
/*  98 */     return this.scale * Math.pow(this.rand.nextDouble(), this.RECIPC);
/*     */   }
/*     */   public String toString() {
/* 101 */     return new String("Power function distribution: scale = " + this.scale + ", shape = " + this.shape + ".");
/*     */   } public double variance() {
/* 103 */     return this.scale * this.scale * this.shape / (this.shape + 2.0D) * (this.shape + 1.0D) * (this.shape + 1.0D);
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
/* 123 */       char c = '✐';
/*     */       
/* 125 */       PowerFunction powerFunction = new PowerFunction(2.0D, 0.5D);
/* 126 */       double[] arrayOfDouble = new double[c];
/* 127 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 129 */         arrayOfDouble[b] = powerFunction.random();
/*     */       }
/*     */ 
/*     */       
/* 133 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, powerFunction, H1.NOT_EQUAL, true);
/* 134 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/PowerFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */