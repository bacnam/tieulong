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
/*    */ public class ExtendedMidpointRule
/*    */   implements IntegratingFunction
/*    */ {
/*    */   private double s;
/*    */   
/*    */   public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) {
/* 22 */     if (paramInt == 1) {
/* 23 */       return this.s = (paramDouble2 - paramDouble1) * paramFunction.function(0.5D * (paramDouble1 + paramDouble2));
/*    */     }
/*    */     
/*    */     int i;
/*    */     byte b;
/* 28 */     for (i = 1, b = 1; b < paramInt - 1; ) { i *= 3; b++; }
/* 29 */      double d2 = i;
/* 30 */     double d4 = (paramDouble2 - paramDouble1) / 3.0D * d2;
/* 31 */     double d5 = d4 + d4;
/* 32 */     double d1 = paramDouble1 + 0.5D * d4;
/* 33 */     double d3 = 0.0D;
/* 34 */     for (b = 1; b <= i; b++) {
/*    */       
/* 36 */       d3 += paramFunction.function(d1);
/* 37 */       d1 += d5;
/* 38 */       d3 += paramFunction.function(d1);
/* 39 */       d1 += d4;
/*    */     } 
/* 41 */     this.s = (this.s + (paramDouble2 - paramDouble1) * d3 / d2) / 3.0D;
/* 42 */     return this.s;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/ExtendedMidpointRule.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */