package bsh;

class BSHUnaryExpression
        extends SimpleNode
        implements ParserConstants {
    public int kind;
    public boolean postfix = false;

    BSHUnaryExpression(int id) {
        super(id);
    }

    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        SimpleNode node = (SimpleNode) jjtGetChild(0);

        try {
            if (this.kind == 100 || this.kind == 101) {
                LHS lhs = ((BSHPrimaryExpression) node).toLHS(callstack, interpreter);

                return lhsUnaryOperation(lhs, interpreter.getStrictJava());
            }
            return unaryOperation(node.eval(callstack, interpreter), this.kind);
        } catch (UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    private Object lhsUnaryOperation(LHS lhs, boolean strictJava) throws UtilEvalError {
        Object retVal;
        if (Interpreter.DEBUG) Interpreter.debug("lhsUnaryOperation");

        Object prevalue = lhs.getValue();
        Object postvalue = unaryOperation(prevalue, this.kind);

        if (this.postfix) {
            retVal = prevalue;
        } else {
            retVal = postvalue;
        }
        lhs.assign(postvalue, strictJava);
        return retVal;
    }

    private Object unaryOperation(Object op, int kind) throws UtilEvalError {
        if (op instanceof Boolean || op instanceof Character || op instanceof Number) {
            return primitiveWrapperUnaryOperation(op, kind);
        }
        if (!(op instanceof Primitive)) {
            throw new UtilEvalError("Unary operation " + tokenImage[kind] + " inappropriate for object");
        }

        return Primitive.unaryOperation((Primitive) op, kind);
    }

    private Object primitiveWrapperUnaryOperation(Object val, int kind) throws UtilEvalError {
        Class<?> operandType = val.getClass();
        Object operand = Primitive.promoteToInteger(val);

        if (operand instanceof Boolean) {
            return new Boolean(Primitive.booleanUnaryOperation((Boolean) operand, kind));
        }

        if (operand instanceof Integer) {

            int result = Primitive.intUnaryOperation((Integer) operand, kind);

            if (kind == 100 || kind == 101) {

                if (operandType == byte.class)
                    return new Byte((byte) result);
                if (operandType == short.class)
                    return new Short((short) result);
                if (operandType == char.class) {
                    return new Character((char) result);
                }
            }
            return new Integer(result);
        }
        if (operand instanceof Long)
            return new Long(Primitive.longUnaryOperation((Long) operand, kind));
        if (operand instanceof Float)
            return new Float(Primitive.floatUnaryOperation((Float) operand, kind));
        if (operand instanceof Double) {
            return new Double(Primitive.doubleUnaryOperation((Double) operand, kind));
        }
        throw new InterpreterError("An error occurred.  Please call technical support.");
    }
}

