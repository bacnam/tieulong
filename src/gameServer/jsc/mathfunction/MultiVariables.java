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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MultiVariables
/*    */   implements MathFunctionVariables
/*    */ {
/*    */   private final int n;
/*    */   private String[] names;
/*    */   private double[] x;
/*    */   
/*    */   public MultiVariables(String[] paramArrayOfString) {
/* 31 */     this.n = paramArrayOfString.length;
/* 32 */     if (this.n < 1)
/* 33 */       throw new IllegalArgumentException("No names."); 
/* 34 */     this.x = new double[this.n];
/* 35 */     this.names = new String[this.n];
/* 36 */     System.arraycopy(paramArrayOfString, 0, this.names, 0, this.n);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumberOfVariables() {
/* 43 */     return this.n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVariableName(int paramInt) {
/* 51 */     return this.names[paramInt];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getVariableValue(int paramInt) {
/* 59 */     return this.x[paramInt];
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
/*    */   public void setVariableValue(int paramInt, double paramDouble) {
/* 72 */     if (paramInt < 0 || paramInt >= this.n)
/* 73 */       throw new IllegalArgumentException("Invalid variable index."); 
/* 74 */     this.x[paramInt] = paramDouble;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/MultiVariables.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */