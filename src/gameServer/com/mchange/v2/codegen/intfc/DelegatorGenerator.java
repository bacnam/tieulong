/*     */ package com.mchange.v2.codegen.intfc;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatorGenerator
/*     */ {
/*  46 */   int class_modifiers = 1025;
/*  47 */   int method_modifiers = 1;
/*  48 */   int wrapping_ctor_modifiers = 1;
/*  49 */   int default_ctor_modifiers = 1;
/*     */   
/*     */   boolean wrapping_constructor = true;
/*     */   boolean default_constructor = true;
/*     */   boolean inner_getter = true;
/*     */   boolean inner_setter = true;
/*  55 */   Class superclass = null;
/*  56 */   Class[] extraInterfaces = null;
/*     */ 
/*     */   
/*  59 */   Method[] reflectiveDelegateMethods = null;
/*  60 */   ReflectiveDelegationPolicy reflectiveDelegationPolicy = ReflectiveDelegationPolicy.USE_MAIN_DELEGATE_INTERFACE;
/*     */   
/*  62 */   static final Comparator classComp = new Comparator()
/*     */     {
/*     */       public int compare(Object param1Object1, Object param1Object2) {
/*  65 */         return ((Class)param1Object1).getName().compareTo(((Class)param1Object2).getName());
/*     */       }
/*     */     };
/*     */   public void setGenerateInnerSetter(boolean paramBoolean) {
/*  69 */     this.inner_setter = paramBoolean;
/*     */   }
/*     */   public boolean isGenerateInnerSetter() {
/*  72 */     return this.inner_setter;
/*     */   }
/*     */   public void setGenerateInnerGetter(boolean paramBoolean) {
/*  75 */     this.inner_getter = paramBoolean;
/*     */   }
/*     */   public boolean isGenerateInnerGetter() {
/*  78 */     return this.inner_getter;
/*     */   }
/*     */   public void setGenerateNoArgConstructor(boolean paramBoolean) {
/*  81 */     this.default_constructor = paramBoolean;
/*     */   }
/*     */   public boolean isGenerateNoArgConstructor() {
/*  84 */     return this.default_constructor;
/*     */   }
/*     */   public void setGenerateWrappingConstructor(boolean paramBoolean) {
/*  87 */     this.wrapping_constructor = paramBoolean;
/*     */   }
/*     */   public boolean isGenerateWrappingConstructor() {
/*  90 */     return this.wrapping_constructor;
/*     */   }
/*     */   public void setWrappingConstructorModifiers(int paramInt) {
/*  93 */     this.wrapping_ctor_modifiers = paramInt;
/*     */   }
/*     */   public int getWrappingConstructorModifiers() {
/*  96 */     return this.wrapping_ctor_modifiers;
/*     */   }
/*     */   public void setNoArgConstructorModifiers(int paramInt) {
/*  99 */     this.default_ctor_modifiers = paramInt;
/*     */   }
/*     */   public int getNoArgConstructorModifiers() {
/* 102 */     return this.default_ctor_modifiers;
/*     */   }
/*     */   public void setMethodModifiers(int paramInt) {
/* 105 */     this.method_modifiers = paramInt;
/*     */   }
/*     */   public int getMethodModifiers() {
/* 108 */     return this.method_modifiers;
/*     */   }
/*     */   public void setClassModifiers(int paramInt) {
/* 111 */     this.class_modifiers = paramInt;
/*     */   }
/*     */   public int getClassModifiers() {
/* 114 */     return this.class_modifiers;
/*     */   }
/*     */   public void setSuperclass(Class paramClass) {
/* 117 */     this.superclass = paramClass;
/*     */   }
/*     */   public Class getSuperclass() {
/* 120 */     return this.superclass;
/*     */   }
/*     */   public void setExtraInterfaces(Class[] paramArrayOfClass) {
/* 123 */     this.extraInterfaces = paramArrayOfClass;
/*     */   }
/*     */   public Class[] getExtraInterfaces() {
/* 126 */     return this.extraInterfaces;
/*     */   }
/*     */   public Method[] getReflectiveDelegateMethods() {
/* 129 */     return this.reflectiveDelegateMethods;
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
/*     */   public void setReflectiveDelegateMethods(Method[] paramArrayOfMethod) {
/* 141 */     this.reflectiveDelegateMethods = paramArrayOfMethod;
/*     */   }
/*     */   public ReflectiveDelegationPolicy getReflectiveDelegationPolicy() {
/* 144 */     return this.reflectiveDelegationPolicy;
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
/*     */   public void setReflectiveDelegationPolicy(ReflectiveDelegationPolicy paramReflectiveDelegationPolicy) {
/* 159 */     this.reflectiveDelegationPolicy = paramReflectiveDelegationPolicy;
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
/*     */   public void writeDelegator(Class<?> paramClass, String paramString, Writer paramWriter) throws IOException {
/* 175 */     IndentedWriter indentedWriter = CodegenUtils.toIndentedWriter(paramWriter);
/*     */     
/* 177 */     String str1 = paramString.substring(0, paramString.lastIndexOf('.'));
/* 178 */     String str2 = CodegenUtils.fqcnLastElement(paramString);
/* 179 */     String str3 = (this.superclass != null) ? ClassUtils.simpleClassName(this.superclass) : null;
/* 180 */     String str4 = ClassUtils.simpleClassName(paramClass);
/* 181 */     String[] arrayOfString = null;
/* 182 */     if (this.extraInterfaces != null) {
/*     */       
/* 184 */       arrayOfString = new String[this.extraInterfaces.length]; byte b1; int j;
/* 185 */       for (b1 = 0, j = this.extraInterfaces.length; b1 < j; b1++) {
/* 186 */         arrayOfString[b1] = ClassUtils.simpleClassName(this.extraInterfaces[b1]);
/*     */       }
/*     */     } 
/* 189 */     TreeSet<Class<?>> treeSet = new TreeSet(classComp);
/*     */     
/* 191 */     Method[] arrayOfMethod = paramClass.getMethods();
/*     */ 
/*     */ 
/*     */     
/* 195 */     if (!CodegenUtils.inSamePackage(paramClass.getName(), paramString))
/* 196 */       treeSet.add(paramClass); 
/* 197 */     if (this.superclass != null && !CodegenUtils.inSamePackage(this.superclass.getName(), paramString))
/* 198 */       treeSet.add(this.superclass); 
/* 199 */     if (this.extraInterfaces != null) {
/*     */       byte b1; int j;
/* 201 */       for (b1 = 0, j = this.extraInterfaces.length; b1 < j; b1++) {
/*     */         
/* 203 */         Class<?> clazz = this.extraInterfaces[b1];
/* 204 */         if (!CodegenUtils.inSamePackage(clazz.getName(), paramString)) {
/* 205 */           treeSet.add(clazz);
/*     */         }
/*     */       } 
/*     */     } 
/* 209 */     ensureImports(paramString, treeSet, arrayOfMethod);
/*     */     
/* 211 */     if (this.reflectiveDelegateMethods != null) {
/* 212 */       ensureImports(paramString, treeSet, this.reflectiveDelegateMethods);
/*     */     }
/* 214 */     if (this.reflectiveDelegationPolicy.delegateClass != null && !CodegenUtils.inSamePackage(this.reflectiveDelegationPolicy.delegateClass.getName(), paramString)) {
/* 215 */       treeSet.add(this.reflectiveDelegationPolicy.delegateClass);
/*     */     }
/* 217 */     generateBannerComment(indentedWriter);
/* 218 */     indentedWriter.println("package " + str1 + ';');
/* 219 */     indentedWriter.println();
/* 220 */     for (Iterator<Class<?>> iterator = treeSet.iterator(); iterator.hasNext();)
/* 221 */       indentedWriter.println("import " + ((Class)iterator.next()).getName() + ';'); 
/* 222 */     generateExtraImports(indentedWriter);
/* 223 */     indentedWriter.println();
/* 224 */     generateClassJavaDocComment(indentedWriter);
/* 225 */     indentedWriter.print(CodegenUtils.getModifierString(this.class_modifiers) + " class " + str2);
/* 226 */     if (this.superclass != null)
/* 227 */       indentedWriter.print(" extends " + str3); 
/* 228 */     indentedWriter.print(" implements " + str4);
/* 229 */     if (arrayOfString != null) {
/* 230 */       byte b1; int j; for (b1 = 0, j = arrayOfString.length; b1 < j; b1++)
/* 231 */         indentedWriter.print(", " + arrayOfString[b1]); 
/* 232 */     }  indentedWriter.println();
/* 233 */     indentedWriter.println("{");
/* 234 */     indentedWriter.upIndent();
/*     */     
/* 236 */     indentedWriter.println("protected " + str4 + " inner;");
/* 237 */     indentedWriter.println();
/*     */     
/* 239 */     if (this.reflectiveDelegateMethods != null)
/* 240 */       indentedWriter.println("protected Class __delegateClass = null;"); 
/* 241 */     indentedWriter.println();
/*     */     
/* 243 */     indentedWriter.println("private void __setInner( " + str4 + " inner )");
/* 244 */     indentedWriter.println("{");
/* 245 */     indentedWriter.upIndent();
/* 246 */     indentedWriter.println("this.inner = inner;");
/* 247 */     if (this.reflectiveDelegateMethods != null) {
/*     */       String str;
/*     */ 
/*     */       
/* 251 */       if (this.reflectiveDelegationPolicy == ReflectiveDelegationPolicy.USE_MAIN_DELEGATE_INTERFACE) {
/* 252 */         str = str4 + ".class";
/* 253 */       } else if (this.reflectiveDelegationPolicy == ReflectiveDelegationPolicy.USE_RUNTIME_CLASS) {
/* 254 */         str = "inner.getClass()";
/*     */       } else {
/* 256 */         str = ClassUtils.simpleClassName(this.reflectiveDelegationPolicy.delegateClass) + ".class";
/*     */       } 
/* 258 */       indentedWriter.println("this.__delegateClass = inner == null ? null : " + str + ";");
/*     */     } 
/* 260 */     indentedWriter.downIndent();
/* 261 */     indentedWriter.println("}");
/* 262 */     indentedWriter.println();
/*     */     
/* 264 */     if (this.wrapping_constructor) {
/*     */ 
/*     */       
/* 267 */       indentedWriter.println(CodegenUtils.getModifierString(this.wrapping_ctor_modifiers) + ' ' + str2 + '(' + str4 + " inner)");
/* 268 */       indentedWriter.println("{ __setInner( inner ); }");
/*     */     } 
/*     */     
/* 271 */     if (this.default_constructor) {
/*     */       
/* 273 */       indentedWriter.println();
/* 274 */       indentedWriter.println(CodegenUtils.getModifierString(this.default_ctor_modifiers) + ' ' + str2 + "()");
/* 275 */       indentedWriter.println("{}");
/*     */     } 
/*     */     
/* 278 */     if (this.inner_setter) {
/*     */       
/* 280 */       indentedWriter.println();
/* 281 */       indentedWriter.println(CodegenUtils.getModifierString(this.method_modifiers) + " void setInner( " + str4 + " inner )");
/* 282 */       indentedWriter.println("{ __setInner( inner ); }");
/*     */     } 
/* 284 */     if (this.inner_getter) {
/*     */       
/* 286 */       indentedWriter.println();
/* 287 */       indentedWriter.println(CodegenUtils.getModifierString(this.method_modifiers) + ' ' + str4 + " getInner()");
/* 288 */       indentedWriter.println("{ return inner; }");
/*     */     } 
/* 290 */     indentedWriter.println(); byte b; int i;
/* 291 */     for (b = 0, i = arrayOfMethod.length; b < i; b++) {
/*     */       
/* 293 */       Method method = arrayOfMethod[b];
/*     */       
/* 295 */       if (b != 0) indentedWriter.println(); 
/* 296 */       indentedWriter.println(CodegenUtils.methodSignature(this.method_modifiers, method, null));
/* 297 */       indentedWriter.println("{");
/* 298 */       indentedWriter.upIndent();
/*     */       
/* 300 */       generatePreDelegateCode(paramClass, paramString, method, indentedWriter);
/* 301 */       generateDelegateCode(paramClass, paramString, method, indentedWriter);
/* 302 */       generatePostDelegateCode(paramClass, paramString, method, indentedWriter);
/*     */       
/* 304 */       indentedWriter.downIndent();
/* 305 */       indentedWriter.println("}");
/*     */     } 
/*     */     
/* 308 */     if (this.reflectiveDelegateMethods != null) {
/*     */       
/* 310 */       indentedWriter.println("// Methods not in core interface to be delegated via reflection");
/* 311 */       for (b = 0, i = this.reflectiveDelegateMethods.length; b < i; b++) {
/*     */         
/* 313 */         Method method = this.reflectiveDelegateMethods[b];
/*     */         
/* 315 */         if (b != 0) indentedWriter.println(); 
/* 316 */         indentedWriter.println(CodegenUtils.methodSignature(this.method_modifiers, method, null));
/* 317 */         indentedWriter.println("{");
/* 318 */         indentedWriter.upIndent();
/*     */         
/* 320 */         generatePreDelegateCode(paramClass, paramString, method, indentedWriter);
/* 321 */         generateReflectiveDelegateCode(paramClass, paramString, method, indentedWriter);
/* 322 */         generatePostDelegateCode(paramClass, paramString, method, indentedWriter);
/*     */         
/* 324 */         indentedWriter.downIndent();
/* 325 */         indentedWriter.println("}");
/*     */       } 
/*     */     } 
/*     */     
/* 329 */     indentedWriter.println();
/* 330 */     generateExtraDeclarations(paramClass, paramString, indentedWriter);
/*     */     
/* 332 */     indentedWriter.downIndent();
/* 333 */     indentedWriter.println("}");
/*     */   }
/*     */   private void ensureImports(String paramString, Set<Class<?>> paramSet, Method[] paramArrayOfMethod) {
/*     */     byte b;
/*     */     int i;
/* 338 */     for (b = 0, i = paramArrayOfMethod.length; b < i; b++) {
/*     */       
/* 340 */       Class[] arrayOfClass1 = paramArrayOfMethod[b].getParameterTypes(); int j;
/* 341 */       for (byte b1 = 0; b1 < j; b1++) {
/*     */         
/* 343 */         if (!CodegenUtils.inSamePackage(arrayOfClass1[b1].getName(), paramString))
/* 344 */           paramSet.add(CodegenUtils.unarrayClass(arrayOfClass1[b1])); 
/*     */       } 
/* 346 */       Class[] arrayOfClass2 = paramArrayOfMethod[b].getExceptionTypes(); int k;
/* 347 */       for (j = 0, k = arrayOfClass2.length; j < k; j++) {
/*     */         
/* 349 */         if (!CodegenUtils.inSamePackage(arrayOfClass2[j].getName(), paramString))
/*     */         {
/*     */           
/* 352 */           paramSet.add(CodegenUtils.unarrayClass(arrayOfClass2[j]));
/*     */         }
/*     */       } 
/* 355 */       if (!CodegenUtils.inSamePackage(paramArrayOfMethod[b].getReturnType().getName(), paramString)) {
/* 356 */         paramSet.add(CodegenUtils.unarrayClass(paramArrayOfMethod[b].getReturnType()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {
/* 362 */     Class<?> clazz = paramMethod.getReturnType();
/*     */     
/* 364 */     paramIndentedWriter.println(((clazz == void.class) ? "" : "return ") + "inner." + CodegenUtils.methodCall(paramMethod) + ";");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateReflectiveDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {
/* 369 */     Class<?> clazz = paramMethod.getReturnType();
/*     */     
/* 371 */     String str1 = CodegenUtils.reflectiveMethodParameterTypeArray(paramMethod);
/* 372 */     String str2 = CodegenUtils.reflectiveMethodObjectArray(paramMethod);
/*     */     
/* 374 */     Class[] arrayOfClass = paramMethod.getExceptionTypes();
/* 375 */     HashSet hashSet = new HashSet();
/* 376 */     hashSet.addAll(Arrays.asList((Class<?>[][])arrayOfClass));
/*     */     
/* 378 */     paramIndentedWriter.println("try");
/* 379 */     paramIndentedWriter.println("{");
/* 380 */     paramIndentedWriter.upIndent();
/* 381 */     paramIndentedWriter.println("Method m = __delegateClass.getMethod(\"" + paramMethod.getName() + "\", " + str1 + ");");
/* 382 */     paramIndentedWriter.println(((clazz == void.class) ? "" : ("return (" + ClassUtils.simpleClassName(clazz) + ") ")) + "m.invoke( inner, " + str2 + " );");
/*     */     
/* 384 */     paramIndentedWriter.downIndent();
/* 385 */     paramIndentedWriter.println("}");
/* 386 */     if (!hashSet.contains(IllegalAccessException.class)) {
/*     */       
/* 388 */       paramIndentedWriter.println("catch (IllegalAccessException iae)");
/* 389 */       paramIndentedWriter.println("{");
/* 390 */       paramIndentedWriter.upIndent();
/* 391 */       paramIndentedWriter.println("throw new RuntimeException(\"A reflectively delegated method '" + paramMethod.getName() + "' cannot access the object to which the call is delegated\", iae);");
/*     */ 
/*     */       
/* 394 */       paramIndentedWriter.downIndent();
/* 395 */       paramIndentedWriter.println("}");
/*     */     } 
/* 397 */     paramIndentedWriter.println("catch (InvocationTargetException ite)");
/* 398 */     paramIndentedWriter.println("{");
/* 399 */     paramIndentedWriter.upIndent();
/* 400 */     paramIndentedWriter.println("Throwable cause = ite.getCause();");
/* 401 */     paramIndentedWriter.println("if (cause instanceof RuntimeException) throw (RuntimeException) cause;");
/* 402 */     paramIndentedWriter.println("if (cause instanceof Error) throw (Error) cause;");
/* 403 */     int i = arrayOfClass.length;
/* 404 */     if (i > 0)
/*     */     {
/* 406 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 408 */         String str = ClassUtils.simpleClassName(arrayOfClass[b]);
/* 409 */         paramIndentedWriter.println("if (cause instanceof " + str + ") throw (" + str + ") cause;");
/*     */       } 
/*     */     }
/* 412 */     paramIndentedWriter.println("throw new RuntimeException(\"Target of reflectively delegated method '" + paramMethod.getName() + "' threw an Exception.\", cause);");
/* 413 */     paramIndentedWriter.downIndent();
/* 414 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateBannerComment(IndentedWriter paramIndentedWriter) throws IOException {
/* 419 */     paramIndentedWriter.println("/*");
/* 420 */     paramIndentedWriter.println(" * This class generated by " + getClass().getName());
/* 421 */     paramIndentedWriter.println(" * " + new Date());
/* 422 */     paramIndentedWriter.println(" * DO NOT HAND EDIT!!!!");
/* 423 */     paramIndentedWriter.println(" */");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateClassJavaDocComment(IndentedWriter paramIndentedWriter) throws IOException {
/* 428 */     paramIndentedWriter.println("/**");
/* 429 */     paramIndentedWriter.println(" * This class was generated by " + getClass().getName() + ".");
/* 430 */     paramIndentedWriter.println(" */");
/*     */   }
/*     */   
/*     */   protected void generateExtraImports(IndentedWriter paramIndentedWriter) throws IOException {}
/*     */   
/*     */   protected void generatePreDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */   
/*     */   protected void generatePostDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */   
/*     */   protected void generateExtraDeclarations(Class paramClass, String paramString, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/intfc/DelegatorGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */