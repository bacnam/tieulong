package com.mchange.v2.lang;

public final class ObjectUtils {
    public static boolean eqOrBothNull(Object paramObject1, Object paramObject2) {
        if (paramObject1 == paramObject2)
            return true;
        if (paramObject1 == null) {
            return false;
        }
        return paramObject1.equals(paramObject2);
    }

    public static int hashOrZero(Object paramObject) {
        return (paramObject == null) ? 0 : paramObject.hashCode();
    }
}

