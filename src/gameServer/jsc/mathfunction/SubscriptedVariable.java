/*     */ package jsc.mathfunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SubscriptedVariable
/*     */   implements MathFunctionVariables
/*     */ {
/*     */   private final int n;
/*     */   private final int firstSubscript;
/*     */   private final int lastSubscript;
/*     */   private final String name;
/*     */   private double[] x;
/*     */   
/*     */   public SubscriptedVariable(String paramString, int paramInt1, int paramInt2) {
/*  38 */     this.n = 1 + paramInt2 - paramInt1;
/*  39 */     if (this.n < 1)
/*  40 */       throw new IllegalArgumentException("Invalid subscripts."); 
/*  41 */     this.x = new double[this.n];
/*  42 */     this.name = paramString;
/*  43 */     this.firstSubscript = paramInt1;
/*  44 */     this.lastSubscript = paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubscriptedVariable(String paramString, int paramInt) {
/*  54 */     this(paramString, 1, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfVariables() {
/*  60 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVariableName(int paramInt) {
/*  69 */     return this.name + (paramInt + this.firstSubscript);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVariableSubscript(int paramInt) {
/*  78 */     return paramInt + this.firstSubscript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getVariableValue(int paramInt) {
/*  87 */     return this.x[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariableValue(int paramInt, double paramDouble) {
/* 100 */     if (paramInt < 0 || paramInt >= this.n)
/* 101 */       throw new IllegalArgumentException("Invalid variable index."); 
/* 102 */     this.x[paramInt] = paramDouble;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/SubscriptedVariable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */