/*     */ package jsc.curvefitting;
/*     */ 
/*     */ import jsc.ci.AbstractConfidenceInterval;
/*     */ import jsc.ci.ConfidenceInterval;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.StudentsT;
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
/*     */ public class LineFit
/*     */   implements StraightLineFit
/*     */ {
/*     */   private final int n;
/*     */   private double a;
/*     */   private double b;
/*     */   private double chi2;
/*     */   private double ax;
/*     */   private double ay;
/*     */   private double sxx;
/*     */   
/*     */   public LineFit(PairedData paramPairedData) {
/*  37 */     this(paramPairedData, null);
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
/*     */   public LineFit(PairedData paramPairedData, double[] paramArrayOfdouble) {
/*  51 */     this.n = paramPairedData.getN();
/*  52 */     double[] arrayOfDouble1 = paramPairedData.getX();
/*  53 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*  54 */     if (paramArrayOfdouble == null) {
/*  55 */       unweightedLineFit(arrayOfDouble1, arrayOfDouble2);
/*     */     } else {
/*     */       
/*  58 */       if (this.n != paramArrayOfdouble.length) {
/*  59 */         throw new IllegalArgumentException("Weights array is wrong length.");
/*     */       }
/*  61 */       weightedLineFit(arrayOfDouble1, arrayOfDouble2, paramArrayOfdouble);
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
/*     */   private void unweightedLineFit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  73 */     this.a = 0.0D;
/*  74 */     this.b = 0.0D;
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (this.n == 2) {
/*     */ 
/*     */       
/*  81 */       double d = paramArrayOfdouble1[1] - paramArrayOfdouble1[0];
/*  82 */       if (d == 0.0D)
/*  83 */         throw new IllegalArgumentException("X data are constant."); 
/*  84 */       this.b = (paramArrayOfdouble2[1] - paramArrayOfdouble2[0]) / d;
/*  85 */       this.a = paramArrayOfdouble2[0] - this.b * paramArrayOfdouble1[0];
/*     */     }
/*     */     else {
/*     */       
/*  89 */       double d = 0.0D;
/*  90 */       this.ay = 0.0D; this.ax = 0.0D; this.sxx = 0.0D;
/*     */       byte b1;
/*  92 */       for (b1 = 0; b1 < this.n; b1++) {
/*  93 */         this.ax += paramArrayOfdouble1[b1]; this.ay += paramArrayOfdouble2[b1];
/*  94 */       }  this.ax /= this.n;
/*  95 */       this.ay /= this.n;
/*  96 */       for (b1 = 0; b1 < this.n; b1++) {
/*     */         
/*  98 */         double d1 = paramArrayOfdouble1[b1] - this.ax;
/*  99 */         double d2 = paramArrayOfdouble2[b1] - this.ay;
/* 100 */         this.sxx += d1 * d1;
/* 101 */         d += d1 * d2;
/*     */       } 
/*     */ 
/*     */       
/* 105 */       if (this.sxx <= 0.0D) {
/* 106 */         throw new IllegalArgumentException("X data are constant.");
/*     */       }
/*     */       
/* 109 */       this.b = d / this.sxx;
/* 110 */       this.a = this.ay - this.b * this.ax;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.chi2 = 0.0D;
/* 116 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/* 118 */       double d = paramArrayOfdouble2[b] - this.a - this.b * paramArrayOfdouble1[b];
/* 119 */       this.chi2 += d * d;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void weightedLineFit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
/* 130 */     double d2 = 0.0D, d3 = 0.0D, d4 = 0.0D;
/*     */     
/* 132 */     this.b = 0.0D;
/* 133 */     double d5 = 0.0D; byte b;
/* 134 */     for (b = 0; b < this.n; b++) {
/*     */       
/* 136 */       double d = paramArrayOfdouble3[b] * paramArrayOfdouble3[b];
/* 137 */       d5 += d;
/* 138 */       d2 += paramArrayOfdouble1[b] * d;
/* 139 */       d3 += paramArrayOfdouble2[b] * d;
/*     */     } 
/* 141 */     if (d5 <= 0.0D)
/* 142 */       throw new IllegalArgumentException("Zero weights."); 
/* 143 */     double d1 = d2 / d5;
/* 144 */     for (b = 0; b < this.n; b++) {
/*     */       
/* 146 */       double d = (paramArrayOfdouble1[b] - d1) * paramArrayOfdouble3[b];
/* 147 */       d4 += d * d;
/* 148 */       this.b += d * paramArrayOfdouble2[b] * paramArrayOfdouble3[b];
/*     */     } 
/* 150 */     if (d4 <= 0.0D)
/* 151 */       throw new IllegalArgumentException("Weighted X data are constant."); 
/* 152 */     this.b /= d4;
/* 153 */     this.a = (d3 - d2 * this.b) / d5;
/* 154 */     this.chi2 = 0.0D;
/* 155 */     for (b = 0; b < this.n; b++) {
/*     */       
/* 157 */       double d = (paramArrayOfdouble2[b] - this.a - this.b * paramArrayOfdouble1[b]) * paramArrayOfdouble3[b];
/* 158 */       this.chi2 += d * d;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getA() {
/* 167 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/* 174 */     return this.b;
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
/*     */   public ConfidenceInterval getCIA(double paramDouble) {
/* 186 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 187 */       throw new IllegalArgumentException("Invalid confidence coefficient"); 
/* 188 */     if (this.n < 3)
/* 189 */       throw new IllegalArgumentException("Insufficient data for CI."); 
/* 190 */     double d1 = getQuantileOfT(paramDouble);
/* 191 */     double d2 = this.chi2 / (this.n - 2);
/* 192 */     double d3 = Math.sqrt(d2 * (1.0D / this.n + this.ax * this.ax / this.sxx));
/*     */     
/* 194 */     double d4 = d1 * d3;
/* 195 */     return (ConfidenceInterval)new AbstractConfidenceInterval(paramDouble, this.a - d4, this.a + d4);
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
/*     */   public ConfidenceInterval getCIB(double paramDouble) {
/* 208 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 209 */       throw new IllegalArgumentException("Invalid confidence coefficient"); 
/* 210 */     if (this.n < 3)
/* 211 */       throw new IllegalArgumentException("Insufficient data for CI."); 
/* 212 */     double d1 = getQuantileOfT(paramDouble);
/* 213 */     double d2 = this.chi2 / (this.n - 2);
/* 214 */     double d3 = Math.sqrt(d2 / this.sxx);
/*     */     
/* 216 */     double d4 = d1 * d3;
/* 217 */     return (ConfidenceInterval)new AbstractConfidenceInterval(paramDouble, this.b - d4, this.b + d4);
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
/*     */   public double[][] getIntervals(double paramDouble1, int paramInt, double paramDouble2, double paramDouble3) {
/* 253 */     if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
/* 254 */       throw new IllegalArgumentException("Invalid confidence coefficient"); 
/* 255 */     if (this.n < 3)
/* 256 */       throw new IllegalArgumentException("Insufficient data for CI."); 
/* 257 */     if (paramDouble3 <= paramDouble2)
/* 258 */       throw new IllegalArgumentException("Invalid x values."); 
/* 259 */     double d1 = 1.0D / this.n;
/* 260 */     double d2 = getQuantileOfT(paramDouble1);
/* 261 */     double d3 = Math.sqrt(this.chi2 / (this.n - 2));
/* 262 */     double[][] arrayOfDouble = new double[paramInt][5];
/* 263 */     double d4 = (paramDouble3 - paramDouble2) / (paramInt - 1.0D);
/* 264 */     double d5 = d2 * d3;
/*     */     
/* 266 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 268 */       double d11 = paramDouble2 + b * d4;
/* 269 */       double d6 = d11 - this.ax;
/* 270 */       double d7 = this.a + this.b * d11;
/* 271 */       double d8 = d6 * d6 / this.sxx + d1;
/* 272 */       double d9 = d5 * Math.sqrt(d8);
/* 273 */       double d10 = d5 * Math.sqrt(d8 + 1.0D);
/* 274 */       arrayOfDouble[b][0] = d11;
/* 275 */       arrayOfDouble[b][1] = d7 - d9;
/* 276 */       arrayOfDouble[b][2] = d7 + d9;
/* 277 */       arrayOfDouble[b][3] = d7 - d10;
/* 278 */       arrayOfDouble[b][4] = d7 + d10;
/*     */     } 
/* 280 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanX() {
/* 288 */     return this.ax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanY() {
/* 295 */     return this.ay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 302 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   double getQuantileOfT(double paramDouble) {
/* 307 */     StudentsT studentsT = new StudentsT((this.n - 2));
/* 308 */     double d = 1.0D - paramDouble;
/* 309 */     return studentsT.inverseCdf(1.0D - 0.5D * d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSumOfSquares() {
/* 317 */     return this.chi2;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 346 */       double[] arrayOfDouble1 = { 8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D };
/* 347 */       double[] arrayOfDouble2 = { 8.0D, 9.0D, 10.0D, 11.0D, 12.0D };
/* 348 */       double[] arrayOfDouble3 = { 1.0D, 1.0D, 1.0D, 1.0D, 1.0D };
/* 349 */       LineFit lineFit1 = new LineFit(new PairedData(arrayOfDouble1, arrayOfDouble2));
/* 350 */       System.out.println("n = " + lineFit1.getN());
/* 351 */       System.out.println("a = " + lineFit1.getA());
/* 352 */       System.out.println("b = " + lineFit1.getB());
/* 353 */       System.out.println("ss = " + lineFit1.getSumOfSquares());
/*     */       
/* 355 */       LineFit lineFit2 = new LineFit(new PairedData(arrayOfDouble1, arrayOfDouble2), arrayOfDouble3);
/* 356 */       System.out.println("n = " + lineFit2.getN());
/* 357 */       System.out.println("a = " + lineFit2.getA());
/* 358 */       System.out.println("b = " + lineFit2.getB());
/* 359 */       System.out.println("ss = " + lineFit2.getSumOfSquares());
/*     */       
/* 361 */       double[] arrayOfDouble4 = { 0.1D, 0.2D };
/* 362 */       double[] arrayOfDouble5 = { 1.0D, 2.0D };
/* 363 */       LineFit lineFit3 = new LineFit(new PairedData(arrayOfDouble4, arrayOfDouble5));
/* 364 */       System.out.println("n = " + lineFit3.getN());
/* 365 */       System.out.println("a = " + lineFit3.getA());
/* 366 */       System.out.println("b = " + lineFit3.getB());
/* 367 */       System.out.println("ss = " + lineFit3.getSumOfSquares());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/LineFit.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */