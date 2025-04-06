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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogarithmicSeries
/*     */   extends AbstractDistribution
/*     */ {
/*     */   private double alpha;
/*     */   private double C1;
/*     */   private double P1;
/*     */   
/*     */   public LogarithmicSeries(double paramDouble) {
/*  32 */     setAlpha(paramDouble);
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
/*  43 */     if (paramDouble < 1.0D) {
/*  44 */       throw new IllegalArgumentException("Invalid variate-value.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  49 */     if (paramDouble * (1.0D - this.alpha) > 10.0D) {
/*  50 */       return 1.0D;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  55 */     double d1 = this.P1;
/*  56 */     double d2 = d1;
/*     */ 
/*     */     
/*  59 */     for (byte b = 2; b <= paramDouble; b++) {
/*     */       
/*  61 */       d1 *= (1.0D - 1.0D / b) * this.alpha;
/*  62 */       d2 += d1;
/*  63 */       if (d1 <= 0.0D)
/*  64 */         break;  if (d2 >= 1.0D) return 1.0D;
/*     */     
/*     */     } 
/*  67 */     return d2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAlpha() {
/*  75 */     return this.alpha;
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
/*  87 */     if (paramDouble < 0.0D || paramDouble >= 1.0D) {
/*  88 */       throw new IllegalArgumentException("Invalid probability.");
/*     */     }
/*     */ 
/*     */     
/*  92 */     double d3 = 1.0D;
/*     */ 
/*     */     
/*  95 */     double d1 = this.P1;
/*  96 */     double d2 = d1;
/*     */ 
/*     */     
/*  99 */     while (d2 < paramDouble && d1 > 0.0D) {
/*     */       
/* 101 */       d3++;
/* 102 */       d1 *= (1.0D - 1.0D / d3) * this.alpha;
/* 103 */       d2 += d1;
/*     */     } 
/* 105 */     return d3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/* 113 */     return true;
/*     */   } public double mean() {
/* 115 */     return this.P1 / (1.0D - this.alpha);
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
/* 126 */     if (paramDouble < 1.0D)
/* 127 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 128 */     return -Math.pow(this.alpha, paramDouble) / paramDouble * this.C1;
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
/*     */   public double random() {
/* 146 */     if (this.alpha > 0.986D) {
/*     */ 
/*     */ 
/*     */       
/* 150 */       double d8 = this.C1;
/* 151 */       double d7 = this.rand.nextDouble();
/* 152 */       if (d7 > this.alpha) {
/* 153 */         return 1.0D;
/*     */       }
/*     */       
/* 156 */       double d6 = 1.0D - this.rand.nextDouble();
/* 157 */       double d5 = 1.0D - Math.exp(d6 * d8);
/* 158 */       if (d7 < d5 * d5)
/* 159 */         return Math.floor(1.0D + Math.log(d7) / Math.log(d5)); 
/* 160 */       if (d7 > d5) {
/* 161 */         return 1.0D;
/*     */       }
/* 163 */       return 2.0D;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     double d4 = this.P1;
/* 171 */     double d3 = this.rand.nextDouble();
/* 172 */     double d2 = 1.0D;
/* 173 */     double d1 = d4;
/* 174 */     while (d3 > d1) {
/* 175 */       d3 -= d1; d2++; d1 *= this.alpha * (d2 - 1.0D) / d2;
/* 176 */     }  return d2;
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
/*     */   public void setAlpha(double paramDouble) {
/* 189 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 190 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 191 */     this.alpha = paramDouble;
/* 192 */     this.C1 = Math.log(1.0D - paramDouble);
/* 193 */     this.P1 = -paramDouble / this.C1;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 197 */     return new String("Logarithmic series distribution: alpha = " + this.alpha + ".");
/*     */   } public double variance() {
/* 199 */     return this.P1 * (1.0D - this.P1) / (1.0D - this.alpha) * (1.0D - this.alpha);
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
/* 210 */       double d = 0.9D;
/* 211 */       LogarithmicSeries logarithmicSeries = new LogarithmicSeries(d);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       char c = '✐';
/*     */       
/* 221 */       logarithmicSeries = new LogarithmicSeries(0.5D);
/* 222 */       int[] arrayOfInt = new int[c];
/* 223 */       for (byte b = 0; b < c; ) { arrayOfInt[b] = (int)logarithmicSeries.random(); b++; }
/*     */       
/* 225 */       ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new Tally(arrayOfInt), logarithmicSeries, 0);
/* 226 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 227 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/LogarithmicSeries.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */