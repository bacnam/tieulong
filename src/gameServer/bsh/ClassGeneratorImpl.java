/*     */ package bsh;
/*     */ 
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassGeneratorImpl
/*     */   extends ClassGenerator
/*     */ {
/*     */   public Class generateClass(String name, Modifiers modifiers, Class[] interfaces, Class superClass, BSHBlock block, boolean isInterface, CallStack callstack, Interpreter interpreter) throws EvalError {
/*  25 */     return generateClassImpl(name, modifiers, interfaces, superClass, block, isInterface, callstack, interpreter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeSuperclassMethod(BshClassManager bcm, Object instance, String methodName, Object[] args) throws UtilEvalError, ReflectError, InvocationTargetException {
/*  35 */     return invokeSuperclassMethodImpl(bcm, instance, methodName, args);
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
/*     */   public void setInstanceNameSpaceParent(Object instance, String className, NameSpace parent) {
/*  47 */     This ithis = ClassGeneratorUtil.getClassInstanceThis(instance, className);
/*     */     
/*  49 */     ithis.getNameSpace().setParent(parent);
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
/*     */   public static Class generateClassImpl(String name, Modifiers modifiers, Class[] interfaces, Class superClass, BSHBlock block, boolean isInterface, CallStack callstack, Interpreter interpreter) throws EvalError {
/*     */     try {
/*  66 */       Capabilities.setAccessibility(true);
/*  67 */     } catch (Unavailable e) {
/*     */       
/*  69 */       throw new EvalError("Defining classes currently requires reflective Accessibility.", block, callstack);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  74 */     NameSpace enclosingNameSpace = callstack.top();
/*  75 */     String packageName = enclosingNameSpace.getPackage();
/*  76 */     String className = enclosingNameSpace.isClass ? (enclosingNameSpace.getName() + "$" + name) : name;
/*     */     
/*  78 */     String fqClassName = (packageName == null) ? className : (packageName + "." + className);
/*     */     
/*  80 */     String bshStaticFieldName = "_bshStatic" + className;
/*     */     
/*  82 */     BshClassManager bcm = interpreter.getClassManager();
/*     */     
/*  84 */     bcm.definingClass(fqClassName);
/*     */ 
/*     */     
/*  87 */     NameSpace classStaticNameSpace = new NameSpace(enclosingNameSpace, className);
/*     */     
/*  89 */     classStaticNameSpace.isClass = true;
/*     */     
/*  91 */     callstack.push(classStaticNameSpace);
/*     */ 
/*     */ 
/*     */     
/*  95 */     block.evalBlock(callstack, interpreter, true, ClassNodeFilter.CLASSCLASSES);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     Variable[] variables = getDeclaredVariables(block, callstack, interpreter, packageName);
/*     */     
/* 102 */     DelayedEvalBshMethod[] methods = getDeclaredMethods(block, callstack, interpreter, packageName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     Class genClass = null;
/*     */ 
/*     */ 
/*     */     
/* 111 */     if (genClass == null) {
/*     */       
/* 113 */       Interpreter.debug("generating class: " + fqClassName);
/* 114 */       ClassGeneratorUtil classGenerator = new ClassGeneratorUtil(modifiers, className, packageName, superClass, interfaces, variables, methods, classStaticNameSpace, isInterface);
/*     */ 
/*     */       
/* 117 */       byte[] code = classGenerator.generateClass();
/*     */ 
/*     */       
/* 120 */       debugClasses(className, code);
/*     */ 
/*     */       
/* 123 */       genClass = bcm.defineClass(fqClassName, code);
/*     */     } else {
/*     */       
/* 126 */       Interpreter.debug("Found existing generated class: " + fqClassName);
/*     */     } 
/*     */     
/* 129 */     enclosingNameSpace.importClass(fqClassName.replace('$', '.'));
/*     */     
/*     */     try {
/* 132 */       classStaticNameSpace.setLocalVariable("_bshInstanceInitializer", block, false);
/*     */     }
/* 134 */     catch (UtilEvalError e) {
/* 135 */       throw new InterpreterError("unable to init static: " + e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 140 */     classStaticNameSpace.setClassStatic(genClass);
/*     */ 
/*     */     
/* 143 */     block.evalBlock(callstack, interpreter, true, ClassNodeFilter.CLASSSTATIC);
/*     */ 
/*     */ 
/*     */     
/* 147 */     callstack.pop();
/*     */     
/* 149 */     if (!genClass.isInterface()) {
/*     */       
/*     */       try {
/*     */         
/* 153 */         LHS lhs = Reflect.getLHSStaticField(genClass, bshStaticFieldName);
/* 154 */         lhs.assign(classStaticNameSpace.getThis(interpreter), false);
/*     */       }
/* 156 */       catch (Exception e) {
/* 157 */         throw new InterpreterError("Error in class gen setup: " + e);
/*     */       } 
/*     */     }
/*     */     
/* 161 */     bcm.doneDefiningClass(fqClassName);
/* 162 */     return genClass;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void debugClasses(String className, byte[] code) {
/* 167 */     String dir = System.getProperty("debugClasses");
/* 168 */     if (dir != null) {
/*     */       try {
/* 170 */         FileOutputStream out = new FileOutputStream(dir + "/" + className + ".class");
/*     */         
/* 172 */         out.write(code);
/* 173 */         out.close();
/* 174 */       } catch (IOException e) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Variable[] getDeclaredVariables(BSHBlock body, CallStack callstack, Interpreter interpreter, String defaultPackage) {
/* 182 */     List<Variable> vars = new ArrayList();
/* 183 */     for (int child = 0; child < body.jjtGetNumChildren(); child++) {
/*     */       
/* 185 */       SimpleNode node = (SimpleNode)body.jjtGetChild(child);
/* 186 */       if (node instanceof BSHTypedVariableDeclaration) {
/*     */         
/* 188 */         BSHTypedVariableDeclaration tvd = (BSHTypedVariableDeclaration)node;
/*     */         
/* 190 */         Modifiers modifiers = tvd.modifiers;
/*     */         
/* 192 */         String type = tvd.getTypeDescriptor(callstack, interpreter, defaultPackage);
/*     */ 
/*     */         
/* 195 */         BSHVariableDeclarator[] vardec = tvd.getDeclarators();
/* 196 */         for (int i = 0; i < vardec.length; i++) {
/*     */           
/* 198 */           String name = (vardec[i]).name;
/*     */           try {
/* 200 */             Variable var = new Variable(name, type, null, modifiers);
/*     */             
/* 202 */             vars.add(var);
/* 203 */           } catch (UtilEvalError e) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 210 */     return vars.<Variable>toArray(new Variable[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static DelayedEvalBshMethod[] getDeclaredMethods(BSHBlock body, CallStack callstack, Interpreter interpreter, String defaultPackage) throws EvalError {
/* 219 */     List<DelayedEvalBshMethod> methods = new ArrayList();
/* 220 */     for (int child = 0; child < body.jjtGetNumChildren(); child++) {
/*     */       
/* 222 */       SimpleNode node = (SimpleNode)body.jjtGetChild(child);
/* 223 */       if (node instanceof BSHMethodDeclaration) {
/*     */         
/* 225 */         BSHMethodDeclaration md = (BSHMethodDeclaration)node;
/* 226 */         md.insureNodesParsed();
/* 227 */         Modifiers modifiers = md.modifiers;
/* 228 */         String name = md.name;
/* 229 */         String returnType = md.getReturnTypeDescriptor(callstack, interpreter, defaultPackage);
/*     */         
/* 231 */         BSHReturnType returnTypeNode = md.getReturnTypeNode();
/* 232 */         BSHFormalParameters paramTypesNode = md.paramsNode;
/* 233 */         String[] paramTypes = paramTypesNode.getTypeDescriptors(callstack, interpreter, defaultPackage);
/*     */ 
/*     */         
/* 236 */         DelayedEvalBshMethod bm = new DelayedEvalBshMethod(name, returnType, returnTypeNode, md.paramsNode.getParamNames(), paramTypes, paramTypesNode, md.blockNode, null, modifiers, callstack, interpreter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 245 */         methods.add(bm);
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     return methods.<DelayedEvalBshMethod>toArray(new DelayedEvalBshMethod[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   static class ClassNodeFilter
/*     */     implements BSHBlock.NodeFilter
/*     */   {
/*     */     public static final int STATIC = 0;
/*     */     
/*     */     public static final int INSTANCE = 1;
/*     */     
/*     */     public static final int CLASSES = 2;
/*     */     
/* 262 */     public static ClassNodeFilter CLASSSTATIC = new ClassNodeFilter(0);
/*     */     
/* 264 */     public static ClassNodeFilter CLASSINSTANCE = new ClassNodeFilter(1);
/*     */     
/* 266 */     public static ClassNodeFilter CLASSCLASSES = new ClassNodeFilter(2);
/*     */     
/*     */     int context;
/*     */     
/*     */     private ClassNodeFilter(int context) {
/* 271 */       this.context = context;
/*     */     }
/*     */     
/*     */     public boolean isVisible(SimpleNode node) {
/* 275 */       if (this.context == 2) {
/* 276 */         return node instanceof BSHClassDeclaration;
/*     */       }
/*     */       
/* 279 */       if (node instanceof BSHClassDeclaration) {
/* 280 */         return false;
/*     */       }
/* 282 */       if (this.context == 0) {
/* 283 */         return isStatic(node);
/*     */       }
/* 285 */       if (this.context == 1) {
/* 286 */         return !isStatic(node);
/*     */       }
/*     */       
/* 289 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isStatic(SimpleNode node) {
/* 294 */       if (node instanceof BSHTypedVariableDeclaration) {
/* 295 */         return (((BSHTypedVariableDeclaration)node).modifiers != null && ((BSHTypedVariableDeclaration)node).modifiers.hasModifier("static"));
/*     */       }
/*     */ 
/*     */       
/* 299 */       if (node instanceof BSHMethodDeclaration) {
/* 300 */         return (((BSHMethodDeclaration)node).modifiers != null && ((BSHMethodDeclaration)node).modifiers.hasModifier("static"));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 305 */       if (node instanceof BSHBlock) {
/* 306 */         return false;
/*     */       }
/* 308 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object invokeSuperclassMethodImpl(BshClassManager bcm, Object instance, String methodName, Object[] args) throws UtilEvalError, ReflectError, InvocationTargetException {
/* 317 */     String superName = "_bshSuper" + methodName;
/*     */ 
/*     */     
/* 320 */     Class<?> clas = instance.getClass();
/* 321 */     Method superMethod = Reflect.resolveJavaMethod(bcm, clas, superName, Types.getTypes(args), false);
/*     */     
/* 323 */     if (superMethod != null) {
/* 324 */       return Reflect.invokeMethod(superMethod, instance, args);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 329 */     Class<?> superClass = clas.getSuperclass();
/* 330 */     superMethod = Reflect.resolveExpectedJavaMethod(bcm, superClass, instance, methodName, args, false);
/*     */ 
/*     */     
/* 333 */     return Reflect.invokeMethod(superMethod, instance, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class getExistingGeneratedClass(BshClassManager bcm, String fqClassName, String bshStaticFieldName) {
/* 344 */     Class genClass = bcm.classForName(fqClassName);
/*     */     
/* 346 */     boolean isGenClass = false;
/* 347 */     if (genClass != null) {
/*     */       try {
/* 349 */         isGenClass = (Reflect.resolveJavaField(genClass, bshStaticFieldName, true) != null);
/*     */       }
/* 351 */       catch (Exception e) {}
/*     */     }
/* 353 */     return isGenClass ? genClass : null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ClassGeneratorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */