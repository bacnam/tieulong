/*      */ package bsh;
/*      */ 
/*      */ import bsh.org.objectweb.asm.ClassWriter;
/*      */ import bsh.org.objectweb.asm.CodeVisitor;
/*      */ import bsh.org.objectweb.asm.Constants;
/*      */ import bsh.org.objectweb.asm.Label;
/*      */ import bsh.org.objectweb.asm.Type;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassGeneratorUtil
/*      */   implements Constants
/*      */ {
/*      */   static final String BSHSTATIC = "_bshStatic";
/*      */   static final String BSHTHIS = "_bshThis";
/*      */   static final String BSHSUPER = "_bshSuper";
/*      */   static final String BSHINIT = "_bshInstanceInitializer";
/*      */   static final String BSHCONSTRUCTORS = "_bshConstructors";
/*      */   static final int DEFAULTCONSTRUCTOR = -1;
/*      */   static final String OBJECT = "Ljava/lang/Object;";
/*      */   String className;
/*      */   String fqClassName;
/*      */   Class superClass;
/*      */   String superClassName;
/*      */   Class[] interfaces;
/*      */   Variable[] vars;
/*      */   Constructor[] superConstructors;
/*      */   DelayedEvalBshMethod[] constructors;
/*      */   DelayedEvalBshMethod[] methods;
/*      */   NameSpace classStaticNameSpace;
/*      */   Modifiers classModifiers;
/*      */   boolean isInterface;
/*      */   
/*      */   public ClassGeneratorUtil(Modifiers classModifiers, String className, String packageName, Class<Object> superClass, Class[] interfaces, Variable[] vars, DelayedEvalBshMethod[] bshmethods, NameSpace classStaticNameSpace, boolean isInterface) {
/*  131 */     this.classModifiers = classModifiers;
/*  132 */     this.className = className;
/*  133 */     if (packageName != null) {
/*  134 */       this.fqClassName = packageName.replace('.', '/') + "/" + className;
/*      */     } else {
/*  136 */       this.fqClassName = className;
/*  137 */     }  if (superClass == null)
/*  138 */       superClass = Object.class; 
/*  139 */     this.superClass = superClass;
/*  140 */     this.superClassName = Type.getInternalName(superClass);
/*  141 */     if (interfaces == null)
/*  142 */       interfaces = new Class[0]; 
/*  143 */     this.interfaces = interfaces;
/*  144 */     this.vars = vars;
/*  145 */     this.classStaticNameSpace = classStaticNameSpace;
/*  146 */     this.superConstructors = (Constructor[])superClass.getDeclaredConstructors();
/*      */ 
/*      */     
/*  149 */     List<BshMethod> consl = new ArrayList();
/*  150 */     List<BshMethod> methodsl = new ArrayList();
/*  151 */     String classBaseName = getBaseName(className);
/*  152 */     for (int i = 0; i < bshmethods.length; i++) {
/*      */       
/*  154 */       BshMethod bm = bshmethods[i];
/*      */       
/*  156 */       if (bm.getName().equals(classBaseName) && bm.getReturnType() == null) {
/*      */ 
/*      */         
/*  159 */         consl.add(bm);
/*      */       } else {
/*  161 */         methodsl.add(bm);
/*      */       } 
/*      */     } 
/*  164 */     this.constructors = consl.<DelayedEvalBshMethod>toArray(new DelayedEvalBshMethod[0]);
/*      */     
/*  166 */     this.methods = methodsl.<DelayedEvalBshMethod>toArray(new DelayedEvalBshMethod[0]);
/*      */ 
/*      */     
/*      */     try {
/*  170 */       classStaticNameSpace.setLocalVariable("_bshConstructors", this.constructors, false);
/*      */     }
/*  172 */     catch (UtilEvalError e) {
/*  173 */       throw new InterpreterError("can't set cons var");
/*      */     } 
/*      */     
/*  176 */     this.isInterface = isInterface;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] generateClass() {
/*  185 */     int classMods = getASMModifiers(this.classModifiers) | 0x1;
/*  186 */     if (this.isInterface) {
/*  187 */       classMods |= 0x200;
/*      */     }
/*  189 */     String[] interfaceNames = new String[this.interfaces.length];
/*  190 */     for (int i = 0; i < this.interfaces.length; i++) {
/*  191 */       interfaceNames[i] = Type.getInternalName(this.interfaces[i]);
/*      */     }
/*  193 */     String sourceFile = "BeanShell Generated via ASM (www.objectweb.org)";
/*  194 */     ClassWriter cw = new ClassWriter(true);
/*  195 */     cw.visit(classMods, this.fqClassName, this.superClassName, interfaceNames, sourceFile);
/*      */ 
/*      */     
/*  198 */     if (!this.isInterface) {
/*      */ 
/*      */       
/*  201 */       generateField("_bshThis" + this.className, "Lbsh/This;", 1, cw);
/*      */ 
/*      */ 
/*      */       
/*  205 */       generateField("_bshStatic" + this.className, "Lbsh/This;", 9, cw);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  210 */     for (int j = 0; j < this.vars.length; j++) {
/*      */       
/*  212 */       String type = this.vars[j].getTypeDescriptor();
/*      */ 
/*      */ 
/*      */       
/*  216 */       if (!this.vars[j].hasModifier("private") && type != null) {
/*      */         int modifiers;
/*      */ 
/*      */         
/*  220 */         if (this.isInterface) {
/*  221 */           modifiers = 25;
/*      */         } else {
/*  223 */           modifiers = getASMModifiers(this.vars[j].getModifiers());
/*      */         } 
/*  225 */         generateField(this.vars[j].getName(), type, modifiers, cw);
/*      */       } 
/*      */     } 
/*      */     
/*  229 */     boolean hasConstructor = false; int k;
/*  230 */     for (k = 0; k < this.constructors.length; k++) {
/*      */ 
/*      */       
/*  233 */       if (!this.constructors[k].hasModifier("private")) {
/*      */ 
/*      */         
/*  236 */         int modifiers = getASMModifiers(this.constructors[k].getModifiers());
/*  237 */         generateConstructor(k, this.constructors[k].getParamTypeDescriptors(), modifiers, cw);
/*      */         
/*  239 */         hasConstructor = true;
/*      */       } 
/*      */     } 
/*      */     
/*  243 */     if (!this.isInterface && !hasConstructor) {
/*  244 */       generateConstructor(-1, new String[0], 1, cw);
/*      */     }
/*      */ 
/*      */     
/*  248 */     for (k = 0; k < this.methods.length; k++) {
/*      */       
/*  250 */       String returnType = this.methods[k].getReturnTypeDescriptor();
/*      */ 
/*      */       
/*  253 */       if (!this.methods[k].hasModifier("private")) {
/*      */ 
/*      */         
/*  256 */         int modifiers = getASMModifiers(this.methods[k].getModifiers());
/*  257 */         if (this.isInterface) {
/*  258 */           modifiers |= 0x401;
/*      */         }
/*  260 */         generateMethod(this.className, this.fqClassName, this.methods[k].getName(), returnType, this.methods[k].getParamTypeDescriptors(), modifiers, cw);
/*      */ 
/*      */ 
/*      */         
/*  264 */         boolean isStatic = ((modifiers & 0x8) > 0);
/*  265 */         boolean overridden = classContainsMethod(this.superClass, this.methods[k].getName(), this.methods[k].getParamTypeDescriptors());
/*      */ 
/*      */         
/*  268 */         if (!isStatic && overridden) {
/*  269 */           generateSuperDelegateMethod(this.superClassName, this.methods[k].getName(), returnType, this.methods[k].getParamTypeDescriptors(), modifiers, cw);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  274 */     return cw.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int getASMModifiers(Modifiers modifiers) {
/*  282 */     int mods = 0;
/*  283 */     if (modifiers == null) {
/*  284 */       return mods;
/*      */     }
/*  286 */     if (modifiers.hasModifier("public"))
/*  287 */       mods++; 
/*  288 */     if (modifiers.hasModifier("protected"))
/*  289 */       mods += 4; 
/*  290 */     if (modifiers.hasModifier("static"))
/*  291 */       mods += 8; 
/*  292 */     if (modifiers.hasModifier("synchronized"))
/*  293 */       mods += 32; 
/*  294 */     if (modifiers.hasModifier("abstract")) {
/*  295 */       mods += 1024;
/*      */     }
/*  297 */     return mods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void generateField(String fieldName, String type, int modifiers, ClassWriter cw) {
/*  306 */     cw.visitField(modifiers, fieldName, type, null);
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
/*      */   static void generateMethod(String className, String fqClassName, String methodName, String returnType, String[] paramTypes, int modifiers, ClassWriter cw) {
/*  320 */     String[] exceptions = null;
/*  321 */     boolean isStatic = ((modifiers & 0x8) != 0);
/*      */     
/*  323 */     if (returnType == null) {
/*  324 */       returnType = "Ljava/lang/Object;";
/*      */     }
/*  326 */     String methodDescriptor = getMethodDescriptor(returnType, paramTypes);
/*      */ 
/*      */     
/*  329 */     CodeVisitor cv = cw.visitMethod(modifiers, methodName, methodDescriptor, exceptions);
/*      */ 
/*      */     
/*  332 */     if ((modifiers & 0x400) != 0) {
/*      */       return;
/*      */     }
/*      */     
/*  336 */     if (isStatic) {
/*      */       
/*  338 */       cv.visitFieldInsn(178, fqClassName, "_bshStatic" + className, "Lbsh/This;");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  343 */       cv.visitVarInsn(25, 0);
/*      */ 
/*      */       
/*  346 */       cv.visitFieldInsn(180, fqClassName, "_bshThis" + className, "Lbsh/This;");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  351 */     cv.visitLdcInsn(methodName);
/*      */ 
/*      */     
/*  354 */     generateParameterReifierCode(paramTypes, isStatic, cv);
/*      */ 
/*      */     
/*  357 */     cv.visitInsn(1);
/*  358 */     cv.visitInsn(1);
/*  359 */     cv.visitInsn(1);
/*      */ 
/*      */     
/*  362 */     cv.visitInsn(4);
/*      */ 
/*      */     
/*  365 */     cv.visitMethodInsn(182, "bsh/This", "invokeMethod", Type.getMethodDescriptor(Type.getType(Object.class), new Type[] { Type.getType(String.class), Type.getType(Object[].class), Type.getType(Interpreter.class), Type.getType(CallStack.class), Type.getType(SimpleNode.class), Type.getType(boolean.class) }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  381 */     cv.visitMethodInsn(184, "bsh/Primitive", "unwrap", "(Ljava/lang/Object;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     generateReturnCode(returnType, cv);
/*      */ 
/*      */     
/*  389 */     cv.visitMaxs(0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void generateConstructor(int index, String[] paramTypes, int modifiers, ClassWriter cw) {
/*  399 */     int argsVar = paramTypes.length + 1;
/*      */     
/*  401 */     int consArgsVar = paramTypes.length + 2;
/*      */     
/*  403 */     String[] exceptions = null;
/*  404 */     String methodDescriptor = getMethodDescriptor("V", paramTypes);
/*      */ 
/*      */     
/*  407 */     CodeVisitor cv = cw.visitMethod(modifiers, "<init>", methodDescriptor, exceptions);
/*      */ 
/*      */ 
/*      */     
/*  411 */     generateParameterReifierCode(paramTypes, false, cv);
/*  412 */     cv.visitVarInsn(58, argsVar);
/*      */ 
/*      */     
/*  415 */     generateConstructorSwitch(index, argsVar, consArgsVar, cv);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  420 */     cv.visitVarInsn(25, 0);
/*      */ 
/*      */     
/*  423 */     cv.visitLdcInsn(this.className);
/*      */ 
/*      */     
/*  426 */     cv.visitVarInsn(25, argsVar);
/*      */ 
/*      */     
/*  429 */     cv.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "initInstance", "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)V");
/*      */ 
/*      */ 
/*      */     
/*  433 */     cv.visitInsn(177);
/*      */ 
/*      */     
/*  436 */     cv.visitMaxs(0, 0);
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
/*      */   void generateConstructorSwitch(int consIndex, int argsVar, int consArgsVar, CodeVisitor cv) {
/*  455 */     Label defaultLabel = new Label();
/*  456 */     Label endLabel = new Label();
/*  457 */     int cases = this.superConstructors.length + this.constructors.length;
/*      */     
/*  459 */     Label[] labels = new Label[cases];
/*  460 */     for (int i = 0; i < cases; i++) {
/*  461 */       labels[i] = new Label();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  467 */     cv.visitLdcInsn(this.superClass.getName());
/*      */ 
/*      */     
/*  470 */     cv.visitFieldInsn(178, this.fqClassName, "_bshStatic" + this.className, "Lbsh/This;");
/*      */ 
/*      */ 
/*      */     
/*  474 */     cv.visitVarInsn(25, argsVar);
/*      */ 
/*      */     
/*  477 */     cv.visitIntInsn(16, consIndex);
/*      */ 
/*      */     
/*  480 */     cv.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "getConstructorArgs", "(Ljava/lang/String;Lbsh/This;[Ljava/lang/Object;I)Lbsh/ClassGeneratorUtil$ConstructorArgs;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  487 */     cv.visitVarInsn(58, consArgsVar);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  492 */     cv.visitVarInsn(25, consArgsVar);
/*  493 */     cv.visitFieldInsn(180, "bsh/ClassGeneratorUtil$ConstructorArgs", "selector", "I");
/*      */ 
/*      */ 
/*      */     
/*  497 */     cv.visitTableSwitchInsn(0, cases - 1, defaultLabel, labels);
/*      */ 
/*      */ 
/*      */     
/*  501 */     int index = 0; int j;
/*  502 */     for (j = 0; j < this.superConstructors.length; j++, index++) {
/*  503 */       doSwitchBranch(index, this.superClassName, getTypeDescriptors(this.superConstructors[j].getParameterTypes()), endLabel, labels, consArgsVar, cv);
/*      */     }
/*      */     
/*  506 */     for (j = 0; j < this.constructors.length; j++, index++) {
/*  507 */       doSwitchBranch(index, this.fqClassName, this.constructors[j].getParamTypeDescriptors(), endLabel, labels, consArgsVar, cv);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  512 */     cv.visitLabel(defaultLabel);
/*      */     
/*  514 */     cv.visitVarInsn(25, 0);
/*  515 */     cv.visitMethodInsn(183, this.superClassName, "<init>", "()V");
/*      */ 
/*      */     
/*  518 */     cv.visitLabel(endLabel);
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
/*      */   static void doSwitchBranch(int index, String targetClassName, String[] paramTypes, Label endLabel, Label[] labels, int consArgsVar, CodeVisitor cv) {
/*  532 */     cv.visitLabel(labels[index]);
/*      */     
/*  534 */     cv.visitVarInsn(25, 0);
/*      */ 
/*      */     
/*  537 */     for (int i = 0; i < paramTypes.length; i++) {
/*      */       
/*  539 */       String retType, type = paramTypes[i];
/*  540 */       String method = null;
/*  541 */       if (type.equals("Z")) {
/*  542 */         method = "getBoolean";
/*  543 */       } else if (type.equals("B")) {
/*  544 */         method = "getByte";
/*  545 */       } else if (type.equals("C")) {
/*  546 */         method = "getChar";
/*  547 */       } else if (type.equals("S")) {
/*  548 */         method = "getShort";
/*  549 */       } else if (type.equals("I")) {
/*  550 */         method = "getInt";
/*  551 */       } else if (type.equals("J")) {
/*  552 */         method = "getLong";
/*  553 */       } else if (type.equals("D")) {
/*  554 */         method = "getDouble";
/*  555 */       } else if (type.equals("F")) {
/*  556 */         method = "getFloat";
/*      */       } else {
/*  558 */         method = "getObject";
/*      */       } 
/*      */       
/*  561 */       cv.visitVarInsn(25, consArgsVar);
/*  562 */       String className = "bsh/ClassGeneratorUtil$ConstructorArgs";
/*      */       
/*  564 */       if (method.equals("getObject")) {
/*  565 */         retType = "Ljava/lang/Object;";
/*      */       } else {
/*  567 */         retType = type;
/*  568 */       }  cv.visitMethodInsn(182, className, method, "()" + retType);
/*      */       
/*  570 */       if (method.equals("getObject")) {
/*  571 */         cv.visitTypeInsn(192, descriptorToClassName(type));
/*      */       }
/*      */     } 
/*      */     
/*  575 */     String descriptor = getMethodDescriptor("V", paramTypes);
/*  576 */     cv.visitMethodInsn(183, targetClassName, "<init>", descriptor);
/*      */     
/*  578 */     cv.visitJumpInsn(167, endLabel);
/*      */   }
/*      */ 
/*      */   
/*      */   static String getMethodDescriptor(String returnType, String[] paramTypes) {
/*  583 */     StringBuffer sb = new StringBuffer("(");
/*  584 */     for (int i = 0; i < paramTypes.length; i++)
/*  585 */       sb.append(paramTypes[i]); 
/*  586 */     sb.append(")" + returnType);
/*  587 */     return sb.toString();
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
/*      */   static void generateSuperDelegateMethod(String superClassName, String methodName, String returnType, String[] paramTypes, int modifiers, ClassWriter cw) {
/*  600 */     String[] exceptions = null;
/*      */     
/*  602 */     if (returnType == null) {
/*  603 */       returnType = "Ljava/lang/Object;";
/*      */     }
/*  605 */     String methodDescriptor = getMethodDescriptor(returnType, paramTypes);
/*      */ 
/*      */     
/*  608 */     CodeVisitor cv = cw.visitMethod(modifiers, "_bshSuper" + methodName, methodDescriptor, exceptions);
/*      */ 
/*      */     
/*  611 */     cv.visitVarInsn(25, 0);
/*      */     
/*  613 */     int localVarIndex = 1;
/*  614 */     for (int i = 0; i < paramTypes.length; i++) {
/*      */       
/*  616 */       if (isPrimitive(paramTypes[i])) {
/*  617 */         cv.visitVarInsn(21, localVarIndex);
/*      */       } else {
/*  619 */         cv.visitVarInsn(25, localVarIndex);
/*  620 */       }  localVarIndex += (paramTypes[i].equals("D") || paramTypes[i].equals("J")) ? 2 : 1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  625 */     cv.visitMethodInsn(183, superClassName, methodName, methodDescriptor);
/*      */ 
/*      */     
/*  628 */     generatePlainReturnCode(returnType, cv);
/*      */ 
/*      */     
/*  631 */     cv.visitMaxs(20, 20);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   boolean classContainsMethod(Class clas, String methodName, String[] paramTypes) {
/*  637 */     while (clas != null) {
/*      */       
/*  639 */       Method[] methods = clas.getDeclaredMethods();
/*  640 */       for (int i = 0; i < methods.length; i++) {
/*      */         
/*  642 */         if (methods[i].getName().equals(methodName)) {
/*      */           
/*  644 */           String[] methodParamTypes = getTypeDescriptors(methods[i].getParameterTypes());
/*      */           
/*  646 */           boolean found = true;
/*  647 */           for (int j = 0; j < methodParamTypes.length; j++) {
/*      */             
/*  649 */             if (!paramTypes[j].equals(methodParamTypes[j])) {
/*  650 */               found = false;
/*      */               break;
/*      */             } 
/*      */           } 
/*  654 */           if (found) {
/*  655 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*  659 */       clas = clas.getSuperclass();
/*      */     } 
/*      */     
/*  662 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void generatePlainReturnCode(String returnType, CodeVisitor cv) {
/*  670 */     if (returnType.equals("V")) {
/*  671 */       cv.visitInsn(177);
/*      */     }
/*  673 */     else if (isPrimitive(returnType)) {
/*      */       
/*  675 */       int opcode = 172;
/*  676 */       if (returnType.equals("D")) {
/*  677 */         opcode = 175;
/*  678 */       } else if (returnType.equals("F")) {
/*  679 */         opcode = 174;
/*  680 */       } else if (returnType.equals("J")) {
/*  681 */         opcode = 173;
/*      */       } 
/*  683 */       cv.visitInsn(opcode);
/*      */     } else {
/*      */       
/*  686 */       cv.visitTypeInsn(192, descriptorToClassName(returnType));
/*  687 */       cv.visitInsn(176);
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
/*      */   public static void generateParameterReifierCode(String[] paramTypes, boolean isStatic, CodeVisitor cv) {
/*  705 */     cv.visitIntInsn(17, paramTypes.length);
/*  706 */     cv.visitTypeInsn(189, "java/lang/Object");
/*  707 */     int localVarIndex = isStatic ? 0 : 1;
/*  708 */     for (int i = 0; i < paramTypes.length; i++) {
/*      */       
/*  710 */       String param = paramTypes[i];
/*  711 */       cv.visitInsn(89);
/*  712 */       cv.visitIntInsn(17, i);
/*  713 */       if (isPrimitive(param)) {
/*      */         int opcode;
/*      */         
/*  716 */         if (param.equals("F")) {
/*  717 */           opcode = 23;
/*  718 */         } else if (param.equals("D")) {
/*  719 */           opcode = 24;
/*  720 */         } else if (param.equals("J")) {
/*  721 */           opcode = 22;
/*      */         } else {
/*  723 */           opcode = 21;
/*      */         } 
/*      */         
/*  726 */         String type = "bsh/Primitive";
/*  727 */         cv.visitTypeInsn(187, type);
/*  728 */         cv.visitInsn(89);
/*  729 */         cv.visitVarInsn(opcode, localVarIndex);
/*  730 */         String desc = param;
/*  731 */         cv.visitMethodInsn(183, type, "<init>", "(" + desc + ")V");
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/*  739 */         cv.visitVarInsn(25, localVarIndex);
/*      */       } 
/*  741 */       cv.visitInsn(83);
/*  742 */       localVarIndex += (param.equals("D") || param.equals("J")) ? 2 : 1;
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
/*      */   public static void generateReturnCode(String returnType, CodeVisitor cv) {
/*  759 */     if (returnType.equals("V")) {
/*      */       
/*  761 */       cv.visitInsn(87);
/*  762 */       cv.visitInsn(177);
/*      */     }
/*  764 */     else if (isPrimitive(returnType)) {
/*      */       String type, meth;
/*  766 */       int opcode = 172;
/*      */ 
/*      */       
/*  769 */       if (returnType.equals("B")) {
/*  770 */         type = "java/lang/Byte";
/*  771 */         meth = "byteValue";
/*  772 */       } else if (returnType.equals("I")) {
/*  773 */         type = "java/lang/Integer";
/*  774 */         meth = "intValue";
/*  775 */       } else if (returnType.equals("Z")) {
/*  776 */         type = "java/lang/Boolean";
/*  777 */         meth = "booleanValue";
/*  778 */       } else if (returnType.equals("D")) {
/*  779 */         opcode = 175;
/*  780 */         type = "java/lang/Double";
/*  781 */         meth = "doubleValue";
/*  782 */       } else if (returnType.equals("F")) {
/*  783 */         opcode = 174;
/*  784 */         type = "java/lang/Float";
/*  785 */         meth = "floatValue";
/*  786 */       } else if (returnType.equals("J")) {
/*  787 */         opcode = 173;
/*  788 */         type = "java/lang/Long";
/*  789 */         meth = "longValue";
/*  790 */       } else if (returnType.equals("C")) {
/*  791 */         type = "java/lang/Character";
/*  792 */         meth = "charValue";
/*      */       } else {
/*  794 */         type = "java/lang/Short";
/*  795 */         meth = "shortValue";
/*      */       } 
/*      */       
/*  798 */       String desc = returnType;
/*  799 */       cv.visitTypeInsn(192, type);
/*  800 */       cv.visitMethodInsn(182, type, meth, "()" + desc);
/*  801 */       cv.visitInsn(opcode);
/*      */     } else {
/*      */       
/*  804 */       cv.visitTypeInsn(192, descriptorToClassName(returnType));
/*  805 */       cv.visitInsn(176);
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
/*      */   public static ConstructorArgs getConstructorArgs(String superClassName, This classStaticThis, Object[] consArgs, int index) {
/*      */     DelayedEvalBshMethod[] constructors;
/*      */     try {
/*  826 */       constructors = (DelayedEvalBshMethod[])classStaticThis.getNameSpace().getVariable("_bshConstructors");
/*      */     
/*      */     }
/*  829 */     catch (Exception e) {
/*  830 */       throw new InterpreterError("unable to get instance initializer: " + e);
/*      */     } 
/*      */ 
/*      */     
/*  834 */     if (index == -1) {
/*  835 */       return ConstructorArgs.DEFAULT;
/*      */     }
/*  837 */     DelayedEvalBshMethod constructor = constructors[index];
/*      */     
/*  839 */     if (constructor.methodBody.jjtGetNumChildren() == 0) {
/*  840 */       return ConstructorArgs.DEFAULT;
/*      */     }
/*      */     
/*  843 */     String altConstructor = null;
/*  844 */     BSHArguments argsNode = null;
/*  845 */     SimpleNode firstStatement = (SimpleNode)constructor.methodBody.jjtGetChild(0);
/*      */     
/*  847 */     if (firstStatement instanceof BSHPrimaryExpression)
/*  848 */       firstStatement = (SimpleNode)firstStatement.jjtGetChild(0); 
/*  849 */     if (firstStatement instanceof BSHMethodInvocation) {
/*      */       
/*  851 */       BSHMethodInvocation methodNode = (BSHMethodInvocation)firstStatement;
/*      */       
/*  853 */       BSHAmbiguousName methodName = methodNode.getNameNode();
/*  854 */       if (methodName.text.equals("super") || methodName.text.equals("this")) {
/*      */ 
/*      */         
/*  857 */         altConstructor = methodName.text;
/*  858 */         argsNode = methodNode.getArgsNode();
/*      */       } 
/*      */     } 
/*      */     
/*  862 */     if (altConstructor == null) {
/*  863 */       return ConstructorArgs.DEFAULT;
/*      */     }
/*      */ 
/*      */     
/*  867 */     NameSpace consArgsNameSpace = new NameSpace(classStaticThis.getNameSpace(), "consArgs");
/*      */     
/*  869 */     String[] consArgNames = constructor.getParameterNames();
/*  870 */     Class[] consArgTypes = constructor.getParameterTypes();
/*  871 */     for (int i = 0; i < consArgs.length; i++) {
/*      */       
/*      */       try {
/*  874 */         consArgsNameSpace.setTypedVariable(consArgNames[i], consArgTypes[i], consArgs[i], (Modifiers)null);
/*      */       
/*      */       }
/*  877 */       catch (UtilEvalError e) {
/*  878 */         throw new InterpreterError("err setting local cons arg:" + e);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  884 */     CallStack callstack = new CallStack();
/*  885 */     callstack.push(consArgsNameSpace);
/*  886 */     Object[] args = null;
/*  887 */     Interpreter interpreter = classStaticThis.declaringInterpreter;
/*      */     
/*      */     try {
/*  890 */       args = argsNode.getArguments(callstack, interpreter);
/*  891 */     } catch (EvalError e) {
/*  892 */       throw new InterpreterError("Error evaluating constructor args: " + e);
/*      */     } 
/*      */ 
/*      */     
/*  896 */     Class[] argTypes = Types.getTypes(args);
/*  897 */     args = Primitive.unwrap(args);
/*  898 */     Class superClass = interpreter.getClassManager().classForName(superClassName);
/*      */     
/*  900 */     if (superClass == null) {
/*  901 */       throw new InterpreterError("can't find superclass: " + superClassName);
/*      */     }
/*  903 */     Constructor[] superCons = (Constructor[])superClass.getDeclaredConstructors();
/*      */ 
/*      */     
/*  906 */     if (altConstructor.equals("super")) {
/*      */       
/*  908 */       int k = Reflect.findMostSpecificConstructorIndex(argTypes, superCons);
/*      */       
/*  910 */       if (k == -1)
/*  911 */         throw new InterpreterError("can't find constructor for args!"); 
/*  912 */       return new ConstructorArgs(k, args);
/*      */     } 
/*      */ 
/*      */     
/*  916 */     Class[][] candidates = new Class[constructors.length][]; int j;
/*  917 */     for (j = 0; j < candidates.length; j++)
/*  918 */       candidates[j] = constructors[j].getParameterTypes(); 
/*  919 */     j = Reflect.findMostSpecificSignature(argTypes, candidates);
/*  920 */     if (j == -1) {
/*  921 */       throw new InterpreterError("can't find constructor for args 2!");
/*      */     }
/*      */     
/*  924 */     int selector = j + superCons.length;
/*  925 */     int ourSelector = index + superCons.length;
/*      */ 
/*      */     
/*  928 */     if (selector == ourSelector) {
/*  929 */       throw new InterpreterError("Recusive constructor call.");
/*      */     }
/*  931 */     return new ConstructorArgs(selector, args);
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
/*      */   public static void initInstance(Object instance, String className, Object[] args) {
/*      */     Interpreter interpreter;
/*      */     NameSpace instanceNameSpace;
/*  945 */     Class[] sig = Types.getTypes(args);
/*  946 */     CallStack callstack = new CallStack();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     This instanceThis = getClassInstanceThis(instance, className);
/*      */ 
/*      */     
/*  955 */     if (instanceThis == null) {
/*      */       BSHBlock instanceInitBlock;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  961 */       This classStaticThis = getClassStaticThis(instance.getClass(), className);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  977 */       if (classStaticThis == null) {
/*  978 */         throw new InterpreterError("Failed to init class: " + className);
/*      */       }
/*  980 */       interpreter = classStaticThis.declaringInterpreter;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  985 */         instanceInitBlock = (BSHBlock)classStaticThis.getNameSpace().getVariable("_bshInstanceInitializer");
/*      */       }
/*  987 */       catch (Exception e) {
/*  988 */         throw new InterpreterError("unable to get instance initializer: " + e);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  993 */       instanceNameSpace = new NameSpace(classStaticThis.getNameSpace(), className);
/*      */       
/*  995 */       instanceNameSpace.isClass = true;
/*      */ 
/*      */       
/*  998 */       instanceThis = instanceNameSpace.getThis(interpreter);
/*      */       try {
/* 1000 */         LHS lhs = Reflect.getLHSObjectField(instance, "_bshThis" + className);
/*      */         
/* 1002 */         lhs.assign(instanceThis, false);
/* 1003 */       } catch (Exception e) {
/* 1004 */         throw new InterpreterError("Error in class gen setup: " + e);
/*      */       } 
/*      */ 
/*      */       
/* 1008 */       instanceNameSpace.setClassInstance(instance);
/*      */ 
/*      */       
/* 1011 */       callstack.push(instanceNameSpace);
/*      */ 
/*      */       
/*      */       try {
/* 1015 */         instanceInitBlock.evalBlock(callstack, interpreter, true, ClassGeneratorImpl.ClassNodeFilter.CLASSINSTANCE);
/*      */       
/*      */       }
/* 1018 */       catch (Exception e) {
/* 1019 */         throw new InterpreterError("Error in class initialization: " + e);
/*      */       } 
/*      */       
/* 1022 */       callstack.pop();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1028 */       interpreter = instanceThis.declaringInterpreter;
/* 1029 */       instanceNameSpace = instanceThis.getNameSpace();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1034 */     String constructorName = getBaseName(className);
/*      */     
/*      */     try {
/* 1037 */       BshMethod constructor = instanceNameSpace.getMethod(constructorName, sig, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1045 */       if (constructor != null && constructor.getReturnType() != null) {
/* 1046 */         constructor = null;
/*      */       }
/*      */       
/* 1049 */       if (args.length > 0 && constructor == null) {
/* 1050 */         throw new InterpreterError("Can't find constructor: " + className);
/*      */       }
/*      */ 
/*      */       
/* 1054 */       if (constructor != null) {
/* 1055 */         constructor.invoke(args, interpreter, callstack, null, false);
/*      */       }
/* 1057 */     } catch (Exception e) {
/* 1058 */       if (Interpreter.DEBUG)
/* 1059 */         e.printStackTrace(); 
/* 1060 */       if (e instanceof TargetError)
/* 1061 */         e = (Exception)((TargetError)e).getTarget(); 
/* 1062 */       if (e instanceof InvocationTargetException) {
/* 1063 */         e = (Exception)((InvocationTargetException)e).getTargetException();
/*      */       }
/* 1065 */       throw new InterpreterError("Error in class initialization: " + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static This getClassStaticThis(Class clas, String className) {
/*      */     try {
/* 1076 */       return (This)Reflect.getStaticFieldValue(clas, "_bshStatic" + className);
/*      */     }
/* 1078 */     catch (Exception e) {
/* 1079 */       throw new InterpreterError("Unable to get class static space: " + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static This getClassInstanceThis(Object instance, String className) {
/*      */     try {
/* 1091 */       Object o = Reflect.getObjectFieldValue(instance, "_bshThis" + className);
/* 1092 */       return (This)Primitive.unwrap(o);
/* 1093 */     } catch (Exception e) {
/* 1094 */       throw new InterpreterError("Generated class: Error getting This" + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isPrimitive(String typeDescriptor) {
/* 1104 */     return (typeDescriptor.length() == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   static String[] getTypeDescriptors(Class[] cparams) {
/* 1109 */     String[] sa = new String[cparams.length];
/* 1110 */     for (int i = 0; i < sa.length; i++)
/* 1111 */       sa[i] = BSHType.getTypeDescriptor(cparams[i]); 
/* 1112 */     return sa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String descriptorToClassName(String s) {
/* 1122 */     if (s.startsWith("[") || !s.startsWith("L"))
/* 1123 */       return s; 
/* 1124 */     return s.substring(1, s.length() - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getBaseName(String className) {
/* 1129 */     int i = className.indexOf("$");
/* 1130 */     if (i == -1) {
/* 1131 */       return className;
/*      */     }
/* 1133 */     return className.substring(i + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ConstructorArgs
/*      */   {
/* 1145 */     public static ConstructorArgs DEFAULT = new ConstructorArgs();
/*      */     
/* 1147 */     public int selector = -1;
/*      */     Object[] args;
/* 1149 */     int arg = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     ConstructorArgs() {}
/*      */ 
/*      */     
/*      */     ConstructorArgs(int selector, Object[] args) {
/* 1157 */       this.selector = selector;
/* 1158 */       this.args = args;
/*      */     }
/*      */     Object next() {
/* 1161 */       return this.args[this.arg++];
/*      */     }
/* 1163 */     public boolean getBoolean() { return ((Boolean)next()).booleanValue(); }
/* 1164 */     public byte getByte() { return ((Byte)next()).byteValue(); }
/* 1165 */     public char getChar() { return ((Character)next()).charValue(); }
/* 1166 */     public short getShort() { return ((Short)next()).shortValue(); }
/* 1167 */     public int getInt() { return ((Integer)next()).intValue(); }
/* 1168 */     public long getLong() { return ((Long)next()).longValue(); }
/* 1169 */     public double getDouble() { return ((Double)next()).doubleValue(); }
/* 1170 */     public float getFloat() { return ((Float)next()).floatValue(); } public Object getObject() {
/* 1171 */       return next();
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
/*      */   public static void startInterpreterForClass(Class clas) {
/* 1185 */     System.out.println("starting interpreter for class: " + clas);
/* 1186 */     String baseName = Name.suffix(clas.getName(), 1);
/* 1187 */     String resName = baseName + ".bsh";
/* 1188 */     Reader in = new InputStreamReader(clas.getResourceAsStream(resName));
/*      */ 
/*      */     
/* 1191 */     if (in == null) {
/* 1192 */       throw new InterpreterError("Script for BeanShell class file not found: " + resName);
/*      */     }
/*      */     
/* 1195 */     Interpreter bsh = new Interpreter();
/*      */     try {
/* 1197 */       bsh.eval(in, bsh.getNameSpace(), resName);
/* 1198 */     } catch (TargetError e) {
/* 1199 */       System.out.println("Script threw exception: " + e);
/* 1200 */       if (e.inNativeCode())
/* 1201 */         e.printStackTrace(System.err); 
/* 1202 */     } catch (EvalError e) {
/* 1203 */       System.out.println("Evaluation Error: " + e);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ClassGeneratorUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */