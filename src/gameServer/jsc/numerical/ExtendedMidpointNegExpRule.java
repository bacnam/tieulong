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
/*    */ 
/*    */ public class ExtendedMidpointNegExpRule
/*    */   implements IntegratingFunction
/*    */ {
/*    */   private double s;
/*    */   
/*    */   public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) throws NumericalException {
/* 33 */     paramDouble2 = (paramDouble2 == 0.0D) ? 1.0D : Math.exp(paramDouble2);
/* 34 */     paramDouble1 = 0.0D;
/*    */     
/* 36 */     if (paramInt == 1) {
/*    */       
/* 38 */       double d = 0.5D * (paramDouble1 + paramDouble2);
/* 39 */       return this.s = (paramDouble2 - paramDouble1) * paramFunction.function(Math.log(d)) / d;
/*    */     } 
/*    */     int i;
/*    */     byte b;
/* 43 */     for (i = 1, b = 1; b < paramInt - 1; ) { i *= 3; b++; }
/* 44 */      double d2 = i;
/* 45 */     double d4 = (paramDouble2 - paramDouble1) / 3.0D * d2;
/* 46 */     double d5 = d4 + d4;
/* 47 */     double d1 = paramDouble1 + 0.5D * d4;
/* 48 */     double d3 = 0.0D;
/* 49 */     for (b = 1; b <= i; b++) {
/*    */       
/* 51 */       if (d1 <= 0.0D) throw new NumericalException("-infinity"); 
/* 52 */       d3 += paramFunction.function(Math.log(d1)) / d1;
/* 53 */       d1 += d5;
/* 54 */       d3 += paramFunction.function(Math.log(d1)) / d1;
/* 55 */       d1 += d4;
/*    */     } 
/* 57 */     this.s = (this.s + (paramDouble2 - paramDouble1) * d3 / d2) / 3.0D;
/* 58 */     return this.s;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/ExtendedMidpointNegExpRule.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */