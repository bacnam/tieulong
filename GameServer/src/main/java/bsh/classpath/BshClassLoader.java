package bsh.classpath;

import bsh.BshClassManager;
import bsh.Interpreter;

import java.net.URL;
import java.net.URLClassLoader;

public class BshClassLoader
        extends URLClassLoader {
    BshClassManager classManager;

    public BshClassLoader(BshClassManager classManager, URL[] bases) {
        super(bases);
        this.classManager = classManager;
    }

    public BshClassLoader(BshClassManager classManager, BshClassPath bcp) {
        this(classManager, bcp.getPathComponents());
    }

    protected BshClassLoader(BshClassManager classManager) {
        this(classManager, new URL[0]);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = null;

        c = findLoadedClass(name);
        if (c != null) {
            return c;
        }

        if (name.startsWith("bsh")) {
            try {
                return Interpreter.class.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        try {
            c = findClass(name);
        } catch (ClassNotFoundException e) {
        }

        if (c == null) {
            throw new ClassNotFoundException("here in loaClass");
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        ClassManagerImpl bcm = (ClassManagerImpl) getClassManager();

        ClassLoader cl = bcm.getLoaderForClass(name);

        if (cl != null && cl != this) {
            try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("Designated loader could not find class: " + e);
            }
        }

        if ((getURLs()).length > 0) {
            try {
                return super.findClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        cl = bcm.getBaseLoader();

        if (cl != null && cl != this) {
            try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        return bcm.plainClassForName(name);
    }

    BshClassManager getClassManager() {
        return this.classManager;
    }
}

