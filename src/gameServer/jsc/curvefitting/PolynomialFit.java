/*    */ package jsc.curvefitting;
/*    */ 
/*    */ import Jama.Matrix;
/*    */ import jsc.datastructures.PairedData;
/*    */ import jsc.distributions.Normal;
/*    */ import jsc.util.Polynomial;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PolynomialFit
/*    */   extends GeneralLinearLeastSquares
/*    */ {
/*    */   private final Polynomial poly;
/*    */   
/*    */   public PolynomialFit(PairedData paramPairedData, double[] paramArrayOfdouble, int paramInt) {
/* 31 */     super(paramPairedData, paramArrayOfdouble, paramInt + 1, new PolynomialFunctionVector(paramInt));
/* 32 */     this.poly = new Polynomial(getA());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PolynomialFit(PairedData paramPairedData, int paramInt) {
/* 43 */     this(paramPairedData, (double[])null, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Polynomial getPolynomial() {
/* 50 */     return this.poly;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 62 */       byte b1 = 20;
/*    */       
/* 64 */       double[] arrayOfDouble1 = new double[b1];
/* 65 */       double[] arrayOfDouble2 = new double[b1];
/*    */       
/* 67 */       double[] arrayOfDouble3 = { 1.0D, 2.0D, 3.0D, -1.0D, 0.5D };
/* 68 */       int i = arrayOfDouble3.length;
/* 69 */       int j = i - 1;
/* 70 */       Normal normal = new Normal(0.0D, 0.01D);
/*    */       
/* 72 */       for (byte b2 = 0; b2 < b1; b2++) {
/*    */         
/* 74 */         arrayOfDouble1[b2] = 1.0D + b2; double d; byte b;
/* 75 */         for (d = 1.0D, b = 1; b <= j; ) { d += arrayOfDouble3[b] * Math.pow(arrayOfDouble1[b2], b); b++; }
/* 76 */          arrayOfDouble2[b2] = d + normal.random();
/*    */       } 
/* 78 */       PolynomialFit polynomialFit = new PolynomialFit(new PairedData(arrayOfDouble1, arrayOfDouble2), j);
/* 79 */       Polynomial polynomial = polynomialFit.getPolynomial();
/* 80 */       for (byte b3 = 0; b3 < polynomialFit.getM(); b3++)
/* 81 */         System.out.println("a(" + b3 + ") = " + polynomial.getCoefficient(b3)); 
/* 82 */       System.out.println("Sum of squares = " + polynomialFit.getSumOfSquares());
/* 83 */       System.out.println("\nCovariance matrix");
/* 84 */       Matrix matrix = polynomialFit.getCovariance();
/* 85 */       matrix.print(12, 4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/PolynomialFit.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */