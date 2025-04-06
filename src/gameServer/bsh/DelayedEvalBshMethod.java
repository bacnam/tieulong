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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelayedEvalBshMethod
/*     */   extends BshMethod
/*     */ {
/*     */   String returnTypeDescriptor;
/*     */   BSHReturnType returnTypeNode;
/*     */   String[] paramTypeDescriptors;
/*     */   BSHFormalParameters paramTypesNode;
/*     */   transient CallStack callstack;
/*     */   transient Interpreter interpreter;
/*     */   
/*     */   DelayedEvalBshMethod(String name, String returnTypeDescriptor, BSHReturnType returnTypeNode, String[] paramNames, String[] paramTypeDescriptors, BSHFormalParameters paramTypesNode, BSHBlock methodBody, NameSpace declaringNameSpace, Modifiers modifiers, CallStack callstack, Interpreter interpreter) {
/*  69 */     super(name, null, paramNames, null, methodBody, declaringNameSpace, modifiers);
/*     */ 
/*     */     
/*  72 */     this.returnTypeDescriptor = returnTypeDescriptor;
/*  73 */     this.returnTypeNode = returnTypeNode;
/*  74 */     this.paramTypeDescriptors = paramTypeDescriptors;
/*  75 */     this.paramTypesNode = paramTypesNode;
/*  76 */     this.callstack = callstack;
/*  77 */     this.interpreter = interpreter;
/*     */   }
/*     */   public String getReturnTypeDescriptor() {
/*  80 */     return this.returnTypeDescriptor;
/*     */   }
/*     */   
/*     */   public Class getReturnType() {
/*  84 */     if (this.returnTypeNode == null) {
/*  85 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  89 */       return this.returnTypeNode.evalReturnType(this.callstack, this.interpreter);
/*  90 */     } catch (EvalError e) {
/*  91 */       throw new InterpreterError("can't eval return type: " + e);
/*     */     } 
/*     */   }
/*     */   public String[] getParamTypeDescriptors() {
/*  95 */     return this.paramTypeDescriptors;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class[] getParameterTypes() {
/*     */     try {
/* 101 */       return (Class[])this.paramTypesNode.eval(this.callstack, this.interpreter);
/* 102 */     } catch (EvalError e) {
/* 103 */       throw new InterpreterError("can't eval param types: " + e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/DelayedEvalBshMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */