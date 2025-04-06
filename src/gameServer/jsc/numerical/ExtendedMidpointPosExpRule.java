/*    */ package jsc.numerical;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtendedMidpointPosExpRule
/*    */   implements IntegratingFunction
/*    */ {
/*    */   private double s;
/*    */   
/*    */   public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) throws NumericalException {
/* 32 */     paramDouble2 = (paramDouble1 == 0.0D) ? 1.0D : Math.exp(-paramDouble1);
/* 33 */     paramDouble1 = 0.0D;
/*    */     
/* 35 */     if (paramInt == 1) {
/*    */       
/* 37 */       double d = 0.5D * (paramDouble1 + paramDouble2);
/* 38 */       return this.s = (paramDouble2 - paramDouble1) * paramFunction.function(-Math.log(d)) / d;
/*    */     } 
/*    */     int i;
/*    */     byte b;
/* 42 */     for (i = 1, b = 1; b < paramInt - 1; ) { i *= 3; b++; }
/* 43 */      double d2 = i;
/* 44 */     double d4 = (paramDouble2 - paramDouble1) / 3.0D * d2;
/* 45 */     double d5 = d4 + d4;
/* 46 */     double d1 = paramDouble1 + 0.5D * d4;
/* 47 */     double d3 = 0.0D;
/* 48 */     for (b = 1; b <= i; b++) {
/*    */       
/* 50 */       if (d1 <= 0.0D) throw new NumericalException("+infinity"); 
/* 51 */       d3 += paramFunction.function(-Math.log(d1)) / d1;
/* 52 */       d1 += d5;
/* 53 */       d3 += paramFunction.function(-Math.log(d1)) / d1;
/* 54 */       d1 += d4;
/*    */     } 
/* 56 */     this.s = (this.s + (paramDouble2 - paramDouble1) * d3 / d2) / 3.0D;
/* 57 */     return this.s;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/ExtendedMidpointPosExpRule.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */