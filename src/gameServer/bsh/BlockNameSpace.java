/*     */ package bsh;
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
/*     */ class BlockNameSpace
/*     */   extends NameSpace
/*     */ {
/*     */   public BlockNameSpace(NameSpace parent) throws EvalError {
/*  57 */     super(parent, parent.getName() + "/BlockNameSpace");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariable(String name, Object value, boolean strictJava, boolean recurse) throws UtilEvalError {
/*  79 */     if (weHaveVar(name)) {
/*     */       
/*  81 */       super.setVariable(name, value, strictJava, false);
/*     */     } else {
/*     */       
/*  84 */       getParent().setVariable(name, value, strictJava, recurse);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockVariable(String name, Object value) throws UtilEvalError {
/*  96 */     super.setVariable(name, value, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean weHaveVar(String name) {
/*     */     
/* 108 */     try { return (getVariableImpl(name, false) != null); }
/* 109 */     catch (UtilEvalError e) { return false; }
/*     */   
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
/*     */   private NameSpace getNonBlockParent() {
/* 147 */     NameSpace parent = getParent();
/* 148 */     if (parent instanceof BlockNameSpace) {
/* 149 */       return ((BlockNameSpace)parent).getNonBlockParent();
/*     */     }
/* 151 */     return parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public This getThis(Interpreter declaringInterpreter) {
/* 162 */     return getNonBlockParent().getThis(declaringInterpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public This getSuper(Interpreter declaringInterpreter) {
/* 169 */     return getNonBlockParent().getSuper(declaringInterpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importClass(String name) {
/* 176 */     getParent().importClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importPackage(String name) {
/* 183 */     getParent().importPackage(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethod(String name, BshMethod method) throws UtilEvalError {
/* 189 */     getParent().setMethod(name, method);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BlockNameSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */