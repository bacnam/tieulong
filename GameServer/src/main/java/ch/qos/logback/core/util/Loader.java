package ch.qos.logback.core.util;

import ch.qos.logback.core.Context;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Loader {
    public static final String IGNORE_TCL_PROPERTY_NAME = "logback.ignoreTCL";
    static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    private static boolean ignoreTCL = false;
    private static boolean HAS_GET_CLASS_LOADER_PERMISSION = false;

    static {
        String ignoreTCLProp = OptionHelper.getSystemProperty("logback.ignoreTCL", null);

        if (ignoreTCLProp != null) {
            ignoreTCL = OptionHelper.toBoolean(ignoreTCLProp, true);
        }

        HAS_GET_CLASS_LOADER_PERMISSION = ((Boolean) AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                try {
                    AccessController.checkPermission(new RuntimePermission("getClassLoader"));

                    return Boolean.valueOf(true);
                } catch (SecurityException e) {

                    return Boolean.valueOf(false);
                }
            }
        })).booleanValue();
    }

    public static Set<URL> getResourceOccurrenceCount(String resource, ClassLoader classLoader) throws IOException {
        Set<URL> urlSet = new HashSet<URL>();
        Enumeration<URL> urlEnum = classLoader.getResources(resource);
        while (urlEnum.hasMoreElements()) {
            URL url = urlEnum.nextElement();
            urlSet.add(url);
        }
        return urlSet;
    }

    public static URL getResource(String resource, ClassLoader classLoader) {
        try {
            return classLoader.getResource(resource);
        } catch (Throwable t) {
            return null;
        }
    }

    public static URL getResourceBySelfClassLoader(String resource) {
        return getResource(resource, getClassLoaderOfClass(Loader.class));
    }

    public static ClassLoader getTCL() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String clazz, Context context) throws ClassNotFoundException {
        ClassLoader cl = getClassLoaderOfObject(context);
        return cl.loadClass(clazz);
    }

    public static ClassLoader getClassLoaderOfObject(Object o) {
        if (o == null) {
            throw new NullPointerException("Argument cannot be null");
        }
        return getClassLoaderOfClass(o.getClass());
    }

    public static ClassLoader getClassLoaderAsPrivileged(final Class<?> clazz) {
        if (!HAS_GET_CLASS_LOADER_PERMISSION) {
            return null;
        }
        return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }

    public static ClassLoader getClassLoaderOfClass(Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return cl;
    }

    public static Class<?> loadClass(String clazz) throws ClassNotFoundException {
        if (ignoreTCL) {
            return Class.forName(clazz);
        }
        try {
            return getTCL().loadClass(clazz);
        } catch (Throwable e) {

            return Class.forName(clazz);
        }
    }
}

