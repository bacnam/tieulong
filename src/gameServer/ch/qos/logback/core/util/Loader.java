/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Loader
/*     */ {
/*     */   static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
/*     */   private static boolean ignoreTCL = false;
/*     */   public static final String IGNORE_TCL_PROPERTY_NAME = "logback.ignoreTCL";
/*     */   private static boolean HAS_GET_CLASS_LOADER_PERMISSION = false;
/*     */   
/*     */   static {
/*  39 */     String ignoreTCLProp = OptionHelper.getSystemProperty("logback.ignoreTCL", null);
/*     */ 
/*     */     
/*  42 */     if (ignoreTCLProp != null) {
/*  43 */       ignoreTCL = OptionHelper.toBoolean(ignoreTCLProp, true);
/*     */     }
/*     */     
/*  46 */     HAS_GET_CLASS_LOADER_PERMISSION = ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>()
/*     */         {
/*     */           public Boolean run() {
/*     */             try {
/*  50 */               AccessController.checkPermission(new RuntimePermission("getClassLoader"));
/*     */               
/*  52 */               return Boolean.valueOf(true);
/*  53 */             } catch (SecurityException e) {
/*     */ 
/*     */               
/*  56 */               return Boolean.valueOf(false);
/*     */             } 
/*     */           }
/*     */         })).booleanValue();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<URL> getResourceOccurrenceCount(String resource, ClassLoader classLoader) throws IOException {
/*  75 */     Set<URL> urlSet = new HashSet<URL>();
/*  76 */     Enumeration<URL> urlEnum = classLoader.getResources(resource);
/*  77 */     while (urlEnum.hasMoreElements()) {
/*  78 */       URL url = urlEnum.nextElement();
/*  79 */       urlSet.add(url);
/*     */     } 
/*  81 */     return urlSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(String resource, ClassLoader classLoader) {
/*     */     try {
/*  92 */       return classLoader.getResource(resource);
/*  93 */     } catch (Throwable t) {
/*  94 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResourceBySelfClassLoader(String resource) {
/* 106 */     return getResource(resource, getClassLoaderOfClass(Loader.class));
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
/*     */   public static ClassLoader getTCL() {
/* 119 */     return Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<?> loadClass(String clazz, Context context) throws ClassNotFoundException {
/* 124 */     ClassLoader cl = getClassLoaderOfObject(context);
/* 125 */     return cl.loadClass(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoaderOfObject(Object o) {
/* 136 */     if (o == null) {
/* 137 */       throw new NullPointerException("Argument cannot be null");
/*     */     }
/* 139 */     return getClassLoaderOfClass(o.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoaderAsPrivileged(final Class<?> clazz) {
/* 149 */     if (!HAS_GET_CLASS_LOADER_PERMISSION) {
/* 150 */       return null;
/*     */     }
/* 152 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*     */         {
/*     */           public ClassLoader run() {
/* 155 */             return clazz.getClassLoader();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoaderOfClass(Class<?> clazz) {
/* 168 */     ClassLoader cl = clazz.getClassLoader();
/* 169 */     if (cl == null) {
/* 170 */       return ClassLoader.getSystemClassLoader();
/*     */     }
/* 172 */     return cl;
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
/*     */   public static Class<?> loadClass(String clazz) throws ClassNotFoundException {
/* 184 */     if (ignoreTCL) {
/* 185 */       return Class.forName(clazz);
/*     */     }
/*     */     try {
/* 188 */       return getTCL().loadClass(clazz);
/* 189 */     } catch (Throwable e) {
/*     */ 
/*     */ 
/*     */       
/* 193 */       return Class.forName(clazz);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/util/Loader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */