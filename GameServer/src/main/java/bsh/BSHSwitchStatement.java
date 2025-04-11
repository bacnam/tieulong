package bsh;

class BSHSwitchStatement
        extends SimpleNode
        implements ParserConstants {
    public BSHSwitchStatement(int id) {
        super(id);
    }

    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        int numchild = jjtGetNumChildren();
        int child = 0;
        SimpleNode switchExp = (SimpleNode) jjtGetChild(child++);
        Object switchVal = switchExp.eval(callstack, interpreter);

        ReturnControl returnControl = null;

        if (child >= numchild)
            throw new EvalError("Empty switch statement.", this, callstack);
        BSHSwitchLabel label = (BSHSwitchLabel) jjtGetChild(child++);

        while (child < numchild && returnControl == null) {

            if (label.isDefault || primitiveEquals(switchVal, label.eval(callstack, interpreter), callstack, switchExp)) {

                while (child < numchild) {

                    Object node = jjtGetChild(child++);
                    if (node instanceof BSHSwitchLabel) {
                        continue;
                    }
                    Object value = ((SimpleNode) node).eval(callstack, interpreter);

                    if (value instanceof ReturnControl) {
                        returnControl = (ReturnControl) value;
                    }
                }

                continue;
            }

            while (child < numchild) {

                Object node = jjtGetChild(child++);
                if (node instanceof BSHSwitchLabel) {
                    label = (BSHSwitchLabel) node;
                }
            }
        }

        if (returnControl != null && returnControl.kind == 46) {
            return returnControl;
        }
        return Primitive.VOID;
    }

    private boolean primitiveEquals(Object switchVal, Object targetVal, CallStack callstack, SimpleNode switchExp) throws EvalError {
        if (switchVal instanceof Primitive || targetVal instanceof Primitive) {

            try {
                Object result = Primitive.binaryOperation(switchVal, targetVal, 90);

                result = Primitive.unwrap(result);
                return result.equals(Boolean.TRUE);
            } catch (UtilEvalError e) {
                throw e.toEvalError("Switch value: " + switchExp.getText() + ": ", this, callstack);
            }
        }

        return switchVal.equals(targetVal);
    }
}

