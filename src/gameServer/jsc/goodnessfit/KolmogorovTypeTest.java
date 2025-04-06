/*     */ package jsc.goodnessfit;
/*     */ 
/*     */ import jsc.distributions.Distribution;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class KolmogorovTypeTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   protected int n;
/*     */   protected Distribution F;
/*     */   protected double D;
/*     */   protected double positiveD;
/*     */   protected double negativeD;
/*     */   protected int indexOfD;
/*     */   protected int indexOfPositiveD;
/*     */   protected int indexOfNegativeD;
/*     */   protected double testStatistic;
/*     */   protected SampleDistributionFunction sdf;
/*     */   
/*     */   public KolmogorovTypeTest(double[] paramArrayOfdouble, Distribution paramDistribution, H1 paramH1) {
/*  73 */     double d = 0.0D;
/*     */ 
/*     */     
/*  76 */     this.sdf = new SampleDistributionFunction(paramArrayOfdouble);
/*  77 */     this.n = this.sdf.getN();
/*  78 */     this.F = paramDistribution;
/*     */     
/*  80 */     this.indexOfD = 0;
/*  81 */     this.indexOfPositiveD = 0;
/*  82 */     this.indexOfNegativeD = 0;
/*     */     
/*  84 */     double[] arrayOfDouble = new double[this.n];
/*  85 */     for (byte b = 0; b < this.n; b++) {
/*     */ 
/*     */       
/*  88 */       arrayOfDouble[b] = paramDistribution.cdf(this.sdf.getOrderedX(b));
/*  89 */       if (arrayOfDouble[b] < 0.0D || arrayOfDouble[b] > 1.0D)
/*  90 */         throw new IllegalArgumentException("Invalid distribution cdf."); 
/*  91 */       double d3 = this.sdf.getOrderedS(b);
/*     */ 
/*     */ 
/*     */       
/*  95 */       double d1 = paramDistribution.isDiscrete() ? 0.0D : (d - arrayOfDouble[b]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       double d2 = Math.max(Math.abs(d1), Math.abs(d3 - arrayOfDouble[b]));
/* 108 */       if (d2 > this.D) { this.D = d2; this.indexOfD = b; }
/* 109 */        d2 = Math.max(d1, d3 - arrayOfDouble[b]);
/* 110 */       if (d2 > this.positiveD) { this.positiveD = d2; this.indexOfPositiveD = b; }
/* 111 */        d2 = Math.max(-d1, arrayOfDouble[b] - d3);
/* 112 */       if (d2 > this.negativeD) { this.negativeD = d2; this.indexOfNegativeD = b; }
/*     */       
/* 114 */       d = d3;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     if (paramH1 == H1.NOT_EQUAL) { this.testStatistic = this.D; }
/* 119 */     else if (paramH1 == H1.LESS_THAN) { this.testStatistic = this.positiveD; }
/* 120 */     else { this.testStatistic = this.negativeD; }
/*     */   
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
/*     */   public KolmogorovTypeTest(double[] paramArrayOfdouble, Distribution paramDistribution) {
/* 139 */     this(paramArrayOfdouble, paramDistribution, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getD() {
/* 146 */     return this.D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPositiveD() {
/* 153 */     return this.positiveD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getNegativeD() {
/* 160 */     return this.negativeD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 167 */     return this.testStatistic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 174 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Distribution getF() {
/* 181 */     return this.F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SampleDistributionFunction getSdf() {
/* 188 */     return this.sdf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfD() {
/* 196 */     return this.indexOfD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfPositiveD() {
/* 204 */     return this.indexOfPositiveD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfNegativeD() {
/* 212 */     return this.indexOfNegativeD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double xOfD() {
/* 220 */     return this.sdf.getOrderedX(this.indexOfD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double xOfPositiveD() {
/* 228 */     return this.sdf.getOrderedX(this.indexOfPositiveD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double xOfNegativeD() {
/* 236 */     return this.sdf.getOrderedX(this.indexOfNegativeD);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/goodnessfit/KolmogorovTypeTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */