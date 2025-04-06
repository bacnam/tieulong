/*     */ package jsc.onesample;
/*     */ 
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
/*     */ public class Ttest
/*     */   implements SignificanceTest
/*     */ {
/*     */   protected int n;
/*     */   private final double t;
/*     */   private final MeanVar mv;
/*     */   private final double SP;
/*     */   
/*     */   public Ttest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
/*  36 */     this.mv = new MeanVar(paramArrayOfdouble);
/*  37 */     this.n = this.mv.getN();
/*  38 */     this.t = (this.mv.getMean() - paramDouble) / this.mv.getSd() / Math.sqrt(this.n);
/*  39 */     double d = StudentsT.tailProb(this.t, (this.n - 1));
/*     */     
/*  41 */     if (paramH1 == H1.NOT_EQUAL) {
/*  42 */       this.SP = d + d;
/*  43 */     } else if (paramH1 == H1.LESS_THAN) {
/*  44 */       this.SP = (this.t < 0.0D) ? d : (1.0D - d);
/*     */     } else {
/*  46 */       this.SP = (this.t > 0.0D) ? d : (1.0D - d);
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
/*     */   public Ttest(double[] paramArrayOfdouble, double paramDouble) {
/*  58 */     this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMean() {
/*  65 */     return this.mv.getMean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  72 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSd() {
/*  79 */     return this.mv.getSd();
/*     */   } public double getSP() {
/*  81 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatistic() {
/*  88 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/*  95 */     return this.t;
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
/* 107 */       double[] arrayOfDouble = { 4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D };
/* 108 */       double d = 5.0D;
/* 109 */       Ttest ttest1 = new Ttest(arrayOfDouble, d, H1.NOT_EQUAL);
/* 110 */       System.out.println("H1: mu <> " + d + " t = " + ttest1.getTestStatistic() + " SP = " + ttest1.getSP());
/* 111 */       Ttest ttest2 = new Ttest(arrayOfDouble, d, H1.LESS_THAN);
/* 112 */       System.out.println("H1: mu < " + d + " t = " + ttest2.getTestStatistic() + " SP = " + ttest2.getSP());
/* 113 */       Ttest ttest3 = new Ttest(arrayOfDouble, d, H1.GREATER_THAN);
/* 114 */       System.out.println("H1: mu > " + d + " t = " + ttest3.getTestStatistic() + " SP = " + ttest3.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/Ttest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */