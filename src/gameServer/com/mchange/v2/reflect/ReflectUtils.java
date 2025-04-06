/*     */ package com.mchange.v2.reflect;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public final class ReflectUtils
/*     */ {
/*  43 */   public static final Class[] PROXY_CTOR_ARGS = new Class[] { InvocationHandler.class };
/*     */ 
/*     */   
/*     */   public static Constructor findProxyConstructor(ClassLoader paramClassLoader, Class paramClass) throws NoSuchMethodException {
/*  47 */     return findProxyConstructor(paramClassLoader, new Class[] { paramClass });
/*     */   }
/*     */ 
/*     */   
/*     */   public static Constructor findProxyConstructor(ClassLoader paramClassLoader, Class[] paramArrayOfClass) throws NoSuchMethodException {
/*  52 */     Class<?> clazz = Proxy.getProxyClass(paramClassLoader, paramArrayOfClass);
/*  53 */     return clazz.getConstructor(PROXY_CTOR_ARGS);
/*     */   }
/*     */   
/*     */   public static boolean isPublic(Member paramMember) {
/*  57 */     return ((paramMember.getModifiers() & 0x1) != 0);
/*     */   }
/*     */   public static boolean isPublic(Class paramClass) {
/*  60 */     return ((paramClass.getModifiers() & 0x1) != 0);
/*     */   }
/*     */   public static Class findPublicParent(Class paramClass) {
/*     */     do {
/*  64 */       paramClass = paramClass.getSuperclass();
/*  65 */     } while (paramClass != null && !isPublic(paramClass));
/*  66 */     return paramClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterator traverseInterfaces(Class<?> paramClass) {
/*  71 */     HashSet<Class<?>> hashSet = new HashSet();
/*  72 */     if (paramClass.isInterface()) hashSet.add(paramClass); 
/*  73 */     addParentInterfaces(hashSet, paramClass);
/*  74 */     return hashSet.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addParentInterfaces(Set<Class<?>> paramSet, Class paramClass) {
/*  79 */     Class[] arrayOfClass = paramClass.getInterfaces(); byte b; int i;
/*  80 */     for (b = 0, i = arrayOfClass.length; b < i; b++) {
/*     */       
/*  82 */       paramSet.add(arrayOfClass[b]);
/*  83 */       addParentInterfaces(paramSet, arrayOfClass[b]);
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
/*     */   public static Method findInPublicScope(Method paramMethod) {
/*  95 */     if (!isPublic(paramMethod))
/*  96 */       return null; 
/*  97 */     Class<?> clazz1 = paramMethod.getDeclaringClass();
/*  98 */     if (isPublic(clazz1)) {
/*  99 */       return paramMethod;
/*     */     }
/*     */     
/* 102 */     Class<?> clazz2 = clazz1;
/* 103 */     while ((clazz2 = findPublicParent(clazz2)) != null) {
/*     */       
/*     */       try {
/* 106 */         return clazz2.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
/* 107 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*     */     } 
/*     */ 
/*     */     
/* 111 */     Iterator<Class<?>> iterator = traverseInterfaces(clazz1);
/* 112 */     while (iterator.hasNext()) {
/*     */       
/* 114 */       clazz2 = iterator.next();
/* 115 */       if (isPublic(clazz2)) {
/*     */         
/*     */         try {
/* 118 */           return clazz2.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
/* 119 */         } catch (NoSuchMethodException noSuchMethodException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 124 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/reflect/ReflectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */