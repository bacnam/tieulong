package BaseCommon;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommClass {
    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static void setClassLoader(ClassLoader classLoader) {
        CommClass.classLoader = classLoader;
    }

    public static Set<Class<?>> getClasses(String pack) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        boolean recursive = true;

        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');

        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {

                URL url = dirs.nextElement();

                String protocol = url.getProtocol();
                if (protocol != null) {
                    String filePath;
                    String str1;
                    switch ((str1 = protocol).hashCode()) {
                        case 104987:
                            if (!str1.equals("jar")) {
                                continue;
                            }

                            try {
                                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();

                                Enumeration<JarEntry> entries = jar.entries();

                                while (entries.hasMoreElements()) {

                                    JarEntry entry = entries.nextElement();
                                    String name = entry.getName();

                                    if (name.charAt(0) == '/') {
                                        name = name.substring(1);
                                    }

                                    if (name.startsWith(packageDirName)) {
                                        int idx = name.lastIndexOf('/');

                                        if (idx != -1) {
                                            packageName = name.substring(0, idx).replace('/', '.');
                                        }

                                        if (idx != -1 || recursive) {
                                            if (name.endsWith(".class") && !entry.isDirectory()) {

                                                String className = name.substring(packageName.length() + 1, name.length() - 6);

                                                try {
                                                    classes.add(classLoader.loadClass(String.valueOf(packageName) + '.' + className));
                                                } catch (ClassNotFoundException e) {

                                                    CommLog.error(CommClass.class.getName(), e);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                CommLog.error(CommClass.class.getName(), e);
                            }
                        case 3143036:
                            if (!str1.equals("file"))
                                continue;
                            filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                            findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                    }
                }
            }
        } catch (IOException e) {
            CommLog.error(CommClass.class.getName(), e);
        }

        return classes;
    }

    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return !((!recursive || !file.isDirectory()) && !file.getName().endsWith(".class"));
            }
        });
        byte b;
        int i;
        File[] arrayOfFile1;
        for (i = (arrayOfFile1 = dirfiles).length, b = 0; b < i; ) {
            File file = arrayOfFile1[b];

            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(String.valueOf(packageName) + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {

                String className = file.getName().substring(0, file.getName().length() - 6);

                try {
                    classes.add(classLoader.loadClass(String.valueOf(packageName) + '.' + className));
                } catch (ClassNotFoundException e) {

                    CommLog.error(CommClass.class.getName(), e);
                }
            }
            b++;
        }

    }

    public static List<Class<?>> getAllAssignedClass(Class<?> cls) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    public static List<Class<?>> getClasses(Class<?> cls) throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists())
            return classes;
        byte b;
        int i;
        File[] arrayOfFile;
        for (i = (arrayOfFile = dir.listFiles()).length, b = 0; b < i; ) {
            File f = arrayOfFile[b];
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, String.valueOf(pk) + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class"))
                classes.add(forName(String.valueOf(pk) + "." + name.substring(0, name.length() - 6)));
            b++;
        }

        return classes;
    }

    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> returnClassList = new ArrayList<>();

        if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
            String packageName = c.getPackage().getName();
            Set<Class<?>> allClass = getClasses(packageName);

            for (Class<?> cs : allClass) {

                if (!c.isAssignableFrom(cs)) {
                    continue;
                }

                if (c.equals(cs)) {
                    continue;
                }

                returnClassList.add(cs);
            }
        }

        return returnClassList;
    }

    public static List<Class<?>> getAllClassByInterface(Class<?> c, String packageName) {
        List<Class<?>> returnClassList = new ArrayList<>();

        if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
            Set<Class<?>> allClass = getClasses(packageName);
            for (Class<?> cs : allClass) {

                if (!c.isAssignableFrom(cs)) {
                    continue;
                }

                if (Modifier.isAbstract(cs.getModifiers())) {
                    continue;
                }

                if (c.equals(cs)) {
                    continue;
                }
                returnClassList.add(cs);
            }
        }
        return returnClassList;
    }

    public static String printClassInfo(Object object) {
        StringBuilder sBuilder = new StringBuilder();
        String ent = System.lineSeparator();
        sBuilder.append("output:").append(object.getClass().getSimpleName()).append(ent);

        Field[] fields = object.getClass().getDeclaredFields();
        byte b;
        int i;
        Field[] arrayOfField1;
        for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) {
            Field field = arrayOfField1[b];
            try {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                String varName = field.getName();
                Object varValue = field.get(object);
                sBuilder.append(String.format("(%s)%s = %s", new Object[]{field.getType().getSimpleName(), varName, varValue})).append(ent);
                field.setAccessible(accessFlag);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                CommLog.error(CommClass.class.getName(), e);
            }
            b++;
        }

        return sBuilder.toString();
    }

    public static String getClassPropertyInfos(Object object) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        Field[] fields = object.getClass().getDeclaredFields();
        byte b;
        int i;
        Field[] arrayOfField1;
        for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) {
            Field field = arrayOfField1[b];
            try {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                String varName = field.getName();
                Object varValue = field.get(object);
                Class<?> type = field.getType();
                if (type.isAssignableFrom(Collection.class.getClass())) {
                    Collection<?> lst = (Collection) varValue;
                    for (Object objInlist : lst) {
                        sBuilder.append(getClassPropertyInfos(objInlist));
                    }
                } else {
                    sBuilder.append(String.format("%s:%s,", new Object[]{varName, varValue}));
                }

                field.setAccessible(accessFlag);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                CommLog.error(CommClass.class.getName(), e);
            }
            b++;
        }

        sBuilder.append("],");

        return sBuilder.toString();
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        return classLoader.loadClass(name);
    }
}

