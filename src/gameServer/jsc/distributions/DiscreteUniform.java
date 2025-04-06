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
/*     */ 
/*     */ public class DiscreteUniform
/*     */   extends AbstractDiscreteDistribution
/*     */ {
/*     */   private long a;
/*     */   private long b;
/*     */   private double n;
/*     */   
/*     */   public DiscreteUniform(long paramLong1, long paramLong2) {
/*  27 */     super(paramLong1, paramLong2); setInterval(paramLong1, paramLong2);
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
/*  40 */     return (1.0D + paramDouble - this.a) / this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getA() {
/*  48 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getB() {
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
/*  66 */     if (paramDouble < 0.0D || paramDouble > 1.0D) {
/*  67 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/*     */ 
/*     */     
/*  71 */     if (paramDouble == 1.0D) return this.b; 
/*  72 */     long l = this.a;
/*  73 */     double d = 1.0D / this.n;
/*  74 */     while (d < paramDouble && l < this.b) {
/*  75 */       l++; d = (1.0D + l - this.a) / this.n;
/*  76 */     }  return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/*  86 */     return 0.5D * (this.a + this.b);
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
/*  99 */     if (paramDouble < this.a || paramDouble > this.b)
/* 100 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 101 */     return 1.0D / this.n;
/*     */   }
/*     */   public double random() {
/* 104 */     return Math.floor(this.a + this.rand.nextDouble() * this.n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(long paramLong1, long paramLong2) {
/* 115 */     if (paramLong2 <= paramLong1)
/* 116 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 117 */     this.a = paramLong1;
/* 118 */     this.b = paramLong2;
/* 119 */     this.minValue = paramLong1;
/* 120 */     this.maxValue = paramLong2;
/* 121 */     this.n = (paramLong2 - paramLong1 + 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return new String("Discrete uniform distribution: a = " + this.a + ", b = " + this.b + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 135 */     double d1 = mean();
/* 136 */     double d2 = 0.0D;
/*     */     
/* 138 */     for (long l = this.a; l <= this.b; l++) {
/*     */       
/* 140 */       double d = l - d1;
/* 141 */       d2 += d * d;
/*     */     } 
/* 143 */     return d2 / this.n;
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
/* 156 */       long l1 = 1L;
/* 157 */       long l2 = 10L;
/* 158 */       double d = 0.5D;
/* 159 */       DiscreteUniform discreteUniform = new DiscreteUniform(l1, l2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       char c = '✐';
/* 168 */       int[] arrayOfInt = new int[c];
/* 169 */       discreteUniform = new DiscreteUniform(10L, 20L);
/* 170 */       for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)discreteUniform.random(); b++; }
/* 171 */        ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), discreteUniform, 0);
/* 172 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 173 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/DiscreteUniform.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */