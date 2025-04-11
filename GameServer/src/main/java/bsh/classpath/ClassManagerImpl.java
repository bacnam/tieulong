package bsh.classpath;

import bsh.BshClassManager;
import bsh.ClassPathException;
import bsh.Interpreter;
import bsh.InterpreterError;
import bsh.UtilEvalError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class ClassManagerImpl
        extends BshClassManager {
    static final String BSH_PACKAGE = "bsh";
    private BshClassPath baseClassPath;
    private boolean superImport;
    private BshClassPath fullClassPath;
    private Vector listeners = new Vector();
    private ReferenceQueue refQueue = new ReferenceQueue();

    private BshClassLoader baseLoader;

    private Map loaderMap;

    public ClassManagerImpl() {
        reset();
    }

    public Class classForName(String name) {
        Class<?> c = (Class) this.absoluteClassCache.get(name);
        if (c != null) {
            return c;
        }

        if (this.absoluteNonClasses.get(name) != null) {
            if (Interpreter.DEBUG)
                Interpreter.debug("absoluteNonClass list hit: " + name);
            return null;
        }

        if (Interpreter.DEBUG) {
            Interpreter.debug("Trying to load class: " + name);
        }

        ClassLoader overlayLoader = getLoaderForClass(name);
        if (overlayLoader != null) {

            try {
                c = overlayLoader.loadClass(name);
            } catch (Exception e) {

            } catch (NoClassDefFoundError e2) {
                throw noClassDefFound(name, e2);
            }
        }

        if (c == null &&
                name.startsWith("bsh")) {
            try {
                c = Interpreter.class.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        if (c == null &&
                this.baseLoader != null) {
            try {
                c = this.baseLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        if (c == null &&
                this.externalClassLoader != null) {
            try {
                c = this.externalClassLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        if (c == null) {

            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

                if (contextClassLoader != null)
                    c = Class.forName(name, true, contextClassLoader);
            } catch (ClassNotFoundException e) {
            } catch (SecurityException e) {
            }
        }

        if (c == null) {
            try {
                c = plainClassForName(name);
            } catch (ClassNotFoundException e) {
            }
        }

        if (c == null) {
            c = loadSourceClass(name);
        }

        cacheClassInfo(name, c);

        return c;
    }

    public URL getResource(String path) {
        URL url = null;
        if (this.baseLoader != null) {
            url = this.baseLoader.getResource(path.substring(1));
        }
        if (url == null)
            url = super.getResource(path);
        return url;
    }

    public InputStream getResourceAsStream(String path) {
        InputStream in = null;
        if (this.baseLoader != null) {

            in = this.baseLoader.getResourceAsStream(path.substring(1));
        }
        if (in == null) {
            in = super.getResourceAsStream(path);
        }
        return in;
    }

    ClassLoader getLoaderForClass(String name) {
        return (ClassLoader) this.loaderMap.get(name);
    }

    public void addClassPath(URL path) throws IOException {
        if (this.baseLoader == null) {
            setClassPath(new URL[] { path });
        } else {

            this.baseLoader.addURL(path);
            this.baseClassPath.add(path);
            classLoaderChanged();
        }
    }

    public void reset() {
        this.baseClassPath = new BshClassPath("baseClassPath");
        this.baseLoader = null;
        this.loaderMap = new HashMap<Object, Object>();
        classLoaderChanged();
    }

    public void setClassPath(URL[] cp) {
        this.baseClassPath.setPath(cp);
        initBaseLoader();
        this.loaderMap = new HashMap<Object, Object>();
        classLoaderChanged();
    }

    public void reloadAllClasses() throws ClassPathException {
        BshClassPath bcp = new BshClassPath("temp");
        bcp.addComponent(this.baseClassPath);
        bcp.addComponent(BshClassPath.getUserClassPath());
        setClassPath(bcp.getPathComponents());
    }

    private void initBaseLoader() {
        this.baseLoader = new BshClassLoader(this, this.baseClassPath);
    }

    public void reloadClasses(String[] classNames) throws ClassPathException {
        if (this.baseLoader == null) {
            initBaseLoader();
        }
        DiscreteFilesClassLoader.ClassSourceMap map = new DiscreteFilesClassLoader.ClassSourceMap();

        for (int i = 0; i < classNames.length; i++) {
            String name = classNames[i];

            BshClassPath.ClassSource classSource = this.baseClassPath.getClassSource(name);

            if (classSource == null) {
                BshClassPath.getUserClassPath().insureInitialized();
                classSource = BshClassPath.getUserClassPath().getClassSource(name);
            }

            if (classSource == null) {
                throw new ClassPathException("Nothing known about class: " + name);
            }

            if (classSource instanceof BshClassPath.JarClassSource) {
                throw new ClassPathException("Cannot reload class: " + name + " from source: " + classSource);
            }

            map.put(name, classSource);
        }

        ClassLoader cl = new DiscreteFilesClassLoader(this, map);

        Iterator<?> it = map.keySet().iterator();
        while (it.hasNext()) {
            this.loaderMap.put((String) it.next(), cl);
        }
        classLoaderChanged();
    }

    public void reloadPackage(String pack) throws ClassPathException {
        Collection classes = this.baseClassPath.getClassesForPackage(pack);

        if (classes == null) {
            classes = BshClassPath.getUserClassPath().getClassesForPackage(pack);
        }

        if (classes == null) {
            throw new ClassPathException("No classes found for package: " + pack);
        }
        reloadClasses((String[]) classes.toArray((Object[]) new String[0]));
    }

    public BshClassPath getClassPath() throws ClassPathException {
        if (this.fullClassPath != null) {
            return this.fullClassPath;
        }
        this.fullClassPath = new BshClassPath("BeanShell Full Class Path");
        this.fullClassPath.addComponent(BshClassPath.getUserClassPath());
        try {
            this.fullClassPath.addComponent(BshClassPath.getBootClassPath());
        } catch (ClassPathException e) {
            System.err.println("Warning: can't get boot class path");
        }
        this.fullClassPath.addComponent(this.baseClassPath);

        return this.fullClassPath;
    }

    public void doSuperImport() throws UtilEvalError {
        try {
            getClassPath().insureInitialized();

            getClassNameByUnqName("");

        } catch (ClassPathException e) {
            throw new UtilEvalError("Error importing classpath " + e);
        }

        this.superImport = true;
    }

    protected boolean hasSuperImport() {
        return this.superImport;
    }

    public String getClassNameByUnqName(String name) throws ClassPathException {
        return getClassPath().getClassNameByUnqName(name);
    }

    public void addListener(BshClassManager.Listener l) {
        this.listeners.addElement(new WeakReference<BshClassManager.Listener>(l, this.refQueue));

        Reference deadref;

        while ((deadref = this.refQueue.poll()) != null) {
            boolean ok = this.listeners.removeElement(deadref);
            if (ok) {
                continue;
            }
            if (Interpreter.DEBUG)
                Interpreter.debug("tried to remove non-existent weak ref: " + deadref);

        }
    }

    public void removeListener(BshClassManager.Listener l) {
        throw new Error("unimplemented");
    }

    public ClassLoader getBaseLoader() {
        return this.baseLoader;
    }

    public Class defineClass(String name, byte[] code) {
        this.baseClassPath.setClassSource(name, new BshClassPath.GeneratedClassSource(code));
        try {
            reloadClasses(new String[] { name });
        } catch (ClassPathException e) {
            throw new InterpreterError("defineClass: " + e);
        }
        return classForName(name);
    }

    protected void classLoaderChanged() {
        clearCaches();

        Vector<WeakReference<BshClassManager.Listener>> toRemove = new Vector();
        for (Enumeration<WeakReference> enumeration = this.listeners.elements(); enumeration.hasMoreElements();) {

            WeakReference<BshClassManager.Listener> wr = enumeration.nextElement();
            BshClassManager.Listener l = wr.get();
            if (l == null) {
                toRemove.add(wr);
                continue;
            }
            l.classLoaderChanged();
        }
        for (Enumeration<WeakReference<BshClassManager.Listener>> e = toRemove.elements(); e.hasMoreElements();) {
            this.listeners.removeElement(e.nextElement());
        }
    }

    public void dump(PrintWriter i) {
        i.println("Bsh Class Manager Dump: ");
        i.println("----------------------- ");
        i.println("baseLoader = " + this.baseLoader);
        i.println("loaderMap= " + this.loaderMap);
        i.println("----------------------- ");
        i.println("baseClassPath = " + this.baseClassPath);
    }
}
