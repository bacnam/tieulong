package bsh;

import java.lang.reflect.Array;

class BSHType
        extends SimpleNode
        implements BshClassManager.Listener {
    String descriptor;
    private Class baseType;
    private int arrayDims;
    private Class type;

    BSHType(int id) {
        super(id);
    }

    public static String getTypeDescriptor(Class<?> clas) {
        if (clas == boolean.class)
            return "Z";
        if (clas == char.class)
            return "C";
        if (clas == byte.class)
            return "B";
        if (clas == short.class)
            return "S";
        if (clas == int.class)
            return "I";
        if (clas == long.class)
            return "J";
        if (clas == float.class)
            return "F";
        if (clas == double.class)
            return "D";
        if (clas == void.class)
            return "V";

        String name = clas.getName().replace('.', '/');

        if (name.startsWith("[") || name.endsWith(";")) {
            return name;
        }
        return "L" + name.replace('.', '/') + ";";
    }

    public void addArrayDimension() {
        this.arrayDims++;
    }

    SimpleNode getTypeNode() {
        return (SimpleNode) jjtGetChild(0);
    }

    public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
        String descriptor;
        if (this.descriptor != null) {
            return this.descriptor;
        }

        SimpleNode node = getTypeNode();
        if (node instanceof BSHPrimitiveType) {
            descriptor = getTypeDescriptor(((BSHPrimitiveType) node).type);
        } else {

            String clasName = ((BSHAmbiguousName) node).text;
            BshClassManager bcm = interpreter.getClassManager();

            String definingClass = bcm.getClassBeingDefined(clasName);

            Class clas = null;
            if (definingClass == null) {

                try {
                    clas = ((BSHAmbiguousName) node).toClass(callstack, interpreter);
                } catch (EvalError e) {
                }

            } else {

                clasName = definingClass;
            }
            if (clas != null) {

                descriptor = getTypeDescriptor(clas);

            } else if (defaultPackage == null || Name.isCompound(clasName)) {
                descriptor = "L" + clasName.replace('.', '/') + ";";
            } else {
                descriptor = "L" + defaultPackage.replace('.', '/') + "/" + clasName + ";";
            }
        }

        for (int i = 0; i < this.arrayDims; i++) {
            descriptor = "[" + descriptor;
        }
        this.descriptor = descriptor;

        return descriptor;
    }

    public Class getType(CallStack callstack, Interpreter interpreter) throws EvalError {
        if (this.type != null) {
            return this.type;
        }

        SimpleNode node = getTypeNode();
        if (node instanceof BSHPrimitiveType) {
            this.baseType = ((BSHPrimitiveType) node).getType();
        } else {
            this.baseType = ((BSHAmbiguousName) node).toClass(callstack, interpreter);
        }

        if (this.arrayDims > 0) {

            try {
                int[] dims = new int[this.arrayDims];
                Object obj = Array.newInstance(this.baseType, dims);
                this.type = obj.getClass();
            } catch (Exception e) {
                throw new EvalError("Couldn't construct array type", this, callstack);
            }
        } else {

            this.type = this.baseType;
        }

        interpreter.getClassManager().addListener(this);

        return this.type;
    }

    public Class getBaseType() {
        return this.baseType;
    }

    public int getArrayDims() {
        return this.arrayDims;
    }

    public void classLoaderChanged() {
        this.type = null;
        this.baseType = null;
    }
}