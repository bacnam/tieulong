/*    */ package jsc.mathfunction;
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
/*    */ public final class TwoVariables
/*    */   implements MathFunctionVariables
/*    */ {
/*    */   private final String xName;
/*    */   private final String yName;
/*    */   private double x;
/*    */   private double y;
/*    */   
/*    */   public TwoVariables(String paramString1, String paramString2) {
/* 29 */     this.xName = paramString1; this.yName = paramString2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumberOfVariables() {
/* 35 */     return 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVariableName(int paramInt) {
/* 44 */     return (paramInt == 0) ? this.xName : this.yName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getVariableValue(int paramInt) {
/* 53 */     return (paramInt == 0) ? this.x : this.y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVariableValue(int paramInt, double paramDouble) {
/* 64 */     if (paramInt == 0) { this.x = paramDouble; }
/* 65 */     else if (paramInt == 1) { this.y = paramDouble; }
/*    */     else
/* 67 */     { throw new IllegalArgumentException("Invalid variable index."); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/TwoVariables.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */