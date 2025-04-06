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
/*     */ class BSHMethodDeclaration
/*     */   extends SimpleNode
/*     */ {
/*     */   public String name;
/*     */   BSHReturnType returnTypeNode;
/*     */   BSHFormalParameters paramsNode;
/*     */   BSHBlock blockNode;
/*     */   int firstThrowsClause;
/*     */   public Modifiers modifiers;
/*     */   Class returnType;
/*  54 */   int numThrows = 0;
/*     */   BSHMethodDeclaration(int id) {
/*  56 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void insureNodesParsed() {
/*  64 */     if (this.paramsNode != null) {
/*     */       return;
/*     */     }
/*  67 */     Object firstNode = jjtGetChild(0);
/*  68 */     this.firstThrowsClause = 1;
/*  69 */     if (firstNode instanceof BSHReturnType) {
/*     */       
/*  71 */       this.returnTypeNode = (BSHReturnType)firstNode;
/*  72 */       this.paramsNode = (BSHFormalParameters)jjtGetChild(1);
/*  73 */       if (jjtGetNumChildren() > 2 + this.numThrows)
/*  74 */         this.blockNode = (BSHBlock)jjtGetChild(2 + this.numThrows); 
/*  75 */       this.firstThrowsClause++;
/*     */     }
/*     */     else {
/*     */       
/*  79 */       this.paramsNode = (BSHFormalParameters)jjtGetChild(0);
/*  80 */       this.blockNode = (BSHBlock)jjtGetChild(1 + this.numThrows);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Class evalReturnType(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  91 */     insureNodesParsed();
/*  92 */     if (this.returnTypeNode != null) {
/*  93 */       return this.returnTypeNode.evalReturnType(callstack, interpreter);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String getReturnTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/* 101 */     insureNodesParsed();
/* 102 */     if (this.returnTypeNode == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     return this.returnTypeNode.getTypeDescriptor(callstack, interpreter, defaultPackage);
/*     */   }
/*     */ 
/*     */   
/*     */   BSHReturnType getReturnTypeNode() {
/* 110 */     insureNodesParsed();
/* 111 */     return this.returnTypeNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 121 */     this.returnType = evalReturnType(callstack, interpreter);
/* 122 */     evalNodes(callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     NameSpace namespace = callstack.top();
/* 133 */     BshMethod bshMethod = new BshMethod(this, namespace, this.modifiers);
/*     */     try {
/* 135 */       namespace.setMethod(this.name, bshMethod);
/* 136 */     } catch (UtilEvalError e) {
/* 137 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */     
/* 140 */     return Primitive.VOID;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void evalNodes(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 146 */     insureNodesParsed();
/*     */     
/*     */     int i;
/* 149 */     for (i = this.firstThrowsClause; i < this.numThrows + this.firstThrowsClause; i++) {
/* 150 */       ((BSHAmbiguousName)jjtGetChild(i)).toClass(callstack, interpreter);
/*     */     }
/*     */     
/* 153 */     this.paramsNode.eval(callstack, interpreter);
/*     */ 
/*     */     
/* 156 */     if (interpreter.getStrictJava()) {
/*     */       
/* 158 */       for (i = 0; i < this.paramsNode.paramTypes.length; i++) {
/* 159 */         if (this.paramsNode.paramTypes[i] == null)
/*     */         {
/*     */           
/* 162 */           throw new EvalError("(Strict Java Mode) Undeclared argument type, parameter: " + this.paramsNode.getParamNames()[i] + " in method: " + this.name, this, null);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 167 */       if (this.returnType == null)
/*     */       {
/*     */         
/* 170 */         throw new EvalError("(Strict Java Mode) Undeclared return type for method: " + this.name, this, null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return "MethodDeclaration: " + this.name;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHMethodDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */