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
/*     */ public class Poisson
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double mean;
/*     */   private double alxm;
/*     */   private double gg;
/*     */   private double sq;
/*     */   
/*     */   public Poisson(double paramDouble) {
/*  25 */     setMean(paramDouble);
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
/*  36 */     if (paramDouble < 0.0D)
/*  37 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  38 */     return 1.0D - Gamma.incompleteGamma(this.mean, 1.0D + paramDouble);
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
/*  51 */     if (paramDouble < 0.0D || paramDouble > 1.0D) {
/*  52 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/*     */     
/*  55 */     double d = 0.0D;
/*     */ 
/*     */     
/*  58 */     if (this.mean < 20.0D) {
/*     */ 
/*     */ 
/*     */       
/*  62 */       for (; cdf(d) < paramDouble - 1.0E-8D; d++);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  68 */       Normal normal = new Normal(this.mean, Math.sqrt(this.mean));
/*  69 */       d = Math.floor(normal.inverseCdf(paramDouble) + 0.5D);
/*  70 */       if (d < 0.0D) d = 0.0D; 
/*  71 */       double d1 = cdf(d);
/*     */       
/*  73 */       while (d1 > paramDouble && d > 0.0D) {
/*  74 */         d--; d1 = cdf(d);
/*  75 */       }  while (d1 < paramDouble) {
/*  76 */         d++; d1 = cdf(d);
/*     */       } 
/*  78 */     }  return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/*  86 */     return true;
/*     */   } public double mean() {
/*  88 */     return this.mean;
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
/*  99 */     if (paramDouble < 0.0D)
/* 100 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 101 */     return Math.exp(paramDouble * Math.log(this.mean) - this.mean - Maths.logFactorial((long)paramDouble));
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
/*     */   public double random() {
/*     */     double d;
/* 121 */     if (this.mean < 12.0D) {
/*     */       
/* 123 */       d = -1.0D;
/* 124 */       double d1 = 1.0D;
/*     */       do {
/* 126 */         d++;
/* 127 */         d1 *= this.rand.nextDouble();
/* 128 */       } while (d1 > this.gg);
/*     */     }
/*     */     else {
/*     */       
/*     */       while (true)
/*     */       
/* 134 */       { double d1 = Math.tan(Math.PI * this.rand.nextDouble());
/* 135 */         d = this.sq * d1 + this.mean;
/* 136 */         if (d >= 0.0D)
/* 137 */         { d = Math.floor(d);
/* 138 */           double d2 = 0.9D * (1.0D + d1 * d1) * Math.exp(d * this.alxm - Maths.logGamma(d + 1.0D) - this.gg);
/* 139 */           if (this.rand.nextDouble() <= d2)
/*     */             break;  }  } 
/* 141 */     }  return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMean(double paramDouble) {
/* 152 */     if (paramDouble <= 0.0D)
/* 153 */       throw new IllegalArgumentException("Invalid Poisson parameter."); 
/* 154 */     this.mean = paramDouble;
/*     */ 
/*     */     
/* 157 */     if (paramDouble < 12.0D) {
/* 158 */       this.gg = Math.exp(-paramDouble);
/*     */     } else {
/*     */       
/* 161 */       this.sq = Math.sqrt(2.0D * paramDouble);
/* 162 */       this.alxm = Math.log(paramDouble);
/* 163 */       this.gg = paramDouble * this.alxm - Maths.logGamma(paramDouble + 1.0D);
/*     */     } 
/*     */   }
/*     */   public String toString() {
/* 167 */     return new String("Poisson distribution: mean = " + this.mean + ".");
/*     */   } public double variance() {
/* 169 */     return this.mean;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 190 */       int i = 1000000;
/*     */       
/* 192 */       Poisson poisson = new Poisson(15.0D);
/* 193 */       int[] arrayOfInt = new int[i];
/* 194 */       for (byte b = 0; b < i; ) { arrayOfInt[b] = (int)poisson.random(); b++; }
/*     */       
/* 196 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), poisson, 0);
/* 197 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 198 */       System.out.println("m = " + i + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Poisson.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */