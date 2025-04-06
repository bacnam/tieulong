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
/*     */ class BSHAllocationExpression
/*     */   extends SimpleNode
/*     */ {
/*     */   BSHAllocationExpression(int id) {
/*  45 */     super(id);
/*  46 */   } private static int innerClassCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*  52 */     SimpleNode type = (SimpleNode)jjtGetChild(0);
/*     */ 
/*     */     
/*  55 */     SimpleNode args = (SimpleNode)jjtGetChild(1);
/*     */     
/*  57 */     if (type instanceof BSHAmbiguousName) {
/*     */       
/*  59 */       BSHAmbiguousName name = (BSHAmbiguousName)type;
/*     */       
/*  61 */       if (args instanceof BSHArguments) {
/*  62 */         return objectAllocation(name, (BSHArguments)args, callstack, interpreter);
/*     */       }
/*     */       
/*  65 */       return objectArrayAllocation(name, (BSHArrayDimensions)args, callstack, interpreter);
/*     */     } 
/*     */ 
/*     */     
/*  69 */     return primitiveArrayAllocation((BSHPrimitiveType)type, (BSHArrayDimensions)args, callstack, interpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object objectAllocation(BSHAmbiguousName nameNode, BSHArguments argumentsNode, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  79 */     NameSpace namespace = callstack.top();
/*     */     
/*  81 */     Object[] args = argumentsNode.getArguments(callstack, interpreter);
/*  82 */     if (args == null) {
/*  83 */       throw new EvalError("Null args in new.", this, callstack);
/*     */     }
/*     */     
/*  86 */     Object obj = nameNode.toObject(callstack, interpreter, false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     obj = nameNode.toObject(callstack, interpreter, true);
/*     */ 
/*     */     
/*  94 */     Class type = null;
/*  95 */     if (obj instanceof ClassIdentifier) {
/*  96 */       type = ((ClassIdentifier)obj).getTargetClass();
/*     */     } else {
/*  98 */       throw new EvalError("Unknown class: " + nameNode.text, this, callstack);
/*     */     } 
/*     */ 
/*     */     
/* 102 */     boolean hasBody = (jjtGetNumChildren() > 2);
/*     */     
/* 104 */     if (hasBody) {
/*     */       
/* 106 */       BSHBlock body = (BSHBlock)jjtGetChild(2);
/* 107 */       if (type.isInterface()) {
/* 108 */         return constructWithInterfaceBody(type, args, body, callstack, interpreter);
/*     */       }
/*     */       
/* 111 */       return constructWithClassBody(type, args, body, callstack, interpreter);
/*     */     } 
/*     */     
/* 114 */     return constructObject(type, args, callstack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object constructObject(Class type, Object[] args, CallStack callstack) throws EvalError {
/*     */     Object obj;
/*     */     try {
/* 123 */       obj = Reflect.constructObject(type, args);
/* 124 */     } catch (ReflectError e) {
/* 125 */       throw new EvalError("Constructor error: " + e.getMessage(), this, callstack);
/*     */     }
/* 127 */     catch (InvocationTargetException e) {
/*     */       
/* 129 */       Interpreter.debug("The constructor threw an exception:\n\t" + e.getTargetException());
/*     */       
/* 131 */       throw new TargetError("Object constructor", e.getTargetException(), this, callstack, true);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 136 */     String className = type.getName();
/*     */     
/* 138 */     if (className.indexOf("$") == -1) {
/* 139 */       return obj;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     This ths = callstack.top().getThis(null);
/* 148 */     NameSpace instanceNameSpace = Name.getClassNameSpace(ths.getNameSpace());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     if (instanceNameSpace != null && className.startsWith(instanceNameSpace.getName() + "$")) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 160 */         ClassGenerator.getClassGenerator().setInstanceNameSpaceParent(obj, className, instanceNameSpace);
/*     */       }
/* 162 */       catch (UtilEvalError e) {
/* 163 */         throw e.toEvalError(this, callstack);
/*     */       } 
/*     */     }
/*     */     
/* 167 */     return obj;
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
/*     */   private Object constructWithClassBody(Class type, Object[] args, BSHBlock block, CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     Class clas;
/* 182 */     String name = callstack.top().getName() + "$" + ++innerClassCount;
/* 183 */     Modifiers modifiers = new Modifiers();
/* 184 */     modifiers.addModifier(0, "public");
/*     */     
/*     */     try {
/* 187 */       clas = ClassGenerator.getClassGenerator().generateClass(name, modifiers, null, type, block, false, callstack, interpreter);
/*     */ 
/*     */     
/*     */     }
/* 191 */     catch (UtilEvalError e) {
/* 192 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */     try {
/* 195 */       return Reflect.constructObject(clas, args);
/* 196 */     } catch (Exception e) {
/* 197 */       if (e instanceof InvocationTargetException) {
/* 198 */         e = (Exception)((InvocationTargetException)e).getTargetException();
/*     */       }
/* 200 */       if (Interpreter.DEBUG)
/* 201 */         e.printStackTrace(); 
/* 202 */       throw new EvalError("Error constructing inner class instance: " + e, this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object constructWithInterfaceBody(Class type, Object[] args, BSHBlock body, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 213 */     NameSpace namespace = callstack.top();
/* 214 */     NameSpace local = new NameSpace(namespace, "AnonymousBlock");
/* 215 */     callstack.push(local);
/* 216 */     body.eval(callstack, interpreter, true);
/* 217 */     callstack.pop();
/*     */ 
/*     */     
/* 220 */     local.importStatic(type);
/*     */     try {
/* 222 */       return local.getThis(interpreter).getInterface(type);
/* 223 */     } catch (UtilEvalError e) {
/* 224 */       throw e.toEvalError(this, callstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object objectArrayAllocation(BSHAmbiguousName nameNode, BSHArrayDimensions dimensionsNode, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 234 */     NameSpace namespace = callstack.top();
/* 235 */     Class type = nameNode.toClass(callstack, interpreter);
/* 236 */     if (type == null) {
/* 237 */       throw new EvalError("Class " + nameNode.getName(namespace) + " not found.", this, callstack);
/*     */     }
/*     */     
/* 240 */     return arrayAllocation(dimensionsNode, type, callstack, interpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object primitiveArrayAllocation(BSHPrimitiveType typeNode, BSHArrayDimensions dimensionsNode, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 249 */     Class type = typeNode.getType();
/*     */     
/* 251 */     return arrayAllocation(dimensionsNode, type, callstack, interpreter);
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
/*     */   private Object arrayAllocation(BSHArrayDimensions dimensionsNode, Class type, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 264 */     Object result = dimensionsNode.eval(type, callstack, interpreter);
/* 265 */     if (result != Primitive.VOID) {
/* 266 */       return result;
/*     */     }
/* 268 */     return arrayNewInstance(type, dimensionsNode, callstack);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object arrayNewInstance(Class<?> type, BSHArrayDimensions dimensionsNode, CallStack callstack) throws EvalError {
/* 305 */     if (dimensionsNode.numUndefinedDims > 0) {
/*     */       
/* 307 */       Object proto = Array.newInstance(type, new int[dimensionsNode.numUndefinedDims]);
/*     */       
/* 309 */       type = proto.getClass();
/*     */     } 
/*     */     
/*     */     try {
/* 313 */       return Array.newInstance(type, dimensionsNode.definedDimensions);
/*     */     }
/* 315 */     catch (NegativeArraySizeException e1) {
/* 316 */       throw new TargetError(e1, this, callstack);
/* 317 */     } catch (Exception e) {
/* 318 */       throw new EvalError("Can't construct primitive array: " + e.getMessage(), this, callstack);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHAllocationExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */