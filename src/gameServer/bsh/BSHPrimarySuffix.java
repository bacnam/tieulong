/*     */ package bsh;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BSHPrimarySuffix
/*     */   extends SimpleNode
/*     */ {
/*     */   public static final int CLASS = 0;
/*     */   public static final int INDEX = 1;
/*     */   public static final int NAME = 2;
/*     */   public static final int PROPERTY = 3;
/*     */   public int operation;
/*     */   Object index;
/*     */   public String field;
/*     */   
/*     */   BSHPrimarySuffix(int id) {
/*  53 */     super(id);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object doSuffix(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  75 */     if (this.operation == 0) {
/*  76 */       if (obj instanceof BSHType) {
/*  77 */         if (toLHS) {
/*  78 */           throw new EvalError("Can't assign .class", this, callstack);
/*     */         }
/*  80 */         NameSpace namespace = callstack.top();
/*  81 */         return ((BSHType)obj).getType(callstack, interpreter);
/*     */       } 
/*  83 */       throw new EvalError("Attempt to use .class suffix on non class.", this, callstack);
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
/*  99 */     if (obj instanceof SimpleNode) {
/* 100 */       if (obj instanceof BSHAmbiguousName) {
/* 101 */         obj = ((BSHAmbiguousName)obj).toObject(callstack, interpreter);
/*     */       } else {
/* 103 */         obj = ((SimpleNode)obj).eval(callstack, interpreter);
/*     */       } 
/* 105 */     } else if (obj instanceof LHS) {
/*     */       try {
/* 107 */         obj = ((LHS)obj).getValue();
/* 108 */       } catch (UtilEvalError e) {
/* 109 */         throw e.toEvalError(this, callstack);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 114 */       switch (this.operation) {
/*     */         
/*     */         case 1:
/* 117 */           return doIndex(obj, toLHS, callstack, interpreter);
/*     */         
/*     */         case 2:
/* 120 */           return doName(obj, toLHS, callstack, interpreter);
/*     */         
/*     */         case 3:
/* 123 */           return doProperty(toLHS, obj, callstack, interpreter);
/*     */       } 
/*     */       
/* 126 */       throw new InterpreterError("Unknown suffix type");
/*     */     
/*     */     }
/* 129 */     catch (ReflectError e) {
/*     */       
/* 131 */       throw new EvalError("reflection error: " + e, this, callstack);
/*     */     }
/* 133 */     catch (InvocationTargetException e) {
/*     */       
/* 135 */       throw new TargetError("target exception", e.getTargetException(), this, callstack, true);
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
/*     */ 
/*     */   
/*     */   private Object doName(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError, ReflectError, InvocationTargetException {
/*     */     try {
/* 151 */       if (this.field.equals("length") && obj.getClass().isArray()) {
/* 152 */         if (toLHS) {
/* 153 */           throw new EvalError("Can't assign array length", this, callstack);
/*     */         }
/*     */         
/* 156 */         return new Primitive(Array.getLength(obj));
/*     */       } 
/*     */       
/* 159 */       if (jjtGetNumChildren() == 0) {
/* 160 */         if (toLHS) {
/* 161 */           return Reflect.getLHSObjectField(obj, this.field);
/*     */         }
/* 163 */         return Reflect.getObjectFieldValue(obj, this.field);
/*     */       } 
/*     */ 
/*     */       
/* 167 */       Object[] oa = ((BSHArguments)jjtGetChild(0)).getArguments(callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 176 */         return Reflect.invokeObjectMethod(obj, this.field, oa, interpreter, callstack, this);
/*     */       }
/* 178 */       catch (ReflectError e) {
/* 179 */         throw new EvalError("Error in method invocation: " + e.getMessage(), this, callstack);
/*     */       
/*     */       }
/* 182 */       catch (InvocationTargetException e) {
/*     */         
/* 184 */         String msg = "Method Invocation " + this.field;
/* 185 */         Throwable te = e.getTargetException();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 192 */         boolean isNative = true;
/* 193 */         if (te instanceof EvalError)
/* 194 */           if (te instanceof TargetError) {
/* 195 */             isNative = ((TargetError)te).inNativeCode();
/*     */           } else {
/* 197 */             isNative = false;
/*     */           }  
/* 199 */         throw new TargetError(msg, te, this, callstack, isNative);
/*     */       }
/*     */     
/* 202 */     } catch (UtilEvalError e) {
/* 203 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getIndexAux(Object obj, CallStack callstack, Interpreter interpreter, SimpleNode callerInfo) throws EvalError {
/*     */     int index;
/* 214 */     if (!obj.getClass().isArray()) {
/* 215 */       throw new EvalError("Not an array", callerInfo, callstack);
/*     */     }
/*     */     
/*     */     try {
/* 219 */       Object indexVal = ((SimpleNode)callerInfo.jjtGetChild(0)).eval(callstack, interpreter);
/*     */ 
/*     */       
/* 222 */       if (!(indexVal instanceof Primitive)) {
/* 223 */         indexVal = Types.castObject(indexVal, int.class, 1);
/*     */       }
/* 225 */       index = ((Primitive)indexVal).intValue();
/* 226 */     } catch (UtilEvalError e) {
/* 227 */       Interpreter.debug("doIndex: " + e);
/* 228 */       throw e.toEvalError("Arrays may only be indexed by integer types.", callerInfo, callstack);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 233 */     return index;
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
/*     */   private Object doIndex(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError, ReflectError {
/* 245 */     int index = getIndexAux(obj, callstack, interpreter, this);
/* 246 */     if (toLHS) {
/* 247 */       return new LHS(obj, index);
/*     */     }
/*     */     try {
/* 250 */       return Reflect.getIndex(obj, index);
/* 251 */     } catch (UtilEvalError e) {
/* 252 */       throw e.toEvalError(this, callstack);
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
/*     */   private Object doProperty(boolean toLHS, Object obj, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 264 */     if (obj == Primitive.VOID) {
/* 265 */       throw new EvalError("Attempt to access property on undefined variable or class name", this, callstack);
/*     */     }
/*     */ 
/*     */     
/* 269 */     if (obj instanceof Primitive) {
/* 270 */       throw new EvalError("Attempt to access property on a primitive", this, callstack);
/*     */     }
/*     */     
/* 273 */     Object value = ((SimpleNode)jjtGetChild(0)).eval(callstack, interpreter);
/*     */ 
/*     */     
/* 276 */     if (!(value instanceof String)) {
/* 277 */       throw new EvalError("Property expression must be a String or identifier.", this, callstack);
/*     */     }
/*     */ 
/*     */     
/* 281 */     if (toLHS) {
/* 282 */       return new LHS(obj, (String)value);
/*     */     }
/*     */     
/* 285 */     CollectionManager cm = CollectionManager.getCollectionManager();
/* 286 */     if (cm.isMap(obj)) {
/*     */       
/* 288 */       Object val = cm.getFromMap(obj, value);
/* 289 */       return (val == null) ? (val = Primitive.NULL) : val;
/*     */     } 
/*     */     
/*     */     try {
/* 293 */       return Reflect.getObjectProperty(obj, (String)value);
/*     */     }
/* 295 */     catch (UtilEvalError e) {
/*     */       
/* 297 */       throw e.toEvalError("Property: " + value, this, callstack);
/*     */     }
/* 299 */     catch (ReflectError e) {
/*     */       
/* 301 */       throw new EvalError("No such property: " + value, this, callstack);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHPrimarySuffix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */