package bsh;

import java.lang.reflect.Array;

class BSHArrayDimensions
        extends SimpleNode {
    public Class baseType;
    public int numDefinedDims;
    public int numUndefinedDims;
    public int[] definedDimensions;

    BSHArrayDimensions(int id) {
        super(id);
    }

    public void addDefinedDimension() {
        this.numDefinedDims++;
    }

    public void addUndefinedDimension() {
        this.numUndefinedDims++;
    }

    public Object eval(Class type, CallStack callstack, Interpreter interpreter) throws EvalError {
        if (Interpreter.DEBUG) Interpreter.debug("array base type = " + type);
        this.baseType = type;
        return eval(callstack, interpreter);
    }

    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        SimpleNode child = (SimpleNode) jjtGetChild(0);

        if (child instanceof BSHArrayInitializer) {

            if (this.baseType == null) {
                throw new EvalError("Internal Array Eval err:  unknown base type", this, callstack);
            }

            Object initValue = ((BSHArrayInitializer) child).eval(this.baseType, this.numUndefinedDims, callstack, interpreter);

            Class<?> arrayClass = initValue.getClass();
            int actualDimensions = Reflect.getArrayDimensions(arrayClass);
            this.definedDimensions = new int[actualDimensions];

            if (this.definedDimensions.length != this.numUndefinedDims) {
                throw new EvalError("Incompatible initializer. Allocation calls for a " + this.numUndefinedDims + " dimensional array, but initializer is a " + actualDimensions + " dimensional array", this, callstack);
            }

            Object arraySlice = initValue;
            for (int j = 0; j < this.definedDimensions.length; j++) {
                this.definedDimensions[j] = Array.getLength(arraySlice);
                if (this.definedDimensions[j] > 0) {
                    arraySlice = Array.get(arraySlice, 0);
                }
            }
            return initValue;
        }

        this.definedDimensions = new int[this.numDefinedDims];

        for (int i = 0; i < this.numDefinedDims; i++) {

            try {
                Object length = ((SimpleNode) jjtGetChild(i)).eval(callstack, interpreter);

                this.definedDimensions[i] = ((Primitive) length).intValue();
            } catch (Exception e) {

                throw new EvalError("Array index: " + i + " does not evaluate to an integer", this, callstack);
            }
        }

        return Primitive.VOID;
    }
}

