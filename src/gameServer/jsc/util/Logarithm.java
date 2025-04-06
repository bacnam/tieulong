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
/*    */ public class Logarithm
/*    */ {
/*    */   double base;
/*    */   double logBase;
/*    */   
/*    */   public Logarithm(double paramDouble) {
/* 22 */     this.base = paramDouble;
/* 23 */     this.logBase = Math.log(paramDouble);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double antilog(double paramDouble) {
/* 32 */     return Math.pow(this.base, paramDouble);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double log(double paramDouble) {
/* 40 */     return Math.log(paramDouble) / this.logBase;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Logarithm.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */