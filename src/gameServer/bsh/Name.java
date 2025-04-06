/*      */ package bsh;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class Name
/*      */   implements Serializable
/*      */ {
/*      */   public NameSpace namespace;
/*  102 */   String value = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String evalName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String lastEvalName;
/*      */ 
/*      */ 
/*      */   
/*  116 */   private static String FINISHED = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object evalBaseObject;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int callstackDepth;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Class asClass;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Class classOfStaticMethod;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reset() {
/*  144 */     this.evalName = this.value;
/*  145 */     this.evalBaseObject = null;
/*  146 */     this.callstackDepth = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Name(NameSpace namespace, String s) {
/*  157 */     this.namespace = namespace;
/*  158 */     this.value = s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object toObject(CallStack callstack, Interpreter interpreter) throws UtilEvalError {
/*  185 */     return toObject(callstack, interpreter, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object toObject(CallStack callstack, Interpreter interpreter, boolean forceClass) throws UtilEvalError {
/*  198 */     reset();
/*      */     
/*  200 */     Object obj = null;
/*  201 */     while (this.evalName != null) {
/*  202 */       obj = consumeNextObjectField(callstack, interpreter, forceClass, false);
/*      */     }
/*      */     
/*  205 */     if (obj == null) {
/*  206 */       throw new InterpreterError("null value in toObject()");
/*      */     }
/*  208 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object completeRound(String lastEvalName, String nextEvalName, Object returnObject) {
/*  214 */     if (returnObject == null)
/*  215 */       throw new InterpreterError("lastEvalName = " + lastEvalName); 
/*  216 */     this.lastEvalName = lastEvalName;
/*  217 */     this.evalName = nextEvalName;
/*  218 */     this.evalBaseObject = returnObject;
/*  219 */     return returnObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object consumeNextObjectField(CallStack callstack, Interpreter interpreter, boolean forceClass, boolean autoAllocateThis) throws UtilEvalError {
/*  239 */     if (this.evalBaseObject == null && !isCompound(this.evalName) && !forceClass) {
/*      */ 
/*      */       
/*  242 */       Object obj = resolveThisFieldReference(callstack, this.namespace, interpreter, this.evalName, false);
/*      */ 
/*      */       
/*  245 */       if (obj != Primitive.VOID) {
/*  246 */         return completeRound(this.evalName, FINISHED, obj);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  254 */     String varName = prefix(this.evalName, 1);
/*  255 */     if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !forceClass) {
/*      */       Object obj;
/*      */       
/*  258 */       if (Interpreter.DEBUG) {
/*  259 */         Interpreter.debug("trying to resolve variable: " + varName);
/*      */       }
/*      */ 
/*      */       
/*  263 */       if (this.evalBaseObject == null) {
/*  264 */         obj = resolveThisFieldReference(callstack, this.namespace, interpreter, varName, false);
/*      */       } else {
/*      */         
/*  267 */         obj = resolveThisFieldReference(callstack, ((This)this.evalBaseObject).namespace, interpreter, varName, true);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  272 */       if (obj != Primitive.VOID) {
/*      */ 
/*      */         
/*  275 */         if (Interpreter.DEBUG) {
/*  276 */           Interpreter.debug("resolved variable: " + varName + " in namespace: " + this.namespace);
/*      */         }
/*      */         
/*  279 */         return completeRound(varName, suffix(this.evalName), obj);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  287 */     if (this.evalBaseObject == null) {
/*      */       
/*  289 */       if (Interpreter.DEBUG) {
/*  290 */         Interpreter.debug("trying class: " + this.evalName);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  295 */       Class clas = null;
/*  296 */       int i = 1;
/*  297 */       String className = null;
/*  298 */       for (; i <= countParts(this.evalName); i++) {
/*      */         
/*  300 */         className = prefix(this.evalName, i);
/*  301 */         if ((clas = this.namespace.getClass(className)) != null) {
/*      */           break;
/*      */         }
/*      */       } 
/*  305 */       if (clas != null) {
/*  306 */         return completeRound(className, suffix(this.evalName, countParts(this.evalName) - i), new ClassIdentifier(clas));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  313 */       if (Interpreter.DEBUG) {
/*  314 */         Interpreter.debug("not a class, trying var prefix " + this.evalName);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  319 */     if ((this.evalBaseObject == null || this.evalBaseObject instanceof This) && !forceClass && autoAllocateThis) {
/*      */ 
/*      */       
/*  322 */       NameSpace targetNameSpace = (this.evalBaseObject == null) ? this.namespace : ((This)this.evalBaseObject).namespace;
/*      */ 
/*      */       
/*  325 */       Object obj = (new NameSpace(targetNameSpace, "auto: " + varName)).getThis(interpreter);
/*      */       
/*  327 */       targetNameSpace.setVariable(varName, obj, false);
/*  328 */       return completeRound(varName, suffix(this.evalName), obj);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  339 */     if (this.evalBaseObject == null) {
/*  340 */       if (!isCompound(this.evalName)) {
/*  341 */         return completeRound(this.evalName, FINISHED, Primitive.VOID);
/*      */       }
/*  343 */       throw new UtilEvalError("Class or variable not found: " + this.evalName);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  358 */     if (this.evalBaseObject == Primitive.NULL) {
/*  359 */       throw new UtilTargetError(new NullPointerException("Null Pointer while evaluating: " + this.value));
/*      */     }
/*      */     
/*  362 */     if (this.evalBaseObject == Primitive.VOID) {
/*  363 */       throw new UtilEvalError("Undefined variable or class name while evaluating: " + this.value);
/*      */     }
/*      */     
/*  366 */     if (this.evalBaseObject instanceof Primitive) {
/*  367 */       throw new UtilEvalError("Can't treat primitive like an object. Error while evaluating: " + this.value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  374 */     if (this.evalBaseObject instanceof ClassIdentifier) {
/*      */       
/*  376 */       Class<?> clas = ((ClassIdentifier)this.evalBaseObject).getTargetClass();
/*  377 */       String str = prefix(this.evalName, 1);
/*      */ 
/*      */ 
/*      */       
/*  381 */       if (str.equals("this")) {
/*      */ 
/*      */         
/*  384 */         NameSpace ns = this.namespace;
/*  385 */         while (ns != null) {
/*      */ 
/*      */           
/*  388 */           if (ns.classInstance != null && ns.classInstance.getClass() == clas)
/*      */           {
/*      */             
/*  391 */             return completeRound(str, suffix(this.evalName), ns.classInstance);
/*      */           }
/*  393 */           ns = ns.getParent();
/*      */         } 
/*  395 */         throw new UtilEvalError("Can't find enclosing 'this' instance of class: " + clas);
/*      */       } 
/*      */ 
/*      */       
/*  399 */       Object obj = null;
/*      */       
/*      */       try {
/*  402 */         if (Interpreter.DEBUG) {
/*  403 */           Interpreter.debug("Name call to getStaticFieldValue, class: " + clas + ", field:" + str);
/*      */         }
/*  405 */         obj = Reflect.getStaticFieldValue(clas, str);
/*  406 */       } catch (ReflectError e) {
/*  407 */         if (Interpreter.DEBUG) {
/*  408 */           Interpreter.debug("field reflect error: " + e);
/*      */         }
/*      */       } 
/*      */       
/*  412 */       if (obj == null) {
/*  413 */         String iclass = clas.getName() + "$" + str;
/*  414 */         Class c = this.namespace.getClass(iclass);
/*  415 */         if (c != null) {
/*  416 */           obj = new ClassIdentifier(c);
/*      */         }
/*      */       } 
/*  419 */       if (obj == null) {
/*  420 */         throw new UtilEvalError("No static field or inner class: " + str + " of " + clas);
/*      */       }
/*      */ 
/*      */       
/*  424 */       return completeRound(str, suffix(this.evalName), obj);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  431 */     if (forceClass) {
/*  432 */       throw new UtilEvalError(this.value + " does not resolve to a class name.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  439 */     String field = prefix(this.evalName, 1);
/*      */ 
/*      */     
/*  442 */     if (field.equals("length") && this.evalBaseObject.getClass().isArray()) {
/*      */       
/*  444 */       Object obj = new Primitive(Array.getLength(this.evalBaseObject));
/*  445 */       return completeRound(field, suffix(this.evalName), obj);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  451 */       Object obj = Reflect.getObjectFieldValue(this.evalBaseObject, field);
/*  452 */       return completeRound(field, suffix(this.evalName), obj);
/*  453 */     } catch (ReflectError e) {
/*      */ 
/*      */       
/*  456 */       throw new UtilEvalError("Cannot access field: " + field + ", on object: " + this.evalBaseObject);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object resolveThisFieldReference(CallStack callstack, NameSpace thisNameSpace, Interpreter interpreter, String varName, boolean specialFieldsVisible) throws UtilEvalError {
/*  481 */     if (varName.equals("this")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  489 */       if (specialFieldsVisible) {
/*  490 */         throw new UtilEvalError("Redundant to call .this on This type");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  495 */       This ths = thisNameSpace.getThis(interpreter);
/*  496 */       thisNameSpace = ths.getNameSpace();
/*  497 */       Object result = ths;
/*      */       
/*  499 */       NameSpace classNameSpace = getClassNameSpace(thisNameSpace);
/*  500 */       if (classNameSpace != null)
/*      */       {
/*  502 */         if (isCompound(this.evalName)) {
/*  503 */           result = classNameSpace.getThis(interpreter);
/*      */         } else {
/*  505 */           result = classNameSpace.getClassInstance();
/*      */         } 
/*      */       }
/*  508 */       return result;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  516 */     if (varName.equals("super")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  522 */       This ths = thisNameSpace.getSuper(interpreter);
/*  523 */       thisNameSpace = ths.getNameSpace();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  530 */       if (thisNameSpace.getParent() != null && (thisNameSpace.getParent()).isClass)
/*      */       {
/*      */ 
/*      */         
/*  534 */         ths = thisNameSpace.getParent().getThis(interpreter);
/*      */       }
/*  536 */       return ths;
/*      */     } 
/*      */     
/*  539 */     Object obj = null;
/*      */     
/*  541 */     if (varName.equals("global")) {
/*  542 */       obj = thisNameSpace.getGlobal(interpreter);
/*      */     }
/*  544 */     if (obj == null && specialFieldsVisible)
/*      */     {
/*  546 */       if (varName.equals("namespace")) {
/*  547 */         obj = thisNameSpace;
/*  548 */       } else if (varName.equals("variables")) {
/*  549 */         obj = thisNameSpace.getVariableNames();
/*  550 */       } else if (varName.equals("methods")) {
/*  551 */         obj = thisNameSpace.getMethodNames();
/*  552 */       } else if (varName.equals("interpreter")) {
/*  553 */         if (this.lastEvalName.equals("this")) {
/*  554 */           obj = interpreter;
/*      */         } else {
/*  556 */           throw new UtilEvalError("Can only call .interpreter on literal 'this'");
/*      */         } 
/*      */       } 
/*      */     }
/*  560 */     if (obj == null && specialFieldsVisible && varName.equals("caller")) {
/*      */       
/*  562 */       if (this.lastEvalName.equals("this") || this.lastEvalName.equals("caller")) {
/*      */ 
/*      */         
/*  565 */         if (callstack == null)
/*  566 */           throw new InterpreterError("no callstack"); 
/*  567 */         obj = callstack.get(++this.callstackDepth).getThis(interpreter);
/*      */       }
/*      */       else {
/*      */         
/*  571 */         throw new UtilEvalError("Can only call .caller on literal 'this' or literal '.caller'");
/*      */       } 
/*      */ 
/*      */       
/*  575 */       return obj;
/*      */     } 
/*      */     
/*  578 */     if (obj == null && specialFieldsVisible && varName.equals("callstack"))
/*      */     {
/*      */       
/*  581 */       if (this.lastEvalName.equals("this")) {
/*      */ 
/*      */         
/*  584 */         if (callstack == null)
/*  585 */           throw new InterpreterError("no callstack"); 
/*  586 */         obj = callstack;
/*      */       } else {
/*      */         
/*  589 */         throw new UtilEvalError("Can only call .callstack on literal 'this'");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  594 */     if (obj == null) {
/*  595 */       obj = thisNameSpace.getVariable(varName);
/*      */     }
/*  597 */     if (obj == null) {
/*  598 */       throw new InterpreterError("null this field ref:" + varName);
/*      */     }
/*  600 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static NameSpace getClassNameSpace(NameSpace thisNameSpace) {
/*  610 */     if (thisNameSpace.isClass) {
/*  611 */       return thisNameSpace;
/*      */     }
/*  613 */     if (thisNameSpace.isMethod && thisNameSpace.getParent() != null && (thisNameSpace.getParent()).isClass)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  618 */       return thisNameSpace.getParent();
/*      */     }
/*  620 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Class toClass() throws ClassNotFoundException, UtilEvalError {
/*  634 */     if (this.asClass != null) {
/*  635 */       return this.asClass;
/*      */     }
/*  637 */     reset();
/*      */ 
/*      */     
/*  640 */     if (this.evalName.equals("var")) {
/*  641 */       return this.asClass = null;
/*      */     }
/*      */     
/*  644 */     Class clas = this.namespace.getClass(this.evalName);
/*      */     
/*  646 */     if (clas == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  652 */       Object obj = null;
/*      */ 
/*      */       
/*      */       try {
/*  656 */         obj = toObject(null, null, true);
/*  657 */       } catch (UtilEvalError e) {}
/*      */       
/*  659 */       if (obj instanceof ClassIdentifier) {
/*  660 */         clas = ((ClassIdentifier)obj).getTargetClass();
/*      */       }
/*      */     } 
/*  663 */     if (clas == null) {
/*  664 */       throw new ClassNotFoundException("Class: " + this.value + " not found in namespace");
/*      */     }
/*      */     
/*  667 */     this.asClass = clas;
/*  668 */     return this.asClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized LHS toLHS(CallStack callstack, Interpreter interpreter) throws UtilEvalError {
/*  678 */     reset();
/*      */ 
/*      */ 
/*      */     
/*  682 */     if (!isCompound(this.evalName)) {
/*      */       
/*  684 */       if (this.evalName.equals("this")) {
/*  685 */         throw new UtilEvalError("Can't assign to 'this'.");
/*      */       }
/*      */       
/*  688 */       LHS lhs = new LHS(this.namespace, this.evalName, false);
/*  689 */       return lhs;
/*      */     } 
/*      */ 
/*      */     
/*  693 */     Object obj = null;
/*      */     try {
/*  695 */       while (this.evalName != null && isCompound(this.evalName))
/*      */       {
/*  697 */         obj = consumeNextObjectField(callstack, interpreter, false, true);
/*      */       
/*      */       }
/*      */     }
/*  701 */     catch (UtilEvalError e) {
/*  702 */       throw new UtilEvalError("LHS evaluation: " + e.getMessage());
/*      */     } 
/*      */ 
/*      */     
/*  706 */     if (this.evalName == null && obj instanceof ClassIdentifier) {
/*  707 */       throw new UtilEvalError("Can't assign to class: " + this.value);
/*      */     }
/*  709 */     if (obj == null) {
/*  710 */       throw new UtilEvalError("Error in LHS: " + this.value);
/*      */     }
/*      */     
/*  713 */     if (obj instanceof This) {
/*      */ 
/*      */       
/*  716 */       if (this.evalName.equals("namespace") || this.evalName.equals("variables") || this.evalName.equals("methods") || this.evalName.equals("caller"))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  722 */         throw new UtilEvalError("Can't assign to special variable: " + this.evalName);
/*      */       }
/*      */       
/*  725 */       Interpreter.debug("found This reference evaluating LHS");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  736 */       boolean localVar = !this.lastEvalName.equals("super");
/*  737 */       return new LHS(((This)obj).namespace, this.evalName, localVar);
/*      */     } 
/*      */     
/*  740 */     if (this.evalName != null) {
/*      */       
/*      */       try {
/*  743 */         if (obj instanceof ClassIdentifier) {
/*      */           
/*  745 */           Class clas = ((ClassIdentifier)obj).getTargetClass();
/*  746 */           LHS lHS = Reflect.getLHSStaticField(clas, this.evalName);
/*  747 */           return lHS;
/*      */         } 
/*  749 */         LHS lhs = Reflect.getLHSObjectField(obj, this.evalName);
/*  750 */         return lhs;
/*      */       }
/*  752 */       catch (ReflectError e) {
/*  753 */         throw new UtilEvalError("Field access: " + e);
/*      */       } 
/*      */     }
/*      */     
/*  757 */     throw new InterpreterError("Internal error in lhs...");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object invokeMethod(Interpreter interpreter, Object[] args, CallStack callstack, SimpleNode callerInfo) throws UtilEvalError, EvalError, ReflectError, InvocationTargetException {
/*  790 */     String methodName = suffix(this.value, 1);
/*  791 */     BshClassManager bcm = interpreter.getClassManager();
/*  792 */     NameSpace namespace = callstack.top();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  797 */     if (this.classOfStaticMethod != null)
/*      */     {
/*  799 */       return Reflect.invokeStaticMethod(bcm, this.classOfStaticMethod, methodName, args);
/*      */     }
/*      */ 
/*      */     
/*  803 */     if (!isCompound(this.value)) {
/*  804 */       return invokeLocalMethod(interpreter, args, callstack, callerInfo);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  813 */     String prefix = prefix(this.value);
/*      */ 
/*      */     
/*  816 */     if (prefix.equals("super") && countParts(this.value) == 2) {
/*      */ 
/*      */       
/*  819 */       This ths = namespace.getThis(interpreter);
/*  820 */       NameSpace thisNameSpace = ths.getNameSpace();
/*  821 */       NameSpace classNameSpace = getClassNameSpace(thisNameSpace);
/*  822 */       if (classNameSpace != null) {
/*      */         
/*  824 */         Object instance = classNameSpace.getClassInstance();
/*  825 */         return ClassGenerator.getClassGenerator().invokeSuperclassMethod(bcm, instance, methodName, args);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  831 */     Name targetName = namespace.getNameResolver(prefix);
/*  832 */     Object obj = targetName.toObject(callstack, interpreter);
/*      */     
/*  834 */     if (obj == Primitive.VOID) {
/*  835 */       throw new UtilEvalError("Attempt to resolve method: " + methodName + "() on undefined variable or class name: " + targetName);
/*      */     }
/*      */ 
/*      */     
/*  839 */     if (!(obj instanceof ClassIdentifier)) {
/*      */       
/*  841 */       if (obj instanceof Primitive) {
/*      */         
/*  843 */         if (obj == Primitive.NULL) {
/*  844 */           throw new UtilTargetError(new NullPointerException("Null Pointer in Method Invocation"));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  851 */         if (Interpreter.DEBUG) {
/*  852 */           Interpreter.debug("Attempt to access method on primitive... allowing bsh.Primitive to peek through for debugging");
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  858 */       return Reflect.invokeObjectMethod(obj, methodName, args, interpreter, callstack, callerInfo);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  865 */     if (Interpreter.DEBUG) {
/*  866 */       Interpreter.debug("invokeMethod: trying static - " + targetName);
/*      */     }
/*  868 */     Class clas = ((ClassIdentifier)obj).getTargetClass();
/*      */ 
/*      */     
/*  871 */     this.classOfStaticMethod = clas;
/*      */     
/*  873 */     if (clas != null) {
/*  874 */       return Reflect.invokeStaticMethod(bcm, clas, methodName, args);
/*      */     }
/*      */     
/*  877 */     throw new UtilEvalError("invokeMethod: unknown target: " + targetName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object invokeLocalMethod(Interpreter interpreter, Object[] args, CallStack callstack, SimpleNode callerInfo) throws EvalError {
/*      */     Object commandObject;
/*  897 */     if (Interpreter.DEBUG)
/*  898 */       Interpreter.debug("invokeLocalMethod: " + this.value); 
/*  899 */     if (interpreter == null) {
/*  900 */       throw new InterpreterError("invokeLocalMethod: interpreter = null");
/*      */     }
/*      */     
/*  903 */     String commandName = this.value;
/*  904 */     Class[] argTypes = Types.getTypes(args);
/*      */ 
/*      */     
/*  907 */     BshMethod meth = null;
/*      */     try {
/*  909 */       meth = this.namespace.getMethod(commandName, argTypes);
/*  910 */     } catch (UtilEvalError e) {
/*  911 */       throw e.toEvalError("Local method invocation", callerInfo, callstack);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  916 */     if (meth != null) {
/*  917 */       return meth.invoke(args, interpreter, callstack, callerInfo);
/*      */     }
/*  919 */     BshClassManager bcm = interpreter.getClassManager();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  925 */       commandObject = this.namespace.getCommand(commandName, argTypes, interpreter);
/*      */     }
/*  927 */     catch (UtilEvalError e) {
/*  928 */       throw e.toEvalError("Error loading command: ", callerInfo, callstack);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  933 */     if (commandObject == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  938 */       BshMethod invokeMethod = null;
/*      */       try {
/*  940 */         invokeMethod = this.namespace.getMethod("invoke", new Class[] { null, null });
/*      */       }
/*  942 */       catch (UtilEvalError e) {
/*  943 */         throw e.toEvalError("Local method invocation", callerInfo, callstack);
/*      */       } 
/*      */ 
/*      */       
/*  947 */       if (invokeMethod != null) {
/*  948 */         return invokeMethod.invoke(new Object[] { commandName, args }, interpreter, callstack, callerInfo);
/*      */       }
/*      */ 
/*      */       
/*  952 */       throw new EvalError("Command not found: " + StringUtil.methodString(commandName, argTypes), callerInfo, callstack);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  957 */     if (commandObject instanceof BshMethod) {
/*  958 */       return ((BshMethod)commandObject).invoke(args, interpreter, callstack, callerInfo);
/*      */     }
/*      */     
/*  961 */     if (commandObject instanceof Class) {
/*      */       try {
/*  963 */         return Reflect.invokeCompiledCommand((Class)commandObject, args, interpreter, callstack);
/*      */       }
/*  965 */       catch (UtilEvalError e) {
/*  966 */         throw e.toEvalError("Error invoking compiled command: ", callerInfo, callstack);
/*      */       } 
/*      */     }
/*      */     
/*  970 */     throw new InterpreterError("invalid command type");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCompound(String value) {
/* 1002 */     return (value.indexOf('.') != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int countParts(String value) {
/* 1008 */     if (value == null) {
/* 1009 */       return 0;
/*      */     }
/* 1011 */     int count = 0;
/* 1012 */     int index = -1;
/* 1013 */     while ((index = value.indexOf('.', index + 1)) != -1)
/* 1014 */       count++; 
/* 1015 */     return count + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   static String prefix(String value) {
/* 1020 */     if (!isCompound(value)) {
/* 1021 */       return null;
/*      */     }
/* 1023 */     return prefix(value, countParts(value) - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   static String prefix(String value, int parts) {
/* 1028 */     if (parts < 1) {
/* 1029 */       return null;
/*      */     }
/* 1031 */     int count = 0;
/* 1032 */     int index = -1;
/*      */ 
/*      */     
/* 1035 */     while ((index = value.indexOf('.', index + 1)) != -1 && ++count < parts);
/*      */ 
/*      */     
/* 1038 */     return (index == -1) ? value : value.substring(0, index);
/*      */   }
/*      */ 
/*      */   
/*      */   static String suffix(String name) {
/* 1043 */     if (!isCompound(name)) {
/* 1044 */       return null;
/*      */     }
/* 1046 */     return suffix(name, countParts(name) - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String suffix(String value, int parts) {
/* 1051 */     if (parts < 1) {
/* 1052 */       return null;
/*      */     }
/* 1054 */     int count = 0;
/* 1055 */     int index = value.length() + 1;
/*      */ 
/*      */     
/* 1058 */     while ((index = value.lastIndexOf('.', index - 1)) != -1 && ++count < parts);
/*      */     
/* 1060 */     return (index == -1) ? value : value.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1066 */     return this.value;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Name.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */