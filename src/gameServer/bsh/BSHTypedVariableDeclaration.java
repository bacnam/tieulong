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
/*     */ class BSHTypedVariableDeclaration
/*     */   extends SimpleNode
/*     */ {
/*     */   public Modifiers modifiers;
/*     */   
/*     */   BSHTypedVariableDeclaration(int id) {
/*  40 */     super(id);
/*     */   }
/*     */   private BSHType getTypeNode() {
/*  43 */     return (BSHType)jjtGetChild(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Class evalType(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  49 */     BSHType typeNode = getTypeNode();
/*  50 */     return typeNode.getType(callstack, interpreter);
/*     */   }
/*     */ 
/*     */   
/*     */   BSHVariableDeclarator[] getDeclarators() {
/*  55 */     int n = jjtGetNumChildren();
/*  56 */     int start = 1;
/*  57 */     BSHVariableDeclarator[] bvda = new BSHVariableDeclarator[n - start];
/*  58 */     for (int i = start; i < n; i++)
/*     */     {
/*  60 */       bvda[i - start] = (BSHVariableDeclarator)jjtGetChild(i);
/*     */     }
/*  62 */     return bvda;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     try {
/*  73 */       NameSpace namespace = callstack.top();
/*  74 */       BSHType typeNode = getTypeNode();
/*  75 */       Class type = typeNode.getType(callstack, interpreter);
/*     */       
/*  77 */       BSHVariableDeclarator[] bvda = getDeclarators();
/*  78 */       for (int i = 0; i < bvda.length; i++) {
/*     */         
/*  80 */         BSHVariableDeclarator dec = bvda[i];
/*     */ 
/*     */ 
/*     */         
/*  84 */         Object value = dec.eval(typeNode, callstack, interpreter);
/*     */         
/*     */         try {
/*  87 */           namespace.setTypedVariable(dec.name, type, value, this.modifiers);
/*     */         }
/*  89 */         catch (UtilEvalError e) {
/*  90 */           throw e.toEvalError(this, callstack);
/*     */         } 
/*     */       } 
/*  93 */     } catch (EvalError e) {
/*  94 */       e.reThrow("Typed variable declaration");
/*     */     } 
/*     */     
/*  97 */     return Primitive.VOID;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/* 103 */     return getTypeNode().getTypeDescriptor(callstack, interpreter, defaultPackage);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHTypedVariableDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */