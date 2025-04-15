package bsh;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NameSpace
        implements Serializable, BshClassManager.Listener, NameSource {
    public static final NameSpace JAVACODE = new NameSpace((BshClassManager) null, "Called from compiled Java code.");

    static {
        JAVACODE.isMethod = true;
    }

    protected Hashtable importedClasses;
    SimpleNode callerInfoNode;
    boolean isMethod;
    boolean isClass;
    Class classStatic;
    Object classInstance;
    Vector nameSourceListeners;
    private String nsName;
    private NameSpace parent;
    private Hashtable variables;
    private Hashtable methods;
    private Vector importedPackages;
    private Vector importedCommands;
    private Vector importedObjects;
    private Vector importedStatic;
    private String packageName;
    private transient BshClassManager classManager;
    private This thisReference;
    private Hashtable names;
    private transient Hashtable classCache;

    public NameSpace(NameSpace parent, String name) {
        this(parent, null, name);
    }

    public NameSpace(BshClassManager classManager, String name) {
        this(null, classManager, name);
    }

    public NameSpace(NameSpace parent, BshClassManager classManager, String name) {
        setName(name);
        setParent(parent);
        setClassManager(classManager);

        if (classManager != null) {
            classManager.addListener(this);
        }
    }

    public static Class identifierToClass(ClassIdentifier ci) {
        return ci.getTargetClass();
    }

    void setClassStatic(Class clas) {
        this.classStatic = clas;
        importStatic(clas);
    }

    Object getClassInstance() throws UtilEvalError {
        if (this.classInstance != null) {
            return this.classInstance;
        }
        if (this.classStatic != null) {

            throw new UtilEvalError("Can't refer to class instance from static context.");
        }

        throw new InterpreterError("Can't resolve class instance 'this' in: " + this);
    }

    void setClassInstance(Object instance) {
        this.classInstance = instance;
        importObject(instance);
    }

    public String getName() {
        return this.nsName;
    }

    public void setName(String name) {
        this.nsName = name;
    }

    SimpleNode getNode() {
        if (this.callerInfoNode != null)
            return this.callerInfoNode;
        if (this.parent != null) {
            return this.parent.getNode();
        }
        return null;
    }

    void setNode(SimpleNode node) {
        this.callerInfoNode = node;
    }

    public Object get(String name, Interpreter interpreter) throws UtilEvalError {
        CallStack callstack = new CallStack(this);
        return getNameResolver(name).toObject(callstack, interpreter);
    }

    public void setVariable(String name, Object value, boolean strictJava) throws UtilEvalError {
        boolean recurse = Interpreter.LOCALSCOPING ? strictJava : true;
        setVariable(name, value, strictJava, recurse);
    }

    void setLocalVariable(String name, Object value, boolean strictJava) throws UtilEvalError {
        setVariable(name, value, strictJava, false);
    }

    void setVariable(String name, Object value, boolean strictJava, boolean recurse) throws UtilEvalError {
        if (this.variables == null) {
            this.variables = new Hashtable<Object, Object>();
        }

        if (value == null) {
            throw new InterpreterError("null variable value");
        }

        Variable existing = getVariableImpl(name, recurse);

        if (existing != null) {

            try {
                existing.setValue(value, 1);
            } catch (UtilEvalError e) {
                throw new UtilEvalError("Variable assignment: " + name + ": " + e.getMessage());
            }

        } else {

            if (strictJava) {
                throw new UtilEvalError("(Strict Java mode) Assignment to undeclared variable: " + name);
            }

            NameSpace varScope = this;

            varScope.variables.put(name, createVariable(name, value, (Modifiers) null));

            nameSpaceChanged();
        }
    }

    protected Variable createVariable(String name, Object value, Modifiers mods) throws UtilEvalError {
        return createVariable(name, null, value, mods);
    }

    protected Variable createVariable(String name, Class type, Object value, Modifiers mods) throws UtilEvalError {
        return new Variable(name, type, value, mods);
    }

    protected Variable createVariable(String name, Class type, LHS lhs) throws UtilEvalError {
        return new Variable(name, type, lhs);
    }

    public void unsetVariable(String name) {
        if (this.variables != null) {

            this.variables.remove(name);
            nameSpaceChanged();
        }
    }

    public String[] getVariableNames() {
        if (this.variables == null) {
            return new String[0];
        }
        return enumerationToStringArray(this.variables.keys());
    }

    public String[] getMethodNames() {
        if (this.methods == null) {
            return new String[0];
        }
        return enumerationToStringArray(this.methods.keys());
    }

    public BshMethod[] getMethods() {
        if (this.methods == null) {
            return new BshMethod[0];
        }
        return flattenMethodCollection(this.methods.elements());
    }

    private String[] enumerationToStringArray(Enumeration e) {
        Vector v = new Vector();
        while (e.hasMoreElements())
            v.addElement(e.nextElement());
        String[] sa = new String[v.size()];
        v.copyInto((Object[]) sa);
        return sa;
    }

    private BshMethod[] flattenMethodCollection(Enumeration e) {
        Vector<Object> v = new Vector();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof BshMethod) {
                v.addElement(o);
                continue;
            }
            Vector ov = (Vector) o;
            for (int i = 0; i < ov.size(); i++) {
                v.addElement(ov.elementAt(i));
            }
        }
        BshMethod[] bma = new BshMethod[v.size()];
        v.copyInto((Object[]) bma);
        return bma;
    }

    public NameSpace getParent() {
        return this.parent;
    }

    public void setParent(NameSpace parent) {
        this.parent = parent;

        if (parent == null) {
            loadDefaultImports();
        }
    }

    public This getSuper(Interpreter declaringInterpreter) {
        if (this.parent != null) {
            return this.parent.getThis(declaringInterpreter);
        }
        return getThis(declaringInterpreter);
    }

    public This getGlobal(Interpreter declaringInterpreter) {
        if (this.parent != null) {
            return this.parent.getGlobal(declaringInterpreter);
        }
        return getThis(declaringInterpreter);
    }

    public This getThis(Interpreter declaringInterpreter) {
        if (this.thisReference == null) {
            this.thisReference = This.getThis(this, declaringInterpreter);
        }
        return this.thisReference;
    }

    public BshClassManager getClassManager() {
        if (this.classManager != null)
            return this.classManager;
        if (this.parent != null && this.parent != JAVACODE) {
            return this.parent.getClassManager();
        }

        this.classManager = BshClassManager.createClassManager(null);

        return this.classManager;
    }

    void setClassManager(BshClassManager classManager) {
        this.classManager = classManager;
    }

    public void prune() {
        if (this.classManager == null) {

            setClassManager(BshClassManager.createClassManager(null));
        }

        setParent(null);
    }

    public Object getVariable(String name) throws UtilEvalError {
        return getVariable(name, true);
    }

    public Object getVariable(String name, boolean recurse) throws UtilEvalError {
        Variable var = getVariableImpl(name, recurse);
        return unwrapVariable(var);
    }

    protected Variable getVariableImpl(String name, boolean recurse) throws UtilEvalError {
        Variable var = null;

        if (var == null && this.isClass) {
            var = getImportedVar(name);
        }
        if (var == null && this.variables != null) {
            var = (Variable) this.variables.get(name);
        }

        if (var == null && !this.isClass) {
            var = getImportedVar(name);
        }

        if (recurse && var == null && this.parent != null) {
            var = this.parent.getVariableImpl(name, recurse);
        }
        return var;
    }

    public Variable[] getDeclaredVariables() {
        if (this.variables == null)
            return new Variable[0];
        Variable[] vars = new Variable[this.variables.size()];
        int i = 0;
        for (Enumeration<Variable> e = this.variables.elements(); e.hasMoreElements(); )
            vars[i++] = e.nextElement();
        return vars;
    }

    protected Object unwrapVariable(Variable var) throws UtilEvalError {
        return (var == null) ? Primitive.VOID : var.getValue();
    }

    public void setTypedVariable(String name, Class type, Object value, boolean isFinal) throws UtilEvalError {
        Modifiers modifiers = new Modifiers();
        if (isFinal)
            modifiers.addModifier(2, "final");
        setTypedVariable(name, type, value, modifiers);
    }

    public void setTypedVariable(String name, Class type, Object value, Modifiers modifiers) throws UtilEvalError {
        if (this.variables == null) {
            this.variables = new Hashtable<Object, Object>();
        }

        Variable existing = getVariableImpl(name, false);

        if (existing != null) {

            if (existing.getType() != null) {

                if (existing.getType() != type) {
                    throw new UtilEvalError(
                            "Typed variable: " + name + " was previously declared with type: " + existing.getType());
                }

                existing.setValue(value, 0);

                return;
            }
        }

        this.variables.put(name, createVariable(name, type, value, modifiers));
    }

    public void setMethod(String name, BshMethod method) throws UtilEvalError {
        if (this.methods == null) {
            this.methods = new Hashtable<Object, Object>();
        }
        Object m = this.methods.get(name);

        if (m == null) {
            this.methods.put(name, method);
        } else if (m instanceof BshMethod) {
            Vector<Object> v = new Vector();
            v.addElement(m);
            v.addElement(method);
            this.methods.put(name, v);
        } else {
            ((Vector<BshMethod>) m).addElement(method);
        }
    }

    public BshMethod getMethod(String name, Class[] sig) throws UtilEvalError {
        return getMethod(name, sig, false);
    }

    public BshMethod getMethod(String name, Class[] sig, boolean declaredOnly) throws UtilEvalError {
        BshMethod method = null;

        if (method == null && this.isClass && !declaredOnly) {
            method = getImportedMethod(name, sig);
        }
        Object m = null;
        if (method == null && this.methods != null) {

            m = this.methods.get(name);

            if (m != null) {
                BshMethod[] ma;

                if (m instanceof Vector) {

                    Vector vm = (Vector) m;
                    ma = new BshMethod[vm.size()];
                    vm.copyInto((Object[]) ma);
                } else {
                    ma = new BshMethod[]{(BshMethod) m};
                }

                Class[][] candidates = new Class[ma.length][];
                for (int i = 0; i < ma.length; i++) {
                    candidates[i] = ma[i].getParameterTypes();
                }
                int match = Reflect.findMostSpecificSignature(sig, candidates);

                if (match != -1) {
                    method = ma[match];
                }
            }
        }
        if (method == null && !this.isClass && !declaredOnly) {
            method = getImportedMethod(name, sig);
        }

        if (!declaredOnly && method == null && this.parent != null) {
            return this.parent.getMethod(name, sig);
        }
        return method;
    }

    public void importClass(String name) {
        if (this.importedClasses == null) {
            this.importedClasses = new Hashtable<Object, Object>();
        }
        this.importedClasses.put(Name.suffix(name, 1), name);
        nameSpaceChanged();
    }

    public void importPackage(String name) {
        if (this.importedPackages == null) {
            this.importedPackages = new Vector();
        }

        if (this.importedPackages.contains(name)) {
            this.importedPackages.remove(name);
        }
        this.importedPackages.addElement(name);
        nameSpaceChanged();
    }

    public void importCommands(String name) {
        if (this.importedCommands == null) {
            this.importedCommands = new Vector();
        }

        name = name.replace('.', '/');

        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        if (name.length() > 1 && name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }

        if (this.importedCommands.contains(name)) {
            this.importedCommands.remove(name);
        }
        this.importedCommands.addElement(name);
        nameSpaceChanged();
    }

    public Object getCommand(String name, Class[] argTypes, Interpreter interpreter) throws UtilEvalError {
        if (Interpreter.DEBUG)
            Interpreter.debug("getCommand: " + name);
        BshClassManager bcm = interpreter.getClassManager();

        if (this.importedCommands != null) {

            for (int i = this.importedCommands.size() - 1; i >= 0; i--) {

                String scriptPath, className, path = this.importedCommands.elementAt(i).toString();

                if (path.equals("/")) {
                    scriptPath = path + name + ".bsh";
                } else {
                    scriptPath = path + "/" + name + ".bsh";
                }
                Interpreter.debug("searching for script: " + scriptPath);

                InputStream in = bcm.getResourceAsStream(scriptPath);

                if (in != null) {
                    return loadScriptedCommand(in, name, argTypes, scriptPath, interpreter);
                }

                if (path.equals("/")) {
                    className = name;
                } else {
                    className = path.substring(1).replace('/', '.') + "." + name;
                }
                Interpreter.debug("searching for class: " + className);
                Class clas = bcm.classForName(className);
                if (clas != null) {
                    return clas;
                }
            }
        }
        if (this.parent != null) {
            return this.parent.getCommand(name, argTypes, interpreter);
        }
        return null;
    }

    protected BshMethod getImportedMethod(String name, Class[] sig) throws UtilEvalError {
        if (this.importedObjects != null) {
            for (int i = 0; i < this.importedObjects.size(); i++) {

                Object object = this.importedObjects.elementAt(i);
                Class<?> clas = object.getClass();
                Method method = Reflect.resolveJavaMethod(getClassManager(), clas, name, sig, false);

                if (method != null) {
                    return new BshMethod(method, object);
                }
            }
        }
        if (this.importedStatic != null)
            for (int i = 0; i < this.importedStatic.size(); i++) {

                Class clas = this.importedStatic.elementAt(i).getClass();
                Method method = Reflect.resolveJavaMethod(getClassManager(), clas, name, sig, true);

                if (method != null) {
                    return new BshMethod(method, null);
                }
            }
        return null;
    }

    protected Variable getImportedVar(String name) throws UtilEvalError {
        if (this.importedObjects != null) {
            for (int i = 0; i < this.importedObjects.size(); i++) {

                Object object = this.importedObjects.elementAt(i);
                Class<?> clas = object.getClass();
                Field field = Reflect.resolveJavaField(clas, name, false);

                if (field != null) {
                    return createVariable(name, field.getType(), new LHS(object, field));
                }
            }
        }

        if (this.importedStatic != null)
            for (int i = 0; i < this.importedStatic.size(); i++) {

                Class clas = this.importedStatic.elementAt(i).getClass();
                Field field = Reflect.resolveJavaField(clas, name, true);

                if (field != null) {
                    return createVariable(name, field.getType(), new LHS(field));
                }
            }
        return null;
    }

    private BshMethod loadScriptedCommand(InputStream in, String name, Class[] argTypes, String resourcePath,
                                          Interpreter interpreter) throws UtilEvalError {
        try {
            interpreter.eval(new InputStreamReader(in), this, resourcePath);
        } catch (EvalError e) {

            Interpreter.debug(e.toString());
            throw new UtilEvalError("Error loading script: " + e.getMessage());
        }

        BshMethod meth = getMethod(name, argTypes);

        return meth;
    }

    void cacheClass(String name, Class<?> c) {
        if (this.classCache == null) {
            this.classCache = new Hashtable<Object, Object>();
        }

        this.classCache.put(name, c);
    }

    public Class getClass(String name) throws UtilEvalError {
        Class c = getClassImpl(name);
        if (c != null) {
            return c;
        }

        if (this.parent != null) {
            return this.parent.getClass(name);
        }
        return null;
    }

    private Class getClassImpl(String name) throws UtilEvalError {
        Class c = null;

        if (this.classCache != null) {
            c = (Class) this.classCache.get(name);

            if (c != null) {
                return c;
            }
        }

        boolean unqualifiedName = !Name.isCompound(name);

        if (unqualifiedName) {

            if (c == null) {
                c = getImportedClassImpl(name);
            }

            if (c != null) {
                cacheClass(name, c);
                return c;
            }
        }

        c = classForName(name);
        if (c != null) {

            if (unqualifiedName)
                cacheClass(name, c);
            return c;
        }

        if (Interpreter.DEBUG)
            Interpreter.debug("getClass(): " + name + " not\tfound in " + this);
        return null;
    }

    private Class getImportedClassImpl(String name) throws UtilEvalError {
        String fullname = null;
        if (this.importedClasses != null) {
            fullname = (String) this.importedClasses.get(name);
        }

        if (fullname != null) {

            Class clas = classForName(fullname);

            if (clas == null) {

                if (Name.isCompound(fullname)) {
                    try {
                        clas = getNameResolver(fullname).toClass();
                    } catch (ClassNotFoundException e) {
                    }
                } else if (Interpreter.DEBUG) {
                    Interpreter.debug("imported unpackaged name not found:" + fullname);
                }

                if (clas != null) {

                    getClassManager().cacheClassInfo(fullname, clas);
                    return clas;
                }
            } else {
                return clas;
            }

            return null;
        }

        if (this.importedPackages != null)
            for (int i = this.importedPackages.size() - 1; i >= 0; i--) {

                String s = (String) this.importedPackages.elementAt(i) + "." + name;
                Class c = classForName(s);
                if (c != null) {
                    return c;
                }
            }
        BshClassManager bcm = getClassManager();

        if (bcm.hasSuperImport()) {

            String s = bcm.getClassNameByUnqName(name);
            if (s != null) {
                return classForName(s);
            }
        }
        return null;
    }

    private Class classForName(String name) {
        return getClassManager().classForName(name);
    }

    public String[] getAllNames() {
        Vector vec = new Vector();
        getAllNamesAux(vec);
        String[] names = new String[vec.size()];
        vec.copyInto((Object[]) names);
        return names;
    }

    protected void getAllNamesAux(Vector vec) {
        Enumeration varNames = this.variables.keys();
        while (varNames.hasMoreElements()) {
            vec.addElement(varNames.nextElement());
        }
        Enumeration methodNames = this.methods.keys();
        while (methodNames.hasMoreElements()) {
            vec.addElement(methodNames.nextElement());
        }
        if (this.parent != null) {
            this.parent.getAllNamesAux(vec);
        }
    }

    public void addNameSourceListener(NameSource.Listener listener) {
        if (this.nameSourceListeners == null)
            this.nameSourceListeners = new Vector();
        this.nameSourceListeners.addElement(listener);
    }

    public void doSuperImport() throws UtilEvalError {
        getClassManager().doSuperImport();
    }

    public String toString() {
        return "NameSpace: "
                + ((this.nsName == null) ? super.toString() : (this.nsName + " (" + super.toString() + ")"))
                + (this.isClass ? " (isClass) " : "") + (this.isMethod ? " (method) " : "")
                + ((this.classStatic != null) ? " (class static) " : "")
                + ((this.classInstance != null) ? " (class instance) " : "");
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        this.names = null;

        s.defaultWriteObject();
    }

    public Object invokeMethod(String methodName, Object[] args, Interpreter interpreter) throws EvalError {
        return invokeMethod(methodName, args, interpreter, null, null);
    }

    public Object invokeMethod(String methodName, Object[] args, Interpreter interpreter, CallStack callstack,
                               SimpleNode callerInfo) throws EvalError {
        return getThis(interpreter).invokeMethod(methodName, args, interpreter, callstack, callerInfo, false);
    }

    public void classLoaderChanged() {
        nameSpaceChanged();
    }

    public void nameSpaceChanged() {
        this.classCache = null;
        this.names = null;
    }

    public void loadDefaultImports() {
        importClass("bsh.EvalError");
        importClass("bsh.Interpreter");
        importPackage("javax.swing.event");
        importPackage("javax.swing");
        importPackage("java.awt.event");
        importPackage("java.awt");
        importPackage("java.net");
        importPackage("java.util");
        importPackage("java.io");
        importPackage("java.lang");
        importCommands("/bsh/commands");
    }

    Name getNameResolver(String ambigname) {
        if (this.names == null) {
            this.names = new Hashtable<Object, Object>();
        }
        Name name = (Name) this.names.get(ambigname);

        if (name == null) {
            name = new Name(this, ambigname);
            this.names.put(ambigname, name);
        }

        return name;
    }

    public int getInvocationLine() {
        SimpleNode node = getNode();
        if (node != null) {
            return node.getLineNumber();
        }
        return -1;
    }

    public String getInvocationText() {
        SimpleNode node = getNode();
        if (node != null) {
            return node.getText();
        }
        return "<invoked from Java code>";
    }

    public void clear() {
        this.variables = null;
        this.methods = null;
        this.importedClasses = null;
        this.importedPackages = null;
        this.importedCommands = null;
        this.importedObjects = null;
        if (this.parent == null)
            loadDefaultImports();
        this.classCache = null;
        this.names = null;
    }

    public void importObject(Object obj) {
        if (this.importedObjects == null) {
            this.importedObjects = new Vector();
        }

        if (this.importedObjects.contains(obj)) {
            this.importedObjects.remove(obj);
        }
        this.importedObjects.addElement(obj);
        nameSpaceChanged();
    }

    public void importStatic(Class<?> clas) {
        if (this.importedStatic == null) {
            this.importedStatic = new Vector();
        }

        if (this.importedStatic.contains(clas)) {
            this.importedStatic.remove(clas);
        }
        this.importedStatic.addElement(clas);
        nameSpaceChanged();
    }

    String getPackage() {
        if (this.packageName != null) {
            return this.packageName;
        }
        if (this.parent != null) {
            return this.parent.getPackage();
        }
        return null;
    }

    void setPackage(String packageName) {
        this.packageName = packageName;
    }
}
