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
/*    */ public class ExtendedTrapezoidalRule
/*    */   implements IntegratingFunction
/*    */ {
/*    */   private double s;
/*    */   
/*    */   public double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) {
/* 22 */     if (paramInt == 1)
/* 23 */       return this.s = 0.5D * (paramDouble2 - paramDouble1) * (paramFunction.function(paramDouble1) + paramFunction.function(paramDouble2)); 
/*    */     int i;
/*    */     byte b;
/* 26 */     for (i = 1, b = 1; b < paramInt - 1; ) { i <<= 1; b++; }
/* 27 */      double d2 = i;
/* 28 */     double d4 = (paramDouble2 - paramDouble1) / d2;
/* 29 */     double d1 = paramDouble1 + 0.5D * d4; double d3;
/* 30 */     for (d3 = 0.0D, b = 1; b <= i; ) { d3 += paramFunction.function(d1); b++; d1 += d4; }
/* 31 */      this.s = 0.5D * (this.s + (paramDouble2 - paramDouble1) * d3 / d2);
/* 32 */     return this.s;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/ExtendedTrapezoidalRule.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */