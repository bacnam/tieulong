/*      */ package bsh;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
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
/*      */ public class NameSpace
/*      */   implements Serializable, BshClassManager.Listener, NameSource
/*      */ {
/*   70 */   public static final NameSpace JAVACODE = new NameSpace((BshClassManager)null, "Called from compiled Java code.");
/*      */   
/*      */   static {
/*   73 */     JAVACODE.isMethod = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String nsName;
/*      */ 
/*      */   
/*      */   private NameSpace parent;
/*      */ 
/*      */   
/*      */   private Hashtable variables;
/*      */ 
/*      */   
/*      */   private Hashtable methods;
/*      */ 
/*      */   
/*      */   protected Hashtable importedClasses;
/*      */ 
/*      */   
/*      */   private Vector importedPackages;
/*      */ 
/*      */   
/*      */   private Vector importedCommands;
/*      */ 
/*      */   
/*      */   private Vector importedObjects;
/*      */   
/*      */   private Vector importedStatic;
/*      */   
/*      */   private String packageName;
/*      */   
/*      */   private transient BshClassManager classManager;
/*      */   
/*      */   private This thisReference;
/*      */   
/*      */   private Hashtable names;
/*      */   
/*      */   SimpleNode callerInfoNode;
/*      */   
/*      */   boolean isMethod;
/*      */   
/*      */   boolean isClass;
/*      */   
/*      */   Class classStatic;
/*      */   
/*      */   Object classInstance;
/*      */   
/*      */   private transient Hashtable classCache;
/*      */   
/*      */   Vector nameSourceListeners;
/*      */ 
/*      */   
/*      */   void setClassStatic(Class clas) {
/*  127 */     this.classStatic = clas;
/*  128 */     importStatic(clas);
/*      */   }
/*      */   void setClassInstance(Object instance) {
/*  131 */     this.classInstance = instance;
/*  132 */     importObject(instance);
/*      */   }
/*      */ 
/*      */   
/*      */   Object getClassInstance() throws UtilEvalError {
/*  137 */     if (this.classInstance != null) {
/*  138 */       return this.classInstance;
/*      */     }
/*  140 */     if (this.classStatic != null)
/*      */     {
/*      */       
/*  143 */       throw new UtilEvalError("Can't refer to class instance from static context.");
/*      */     }
/*      */     
/*  146 */     throw new InterpreterError("Can't resolve class instance 'this' in: " + this);
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
/*      */   public NameSpace(NameSpace parent, String name) {
/*  171 */     this(parent, null, name);
/*      */   }
/*      */ 
/*      */   
/*      */   public NameSpace(BshClassManager classManager, String name) {
/*  176 */     this(null, classManager, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NameSpace(NameSpace parent, BshClassManager classManager, String name) {
/*  187 */     setName(name);
/*  188 */     setParent(parent);
/*  189 */     setClassManager(classManager);
/*      */ 
/*      */     
/*  192 */     if (classManager != null) {
/*  193 */       classManager.addListener(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  199 */     this.nsName = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  208 */     return this.nsName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setNode(SimpleNode node) {
/*  217 */     this.callerInfoNode = node;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SimpleNode getNode() {
/*  224 */     if (this.callerInfoNode != null)
/*  225 */       return this.callerInfoNode; 
/*  226 */     if (this.parent != null) {
/*  227 */       return this.parent.getNode();
/*      */     }
/*  229 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object get(String name, Interpreter interpreter) throws UtilEvalError {
/*  238 */     CallStack callstack = new CallStack(this);
/*  239 */     return getNameResolver(name).toObject(callstack, interpreter);
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
/*      */   public void setVariable(String name, Object value, boolean strictJava) throws UtilEvalError {
/*  261 */     boolean recurse = Interpreter.LOCALSCOPING ? strictJava : true;
/*  262 */     setVariable(name, value, strictJava, recurse);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setLocalVariable(String name, Object value, boolean strictJava) throws UtilEvalError {
/*  272 */     setVariable(name, value, strictJava, false);
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
/*      */   void setVariable(String name, Object value, boolean strictJava, boolean recurse) throws UtilEvalError {
/*  302 */     if (this.variables == null) {
/*  303 */       this.variables = new Hashtable<Object, Object>();
/*      */     }
/*      */     
/*  306 */     if (value == null) {
/*  307 */       throw new InterpreterError("null variable value");
/*      */     }
/*      */     
/*  310 */     Variable existing = getVariableImpl(name, recurse);
/*      */ 
/*      */     
/*  313 */     if (existing != null) {
/*      */       
/*      */       try {
/*  316 */         existing.setValue(value, 1);
/*  317 */       } catch (UtilEvalError e) {
/*  318 */         throw new UtilEvalError("Variable assignment: " + name + ": " + e.getMessage());
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  324 */       if (strictJava) {
/*  325 */         throw new UtilEvalError("(Strict Java mode) Assignment to undeclared variable: " + name);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  332 */       NameSpace varScope = this;
/*      */       
/*  334 */       varScope.variables.put(name, createVariable(name, value, (Modifiers)null));
/*      */ 
/*      */ 
/*      */       
/*  338 */       nameSpaceChanged();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Variable createVariable(String name, Object value, Modifiers mods) throws UtilEvalError {
/*  346 */     return createVariable(name, null, value, mods);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Variable createVariable(String name, Class type, Object value, Modifiers mods) throws UtilEvalError {
/*  353 */     return new Variable(name, type, value, mods);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Variable createVariable(String name, Class type, LHS lhs) throws UtilEvalError {
/*  360 */     return new Variable(name, type, lhs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unsetVariable(String name) {
/*  368 */     if (this.variables != null) {
/*      */       
/*  370 */       this.variables.remove(name);
/*  371 */       nameSpaceChanged();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getVariableNames() {
/*  380 */     if (this.variables == null) {
/*  381 */       return new String[0];
/*      */     }
/*  383 */     return enumerationToStringArray(this.variables.keys());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getMethodNames() {
/*  392 */     if (this.methods == null) {
/*  393 */       return new String[0];
/*      */     }
/*  395 */     return enumerationToStringArray(this.methods.keys());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BshMethod[] getMethods() {
/*  405 */     if (this.methods == null) {
/*  406 */       return new BshMethod[0];
/*      */     }
/*  408 */     return flattenMethodCollection(this.methods.elements());
/*      */   }
/*      */   
/*      */   private String[] enumerationToStringArray(Enumeration e) {
/*  412 */     Vector v = new Vector();
/*  413 */     while (e.hasMoreElements())
/*  414 */       v.addElement(e.nextElement()); 
/*  415 */     String[] sa = new String[v.size()];
/*  416 */     v.copyInto((Object[])sa);
/*  417 */     return sa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BshMethod[] flattenMethodCollection(Enumeration e) {
/*  425 */     Vector<Object> v = new Vector();
/*  426 */     while (e.hasMoreElements()) {
/*  427 */       Object o = e.nextElement();
/*  428 */       if (o instanceof BshMethod) {
/*  429 */         v.addElement(o); continue;
/*      */       } 
/*  431 */       Vector ov = (Vector)o;
/*  432 */       for (int i = 0; i < ov.size(); i++) {
/*  433 */         v.addElement(ov.elementAt(i));
/*      */       }
/*      */     } 
/*  436 */     BshMethod[] bma = new BshMethod[v.size()];
/*  437 */     v.copyInto((Object[])bma);
/*  438 */     return bma;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NameSpace getParent() {
/*  447 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public This getSuper(Interpreter declaringInterpreter) {
/*  456 */     if (this.parent != null) {
/*  457 */       return this.parent.getThis(declaringInterpreter);
/*      */     }
/*  459 */     return getThis(declaringInterpreter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public This getGlobal(Interpreter declaringInterpreter) {
/*  469 */     if (this.parent != null) {
/*  470 */       return this.parent.getGlobal(declaringInterpreter);
/*      */     }
/*  472 */     return getThis(declaringInterpreter);
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
/*      */ 
/*      */   
/*      */   public This getThis(Interpreter declaringInterpreter) {
/*  507 */     if (this.thisReference == null) {
/*  508 */       this.thisReference = This.getThis(this, declaringInterpreter);
/*      */     }
/*  510 */     return this.thisReference;
/*      */   }
/*      */ 
/*      */   
/*      */   public BshClassManager getClassManager() {
/*  515 */     if (this.classManager != null)
/*  516 */       return this.classManager; 
/*  517 */     if (this.parent != null && this.parent != JAVACODE) {
/*  518 */       return this.parent.getClassManager();
/*      */     }
/*      */     
/*  521 */     this.classManager = BshClassManager.createClassManager(null);
/*      */ 
/*      */     
/*  524 */     return this.classManager;
/*      */   }
/*      */   
/*      */   void setClassManager(BshClassManager classManager) {
/*  528 */     this.classManager = classManager;
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
/*      */   public void prune() {
/*  541 */     if (this.classManager == null)
/*      */     {
/*      */       
/*  544 */       setClassManager(BshClassManager.createClassManager(null));
/*      */     }
/*      */     
/*  547 */     setParent(null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setParent(NameSpace parent) {
/*  552 */     this.parent = parent;
/*      */ 
/*      */     
/*  555 */     if (parent == null) {
/*  556 */       loadDefaultImports();
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
/*      */   public Object getVariable(String name) throws UtilEvalError {
/*  572 */     return getVariable(name, true);
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
/*      */   public Object getVariable(String name, boolean recurse) throws UtilEvalError {
/*  590 */     Variable var = getVariableImpl(name, recurse);
/*  591 */     return unwrapVariable(var);
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
/*      */   protected Variable getVariableImpl(String name, boolean recurse) throws UtilEvalError {
/*  605 */     Variable var = null;
/*      */ 
/*      */ 
/*      */     
/*  609 */     if (var == null && this.isClass) {
/*  610 */       var = getImportedVar(name);
/*      */     }
/*  612 */     if (var == null && this.variables != null) {
/*  613 */       var = (Variable)this.variables.get(name);
/*      */     }
/*      */     
/*  616 */     if (var == null && !this.isClass) {
/*  617 */       var = getImportedVar(name);
/*      */     }
/*      */     
/*  620 */     if (recurse && var == null && this.parent != null) {
/*  621 */       var = this.parent.getVariableImpl(name, recurse);
/*      */     }
/*  623 */     return var;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Variable[] getDeclaredVariables() {
/*  631 */     if (this.variables == null)
/*  632 */       return new Variable[0]; 
/*  633 */     Variable[] vars = new Variable[this.variables.size()];
/*  634 */     int i = 0;
/*  635 */     for (Enumeration<Variable> e = this.variables.elements(); e.hasMoreElements();)
/*  636 */       vars[i++] = e.nextElement(); 
/*  637 */     return vars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object unwrapVariable(Variable var) throws UtilEvalError {
/*  648 */     return (var == null) ? Primitive.VOID : var.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTypedVariable(String name, Class type, Object value, boolean isFinal) throws UtilEvalError {
/*  658 */     Modifiers modifiers = new Modifiers();
/*  659 */     if (isFinal)
/*  660 */       modifiers.addModifier(2, "final"); 
/*  661 */     setTypedVariable(name, type, value, modifiers);
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
/*      */   public void setTypedVariable(String name, Class type, Object value, Modifiers modifiers) throws UtilEvalError {
/*  689 */     if (this.variables == null) {
/*  690 */       this.variables = new Hashtable<Object, Object>();
/*      */     }
/*      */     
/*  693 */     Variable existing = getVariableImpl(name, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  699 */     if (existing != null)
/*      */     {
/*      */       
/*  702 */       if (existing.getType() != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  707 */         if (existing.getType() != type)
/*      */         {
/*  709 */           throw new UtilEvalError("Typed variable: " + name + " was previously declared with type: " + existing.getType());
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  715 */         existing.setValue(value, 0);
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  724 */     this.variables.put(name, createVariable(name, type, value, modifiers));
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
/*      */   public void setMethod(String name, BshMethod method) throws UtilEvalError {
/*  749 */     if (this.methods == null) {
/*  750 */       this.methods = new Hashtable<Object, Object>();
/*      */     }
/*  752 */     Object m = this.methods.get(name);
/*      */     
/*  754 */     if (m == null) {
/*  755 */       this.methods.put(name, method);
/*      */     }
/*  757 */     else if (m instanceof BshMethod) {
/*  758 */       Vector<Object> v = new Vector();
/*  759 */       v.addElement(m);
/*  760 */       v.addElement(method);
/*  761 */       this.methods.put(name, v);
/*      */     } else {
/*  763 */       ((Vector<BshMethod>)m).addElement(method);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BshMethod getMethod(String name, Class[] sig) throws UtilEvalError {
/*  773 */     return getMethod(name, sig, false);
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
/*      */   public BshMethod getMethod(String name, Class[] sig, boolean declaredOnly) throws UtilEvalError {
/*  793 */     BshMethod method = null;
/*      */ 
/*      */ 
/*      */     
/*  797 */     if (method == null && this.isClass && !declaredOnly) {
/*  798 */       method = getImportedMethod(name, sig);
/*      */     }
/*  800 */     Object m = null;
/*  801 */     if (method == null && this.methods != null) {
/*      */       
/*  803 */       m = this.methods.get(name);
/*      */ 
/*      */       
/*  806 */       if (m != null) {
/*      */         BshMethod[] ma;
/*      */ 
/*      */         
/*  810 */         if (m instanceof Vector) {
/*      */           
/*  812 */           Vector vm = (Vector)m;
/*  813 */           ma = new BshMethod[vm.size()];
/*  814 */           vm.copyInto((Object[])ma);
/*      */         } else {
/*  816 */           ma = new BshMethod[] { (BshMethod)m };
/*      */         } 
/*      */         
/*  819 */         Class[][] candidates = new Class[ma.length][];
/*  820 */         for (int i = 0; i < ma.length; i++) {
/*  821 */           candidates[i] = ma[i].getParameterTypes();
/*      */         }
/*  823 */         int match = Reflect.findMostSpecificSignature(sig, candidates);
/*      */         
/*  825 */         if (match != -1) {
/*  826 */           method = ma[match];
/*      */         }
/*      */       } 
/*      */     } 
/*  830 */     if (method == null && !this.isClass && !declaredOnly) {
/*  831 */       method = getImportedMethod(name, sig);
/*      */     }
/*      */     
/*  834 */     if (!declaredOnly && method == null && this.parent != null) {
/*  835 */       return this.parent.getMethod(name, sig);
/*      */     }
/*  837 */     return method;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void importClass(String name) {
/*  846 */     if (this.importedClasses == null) {
/*  847 */       this.importedClasses = new Hashtable<Object, Object>();
/*      */     }
/*  849 */     this.importedClasses.put(Name.suffix(name, 1), name);
/*  850 */     nameSpaceChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void importPackage(String name) {
/*  858 */     if (this.importedPackages == null) {
/*  859 */       this.importedPackages = new Vector();
/*      */     }
/*      */     
/*  862 */     if (this.importedPackages.contains(name)) {
/*  863 */       this.importedPackages.remove(name);
/*      */     }
/*  865 */     this.importedPackages.addElement(name);
/*  866 */     nameSpaceChanged();
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
/*      */   public void importCommands(String name) {
/*  878 */     if (this.importedCommands == null) {
/*  879 */       this.importedCommands = new Vector();
/*      */     }
/*      */     
/*  882 */     name = name.replace('.', '/');
/*      */     
/*  884 */     if (!name.startsWith("/")) {
/*  885 */       name = "/" + name;
/*      */     }
/*  887 */     if (name.length() > 1 && name.endsWith("/")) {
/*  888 */       name = name.substring(0, name.length() - 1);
/*      */     }
/*      */     
/*  891 */     if (this.importedCommands.contains(name)) {
/*  892 */       this.importedCommands.remove(name);
/*      */     }
/*  894 */     this.importedCommands.addElement(name);
/*  895 */     nameSpaceChanged();
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
/*      */   
/*      */   public Object getCommand(String name, Class[] argTypes, Interpreter interpreter) throws UtilEvalError {
/*  929 */     if (Interpreter.DEBUG) Interpreter.debug("getCommand: " + name); 
/*  930 */     BshClassManager bcm = interpreter.getClassManager();
/*      */     
/*  932 */     if (this.importedCommands != null)
/*      */     {
/*      */       
/*  935 */       for (int i = this.importedCommands.size() - 1; i >= 0; i--) {
/*      */         
/*  937 */         String scriptPath, className, path = this.importedCommands.elementAt(i);
/*      */ 
/*      */         
/*  940 */         if (path.equals("/")) {
/*  941 */           scriptPath = path + name + ".bsh";
/*      */         } else {
/*  943 */           scriptPath = path + "/" + name + ".bsh";
/*      */         } 
/*  945 */         Interpreter.debug("searching for script: " + scriptPath);
/*      */         
/*  947 */         InputStream in = bcm.getResourceAsStream(scriptPath);
/*      */         
/*  949 */         if (in != null) {
/*  950 */           return loadScriptedCommand(in, name, argTypes, scriptPath, interpreter);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  955 */         if (path.equals("/")) {
/*  956 */           className = name;
/*      */         } else {
/*  958 */           className = path.substring(1).replace('/', '.') + "." + name;
/*      */         } 
/*  960 */         Interpreter.debug("searching for class: " + className);
/*  961 */         Class clas = bcm.classForName(className);
/*  962 */         if (clas != null) {
/*  963 */           return clas;
/*      */         }
/*      */       } 
/*      */     }
/*  967 */     if (this.parent != null) {
/*  968 */       return this.parent.getCommand(name, argTypes, interpreter);
/*      */     }
/*  970 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BshMethod getImportedMethod(String name, Class[] sig) throws UtilEvalError {
/*  977 */     if (this.importedObjects != null) {
/*  978 */       for (int i = 0; i < this.importedObjects.size(); i++) {
/*      */         
/*  980 */         Object object = this.importedObjects.elementAt(i);
/*  981 */         Class<?> clas = object.getClass();
/*  982 */         Method method = Reflect.resolveJavaMethod(getClassManager(), clas, name, sig, false);
/*      */         
/*  984 */         if (method != null) {
/*  985 */           return new BshMethod(method, object);
/*      */         }
/*      */       } 
/*      */     }
/*  989 */     if (this.importedStatic != null)
/*  990 */       for (int i = 0; i < this.importedStatic.size(); i++) {
/*      */         
/*  992 */         Class clas = this.importedStatic.elementAt(i);
/*  993 */         Method method = Reflect.resolveJavaMethod(getClassManager(), clas, name, sig, true);
/*      */         
/*  995 */         if (method != null) {
/*  996 */           return new BshMethod(method, null);
/*      */         }
/*      */       }  
/*  999 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Variable getImportedVar(String name) throws UtilEvalError {
/* 1006 */     if (this.importedObjects != null) {
/* 1007 */       for (int i = 0; i < this.importedObjects.size(); i++) {
/*      */         
/* 1009 */         Object object = this.importedObjects.elementAt(i);
/* 1010 */         Class<?> clas = object.getClass();
/* 1011 */         Field field = Reflect.resolveJavaField(clas, name, false);
/*      */         
/* 1013 */         if (field != null) {
/* 1014 */           return createVariable(name, field.getType(), new LHS(object, field));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1019 */     if (this.importedStatic != null)
/* 1020 */       for (int i = 0; i < this.importedStatic.size(); i++) {
/*      */         
/* 1022 */         Class clas = this.importedStatic.elementAt(i);
/* 1023 */         Field field = Reflect.resolveJavaField(clas, name, true);
/*      */         
/* 1025 */         if (field != null) {
/* 1026 */           return createVariable(name, field.getType(), new LHS(field));
/*      */         }
/*      */       }  
/* 1029 */     return null;
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
/*      */   private BshMethod loadScriptedCommand(InputStream in, String name, Class[] argTypes, String resourcePath, Interpreter interpreter) throws UtilEvalError {
/*      */     try {
/* 1048 */       interpreter.eval(new InputStreamReader(in), this, resourcePath);
/*      */     }
/* 1050 */     catch (EvalError e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1056 */       Interpreter.debug(e.toString());
/* 1057 */       throw new UtilEvalError("Error loading script: " + e.getMessage());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1062 */     BshMethod meth = getMethod(name, argTypes);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1069 */     return meth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void cacheClass(String name, Class<?> c) {
/* 1076 */     if (this.classCache == null) {
/* 1077 */       this.classCache = new Hashtable<Object, Object>();
/*      */     }
/*      */ 
/*      */     
/* 1081 */     this.classCache.put(name, c);
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
/*      */   public Class getClass(String name) throws UtilEvalError {
/* 1094 */     Class c = getClassImpl(name);
/* 1095 */     if (c != null) {
/* 1096 */       return c;
/*      */     }
/*      */     
/* 1099 */     if (this.parent != null) {
/* 1100 */       return this.parent.getClass(name);
/*      */     }
/* 1102 */     return null;
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
/*      */   private Class getClassImpl(String name) throws UtilEvalError {
/* 1125 */     Class c = null;
/*      */ 
/*      */     
/* 1128 */     if (this.classCache != null) {
/* 1129 */       c = (Class)this.classCache.get(name);
/*      */       
/* 1131 */       if (c != null) {
/* 1132 */         return c;
/*      */       }
/*      */     } 
/*      */     
/* 1136 */     boolean unqualifiedName = !Name.isCompound(name);
/*      */ 
/*      */     
/* 1139 */     if (unqualifiedName) {
/*      */ 
/*      */       
/* 1142 */       if (c == null) {
/* 1143 */         c = getImportedClassImpl(name);
/*      */       }
/*      */       
/* 1146 */       if (c != null) {
/* 1147 */         cacheClass(name, c);
/* 1148 */         return c;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     c = classForName(name);
/* 1154 */     if (c != null) {
/*      */       
/* 1156 */       if (unqualifiedName)
/* 1157 */         cacheClass(name, c); 
/* 1158 */       return c;
/*      */     } 
/*      */ 
/*      */     
/* 1162 */     if (Interpreter.DEBUG)
/* 1163 */       Interpreter.debug("getClass(): " + name + " not\tfound in " + this); 
/* 1164 */     return null;
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
/*      */   private Class getImportedClassImpl(String name) throws UtilEvalError {
/* 1176 */     String fullname = null;
/* 1177 */     if (this.importedClasses != null) {
/* 1178 */       fullname = (String)this.importedClasses.get(name);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1183 */     if (fullname != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1189 */       Class clas = classForName(fullname);
/*      */ 
/*      */       
/* 1192 */       if (clas == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1198 */         if (Name.isCompound(fullname))
/*      */         { try {
/* 1200 */             clas = getNameResolver(fullname).toClass();
/* 1201 */           } catch (ClassNotFoundException e) {} }
/*      */         
/* 1203 */         else if (Interpreter.DEBUG) { Interpreter.debug("imported unpackaged name not found:" + fullname); }
/*      */ 
/*      */ 
/*      */         
/* 1207 */         if (clas != null) {
/*      */           
/* 1209 */           getClassManager().cacheClassInfo(fullname, clas);
/* 1210 */           return clas;
/*      */         } 
/*      */       } else {
/* 1213 */         return clas;
/*      */       } 
/*      */ 
/*      */       
/* 1217 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     if (this.importedPackages != null)
/* 1226 */       for (int i = this.importedPackages.size() - 1; i >= 0; i--) {
/*      */         
/* 1228 */         String s = (String)this.importedPackages.elementAt(i) + "." + name;
/* 1229 */         Class c = classForName(s);
/* 1230 */         if (c != null) {
/* 1231 */           return c;
/*      */         }
/*      */       }  
/* 1234 */     BshClassManager bcm = getClassManager();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1241 */     if (bcm.hasSuperImport()) {
/*      */       
/* 1243 */       String s = bcm.getClassNameByUnqName(name);
/* 1244 */       if (s != null) {
/* 1245 */         return classForName(s);
/*      */       }
/*      */     } 
/* 1248 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private Class classForName(String name) {
/* 1253 */     return getClassManager().classForName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAllNames() {
/* 1263 */     Vector vec = new Vector();
/* 1264 */     getAllNamesAux(vec);
/* 1265 */     String[] names = new String[vec.size()];
/* 1266 */     vec.copyInto((Object[])names);
/* 1267 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void getAllNamesAux(Vector vec) {
/* 1275 */     Enumeration varNames = this.variables.keys();
/* 1276 */     while (varNames.hasMoreElements()) {
/* 1277 */       vec.addElement(varNames.nextElement());
/*      */     }
/* 1279 */     Enumeration methodNames = this.methods.keys();
/* 1280 */     while (methodNames.hasMoreElements()) {
/* 1281 */       vec.addElement(methodNames.nextElement());
/*      */     }
/* 1283 */     if (this.parent != null) {
/* 1284 */       this.parent.getAllNamesAux(vec);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNameSourceListener(NameSource.Listener listener) {
/* 1293 */     if (this.nameSourceListeners == null)
/* 1294 */       this.nameSourceListeners = new Vector(); 
/* 1295 */     this.nameSourceListeners.addElement(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doSuperImport() throws UtilEvalError {
/* 1305 */     getClassManager().doSuperImport();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1310 */     return "NameSpace: " + ((this.nsName == null) ? super.toString() : (this.nsName + " (" + super.toString() + ")")) + (this.isClass ? " (isClass) " : "") + (this.isMethod ? " (method) " : "") + ((this.classStatic != null) ? " (class static) " : "") + ((this.classInstance != null) ? " (class instance) " : "");
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
/*      */   private synchronized void writeObject(ObjectOutputStream s) throws IOException {
/* 1328 */     this.names = null;
/*      */     
/* 1330 */     s.defaultWriteObject();
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
/*      */   public Object invokeMethod(String methodName, Object[] args, Interpreter interpreter) throws EvalError {
/* 1347 */     return invokeMethod(methodName, args, interpreter, null, null);
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
/*      */   public Object invokeMethod(String methodName, Object[] args, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo) throws EvalError {
/* 1363 */     return getThis(interpreter).invokeMethod(methodName, args, interpreter, callstack, callerInfo, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void classLoaderChanged() {
/* 1372 */     nameSpaceChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void nameSpaceChanged() {
/* 1379 */     this.classCache = null;
/* 1380 */     this.names = null;
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
/*      */   public void loadDefaultImports() {
/* 1406 */     importClass("bsh.EvalError");
/* 1407 */     importClass("bsh.Interpreter");
/* 1408 */     importPackage("javax.swing.event");
/* 1409 */     importPackage("javax.swing");
/* 1410 */     importPackage("java.awt.event");
/* 1411 */     importPackage("java.awt");
/* 1412 */     importPackage("java.net");
/* 1413 */     importPackage("java.util");
/* 1414 */     importPackage("java.io");
/* 1415 */     importPackage("java.lang");
/* 1416 */     importCommands("/bsh/commands");
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
/*      */   Name getNameResolver(String ambigname) {
/* 1442 */     if (this.names == null) {
/* 1443 */       this.names = new Hashtable<Object, Object>();
/*      */     }
/* 1445 */     Name name = (Name)this.names.get(ambigname);
/*      */     
/* 1447 */     if (name == null) {
/* 1448 */       name = new Name(this, ambigname);
/* 1449 */       this.names.put(ambigname, name);
/*      */     } 
/*      */     
/* 1452 */     return name;
/*      */   }
/*      */   
/*      */   public int getInvocationLine() {
/* 1456 */     SimpleNode node = getNode();
/* 1457 */     if (node != null) {
/* 1458 */       return node.getLineNumber();
/*      */     }
/* 1460 */     return -1;
/*      */   }
/*      */   public String getInvocationText() {
/* 1463 */     SimpleNode node = getNode();
/* 1464 */     if (node != null) {
/* 1465 */       return node.getText();
/*      */     }
/* 1467 */     return "<invoked from Java code>";
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
/*      */   public static Class identifierToClass(ClassIdentifier ci) {
/* 1481 */     return ci.getTargetClass();
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
/*      */   public void clear() {
/* 1493 */     this.variables = null;
/* 1494 */     this.methods = null;
/* 1495 */     this.importedClasses = null;
/* 1496 */     this.importedPackages = null;
/* 1497 */     this.importedCommands = null;
/* 1498 */     this.importedObjects = null;
/* 1499 */     if (this.parent == null)
/* 1500 */       loadDefaultImports(); 
/* 1501 */     this.classCache = null;
/* 1502 */     this.names = null;
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
/*      */   public void importObject(Object obj) {
/* 1519 */     if (this.importedObjects == null) {
/* 1520 */       this.importedObjects = new Vector();
/*      */     }
/*      */     
/* 1523 */     if (this.importedObjects.contains(obj)) {
/* 1524 */       this.importedObjects.remove(obj);
/*      */     }
/* 1526 */     this.importedObjects.addElement(obj);
/* 1527 */     nameSpaceChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void importStatic(Class<?> clas) {
/* 1535 */     if (this.importedStatic == null) {
/* 1536 */       this.importedStatic = new Vector();
/*      */     }
/*      */     
/* 1539 */     if (this.importedStatic.contains(clas)) {
/* 1540 */       this.importedStatic.remove(clas);
/*      */     }
/* 1542 */     this.importedStatic.addElement(clas);
/* 1543 */     nameSpaceChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setPackage(String packageName) {
/* 1552 */     this.packageName = packageName;
/*      */   }
/*      */ 
/*      */   
/*      */   String getPackage() {
/* 1557 */     if (this.packageName != null) {
/* 1558 */       return this.packageName;
/*      */     }
/* 1560 */     if (this.parent != null) {
/* 1561 */       return this.parent.getPackage();
/*      */     }
/* 1563 */     return null;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/NameSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */