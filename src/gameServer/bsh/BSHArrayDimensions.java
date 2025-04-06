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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BSHArrayDimensions
/*     */   extends SimpleNode
/*     */ {
/*     */   public Class baseType;
/*     */   public int numDefinedDims;
/*     */   public int numUndefinedDims;
/*     */   public int[] definedDimensions;
/*     */   
/*     */   BSHArrayDimensions(int id) {
/*  55 */     super(id);
/*     */   }
/*  57 */   public void addDefinedDimension() { this.numDefinedDims++; } public void addUndefinedDimension() {
/*  58 */     this.numUndefinedDims++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(Class type, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  64 */     if (Interpreter.DEBUG) Interpreter.debug("array base type = " + type); 
/*  65 */     this.baseType = type;
/*  66 */     return eval(callstack, interpreter);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  84 */     SimpleNode child = (SimpleNode)jjtGetChild(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (child instanceof BSHArrayInitializer) {
/*     */       
/*  95 */       if (this.baseType == null) {
/*  96 */         throw new EvalError("Internal Array Eval err:  unknown base type", this, callstack);
/*     */       }
/*     */ 
/*     */       
/* 100 */       Object initValue = ((BSHArrayInitializer)child).eval(this.baseType, this.numUndefinedDims, callstack, interpreter);
/*     */ 
/*     */       
/* 103 */       Class<?> arrayClass = initValue.getClass();
/* 104 */       int actualDimensions = Reflect.getArrayDimensions(arrayClass);
/* 105 */       this.definedDimensions = new int[actualDimensions];
/*     */ 
/*     */ 
/*     */       
/* 109 */       if (this.definedDimensions.length != this.numUndefinedDims) {
/* 110 */         throw new EvalError("Incompatible initializer. Allocation calls for a " + this.numUndefinedDims + " dimensional array, but initializer is a " + actualDimensions + " dimensional array", this, callstack);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       Object arraySlice = initValue;
/* 117 */       for (int j = 0; j < this.definedDimensions.length; j++) {
/* 118 */         this.definedDimensions[j] = Array.getLength(arraySlice);
/* 119 */         if (this.definedDimensions[j] > 0) {
/* 120 */           arraySlice = Array.get(arraySlice, 0);
/*     */         }
/*     */       } 
/* 123 */       return initValue;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 128 */     this.definedDimensions = new int[this.numDefinedDims];
/*     */     
/* 130 */     for (int i = 0; i < this.numDefinedDims; i++) {
/*     */       
/*     */       try {
/* 133 */         Object length = ((SimpleNode)jjtGetChild(i)).eval(callstack, interpreter);
/*     */         
/* 135 */         this.definedDimensions[i] = ((Primitive)length).intValue();
/*     */       }
/* 137 */       catch (Exception e) {
/*     */         
/* 139 */         throw new EvalError("Array index: " + i + " does not evaluate to an integer", this, callstack);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     return Primitive.VOID;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHArrayDimensions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */