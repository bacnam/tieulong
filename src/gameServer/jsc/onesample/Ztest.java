/*     */ package jsc.onesample;
/*     */ 
/*     */ import jsc.descriptive.MeanVar;
/*     */ import jsc.distributions.Normal;
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
/*     */ public class Ztest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private final double z;
/*     */   private final MeanVar mv;
/*     */   private double SP;
/*     */   
/*     */   public Ztest(double[] paramArrayOfdouble, double paramDouble1, double paramDouble2, H1 paramH1) {
/*  37 */     this.mv = new MeanVar(paramArrayOfdouble);
/*  38 */     this.z = (this.mv.getMean() - paramDouble1) / paramDouble2 / Math.sqrt(this.mv.getN());
/*  39 */     this.SP = getSP(this.z, paramH1);
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
/*     */   public Ztest(double[] paramArrayOfdouble, double paramDouble1, double paramDouble2) {
/*  51 */     this(paramArrayOfdouble, paramDouble1, paramDouble2, H1.NOT_EQUAL);
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
/*     */   public Ztest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
/*  64 */     this.mv = new MeanVar(paramArrayOfdouble);
/*  65 */     this.z = (this.mv.getMean() - paramDouble) / this.mv.getSd() / Math.sqrt(this.mv.getN());
/*  66 */     this.SP = getSP(this.z, paramH1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ztest(double[] paramArrayOfdouble, double paramDouble) {
/*  77 */     this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMean() {
/*  84 */     return this.mv.getMean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSd() {
/*  91 */     return this.mv.getSd();
/*     */   } public double getSP() {
/*  93 */     return this.SP;
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
/*     */   public static double getSP(double paramDouble, H1 paramH1) {
/* 106 */     if (paramH1 == H1.NOT_EQUAL)
/* 107 */       return 2.0D * Normal.standardTailProb(Math.abs(paramDouble), true); 
/* 108 */     if (paramH1 == H1.LESS_THAN) {
/* 109 */       return Normal.standardTailProb(paramDouble, false);
/*     */     }
/* 111 */     return Normal.standardTailProb(paramDouble, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 121 */     return this.z;
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
/* 133 */       double[] arrayOfDouble = { 4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D };
/* 134 */       Ztest ztest1 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.NOT_EQUAL);
/* 135 */       System.out.println("z = " + ztest1.getTestStatistic() + " SP = " + ztest1.getSP());
/* 136 */       Ztest ztest2 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.LESS_THAN);
/* 137 */       System.out.println("z = " + ztest2.getTestStatistic() + " SP = " + ztest2.getSP());
/* 138 */       Ztest ztest3 = new Ztest(arrayOfDouble, 5.0D, 0.2D, H1.GREATER_THAN);
/* 139 */       System.out.println("z = " + ztest3.getTestStatistic() + " SP = " + ztest3.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/Ztest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */