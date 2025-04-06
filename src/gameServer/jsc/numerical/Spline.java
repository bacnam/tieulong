/*     */ package jsc.numerical;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Spline
/*     */ {
/*     */   private int n;
/*     */   private double[] x;
/*     */   private double[] y;
/*     */   private double[] y2;
/*     */   
/*     */   public Spline(int paramInt, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  37 */     double[] arrayOfDouble = new double[paramInt + 1];
/*     */     
/*  39 */     this.x = new double[paramInt + 1];
/*  40 */     this.y = new double[paramInt + 1];
/*  41 */     this.y2 = new double[paramInt + 1];
/*     */ 
/*     */     
/*  44 */     this.n = paramInt;
/*  45 */     this.x[0] = Double.NEGATIVE_INFINITY; byte b;
/*  46 */     for (b = 0; b < this.n; b++) {
/*     */       
/*  48 */       this.x[b + 1] = paramArrayOfdouble1[b];
/*  49 */       if (this.x[b + 1] <= this.x[b])
/*  50 */         throw new IllegalArgumentException("x values not in ascending order or distinct."); 
/*  51 */       this.y[b + 1] = paramArrayOfdouble2[b];
/*     */     } 
/*     */     
/*  54 */     this.y2[1] = 0.0D;
/*  55 */     arrayOfDouble[1] = 0.0D;
/*     */     
/*  57 */     for (b = 2; b <= this.n - 1; b++) {
/*     */       
/*  59 */       double d4 = (this.x[b] - this.x[b - 1]) / (this.x[b + 1] - this.x[b - 1]);
/*  60 */       double d3 = d4 * this.y2[b - 1] + 2.0D;
/*  61 */       this.y2[b] = (d4 - 1.0D) / d3;
/*  62 */       arrayOfDouble[b] = (this.y[b + 1] - this.y[b]) / (this.x[b + 1] - this.x[b]) - (this.y[b] - this.y[b - 1]) / (this.x[b] - this.x[b - 1]);
/*  63 */       arrayOfDouble[b] = (6.0D * arrayOfDouble[b] / (this.x[b + 1] - this.x[b - 1]) - d4 * arrayOfDouble[b - 1]) / d3;
/*     */     } 
/*  65 */     double d2 = 0.0D, d1 = d2;
/*  66 */     this.y2[this.n] = (d2 - d1 * arrayOfDouble[this.n - 1]) / (d1 * this.y2[this.n - 1] + 1.0D);
/*  67 */     int i = this.n - 1;
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
/*     */   public double getMaxX() {
/* 103 */     return this.x[this.n];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinX() {
/* 109 */     return this.x[1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 116 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getX(int paramInt) {
/* 124 */     return this.x[paramInt + 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getY(int paramInt) {
/* 132 */     return this.y[paramInt + 1];
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
/*     */   public double[] evalDerivative(double paramDouble) {
/* 145 */     double[] arrayOfDouble = new double[2];
/*     */ 
/*     */ 
/*     */     
/* 149 */     int i = 1;
/* 150 */     int j = this.n;
/* 151 */     while (j - i > 1) {
/* 152 */       int k = j + i >> 1;
/* 153 */       if (this.x[k] > paramDouble) { j = k; continue; }
/* 154 */        i = k;
/*     */     } 
/* 156 */     double d1 = this.x[j] - this.x[i];
/* 157 */     if (d1 == 0.0D)
/* 158 */       throw new IllegalArgumentException("Cannot evaluate spline at " + paramDouble); 
/* 159 */     double d3 = (this.x[j] - paramDouble) / d1;
/* 160 */     double d2 = (paramDouble - this.x[i]) / d1;
/*     */     
/* 162 */     double d4 = d3 * d3;
/* 163 */     double d5 = d2 * d2;
/* 164 */     arrayOfDouble[0] = d3 * this.y[i] + d2 * this.y[j] + ((d4 * d3 - d3) * this.y2[i] + (d5 * d2 - d2) * this.y2[j]) * d1 * d1 / 6.0D;
/* 165 */     arrayOfDouble[1] = (this.y[j] - this.y[i]) / d1 + ((3.0D * d5 - 1.0D) * this.y2[j] - (3.0D * d4 - 1.0D) * this.y2[i]) * d1 / 6.0D;
/* 166 */     return arrayOfDouble;
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
/*     */   public double splint(double paramDouble) {
/* 181 */     int i = 1;
/* 182 */     int j = this.n;
/* 183 */     while (j - i > 1) {
/* 184 */       int k = j + i >> 1;
/* 185 */       if (this.x[k] > paramDouble) { j = k; continue; }
/* 186 */        i = k;
/*     */     } 
/* 188 */     double d1 = this.x[j] - this.x[i];
/* 189 */     if (d1 == 0.0D)
/* 190 */       throw new IllegalArgumentException("Cannot evaluate spline at " + paramDouble); 
/* 191 */     double d3 = (this.x[j] - paramDouble) / d1;
/* 192 */     double d2 = (paramDouble - this.x[i]) / d1;
/* 193 */     return d3 * this.y[i] + d2 * this.y[j] + ((d3 * d3 * d3 - d3) * this.y2[i] + (d2 * d2 * d2 - d2) * this.y2[j]) * d1 * d1 / 6.0D;
/*     */   }
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
/* 206 */       char c = 'Ϩ';
/* 207 */       double[] arrayOfDouble1 = new double[c];
/* 208 */       double[] arrayOfDouble2 = new double[c];
/* 209 */       for (byte b = 0; b < c; b++) {
/*     */         
/* 211 */         arrayOfDouble1[b] = b;
/* 212 */         arrayOfDouble2[b] = Math.sin(b);
/*     */       } 
/*     */       
/* 215 */       Spline spline = new Spline(c, arrayOfDouble1, arrayOfDouble2);
/* 216 */       double d = 3.5D;
/* 217 */       System.out.println("f(" + d + ") = " + spline.splint(d));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/Spline.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */