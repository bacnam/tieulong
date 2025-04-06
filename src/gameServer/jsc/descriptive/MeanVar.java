/*     */ package jsc.descriptive;
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
/*     */ public class MeanVar
/*     */   implements Cloneable
/*     */ {
/*     */   private int n;
/*     */   private double mean;
/*     */   private double variance;
/*     */   
/*     */   public MeanVar(double[] paramArrayOfdouble) {
/*  25 */     double d = 0.0D;
/*  26 */     this.n = paramArrayOfdouble.length;
/*  27 */     if (this.n < 2)
/*  28 */       throw new IllegalArgumentException("Less than two data values.");  byte b;
/*  29 */     for (b = 0; b < this.n; ) { d += paramArrayOfdouble[b]; b++; }
/*  30 */      this.mean = d / this.n;
/*  31 */     d = 0.0D;
/*  32 */     for (b = 0; b < this.n; ) { d += (paramArrayOfdouble[b] - this.mean) * (paramArrayOfdouble[b] - this.mean); b++; }
/*  33 */      this.variance = d / (this.n - 1);
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
/*     */   public MeanVar(double paramDouble) {
/*  45 */     this.n = 1;
/*  46 */     this.mean = paramDouble;
/*  47 */     this.variance = 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValue(double paramDouble) {
/*  57 */     double d = this.mean;
/*  58 */     this.mean = d + (paramDouble - d) / (this.n + 1.0D);
/*  59 */     this.variance = (1.0D - 1.0D / this.n) * this.variance + (this.mean - d) * (this.mean - d) * (this.n + 1.0D);
/*  60 */     this.n++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  66 */     Object object = null; try {
/*  67 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/*  69 */       System.out.println("MeanVar can't clone");
/*  70 */     }  return object;
/*     */   }
/*     */   
/*     */   public double getMean() {
/*  74 */     return this.mean;
/*     */   }
/*     */   public int getN() {
/*  77 */     return this.n;
/*     */   }
/*     */   public double getSd() {
/*  80 */     return Math.sqrt(this.variance);
/*     */   }
/*     */   public double getVariance() {
/*  83 */     return this.variance;
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
/*  94 */       double[] arrayOfDouble1 = { 73.0D, 62.7D, 59.3D, 68.2D };
/*  95 */       MeanVar meanVar1 = new MeanVar(arrayOfDouble1);
/*  96 */       System.out.println("n = " + meanVar1.getN());
/*  97 */       System.out.println("Mean = " + meanVar1.getMean());
/*  98 */       System.out.println("Variance = " + meanVar1.getVariance());
/*  99 */       System.out.println("s.d. = " + meanVar1.getSd());
/*     */       
/* 101 */       MeanVar meanVar2 = new MeanVar(73.0D);
/* 102 */       meanVar2.addValue(62.7D); meanVar2.addValue(59.3D); meanVar2.addValue(68.2D);
/* 103 */       System.out.println("n = " + meanVar2.getN());
/* 104 */       System.out.println("Mean = " + meanVar2.getMean());
/* 105 */       System.out.println("Variance = " + meanVar2.getVariance());
/* 106 */       System.out.println("s.d. = " + meanVar2.getSd());
/*     */ 
/*     */       
/* 109 */       double[] arrayOfDouble2 = { 7.000001D, 7.000002D, 7.000003D, 7.000004D, 7.000005D };
/* 110 */       MeanVar meanVar3 = new MeanVar(arrayOfDouble2);
/* 111 */       System.out.println("n = " + meanVar3.getN());
/* 112 */       System.out.println("Mean = " + meanVar3.getMean());
/* 113 */       System.out.println("Variance = " + meanVar3.getVariance());
/* 114 */       System.out.println("s.d. = " + meanVar3.getSd());
/*     */       
/* 116 */       MeanVar meanVar4 = new MeanVar(7.000001D);
/* 117 */       meanVar4.addValue(7.000002D); meanVar4.addValue(7.000003D); meanVar4.addValue(7.000004D);
/* 118 */       meanVar4.addValue(7.000005D);
/* 119 */       System.out.println("n = " + meanVar4.getN());
/* 120 */       System.out.println("Mean = " + meanVar4.getMean());
/* 121 */       System.out.println("Variance = " + meanVar4.getVariance());
/* 122 */       System.out.println("s.d. = " + meanVar4.getSd());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/MeanVar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */