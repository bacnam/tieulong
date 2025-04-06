/*     */ package bsh;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Reflect
/*     */ {
/*     */   public static Object invokeObjectMethod(Object object, String methodName, Object[] args, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo) throws ReflectError, EvalError, InvocationTargetException {
/*  65 */     if (object instanceof This && !This.isExposedThisMethod(methodName)) {
/*  66 */       return ((This)object).invokeMethod(methodName, args, interpreter, callstack, callerInfo, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  73 */       BshClassManager bcm = (interpreter == null) ? null : interpreter.getClassManager();
/*     */       
/*  75 */       Class<?> clas = object.getClass();
/*     */       
/*  77 */       Method method = resolveExpectedJavaMethod(bcm, clas, object, methodName, args, false);
/*     */ 
/*     */       
/*  80 */       return invokeMethod(method, object, args);
/*  81 */     } catch (UtilEvalError e) {
/*  82 */       throw e.toEvalError(callerInfo, callstack);
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
/*     */   public static Object invokeStaticMethod(BshClassManager bcm, Class clas, String methodName, Object[] args) throws ReflectError, UtilEvalError, InvocationTargetException {
/*  95 */     Interpreter.debug("invoke static Method");
/*  96 */     Method method = resolveExpectedJavaMethod(bcm, clas, null, methodName, args, true);
/*     */     
/*  98 */     return invokeMethod(method, null, args);
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
/*     */   static Object invokeMethod(Method method, Object object, Object[] args) throws ReflectError, InvocationTargetException {
/* 110 */     if (args == null) {
/* 111 */       args = new Object[0];
/*     */     }
/* 113 */     logInvokeMethod("Invoking method (entry): ", method, args);
/*     */ 
/*     */     
/* 116 */     Object[] tmpArgs = new Object[args.length];
/* 117 */     Class[] types = method.getParameterTypes();
/*     */     try {
/* 119 */       for (int i = 0; i < args.length; i++) {
/* 120 */         tmpArgs[i] = Types.castObject(args[i], types[i], 1);
/*     */       }
/* 122 */     } catch (UtilEvalError e) {
/* 123 */       throw new InterpreterError("illegal argument type in method invocation: " + e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 128 */     tmpArgs = Primitive.unwrap(tmpArgs);
/*     */     
/* 130 */     logInvokeMethod("Invoking method (after massaging values): ", method, tmpArgs);
/*     */ 
/*     */     
/*     */     try {
/* 134 */       Object returnValue = method.invoke(object, tmpArgs);
/* 135 */       if (returnValue == null)
/* 136 */         returnValue = Primitive.NULL; 
/* 137 */       Class<?> returnType = method.getReturnType();
/*     */       
/* 139 */       return Primitive.wrap(returnValue, returnType);
/* 140 */     } catch (IllegalAccessException e) {
/* 141 */       throw new ReflectError("Cannot access method " + StringUtil.methodString(method.getName(), method.getParameterTypes()) + " in '" + method.getDeclaringClass() + "' :" + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getIndex(Object array, int index) throws ReflectError, UtilTargetError {
/* 151 */     if (Interpreter.DEBUG)
/* 152 */       Interpreter.debug("getIndex: " + array + ", index=" + index); 
/*     */     try {
/* 154 */       Object val = Array.get(array, index);
/* 155 */       return Primitive.wrap(val, array.getClass().getComponentType());
/*     */     }
/* 157 */     catch (ArrayIndexOutOfBoundsException e1) {
/* 158 */       throw new UtilTargetError(e1);
/* 159 */     } catch (Exception e) {
/* 160 */       throw new ReflectError("Array access:" + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setIndex(Object array, int index, Object val) throws ReflectError, UtilTargetError {
/*     */     try {
/* 168 */       val = Primitive.unwrap(val);
/* 169 */       Array.set(array, index, val);
/*     */     }
/* 171 */     catch (ArrayStoreException e2) {
/* 172 */       throw new UtilTargetError(e2);
/* 173 */     } catch (IllegalArgumentException e1) {
/* 174 */       throw new UtilTargetError(new ArrayStoreException(e1.toString()));
/*     */     }
/* 176 */     catch (Exception e) {
/* 177 */       throw new ReflectError("Array access:" + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getStaticFieldValue(Class clas, String fieldName) throws UtilEvalError, ReflectError {
/* 184 */     return getFieldValue(clas, null, fieldName, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getObjectFieldValue(Object object, String fieldName) throws UtilEvalError, ReflectError {
/* 192 */     if (object instanceof This) {
/* 193 */       return ((This)object).namespace.getVariable(fieldName);
/*     */     }
/*     */     try {
/* 196 */       return getFieldValue(object.getClass(), object, fieldName, false);
/*     */     }
/* 198 */     catch (ReflectError e) {
/*     */ 
/*     */       
/* 201 */       if (hasObjectPropertyGetter(object.getClass(), fieldName)) {
/* 202 */         return getObjectProperty(object, fieldName);
/*     */       }
/* 204 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static LHS getLHSStaticField(Class clas, String fieldName) throws UtilEvalError, ReflectError {
/* 212 */     Field f = resolveExpectedJavaField(clas, fieldName, true);
/*     */     
/* 214 */     return new LHS(f);
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
/*     */   static LHS getLHSObjectField(Object object, String fieldName) throws UtilEvalError, ReflectError {
/* 226 */     if (object instanceof This) {
/*     */ 
/*     */ 
/*     */       
/* 230 */       boolean recurse = false;
/* 231 */       return new LHS(((This)object).namespace, fieldName, recurse);
/*     */     } 
/*     */     
/*     */     try {
/* 235 */       Field f = resolveExpectedJavaField(object.getClass(), fieldName, false);
/*     */       
/* 237 */       return new LHS(object, f);
/* 238 */     } catch (ReflectError e) {
/*     */ 
/*     */       
/* 241 */       if (hasObjectPropertySetter(object.getClass(), fieldName)) {
/* 242 */         return new LHS(object, fieldName);
/*     */       }
/* 244 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object getFieldValue(Class clas, Object object, String fieldName, boolean staticOnly) throws UtilEvalError, ReflectError {
/*     */     try {
/* 253 */       Field f = resolveExpectedJavaField(clas, fieldName, staticOnly);
/*     */       
/* 255 */       Object value = f.get(object);
/* 256 */       Class<?> returnType = f.getType();
/* 257 */       return Primitive.wrap(value, returnType);
/*     */     }
/* 259 */     catch (NullPointerException e) {
/* 260 */       throw new ReflectError("???" + fieldName + " is not a static field.");
/*     */     }
/* 262 */     catch (IllegalAccessException e) {
/* 263 */       throw new ReflectError("Can't access field: " + fieldName);
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
/*     */   protected static Field resolveJavaField(Class clas, String fieldName, boolean staticOnly) throws UtilEvalError {
/*     */     try {
/* 278 */       return resolveExpectedJavaField(clas, fieldName, staticOnly);
/* 279 */     } catch (ReflectError e) {
/* 280 */       return null;
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
/*     */   protected static Field resolveExpectedJavaField(Class clas, String fieldName, boolean staticOnly) throws UtilEvalError, ReflectError {
/*     */     Field field;
/*     */     try {
/* 298 */       if (Capabilities.haveAccessibility()) {
/* 299 */         field = findAccessibleField(clas, fieldName);
/*     */       } else {
/*     */         
/* 302 */         field = clas.getField(fieldName);
/*     */       } 
/* 304 */     } catch (NoSuchFieldException e) {
/* 305 */       throw new ReflectError("No such field: " + fieldName);
/* 306 */     } catch (SecurityException e) {
/* 307 */       throw new UtilTargetError("Security Exception while searching fields of: " + clas, e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 312 */     if (staticOnly && !Modifier.isStatic(field.getModifiers())) {
/* 313 */       throw new UtilEvalError("Can't reach instance field: " + fieldName + " from static context: " + clas.getName());
/*     */     }
/*     */ 
/*     */     
/* 317 */     return field;
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
/*     */   private static Field findAccessibleField(Class clas, String fieldName) throws UtilEvalError, NoSuchFieldException {
/*     */     try {
/* 345 */       Field field = clas.getField(fieldName);
/* 346 */       ReflectManager.RMSetAccessible(field);
/* 347 */       return field;
/* 348 */     } catch (NoSuchFieldException e) {
/*     */ 
/*     */       
/* 351 */       while (clas != null) {
/*     */         
/*     */         try {
/* 354 */           Field field = clas.getDeclaredField(fieldName);
/* 355 */           ReflectManager.RMSetAccessible(field);
/* 356 */           return field;
/*     */ 
/*     */         
/*     */         }
/* 360 */         catch (NoSuchFieldException noSuchFieldException) {
/*     */           
/* 362 */           clas = clas.getSuperclass();
/*     */         } 
/* 364 */       }  throw new NoSuchFieldException(fieldName);
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
/*     */   protected static Method resolveExpectedJavaMethod(BshClassManager bcm, Class clas, Object object, String name, Object[] args, boolean staticOnly) throws ReflectError, UtilEvalError {
/* 376 */     if (object == Primitive.NULL) {
/* 377 */       throw new UtilTargetError(new NullPointerException("Attempt to invoke method " + name + " on null value"));
/*     */     }
/*     */     
/* 380 */     Class[] types = Types.getTypes(args);
/* 381 */     Method method = resolveJavaMethod(bcm, clas, name, types, staticOnly);
/*     */     
/* 383 */     if (method == null) {
/* 384 */       throw new ReflectError((staticOnly ? "Static method " : "Method ") + StringUtil.methodString(name, types) + " not found in class'" + clas.getName() + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 389 */     return method;
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
/*     */   protected static Method resolveJavaMethod(BshClassManager bcm, Class clas, String name, Class[] types, boolean staticOnly) throws UtilEvalError {
/* 424 */     if (clas == null) {
/* 425 */       throw new InterpreterError("null class");
/*     */     }
/*     */     
/* 428 */     Method method = null;
/* 429 */     if (bcm == null) {
/* 430 */       Interpreter.debug("resolveJavaMethod UNOPTIMIZED lookup");
/*     */     } else {
/* 432 */       method = bcm.getResolvedMethod(clas, name, types, staticOnly);
/*     */     } 
/* 434 */     if (method == null) {
/*     */       
/* 436 */       boolean publicOnly = !Capabilities.haveAccessibility();
/*     */       
/*     */       try {
/* 439 */         method = findOverloadedMethod(clas, name, types, publicOnly);
/* 440 */       } catch (SecurityException e) {
/* 441 */         throw new UtilTargetError("Security Exception while searching methods of: " + clas, e);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 446 */       checkFoundStaticMethod(method, staticOnly, clas);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       if (method != null && !publicOnly) {
/*     */         try {
/* 453 */           ReflectManager.RMSetAccessible(method);
/* 454 */         } catch (UtilEvalError e) {}
/*     */       }
/*     */ 
/*     */       
/* 458 */       if (method != null && bcm != null) {
/* 459 */         bcm.cacheResolvedMethod(clas, types, method);
/*     */       }
/*     */     } 
/* 462 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Method findOverloadedMethod(Class baseClass, String methodName, Class[] types, boolean publicOnly) {
/* 473 */     if (Interpreter.DEBUG) {
/* 474 */       Interpreter.debug("Searching for method: " + StringUtil.methodString(methodName, types) + " in '" + baseClass.getName() + "'");
/*     */     }
/*     */ 
/*     */     
/* 478 */     Method[] methods = getCandidateMethods(baseClass, methodName, types.length, publicOnly);
/*     */ 
/*     */     
/* 481 */     if (Interpreter.DEBUG)
/* 482 */       Interpreter.debug("Looking for most specific method: " + methodName); 
/* 483 */     Method method = findMostSpecificMethod(types, methods);
/*     */     
/* 485 */     return method;
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
/*     */   static Method[] getCandidateMethods(Class baseClass, String methodName, int numArgs, boolean publicOnly) {
/* 505 */     Vector candidates = gatherMethodsRecursive(baseClass, methodName, numArgs, publicOnly, null);
/*     */ 
/*     */ 
/*     */     
/* 509 */     Method[] ma = new Method[candidates.size()];
/* 510 */     candidates.copyInto((Object[])ma);
/* 511 */     return ma;
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
/*     */   private static Vector gatherMethodsRecursive(Class baseClass, String methodName, int numArgs, boolean publicOnly, Vector candidates) {
/* 534 */     if (candidates == null) {
/* 535 */       candidates = new Vector();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     if (publicOnly) {
/* 542 */       if (isPublic(baseClass)) {
/* 543 */         addCandidates(baseClass.getMethods(), methodName, numArgs, publicOnly, candidates);
/*     */       }
/*     */     } else {
/* 546 */       addCandidates(baseClass.getDeclaredMethods(), methodName, numArgs, publicOnly, candidates);
/*     */     } 
/*     */ 
/*     */     
/* 550 */     Class[] intfs = baseClass.getInterfaces();
/* 551 */     for (int i = 0; i < intfs.length; i++) {
/* 552 */       gatherMethodsRecursive(intfs[i], methodName, numArgs, publicOnly, candidates);
/*     */     }
/*     */ 
/*     */     
/* 556 */     Class superclass = baseClass.getSuperclass();
/* 557 */     if (superclass != null) {
/* 558 */       gatherMethodsRecursive(superclass, methodName, numArgs, publicOnly, candidates);
/*     */     }
/*     */     
/* 561 */     return candidates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vector addCandidates(Method[] methods, String methodName, int numArgs, boolean publicOnly, Vector<Method> candidates) {
/* 568 */     for (int i = 0; i < methods.length; i++) {
/*     */       
/* 570 */       Method m = methods[i];
/* 571 */       if (m.getName().equals(methodName) && (m.getParameterTypes()).length == numArgs && (!publicOnly || isPublic(m)))
/*     */       {
/*     */ 
/*     */         
/* 575 */         candidates.add(m); } 
/*     */     } 
/* 577 */     return candidates;
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
/*     */   static Object constructObject(Class clas, Object[] args) throws ReflectError, InvocationTargetException {
/* 593 */     if (clas.isInterface()) {
/* 594 */       throw new ReflectError("Can't create instance of an interface: " + clas);
/*     */     }
/*     */     
/* 597 */     Object obj = null;
/* 598 */     Class[] types = Types.getTypes(args);
/* 599 */     Constructor con = null;
/*     */ 
/*     */ 
/*     */     
/* 603 */     Constructor<?>[] arrayOfConstructor = Capabilities.haveAccessibility() ? clas.getDeclaredConstructors() : clas.getConstructors();
/*     */ 
/*     */ 
/*     */     
/* 607 */     if (Interpreter.DEBUG)
/* 608 */       Interpreter.debug("Looking for most specific constructor: " + clas); 
/* 609 */     con = findMostSpecificConstructor(types, (Constructor[])arrayOfConstructor);
/* 610 */     if (con == null) {
/* 611 */       throw cantFindConstructor(clas, types);
/*     */     }
/* 613 */     if (!isPublic(con)) {
/*     */       try {
/* 615 */         ReflectManager.RMSetAccessible(con);
/* 616 */       } catch (UtilEvalError e) {}
/*     */     }
/* 618 */     args = Primitive.unwrap(args);
/*     */     try {
/* 620 */       obj = con.newInstance(args);
/* 621 */     } catch (InstantiationException e) {
/* 622 */       throw new ReflectError("The class " + clas + " is abstract ");
/* 623 */     } catch (IllegalAccessException e) {
/* 624 */       throw new ReflectError("We don't have permission to create an instance.Use setAccessibility(true) to enable access.");
/*     */     
/*     */     }
/* 627 */     catch (IllegalArgumentException e) {
/* 628 */       throw new ReflectError("The number of arguments was wrong");
/*     */     } 
/* 630 */     if (obj == null) {
/* 631 */       throw new ReflectError("Couldn't construct the object");
/*     */     }
/* 633 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Constructor findMostSpecificConstructor(Class[] idealMatch, Constructor[] constructors) {
/* 644 */     int match = findMostSpecificConstructorIndex(idealMatch, constructors);
/* 645 */     return (match == -1) ? null : constructors[match];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int findMostSpecificConstructorIndex(Class[] idealMatch, Constructor[] constructors) {
/* 651 */     Class[][] candidates = new Class[constructors.length][];
/* 652 */     for (int i = 0; i < candidates.length; i++) {
/* 653 */       candidates[i] = constructors[i].getParameterTypes();
/*     */     }
/* 655 */     return findMostSpecificSignature(idealMatch, candidates);
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
/*     */   static Method findMostSpecificMethod(Class[] idealMatch, Method[] methods) {
/* 674 */     Class[][] candidateSigs = new Class[methods.length][];
/* 675 */     for (int i = 0; i < methods.length; i++) {
/* 676 */       candidateSigs[i] = methods[i].getParameterTypes();
/*     */     }
/* 678 */     int match = findMostSpecificSignature(idealMatch, candidateSigs);
/* 679 */     return (match == -1) ? null : methods[match];
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
/*     */   static int findMostSpecificSignature(Class[] idealMatch, Class[][] candidates) {
/* 706 */     int round = 1;
/* 707 */     for (; round <= 4; round++) {
/*     */       
/* 709 */       Class[] bestMatch = null;
/* 710 */       int bestMatchIndex = -1;
/*     */       
/* 712 */       for (int i = 0; i < candidates.length; i++) {
/*     */         
/* 714 */         Class[] targetMatch = candidates[i];
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 719 */         if (Types.isSignatureAssignable(idealMatch, targetMatch, round) && (bestMatch == null || Types.isSignatureAssignable(targetMatch, bestMatch, 1))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 727 */           bestMatch = targetMatch;
/* 728 */           bestMatchIndex = i;
/*     */         } 
/*     */       } 
/*     */       
/* 732 */       if (bestMatch != null) {
/* 733 */         return bestMatchIndex;
/*     */       }
/*     */     } 
/* 736 */     return -1;
/*     */   }
/*     */   
/*     */   private static String accessorName(String getorset, String propName) {
/* 740 */     return getorset + String.valueOf(Character.toUpperCase(propName.charAt(0))) + propName.substring(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasObjectPropertyGetter(Class clas, String propName) {
/* 748 */     String getterName = accessorName("get", propName);
/*     */     try {
/* 750 */       clas.getMethod(getterName, new Class[0]);
/* 751 */       return true;
/* 752 */     } catch (NoSuchMethodException e) {
/* 753 */       getterName = accessorName("is", propName);
/*     */       try {
/* 755 */         Method m = clas.getMethod(getterName, new Class[0]);
/* 756 */         return (m.getReturnType() == boolean.class);
/* 757 */       } catch (NoSuchMethodException noSuchMethodException) {
/* 758 */         return false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasObjectPropertySetter(Class clas, String propName) {
/* 765 */     String setterName = accessorName("set", propName);
/* 766 */     Method[] methods = clas.getMethods();
/*     */ 
/*     */ 
/*     */     
/* 770 */     for (int i = 0; i < methods.length; i++) {
/* 771 */       if (methods[i].getName().equals(setterName))
/* 772 */         return true; 
/* 773 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getObjectProperty(Object obj, String propName) throws UtilEvalError, ReflectError {
/* 780 */     Object[] args = new Object[0];
/*     */     
/* 782 */     Interpreter.debug("property access: ");
/* 783 */     Method method = null;
/*     */     
/* 785 */     Exception e1 = null, e2 = null;
/*     */     try {
/* 787 */       String accessorName = accessorName("get", propName);
/* 788 */       method = resolveExpectedJavaMethod(null, obj.getClass(), obj, accessorName, args, false);
/*     */     }
/* 790 */     catch (Exception e) {
/* 791 */       e1 = e;
/*     */     } 
/* 793 */     if (method == null)
/*     */       try {
/* 795 */         String accessorName = accessorName("is", propName);
/* 796 */         method = resolveExpectedJavaMethod(null, obj.getClass(), obj, accessorName, args, false);
/*     */ 
/*     */         
/* 799 */         if (method.getReturnType() != boolean.class)
/* 800 */           method = null; 
/* 801 */       } catch (Exception e) {
/* 802 */         e2 = e;
/*     */       }  
/* 804 */     if (method == null) {
/* 805 */       throw new ReflectError("Error in property getter: " + e1 + ((e2 != null) ? (" : " + e2) : ""));
/*     */     }
/*     */     
/*     */     try {
/* 809 */       return invokeMethod(method, obj, args);
/*     */     }
/* 811 */     catch (InvocationTargetException e) {
/*     */       
/* 813 */       throw new UtilEvalError("Property accessor threw exception: " + e.getTargetException());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setObjectProperty(Object obj, String propName, Object value) throws ReflectError, UtilEvalError {
/* 822 */     String accessorName = accessorName("set", propName);
/* 823 */     Object[] args = { value };
/*     */     
/* 825 */     Interpreter.debug("property access: ");
/*     */     try {
/* 827 */       Method method = resolveExpectedJavaMethod(null, obj.getClass(), obj, accessorName, args, false);
/*     */       
/* 829 */       invokeMethod(method, obj, args);
/*     */     }
/* 831 */     catch (InvocationTargetException e) {
/*     */       
/* 833 */       throw new UtilEvalError("Property accessor threw exception: " + e.getTargetException());
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
/*     */   public static String normalizeClassName(Class type) {
/* 846 */     if (!type.isArray()) {
/* 847 */       return type.getName();
/*     */     }
/* 849 */     StringBuffer className = new StringBuffer();
/*     */     try {
/* 851 */       className.append(getArrayBaseType(type).getName() + " ");
/* 852 */       for (int i = 0; i < getArrayDimensions(type); i++)
/* 853 */         className.append("[]"); 
/* 854 */     } catch (ReflectError e) {}
/*     */     
/* 856 */     return className.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getArrayDimensions(Class arrayClass) {
/* 865 */     if (!arrayClass.isArray()) {
/* 866 */       return 0;
/*     */     }
/* 868 */     return arrayClass.getName().lastIndexOf('[') + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class getArrayBaseType(Class arrayClass) throws ReflectError {
/* 878 */     if (!arrayClass.isArray()) {
/* 879 */       throw new ReflectError("The class is not an array.");
/*     */     }
/* 881 */     return arrayClass.getComponentType();
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
/*     */   public static Object invokeCompiledCommand(Class commandClass, Object[] args, Interpreter interpreter, CallStack callstack) throws UtilEvalError {
/* 899 */     Object[] invokeArgs = new Object[args.length + 2];
/* 900 */     invokeArgs[0] = interpreter;
/* 901 */     invokeArgs[1] = callstack;
/* 902 */     System.arraycopy(args, 0, invokeArgs, 2, args.length);
/* 903 */     BshClassManager bcm = interpreter.getClassManager();
/*     */     try {
/* 905 */       return invokeStaticMethod(bcm, commandClass, "invoke", invokeArgs);
/*     */     }
/* 907 */     catch (InvocationTargetException e) {
/* 908 */       throw new UtilEvalError("Error in compiled command: " + e.getTargetException());
/*     */     }
/* 910 */     catch (ReflectError e) {
/* 911 */       throw new UtilEvalError("Error invoking compiled command: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void logInvokeMethod(String msg, Method method, Object[] args) {
/* 918 */     if (Interpreter.DEBUG) {
/*     */       
/* 920 */       Interpreter.debug(msg + method + " with args:");
/* 921 */       for (int i = 0; i < args.length; i++) {
/* 922 */         Interpreter.debug("args[" + i + "] = " + args[i] + " type = " + args[i].getClass());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkFoundStaticMethod(Method method, boolean staticOnly, Class clas) throws UtilEvalError {
/* 933 */     if (method != null && staticOnly && !isStatic(method)) {
/* 934 */       throw new UtilEvalError("Cannot reach instance method: " + StringUtil.methodString(method.getName(), method.getParameterTypes()) + " from static context: " + clas.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ReflectError cantFindConstructor(Class clas, Class[] types) {
/* 944 */     if (types.length == 0) {
/* 945 */       return new ReflectError("Can't find default constructor for: " + clas);
/*     */     }
/*     */     
/* 948 */     return new ReflectError("Can't find constructor: " + StringUtil.methodString(clas.getName(), types) + " in class: " + clas.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPublic(Class c) {
/* 955 */     return Modifier.isPublic(c.getModifiers());
/*     */   }
/*     */   private static boolean isPublic(Method m) {
/* 958 */     return Modifier.isPublic(m.getModifiers());
/*     */   }
/*     */   private static boolean isPublic(Constructor c) {
/* 961 */     return Modifier.isPublic(c.getModifiers());
/*     */   }
/*     */   private static boolean isStatic(Method m) {
/* 964 */     return Modifier.isStatic(m.getModifiers());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Reflect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */