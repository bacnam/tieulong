package bsh.org.objectweb.asm;

import java.lang.reflect.Method;

public class Type {
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final Type VOID_TYPE = new Type(0);

    public static final Type BOOLEAN_TYPE = new Type(1);

    public static final Type CHAR_TYPE = new Type(2);

    public static final Type BYTE_TYPE = new Type(3);

    public static final Type SHORT_TYPE = new Type(4);

    public static final Type INT_TYPE = new Type(5);

    public static final Type FLOAT_TYPE = new Type(6);

    public static final Type LONG_TYPE = new Type(7);

    public static final Type DOUBLE_TYPE = new Type(8);

    private final int sort;

    private char[] buf;

    private int off;

    private int len;

    private Type(int sort) {
        this.sort = sort;
        this.len = 1;
    }

    private Type(int sort, char[] buf, int off, int len) {
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }

    public static Type getType(String typeDescriptor) {
        return getType(typeDescriptor.toCharArray(), 0);
    }

    public static Type getType(Class<?> c) {
        if (c.isPrimitive()) {
            if (c == int.class)
                return INT_TYPE;
            if (c == void.class)
                return VOID_TYPE;
            if (c == boolean.class)
                return BOOLEAN_TYPE;
            if (c == byte.class)
                return BYTE_TYPE;
            if (c == char.class)
                return CHAR_TYPE;
            if (c == short.class)
                return SHORT_TYPE;
            if (c == double.class)
                return DOUBLE_TYPE;
            if (c == float.class) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }

        return getType(getDescriptor(c));
    }

    public static Type[] getArgumentTypes(String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        int off = 1;
        int size = 0;
        while (true) {
            char car = buf[off++];
            if (car == ')')
                break;
            if (car == 'L') {
                while (buf[off++] != ';')
                    ;

                size++;
                continue;
            }
            if (car != '[') {
                size++;
            }
        }
        Type[] args = new Type[size];
        off = 1;
        size = 0;
        while (buf[off] != ')') {
            args[size] = getType(buf, off);
            off += (args[size]).len;
            size++;
        }
        return args;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class[] classes = method.getParameterTypes();
        Type[] types = new Type[classes.length];
        for (int i = classes.length - 1; i >= 0; i--) {
            types[i] = getType(classes[i]);
        }
        return types;
    }

    public static Type getReturnType(String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        return getType(buf, methodDescriptor.indexOf(')') + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    private static Type getType(char[] buf, int off) {
        switch (buf[off]) {
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'B':
                return BYTE_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case '[':
                int len = 1;
                while (buf[off + len] == '[') {
                    len++;
                }
                if (buf[off + len] == 'L') {
                    len++;
                    while (buf[off + len] != ';') {
                        len++;
                    }
                }
                return new Type(9, buf, off, len + 1);
        }

        int len = 1;
        while (buf[off + len] != ';') {
            len++;
        }
        return new Type(10, buf, off, len + 1);
    }

    public static String getMethodDescriptor(Type returnType, Type[] argumentTypes) {
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i = 0; i < argumentTypes.length; i++) {
            argumentTypes[i].getDescriptor(buf);
        }
        buf.append(')');
        returnType.getDescriptor(buf);
        return buf.toString();
    }

    public static String getInternalName(Class c) {
        return c.getName().replace('.', '/');
    }

    public static String getDescriptor(Class c) {
        StringBuffer buf = new StringBuffer();
        getDescriptor(buf, c);
        return buf.toString();
    }

    public static String getMethodDescriptor(Method m) {
        Class[] parameters = m.getParameterTypes();
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (int i = 0; i < parameters.length; i++) {
            getDescriptor(buf, parameters[i]);
        }
        buf.append(')');
        getDescriptor(buf, m.getReturnType());
        return buf.toString();
    }

    private static void getDescriptor(StringBuffer buf, Class<?> c) {
        Class<?> d = c;
        while (true) {
            if (d.isPrimitive()) {
                char car;
                if (d == int.class) {
                    car = 'I';
                } else if (d == void.class) {
                    car = 'V';
                } else if (d == boolean.class) {
                    car = 'Z';
                } else if (d == byte.class) {
                    car = 'B';
                } else if (d == char.class) {
                    car = 'C';
                } else if (d == short.class) {
                    car = 'S';
                } else if (d == double.class) {
                    car = 'D';
                } else if (d == float.class) {
                    car = 'F';
                } else {
                    car = 'J';
                }
                buf.append(car);
                return;
            }
            if (d.isArray()) {
                buf.append('[');
                d = (Class) d.getComponentType();
                continue;
            }
            break;
        }
        buf.append('L');
        String name = d.getName();
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char car = name.charAt(i);
            buf.append((car == '.') ? 47 : car);
        }
        buf.append(';');
    }

    public int getSort() {
        return this.sort;
    }

    public int getDimensions() {
        int i = 1;
        while (this.buf[this.off + i] == '[') {
            i++;
        }
        return i;
    }

    public Type getElementType() {
        return getType(this.buf, this.off + getDimensions());
    }

    public String getClassName() {
        return (new String(this.buf, this.off + 1, this.len - 2)).replace('/', '.');
    }

    public String getInternalName() {
        return new String(this.buf, this.off + 1, this.len - 2);
    }

    public String getDescriptor() {
        StringBuffer buf = new StringBuffer();
        getDescriptor(buf);
        return buf.toString();
    }

    private void getDescriptor(StringBuffer buf) {
        switch (this.sort) {
            case 0:
                buf.append('V');
                return;
            case 1:
                buf.append('Z');
                return;
            case 2:
                buf.append('C');
                return;
            case 3:
                buf.append('B');
                return;
            case 4:
                buf.append('S');
                return;
            case 5:
                buf.append('I');
                return;
            case 6:
                buf.append('F');
                return;
            case 7:
                buf.append('J');
                return;
            case 8:
                buf.append('D');
                return;
        }

        buf.append(this.buf, this.off, this.len);
    }

    public int getSize() {
        return (this.sort == 7 || this.sort == 8) ? 2 : 1;
    }

    public int getOpcode(int opcode) {
        if (opcode == 46 || opcode == 79) {
            switch (this.sort) {
                case 0:
                    return opcode + 5;
                case 1:
                case 3:
                    return opcode + 6;
                case 2:
                    return opcode + 7;
                case 4:
                    return opcode + 8;
                case 5:
                    return opcode;
                case 6:
                    return opcode + 2;
                case 7:
                    return opcode + 1;
                case 8:
                    return opcode + 3;
            }

            return opcode + 4;
        }

        switch (this.sort) {
            case 0:
                return opcode + 5;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return opcode;
            case 6:
                return opcode + 2;
            case 7:
                return opcode + 1;
            case 8:
                return opcode + 3;
        }

        return opcode + 4;
    }
}
