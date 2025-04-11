package bsh;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

class BSHPrimarySuffix
        extends SimpleNode {
    public static final int CLASS = 0;
    public static final int INDEX = 1;
    public static final int NAME = 2;
    public static final int PROPERTY = 3;
    public int operation;
    public String field;
    Object index;

    BSHPrimarySuffix(int id) {
        super(id);
    }

    static int getIndexAux(Object obj, CallStack callstack, Interpreter interpreter, SimpleNode callerInfo) throws EvalError {
        int index;
        if (!obj.getClass().isArray()) {
            throw new EvalError("Not an array", callerInfo, callstack);
        }

        try {
            Object indexVal = ((SimpleNode) callerInfo.jjtGetChild(0)).eval(callstack, interpreter);

            if (!(indexVal instanceof Primitive)) {
                indexVal = Types.castObject(indexVal, int.class, 1);
            }
            index = ((Primitive) indexVal).intValue();
        } catch (UtilEvalError e) {
            Interpreter.debug("doIndex: " + e);
            throw e.toEvalError("Arrays may only be indexed by integer types.", callerInfo, callstack);
        }

        return index;
    }

    public Object doSuffix(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError {
        if (this.operation == 0) {
            if (obj instanceof BSHType) {
                if (toLHS) {
                    throw new EvalError("Can't assign .class", this, callstack);
                }
                NameSpace namespace = callstack.top();
                return ((BSHType) obj).getType(callstack, interpreter);
            }
            throw new EvalError("Attempt to use .class suffix on non class.", this, callstack);
        }

        if (obj instanceof SimpleNode) {
            if (obj instanceof BSHAmbiguousName) {
                obj = ((BSHAmbiguousName) obj).toObject(callstack, interpreter);
            } else {
                obj = ((SimpleNode) obj).eval(callstack, interpreter);
            }
        } else if (obj instanceof LHS) {
            try {
                obj = ((LHS) obj).getValue();
            } catch (UtilEvalError e) {
                throw e.toEvalError(this, callstack);
            }
        }

        try {
            switch (this.operation) {

                case 1:
                    return doIndex(obj, toLHS, callstack, interpreter);

                case 2:
                    return doName(obj, toLHS, callstack, interpreter);

                case 3:
                    return doProperty(toLHS, obj, callstack, interpreter);
            }

            throw new InterpreterError("Unknown suffix type");

        } catch (ReflectError e) {

            throw new EvalError("reflection error: " + e, this, callstack);
        } catch (InvocationTargetException e) {

            throw new TargetError("target exception", e.getTargetException(), this, callstack, true);
        }
    }

    private Object doName(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError, ReflectError, InvocationTargetException {
        try {
            if (this.field.equals("length") && obj.getClass().isArray()) {
                if (toLHS) {
                    throw new EvalError("Can't assign array length", this, callstack);
                }

                return new Primitive(Array.getLength(obj));
            }

            if (jjtGetNumChildren() == 0) {
                if (toLHS) {
                    return Reflect.getLHSObjectField(obj, this.field);
                }
                return Reflect.getObjectFieldValue(obj, this.field);
            }

            Object[] oa = ((BSHArguments) jjtGetChild(0)).getArguments(callstack, interpreter);

            try {
                return Reflect.invokeObjectMethod(obj, this.field, oa, interpreter, callstack, this);
            } catch (ReflectError e) {
                throw new EvalError("Error in method invocation: " + e.getMessage(), this, callstack);

            } catch (InvocationTargetException e) {

                String msg = "Method Invocation " + this.field;
                Throwable te = e.getTargetException();

                boolean isNative = true;
                if (te instanceof EvalError)
                    if (te instanceof TargetError) {
                        isNative = ((TargetError) te).inNativeCode();
                    } else {
                        isNative = false;
                    }
                throw new TargetError(msg, te, this, callstack, isNative);
            }

        } catch (UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    private Object doIndex(Object obj, boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError, ReflectError {
        int index = getIndexAux(obj, callstack, interpreter, this);
        if (toLHS) {
            return new LHS(obj, index);
        }
        try {
            return Reflect.getIndex(obj, index);
        } catch (UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    private Object doProperty(boolean toLHS, Object obj, CallStack callstack, Interpreter interpreter) throws EvalError {
        if (obj == Primitive.VOID) {
            throw new EvalError("Attempt to access property on undefined variable or class name", this, callstack);
        }

        if (obj instanceof Primitive) {
            throw new EvalError("Attempt to access property on a primitive", this, callstack);
        }

        Object value = ((SimpleNode) jjtGetChild(0)).eval(callstack, interpreter);

        if (!(value instanceof String)) {
            throw new EvalError("Property expression must be a String or identifier.", this, callstack);
        }

        if (toLHS) {
            return new LHS(obj, (String) value);
        }

        CollectionManager cm = CollectionManager.getCollectionManager();
        if (cm.isMap(obj)) {

            Object val = cm.getFromMap(obj, value);
            return (val == null) ? (val = Primitive.NULL) : val;
        }

        try {
            return Reflect.getObjectProperty(obj, (String) value);
        } catch (UtilEvalError e) {

            throw e.toEvalError("Property: " + value, this, callstack);
        } catch (ReflectError e) {

            throw new EvalError("No such property: " + value, this, callstack);
        }
    }
}

