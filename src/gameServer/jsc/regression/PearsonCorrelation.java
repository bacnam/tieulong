/*     */ package jsc.regression;
/*     */ 
/*     */ import jsc.curvefitting.StraightLineFit;
/*     */ import jsc.datastructures.PairedData;
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
/*     */ public class PearsonCorrelation
/*     */   implements StraightLineFit
/*     */ {
/*     */   private final int n;
/*     */   private final double r;
/*     */   private final double a;
/*     */   private final double b;
/*     */   
/*     */   public PearsonCorrelation(PairedData paramPairedData) {
/*  28 */     this.n = paramPairedData.getN();
/*  29 */     double[] arrayOfDouble1 = paramPairedData.getX();
/*  30 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D, d4 = 0.0D, d5 = 0.0D;
/*     */     byte b;
/*  40 */     for (b = 0; b < this.n; b++) {
/*  41 */       d5 += arrayOfDouble1[b]; d4 += arrayOfDouble2[b];
/*  42 */     }  d5 /= this.n;
/*  43 */     d4 /= this.n;
/*  44 */     for (b = 0; b < this.n; b++) {
/*     */       
/*  46 */       double d6 = arrayOfDouble1[b] - d5;
/*  47 */       double d7 = arrayOfDouble2[b] - d4;
/*  48 */       d3 += d6 * d6;
/*  49 */       d1 += d7 * d7;
/*  50 */       d2 += d6 * d7;
/*     */     } 
/*     */ 
/*     */     
/*  54 */     if (d3 <= 0.0D) {
/*  55 */       throw new IllegalArgumentException("X data are constant.");
/*     */     }
/*     */     
/*  58 */     this.b = d2 / d3;
/*  59 */     this.a = d4 - this.b * d5;
/*     */ 
/*     */     
/*  62 */     if (d1 <= 0.0D) {
/*  63 */       throw new IllegalArgumentException("Y data are constant.");
/*     */     }
/*  65 */     this.r = d2 / Math.sqrt(d3 * d1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getA() {
/*  73 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/*  80 */     return this.b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  87 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR() {
/*  94 */     return this.r;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 109 */       double[] arrayOfDouble1 = { 8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D };
/* 110 */       double[] arrayOfDouble2 = { 8.0D, 9.0D, 10.0D, 11.0D, 12.0D };
/* 111 */       PearsonCorrelation pearsonCorrelation = new PearsonCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2));
/* 112 */       System.out.println("n = " + pearsonCorrelation.getN());
/* 113 */       System.out.println("r = " + pearsonCorrelation.getR());
/* 114 */       System.out.println("a = " + pearsonCorrelation.getA());
/* 115 */       System.out.println("b = " + pearsonCorrelation.getB());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/regression/PearsonCorrelation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */