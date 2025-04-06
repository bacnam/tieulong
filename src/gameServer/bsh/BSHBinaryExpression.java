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
/*     */ class BSHBinaryExpression
/*     */   extends SimpleNode
/*     */   implements ParserConstants
/*     */ {
/*     */   public int kind;
/*     */   
/*     */   BSHBinaryExpression(int id) {
/*  47 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  52 */     Object lhs = ((SimpleNode)jjtGetChild(0)).eval(callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     if (this.kind == 35) {
/*     */ 
/*     */       
/*  60 */       if (lhs == Primitive.NULL) {
/*  61 */         return new Primitive(false);
/*     */       }
/*  63 */       Class<Primitive> clazz = ((BSHType)jjtGetChild(1)).getType(callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  76 */       if (lhs instanceof Primitive) {
/*  77 */         if (clazz == Primitive.class) {
/*  78 */           return new Primitive(true);
/*     */         }
/*  80 */         return new Primitive(false);
/*     */       } 
/*     */       
/*  83 */       boolean ret = Types.isJavaBaseAssignable(clazz, lhs.getClass());
/*  84 */       return new Primitive(ret);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     if (this.kind == 98 || this.kind == 99) {
/*  96 */       Object obj = lhs;
/*  97 */       if (isPrimitiveValue(lhs))
/*  98 */         obj = ((Primitive)lhs).getValue(); 
/*  99 */       if (obj instanceof Boolean && !((Boolean)obj).booleanValue())
/*     */       {
/* 101 */         return new Primitive(false);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 107 */     if (this.kind == 96 || this.kind == 97) {
/* 108 */       Object obj = lhs;
/* 109 */       if (isPrimitiveValue(lhs))
/* 110 */         obj = ((Primitive)lhs).getValue(); 
/* 111 */       if (obj instanceof Boolean && ((Boolean)obj).booleanValue() == true)
/*     */       {
/* 113 */         return new Primitive(true);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     boolean isLhsWrapper = isWrapper(lhs);
/* 123 */     Object rhs = ((SimpleNode)jjtGetChild(1)).eval(callstack, interpreter);
/* 124 */     boolean isRhsWrapper = isWrapper(rhs);
/* 125 */     if ((isLhsWrapper || isPrimitiveValue(lhs)) && (isRhsWrapper || isPrimitiveValue(rhs)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 131 */       if (!isLhsWrapper || !isRhsWrapper || this.kind != 90) {
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 139 */           return Primitive.binaryOperation(lhs, rhs, this.kind);
/* 140 */         } catch (UtilEvalError e) {
/* 141 */           throw e.toEvalError(this, callstack);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     switch (this.kind) {
/*     */       
/*     */       case 90:
/* 185 */         return new Primitive((lhs == rhs));
/*     */       
/*     */       case 95:
/* 188 */         return new Primitive((lhs != rhs));
/*     */       
/*     */       case 102:
/* 191 */         if (lhs instanceof String || rhs instanceof String) {
/* 192 */           return lhs.toString() + rhs.toString();
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 197 */     if (lhs instanceof Primitive || rhs instanceof Primitive) {
/* 198 */       if (lhs == Primitive.VOID || rhs == Primitive.VOID) {
/* 199 */         throw new EvalError("illegal use of undefined variable, class, or 'void' literal", this, callstack);
/*     */       }
/*     */ 
/*     */       
/* 203 */       if (lhs == Primitive.NULL || rhs == Primitive.NULL) {
/* 204 */         throw new EvalError("illegal use of null value or 'null' literal", this, callstack);
/*     */       }
/*     */     } 
/* 207 */     throw new EvalError("Operator: '" + tokenImage[this.kind] + "' inappropriate for objects", this, callstack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPrimitiveValue(Object obj) {
/* 216 */     return (obj instanceof Primitive && obj != Primitive.VOID && obj != Primitive.NULL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isWrapper(Object obj) {
/* 224 */     return (obj instanceof Boolean || obj instanceof Character || obj instanceof Number);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHBinaryExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */