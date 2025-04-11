package com.mchange.v2.reflect;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class ReflectUtils {
    public static final Class[] PROXY_CTOR_ARGS = new Class[]{InvocationHandler.class};

    public static Constructor findProxyConstructor(ClassLoader paramClassLoader, Class paramClass) throws NoSuchMethodException {
        return findProxyConstructor(paramClassLoader, new Class[]{paramClass});
    }

    public static Constructor findProxyConstructor(ClassLoader paramClassLoader, Class[] paramArrayOfClass) throws NoSuchMethodException {
        Class<?> clazz = Proxy.getProxyClass(paramClassLoader, paramArrayOfClass);
        return clazz.getConstructor(PROXY_CTOR_ARGS);
    }

    public static boolean isPublic(Member paramMember) {
        return ((paramMember.getModifiers() & 0x1) != 0);
    }

    public static boolean isPublic(Class paramClass) {
        return ((paramClass.getModifiers() & 0x1) != 0);
    }

    public static Class findPublicParent(Class paramClass) {
        do {
            paramClass = paramClass.getSuperclass();
        } while (paramClass != null && !isPublic(paramClass));
        return paramClass;
    }

    public static Iterator traverseInterfaces(Class<?> paramClass) {
        HashSet<Class<?>> hashSet = new HashSet();
        if (paramClass.isInterface()) hashSet.add(paramClass);
        addParentInterfaces(hashSet, paramClass);
        return hashSet.iterator();
    }

    private static void addParentInterfaces(Set<Class<?>> paramSet, Class paramClass) {
        Class[] arrayOfClass = paramClass.getInterfaces();
        byte b;
        int i;
        for (b = 0, i = arrayOfClass.length; b < i; b++) {

            paramSet.add(arrayOfClass[b]);
            addParentInterfaces(paramSet, arrayOfClass[b]);
        }
    }

    public static Method findInPublicScope(Method paramMethod) {
        if (!isPublic(paramMethod))
            return null;
        Class<?> clazz1 = paramMethod.getDeclaringClass();
        if (isPublic(clazz1)) {
            return paramMethod;
        }

        Class<?> clazz2 = clazz1;
        while ((clazz2 = findPublicParent(clazz2)) != null) {

            try {
                return clazz2.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
            } catch (NoSuchMethodException noSuchMethodException) {
            }
        }

        Iterator<Class<?>> iterator = traverseInterfaces(clazz1);
        while (iterator.hasNext()) {

            clazz2 = iterator.next();
            if (isPublic(clazz2)) {

                try {
                    return clazz2.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
                } catch (NoSuchMethodException noSuchMethodException) {
                }
            }
        }

        return null;
    }
}

