package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;

import java.lang.reflect.Array;

@GwtCompatible(emulated = true)
class Platform {
    static <T> T[] clone(T[] array) {
        return (T[]) array.clone();
    }

    static void unsafeArrayCopy(Object[] src, int srcPos, Object[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    @GwtIncompatible("Array.newInstance(Class, int)")
    static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    static <T> T[] newArray(T[] reference, int length) {
        Class<?> type = reference.getClass().getComponentType();

        T[] result = (T[]) Array.newInstance(type, length);
        return result;
    }

    static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
}

