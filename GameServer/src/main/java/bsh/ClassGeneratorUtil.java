package bsh;

import bsh.org.objectweb.asm.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassGeneratorUtil
        implements Constants {
    static final String BSHSTATIC = "_bshStatic";
    static final String BSHTHIS = "_bshThis";
    static final String BSHSUPER = "_bshSuper";
    static final String BSHINIT = "_bshInstanceInitializer";
    static final String BSHCONSTRUCTORS = "_bshConstructors";
    static final int DEFAULTCONSTRUCTOR = -1;
    static final String OBJECT = "Ljava/lang/Object;";
    String className;
    String fqClassName;
    Class superClass;
    String superClassName;
    Class[] interfaces;
    Variable[] vars;
    Constructor[] superConstructors;
    DelayedEvalBshMethod[] constructors;
    DelayedEvalBshMethod[] methods;
    NameSpace classStaticNameSpace;
    Modifiers classModifiers;
    boolean isInterface;

    public ClassGeneratorUtil(Modifiers classModifiers, String className, String packageName, Class<Object> superClass,
                              Class[] interfaces, Variable[] vars, DelayedEvalBshMethod[] bshmethods, NameSpace classStaticNameSpace,
                              boolean isInterface) {
        this.classModifiers = classModifiers;
        this.className = className;
        if (packageName != null) {
            this.fqClassName = packageName.replace('.', '/') + "/" + className;
        } else {
            this.fqClassName = className;
        }
        if (superClass == null)
            superClass = Object.class;
        this.superClass = superClass;
        this.superClassName = Type.getInternalName(superClass);
        if (interfaces == null)
            interfaces = new Class[0];
        this.interfaces = interfaces;
        this.vars = vars;
        this.classStaticNameSpace = classStaticNameSpace;
        this.superConstructors = (Constructor[]) superClass.getDeclaredConstructors();

        List<BshMethod> consl = new ArrayList();
        List<BshMethod> methodsl = new ArrayList();
        String classBaseName = getBaseName(className);
        for (int i = 0; i < bshmethods.length; i++) {

            BshMethod bm = bshmethods[i];

            if (bm.getName().equals(classBaseName) && bm.getReturnType() == null) {

                consl.add(bm);
            } else {
                methodsl.add(bm);
            }
        }
        this.constructors = consl.<DelayedEvalBshMethod>toArray(new DelayedEvalBshMethod[0]);

        this.methods = methodsl.<DelayedEvalBshMethod>toArray(new DelayedEvalBshMethod[0]);

        try {
            classStaticNameSpace.setLocalVariable("_bshConstructors", this.constructors, false);
        } catch (UtilEvalError e) {
            throw new InterpreterError("can't set cons var");
        }

        this.isInterface = isInterface;
    }

    static int getASMModifiers(Modifiers modifiers) {
        int mods = 0;
        if (modifiers == null) {
            return mods;
        }
        if (modifiers.hasModifier("public"))
            mods++;
        if (modifiers.hasModifier("protected"))
            mods += 4;
        if (modifiers.hasModifier("static"))
            mods += 8;
        if (modifiers.hasModifier("synchronized"))
            mods += 32;
        if (modifiers.hasModifier("abstract")) {
            mods += 1024;
        }
        return mods;
    }

    static void generateField(String fieldName, String type, int modifiers, ClassWriter cw) {
        cw.visitField(modifiers, fieldName, type, null);
    }

    static void generateMethod(String className, String fqClassName, String methodName, String returnType,
                               String[] paramTypes, int modifiers, ClassWriter cw) {
        String[] exceptions = null;
        boolean isStatic = ((modifiers & 0x8) != 0);

        if (returnType == null) {
            returnType = "Ljava/lang/Object;";
        }
        String methodDescriptor = getMethodDescriptor(returnType, paramTypes);

        CodeVisitor cv = cw.visitMethod(modifiers, methodName, methodDescriptor, exceptions);

        if ((modifiers & 0x400) != 0) {
            return;
        }

        if (isStatic) {

            cv.visitFieldInsn(178, fqClassName, "_bshStatic" + className, "Lbsh/This;");

        } else {

            cv.visitVarInsn(25, 0);

            cv.visitFieldInsn(180, fqClassName, "_bshThis" + className, "Lbsh/This;");
        }

        cv.visitLdcInsn(methodName);

        generateParameterReifierCode(paramTypes, isStatic, cv);

        cv.visitInsn(1);
        cv.visitInsn(1);
        cv.visitInsn(1);

        cv.visitInsn(4);

        cv.visitMethodInsn(182, "bsh/This", "invokeMethod",
                Type.getMethodDescriptor(Type.getType(Object.class),
                        new Type[]{Type.getType(String.class), Type.getType(Object[].class),
                                Type.getType(Interpreter.class), Type.getType(CallStack.class),
                                Type.getType(SimpleNode.class), Type.getType(boolean.class)}));

        cv.visitMethodInsn(184, "bsh/Primitive", "unwrap", "(Ljava/lang/Object;)Ljava/lang/Object;");

        generateReturnCode(returnType, cv);

        cv.visitMaxs(0, 0);
    }

    static void doSwitchBranch(int index, String targetClassName, String[] paramTypes, Label endLabel, Label[] labels,
                               int consArgsVar, CodeVisitor cv) {
        cv.visitLabel(labels[index]);

        cv.visitVarInsn(25, 0);

        for (int i = 0; i < paramTypes.length; i++) {

            String retType, type = paramTypes[i];
            String method = null;
            if (type.equals("Z")) {
                method = "getBoolean";
            } else if (type.equals("B")) {
                method = "getByte";
            } else if (type.equals("C")) {
                method = "getChar";
            } else if (type.equals("S")) {
                method = "getShort";
            } else if (type.equals("I")) {
                method = "getInt";
            } else if (type.equals("J")) {
                method = "getLong";
            } else if (type.equals("D")) {
                method = "getDouble";
            } else if (type.equals("F")) {
                method = "getFloat";
            } else {
                method = "getObject";
            }

            cv.visitVarInsn(25, consArgsVar);
            String className = "bsh/ClassGeneratorUtil$ConstructorArgs";

            if (method.equals("getObject")) {
                retType = "Ljava/lang/Object;";
            } else {
                retType = type;
            }
            cv.visitMethodInsn(182, className, method, "()" + retType);

            if (method.equals("getObject")) {
                cv.visitTypeInsn(192, descriptorToClassName(type));
            }
        }

        String descriptor = getMethodDescriptor("V", paramTypes);
        cv.visitMethodInsn(183, targetClassName, "<init>", descriptor);

        cv.visitJumpInsn(167, endLabel);
    }

    static String getMethodDescriptor(String returnType, String[] paramTypes) {
        StringBuffer sb = new StringBuffer("(");
        for (int i = 0; i < paramTypes.length; i++)
            sb.append(paramTypes[i]);
        sb.append(")" + returnType);
        return sb.toString();
    }

    static void generateSuperDelegateMethod(String superClassName, String methodName, String returnType,
                                            String[] paramTypes, int modifiers, ClassWriter cw) {
        String[] exceptions = null;

        if (returnType == null) {
            returnType = "Ljava/lang/Object;";
        }
        String methodDescriptor = getMethodDescriptor(returnType, paramTypes);

        CodeVisitor cv = cw.visitMethod(modifiers, "_bshSuper" + methodName, methodDescriptor, exceptions);

        cv.visitVarInsn(25, 0);

        int localVarIndex = 1;
        for (int i = 0; i < paramTypes.length; i++) {

            if (isPrimitive(paramTypes[i])) {
                cv.visitVarInsn(21, localVarIndex);
            } else {
                cv.visitVarInsn(25, localVarIndex);
            }
            localVarIndex += (paramTypes[i].equals("D") || paramTypes[i].equals("J")) ? 2 : 1;
        }

        cv.visitMethodInsn(183, superClassName, methodName, methodDescriptor);

        generatePlainReturnCode(returnType, cv);

        cv.visitMaxs(20, 20);
    }

    static void generatePlainReturnCode(String returnType, CodeVisitor cv) {
        if (returnType.equals("V")) {
            cv.visitInsn(177);
        } else if (isPrimitive(returnType)) {

            int opcode = 172;
            if (returnType.equals("D")) {
                opcode = 175;
            } else if (returnType.equals("F")) {
                opcode = 174;
            } else if (returnType.equals("J")) {
                opcode = 173;
            }
            cv.visitInsn(opcode);
        } else {

            cv.visitTypeInsn(192, descriptorToClassName(returnType));
            cv.visitInsn(176);
        }
    }

    public static void generateParameterReifierCode(String[] paramTypes, boolean isStatic, CodeVisitor cv) {
        cv.visitIntInsn(17, paramTypes.length);
        cv.visitTypeInsn(189, "java/lang/Object");
        int localVarIndex = isStatic ? 0 : 1;
        for (int i = 0; i < paramTypes.length; i++) {

            String param = paramTypes[i];
            cv.visitInsn(89);
            cv.visitIntInsn(17, i);
            if (isPrimitive(param)) {
                int opcode;

                if (param.equals("F")) {
                    opcode = 23;
                } else if (param.equals("D")) {
                    opcode = 24;
                } else if (param.equals("J")) {
                    opcode = 22;
                } else {
                    opcode = 21;
                }

                String type = "bsh/Primitive";
                cv.visitTypeInsn(187, type);
                cv.visitInsn(89);
                cv.visitVarInsn(opcode, localVarIndex);
                String desc = param;
                cv.visitMethodInsn(183, type, "<init>", "(" + desc + ")V");

            } else {

                cv.visitVarInsn(25, localVarIndex);
            }
            cv.visitInsn(83);
            localVarIndex += (param.equals("D") || param.equals("J")) ? 2 : 1;
        }
    }

    public static void generateReturnCode(String returnType, CodeVisitor cv) {
        if (returnType.equals("V")) {

            cv.visitInsn(87);
            cv.visitInsn(177);
        } else if (isPrimitive(returnType)) {
            String type, meth;
            int opcode = 172;

            if (returnType.equals("B")) {
                type = "java/lang/Byte";
                meth = "byteValue";
            } else if (returnType.equals("I")) {
                type = "java/lang/Integer";
                meth = "intValue";
            } else if (returnType.equals("Z")) {
                type = "java/lang/Boolean";
                meth = "booleanValue";
            } else if (returnType.equals("D")) {
                opcode = 175;
                type = "java/lang/Double";
                meth = "doubleValue";
            } else if (returnType.equals("F")) {
                opcode = 174;
                type = "java/lang/Float";
                meth = "floatValue";
            } else if (returnType.equals("J")) {
                opcode = 173;
                type = "java/lang/Long";
                meth = "longValue";
            } else if (returnType.equals("C")) {
                type = "java/lang/Character";
                meth = "charValue";
            } else {
                type = "java/lang/Short";
                meth = "shortValue";
            }

            String desc = returnType;
            cv.visitTypeInsn(192, type);
            cv.visitMethodInsn(182, type, meth, "()" + desc);
            cv.visitInsn(opcode);
        } else {

            cv.visitTypeInsn(192, descriptorToClassName(returnType));
            cv.visitInsn(176);
        }
    }

    public static ConstructorArgs getConstructorArgs(String superClassName, This classStaticThis, Object[] consArgs,
                                                     int index) {
        DelayedEvalBshMethod[] constructors;
        try {
            constructors = (DelayedEvalBshMethod[]) classStaticThis.getNameSpace().getVariable("_bshConstructors");

        } catch (Exception e) {
            throw new InterpreterError("unable to get instance initializer: " + e);
        }

        if (index == -1) {
            return ConstructorArgs.DEFAULT;
        }
        DelayedEvalBshMethod constructor = constructors[index];

        if (constructor.methodBody.jjtGetNumChildren() == 0) {
            return ConstructorArgs.DEFAULT;
        }

        String altConstructor = null;
        BSHArguments argsNode = null;
        SimpleNode firstStatement = (SimpleNode) constructor.methodBody.jjtGetChild(0);

        if (firstStatement instanceof BSHPrimaryExpression)
            firstStatement = (SimpleNode) firstStatement.jjtGetChild(0);
        if (firstStatement instanceof BSHMethodInvocation) {

            BSHMethodInvocation methodNode = (BSHMethodInvocation) firstStatement;

            BSHAmbiguousName methodName = methodNode.getNameNode();
            if (methodName.text.equals("super") || methodName.text.equals("this")) {

                altConstructor = methodName.text;
                argsNode = methodNode.getArgsNode();
            }
        }

        if (altConstructor == null) {
            return ConstructorArgs.DEFAULT;
        }

        NameSpace consArgsNameSpace = new NameSpace(classStaticThis.getNameSpace(), "consArgs");

        String[] consArgNames = constructor.getParameterNames();
        Class[] consArgTypes = constructor.getParameterTypes();
        for (int i = 0; i < consArgs.length; i++) {

            try {
                consArgsNameSpace.setTypedVariable(consArgNames[i], consArgTypes[i], consArgs[i], (Modifiers) null);

            } catch (UtilEvalError e) {
                throw new InterpreterError("err setting local cons arg:" + e);
            }
        }

        CallStack callstack = new CallStack();
        callstack.push(consArgsNameSpace);
        Object[] args = null;
        Interpreter interpreter = classStaticThis.declaringInterpreter;

        try {
            args = argsNode.getArguments(callstack, interpreter);
        } catch (EvalError e) {
            throw new InterpreterError("Error evaluating constructor args: " + e);
        }

        Class[] argTypes = Types.getTypes(args);
        args = Primitive.unwrap(args);
        Class superClass = interpreter.getClassManager().classForName(superClassName);

        if (superClass == null) {
            throw new InterpreterError("can't find superclass: " + superClassName);
        }
        Constructor[] superCons = (Constructor[]) superClass.getDeclaredConstructors();

        if (altConstructor.equals("super")) {

            int k = Reflect.findMostSpecificConstructorIndex(argTypes, superCons);

            if (k == -1)
                throw new InterpreterError("can't find constructor for args!");
            return new ConstructorArgs(k, args);
        }

        Class[][] candidates = new Class[constructors.length][];
        int j;
        for (j = 0; j < candidates.length; j++)
            candidates[j] = constructors[j].getParameterTypes();
        j = Reflect.findMostSpecificSignature(argTypes, candidates);
        if (j == -1) {
            throw new InterpreterError("can't find constructor for args 2!");
        }

        int selector = j + superCons.length;
        int ourSelector = index + superCons.length;

        if (selector == ourSelector) {
            throw new InterpreterError("Recusive constructor call.");
        }
        return new ConstructorArgs(selector, args);
    }

    public static void initInstance(Object instance, String className, Object[] args) {
        Interpreter interpreter;
        NameSpace instanceNameSpace;
        Class[] sig = Types.getTypes(args);
        CallStack callstack = new CallStack();

        This instanceThis = getClassInstanceThis(instance, className);

        if (instanceThis == null) {
            BSHBlock instanceInitBlock;

            This classStaticThis = getClassStaticThis(instance.getClass(), className);

            if (classStaticThis == null) {
                throw new InterpreterError("Failed to init class: " + className);
            }
            interpreter = classStaticThis.declaringInterpreter;

            try {
                instanceInitBlock = (BSHBlock) classStaticThis.getNameSpace().getVariable("_bshInstanceInitializer");
            } catch (Exception e) {
                throw new InterpreterError("unable to get instance initializer: " + e);
            }

            instanceNameSpace = new NameSpace(classStaticThis.getNameSpace(), className);

            instanceNameSpace.isClass = true;

            instanceThis = instanceNameSpace.getThis(interpreter);
            try {
                LHS lhs = Reflect.getLHSObjectField(instance, "_bshThis" + className);

                lhs.assign(instanceThis, false);
            } catch (Exception e) {
                throw new InterpreterError("Error in class gen setup: " + e);
            }

            instanceNameSpace.setClassInstance(instance);

            callstack.push(instanceNameSpace);

            try {
                instanceInitBlock.evalBlock(callstack, interpreter, true,
                        ClassGeneratorImpl.ClassNodeFilter.CLASSINSTANCE);

            } catch (Exception e) {
                throw new InterpreterError("Error in class initialization: " + e);
            }

            callstack.pop();

        } else {

            interpreter = instanceThis.declaringInterpreter;
            instanceNameSpace = instanceThis.getNameSpace();
        }

        String constructorName = getBaseName(className);

        try {
            BshMethod constructor = instanceNameSpace.getMethod(constructorName, sig, true);

            if (constructor != null && constructor.getReturnType() != null) {
                constructor = null;
            }

            if (args.length > 0 && constructor == null) {
                throw new InterpreterError("Can't find constructor: " + className);
            }

            if (constructor != null) {
                constructor.invoke(args, interpreter, callstack, null, false);
            }
        } catch (Exception e) {
            if (Interpreter.DEBUG)
                e.printStackTrace();
            if (e instanceof TargetError)
                e = (Exception) ((TargetError) e).getTarget();
            if (e instanceof InvocationTargetException) {
                e = (Exception) ((InvocationTargetException) e).getTargetException();
            }
            throw new InterpreterError("Error in class initialization: " + e);
        }
    }

    static This getClassStaticThis(Class clas, String className) {
        try {
            return (This) Reflect.getStaticFieldValue(clas, "_bshStatic" + className);
        } catch (Exception e) {
            throw new InterpreterError("Unable to get class static space: " + e);
        }
    }

    static This getClassInstanceThis(Object instance, String className) {
        try {
            Object o = Reflect.getObjectFieldValue(instance, "_bshThis" + className);
            return (This) Primitive.unwrap(o);
        } catch (Exception e) {
            throw new InterpreterError("Generated class: Error getting This" + e);
        }
    }

    private static boolean isPrimitive(String typeDescriptor) {
        return (typeDescriptor.length() == 1);
    }

    static String[] getTypeDescriptors(Class[] cparams) {
        String[] sa = new String[cparams.length];
        for (int i = 0; i < sa.length; i++)
            sa[i] = BSHType.getTypeDescriptor(cparams[i]);
        return sa;
    }

    private static String descriptorToClassName(String s) {
        if (s.startsWith("[") || !s.startsWith("L"))
            return s;
        return s.substring(1, s.length() - 1);
    }

    private static String getBaseName(String className) {
        int i = className.indexOf("$");
        if (i == -1) {
            return className;
        }
        return className.substring(i + 1);
    }

    public static void startInterpreterForClass(Class clas) {
        System.out.println("starting interpreter for class: " + clas);
        String baseName = Name.suffix(clas.getName(), 1);
        String resName = baseName + ".bsh";
        Reader in = new InputStreamReader(clas.getResourceAsStream(resName));

        if (in == null) {
            throw new InterpreterError("Script for BeanShell class file not found: " + resName);
        }

        Interpreter bsh = new Interpreter();
        try {
            bsh.eval(in, bsh.getNameSpace(), resName);
        } catch (TargetError e) {
            System.out.println("Script threw exception: " + e);
            if (e.inNativeCode())
                e.printStackTrace(System.err);
        } catch (EvalError e) {
            System.out.println("Evaluation Error: " + e);
        }
    }

    public byte[] generateClass() {
        int classMods = getASMModifiers(this.classModifiers) | 0x1;
        if (this.isInterface) {
            classMods |= 0x200;
        }
        String[] interfaceNames = new String[this.interfaces.length];
        for (int i = 0; i < this.interfaces.length; i++) {
            interfaceNames[i] = Type.getInternalName(this.interfaces[i]);
        }
        String sourceFile = "BeanShell Generated via ASM (www.objectweb.org)";
        ClassWriter cw = new ClassWriter(true);
        cw.visit(classMods, this.fqClassName, this.superClassName, interfaceNames, sourceFile);

        if (!this.isInterface) {

            generateField("_bshThis" + this.className, "Lbsh/This;", 1, cw);

            generateField("_bshStatic" + this.className, "Lbsh/This;", 9, cw);
        }

        for (int j = 0; j < this.vars.length; j++) {

            String type = this.vars[j].getTypeDescriptor();

            if (!this.vars[j].hasModifier("private") && type != null) {
                int modifiers;

                if (this.isInterface) {
                    modifiers = 25;
                } else {
                    modifiers = getASMModifiers(this.vars[j].getModifiers());
                }
                generateField(this.vars[j].getName(), type, modifiers, cw);
            }
        }

        boolean hasConstructor = false;
        int k;
        for (k = 0; k < this.constructors.length; k++) {

            if (!this.constructors[k].hasModifier("private")) {

                int modifiers = getASMModifiers(this.constructors[k].getModifiers());
                generateConstructor(k, this.constructors[k].getParamTypeDescriptors(), modifiers, cw);

                hasConstructor = true;
            }
        }

        if (!this.isInterface && !hasConstructor) {
            generateConstructor(-1, new String[0], 1, cw);
        }

        for (k = 0; k < this.methods.length; k++) {

            String returnType = this.methods[k].getReturnTypeDescriptor();

            if (!this.methods[k].hasModifier("private")) {

                int modifiers = getASMModifiers(this.methods[k].getModifiers());
                if (this.isInterface) {
                    modifiers |= 0x401;
                }
                generateMethod(this.className, this.fqClassName, this.methods[k].getName(), returnType,
                        this.methods[k].getParamTypeDescriptors(), modifiers, cw);

                boolean isStatic = ((modifiers & 0x8) > 0);
                boolean overridden = classContainsMethod(this.superClass, this.methods[k].getName(),
                        this.methods[k].getParamTypeDescriptors());

                if (!isStatic && overridden) {
                    generateSuperDelegateMethod(this.superClassName, this.methods[k].getName(), returnType,
                            this.methods[k].getParamTypeDescriptors(), modifiers, cw);
                }
            }
        }

        return cw.toByteArray();
    }

    void generateConstructor(int index, String[] paramTypes, int modifiers, ClassWriter cw) {
        int argsVar = paramTypes.length + 1;

        int consArgsVar = paramTypes.length + 2;

        String[] exceptions = null;
        String methodDescriptor = getMethodDescriptor("V", paramTypes);

        CodeVisitor cv = cw.visitMethod(modifiers, "<init>", methodDescriptor, exceptions);

        generateParameterReifierCode(paramTypes, false, cv);
        cv.visitVarInsn(58, argsVar);

        generateConstructorSwitch(index, argsVar, consArgsVar, cv);

        cv.visitVarInsn(25, 0);

        cv.visitLdcInsn(this.className);

        cv.visitVarInsn(25, argsVar);

        cv.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "initInstance",
                "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)V");

        cv.visitInsn(177);

        cv.visitMaxs(0, 0);
    }

    void generateConstructorSwitch(int consIndex, int argsVar, int consArgsVar, CodeVisitor cv) {
        Label defaultLabel = new Label();
        Label endLabel = new Label();
        int cases = this.superConstructors.length + this.constructors.length;

        Label[] labels = new Label[cases];
        for (int i = 0; i < cases; i++) {
            labels[i] = new Label();
        }

        cv.visitLdcInsn(this.superClass.getName());

        cv.visitFieldInsn(178, this.fqClassName, "_bshStatic" + this.className, "Lbsh/This;");

        cv.visitVarInsn(25, argsVar);

        cv.visitIntInsn(16, consIndex);

        cv.visitMethodInsn(184, "bsh/ClassGeneratorUtil", "getConstructorArgs",
                "(Ljava/lang/String;Lbsh/This;[Ljava/lang/Object;I)Lbsh/ClassGeneratorUtil$ConstructorArgs;");

        cv.visitVarInsn(58, consArgsVar);

        cv.visitVarInsn(25, consArgsVar);
        cv.visitFieldInsn(180, "bsh/ClassGeneratorUtil$ConstructorArgs", "selector", "I");

        cv.visitTableSwitchInsn(0, cases - 1, defaultLabel, labels);

        int index = 0;
        int j;
        for (j = 0; j < this.superConstructors.length; j++, index++) {
            doSwitchBranch(index, this.superClassName,
                    getTypeDescriptors(this.superConstructors[j].getParameterTypes()), endLabel, labels, consArgsVar,
                    cv);
        }

        for (j = 0; j < this.constructors.length; j++, index++) {
            doSwitchBranch(index, this.fqClassName, this.constructors[j].getParamTypeDescriptors(), endLabel, labels,
                    consArgsVar, cv);
        }

        cv.visitLabel(defaultLabel);

        cv.visitVarInsn(25, 0);
        cv.visitMethodInsn(183, this.superClassName, "<init>", "()V");

        cv.visitLabel(endLabel);
    }

    boolean classContainsMethod(Class clas, String methodName, String[] paramTypes) {
        while (clas != null) {

            Method[] methods = clas.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {

                if (methods[i].getName().equals(methodName)) {

                    String[] methodParamTypes = getTypeDescriptors(methods[i].getParameterTypes());

                    boolean found = true;
                    for (int j = 0; j < methodParamTypes.length; j++) {

                        if (!paramTypes[j].equals(methodParamTypes[j])) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        return true;
                    }
                }
            }
            clas = clas.getSuperclass();
        }

        return false;
    }

    public static class ConstructorArgs {
        public static ConstructorArgs DEFAULT = new ConstructorArgs();

        public int selector = -1;
        Object[] args;
        int arg = 0;

        ConstructorArgs() {
        }

        ConstructorArgs(int selector, Object[] args) {
            this.selector = selector;
            this.args = args;
        }

        Object next() {
            return this.args[this.arg++];
        }

        public boolean getBoolean() {
            return ((Boolean) next()).booleanValue();
        }

        public byte getByte() {
            return ((Byte) next()).byteValue();
        }

        public char getChar() {
            return ((Character) next()).charValue();
        }

        public short getShort() {
            return ((Short) next()).shortValue();
        }

        public int getInt() {
            return ((Integer) next()).intValue();
        }

        public long getLong() {
            return ((Long) next()).longValue();
        }

        public double getDouble() {
            return ((Double) next()).doubleValue();
        }

        public float getFloat() {
            return ((Float) next()).floatValue();
        }

        public Object getObject() {
            return next();
        }
    }
}
