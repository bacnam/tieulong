package BaseCommon;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        String packageDirName = pack.replace('.', '/');

        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if (protocol != null) {
                    switch (protocol.hashCode()) {
                        case 104987:
                            if (!protocol.equals("jar")) {
                                break;
                            }

                            try {
                                JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                                Enumeration<JarEntry> entries = jar.entries();

                                while (entries.hasMoreElements()) {
                                    JarEntry entry = entries.nextElement();
                                    String name = entry.getName();
                                    if (name.charAt(0) == '/') {
                                        name = name.substring(1);
                                    }

                                    if (name.startsWith(packageDirName)) {
                                        int idx = name.lastIndexOf(47);
                                        if (idx != -1) {
                                            packageName = name.substring(0, idx).replace('/', '.');
                                        }

                                        if ((idx != -1 || recursive) && name.endsWith(".class") && !entry.isDirectory()) {
                                            String className = name.substring(packageName.length() + 1, name.length() - 6);

                                            try {
                                                classes.add(classLoader.loadClass(packageName + '.' + className));
                                            } catch (ClassNotFoundException var17) {
                                                CommLog.error(CommClass.class.getName(), (Throwable)var17);
                                            }
                                        }
                                    }
                                }
                            } catch (IOException var18) {
                                CommLog.error(CommClass.class.getName(), (Throwable)var18);
                            }
                            break;
                        case 3143036:
                            if (protocol.equals("file")) {
                                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                            }
                    }
                }
            }
        } catch (IOException var19) {
            CommLog.error(CommClass.class.getName(), (Throwable)var19);
        }

        return classes;
    }

    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] dirfiles = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return recursive && file.isDirectory() || file.getName().endsWith(".class");
                }
            });

            for (File file : dirfiles) {
                if (file.isDirectory()) {
                    findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);

                    try {
                        classes.add(classLoader.loadClass(packageName + '.' + className));
                    } catch (ClassNotFoundException var12) {
                        CommLog.error(CommClass.class.getName(), (Throwable)var12);
                    }
                }
            }
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
        if (!dir.exists()) {
            return classes;
        } else {
            File[] var6;
            for (File f : var6 = dir.listFiles()) {
                if (f.isDirectory()) {
                    classes.addAll(getClasses(f, pk + "." + f.getName()));
                }

                String name = f.getName();
                if (name.endsWith(".class")) {
                    classes.add(forName(pk + "." + name.substring(0, name.length() - 6)));
                }
            }

            return classes;
        }
    }

    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> returnClassList = new ArrayList<>();
        if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
            String packageName = c.getPackage().getName();

            for (Class<?> cs : getClasses(packageName)) {
                if (c.isAssignableFrom(cs) && !c.equals(cs)) {
                    returnClassList.add(cs);
                }
            }
        }

        return returnClassList;
    }

    public static List<Class<?>> getAllClassByInterface(Class<?> c, String packageName) {
        List<Class<?>> returnClassList = new ArrayList<>();
        if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
            for (Class<?> cs : getClasses(packageName)) {
                if (c.isAssignableFrom(cs) && !Modifier.isAbstract(cs.getModifiers()) && !c.equals(cs)) {
                    returnClassList.add(cs);
                }
            }
        }

        return returnClassList;
    }

    public static String printClassInfo(Object object) {
        StringBuilder sBuilder = new StringBuilder();
        String ent = System.lineSeparator();
        sBuilder.append("output:").append(object.getClass().getSimpleName()).append(ent);
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                String varName = field.getName();
                Object varValue = field.get(object);
                sBuilder.append(String.format("(%s)%s = %s", field.getType().getSimpleName(), varName, varValue)).append(ent);
                field.setAccessible(accessFlag);
            } catch (IllegalArgumentException | IllegalAccessException | SecurityException var11) {
                CommLog.error(CommClass.class.getName(), (Throwable)var11);
            }
        }

        return sBuilder.toString();
    }

    public static String getClassPropertyInfos(Object object) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                String varName = field.getName();
                Object varValue = field.get(object);
                Class<?> type = field.getType();
                if (type.isAssignableFrom(Collection.class.getClass())) {
                    for (Object objInlist : (Collection)varValue) {
                        sBuilder.append(getClassPropertyInfos(objInlist));
                    }
                } else {
                    sBuilder.append(String.format("%s:%s,", varName, varValue));
                }

                field.setAccessible(accessFlag);
            } catch (IllegalArgumentException | IllegalAccessException | SecurityException var14) {
                CommLog.error(CommClass.class.getName(), (Throwable)var14);
            }
        }

        sBuilder.append("],");
        return sBuilder.toString();
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        return classLoader.loadClass(name);
    }
}
