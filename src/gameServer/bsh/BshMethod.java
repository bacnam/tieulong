/*     */ package bsh;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BshMethod
/*     */   implements Serializable
/*     */ {
/*     */   NameSpace declaringNameSpace;
/*     */   Modifiers modifiers;
/*     */   private String name;
/*     */   private Class creturnType;
/*     */   private String[] paramNames;
/*     */   private int numArgs;
/*     */   private Class[] cparamTypes;
/*     */   BSHBlock methodBody;
/*     */   private Method javaMethod;
/*     */   private Object javaObject;
/*     */   
/*     */   BshMethod(BSHMethodDeclaration method, NameSpace declaringNameSpace, Modifiers modifiers) {
/*  89 */     this(method.name, method.returnType, method.paramsNode.getParamNames(), method.paramsNode.paramTypes, method.blockNode, declaringNameSpace, modifiers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BshMethod(String name, Class returnType, String[] paramNames, Class[] paramTypes, BSHBlock methodBody, NameSpace declaringNameSpace, Modifiers modifiers) {
/*  99 */     this.name = name;
/* 100 */     this.creturnType = returnType;
/* 101 */     this.paramNames = paramNames;
/* 102 */     if (paramNames != null)
/* 103 */       this.numArgs = paramNames.length; 
/* 104 */     this.cparamTypes = paramTypes;
/* 105 */     this.methodBody = methodBody;
/* 106 */     this.declaringNameSpace = declaringNameSpace;
/* 107 */     this.modifiers = modifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BshMethod(Method method, Object object) {
/* 116 */     this(method.getName(), method.getReturnType(), null, method.getParameterTypes(), null, null, null);
/*     */ 
/*     */ 
/*     */     
/* 120 */     this.javaMethod = method;
/* 121 */     this.javaObject = object;
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
/*     */   public Class[] getParameterTypes() {
/* 134 */     return this.cparamTypes; } public String[] getParameterNames() {
/* 135 */     return this.paramNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getReturnType() {
/* 146 */     return this.creturnType;
/*     */   } public Modifiers getModifiers() {
/* 148 */     return this.modifiers;
/*     */   } public String getName() {
/* 150 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object[] argValues, Interpreter interpreter) throws EvalError {
/* 161 */     return invoke(argValues, interpreter, null, null, false);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo) throws EvalError {
/* 186 */     return invoke(argValues, interpreter, callstack, callerInfo, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object invoke(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo, boolean overrideNameSpace) throws EvalError {
/* 215 */     if (argValues != null)
/* 216 */       for (int i = 0; i < argValues.length; i++) {
/* 217 */         if (argValues[i] == null)
/* 218 */           throw new Error("HERE!"); 
/*     */       }  
/* 220 */     if (this.javaMethod != null) {
/*     */       try {
/* 222 */         return Reflect.invokeMethod(this.javaMethod, this.javaObject, argValues);
/*     */       }
/* 224 */       catch (ReflectError e) {
/* 225 */         throw new EvalError("Error invoking Java method: " + e, callerInfo, callstack);
/*     */       }
/* 227 */       catch (InvocationTargetException e2) {
/* 228 */         throw new TargetError("Exception invoking imported object method.", e2, callerInfo, callstack, true);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 234 */     if (this.modifiers != null && this.modifiers.hasModifier("synchronized")) {
/*     */       Object object;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 240 */       if (this.declaringNameSpace.isClass) {
/*     */         
/*     */         try {
/* 243 */           object = this.declaringNameSpace.getClassInstance();
/* 244 */         } catch (UtilEvalError e) {
/* 245 */           throw new InterpreterError("Can't get class instance for synchronized method.");
/*     */         } 
/*     */       } else {
/*     */         
/* 249 */         object = this.declaringNameSpace.getThis(interpreter);
/*     */       } 
/* 251 */       synchronized (object) {
/*     */         
/* 253 */         return invokeImpl(argValues, interpreter, callstack, callerInfo, overrideNameSpace);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return invokeImpl(argValues, interpreter, callstack, callerInfo, overrideNameSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object invokeImpl(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo, boolean overrideNameSpace) throws EvalError {
/*     */     NameSpace localNameSpace;
/* 267 */     Class<void> returnType = getReturnType();
/* 268 */     Class[] paramTypes = getParameterTypes();
/*     */ 
/*     */     
/* 271 */     if (callstack == null) {
/* 272 */       callstack = new CallStack(this.declaringNameSpace);
/*     */     }
/* 274 */     if (argValues == null) {
/* 275 */       argValues = new Object[0];
/*     */     }
/*     */     
/* 278 */     if (argValues.length != this.numArgs)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       throw new EvalError("Wrong number of arguments for local method: " + this.name, callerInfo, callstack);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     if (overrideNameSpace) {
/* 302 */       localNameSpace = callstack.top();
/*     */     } else {
/*     */       
/* 305 */       localNameSpace = new NameSpace(this.declaringNameSpace, this.name);
/* 306 */       localNameSpace.isMethod = true;
/*     */     } 
/*     */     
/* 309 */     localNameSpace.setNode(callerInfo);
/*     */ 
/*     */     
/* 312 */     for (int i = 0; i < this.numArgs; i++) {
/*     */ 
/*     */       
/* 315 */       if (paramTypes[i] != null) {
/*     */         
/*     */         try {
/* 318 */           argValues[i] = Types.castObject(argValues[i], paramTypes[i], 1);
/*     */ 
/*     */         
/*     */         }
/* 322 */         catch (UtilEvalError e) {
/* 323 */           throw new EvalError("Invalid argument: `" + this.paramNames[i] + "'" + " for method: " + this.name + " : " + e.getMessage(), callerInfo, callstack);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 330 */           localNameSpace.setTypedVariable(this.paramNames[i], paramTypes[i], argValues[i], (Modifiers)null);
/*     */         }
/* 332 */         catch (UtilEvalError e2) {
/* 333 */           throw e2.toEvalError("Typed method parameter assignment", callerInfo, callstack);
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 341 */         if (argValues[i] == Primitive.VOID) {
/* 342 */           throw new EvalError("Undefined variable or class name, parameter: " + this.paramNames[i] + " to method: " + this.name, callerInfo, callstack);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 348 */           localNameSpace.setLocalVariable(this.paramNames[i], argValues[i], interpreter.getStrictJava());
/*     */         
/*     */         }
/* 351 */         catch (UtilEvalError e3) {
/* 352 */           throw e3.toEvalError(callerInfo, callstack);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 358 */     if (!overrideNameSpace) {
/* 359 */       callstack.push(localNameSpace);
/*     */     }
/*     */     
/* 362 */     Object ret = this.methodBody.eval(callstack, interpreter, true);
/*     */ 
/*     */ 
/*     */     
/* 366 */     CallStack returnStack = callstack.copy();
/*     */ 
/*     */     
/* 369 */     if (!overrideNameSpace) {
/* 370 */       callstack.pop();
/*     */     }
/* 372 */     ReturnControl retControl = null;
/* 373 */     if (ret instanceof ReturnControl) {
/*     */       
/* 375 */       retControl = (ReturnControl)ret;
/*     */ 
/*     */       
/* 378 */       if (retControl.kind == 46) {
/* 379 */         ret = ((ReturnControl)ret).value;
/*     */       } else {
/*     */         
/* 382 */         throw new EvalError("'continue' or 'break' in method body", retControl.returnPoint, returnStack);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 387 */       if (returnType == void.class && ret != Primitive.VOID) {
/* 388 */         throw new EvalError("Cannot return value from void method", retControl.returnPoint, returnStack);
/*     */       }
/*     */     } 
/*     */     
/* 392 */     if (returnType != null) {
/*     */ 
/*     */       
/* 395 */       if (returnType == void.class) {
/* 396 */         return Primitive.VOID;
/*     */       }
/*     */       
/*     */       try {
/* 400 */         ret = Types.castObject(ret, returnType, 1);
/*     */       
/*     */       }
/* 403 */       catch (UtilEvalError e) {
/*     */ 
/*     */ 
/*     */         
/* 407 */         SimpleNode node = callerInfo;
/* 408 */         if (retControl != null)
/* 409 */           node = retControl.returnPoint; 
/* 410 */         throw e.toEvalError("Incorrect type returned from method: " + this.name + e.getMessage(), node, callstack);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 416 */     return ret;
/*     */   }
/*     */   
/*     */   public boolean hasModifier(String name) {
/* 420 */     return (this.modifiers != null && this.modifiers.hasModifier(name));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 424 */     return "Scripted Method: " + StringUtil.methodString(this.name, getParameterTypes());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BshMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */