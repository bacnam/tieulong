/*     */ package bsh;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class This
/*     */   implements Serializable, Runnable
/*     */ {
/*     */   NameSpace namespace;
/*     */   transient Interpreter declaringInterpreter;
/*     */   
/*     */   static This getThis(NameSpace namespace, Interpreter declaringInterpreter) {
/*     */     try {
/*     */       Class<?> c;
/*  84 */       if (Capabilities.canGenerateInterfaces()) {
/*  85 */         c = Class.forName("bsh.XThis");
/*  86 */       } else if (Capabilities.haveSwing()) {
/*  87 */         c = Class.forName("bsh.JThis");
/*     */       } else {
/*  89 */         return new This(namespace, declaringInterpreter);
/*     */       } 
/*  91 */       return (This)Reflect.constructObject(c, new Object[] { namespace, declaringInterpreter });
/*     */     
/*     */     }
/*  94 */     catch (Exception e) {
/*  95 */       Class c; throw new InterpreterError("internal error 1 in This: " + c);
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
/*     */   public Object getInterface(Class clas) throws UtilEvalError {
/* 110 */     if (clas.isInstance(this)) {
/* 111 */       return this;
/*     */     }
/* 113 */     throw new UtilEvalError("Dynamic proxy mechanism not available. Cannot construct interface type: " + clas);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getInterface(Class[] ca) throws UtilEvalError {
/* 124 */     for (int i = 0; i < ca.length; i++) {
/* 125 */       if (!ca[i].isInstance(this)) {
/* 126 */         throw new UtilEvalError("Dynamic proxy mechanism not available. Cannot construct interface type: " + ca[i]);
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected This(NameSpace namespace, Interpreter declaringInterpreter) {
/* 139 */     this.namespace = namespace;
/* 140 */     this.declaringInterpreter = declaringInterpreter;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameSpace getNameSpace() {
/* 145 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 149 */     return "'this' reference to Bsh object: " + this.namespace;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 154 */       invokeMethod("run", new Object[0]);
/* 155 */     } catch (EvalError e) {
/* 156 */       this.declaringInterpreter.error("Exception in runnable:" + e);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeMethod(String name, Object[] args) throws EvalError {
/* 174 */     return invokeMethod(name, args, null, null, null, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeMethod(String methodName, Object[] args, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo, boolean declaredOnly) throws EvalError {
/* 230 */     if (args != null) {
/*     */       
/* 232 */       Object[] oa = new Object[args.length];
/* 233 */       for (int i = 0; i < args.length; i++)
/* 234 */         oa[i] = (args[i] == null) ? Primitive.NULL : args[i]; 
/* 235 */       args = oa;
/*     */     } 
/*     */     
/* 238 */     if (interpreter == null)
/* 239 */       interpreter = this.declaringInterpreter; 
/* 240 */     if (callstack == null)
/* 241 */       callstack = new CallStack(this.namespace); 
/* 242 */     if (callerInfo == null) {
/* 243 */       callerInfo = SimpleNode.JAVACODE;
/*     */     }
/*     */     
/* 246 */     Class[] types = Types.getTypes(args);
/* 247 */     BshMethod bshMethod = null;
/*     */     try {
/* 249 */       bshMethod = this.namespace.getMethod(methodName, types, declaredOnly);
/* 250 */     } catch (UtilEvalError e) {}
/*     */ 
/*     */ 
/*     */     
/* 254 */     if (bshMethod != null) {
/* 255 */       return bshMethod.invoke(args, interpreter, callstack, callerInfo);
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
/* 267 */     if (methodName.equals("toString")) {
/* 268 */       return toString();
/*     */     }
/*     */     
/* 271 */     if (methodName.equals("hashCode")) {
/* 272 */       return new Integer(hashCode());
/*     */     }
/*     */     
/* 275 */     if (methodName.equals("equals")) {
/* 276 */       Object obj = args[0];
/* 277 */       return new Boolean((this == obj));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 284 */       bshMethod = this.namespace.getMethod("invoke", new Class[] { null, null });
/*     */     }
/* 286 */     catch (UtilEvalError e) {}
/*     */ 
/*     */     
/* 289 */     if (bshMethod != null) {
/* 290 */       return bshMethod.invoke(new Object[] { methodName, args }, interpreter, callstack, callerInfo);
/*     */     }
/*     */     
/* 293 */     throw new EvalError("Method " + StringUtil.methodString(methodName, types) + " not found in bsh scripted object: " + this.namespace.getName(), callerInfo, callstack);
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
/*     */   public static void bind(This ths, NameSpace namespace, Interpreter declaringInterpreter) {
/* 313 */     ths.namespace.setParent(namespace);
/* 314 */     ths.declaringInterpreter = declaringInterpreter;
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
/*     */   static boolean isExposedThisMethod(String name) {
/* 329 */     return (name.equals("getClass") || name.equals("invokeMethod") || name.equals("getInterface") || name.equals("wait") || name.equals("notify") || name.equals("notifyAll"));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/This.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */