package ch.qos.logback.classic.util;

import ch.qos.logback.core.util.Loader;

import java.lang.reflect.Method;
import java.util.Iterator;

public class EnvUtil {
    private static final Method serviceLoaderLoadMethod;
    private static final Method serviceLoaderIteratorMethod;
    static ClassLoader testServiceLoaderClassLoader = null;

    static {
        Method tLoadMethod = null;
        Method tIteratorMethod = null;
        try {
            Class<?> clazz = Class.forName("java.util.ServiceLoader");
            tLoadMethod = clazz.getMethod("load", new Class[]{Class.class, ClassLoader.class});
            tIteratorMethod = clazz.getMethod("iterator", new Class[0]);
        } catch (ClassNotFoundException ce) {

        } catch (NoSuchMethodException ne) {
        }

        serviceLoaderLoadMethod = tLoadMethod;
        serviceLoaderIteratorMethod = tIteratorMethod;
    }

    public static boolean isGroovyAvailable() {
        ClassLoader classLoader = Loader.getClassLoaderOfClass(EnvUtil.class);
        try {
            Class<?> bindingClass = classLoader.loadClass("groovy.lang.Binding");
            return (bindingClass != null);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isServiceLoaderAvailable() {
        return (serviceLoaderLoadMethod != null && serviceLoaderIteratorMethod != null);
    }

    private static ClassLoader getServiceLoaderClassLoader() {
        return (testServiceLoaderClassLoader == null) ? Loader.getClassLoaderOfClass(EnvUtil.class) : testServiceLoaderClassLoader;
    }

    public static <T> T loadFromServiceLoader(Class<T> c) {
        if (isServiceLoaderAvailable()) {
            Object loader;
            Iterator<T> it;
            try {
                loader = serviceLoaderLoadMethod.invoke(null, new Object[]{c, getServiceLoaderClassLoader()});
            } catch (Exception e) {
                throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#load()", e);
            }

            try {
                it = (Iterator<T>) serviceLoaderIteratorMethod.invoke(loader, new Object[0]);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#iterator()", e);
            }
            if (it.hasNext())
                return it.next();
        }
        return null;
    }
}

