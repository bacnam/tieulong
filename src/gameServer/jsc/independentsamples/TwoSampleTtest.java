/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.ci.ConfidenceInterval;
/*     */ import jsc.combinatorics.Enumerator;
/*     */ import jsc.combinatorics.MultiSetPermutations;
/*     */ import jsc.descriptive.MeanVar;
/*     */ import jsc.distributions.StudentsT;
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
/*     */ public class TwoSampleTtest
/*     */   extends PermutableTwoSampleStatistic
/*     */   implements TwoSampleStatistic, SignificanceTest, ConfidenceInterval
/*     */ {
/*     */   private final boolean sameVar;
/*     */   private final double d;
/*     */   private final double df;
/*     */   private final double s;
/*     */   private final double t;
/*     */   private final MeanVar mvA;
/*     */   private final MeanVar mvB;
/*     */   private final double SP;
/*     */   private double confidenceCoeff;
/*     */   private double lowerLimit;
/*     */   private double upperLimit;
/*     */   private final double[] xA;
/*     */   private final double[] xB;
/*     */   
/*     */   public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean, double paramDouble) {
/*  51 */     super(paramArrayOfdouble1, paramArrayOfdouble2);
/*  52 */     this.sameVar = paramBoolean;
/*  53 */     this.xA = paramArrayOfdouble1;
/*  54 */     this.xB = paramArrayOfdouble2;
/*  55 */     this.mvA = new MeanVar(paramArrayOfdouble1);
/*  56 */     this.mvB = new MeanVar(paramArrayOfdouble2);
/*  57 */     int i = this.mvA.getN();
/*  58 */     int j = this.mvB.getN();
/*     */     
/*  60 */     if (paramBoolean) {
/*     */       
/*  62 */       this.df = (i + j - 2);
/*  63 */       double d1 = ((i - 1) * this.mvA.getVariance() + (j - 1) * this.mvB.getVariance()) / this.df;
/*  64 */       this.s = Math.sqrt(d1 * (1.0D / i + 1.0D / j));
/*     */     }
/*     */     else {
/*     */       
/*  68 */       double d1 = this.mvA.getVariance() / i;
/*  69 */       double d2 = this.mvB.getVariance() / j;
/*  70 */       this.df = Math.floor((d1 + d2) * (d1 + d2) / (d1 * d1 / (i - 1) + d2 * d2 / (j - 1)));
/*     */       
/*  72 */       this.s = Math.sqrt(d1 + d2);
/*     */     } 
/*     */     
/*  75 */     this.d = this.mvA.getMean() - this.mvB.getMean();
/*  76 */     this.t = this.d / this.s;
/*  77 */     double d = StudentsT.tailProb(this.t, this.df);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     if (paramH1 == H1.NOT_EQUAL) {
/*  85 */       this.SP = d + d;
/*  86 */     } else if (paramH1 == H1.LESS_THAN) {
/*  87 */       this.SP = (this.t < 0.0D) ? d : (1.0D - d);
/*     */     } else {
/*  89 */       this.SP = (this.t > 0.0D) ? d : (1.0D - d);
/*     */     } 
/*     */ 
/*     */     
/*  93 */     setConfidenceCoeff(paramDouble);
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
/*     */   public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, boolean paramBoolean) {
/* 106 */     this(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, paramBoolean, 0.95D);
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
/*     */   public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean) {
/* 118 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, paramBoolean);
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
/*     */   public TwoSampleTtest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 130 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, false);
/*     */   }
/*     */   
/*     */   public double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*     */     double d;
/* 135 */     MeanVar meanVar1 = new MeanVar(paramArrayOfdouble1);
/* 136 */     MeanVar meanVar2 = new MeanVar(paramArrayOfdouble2);
/* 137 */     int i = meanVar1.getN();
/* 138 */     int j = meanVar2.getN();
/*     */     
/* 140 */     if (this.sameVar) {
/*     */       
/* 142 */       double d1 = ((i - 1) * meanVar1.getVariance() + (j - 1) * meanVar2.getVariance()) / this.df;
/* 143 */       d = Math.sqrt(d1 * (1.0D / i + 1.0D / j));
/*     */     }
/*     */     else {
/*     */       
/* 147 */       double d1 = meanVar1.getVariance() / i;
/* 148 */       double d2 = meanVar2.getVariance() / j;
/* 149 */       d = Math.sqrt(d1 + d2);
/*     */     } 
/*     */     
/* 152 */     return (meanVar1.getMean() - meanVar2.getMean()) / d;
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
/*     */   public double getDf() {
/* 185 */     return this.df;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumerator getEnumerator() {
/* 195 */     int[] arrayOfInt = new int[2];
/* 196 */     arrayOfInt[0] = this.mvA.getN(); arrayOfInt[1] = this.mvB.getN();
/* 197 */     return (Enumerator)new MultiSetPermutations(arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanA() {
/* 205 */     return this.mvA.getMean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanB() {
/* 212 */     return this.mvB.getMean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanDiff() {
/* 219 */     return this.d;
/*     */   }
/* 221 */   public double[] getSampleA() { return this.xA; } public double[] getSampleB() {
/* 222 */     return this.xB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSdA() {
/* 229 */     return this.mvA.getSd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSdB() {
/* 236 */     return this.mvB.getSd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSdDiff() {
/* 243 */     return this.s;
/*     */   } public int sizeA() {
/* 245 */     return this.mvA.getN();
/*     */   } public int sizeB() {
/* 247 */     return this.mvB.getN();
/*     */   } public double getSP() {
/* 249 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatistic() {
/* 256 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 263 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLowerLimit() {
/* 270 */     return this.lowerLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getUpperLimit() {
/* 277 */     return this.upperLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getConfidenceCoeff() {
/* 284 */     return this.confidenceCoeff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfidenceCoeff(double paramDouble) {
/* 294 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/* 295 */       throw new IllegalArgumentException("Invalid confidence coefficient."); 
/* 296 */     this.confidenceCoeff = paramDouble;
/* 297 */     StudentsT studentsT = new StudentsT(this.df);
/* 298 */     double d1 = studentsT.inverseCdf(0.5D + 0.5D * paramDouble);
/* 299 */     double d2 = d1 * this.s;
/* 300 */     this.lowerLimit = this.d - d2;
/* 301 */     this.upperLimit = this.d + d2;
/*     */   }
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
/* 313 */       double[] arrayOfDouble1 = { 90.0D, 72.0D, 61.0D, 66.0D, 81.0D, 69.0D, 59.0D, 70.0D };
/* 314 */       double[] arrayOfDouble2 = { 62.0D, 85.0D, 78.0D, 66.0D, 80.0D, 91.0D, 69.0D, 77.0D, 84.0D };
/* 315 */       TwoSampleTtest twoSampleTtest1 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2);
/* 316 */       System.out.println("H1: means not equal: t = " + twoSampleTtest1.getTestStatistic() + " SP = " + twoSampleTtest1.getSP());
/*     */ 
/*     */       
/* 319 */       System.out.println("CI = [" + twoSampleTtest1.getLowerLimit() + ", " + twoSampleTtest1.getUpperLimit() + "]");
/* 320 */       TwoSampleTtest twoSampleTtest2 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.LESS_THAN, false);
/* 321 */       System.out.println("H1: mean A < mean B: t = " + twoSampleTtest2.getTestStatistic() + " SP = " + twoSampleTtest2.getSP());
/* 322 */       System.out.println("CI = [" + twoSampleTtest2.getLowerLimit() + ", " + twoSampleTtest2.getUpperLimit() + "]");
/* 323 */       TwoSampleTtest twoSampleTtest3 = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, false);
/* 324 */       System.out.println("H1: mean A > mean B: t = " + twoSampleTtest3.getTestStatistic() + " SP = " + twoSampleTtest3.getSP());
/* 325 */       System.out.println("CI = [" + twoSampleTtest3.getLowerLimit() + ", " + twoSampleTtest3.getUpperLimit() + "]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/TwoSampleTtest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */