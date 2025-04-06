/*     */ package jsc.curvefitting;
/*     */ 
/*     */ import Jama.Matrix;
/*     */ import Jama.SingularValueDecomposition;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Normal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GeneralLinearLeastSquares
/*     */ {
/*     */   private final int n;
/*     */   private final int ma;
/*     */   private final double[] weight;
/*     */   private double[] a;
/*     */   private Matrix cvm;
/*     */   private final FunctionVector fv;
/*     */   private double chisq;
/*     */   
/*     */   public GeneralLinearLeastSquares(PairedData paramPairedData, double[] paramArrayOfdouble, int paramInt, FunctionVector paramFunctionVector) {
/*  59 */     this.n = paramPairedData.getN();
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.ma = paramInt;
/*  64 */     if (this.n < paramInt)
/*  65 */       throw new IllegalArgumentException("Insufficient data to estimate model."); 
/*  66 */     if (paramArrayOfdouble == null) {
/*     */       
/*  68 */       this.weight = new double[this.n];
/*  69 */       for (byte b = 0; b < this.n; ) { this.weight[b] = 1.0D; b++; }
/*     */     
/*     */     } else {
/*     */       
/*  73 */       if (this.n != paramArrayOfdouble.length)
/*  74 */         throw new IllegalArgumentException("Weights array is wrong length."); 
/*  75 */       this.weight = paramArrayOfdouble;
/*     */     } 
/*     */     
/*  78 */     this.fv = paramFunctionVector;
/*  79 */     svdfit(paramPairedData.getX(), paramPairedData.getY());
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
/*     */   public GeneralLinearLeastSquares(PairedData paramPairedData, int paramInt, FunctionVector paramFunctionVector) {
/*  95 */     this(paramPairedData, null, paramInt, paramFunctionVector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getA() {
/* 103 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix getCovariance() {
/* 110 */     return this.cvm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getM() {
/* 117 */     return this.ma;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 124 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSumOfSquares() {
/* 131 */     return this.chisq;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double[] svbksb(Matrix paramMatrix1, double[] paramArrayOfdouble1, Matrix paramMatrix2, double[] paramArrayOfdouble2) {
/* 138 */     int i = paramMatrix1.getColumnDimension();
/* 139 */     int j = paramMatrix1.getRowDimension();
/*     */     
/* 141 */     double[] arrayOfDouble1 = new double[i]; byte b;
/* 142 */     for (b = 0; b < i; b++) {
/*     */       
/* 144 */       double d = 0.0D;
/* 145 */       if (paramArrayOfdouble1[b] != 0.0D) {
/*     */         
/* 147 */         for (byte b1 = 0; b1 < j; ) { d += paramMatrix1.get(b1, b) * paramArrayOfdouble2[b1]; b1++; }
/* 148 */          d /= paramArrayOfdouble1[b];
/*     */       } 
/* 150 */       arrayOfDouble1[b] = d;
/*     */     } 
/*     */     
/* 153 */     double[] arrayOfDouble2 = new double[i];
/* 154 */     for (b = 0; b < i; b++) {
/*     */       
/* 156 */       double d = 0.0D;
/* 157 */       for (byte b1 = 0; b1 < i; ) { d += paramMatrix2.get(b, b1) * arrayOfDouble1[b1]; b1++; }
/* 158 */        arrayOfDouble2[b] = d;
/*     */     } 
/* 160 */     return arrayOfDouble2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void svdfit(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 168 */     double d3 = this.n * Double.MIN_VALUE;
/*     */     
/* 170 */     double[] arrayOfDouble1 = new double[this.n];
/*     */ 
/*     */ 
/*     */     
/* 174 */     Matrix matrix1 = new Matrix(this.n, this.ma); byte b2;
/* 175 */     for (b2 = 0; b2 < this.n; b2++) {
/*     */       
/* 177 */       double[] arrayOfDouble = this.fv.function(paramArrayOfdouble1[b2]);
/* 178 */       double d = this.weight[b2];
/* 179 */       for (byte b = 0; b < this.ma; ) { matrix1.set(b2, b, arrayOfDouble[b] * d); b++; }
/* 180 */        arrayOfDouble1[b2] = paramArrayOfdouble2[b2] * d;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(matrix1);
/* 187 */     double[] arrayOfDouble2 = singularValueDecomposition.getSingularValues();
/* 188 */     Matrix matrix2 = singularValueDecomposition.getV();
/*     */ 
/*     */     
/* 191 */     double d1 = 0.0D; byte b1;
/* 192 */     for (b1 = 0; b1 < this.ma; b1++) {
/* 193 */       if (arrayOfDouble2[b1] > d1) d1 = arrayOfDouble2[b1]; 
/* 194 */     }  double d2 = d3 * d1;
/* 195 */     for (b1 = 0; b1 < this.ma; b1++) {
/* 196 */       if (arrayOfDouble2[b1] < d2) arrayOfDouble2[b1] = 0.0D; 
/*     */     } 
/* 198 */     matrix1 = singularValueDecomposition.getU();
/* 199 */     this.a = svbksb(matrix1, arrayOfDouble2, matrix2, arrayOfDouble1);
/*     */ 
/*     */     
/* 202 */     this.chisq = 0.0D;
/* 203 */     for (b2 = 0; b2 < this.n; b2++) {
/*     */       
/* 205 */       double[] arrayOfDouble = this.fv.function(paramArrayOfdouble1[b2]); double d5;
/* 206 */       for (d5 = 0.0D, b1 = 0; b1 < this.ma; ) { d5 += this.a[b1] * arrayOfDouble[b1]; b1++; }
/* 207 */        double d4 = (paramArrayOfdouble2[b2] - d5) * this.weight[b2];
/* 208 */       this.chisq += d4 * d4;
/*     */     } 
/*     */ 
/*     */     
/* 212 */     this.cvm = new Matrix(this.ma, this.ma);
/* 213 */     svdvar(matrix2, arrayOfDouble2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void svdvar(Matrix paramMatrix, double[] paramArrayOfdouble) {
/* 221 */     int i = paramArrayOfdouble.length;
/*     */     
/* 223 */     double[] arrayOfDouble = new double[i]; byte b;
/* 224 */     for (b = 0; b < i; b++) {
/*     */       
/* 226 */       arrayOfDouble[b] = 0.0D;
/* 227 */       if (paramArrayOfdouble[b] != 0.0D) arrayOfDouble[b] = 1.0D / paramArrayOfdouble[b] * paramArrayOfdouble[b]; 
/*     */     } 
/* 229 */     for (b = 0; b < i; b++) {
/*     */       
/* 231 */       for (byte b1 = 0; b1 <= b; b1++) {
/*     */         byte b2; double d;
/* 233 */         for (d = 0.0D, b2 = 0; b2 < i; ) { d += paramMatrix.get(b, b2) * paramMatrix.get(b1, b2) * arrayOfDouble[b2]; b2++; }
/* 234 */          this.cvm.set(b1, b, d);
/* 235 */         this.cvm.set(b, b1, d);
/*     */       } 
/*     */     } 
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
/* 249 */       byte b1 = 20;
/*     */       
/* 251 */       double[] arrayOfDouble1 = new double[b1];
/* 252 */       double[] arrayOfDouble2 = new double[b1];
/*     */       
/* 254 */       double[] arrayOfDouble3 = { 1.0D, 2.0D, 3.0D, -1.0D, 0.5D };
/* 255 */       int i = arrayOfDouble3.length;
/* 256 */       Normal normal = new Normal(0.0D, 0.01D);
/*     */       
/* 258 */       PolynomialFunctionVector polynomialFunctionVector = new PolynomialFunctionVector(i - 1);
/* 259 */       for (byte b2 = 0; b2 < b1; b2++) {
/*     */         
/* 261 */         arrayOfDouble1[b2] = 1.0D + b2;
/* 262 */         double[] arrayOfDouble = polynomialFunctionVector.function(arrayOfDouble1[b2]); double d; byte b;
/* 263 */         for (d = 0.0D, b = 0; b < i; ) { d += arrayOfDouble3[b] * arrayOfDouble[b]; b++; }
/* 264 */          arrayOfDouble2[b2] = d + normal.random();
/*     */       } 
/* 266 */       GeneralLinearLeastSquares generalLinearLeastSquares = new GeneralLinearLeastSquares(new PairedData(arrayOfDouble1, arrayOfDouble2), i, polynomialFunctionVector);
/* 267 */       double[] arrayOfDouble4 = generalLinearLeastSquares.getA();
/* 268 */       for (byte b3 = 0; b3 < generalLinearLeastSquares.getM(); b3++)
/* 269 */         System.out.println("a(" + b3 + ") = " + arrayOfDouble4[b3]); 
/* 270 */       System.out.println("Sum of squares = " + generalLinearLeastSquares.getSumOfSquares());
/* 271 */       System.out.println("\nCovariance matrix");
/* 272 */       Matrix matrix = generalLinearLeastSquares.getCovariance();
/* 273 */       matrix.print(12, 4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/GeneralLinearLeastSquares.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */