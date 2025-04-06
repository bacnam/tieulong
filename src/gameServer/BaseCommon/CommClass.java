/*     */ package BaseCommon;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class CommClass
/*     */ {
/*  22 */   private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */   
/*     */   public static void setClassLoader(ClassLoader classLoader) {
/*  25 */     CommClass.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<Class<?>> getClasses(String pack) {
/*  37 */     Set<Class<?>> classes = new LinkedHashSet<>();
/*     */     
/*  39 */     boolean recursive = true;
/*     */     
/*  41 */     String packageName = pack;
/*  42 */     String packageDirName = packageName.replace('.', '/');
/*     */ 
/*     */     
/*     */     try {
/*  46 */       Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
/*     */       
/*  48 */       while (dirs.hasMoreElements()) {
/*     */         
/*  50 */         URL url = dirs.nextElement();
/*     */         
/*  52 */         String protocol = url.getProtocol();
/*  53 */         if (protocol != null) {
/*     */           String filePath;
/*     */           String str1;
/*  56 */           switch ((str1 = protocol).hashCode()) { case 104987: if (!str1.equals("jar")) {
/*     */                 continue;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  71 */               try { JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
/*     */                 
/*  73 */                 Enumeration<JarEntry> entries = jar.entries();
/*     */                 
/*  75 */                 while (entries.hasMoreElements()) {
/*     */                   
/*  77 */                   JarEntry entry = entries.nextElement();
/*  78 */                   String name = entry.getName();
/*     */                   
/*  80 */                   if (name.charAt(0) == '/')
/*     */                   {
/*  82 */                     name = name.substring(1);
/*     */                   }
/*     */                   
/*  85 */                   if (name.startsWith(packageDirName)) {
/*  86 */                     int idx = name.lastIndexOf('/');
/*     */                     
/*  88 */                     if (idx != -1)
/*     */                     {
/*  90 */                       packageName = name.substring(0, idx).replace('/', '.');
/*     */                     }
/*     */                     
/*  93 */                     if (idx != -1 || recursive)
/*     */                     {
/*  95 */                       if (name.endsWith(".class") && !entry.isDirectory()) {
/*     */                         
/*  97 */                         String className = name.substring(packageName.length() + 1, name.length() - 6);
/*     */                         
/*     */                         try {
/* 100 */                           classes.add(classLoader.loadClass(String.valueOf(packageName) + '.' + className));
/* 101 */                         } catch (ClassNotFoundException e) {
/*     */ 
/*     */                           
/* 104 */                           CommLog.error(CommClass.class.getName(), e);
/*     */                         } 
/*     */                       } 
/*     */                     }
/*     */                   } 
/*     */                 }  }
/* 110 */               catch (IOException e)
/*     */               
/* 112 */               { CommLog.error(CommClass.class.getName(), e); } 
/*     */             case 3143036:
/*     */               if (!str1.equals("file"))
/*     */                 continue;  filePath = URLDecoder.decode(url.getFile(), "UTF-8"); findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes); } 
/*     */         } 
/*     */       } 
/* 118 */     } catch (IOException e) {
/* 119 */       CommLog.error(CommClass.class.getName(), e);
/*     */     } 
/*     */     
/* 122 */     return classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
/* 135 */     File dir = new File(packagePath);
/*     */     
/* 137 */     if (!dir.exists() || !dir.isDirectory()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 142 */     File[] dirfiles = dir.listFiles(new FileFilter()
/*     */         {
/*     */           public boolean accept(File file)
/*     */           {
/* 146 */             return !((!recursive || !file.isDirectory()) && !file.getName().endsWith(".class")); } });
/*     */     byte b;
/*     */     int i;
/*     */     File[] arrayOfFile1;
/* 150 */     for (i = (arrayOfFile1 = dirfiles).length, b = 0; b < i; ) { File file = arrayOfFile1[b];
/*     */       
/* 152 */       if (file.isDirectory()) {
/* 153 */         findAndAddClassesInPackageByFile(String.valueOf(packageName) + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
/*     */       } else {
/*     */         
/* 156 */         String className = file.getName().substring(0, file.getName().length() - 6);
/*     */ 
/*     */         
/*     */         try {
/* 160 */           classes.add(classLoader.loadClass(String.valueOf(packageName) + '.' + className));
/* 161 */         } catch (ClassNotFoundException e) {
/*     */           
/* 163 */           CommLog.error(CommClass.class.getName(), e);
/*     */         } 
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Class<?>> getAllAssignedClass(Class<?> cls) throws IOException, ClassNotFoundException {
/* 178 */     List<Class<?>> classes = new ArrayList<>();
/* 179 */     for (Class<?> c : getClasses(cls)) {
/* 180 */       if (cls.isAssignableFrom(c) && !cls.equals(c)) {
/* 181 */         classes.add(c);
/*     */       }
/*     */     } 
/* 184 */     return classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Class<?>> getClasses(Class<?> cls) throws IOException, ClassNotFoundException {
/* 196 */     String pk = cls.getPackage().getName();
/* 197 */     String path = pk.replace('.', '/');
/* 198 */     ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/* 199 */     URL url = classloader.getResource(path);
/* 200 */     return getClasses(new File(url.getFile()), pk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
/* 212 */     List<Class<?>> classes = new ArrayList<>();
/* 213 */     if (!dir.exists())
/* 214 */       return classes;  byte b; int i;
/*     */     File[] arrayOfFile;
/* 216 */     for (i = (arrayOfFile = dir.listFiles()).length, b = 0; b < i; ) { File f = arrayOfFile[b];
/* 217 */       if (f.isDirectory()) {
/* 218 */         classes.addAll(getClasses(f, String.valueOf(pk) + "." + f.getName()));
/*     */       }
/* 220 */       String name = f.getName();
/* 221 */       if (name.endsWith(".class"))
/* 222 */         classes.add(forName(String.valueOf(pk) + "." + name.substring(0, name.length() - 6))); 
/*     */       b++; }
/*     */     
/* 225 */     return classes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Class> getAllClassByInterface(Class c) {
/* 237 */     List<Class<?>> returnClassList = new ArrayList<>();
/*     */ 
/*     */     
/* 240 */     if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
/* 241 */       String packageName = c.getPackage().getName();
/* 242 */       Set<Class<?>> allClass = getClasses(packageName);
/*     */       
/* 244 */       for (Class<?> cs : allClass) {
/*     */         
/* 246 */         if (!c.isAssignableFrom(cs)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 251 */         if (c.equals(cs)) {
/*     */           continue;
/*     */         }
/*     */         
/* 255 */         returnClassList.add(cs);
/*     */       } 
/*     */     } 
/* 258 */     return returnClassList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Class<?>> getAllClassByInterface(Class<?> c, String packageName) {
/* 270 */     List<Class<?>> returnClassList = new ArrayList<>();
/*     */ 
/*     */     
/* 273 */     if (c.isInterface() || Modifier.isAbstract(c.getModifiers())) {
/* 274 */       Set<Class<?>> allClass = getClasses(packageName);
/* 275 */       for (Class<?> cs : allClass) {
/*     */         
/* 277 */         if (!c.isAssignableFrom(cs)) {
/*     */           continue;
/*     */         }
/*     */         
/* 281 */         if (Modifier.isAbstract(cs.getModifiers())) {
/*     */           continue;
/*     */         }
/*     */         
/* 285 */         if (c.equals(cs)) {
/*     */           continue;
/*     */         }
/* 288 */         returnClassList.add(cs);
/*     */       } 
/*     */     } 
/* 291 */     return returnClassList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String printClassInfo(Object object) {
/* 301 */     StringBuilder sBuilder = new StringBuilder();
/* 302 */     String ent = System.lineSeparator();
/* 303 */     sBuilder.append("output:").append(object.getClass().getSimpleName()).append(ent);
/*     */     
/* 305 */     Field[] fields = object.getClass().getDeclaredFields(); byte b; int i; Field[] arrayOfField1;
/* 306 */     for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) { Field field = arrayOfField1[b];
/*     */       try {
/* 308 */         boolean accessFlag = field.isAccessible();
/* 309 */         field.setAccessible(true);
/* 310 */         String varName = field.getName();
/* 311 */         Object varValue = field.get(object);
/* 312 */         sBuilder.append(String.format("(%s)%s = %s", new Object[] { field.getType().getSimpleName(), varName, varValue })).append(ent);
/* 313 */         field.setAccessible(accessFlag);
/* 314 */       } catch (SecurityException|IllegalArgumentException|IllegalAccessException e) {
/* 315 */         CommLog.error(CommClass.class.getName(), e);
/*     */       } 
/*     */       b++; }
/*     */     
/* 319 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getClassPropertyInfos(Object object) {
/* 323 */     StringBuilder sBuilder = new StringBuilder();
/* 324 */     sBuilder.append("[");
/*     */     
/* 326 */     Field[] fields = object.getClass().getDeclaredFields(); byte b; int i; Field[] arrayOfField1;
/* 327 */     for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) { Field field = arrayOfField1[b];
/*     */       try {
/* 329 */         boolean accessFlag = field.isAccessible();
/* 330 */         field.setAccessible(true);
/* 331 */         String varName = field.getName();
/* 332 */         Object varValue = field.get(object);
/* 333 */         Class<?> type = field.getType();
/* 334 */         if (type.isAssignableFrom(Collection.class.getClass())) {
/* 335 */           Collection<?> lst = (Collection)varValue;
/* 336 */           for (Object objInlist : lst) {
/* 337 */             sBuilder.append(getClassPropertyInfos(objInlist));
/*     */           }
/*     */         } else {
/* 340 */           sBuilder.append(String.format("%s:%s,", new Object[] { varName, varValue }));
/*     */         } 
/*     */         
/* 343 */         field.setAccessible(accessFlag);
/* 344 */       } catch (SecurityException|IllegalArgumentException|IllegalAccessException e) {
/* 345 */         CommLog.error(CommClass.class.getName(), e);
/*     */       }  b++; }
/*     */     
/* 348 */     sBuilder.append("],");
/*     */     
/* 350 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static Class<?> forName(String name) throws ClassNotFoundException {
/* 354 */     return classLoader.loadClass(name);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseCommon/CommClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */