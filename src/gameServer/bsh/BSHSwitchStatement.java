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
/*     */ class BSHSwitchStatement
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   public BSHSwitchStatement(int id) {
/*  41 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  46 */     int numchild = jjtGetNumChildren();
/*  47 */     int child = 0;
/*  48 */     SimpleNode switchExp = (SimpleNode)jjtGetChild(child++);
/*  49 */     Object switchVal = switchExp.eval(callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     ReturnControl returnControl = null;
/*     */ 
/*     */     
/*  61 */     if (child >= numchild)
/*  62 */       throw new EvalError("Empty switch statement.", this, callstack); 
/*  63 */     BSHSwitchLabel label = (BSHSwitchLabel)jjtGetChild(child++);
/*     */ 
/*     */     
/*  66 */     while (child < numchild && returnControl == null) {
/*     */ 
/*     */       
/*  69 */       if (label.isDefault || primitiveEquals(switchVal, label.eval(callstack, interpreter), callstack, switchExp)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  76 */         while (child < numchild) {
/*     */           
/*  78 */           Object node = jjtGetChild(child++);
/*  79 */           if (node instanceof BSHSwitchLabel) {
/*     */             continue;
/*     */           }
/*  82 */           Object value = ((SimpleNode)node).eval(callstack, interpreter);
/*     */ 
/*     */ 
/*     */           
/*  86 */           if (value instanceof ReturnControl) {
/*  87 */             returnControl = (ReturnControl)value;
/*     */           }
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*  94 */       while (child < numchild) {
/*     */         
/*  96 */         Object node = jjtGetChild(child++);
/*  97 */         if (node instanceof BSHSwitchLabel) {
/*  98 */           label = (BSHSwitchLabel)node;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (returnControl != null && returnControl.kind == 46) {
/* 106 */       return returnControl;
/*     */     }
/* 108 */     return Primitive.VOID;
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
/*     */   private boolean primitiveEquals(Object switchVal, Object targetVal, CallStack callstack, SimpleNode switchExp) throws EvalError {
/* 120 */     if (switchVal instanceof Primitive || targetVal instanceof Primitive) {
/*     */       
/*     */       try {
/* 123 */         Object result = Primitive.binaryOperation(switchVal, targetVal, 90);
/*     */         
/* 125 */         result = Primitive.unwrap(result);
/* 126 */         return result.equals(Boolean.TRUE);
/* 127 */       } catch (UtilEvalError e) {
/* 128 */         throw e.toEvalError("Switch value: " + switchExp.getText() + ": ", this, callstack);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 133 */     return switchVal.equals(targetVal);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHSwitchStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */