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
/*     */ class BSHAssignment
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   public int operator;
/*     */   
/*     */   BSHAssignment(int id) {
/*  41 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  47 */     BSHPrimaryExpression lhsNode = (BSHPrimaryExpression)jjtGetChild(0);
/*     */ 
/*     */     
/*  50 */     if (lhsNode == null) {
/*  51 */       throw new InterpreterError("Error, null LHSnode");
/*     */     }
/*  53 */     boolean strictJava = interpreter.getStrictJava();
/*  54 */     LHS lhs = lhsNode.toLHS(callstack, interpreter);
/*  55 */     if (lhs == null) {
/*  56 */       throw new InterpreterError("Error, null LHS");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  61 */     Object lhsValue = null;
/*  62 */     if (this.operator != 81) {
/*     */       try {
/*  64 */         lhsValue = lhs.getValue();
/*  65 */       } catch (UtilEvalError e) {
/*  66 */         throw e.toEvalError(this, callstack);
/*     */       } 
/*     */     }
/*  69 */     SimpleNode rhsNode = (SimpleNode)jjtGetChild(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     Object rhs = rhsNode.eval(callstack, interpreter);
/*     */     
/*  79 */     if (rhs == Primitive.VOID) {
/*  80 */       throw new EvalError("Void assignment.", this, callstack);
/*     */     }
/*     */     try {
/*  83 */       switch (this.operator) {
/*     */         
/*     */         case 81:
/*  86 */           return lhs.assign(rhs, strictJava);
/*     */         
/*     */         case 118:
/*  89 */           return lhs.assign(operation(lhsValue, rhs, 102), strictJava);
/*     */ 
/*     */         
/*     */         case 119:
/*  93 */           return lhs.assign(operation(lhsValue, rhs, 103), strictJava);
/*     */ 
/*     */         
/*     */         case 120:
/*  97 */           return lhs.assign(operation(lhsValue, rhs, 104), strictJava);
/*     */ 
/*     */         
/*     */         case 121:
/* 101 */           return lhs.assign(operation(lhsValue, rhs, 105), strictJava);
/*     */ 
/*     */         
/*     */         case 122:
/*     */         case 123:
/* 106 */           return lhs.assign(operation(lhsValue, rhs, 106), strictJava);
/*     */ 
/*     */         
/*     */         case 124:
/*     */         case 125:
/* 111 */           return lhs.assign(operation(lhsValue, rhs, 108), strictJava);
/*     */ 
/*     */         
/*     */         case 126:
/* 115 */           return lhs.assign(operation(lhsValue, rhs, 110), strictJava);
/*     */ 
/*     */         
/*     */         case 127:
/* 119 */           return lhs.assign(operation(lhsValue, rhs, 111), strictJava);
/*     */ 
/*     */         
/*     */         case 128:
/*     */         case 129:
/* 124 */           return lhs.assign(operation(lhsValue, rhs, 112), strictJava);
/*     */ 
/*     */         
/*     */         case 130:
/*     */         case 131:
/* 129 */           return lhs.assign(operation(lhsValue, rhs, 114), strictJava);
/*     */ 
/*     */         
/*     */         case 132:
/*     */         case 133:
/* 134 */           return lhs.assign(operation(lhsValue, rhs, 116), strictJava);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 139 */       throw new InterpreterError("unimplemented operator in assignment BSH");
/*     */     
/*     */     }
/* 142 */     catch (UtilEvalError e) {
/* 143 */       throw e.toEvalError(this, callstack);
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
/*     */   
/*     */   private Object operation(Object lhs, Object rhs, int kind) throws UtilEvalError {
/* 156 */     if (lhs instanceof String && rhs != Primitive.VOID) {
/* 157 */       if (kind != 102) {
/* 158 */         throw new UtilEvalError("Use of non + operator with String LHS");
/*     */       }
/*     */       
/* 161 */       return (String)lhs + rhs;
/*     */     } 
/*     */     
/* 164 */     if (lhs instanceof Primitive || rhs instanceof Primitive) {
/* 165 */       if (lhs == Primitive.VOID || rhs == Primitive.VOID) {
/* 166 */         throw new UtilEvalError("Illegal use of undefined object or 'void' literal");
/*     */       }
/* 168 */       if (lhs == Primitive.NULL || rhs == Primitive.NULL) {
/* 169 */         throw new UtilEvalError("Illegal use of null object or 'null' literal");
/*     */       }
/*     */     } 
/*     */     
/* 173 */     if ((lhs instanceof Boolean || lhs instanceof Character || lhs instanceof Number || lhs instanceof Primitive) && (rhs instanceof Boolean || rhs instanceof Character || rhs instanceof Number || rhs instanceof Primitive))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 178 */       return Primitive.binaryOperation(lhs, rhs, kind);
/*     */     }
/*     */     
/* 181 */     throw new UtilEvalError("Non primitive value in operator: " + lhs.getClass() + " " + tokenImage[kind] + " " + rhs.getClass());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHAssignment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */