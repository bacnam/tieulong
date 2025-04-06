/*    */ package com.mchange.v1.lang;
/*    */ 
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.util.HashSet;
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
/*    */ public final class Synchronizer
/*    */ {
/*    */   public static Object createSynchronizedWrapper(final Object o) {
/* 52 */     InvocationHandler invocationHandler = new InvocationHandler()
/*    */       {
/*    */         
/*    */         public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
/*    */         {
/* 57 */           synchronized (param1Object) {
/* 58 */             return param1Method.invoke(o, param1ArrayOfObject);
/*    */           }  }
/*    */       };
/* 61 */     Class<?> clazz = o.getClass();
/* 62 */     return Proxy.newProxyInstance(clazz.getClassLoader(), recurseFindInterfaces(clazz), invocationHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Class[] recurseFindInterfaces(Class paramClass) {
/* 69 */     HashSet<Class<?>> hashSet = new HashSet();
/* 70 */     while (paramClass != null) {
/*    */       
/* 72 */       Class[] arrayOfClass1 = paramClass.getInterfaces(); byte b; int i;
/* 73 */       for (b = 0, i = arrayOfClass1.length; b < i; b++)
/* 74 */         hashSet.add(arrayOfClass1[b]); 
/* 75 */       paramClass = paramClass.getSuperclass();
/*    */     } 
/* 77 */     Class[] arrayOfClass = new Class[hashSet.size()];
/* 78 */     hashSet.toArray((Class<?>[][])arrayOfClass);
/* 79 */     return arrayOfClass;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/Synchronizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */