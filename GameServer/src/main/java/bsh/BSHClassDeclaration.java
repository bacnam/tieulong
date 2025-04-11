package bsh;

class BSHClassDeclaration
        extends SimpleNode {
    static final String CLASSINITNAME = "_bshClassInit";
    String name;
    Modifiers modifiers;
    int numInterfaces;
    boolean extend;
    boolean isInterface;

    BSHClassDeclaration(int id) {
        super(id);
    }

    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        BSHBlock block;
        int child = 0;

        Class superClass = null;
        if (this.extend) {

            BSHAmbiguousName superNode = (BSHAmbiguousName) jjtGetChild(child++);
            superClass = superNode.toClass(callstack, interpreter);
        }

        Class[] interfaces = new Class[this.numInterfaces];
        for (int i = 0; i < this.numInterfaces; i++) {
            BSHAmbiguousName node = (BSHAmbiguousName) jjtGetChild(child++);
            interfaces[i] = node.toClass(callstack, interpreter);
            if (!interfaces[i].isInterface()) {
                throw new EvalError("Type: " + node.text + " is not an interface!", this, callstack);
            }
        }

        if (child < jjtGetNumChildren()) {
            block = (BSHBlock) jjtGetChild(child);
        } else {
            block = new BSHBlock(25);
        }
        try {
            return ClassGenerator.getClassGenerator().generateClass(this.name, this.modifiers, interfaces, superClass, block, this.isInterface, callstack, interpreter);

        } catch (UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    public String toString() {
        return "ClassDeclaration: " + this.name;
    }
}

