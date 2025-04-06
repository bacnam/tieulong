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
/*     */ public class ExtremeValue
/*     */   extends AbstractDistribution
/*     */ {
/*     */   static final double C1 = 1.6449340668482264D;
/*  17 */   static final double C2 = Math.sqrt(1.6449340668482264D);
/*     */ 
/*     */ 
/*     */   
/*     */   private double location;
/*     */ 
/*     */ 
/*     */   
/*     */   private double scale;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtremeValue(double paramDouble1, double paramDouble2) {
/*  31 */     if (paramDouble2 <= 0.0D)
/*  32 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  33 */     this.location = paramDouble1;
/*  34 */     this.scale = paramDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/*  43 */     return Math.exp(-Math.exp(-(paramDouble - this.location) / this.scale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLocation() {
/*  50 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/*  57 */     return this.scale;
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
/*  70 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  71 */       throw new IllegalArgumentException("Invalid probability."); 
/*  72 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  73 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY; 
/*  74 */     return this.location - this.scale * Math.log(Math.log(1.0D / paramDouble));
/*     */   }
/*     */   public double mean() {
/*  77 */     return this.location + this.scale * 0.577215664901533D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/*  87 */     double d = -(paramDouble - this.location) / this.scale;
/*  88 */     return Math.exp(d) * Math.exp(-Math.exp(d)) / this.scale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double random() {
/*  97 */     return this.location - this.scale * Math.log(-Math.log(1.0D - this.rand.nextDouble()));
/*     */   } public double sd() {
/*  99 */     return this.scale * C2;
/*     */   }
/*     */   public String toString() {
/* 102 */     return new String("Extreme value distribution: location = " + this.location + ", scale = " + this.scale + ".");
/*     */   } public double variance() {
/* 104 */     return this.scale * this.scale * 1.6449340668482264D;
/*     */   }
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
/* 117 */       double d1 = 5.0D, d2 = 1.5D;
/* 118 */       ExtremeValue extremeValue = new ExtremeValue(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       char c = '✐';
/* 127 */       extremeValue = new ExtremeValue(5.0D, 1.5D);
/* 128 */       double[] arrayOfDouble = new double[c];
/* 129 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = extremeValue.random(); b++; }
/*     */ 
/*     */       
/* 132 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, extremeValue, H1.NOT_EQUAL, false);
/* 133 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/ExtremeValue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */