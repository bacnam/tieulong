/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import ch.qos.logback.core.util.Loader;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnvUtil
/*    */ {
/* 33 */   static ClassLoader testServiceLoaderClassLoader = null;
/*    */   
/*    */   public static boolean isGroovyAvailable() {
/* 36 */     ClassLoader classLoader = Loader.getClassLoaderOfClass(EnvUtil.class);
/*    */     try {
/* 38 */       Class<?> bindingClass = classLoader.loadClass("groovy.lang.Binding");
/* 39 */       return (bindingClass != null);
/* 40 */     } catch (ClassNotFoundException e) {
/* 41 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Method serviceLoaderLoadMethod;
/*    */   
/*    */   private static final Method serviceLoaderIteratorMethod;
/*    */ 
/*    */   
/*    */   static {
/* 53 */     Method tLoadMethod = null;
/* 54 */     Method tIteratorMethod = null;
/*    */     try {
/* 56 */       Class<?> clazz = Class.forName("java.util.ServiceLoader");
/* 57 */       tLoadMethod = clazz.getMethod("load", new Class[] { Class.class, ClassLoader.class });
/* 58 */       tIteratorMethod = clazz.getMethod("iterator", new Class[0]);
/* 59 */     } catch (ClassNotFoundException ce) {
/*    */     
/* 61 */     } catch (NoSuchMethodException ne) {}
/*    */ 
/*    */     
/* 64 */     serviceLoaderLoadMethod = tLoadMethod;
/* 65 */     serviceLoaderIteratorMethod = tIteratorMethod;
/*    */   }
/*    */   
/*    */   public static boolean isServiceLoaderAvailable() {
/* 69 */     return (serviceLoaderLoadMethod != null && serviceLoaderIteratorMethod != null);
/*    */   }
/*    */   
/*    */   private static ClassLoader getServiceLoaderClassLoader() {
/* 73 */     return (testServiceLoaderClassLoader == null) ? Loader.getClassLoaderOfClass(EnvUtil.class) : testServiceLoaderClassLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> T loadFromServiceLoader(Class<T> c) {
/* 78 */     if (isServiceLoaderAvailable()) {
/*    */       Object loader; Iterator<T> it;
/*    */       try {
/* 81 */         loader = serviceLoaderLoadMethod.invoke(null, new Object[] { c, getServiceLoaderClassLoader() });
/* 82 */       } catch (Exception e) {
/* 83 */         throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#load()", e);
/*    */       } 
/*    */ 
/*    */       
/*    */       try {
/* 88 */         it = (Iterator<T>)serviceLoaderIteratorMethod.invoke(loader, new Object[0]);
/* 89 */       } catch (Exception e) {
/* 90 */         throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#iterator()", e);
/*    */       } 
/* 92 */       if (it.hasNext())
/* 93 */         return it.next(); 
/*    */     } 
/* 95 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/util/EnvUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */