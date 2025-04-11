package com.mchange.v1.util;

import com.mchange.v2.lang.ObjectUtils;

public final class ArrayUtils {
    public static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
        byte b;
        int i;
        for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
            if (paramObject.equals(paramArrayOfObject[b])) return b;
        }
        return -1;
    }

    public static int identityIndexOf(Object[] paramArrayOfObject, Object paramObject) {
        byte b;
        int i;
        for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
            if (paramObject == paramArrayOfObject[b]) return b;
        }
        return -1;
    }

    public static boolean startsWith(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
        int i = paramArrayOfbyte1.length;
        int j = paramArrayOfbyte2.length;
        if (i < j)
            return false;
        for (byte b = 0; b < j; b++) {
            if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
                return false;
        }
        return true;
    }

    public static int hashArray(Object[] paramArrayOfObject) {
        int i = paramArrayOfObject.length;
        int j = i;
        for (byte b = 0; b < i; b++) {

            int k = ObjectUtils.hashOrZero(paramArrayOfObject[b]);
            int m = b % 32;
            int n = k >>> m;
            n |= k << 32 - m;
            j ^= n;
        }
        return j;
    }

    public static int hashArray(int[] paramArrayOfint) {
        int i = paramArrayOfint.length;
        int j = i;
        for (byte b = 0; b < i; b++) {

            int k = paramArrayOfint[b];
            int m = b % 32;
            int n = k >>> m;
            n |= k << 32 - m;
            j ^= n;
        }
        return j;
    }

    public static int hashOrZeroArray(Object[] paramArrayOfObject) {
        return (paramArrayOfObject == null) ? 0 : hashArray(paramArrayOfObject);
    }

    public static int hashOrZeroArray(int[] paramArrayOfint) {
        return (paramArrayOfint == null) ? 0 : hashArray(paramArrayOfint);
    }

    public static String stringifyContents(Object[] paramArrayOfObject) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[ ");
        byte b;
        int i;
        for (b = 0, i = paramArrayOfObject.length; b < i; b++) {

            if (b != 0)
                stringBuffer.append(", ");
            stringBuffer.append(paramArrayOfObject[b].toString());
        }
        stringBuffer.append(" ]");
        return stringBuffer.toString();
    }

    private static String toString(String[] paramArrayOfString, int paramInt) {
        StringBuffer stringBuffer = new StringBuffer(paramInt);
        boolean bool = true;
        stringBuffer.append('[');
        byte b;
        int i;
        for (b = 0, i = paramArrayOfString.length; b < i; b++) {

            if (bool) {
                bool = false;
            } else {
                stringBuffer.append(',');
            }
            stringBuffer.append(paramArrayOfString[b]);
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    public static String toString(boolean[] paramArrayOfboolean) {
        String[] arrayOfString = new String[paramArrayOfboolean.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfboolean.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfboolean[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfboolean.length + 1);
    }

    public static String toString(byte[] paramArrayOfbyte) {
        String[] arrayOfString = new String[paramArrayOfbyte.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfbyte.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfbyte[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfbyte.length + 1);
    }

    public static String toString(char[] paramArrayOfchar) {
        String[] arrayOfString = new String[paramArrayOfchar.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfchar.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfchar[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfchar.length + 1);
    }

    public static String toString(short[] paramArrayOfshort) {
        String[] arrayOfString = new String[paramArrayOfshort.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfshort.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfshort[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfshort.length + 1);
    }

    public static String toString(int[] paramArrayOfint) {
        String[] arrayOfString = new String[paramArrayOfint.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfint.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfint[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfint.length + 1);
    }

    public static String toString(long[] paramArrayOflong) {
        String[] arrayOfString = new String[paramArrayOflong.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOflong.length; b < j; b++) {

            String str = String.valueOf(paramArrayOflong[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOflong.length + 1);
    }

    public static String toString(float[] paramArrayOffloat) {
        String[] arrayOfString = new String[paramArrayOffloat.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOffloat.length; b < j; b++) {

            String str = String.valueOf(paramArrayOffloat[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOffloat.length + 1);
    }

    public static String toString(double[] paramArrayOfdouble) {
        String[] arrayOfString = new String[paramArrayOfdouble.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfdouble.length; b < j; b++) {

            String str = String.valueOf(paramArrayOfdouble[b]);
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfdouble.length + 1);
    }

    public static String toString(Object[] paramArrayOfObject) {
        String[] arrayOfString = new String[paramArrayOfObject.length];
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfObject.length; b < j; b++) {
            String str;

            Object object = paramArrayOfObject[b];
            if (object instanceof Object[]) {
                str = toString((Object[]) object);
            } else if (object instanceof double[]) {
                str = toString((double[]) object);
            } else if (object instanceof float[]) {
                str = toString((float[]) object);
            } else if (object instanceof long[]) {
                str = toString((long[]) object);
            } else if (object instanceof int[]) {
                str = toString((int[]) object);
            } else if (object instanceof short[]) {
                str = toString((short[]) object);
            } else if (object instanceof char[]) {
                str = toString((char[]) object);
            } else if (object instanceof byte[]) {
                str = toString((byte[]) object);
            } else if (object instanceof boolean[]) {
                str = toString((boolean[]) object);
            } else {
                str = String.valueOf(paramArrayOfObject[b]);
            }
            i += str.length();
            arrayOfString[b] = str;
        }
        return toString(arrayOfString, i + paramArrayOfObject.length + 1);
    }
}

