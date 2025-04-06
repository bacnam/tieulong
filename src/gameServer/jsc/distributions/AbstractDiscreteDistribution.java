/*     */ package jsc.distributions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDiscreteDistribution
/*     */   extends AbstractDistribution
/*     */ {
/*     */   protected long minValue;
/*     */   protected long maxValue;
/*     */   
/*     */   public AbstractDiscreteDistribution(long paramLong1, long paramLong2) {
/*  40 */     if (paramLong1 >= paramLong2)
/*  41 */       throw new IllegalArgumentException("Invalid variate range: " + paramLong1 + " to " + paramLong2 + "."); 
/*  42 */     this.minValue = paramLong1;
/*  43 */     this.maxValue = paramLong2;
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
/*     */   public double cdf(double paramDouble) {
/*  58 */     if (paramDouble < this.minValue || paramDouble > this.maxValue)
/*  59 */       throw new IllegalArgumentException("Invalid variate-value."); 
/*  60 */     double d = 0.0D;
/*  61 */     long l = this.minValue;
/*  62 */     while (l <= paramDouble) {
/*     */       
/*  64 */       d += pdf(l);
/*  65 */       l++;
/*     */     } 
/*     */     
/*  68 */     return d;
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
/*     */   public double getMaximumPdf() {
/*  82 */     long l1 = this.minValue;
/*  83 */     double d = 0.0D;
/*  84 */     for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {
/*     */       
/*  86 */       double d1 = pdf(l1);
/*  87 */       if (d1 > d) d = d1;
/*     */       
/*  89 */       l1++;
/*     */     } 
/*  91 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxValue() {
/*  99 */     return this.maxValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMinValue() {
/* 106 */     return this.minValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double moment(int paramInt) {
/* 115 */     return moment(paramInt, 0.0D);
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
/*     */   public double moment(int paramInt, double paramDouble) {
/* 127 */     if (paramInt < 0)
/* 128 */       throw new IllegalArgumentException("Invalid moment order."); 
/* 129 */     if (paramInt == 0) return 1.0D; 
/* 130 */     long l1 = this.minValue;
/* 131 */     double d = 0.0D;
/* 132 */     for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {
/*     */       
/* 134 */       d += Math.pow(l1 - paramDouble, paramInt) * pdf(l1);
/*     */       
/* 136 */       l1++;
/*     */     } 
/* 138 */     return d;
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
/*     */   public double inverseCdf(double paramDouble) {
/* 188 */     if (paramDouble < 0.0D || paramDouble > 1.0D) {
/* 189 */       throw new IllegalArgumentException("Invalid probability " + paramDouble);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     double d1 = cdf(this.minValue);
/* 200 */     if (d1 >= paramDouble) return this.minValue;
/*     */     
/* 202 */     double d2 = cdf(this.maxValue);
/* 203 */     if (d2 < paramDouble) return this.maxValue;
/*     */     
/* 205 */     long l1 = this.minValue;
/* 206 */     long l2 = this.maxValue;
/*     */     
/*     */     while (true) {
/* 209 */       long l4 = l2 - l1;
/* 210 */       if (l4 <= 1L) {
/*     */         
/* 212 */         if (cdf(l1) >= paramDouble) {
/* 213 */           return l1;
/*     */         }
/* 215 */         return l2;
/*     */       } 
/* 217 */       l4 /= 2L;
/* 218 */       long l3 = l1 + l4;
/* 219 */       double d = cdf(l3);
/* 220 */       if (d < paramDouble) {
/* 221 */         l1 = l3; continue;
/*     */       } 
/* 223 */       l2 = l3;
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
/*     */   public boolean isDiscrete() {
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 246 */     long l1 = this.minValue;
/* 247 */     double d = 0.0D;
/* 248 */     for (long l2 = 0L; l2 < this.maxValue - this.minValue + 1L; l2++) {
/*     */       
/* 250 */       d += l1 * pdf(l1);
/* 251 */       l1++;
/*     */     } 
/* 253 */     return d;
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
/*     */   public abstract double pdf(double paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 274 */     return moment(2, mean());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/AbstractDiscreteDistribution.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */