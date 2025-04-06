/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.tests.H1;
/*     */ import jsc.util.Maths;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Gamma
/*     */   extends AbstractDistribution
/*     */ {
/*  33 */   private static final double ELIMIT = Math.log(Double.MIN_VALUE);
/*     */   
/*  35 */   private static final double OFLO = Math.sqrt(Double.MAX_VALUE);
/*     */ 
/*     */   
/*     */   private double A;
/*     */ 
/*     */   
/*     */   private double B;
/*     */   
/*     */   private double C;
/*     */   
/*     */   double logGammaShape;
/*     */   
/*     */   double logScale;
/*     */   
/*     */   double shape;
/*     */   
/*     */   double scale;
/*     */ 
/*     */   
/*     */   public Gamma(double paramDouble1, double paramDouble2) {
/*  55 */     if (paramDouble1 <= 0.0D || paramDouble2 <= 0.0D)
/*  56 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  57 */     this.shape = paramDouble1;
/*  58 */     this.scale = paramDouble2;
/*     */     
/*  60 */     this.logGammaShape = Maths.logGamma(paramDouble1);
/*  61 */     this.logScale = Math.log(paramDouble2);
/*     */ 
/*     */     
/*  64 */     if (paramDouble1 > 1.0D) {
/*     */       
/*  66 */       this.A = 1.0D / Math.sqrt(paramDouble1 + paramDouble1 - 1.0D);
/*  67 */       this.B = paramDouble1 - Math.log(4.0D);
/*  68 */       this.C = paramDouble1 + 1.0D / this.A;
/*     */     }
/*     */     else {
/*     */       
/*  72 */       this.A = 1.0D - paramDouble1;
/*  73 */       this.B = (paramDouble1 + Math.E) / Math.E;
/*  74 */       this.C = 1.0D / paramDouble1;
/*     */     } 
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
/*  88 */     return incompleteGamma(paramDouble / this.scale, this.shape);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double gamvar() {
/*     */     int i;
/* 116 */     if (this.shape < 4.0D && (i = (int)this.shape) == this.shape) {
/*     */ 
/*     */ 
/*     */       
/* 120 */       double d = 1.0D;
/* 121 */       for (byte b = 1; b <= i; ) { d *= 1.0D - this.rand.nextDouble(); b++; }
/* 122 */        return -Math.log(d);
/*     */     } 
/* 124 */     if (this.shape > 1.0D)
/*     */     {
/*     */       while (true) {
/*     */ 
/*     */         
/* 129 */         double d = this.rand.nextDouble(); if (d != 0.0D) {
/* 130 */           double d2 = 1.0D - this.rand.nextDouble();
/*     */ 
/*     */           
/* 133 */           double d3 = this.A * Math.log(d / (1.0D - d));
/* 134 */           double d1 = this.shape * Math.exp(d3);
/*     */ 
/*     */           
/* 137 */           if (this.B + this.C * d3 - d1 >= Math.log(d * d * d2)) {
/* 138 */             return d1;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     while (true) {
/* 145 */       double d3 = this.B * this.rand.nextDouble();
/* 146 */       double d2 = -Math.log(1.0D - this.rand.nextDouble());
/* 147 */       if (d3 > 1.0D) {
/*     */         
/* 149 */         double d = -Math.log((this.B - d3) / this.shape);
/* 150 */         if (this.A * Math.log(d) <= d2) return d;
/*     */         
/*     */         continue;
/*     */       } 
/* 154 */       double d1 = Math.pow(d3, this.C);
/* 155 */       if (d1 <= d2) return d1;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getScale() {
/* 166 */     return this.scale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getShape() {
/* 173 */     return this.shape;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double incompleteGamma(double paramDouble1, double paramDouble2) {
/* 199 */     double d9 = 0.0D;
/*     */ 
/*     */     
/* 202 */     if (paramDouble2 <= 0.0D || paramDouble1 < 0.0D) {
/* 203 */       throw new IllegalArgumentException("Invalid argument of incomplete gamma integral.");
/*     */     }
/* 205 */     if (paramDouble1 == 0.0D) return 0.0D;
/*     */ 
/*     */     
/* 208 */     if (paramDouble2 > 1000.0D) {
/*     */       
/* 210 */       double d = 3.0D * Math.sqrt(paramDouble2) * (Math.pow(paramDouble1 / paramDouble2, 0.3333333333333333D) + 1.0D / 9.0D * paramDouble2 - 1.0D);
/* 211 */       return Normal.standardTailProb(d, false);
/*     */     } 
/*     */ 
/*     */     
/* 215 */     if (paramDouble1 > 1000000.0D) return 1.0D;
/*     */     
/* 217 */     if (paramDouble1 <= 1.0D || paramDouble1 < paramDouble2)
/*     */     
/*     */     { 
/*     */ 
/*     */       
/* 222 */       double d10 = paramDouble2 * Math.log(paramDouble1) - paramDouble1 - Maths.logGamma(paramDouble2 + 1.0D);
/* 223 */       double d11 = 1.0D;
/* 224 */       d9 = 1.0D;
/* 225 */       double d12 = paramDouble2;
/*     */       
/*     */       while (true)
/* 228 */       { d12++;
/* 229 */         d11 *= paramDouble1 / d12;
/* 230 */         d9 += d11;
/* 231 */         if (d11 <= 1.0E-17D)
/* 232 */         { d10 += Math.log(d9);
/* 233 */           d9 = 0.0D;
/* 234 */           if (d10 >= ELIMIT) d9 = Math.exp(d10);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 280 */           return d9; }  }  }  double d5 = paramDouble2 * Math.log(paramDouble1) - paramDouble1 - Maths.logGamma(paramDouble2); double d7 = 1.0D - paramDouble2; double d8 = d7 + paramDouble1 + 1.0D; double d6 = 0.0D; double d1 = 1.0D; double d2 = paramDouble1; double d3 = paramDouble1 + 1.0D; double d4 = paramDouble1 * d8; d9 = d3 / d4; while (true) { d7++; d8 += 2.0D; d6++; double d12 = d7 * d6; double d10 = d8 * d3 - d12 * d1; double d11 = d8 * d4 - d12 * d2; if (Math.abs(d11) > 0.0D) { double d = d10 / d11; if (Math.abs(d9 - d) <= Math.min(1.0E-17D, 1.0E-17D * d)) break;  d9 = d; }  d1 = d3; d2 = d4; d3 = d10; d4 = d11; if (Math.abs(d10) >= OFLO) { d1 /= OFLO; d2 /= OFLO; d3 /= OFLO; d4 /= OFLO; }  }  d5 += Math.log(d9); d9 = 1.0D; if (d5 >= ELIMIT) d9 = 1.0D - Math.exp(d5);  return d9;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 295 */     return 0.5D * this.scale * ChiSquared.inverseCdf(paramDouble, this.shape + this.shape, this.logGammaShape);
/*     */   }
/*     */   public double mean() {
/* 298 */     return this.shape * this.scale;
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
/* 309 */     if (paramDouble < 0.0D)
/* 310 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 311 */     return Math.exp((this.shape - 1.0D) * Math.log(paramDouble) - paramDouble / this.scale - this.logGammaShape - this.shape * this.logScale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double random() {
/* 321 */     return this.scale * gamvar();
/*     */   }
/*     */   public String toString() {
/* 324 */     return new String("Gamma distribution: shape = " + this.shape + ", scale = " + this.scale + ".");
/*     */   } public double variance() {
/* 326 */     return this.shape * this.scale * this.scale;
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
/* 337 */       double d1 = 50.0D;
/* 338 */       double d2 = 20.0D;
/* 339 */       Gamma gamma = new Gamma(d1, d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 349 */       char c = '✐';
/*     */ 
/*     */ 
/*     */       
/* 353 */       gamma = new Gamma(0.01D, 1.0D);
/*     */ 
/*     */       
/* 356 */       double[] arrayOfDouble = new double[c];
/* 357 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = gamma.random(); b++; }
/*     */       
/* 359 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, gamma, H1.NOT_EQUAL, false);
/* 360 */       System.out.println("n = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Gamma.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */