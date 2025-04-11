package bsh;

import java.io.Serializable;
import java.util.Hashtable;

public final class Primitive
        implements ParserConstants, Serializable {
    static Hashtable wrapperMap = new Hashtable<Object, Object>();
    private Object value;
    static {
        wrapperMap.put(boolean.class, Boolean.class);
        wrapperMap.put(byte.class, Byte.class);
        wrapperMap.put(short.class, Short.class);
        wrapperMap.put(char.class, Character.class);
        wrapperMap.put(int.class, Integer.class);
        wrapperMap.put(long.class, Long.class);
        wrapperMap.put(float.class, Float.class);
        wrapperMap.put(double.class, Double.class);
        wrapperMap.put(Boolean.class, boolean.class);
        wrapperMap.put(Byte.class, byte.class);
        wrapperMap.put(Short.class, short.class);
        wrapperMap.put(Character.class, char.class);
        wrapperMap.put(Integer.class, int.class);
        wrapperMap.put(Long.class, long.class);
        wrapperMap.put(Float.class, float.class);
        wrapperMap.put(Double.class, double.class);
    }

    private static class Special
            implements Serializable {
        public static final Special NULL_VALUE = new Special();
        public static final Special VOID_TYPE = new Special();
    }

    public static final Primitive NULL = new Primitive(Special.NULL_VALUE);

    public static final Primitive VOID = new Primitive(Special.VOID_TYPE);

    public Primitive(Object value) {
        if (value == null) {
            throw new InterpreterError("Use Primitve.NULL instead of Primitive(null)");
        }

        if (value != Special.NULL_VALUE && value != Special.VOID_TYPE && !isWrapperType(value.getClass())) {

            throw new InterpreterError("Not a wrapper type: " + value.getClass());
        }
        this.value = value;
    }

    public Primitive(boolean value) {
        this(new Boolean(value));
    }

    public Primitive(byte value) {
        this(new Byte(value));
    }

    public Primitive(short value) {
        this(new Short(value));
    }

    public Primitive(char value) {
        this(new Character(value));
    }

    public Primitive(int value) {
        this(new Integer(value));
    }

    public Primitive(long value) {
        this(new Long(value));
    }

    public Primitive(float value) {
        this(new Float(value));
    }

    public Primitive(double value) {
        this(new Double(value));
    }

    public Object getValue() {
        if (this.value == Special.NULL_VALUE) {
            return null;
        }
        if (this.value == Special.VOID_TYPE) {
            throw new InterpreterError("attempt to unwrap void type");
        }
        return this.value;
    }

    public String toString() {
        if (this.value == Special.NULL_VALUE)
            return "null";
        if (this.value == Special.VOID_TYPE) {
            return "void";
        }
        return this.value.toString();
    }

    public Class getType() {
        if (this == VOID) {
            return void.class;
        }

        if (this == NULL) {
            return null;
        }
        return unboxType(this.value.getClass());
    }

    public static Object binaryOperation(Object obj1, Object obj2, int kind) throws UtilEvalError {
        Object result;
        if (obj1 == NULL || obj2 == NULL) {
            throw new UtilEvalError("Null value or 'null' literal in binary operation");
        }
        if (obj1 == VOID || obj2 == VOID) {
            throw new UtilEvalError("Undefined variable, class, or 'void' literal in binary operation");
        }

        Class<?> lhsOrgType = obj1.getClass();
        Class<?> rhsOrgType = obj2.getClass();

        if (obj1 instanceof Primitive)
            obj1 = ((Primitive) obj1).getValue();
        if (obj2 instanceof Primitive) {
            obj2 = ((Primitive) obj2).getValue();
        }
        Object[] operands = promotePrimitives(obj1, obj2);
        Object lhs = operands[0];
        Object rhs = operands[1];

        if (lhs.getClass() != rhs.getClass()) {
            throw new UtilEvalError(
                    "Type mismatch in operator.  " + lhs.getClass() + " cannot be used with " + rhs.getClass());
        }

        try {
            result = binaryOperationImpl(lhs, rhs, kind);
        } catch (ArithmeticException e) {
            throw new UtilTargetError("Arithemetic Exception in binary op", e);
        }

        if ((lhsOrgType == Primitive.class && rhsOrgType == Primitive.class) || result instanceof Boolean) {

            return new Primitive(result);
        }
        return result;
    }

    static Object binaryOperationImpl(Object lhs, Object rhs, int kind) throws UtilEvalError {
        if (lhs instanceof Boolean)
            return booleanBinaryOperation((Boolean) lhs, (Boolean) rhs, kind);
        if (lhs instanceof Integer)
            return intBinaryOperation((Integer) lhs, (Integer) rhs, kind);
        if (lhs instanceof Long)
            return longBinaryOperation((Long) lhs, (Long) rhs, kind);
        if (lhs instanceof Float)
            return floatBinaryOperation((Float) lhs, (Float) rhs, kind);
        if (lhs instanceof Double) {
            return doubleBinaryOperation((Double) lhs, (Double) rhs, kind);
        }
        throw new UtilEvalError("Invalid types in binary operator");
    }

    static Boolean booleanBinaryOperation(Boolean B1, Boolean B2, int kind) {
        boolean lhs = B1.booleanValue();
        boolean rhs = B2.booleanValue();

        switch (kind) {

            case 90:
                return new Boolean((lhs == rhs));

            case 95:
                return new Boolean((lhs != rhs));

            case 96:
            case 97:
                return new Boolean((lhs || rhs));

            case 98:
            case 99:
                return new Boolean((lhs && rhs));
        }

        throw new InterpreterError("unimplemented binary operator");
    }

    static Object longBinaryOperation(Long L1, Long L2, int kind) {
        long lhs = L1.longValue();
        long rhs = L2.longValue();

        switch (kind) {

            case 84:
            case 85:
                return new Boolean((lhs < rhs));

            case 82:
            case 83:
                return new Boolean((lhs > rhs));

            case 90:
                return new Boolean((lhs == rhs));

            case 91:
            case 92:
                return new Boolean((lhs <= rhs));

            case 93:
            case 94:
                return new Boolean((lhs >= rhs));

            case 95:
                return new Boolean((lhs != rhs));

            case 102:
                return new Long(lhs + rhs);

            case 103:
                return new Long(lhs - rhs);

            case 104:
                return new Long(lhs * rhs);

            case 105:
                return new Long(lhs / rhs);

            case 111:
                return new Long(lhs % rhs);

            case 112:
            case 113:
                return new Long(lhs << (int) rhs);

            case 114:
            case 115:
                return new Long(lhs >> (int) rhs);

            case 116:
            case 117:
                return new Long(lhs >>> (int) rhs);

            case 106:
            case 107:
                return new Long(lhs & rhs);

            case 108:
            case 109:
                return new Long(lhs | rhs);

            case 110:
                return new Long(lhs ^ rhs);
        }

        throw new InterpreterError("Unimplemented binary long operator");
    }

    static Object intBinaryOperation(Integer I1, Integer I2, int kind) {
        int lhs = I1.intValue();
        int rhs = I2.intValue();

        switch (kind) {

            case 84:
            case 85:
                return new Boolean((lhs < rhs));

            case 82:
            case 83:
                return new Boolean((lhs > rhs));

            case 90:
                return new Boolean((lhs == rhs));

            case 91:
            case 92:
                return new Boolean((lhs <= rhs));

            case 93:
            case 94:
                return new Boolean((lhs >= rhs));

            case 95:
                return new Boolean((lhs != rhs));

            case 102:
                return new Integer(lhs + rhs);

            case 103:
                return new Integer(lhs - rhs);

            case 104:
                return new Integer(lhs * rhs);

            case 105:
                return new Integer(lhs / rhs);

            case 111:
                return new Integer(lhs % rhs);

            case 112:
            case 113:
                return new Integer(lhs << rhs);

            case 114:
            case 115:
                return new Integer(lhs >> rhs);

            case 116:
            case 117:
                return new Integer(lhs >>> rhs);

            case 106:
            case 107:
                return new Integer(lhs & rhs);

            case 108:
            case 109:
                return new Integer(lhs | rhs);

            case 110:
                return new Integer(lhs ^ rhs);
        }

        throw new InterpreterError("Unimplemented binary integer operator");
    }

    static Object doubleBinaryOperation(Double D1, Double D2, int kind) throws UtilEvalError {
        double lhs = D1.doubleValue();
        double rhs = D2.doubleValue();

        switch (kind) {

            case 84:
            case 85:
                return new Boolean((lhs < rhs));

            case 82:
            case 83:
                return new Boolean((lhs > rhs));

            case 90:
                return new Boolean((lhs == rhs));

            case 91:
            case 92:
                return new Boolean((lhs <= rhs));

            case 93:
            case 94:
                return new Boolean((lhs >= rhs));

            case 95:
                return new Boolean((lhs != rhs));

            case 102:
                return new Double(lhs + rhs);

            case 103:
                return new Double(lhs - rhs);

            case 104:
                return new Double(lhs * rhs);

            case 105:
                return new Double(lhs / rhs);

            case 111:
                return new Double(lhs % rhs);

            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
                throw new UtilEvalError("Can't shift doubles");
        }

        throw new InterpreterError("Unimplemented binary double operator");
    }

    static Object floatBinaryOperation(Float F1, Float F2, int kind) throws UtilEvalError {
        float lhs = F1.floatValue();
        float rhs = F2.floatValue();

        switch (kind) {

            case 84:
            case 85:
                return new Boolean((lhs < rhs));

            case 82:
            case 83:
                return new Boolean((lhs > rhs));

            case 90:
                return new Boolean((lhs == rhs));

            case 91:
            case 92:
                return new Boolean((lhs <= rhs));

            case 93:
            case 94:
                return new Boolean((lhs >= rhs));

            case 95:
                return new Boolean((lhs != rhs));

            case 102:
                return new Float(lhs + rhs);

            case 103:
                return new Float(lhs - rhs);

            case 104:
                return new Float(lhs * rhs);

            case 105:
                return new Float(lhs / rhs);

            case 111:
                return new Float(lhs % rhs);

            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
                throw new UtilEvalError("Can't shift floats ");
        }

        throw new InterpreterError("Unimplemented binary float operator");
    }

    static Object promoteToInteger(Object wrapper) {
        if (wrapper instanceof Character)
            return new Integer(((Character) wrapper).charValue());
        if (wrapper instanceof Byte || wrapper instanceof Short) {
            return new Integer(((Number) wrapper).intValue());
        }
        return wrapper;
    }

    static Object[] promotePrimitives(Object lhs, Object rhs) {
        lhs = promoteToInteger(lhs);
        rhs = promoteToInteger(rhs);

        if (lhs instanceof Number && rhs instanceof Number) {

            Number lnum = (Number) lhs;
            Number rnum = (Number) rhs;

            boolean b;

            if ((b = lnum instanceof Double) || rnum instanceof Double) {

                if (b) {
                    rhs = new Double(rnum.doubleValue());
                } else {
                    lhs = new Double(lnum.doubleValue());
                }
            } else if ((b = lnum instanceof Float) || rnum instanceof Float) {

                if (b) {
                    rhs = new Float(rnum.floatValue());
                } else {
                    lhs = new Float(lnum.floatValue());
                }
            } else if ((b = lnum instanceof Long) || rnum instanceof Long) {

                if (b) {
                    rhs = new Long(rnum.longValue());
                } else {
                    lhs = new Long(lnum.longValue());
                }
            }
        }
        return new Object[] { lhs, rhs };
    }

    public static Primitive unaryOperation(Primitive val, int kind) throws UtilEvalError {
        if (val == NULL) {
            throw new UtilEvalError("illegal use of null object or 'null' literal");
        }
        if (val == VOID) {
            throw new UtilEvalError("illegal use of undefined object or 'void' literal");
        }

        Class<?> operandType = val.getType();
        Object operand = promoteToInteger(val.getValue());

        if (operand instanceof Boolean)
            return new Primitive(booleanUnaryOperation((Boolean) operand, kind));
        if (operand instanceof Integer) {

            int result = intUnaryOperation((Integer) operand, kind);

            if (kind == 100 || kind == 101) {

                if (operandType == byte.class)
                    return new Primitive((byte) result);
                if (operandType == short.class)
                    return new Primitive((short) result);
                if (operandType == char.class) {
                    return new Primitive((char) result);
                }
            }
            return new Primitive(result);
        }
        if (operand instanceof Long)
            return new Primitive(longUnaryOperation((Long) operand, kind));
        if (operand instanceof Float)
            return new Primitive(floatUnaryOperation((Float) operand, kind));
        if (operand instanceof Double) {
            return new Primitive(doubleUnaryOperation((Double) operand, kind));
        }
        throw new InterpreterError("An error occurred.  Please call technical support.");
    }

    static boolean booleanUnaryOperation(Boolean B, int kind) throws UtilEvalError {
        boolean operand = B.booleanValue();
        switch (kind) {

            case 86:
                return !operand;
        }
        throw new UtilEvalError("Operator inappropriate for boolean");
    }

    static int intUnaryOperation(Integer I, int kind) {
        int operand = I.intValue();

        switch (kind) {

            case 102:
                return operand;
            case 103:
                return -operand;
            case 87:
                return operand ^ 0xFFFFFFFF;
            case 100:
                return operand + 1;
            case 101:
                return operand - 1;
        }
        throw new InterpreterError("bad integer unaryOperation");
    }

    static long longUnaryOperation(Long L, int kind) {
        long operand = L.longValue();

        switch (kind) {

            case 102:
                return operand;
            case 103:
                return -operand;
            case 87:
                return operand ^ 0xFFFFFFFFFFFFFFFFL;
            case 100:
                return operand + 1L;
            case 101:
                return operand - 1L;
        }
        throw new InterpreterError("bad long unaryOperation");
    }

    static float floatUnaryOperation(Float F, int kind) {
        float operand = F.floatValue();

        switch (kind) {

            case 102:
                return operand;
            case 103:
                return -operand;
        }
        throw new InterpreterError("bad float unaryOperation");
    }

    static double doubleUnaryOperation(Double D, int kind) {
        double operand = D.doubleValue();

        switch (kind) {

            case 102:
                return operand;
            case 103:
                return -operand;
        }
        throw new InterpreterError("bad double unaryOperation");
    }

    public int intValue() throws UtilEvalError {
        if (this.value instanceof Number) {
            return ((Number) this.value).intValue();
        }
        throw new UtilEvalError("Primitive not a number");
    }

    public boolean booleanValue() throws UtilEvalError {
        if (this.value instanceof Boolean) {
            return ((Boolean) this.value).booleanValue();
        }
        throw new UtilEvalError("Primitive not a boolean");
    }

    public boolean isNumber() {
        return (!(this.value instanceof Boolean) && this != NULL && this != VOID);
    }

    public Number numberValue() throws UtilEvalError {
        Object value = this.value;

        if (value instanceof Character) {
            value = new Integer(((Character) value).charValue());
        }
        if (value instanceof Number) {
            return (Number) value;
        }
        throw new UtilEvalError("Primitive not a number");
    }

    public boolean equals(Object obj) {
        if (obj instanceof Primitive) {
            return ((Primitive) obj).value.equals(this.value);
        }
        return false;
    }

    public int hashCode() {
        return this.value.hashCode() * 21;
    }

    public static Object unwrap(Object obj) {
        if (obj == VOID) {
            return null;
        }

        if (obj instanceof Primitive) {
            return ((Primitive) obj).getValue();
        }
        return obj;
    }

    public static Object[] unwrap(Object[] args) {
        Object[] oa = new Object[args.length];
        for (int i = 0; i < args.length; i++)
            oa[i] = unwrap(args[i]);
        return oa;
    }

    public static Object[] wrap(Object[] args, Class[] paramTypes) {
        if (args == null) {
            return null;
        }
        Object[] oa = new Object[args.length];
        for (int i = 0; i < args.length; i++)
            oa[i] = wrap(args[i], paramTypes[i]);
        return oa;
    }

    public static Object wrap(Object value, Class<?> type) {
        if (type == void.class) {
            return VOID;
        }
        if (value == null) {
            return NULL;
        }
        if (type.isPrimitive() && isWrapperType(value.getClass())) {
            return new Primitive(value);
        }
        return value;
    }

    public static Primitive getDefaultValue(Class<?> type) {
        if (type == null || !type.isPrimitive())
            return NULL;
        if (type == boolean.class) {
            return new Primitive(false);
        }

        try {
            return (new Primitive(0)).castToType(type, 0);
        } catch (UtilEvalError e) {
            throw new InterpreterError("bad cast");
        }
    }

    public static Class boxType(Class primitiveType) {
        Class c = (Class) wrapperMap.get(primitiveType);
        if (c != null)
            return c;
        throw new InterpreterError("Not a primitive type: " + primitiveType);
    }

    public static Class unboxType(Class wrapperType) {
        Class c = (Class) wrapperMap.get(wrapperType);
        if (c != null)
            return c;
        throw new InterpreterError("Not a primitive wrapper type: " + wrapperType);
    }

    public Primitive castToType(Class toType, int operation) throws UtilEvalError {
        return castPrimitive(toType, getType(), this, false, operation);
    }

    static Primitive castPrimitive(Class<?> toType, Class<?> fromType, Primitive fromValue, boolean checkOnly,
            int operation) throws UtilEvalError {
        if (checkOnly && fromValue != null)
            throw new InterpreterError("bad cast param 1");
        if (!checkOnly && fromValue == null)
            throw new InterpreterError("bad cast param 2");
        if (fromType != null && !fromType.isPrimitive())
            throw new InterpreterError("bad fromType:" + fromType);
        if (fromValue == NULL && fromType != null)
            throw new InterpreterError("inconsistent args 1");
        if (fromValue == VOID && fromType != void.class) {
            throw new InterpreterError("inconsistent args 2");
        }

        if (fromType == void.class) {
            if (checkOnly) {
                return Types.INVALID_CAST;
            }
            throw Types.castError(Reflect.normalizeClassName(toType), "void value", operation);
        }

        Object value = null;
        if (fromValue != null) {
            value = fromValue.getValue();
        }
        if (toType.isPrimitive()) {

            if (fromType == null) {
                if (checkOnly) {
                    return Types.INVALID_CAST;
                }
                throw Types.castError("primitive type:" + toType, "Null value", operation);

            }

        } else {

            if (fromType == null) {
                return checkOnly ? Types.VALID_CAST : NULL;
            }

            if (checkOnly) {
                return Types.INVALID_CAST;
            }
            throw Types.castError("object type:" + toType, "primitive value", operation);
        }

        if (fromType == boolean.class) {

            if (toType != boolean.class) {
                if (checkOnly) {
                    return Types.INVALID_CAST;
                }
                throw Types.castError(toType, fromType, operation);
            }
            return checkOnly ? Types.VALID_CAST : fromValue;
        }

        if (operation == 1 && !Types.isJavaAssignable(toType, fromType)) {

            if (checkOnly) {
                return Types.INVALID_CAST;
            }
            throw Types.castError(toType, fromType, operation);
        }

        return checkOnly ? Types.VALID_CAST : new Primitive(castWrapper(toType, value));
    }

    public static boolean isWrapperType(Class type) {
        return (wrapperMap.get(type) != null && !type.isPrimitive());
    }

    static Object castWrapper(Class<?> toType, Object value) {
        if (!toType.isPrimitive())
            throw new InterpreterError("invalid type in castWrapper: " + toType);
        if (value == null)
            throw new InterpreterError("null value in castWrapper, guard");
        if (value instanceof Boolean) {

            if (toType != boolean.class) {
                throw new InterpreterError("bad wrapper cast of boolean");
            }
            return value;
        }

        if (value instanceof Character) {
            value = new Integer(((Character) value).charValue());
        }
        if (!(value instanceof Number)) {
            throw new InterpreterError("bad type in cast");
        }
        Number number = (Number) value;

        if (toType == byte.class)
            return new Byte(number.byteValue());
        if (toType == short.class)
            return new Short(number.shortValue());
        if (toType == char.class)
            return new Character((char) number.intValue());
        if (toType == int.class)
            return new Integer(number.intValue());
        if (toType == long.class)
            return new Long(number.longValue());
        if (toType == float.class)
            return new Float(number.floatValue());
        if (toType == double.class) {
            return new Double(number.doubleValue());
        }
        throw new InterpreterError("error in wrapper cast");
    }
}
