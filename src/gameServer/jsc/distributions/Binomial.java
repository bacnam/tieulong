/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.goodnessfit.ChiSquaredFitTest;
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
/*     */ public class Binomial
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   private long n;
/*     */   private double p;
/*     */   private double plog;
/*     */   private double pclog;
/*     */   private double oldg;
/*     */   
/*     */   public Binomial(long paramLong, double paramDouble) {
/*  33 */     super(0L, paramLong);
/*  34 */     setN(paramLong);
/*  35 */     setP(paramDouble);
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
/*  47 */     if (paramDouble < 0.0D || paramDouble > this.n) {
/*  48 */       throw new IllegalArgumentException("Invalid variate-value.");
/*     */     }
/*     */     
/*  51 */     double d = this.n - paramDouble;
/*  52 */     if (paramDouble == this.n) {
/*  53 */       return 1.0D;
/*     */     }
/*  55 */     return 1.0D - Beta.incompleteBeta(this.p, paramDouble + 1.0D, d, Maths.lnB(paramDouble + 1.0D, d));
/*     */   }
/*     */   
/*     */   public long getN() {
/*  59 */     return this.n;
/*     */   }
/*     */   public double getP() {
/*  62 */     return this.p;
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
/*  74 */     if (paramDouble < 0.0D || paramDouble > 1.0D) {
/*  75 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/*  77 */     double d1 = 0.0D;
/*     */ 
/*     */ 
/*     */     
/*  81 */     if (paramDouble == 1.0D) return this.n; 
/*  82 */     if (this.n < 26L) {
/*     */ 
/*     */ 
/*     */       
/*  86 */       for (; d1 < this.n && cdf(d1) < paramDouble - 1.0E-8D; d1++);
/*  87 */       return d1;
/*     */     } 
/*  89 */     if (this.p < 0.1D) {
/*     */       
/*  91 */       Poisson poisson = new Poisson(this.n * this.p);
/*  92 */       d1 = poisson.inverseCdf(paramDouble);
/*     */     }
/*     */     else {
/*     */       
/*  96 */       Normal normal = new Normal(this.n * this.p, Math.sqrt(this.n * this.p * (1.0D - this.p)));
/*  97 */       d1 = Math.floor(normal.inverseCdf(paramDouble) + 0.5D);
/*     */     } 
/*  99 */     if (d1 < 0.0D) d1 = 0.0D; 
/* 100 */     if (d1 > this.n) d1 = this.n; 
/* 101 */     double d2 = cdf(d1);
/*     */ 
/*     */     
/* 104 */     while (d2 > paramDouble && d1 > 0.0D) {
/* 105 */       d1--; d2 = cdf(d1);
/* 106 */     }  while (d2 < paramDouble && d1 < this.n) {
/* 107 */       d1++; d2 = cdf(d1);
/* 108 */     }  return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 118 */     return this.n * this.p;
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
/* 129 */     if (paramDouble < 0.0D || paramDouble > this.n) {
/* 130 */       return 0.0D;
/*     */     }
/* 132 */     return Math.exp(Maths.logBinomialCoefficient(this.n, (long)paramDouble) + paramDouble * Math.log(this.p) + (this.n - paramDouble) * Math.log(1.0D - this.p));
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
/*     */   public double random() {
/* 148 */     double d3, d1 = this.p;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     double d4 = (d1 <= 0.5D) ? d1 : (1.0D - d1);
/*     */ 
/*     */ 
/*     */     
/* 157 */     double d2 = this.n * d4;
/* 158 */     if (this.n < 25L)
/*     */     
/*     */     { 
/*     */       
/* 162 */       d3 = 0.0D;
/* 163 */       for (byte b = 1; b <= this.n; b++) {
/* 164 */         if (this.rand.nextDouble() < d4) d3++;
/*     */ 
/*     */ 
/*     */         
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */        }
/*     */     
/*     */     else
/*     */     
/*     */     { 
/*     */ 
/*     */ 
/*     */       
/* 187 */       double d5 = 1.0D - d4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 194 */       double d6 = Math.sqrt(2.0D * d2 * d5);
/*     */       
/*     */       while (true)
/* 197 */       { double d8 = Math.PI * this.rand.nextDouble();
/* 198 */         double d9 = Math.tan(d8);
/* 199 */         double d7 = d6 * d9 + d2;
/* 200 */         if (d7 >= 0.0D && d7 < this.n + 1.0D)
/* 201 */         { d7 = Math.floor(d7);
/* 202 */           double d = 1.2D * d6 * (1.0D + d9 * d9) * Math.exp(this.oldg - Maths.logGamma(d7 + 1.0D) - Maths.logGamma(this.n - d7 + 1.0D) + d7 * this.plog + (this.n - d7) * this.pclog);
/*     */           
/* 204 */           if (this.rand.nextDouble() <= d)
/* 205 */           { d3 = d7; } else { continue; }  }
/*     */         else { continue; }
/* 207 */          if (d4 != d1) d3 = this.n - d3; 
/* 208 */         return d3; }  }  if (d4 != d1) d3 = this.n - d3;  return d3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setN(long paramLong) {
/* 219 */     if (paramLong < 1L)
/* 220 */       throw new IllegalArgumentException("Invalid number of trials."); 
/* 221 */     this.n = paramLong;
/* 222 */     this.maxValue = paramLong;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     this.oldg = Maths.logFactorial(paramLong);
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
/*     */   public void setP(double paramDouble) {
/* 239 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 240 */       throw new IllegalArgumentException("Invalid probability of success."); 
/* 241 */     this.p = paramDouble;
/*     */ 
/*     */     
/* 244 */     double d1 = paramDouble;
/* 245 */     double d2 = (d1 <= 0.5D) ? d1 : (1.0D - d1);
/* 246 */     double d3 = 1.0D - d2;
/* 247 */     this.plog = Math.log(d2);
/* 248 */     this.pclog = Math.log(d3);
/*     */   }
/*     */   public String toString() {
/* 251 */     return new String("Binomial distribution: n = " + this.n + ", p = " + this.p + ".");
/*     */   } public double variance() {
/* 253 */     return this.n * this.p * (1.0D - this.p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 263 */       long l1 = 1000L;
/* 264 */       double d1 = 0.99D;
/* 265 */       long l2 = 9L;
/* 266 */       double d2 = 0.9D;
/* 267 */       Binomial binomial = new Binomial(l1, d1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 278 */       int i = 1000000;
/*     */ 
/*     */       
/* 281 */       binomial = new Binomial(25L, 0.038461538461538464D);
/*     */ 
/*     */ 
/*     */       
/* 285 */       int[] arrayOfInt = new int[i];
/* 286 */       for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)binomial.random(); b++; }
/*     */       
/* 288 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), binomial, 0);
/* 289 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 290 */       System.out.println("m = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Binomial.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */