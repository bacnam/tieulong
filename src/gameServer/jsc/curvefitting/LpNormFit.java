/*     */ package jsc.curvefitting;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.numerical.NumericalException;
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
/*     */ public class LpNormFit
/*     */   implements StraightLineFit
/*     */ {
/*     */   private final int n;
/*     */   private double a;
/*     */   private double b;
/*     */   private double p;
/*     */   private double sd;
/*     */   private double[] r;
/*     */   
/*     */   public LpNormFit(double paramDouble1, PairedData paramPairedData, double paramDouble2, int paramInt) throws NumericalException {
/*  59 */     double d1 = 0.0D, d2 = 0.0D;
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.n = paramPairedData.getN();
/*  64 */     double[] arrayOfDouble1 = paramPairedData.getX();
/*  65 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*  66 */     if (this.n < 2) {
/*  67 */       throw new IllegalArgumentException("Less than 2 observations.");
/*     */     }
/*  69 */     double d4 = paramDouble1 - 2.0D;
/*  70 */     double d3 = 2.0D * paramDouble2;
/*  71 */     this.sd = 0.0D;
/*  72 */     this.r = new double[this.n];
/*     */     
/*     */     byte b1;
/*  75 */     for (b1 = 0; b1 < this.n; ) { this.r[b1] = 1.0D; b1++; }
/*     */     
/*  77 */     for (byte b2 = 1; b2 <= paramInt; b2++) {
/*     */       
/*  79 */       byte b = 0;
/*     */ 
/*     */ 
/*     */       
/*  83 */       double d9 = 0.0D;
/*  84 */       double d10 = 0.0D;
/*  85 */       double d11 = 0.0D;
/*  86 */       double d8 = 0.0D;
/*  87 */       double d7 = 0.0D;
/*  88 */       for (b1 = 0; b1 < this.n; b1++) {
/*     */         
/*  90 */         double d = Math.abs(this.r[b1]);
/*  91 */         if (d <= paramDouble2) {
/*  92 */           b++;
/*     */         } else {
/*     */           
/*  95 */           double d15 = Math.pow(d, d4);
/*  96 */           d9 += d15;
/*  97 */           double d12 = d15 / d9;
/*  98 */           double d16 = arrayOfDouble1[b1] - d10;
/*  99 */           double d18 = arrayOfDouble2[b1] - d11;
/* 100 */           double d17 = d16 * d15;
/* 101 */           double d13 = d16 * d17;
/* 102 */           double d14 = d18 * d17;
/* 103 */           d8 += d13 - d13 * d12;
/* 104 */           d7 += d14 - d14 * d12;
/* 105 */           d10 += d16 * d12;
/* 106 */           d11 += d18 * d12;
/*     */         } 
/*     */       } 
/* 109 */       if (d8 < paramDouble2)
/*     */       {
/* 111 */         throw new NumericalException("Weighted sample variance of x is zero."); } 
/* 112 */       this.b = d7 / d8;
/* 113 */       this.a = d11 - this.b * d10;
/*     */ 
/*     */       
/* 116 */       double d6 = 0.0D;
/* 117 */       boolean bool = false;
/* 118 */       for (b1 = 0; b1 < this.n; b1++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         double d13 = arrayOfDouble2[b1] - this.a + this.b * arrayOfDouble1[b1];
/* 124 */         double d12 = Math.abs(d13);
/* 125 */         if (Math.abs(d12 - Math.abs(this.r[b1])) > d3) bool = true;
/*     */         
/* 127 */         d6 += Math.pow(d12, paramDouble1);
/* 128 */         this.r[b1] = d13;
/*     */       } 
/*     */ 
/*     */       
/* 132 */       double d5 = Math.abs(d6 - this.sd) / d6;
/* 133 */       if (!bool)
/* 134 */         return;  if (b2 != 1)
/*     */       {
/* 136 */         if (d6 > this.sd) {
/*     */ 
/*     */           
/* 139 */           this.a = d1; this.b = d2;
/* 140 */           for (b1 = 0; b1 < this.n; b1++)
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 145 */             this.r[b1] = arrayOfDouble2[b1] - this.a + this.b * arrayOfDouble1[b1];
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/* 151 */       this.sd = d6;
/* 152 */       d1 = this.a;
/* 153 */       d2 = this.b;
/*     */     } 
/*     */ 
/*     */     
/* 157 */     throw new NumericalException("Maximum number of iterations exceeded.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getA() {
/* 165 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/* 172 */     return this.b;
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
/*     */   public int getN() {
/* 186 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getNorm() {
/* 193 */     return this.sd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getP() {
/* 200 */     return this.b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getResiduals() {
/* 207 */     return this.r;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) throws NumericalException {
/* 226 */       double[] arrayOfDouble1 = { 0.2D, 0.5D, 0.9D, 1.4D, 2.1D, 2.7D, 3.0D, 3.6D, 4.1D, 4.4D, 5.0D, 5.6D, 6.2D, 6.6D, 7.1D };
/* 227 */       double[] arrayOfDouble2 = { 50.0D, 48.0D, 42.0D, 36.0D, 34.0D, 32.0D, 29.0D, 31.0D, 28.0D, 24.0D, 19.0D, 17.0D, 10.0D, 12.0D, 9.5D };
/* 228 */       LpNormFit lpNormFit = new LpNormFit(1.0D, new PairedData(arrayOfDouble1, arrayOfDouble2), 1.0E-16D, 50);
/* 229 */       int i = lpNormFit.getN();
/* 230 */       double d1 = lpNormFit.getA();
/* 231 */       double d2 = lpNormFit.getB();
/* 232 */       System.out.println("n = " + i);
/* 233 */       System.out.println("a = " + d1);
/* 234 */       System.out.println("b = " + d2);
/* 235 */       System.out.println("Norm = " + lpNormFit.getNorm());
/* 236 */       double[] arrayOfDouble3 = lpNormFit.getResiduals();
/* 237 */       for (byte b = 0; b < i; b++)
/* 238 */         System.out.println(arrayOfDouble3[b] + " " + (arrayOfDouble2[b] - d1 + d2 * arrayOfDouble1[b])); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/LpNormFit.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */