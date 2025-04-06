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
/*     */ class BSHUnaryExpression
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   public int kind;
/*     */   public boolean postfix = false;
/*     */   
/*     */   BSHUnaryExpression(int id) {
/*  42 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  47 */     SimpleNode node = (SimpleNode)jjtGetChild(0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  53 */       if (this.kind == 100 || this.kind == 101) {
/*  54 */         LHS lhs = ((BSHPrimaryExpression)node).toLHS(callstack, interpreter);
/*     */         
/*  56 */         return lhsUnaryOperation(lhs, interpreter.getStrictJava());
/*     */       } 
/*  58 */       return unaryOperation(node.eval(callstack, interpreter), this.kind);
/*     */     }
/*  60 */     catch (UtilEvalError e) {
/*  61 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Object lhsUnaryOperation(LHS lhs, boolean strictJava) throws UtilEvalError {
/*     */     Object retVal;
/*  68 */     if (Interpreter.DEBUG) Interpreter.debug("lhsUnaryOperation");
/*     */     
/*  70 */     Object prevalue = lhs.getValue();
/*  71 */     Object postvalue = unaryOperation(prevalue, this.kind);
/*     */ 
/*     */     
/*  74 */     if (this.postfix) {
/*  75 */       retVal = prevalue;
/*     */     } else {
/*  77 */       retVal = postvalue;
/*     */     } 
/*  79 */     lhs.assign(postvalue, strictJava);
/*  80 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object unaryOperation(Object op, int kind) throws UtilEvalError {
/*  85 */     if (op instanceof Boolean || op instanceof Character || op instanceof Number)
/*     */     {
/*  87 */       return primitiveWrapperUnaryOperation(op, kind);
/*     */     }
/*  89 */     if (!(op instanceof Primitive)) {
/*  90 */       throw new UtilEvalError("Unary operation " + tokenImage[kind] + " inappropriate for object");
/*     */     }
/*     */ 
/*     */     
/*  94 */     return Primitive.unaryOperation((Primitive)op, kind);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object primitiveWrapperUnaryOperation(Object val, int kind) throws UtilEvalError {
/* 100 */     Class<?> operandType = val.getClass();
/* 101 */     Object operand = Primitive.promoteToInteger(val);
/*     */     
/* 103 */     if (operand instanceof Boolean) {
/* 104 */       return new Boolean(Primitive.booleanUnaryOperation((Boolean)operand, kind));
/*     */     }
/*     */     
/* 107 */     if (operand instanceof Integer) {
/*     */       
/* 109 */       int result = Primitive.intUnaryOperation((Integer)operand, kind);
/*     */ 
/*     */       
/* 112 */       if (kind == 100 || kind == 101) {
/*     */         
/* 114 */         if (operandType == byte.class)
/* 115 */           return new Byte((byte)result); 
/* 116 */         if (operandType == short.class)
/* 117 */           return new Short((short)result); 
/* 118 */         if (operandType == char.class) {
/* 119 */           return new Character((char)result);
/*     */         }
/*     */       } 
/* 122 */       return new Integer(result);
/*     */     } 
/* 124 */     if (operand instanceof Long)
/* 125 */       return new Long(Primitive.longUnaryOperation((Long)operand, kind)); 
/* 126 */     if (operand instanceof Float)
/* 127 */       return new Float(Primitive.floatUnaryOperation((Float)operand, kind)); 
/* 128 */     if (operand instanceof Double) {
/* 129 */       return new Double(Primitive.doubleUnaryOperation((Double)operand, kind));
/*     */     }
/* 131 */     throw new InterpreterError("An error occurred.  Please call technical support.");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHUnaryExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */