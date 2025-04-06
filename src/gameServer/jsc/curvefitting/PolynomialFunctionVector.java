/*    */ package jsc.curvefitting;
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
/*    */ public class PolynomialFunctionVector
/*    */   implements FunctionVector
/*    */ {
/*    */   private final int degree;
/*    */   private final double[] f;
/*    */   
/*    */   PolynomialFunctionVector(int paramInt) {
/* 22 */     if (paramInt < 1)
/* 23 */       throw new IllegalArgumentException("Order must be > 0."); 
/* 24 */     this.degree = paramInt;
/* 25 */     this.f = new double[1 + paramInt];
/* 26 */     this.f[0] = 1.0D;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double[] function(double paramDouble) {
/* 38 */     this.f[1] = paramDouble;
/* 39 */     double d = paramDouble;
/* 40 */     for (byte b = 2; b <= this.degree; ) { d *= paramDouble; this.f[b] = d; b++; }
/* 41 */      return this.f;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/PolynomialFunctionVector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */