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
/*     */ public class Uniform
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double a;
/*     */   private double b;
/*     */   
/*     */   public Uniform() {
/*  18 */     this(0.0D, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Uniform(double paramDouble1, double paramDouble2) {
/*  27 */     setInterval(paramDouble1, paramDouble2);
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
/*  38 */     if (paramDouble < this.a || paramDouble > this.b)
/*  39 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  40 */     return (paramDouble - this.a) / (this.b - this.a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getA() {
/*  48 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/*  55 */     return this.b;
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
/*  66 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  67 */       throw new IllegalArgumentException("Invalid probability."); 
/*  68 */     return this.a + (this.b - this.a) * paramDouble;
/*     */   }
/*     */   public double mean() {
/*  71 */     return 0.5D * (this.a + this.b);
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
/*     */   public double pdf(double paramDouble) {
/*  84 */     if (paramDouble < this.a || paramDouble > this.b)
/*  85 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  86 */     return 1.0D / (this.b - this.a);
/*     */   }
/*     */   public double random() {
/*  89 */     return this.a + (this.b - this.a) * this.rand.nextDouble();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(double paramDouble1, double paramDouble2) {
/* 100 */     if (paramDouble2 <= paramDouble1)
/* 101 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 102 */     this.a = paramDouble1;
/* 103 */     this.b = paramDouble2;
/*     */   }
/*     */   public String toString() {
/* 106 */     return new String("Uniform distribution: a = " + this.a + ", b = " + this.b + ".");
/*     */   } public double variance() {
/* 108 */     return (this.b - this.a) * (this.b - this.a) / 12.0D;
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
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 130 */       char c = '✐';
/* 131 */       Uniform uniform = new Uniform(10.0D, 20.0D);
/* 132 */       double[] arrayOfDouble = new double[c];
/* 133 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 135 */         arrayOfDouble[b] = uniform.random();
/*     */       }
/*     */ 
/*     */       
/* 139 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, uniform, H1.NOT_EQUAL, true);
/* 140 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Uniform.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */