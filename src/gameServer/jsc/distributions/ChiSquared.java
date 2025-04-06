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
/*     */ public class ChiSquared
/*     */   extends Gamma
/*     */ {
/*     */   private double df;
/*     */   private static final int MAXIT = 20;
/*     */   private static final double E = 5.0E-17D;
/*     */   
/*     */   public ChiSquared(double paramDouble) {
/*  46 */     super(0.5D * paramDouble, 2.0D);
/*  47 */     this.df = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf() {
/*  55 */     return this.df;
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
/*  67 */     return inverseCdf(paramDouble, this.df, this.logGammaShape);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double inverseCdf(double paramDouble1, double paramDouble2, double paramDouble3) {
/*     */     double d2, d3;
/* 144 */     if (paramDouble1 < 0.0D) throw new IllegalArgumentException("Invalid probability."); 
/* 145 */     if (paramDouble1 > 0.999998D) return Double.POSITIVE_INFINITY;
/*     */ 
/*     */ 
/*     */     
/* 149 */     if (paramDouble2 <= 0.0D) {
/* 150 */       throw new IllegalArgumentException("Invalid distribution parameter.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 155 */     double d4 = 0.5D * paramDouble2;
/* 156 */     double d1 = d4 - 1.0D;
/*     */ 
/*     */     
/* 159 */     if (paramDouble2 >= -(1.24D * Math.log(paramDouble1))) {
/*     */ 
/*     */       
/* 162 */       if (paramDouble2 > 0.32D) {
/*     */         
/* 164 */         double d7 = Normal.inverseStandardCdf(paramDouble1);
/*     */ 
/*     */         
/* 167 */         double d6 = 0.222222D / paramDouble2;
/* 168 */         d2 = paramDouble2 * Math.pow(d7 * Math.sqrt(d6) + 1.0D - d6, 3.0D);
/*     */ 
/*     */         
/* 171 */         if (d2 > 2.2D * paramDouble2 + 6.0D) {
/* 172 */           d2 = -2.0D * (Math.log(1.0D - paramDouble1) - d1 * Math.log(0.5D * d2) + paramDouble3);
/*     */         }
/*     */       } else {
/*     */         
/* 176 */         d2 = 0.4D;
/* 177 */         double d = Math.log(1.0D - paramDouble1);
/*     */         
/*     */         do {
/* 180 */           d3 = d2;
/* 181 */           double d6 = 1.0D + d2 * (4.67D + d2);
/* 182 */           double d7 = d2 * (6.73D + d2 * (6.66D + d2));
/* 183 */           double d8 = -0.5D + (4.67D + d2 + d2) / d6 - (6.73D + d2 * (13.32D + 3.0D * d2)) / d7;
/* 184 */           d2 -= (1.0D - Math.exp(d + paramDouble3 + 0.5D * d2 + d1 * 0.6931471806D) * d7 / d6) / d8;
/* 185 */         } while (Math.abs(d3 / d2 - 1.0D) > 0.01D);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 190 */       d2 = Math.pow(paramDouble1 * d4 * Math.exp(paramDouble3 + d4 * 0.6931471806D), 1.0D / d4);
/*     */ 
/*     */ 
/*     */       
/* 194 */       if (d2 < 5.0E-17D) return d2; 
/*     */     } 
/* 196 */     double d5 = d2;
/* 197 */     byte b = 0;
/*     */     
/*     */     do {
/* 200 */       d3 = d2;
/* 201 */       double d8 = 0.5D * d2;
/* 202 */       double d9 = paramDouble1 - Gamma.incompleteGamma(d8, d4);
/*     */ 
/*     */       
/* 205 */       double d16 = d9 * Math.exp(d4 * 0.6931471806D + paramDouble3 + d8 - d1 * Math.log(d2));
/* 206 */       double d7 = d16 / d2;
/* 207 */       double d6 = 0.5D * d16 - d7 * d1;
/* 208 */       double d10 = (210.0D + d6 * (140.0D + d6 * (105.0D + d6 * (84.0D + d6 * (70.0D + 60.0D * d6))))) / 420.0D;
/* 209 */       double d11 = (420.0D + d6 * (735.0D + d6 * (966.0D + d6 * (1141.0D + 1278.0D * d6)))) / 2520.0D;
/* 210 */       double d12 = (210.0D + d6 * (462.0D + d6 * (707.0D + 932.0D * d6))) / 2520.0D;
/* 211 */       double d13 = (252.0D + d6 * (672.0D + 1182.0D * d6) + d1 * (294.0D + d6 * (889.0D + 1740.0D * d6))) / 5040.0D;
/* 212 */       double d14 = (84.0D + 264.0D * d6 + d1 * (175.0D + 606.0D * d6)) / 2520.0D;
/* 213 */       double d15 = (120.0D + d1 * (346.0D + 127.0D * d1)) / 5040.0D;
/* 214 */       d2 += d16 * (1.0D + 0.5D * d16 * d10 - d7 * d1 * (d10 - d7 * (d11 - d7 * (d12 - d7 * (d13 - d7 * (d14 - d7 * d15))))));
/*     */ 
/*     */ 
/*     */       
/* 218 */       if (Double.isNaN(d2) || d2 < 0.0D)
/*     */       {
/*     */         
/* 221 */         return d5;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 229 */     while (Math.abs(d3 / d2 - 1.0D) > 5.0E-17D && b++ < 20);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     return d2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 246 */     return new String("Chi-squared distribution: df = " + this.df + ".");
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
/*     */   public static double upperTailProb(double paramDouble1, double paramDouble2) {
/* 258 */     return 1.0D - Gamma.incompleteGamma(0.5D * paramDouble1, 0.5D * paramDouble2);
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
/* 278 */       double d = 50.0D;
/* 279 */       ChiSquared chiSquared = new ChiSquared(d);
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
/* 295 */       int i = 100000;
/*     */ 
/*     */ 
/*     */       
/* 299 */       chiSquared = new ChiSquared(100.0D);
/* 300 */       double[] arrayOfDouble = new double[i];
/* 301 */       for (byte b = 0; b < i; ) { arrayOfDouble[b] = chiSquared.random(); b++; }
/*     */ 
/*     */       
/* 304 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, chiSquared, H1.NOT_EQUAL, false);
/* 305 */       System.out.println("n = " + i + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/ChiSquared.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */