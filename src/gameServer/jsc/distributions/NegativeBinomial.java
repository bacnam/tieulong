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
/*     */ 
/*     */ 
/*     */ public class NegativeBinomial
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private long n;
/*     */   private double p;
/*     */   private Poisson poisson;
/*     */   private double LOG1MP;
/*     */   private double LOGP;
/*     */   
/*     */   public NegativeBinomial(long paramLong, double paramDouble) {
/*  35 */     if (paramLong < 1L || paramDouble <= 0.0D || paramDouble >= 1.0D)
/*  36 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/*  37 */     this.n = paramLong;
/*  38 */     this.p = paramDouble;
/*  39 */     this.poisson = new Poisson(paramLong * (1.0D - paramDouble) / paramDouble);
/*  40 */     this.LOG1MP = Math.log(1.0D - paramDouble);
/*  41 */     this.LOGP = Math.log(paramDouble);
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
/*  53 */     if (paramDouble < 0.0D)
/*  54 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  55 */     return Beta.incompleteBeta(this.p, this.n, paramDouble + 1.0D, Maths.lnB(this.n, paramDouble + 1.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getN() {
/*  63 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getP() {
/*  70 */     return this.p;
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
/*  82 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D) {
/*  83 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/*     */     
/*  86 */     double d2 = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     d2 = this.poisson.inverseCdf(paramDouble);
/*  92 */     double d1 = cdf(d2);
/*     */ 
/*     */     
/*  95 */     while (d1 > paramDouble && d2 > 0.0D) {
/*  96 */       d2--; d1 = cdf(d2);
/*  97 */     }  while (d1 < paramDouble) {
/*  98 */       d2++; d1 = cdf(d2);
/*  99 */     }  return d2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/* 107 */     return true;
/*     */   } public double mean() {
/* 109 */     return this.n * (1.0D - this.p) / this.p;
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
/* 120 */     if (paramDouble < 0.0D)
/* 121 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 122 */     return Math.exp(Maths.logBinomialCoefficient((long)(this.n + paramDouble - 1.0D), (long)paramDouble) + this.n * this.LOGP + paramDouble * this.LOG1MP);
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
/*     */   public double random() {
/* 144 */     byte b2 = 0;
/* 145 */     double d1 = 0.0D;
/* 146 */     double d2 = 0.0D;
/*     */     
/* 148 */     if (this.p > 0.6D) {
/*     */       while (true) {
/*     */         
/* 151 */         if (this.rand.nextDouble() < this.p) {
/* 152 */           b2++;
/*     */         } else {
/* 154 */           d1++;
/* 155 */         }  if (b2 >= this.n) {
/* 156 */           return d1;
/*     */         }
/*     */       } 
/*     */     }
/* 160 */     for (byte b1 = 1; b1 <= this.n; b1++)
/* 161 */       d2 += Math.ceil(Math.log(1.0D - this.rand.nextDouble()) / this.LOG1MP); 
/* 162 */     return Math.max(0.0D, Math.floor(d2 - this.n));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 166 */     return new String("Negative binomial distribution: n = " + this.n + ", p = " + this.p + ".");
/*     */   } public double variance() {
/* 168 */     return this.n * (1.0D - this.p) / this.p * this.p;
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
/* 179 */       long l = 40L;
/* 180 */       double d = 0.9D;
/* 181 */       NegativeBinomial negativeBinomial = new NegativeBinomial(l, d);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       negativeBinomial = new NegativeBinomial(10L, 0.4D);
/* 192 */       char c = '✐';
/* 193 */       int[] arrayOfInt = new int[c];
/* 194 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 196 */         arrayOfInt[b] = (int)negativeBinomial.random();
/*     */       }
/*     */       
/* 199 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), negativeBinomial, 0);
/* 200 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 201 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/NegativeBinomial.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */