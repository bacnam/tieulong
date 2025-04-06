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
/*    */ public final class SingleVariable
/*    */   implements MathFunctionVariables
/*    */ {
/*    */   private final String name;
/*    */   private double x;
/*    */   
/*    */   public SingleVariable(String paramString) {
/* 26 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumberOfVariables() {
/* 32 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVariableName(int paramInt) {
/* 40 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getVariableValue(int paramInt) {
/* 48 */     return this.x;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVariableValue(double paramDouble) {
/* 55 */     this.x = paramDouble;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/SingleVariable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */