/*     */ package bsh;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BshClassManager
/*     */ {
/*  83 */   private static Object NOVALUE = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Interpreter declaringInterpreter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader externalClassLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected transient Hashtable absoluteClassCache = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected transient Hashtable absoluteNonClasses = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected transient Hashtable resolvedObjectMethods = new Hashtable<Object, Object>();
/* 114 */   protected transient Hashtable resolvedStaticMethods = new Hashtable<Object, Object>();
/*     */   
/* 116 */   protected transient Hashtable definingClasses = new Hashtable<Object, Object>();
/* 117 */   protected transient Hashtable definingClassesBaseNames = new Hashtable<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BshClassManager createClassManager(Interpreter interpreter) {
/*     */     BshClassManager bshClassManager;
/* 131 */     if (Capabilities.classExists("java.lang.ref.WeakReference") && Capabilities.classExists("java.util.HashMap") && Capabilities.classExists("bsh.classpath.ClassManagerImpl")) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 138 */         Class<?> clas = Class.forName("bsh.classpath.ClassManagerImpl");
/* 139 */         bshClassManager = (BshClassManager)clas.newInstance();
/* 140 */       } catch (Exception e) {
/* 141 */         throw new InterpreterError("Error loading classmanager: " + e);
/*     */       } 
/*     */     } else {
/* 144 */       bshClassManager = new BshClassManager();
/*     */     } 
/* 146 */     if (interpreter == null)
/* 147 */       interpreter = new Interpreter(); 
/* 148 */     bshClassManager.declaringInterpreter = interpreter;
/* 149 */     return bshClassManager;
/*     */   }
/*     */   
/*     */   public boolean classExists(String name) {
/* 153 */     return (classForName(name) != null);
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
/*     */   public Class classForName(String name) {
/* 166 */     if (isClassBeingDefined(name)) {
/* 167 */       throw new InterpreterError("Attempting to load class in the process of being defined: " + name);
/*     */     }
/*     */ 
/*     */     
/* 171 */     Class clas = null;
/*     */     try {
/* 173 */       clas = plainClassForName(name);
/* 174 */     } catch (ClassNotFoundException e) {}
/*     */ 
/*     */     
/* 177 */     if (clas == null) {
/* 178 */       clas = loadSourceClass(name);
/*     */     }
/* 180 */     return clas;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadSourceClass(String name) {
/* 186 */     String fileName = "/" + name.replace('.', '/') + ".java";
/* 187 */     InputStream in = getResourceAsStream(fileName);
/* 188 */     if (in == null) {
/* 189 */       return null;
/*     */     }
/*     */     try {
/* 192 */       System.out.println("Loading class from source file: " + fileName);
/* 193 */       this.declaringInterpreter.eval(new InputStreamReader(in));
/* 194 */     } catch (EvalError e) {
/*     */       
/* 196 */       System.err.println(e);
/*     */     } 
/*     */     try {
/* 199 */       return plainClassForName(name);
/* 200 */     } catch (ClassNotFoundException e) {
/* 201 */       System.err.println("Class not found in source file: " + name);
/* 202 */       return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Class plainClassForName(String name) throws ClassNotFoundException {
/* 223 */     Class<?> c = null;
/*     */     
/*     */     try {
/* 226 */       if (this.externalClassLoader != null) {
/* 227 */         c = this.externalClassLoader.loadClass(name);
/*     */       } else {
/* 229 */         c = Class.forName(name);
/*     */       } 
/* 231 */       cacheClassInfo(name, c);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 243 */     catch (NoClassDefFoundError e) {
/* 244 */       throw noClassDefFound(name, e);
/*     */     } 
/*     */     
/* 247 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String path) {
/* 256 */     URL url = null;
/* 257 */     if (this.externalClassLoader != null)
/*     */     {
/*     */       
/* 260 */       url = this.externalClassLoader.getResource(path.substring(1));
/*     */     }
/* 262 */     if (url == null) {
/* 263 */       url = Interpreter.class.getResource(path);
/*     */     }
/* 265 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getResourceAsStream(String path) {
/* 273 */     InputStream in = null;
/* 274 */     if (this.externalClassLoader != null)
/*     */     {
/*     */       
/* 277 */       in = this.externalClassLoader.getResourceAsStream(path.substring(1));
/*     */     }
/* 279 */     if (in == null) {
/* 280 */       in = Interpreter.class.getResourceAsStream(path);
/*     */     }
/* 282 */     return in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheClassInfo(String name, Class<?> value) {
/* 293 */     if (value != null) {
/* 294 */       this.absoluteClassCache.put(name, value);
/*     */     } else {
/* 296 */       this.absoluteNonClasses.put(name, NOVALUE);
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
/*     */   public void cacheResolvedMethod(Class clas, Class[] types, Method method) {
/* 308 */     if (Interpreter.DEBUG) {
/* 309 */       Interpreter.debug("cacheResolvedMethod putting: " + clas + " " + method);
/*     */     }
/*     */     
/* 312 */     SignatureKey sk = new SignatureKey(clas, method.getName(), types);
/* 313 */     if (Modifier.isStatic(method.getModifiers())) {
/* 314 */       this.resolvedStaticMethods.put(sk, method);
/*     */     } else {
/* 316 */       this.resolvedObjectMethods.put(sk, method);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Method getResolvedMethod(Class clas, String methodName, Class[] types, boolean onlyStatic) {
/* 327 */     SignatureKey sk = new SignatureKey(clas, methodName, types);
/*     */ 
/*     */ 
/*     */     
/* 331 */     Method method = (Method)this.resolvedStaticMethods.get(sk);
/* 332 */     if (method == null && !onlyStatic) {
/* 333 */       method = (Method)this.resolvedObjectMethods.get(sk);
/*     */     }
/* 335 */     if (Interpreter.DEBUG)
/*     */     {
/* 337 */       if (method == null) {
/* 338 */         Interpreter.debug("getResolvedMethod cache MISS: " + clas + " - " + methodName);
/*     */       } else {
/*     */         
/* 341 */         Interpreter.debug("getResolvedMethod cache HIT: " + clas + " - " + method);
/*     */       } 
/*     */     }
/* 344 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearCaches() {
/* 353 */     this.absoluteNonClasses = new Hashtable<Object, Object>();
/* 354 */     this.absoluteClassCache = new Hashtable<Object, Object>();
/* 355 */     this.resolvedObjectMethods = new Hashtable<Object, Object>();
/* 356 */     this.resolvedStaticMethods = new Hashtable<Object, Object>();
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
/*     */   public void setClassLoader(ClassLoader externalCL) {
/* 372 */     this.externalClassLoader = externalCL;
/* 373 */     classLoaderChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassPath(URL path) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 384 */     clearCaches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPath(URL[] cp) throws UtilEvalError {
/* 394 */     throw cmUnavailable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadAllClasses() throws UtilEvalError {
/* 404 */     throw cmUnavailable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadClasses(String[] classNames) throws UtilEvalError {
/* 415 */     throw cmUnavailable();
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
/*     */   public void reloadPackage(String pack) throws UtilEvalError {
/* 427 */     throw cmUnavailable();
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
/*     */   protected void doSuperImport() throws UtilEvalError {
/* 445 */     throw cmUnavailable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasSuperImport() {
/* 453 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getClassNameByUnqName(String name) throws UtilEvalError {
/* 463 */     throw cmUnavailable();
/*     */   }
/*     */   
/*     */   public void addListener(Listener l) {}
/*     */   
/*     */   public void removeListener(Listener l) {}
/*     */   
/*     */   public void dump(PrintWriter pw) {
/* 471 */     pw.println("BshClassManager: no class manager.");
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
/*     */   protected void definingClass(String className) {
/* 488 */     String baseName = Name.suffix(className, 1);
/* 489 */     int i = baseName.indexOf("$");
/* 490 */     if (i != -1)
/* 491 */       baseName = baseName.substring(i + 1); 
/* 492 */     String cur = (String)this.definingClassesBaseNames.get(baseName);
/* 493 */     if (cur != null) {
/* 494 */       throw new InterpreterError("Defining class problem: " + className + ": BeanShell cannot yet simultaneously define two or more " + "dependant classes of the same name.  Attempt to define: " + className + " while defining: " + cur);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 499 */     this.definingClasses.put(className, NOVALUE);
/* 500 */     this.definingClassesBaseNames.put(baseName, className);
/*     */   }
/*     */   
/*     */   protected boolean isClassBeingDefined(String className) {
/* 504 */     return (this.definingClasses.get(className) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getClassBeingDefined(String className) {
/* 512 */     String baseName = Name.suffix(className, 1);
/* 513 */     return (String)this.definingClassesBaseNames.get(baseName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doneDefiningClass(String className) {
/* 521 */     String baseName = Name.suffix(className, 1);
/* 522 */     this.definingClasses.remove(className);
/* 523 */     this.definingClassesBaseNames.remove(baseName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class defineClass(String name, byte[] code) {
/* 532 */     throw new InterpreterError("Can't create class (" + name + ") without class manager package.");
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
/*     */   protected void classLoaderChanged() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Error noClassDefFound(String className, Error e) {
/* 568 */     return new NoClassDefFoundError("A class required by class: " + className + " could not be loaded:\n" + e.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static UtilEvalError cmUnavailable() {
/* 574 */     return new Capabilities.Unavailable("ClassLoading features unavailable.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class SignatureKey
/*     */   {
/*     */     Class clas;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Class[] types;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String methodName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     int hashCode = 0;
/*     */     
/*     */     SignatureKey(Class clas, String methodName, Class[] types) {
/* 606 */       this.clas = clas;
/* 607 */       this.methodName = methodName;
/* 608 */       this.types = types;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 613 */       if (this.hashCode == 0) {
/*     */         
/* 615 */         this.hashCode = this.clas.hashCode() * this.methodName.hashCode();
/* 616 */         if (this.types == null)
/* 617 */           return this.hashCode; 
/* 618 */         for (int i = 0; i < this.types.length; i++) {
/* 619 */           int hc = (this.types[i] == null) ? 21 : this.types[i].hashCode();
/* 620 */           this.hashCode = this.hashCode * (i + 1) + hc;
/*     */         } 
/*     */       } 
/* 623 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 627 */       SignatureKey target = (SignatureKey)o;
/* 628 */       if (this.types == null)
/* 629 */         return (target.types == null); 
/* 630 */       if (this.clas != target.clas)
/* 631 */         return false; 
/* 632 */       if (!this.methodName.equals(target.methodName))
/* 633 */         return false; 
/* 634 */       if (this.types.length != target.types.length)
/* 635 */         return false; 
/* 636 */       for (int i = 0; i < this.types.length; i++) {
/*     */         
/* 638 */         if (this.types[i] == null) {
/*     */           
/* 640 */           if (target.types[i] != null) {
/* 641 */             return false;
/*     */           }
/* 643 */         } else if (!this.types[i].equals(target.types[i])) {
/* 644 */           return false;
/*     */         } 
/*     */       } 
/* 647 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void classLoaderChanged();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BshClassManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */