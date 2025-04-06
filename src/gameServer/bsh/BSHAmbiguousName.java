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
/*     */ class BSHAmbiguousName
/*     */   extends SimpleNode
/*     */ {
/*     */   public String text;
/*     */   
/*     */   BSHAmbiguousName(int id) {
/*  41 */     super(id);
/*     */   }
/*     */   
/*     */   public Name getName(NameSpace namespace) {
/*  45 */     return namespace.getNameResolver(this.text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object toObject(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  51 */     return toObject(callstack, interpreter, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object toObject(CallStack callstack, Interpreter interpreter, boolean forceClass) throws EvalError {
/*     */     try {
/*  59 */       return getName(callstack.top()).toObject(callstack, interpreter, forceClass);
/*     */     
/*     */     }
/*  62 */     catch (UtilEvalError e) {
/*     */       
/*  64 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class toClass(CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     try {
/*  72 */       return getName(callstack.top()).toClass();
/*  73 */     } catch (ClassNotFoundException e) {
/*  74 */       throw new EvalError(e.getMessage(), this, callstack);
/*  75 */     } catch (UtilEvalError e2) {
/*     */       
/*  77 */       throw e2.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LHS toLHS(CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     try {
/*  85 */       return getName(callstack.top()).toLHS(callstack, interpreter);
/*  86 */     } catch (UtilEvalError e) {
/*  87 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  98 */     throw new InterpreterError("Don't know how to eval an ambiguous name!  Use toObject() if you want an object.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return "AmbigousName: " + this.text;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHAmbiguousName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */