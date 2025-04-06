/*    */ package jsc.util;
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
/*    */ public class Polynomial
/*    */ {
/*    */   private final int degree;
/*    */   private final double[] p;
/*    */   private final double[] q;
/*    */   private final double[] r;
/*    */   
/*    */   public Polynomial(double[] paramArrayOfdouble) {
/* 24 */     int i = paramArrayOfdouble.length;
/* 25 */     this.degree = i - 1;
/* 26 */     this.p = new double[i];
/* 27 */     this.q = new double[this.degree];
/* 28 */     this.r = new double[this.degree - 1];
/* 29 */     for (byte b = 0; b < i; ) { this.p[b] = paramArrayOfdouble[b]; b++; }
/*    */   
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
/*    */   public double eval(double paramDouble) {
/* 42 */     this.q[this.degree - 1] = this.p[this.degree];
/* 43 */     for (int i = this.degree - 1; i >= 1; i--)
/* 44 */       this.q[i - 1] = this.p[i] + paramDouble * this.q[i]; 
/* 45 */     return this.p[0] + paramDouble * this.q[0];
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
/*    */ 
/*    */ 
/*    */   
/*    */   public double[] evalDerivative(double paramDouble) {
/* 60 */     double[] arrayOfDouble = new double[2];
/* 61 */     arrayOfDouble[0] = eval(paramDouble);
/* 62 */     if (this.degree == 1) {
/* 63 */       arrayOfDouble[1] = this.p[1];
/*    */     } else {
/*    */       
/* 66 */       this.r[this.degree - 2] = this.q[this.degree - 1];
/* 67 */       for (int i = this.degree - 2; i >= 1; i--)
/* 68 */         this.r[i - 1] = this.q[i] + paramDouble * this.r[i]; 
/* 69 */       arrayOfDouble[1] = this.q[0] + paramDouble * this.r[0];
/*    */     } 
/* 71 */     return arrayOfDouble;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getCoefficient(int paramInt) {
/* 80 */     return this.p[paramInt];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDegree() {
/* 87 */     return this.degree;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Polynomial.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */