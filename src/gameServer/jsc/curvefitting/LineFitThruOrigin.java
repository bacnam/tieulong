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
/*     */ public class LineFitThruOrigin
/*     */   implements StraightLineFit
/*     */ {
/*     */   private final int n;
/*     */   private double b;
/*     */   private double chi2;
/*     */   private double ax;
/*     */   private double ay;
/*     */   private final double sxx;
/*     */   private final PairedData data;
/*     */   
/*     */   public LineFitThruOrigin(PairedData paramPairedData) {
/*  34 */     this.data = paramPairedData;
/*  35 */     this.n = paramPairedData.getN();
/*  36 */     double[] arrayOfDouble1 = paramPairedData.getX();
/*  37 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*     */     
/*  39 */     this.b = 0.0D;
/*     */ 
/*     */     
/*  42 */     double d1 = 0.0D;
/*  43 */     this.ay = 0.0D; this.ax = 0.0D;
/*  44 */     double d2 = 0.0D;
/*     */     byte b;
/*  46 */     for (b = 0; b < this.n; b++) {
/*  47 */       this.ax += arrayOfDouble1[b]; this.ay += arrayOfDouble2[b];
/*  48 */     }  this.ax /= this.n;
/*  49 */     this.ay /= this.n;
/*     */ 
/*     */     
/*  52 */     for (b = 0; b < this.n; b++) {
/*     */       
/*  54 */       double d3 = arrayOfDouble1[b] - this.ax;
/*  55 */       double d4 = arrayOfDouble2[b] - this.ay;
/*  56 */       d2 += d3 * d3;
/*  57 */       d1 += d3 * d4;
/*     */     } 
/*     */ 
/*     */     
/*  61 */     if (d2 <= 0.0D)
/*  62 */       throw new IllegalArgumentException("X data are constant."); 
/*  63 */     this.sxx = d2 + this.n * this.ax * this.ax;
/*  64 */     this.b = (d1 + this.n * this.ax * this.ay) / this.sxx;
/*     */ 
/*     */     
/*  67 */     this.chi2 = 0.0D;
/*  68 */     for (b = 0; b < this.n; b++) {
/*     */       
/*  70 */       double d = arrayOfDouble2[b] - this.b * arrayOfDouble1[b];
/*  71 */       this.chi2 += d * d;
/*     */     } 
/*     */   }
/*     */   public double getA() {
/*  75 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/*  82 */     return this.b;
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
/*     */   public ConfidenceInterval getCIB(double paramDouble) {
/*  94 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/*  95 */       throw new IllegalArgumentException("Invalid confidence coefficient."); 
/*  96 */     if (this.n < 2)
/*  97 */       throw new IllegalArgumentException("Insufficient data for CI."); 
/*  98 */     double d1 = getQuantileOfT(paramDouble);
/*  99 */     double d2 = this.chi2 / (this.n - 1);
/* 100 */     double d3 = Math.sqrt(d2 / this.sxx);
/*     */     
/* 102 */     double d4 = d1 * d3;
/* 103 */     return (ConfidenceInterval)new AbstractConfidenceInterval(paramDouble, this.b - d4, this.b + d4);
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
/*     */   public double[][] getIntervals(double paramDouble1, int paramInt, double paramDouble2, double paramDouble3) {
/* 138 */     if (paramDouble1 <= 0.0D || paramDouble1 >= 1.0D)
/* 139 */       throw new IllegalArgumentException("Invalid confidence coefficient"); 
/* 140 */     if (this.n < 2)
/* 141 */       throw new IllegalArgumentException("Insufficient data for CI."); 
/* 142 */     if (paramDouble3 <= paramDouble2)
/* 143 */       throw new IllegalArgumentException("Invalid x values."); 
/* 144 */     double d1 = getQuantileOfT(paramDouble1);
/* 145 */     double d2 = Math.sqrt(this.chi2 / (this.n - 1));
/* 146 */     double[][] arrayOfDouble = new double[paramInt][5];
/* 147 */     double d3 = (paramDouble3 - paramDouble2) / (paramInt - 1.0D);
/* 148 */     double d4 = d1 * d2;
/*     */     
/* 150 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 152 */       double d9 = paramDouble2 + b * d3;
/* 153 */       double d5 = this.b * d9;
/* 154 */       double d6 = d9 * d9 / this.sxx;
/* 155 */       double d7 = d4 * Math.sqrt(d6);
/* 156 */       double d8 = d4 * Math.sqrt(d6 + 1.0D);
/* 157 */       arrayOfDouble[b][0] = d9;
/* 158 */       arrayOfDouble[b][1] = d5 - d7;
/* 159 */       arrayOfDouble[b][2] = d5 + d7;
/* 160 */       arrayOfDouble[b][3] = d5 - d8;
/* 161 */       arrayOfDouble[b][4] = d5 + d8;
/*     */     } 
/* 163 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanX() {
/* 171 */     return this.ax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMeanY() {
/* 178 */     return this.ay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 185 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   double getQuantileOfT(double paramDouble) {
/* 190 */     StudentsT studentsT = new StudentsT((this.n - 1));
/* 191 */     double d = 1.0D - paramDouble;
/* 192 */     return studentsT.inverseCdf(1.0D - 0.5D * d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSumOfSquares() {
/* 200 */     return this.chi2;
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
/* 226 */       double[] arrayOfDouble1 = { 9.5D, 9.8D, 5.0D, 19.0D, 23.0D, 14.6D, 15.2D, 8.3D, 11.4D, 21.6D, 11.8D, 26.5D, 12.1D, 4.8D, 22.0D, 21.7D, 28.2D, 18.0D, 12.1D, 28.0D };
/*     */       
/* 228 */       double[] arrayOfDouble2 = { 10.7D, 11.7D, 6.5D, 25.6D, 29.4D, 16.3D, 17.2D, 9.5D, 18.4D, 28.8D, 19.7D, 31.2D, 16.6D, 6.5D, 29.0D, 25.7D, 40.5D, 26.5D, 14.2D, 33.1D };
/*     */       
/* 230 */       LineFitThruOrigin lineFitThruOrigin = new LineFitThruOrigin(new PairedData(arrayOfDouble1, arrayOfDouble2));
/* 231 */       System.out.println("n = " + lineFitThruOrigin.getN());
/* 232 */       System.out.println("b = " + lineFitThruOrigin.getB());
/* 233 */       System.out.println("rss = " + lineFitThruOrigin.getSumOfSquares());
/* 234 */       ConfidenceInterval confidenceInterval = lineFitThruOrigin.getCIB(0.9D);
/* 235 */       System.out.println("CI for b = [" + confidenceInterval.getLowerLimit() + ", " + confidenceInterval.getUpperLimit() + "]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/LineFitThruOrigin.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */