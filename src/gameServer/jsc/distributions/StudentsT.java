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
/*     */ public class StudentsT
/*     */   extends AbstractDistribution
/*     */ {
/*  16 */   static final double LOGPI = Math.log(Math.PI);
/*     */ 
/*     */   
/*     */   private ChiSquared chiSquared;
/*     */ 
/*     */   
/*     */   private double df;
/*     */ 
/*     */ 
/*     */   
/*     */   public StudentsT(double paramDouble) {
/*  27 */     setDf(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double cdf(double paramDouble) {
/*  37 */     double d = tailProb(paramDouble, this.df);
/*  38 */     return (paramDouble > 0.0D) ? (1.0D - d) : d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDf() {
/*  46 */     return this.df;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumPdf() {
/*  54 */     return Math.exp(Maths.logGamma(0.5D * (this.df + 1.0D)) - Maths.logGamma(0.5D * this.df) - 0.5D * Math.log(Math.PI * this.df));
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
/*     */     double d1;
/*  68 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/*  69 */       throw new IllegalArgumentException("Invalid probability."); 
/*  70 */     if (paramDouble == 0.0D) return Double.NEGATIVE_INFINITY; 
/*  71 */     if (paramDouble == 1.0D) return Double.POSITIVE_INFINITY;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     if (paramDouble > 0.5D) {
/*  77 */       d1 = 1.0D - paramDouble;
/*     */     } else {
/*  79 */       d1 = paramDouble;
/*     */     } 
/*  81 */     double d2 = Beta.inverseIncompleteBeta(this.df / 2.0D, 0.5D, Maths.lnB(this.df / 2.0D, 0.5D), d1 + d1);
/*  82 */     if (d2 == 1.0D) return 0.0D; 
/*  83 */     if (paramDouble < 0.5D) {
/*  84 */       if (d2 == 0.0D) {
/*  85 */         return Double.NEGATIVE_INFINITY;
/*     */       }
/*  87 */       return -Math.sqrt(this.df * (1.0D - d2) / d2);
/*     */     } 
/*  89 */     if (d2 == 0.0D) {
/*  90 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*  92 */     return Math.sqrt(this.df * (1.0D - d2) / d2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 102 */     return (this.df > 1.0D) ? 0.0D : Double.NaN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double pdf(double paramDouble) {
/* 112 */     return Math.exp(Maths.logGamma(0.5D * (this.df + 1.0D)) - Maths.logGamma(0.5D * this.df) - 0.5D * ((this.df + 1.0D) * Math.log(1.0D + paramDouble * paramDouble / this.df) + LOGPI + Math.log(this.df)));
/*     */   }
/*     */   
/*     */   public double random() {
/* 116 */     return this.rand.nextGaussian() / Math.sqrt(this.chiSquared.random() / this.df);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDf(double paramDouble) {
/* 126 */     if (paramDouble <= 0.0D)
/* 127 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 128 */     this.df = paramDouble;
/* 129 */     this.chiSquared = new ChiSquared(paramDouble);
/* 130 */     this.chiSquared.setSeed(this.rand.nextLong() + 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 135 */     this.rand.setSeed(paramLong);
/* 136 */     this.chiSquared.setSeed(this.rand.nextLong() + 1L);
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
/*     */   public static double tailProb(double paramDouble1, double paramDouble2) {
/* 151 */     return Beta.incompleteBeta(paramDouble2 / (paramDouble2 + paramDouble1 * paramDouble1), paramDouble2 / 2.0D, 0.5D, Maths.lnB(paramDouble2 / 2.0D, 0.5D)) / 2.0D;
/*     */   } public String toString() {
/* 153 */     return new String("Student's t distribution: df = " + this.df + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 162 */     return (this.df > 2.0D) ? (this.df / (this.df - 2.0D)) : Double.NaN;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 188 */       char c = '✐';
/* 189 */       StudentsT studentsT = new StudentsT(10.0D);
/* 190 */       double[] arrayOfDouble = new double[c];
/* 191 */       for (byte b = 0; b < c; b++)
/*     */       {
/* 193 */         arrayOfDouble[b] = studentsT.random();
/*     */       }
/*     */ 
/*     */       
/* 197 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, studentsT, H1.NOT_EQUAL, true);
/* 198 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/StudentsT.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */