/*     */ package bsh;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BSHArrayInitializer
/*     */   extends SimpleNode
/*     */ {
/*     */   BSHArrayInitializer(int id) {
/*  41 */     super(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  46 */     throw new EvalError("Array initializer has no base type.", this, callstack);
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
/*     */   public Object eval(Class<?> baseType, int dimensions, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  61 */     int numInitializers = jjtGetNumChildren();
/*     */ 
/*     */     
/*  64 */     int[] dima = new int[dimensions];
/*     */ 
/*     */     
/*  67 */     dima[0] = numInitializers;
/*  68 */     Object initializers = Array.newInstance(baseType, dima);
/*     */ 
/*     */     
/*  71 */     for (int i = 0; i < numInitializers; i++) {
/*     */       Object currentInitializer;
/*  73 */       SimpleNode node = (SimpleNode)jjtGetChild(i);
/*     */       
/*  75 */       if (node instanceof BSHArrayInitializer) {
/*  76 */         if (dimensions < 2) {
/*  77 */           throw new EvalError("Invalid Location for Intializer, position: " + i, this, callstack);
/*     */         }
/*     */         
/*  80 */         currentInitializer = ((BSHArrayInitializer)node).eval(baseType, dimensions - 1, callstack, interpreter);
/*     */       }
/*     */       else {
/*     */         
/*  84 */         currentInitializer = node.eval(callstack, interpreter);
/*     */       } 
/*  86 */       if (currentInitializer == Primitive.VOID) {
/*  87 */         throw new EvalError("Void in array initializer, position" + i, this, callstack);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       Object value = currentInitializer;
/* 100 */       if (dimensions == 1) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 105 */           value = Types.castObject(currentInitializer, baseType, 0);
/*     */         }
/* 107 */         catch (UtilEvalError e) {
/* 108 */           throw e.toEvalError("Error in array initializer", this, callstack);
/*     */         } 
/*     */ 
/*     */         
/* 112 */         value = Primitive.unwrap(value);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 117 */         Array.set(initializers, i, value);
/* 118 */       } catch (IllegalArgumentException e) {
/* 119 */         Interpreter.debug("illegal arg" + e);
/* 120 */         throwTypeError(baseType, currentInitializer, i, callstack);
/* 121 */       } catch (ArrayStoreException e) {
/* 122 */         Interpreter.debug("arraystore" + e);
/* 123 */         throwTypeError(baseType, currentInitializer, i, callstack);
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return initializers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwTypeError(Class baseType, Object initializer, int argNum, CallStack callstack) throws EvalError {
/*     */     String rhsType;
/* 135 */     if (initializer instanceof Primitive) {
/* 136 */       rhsType = ((Primitive)initializer).getType().getName();
/*     */     } else {
/*     */       
/* 139 */       rhsType = Reflect.normalizeClassName(initializer.getClass());
/*     */     } 
/*     */     
/* 142 */     throw new EvalError("Incompatible type: " + rhsType + " in initializer of array type: " + baseType + " at position: " + argNum, this, callstack);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHArrayInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */