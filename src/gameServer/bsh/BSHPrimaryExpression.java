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
/*     */ class BSHPrimaryExpression
/*     */   extends SimpleNode
/*     */ {
/*     */   BSHPrimaryExpression(int id) {
/*  39 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  47 */     return eval(false, callstack, interpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LHS toLHS(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  56 */     Object obj = eval(true, callstack, interpreter);
/*     */     
/*  58 */     if (!(obj instanceof LHS)) {
/*  59 */       throw new EvalError("Can't assign to:", this, callstack);
/*     */     }
/*  61 */     return (LHS)obj;
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
/*     */   private Object eval(boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  76 */     Object obj = jjtGetChild(0);
/*  77 */     int numChildren = jjtGetNumChildren();
/*     */     
/*  79 */     for (int i = 1; i < numChildren; i++) {
/*  80 */       obj = ((BSHPrimarySuffix)jjtGetChild(i)).doSuffix(obj, toLHS, callstack, interpreter);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (obj instanceof SimpleNode) {
/*  88 */       if (obj instanceof BSHAmbiguousName) {
/*  89 */         if (toLHS) {
/*  90 */           obj = ((BSHAmbiguousName)obj).toLHS(callstack, interpreter);
/*     */         } else {
/*     */           
/*  93 */           obj = ((BSHAmbiguousName)obj).toObject(callstack, interpreter);
/*     */         } 
/*     */       } else {
/*     */         
/*  97 */         if (toLHS)
/*     */         {
/*  99 */           throw new EvalError("Can't assign to prefix.", this, callstack);
/*     */         }
/*     */         
/* 102 */         obj = ((SimpleNode)obj).eval(callstack, interpreter);
/*     */       } 
/*     */     }
/* 105 */     if (obj instanceof LHS) {
/* 106 */       if (toLHS) {
/* 107 */         return obj;
/*     */       }
/*     */       try {
/* 110 */         return ((LHS)obj).getValue();
/* 111 */       } catch (UtilEvalError e) {
/* 112 */         throw e.toEvalError(this, callstack);
/*     */       } 
/*     */     } 
/* 115 */     return obj;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHPrimaryExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */