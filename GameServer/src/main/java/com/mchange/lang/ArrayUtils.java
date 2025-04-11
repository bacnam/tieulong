package com.mchange.lang;

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

    public static int hashAll(Object[] paramArrayOfObject) {
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfObject.length; b < j; b++) {

            Object object = paramArrayOfObject[b];
            if (object != null) i ^= object.hashCode();
        }
        return i;
    }

    public static int hashAll(int[] paramArrayOfint) {
        int i = 0;
        byte b;
        int j;
        for (b = 0, j = paramArrayOfint.length; b < j; b++)
            i ^= paramArrayOfint[b];
        return i;
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
}

