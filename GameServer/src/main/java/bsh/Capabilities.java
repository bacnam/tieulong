package bsh;

import java.util.Hashtable;

public class Capabilities {
    private static boolean accessibility = false;
    private static Hashtable classes = new Hashtable<Object, Object>();

    public static boolean haveSwing() {
        return classExists("javax.swing.JButton");
    }

    public static boolean canGenerateInterfaces() {
        return classExists("java.lang.reflect.Proxy");
    }

    public static boolean haveAccessibility() {
        return accessibility;
    }

    public static void setAccessibility(boolean b) throws Unavailable {
        if (!b) {

            accessibility = false;

            return;
        }
        if (!classExists("java.lang.reflect.AccessibleObject") || !classExists("bsh.reflect.ReflectManagerImpl")) {

            throw new Unavailable("Accessibility unavailable");
        }

        try {
            String.class.getDeclaredMethods();
        } catch (SecurityException e) {
            throw new Unavailable("Accessibility unavailable: " + e);
        }

        accessibility = true;
    }

    public static boolean classExists(String name) {
        Object c = classes.get(name);

        if (c == null) {
            try {
                c = Class.forName(name);
            } catch (ClassNotFoundException e) {
                // Có thể log nếu cần
            }

            if (c != null) {
                classes.put(c, "unused");
            }
        }

        return (c != null);

    }

    public static class Unavailable
            extends UtilEvalError {
        public Unavailable(String s) {
            super(s);
        }
    }
}
