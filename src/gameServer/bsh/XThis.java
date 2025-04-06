/*     */ package bsh;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XThis
/*     */   extends This
/*     */ {
/*     */   Hashtable interfaces;
/*  68 */   InvocationHandler invocationHandler = new Handler();
/*     */   
/*     */   public XThis(NameSpace namespace, Interpreter declaringInterp) {
/*  71 */     super(namespace, declaringInterp);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  75 */     return "'this' reference (XThis) to Bsh object: " + this.namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getInterface(Class clas) {
/*  83 */     return getInterface(new Class[] { clas });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getInterface(Class[] ca) {
/*  91 */     if (this.interfaces == null) {
/*  92 */       this.interfaces = new Hashtable<Object, Object>();
/*     */     }
/*     */     
/*  95 */     int hash = 21;
/*  96 */     for (int i = 0; i < ca.length; i++)
/*  97 */       hash *= ca[i].hashCode() + 3; 
/*  98 */     Object hashKey = new Integer(hash);
/*     */     
/* 100 */     Object interf = this.interfaces.get(hashKey);
/*     */     
/* 102 */     if (interf == null) {
/*     */       
/* 104 */       ClassLoader classLoader = ca[0].getClassLoader();
/* 105 */       interf = Proxy.newProxyInstance(classLoader, ca, this.invocationHandler);
/*     */       
/* 107 */       this.interfaces.put(hashKey, interf);
/*     */     } 
/*     */     
/* 110 */     return interf;
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
/*     */   class Handler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */       try {
/* 131 */         return invokeImpl(proxy, method, args);
/* 132 */       } catch (TargetError te) {
/*     */ 
/*     */ 
/*     */         
/* 136 */         throw te.getTarget();
/* 137 */       } catch (EvalError ee) {
/*     */ 
/*     */         
/* 140 */         if (Interpreter.DEBUG) {
/* 141 */           Interpreter.debug("EvalError in scripted interface: " + XThis.this.toString() + ": " + ee);
/*     */         }
/* 143 */         throw ee;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object invokeImpl(Object proxy, Method method, Object[] args) throws EvalError {
/* 150 */       String methodName = method.getName();
/* 151 */       CallStack callstack = new CallStack(XThis.this.namespace);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       BshMethod equalsMethod = null;
/*     */       try {
/* 163 */         equalsMethod = XThis.this.namespace.getMethod("equals", new Class[] { Object.class });
/*     */       }
/* 165 */       catch (UtilEvalError e) {}
/* 166 */       if (methodName.equals("equals") && equalsMethod == null) {
/* 167 */         Object obj = args[0];
/* 168 */         return new Boolean((proxy == obj));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 175 */       BshMethod toStringMethod = null;
/*     */       try {
/* 177 */         toStringMethod = XThis.this.namespace.getMethod("toString", new Class[0]);
/*     */       }
/* 179 */       catch (UtilEvalError e) {}
/*     */       
/* 181 */       if (methodName.equals("toString") && toStringMethod == null) {
/*     */         
/* 183 */         Class[] ints = proxy.getClass().getInterfaces();
/*     */         
/* 185 */         StringBuffer sb = new StringBuffer(XThis.this.toString() + "\nimplements:");
/*     */         
/* 187 */         for (int i = 0; i < ints.length; i++) {
/* 188 */           sb.append(" " + ints[i].getName() + ((ints.length > 1) ? "," : ""));
/*     */         }
/* 190 */         return sb.toString();
/*     */       } 
/*     */       
/* 193 */       Class[] paramTypes = method.getParameterTypes();
/* 194 */       return Primitive.unwrap(XThis.this.invokeMethod(methodName, Primitive.wrap(args, paramTypes)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/XThis.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */