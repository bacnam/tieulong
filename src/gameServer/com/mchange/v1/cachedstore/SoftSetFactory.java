/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.util.Set;
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
/*    */ public final class SoftSetFactory
/*    */ {
/*    */   public static Set createSynchronousCleanupSoftSet() {
/* 45 */     final ManualCleanupSoftSet inner = new ManualCleanupSoftSet();
/* 46 */     InvocationHandler invocationHandler = new InvocationHandler()
/*    */       {
/*    */         
/*    */         public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable
/*    */         {
/* 51 */           inner.vacuum();
/* 52 */           return param1Method.invoke(inner, param1ArrayOfObject);
/*    */         }
/*    */       };
/* 55 */     return (Set)Proxy.newProxyInstance(SoftSetFactory.class.getClassLoader(), new Class[] { Set.class }, invocationHandler);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/SoftSetFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */