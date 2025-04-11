package javolution.lang;

import javolution.context.LogContext;

import java.lang.reflect.Field;
import java.util.Vector;

public class Initializer {
    public static final Configurable<Boolean> SHOW_INITIALIZED = new Configurable<Boolean>() {
        protected Boolean getDefault() {
            return Boolean.valueOf(false);
        }
    };

    private final ClassLoader classLoader;

    public Initializer(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Class<?>[] loadedClasses() {
        Class<?> cls = this.classLoader.getClass();
        while (cls != ClassLoader.class) {
            cls = cls.getSuperclass();
        }
        try {
            Field fldClasses = cls.getDeclaredField("classes");

            fldClasses.setAccessible(true);
            Vector<Class<?>> list = (Vector<Class<?>>) fldClasses.get(this.classLoader);

            Class<?>[] classes = new Class[list.size()];
            for (int i = 0; i < classes.length; i++) {
                classes[i] = list.get(i);
            }
            return classes;
        } catch (Throwable e) {
            return null;
        }
    }

    public void loadClass(Class<?> cls) {
        try {
            this.classLoader.loadClass(cls.getName());
        } catch (ClassNotFoundException e) {
            LogContext.debug(new Object[]{"Class " + cls + " not found."});
        }
    }

    public boolean initializeLoadedClasses() {
        boolean isInitializationSuccessful = true;
        int nbrClassesInitialized = 0;
        while (true) {
            Class<?>[] classes = loadedClasses();
            if (classes == null) {
                LogContext.debug(new Object[]{"Automatic class initialization not supported."});

                return false;
            }
            if (nbrClassesInitialized >= classes.length)
                break;
            for (int i = nbrClassesInitialized; i < classes.length; i++) {
                Class<?> cls = classes[i];
                try {
                    if (((Boolean) SHOW_INITIALIZED.get()).booleanValue())
                        LogContext.debug(new Object[]{"Initialize ", cls.getName()});
                    Class.forName(cls.getName(), true, this.classLoader);
                } catch (ClassNotFoundException ex) {
                    isInitializationSuccessful = false;
                    LogContext.error(new Object[]{ex});
                }
            }
            nbrClassesInitialized = classes.length;
        }
        LogContext.debug(new Object[]{"Initialization of ", Integer.valueOf(nbrClassesInitialized), " classes loaded by ", this.classLoader});

        return isInitializationSuccessful;
    }
}

