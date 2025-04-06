/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class HttpAdaperter
/*    */ {
/*  8 */   private Object instance = null;
/*  9 */   private Method method = null;
/*    */ 
/*    */   
/*    */   public HttpAdaperter(Object instance, Method method) {
/* 13 */     this.instance = instance;
/* 14 */     this.method = method;
/*    */   }
/*    */   
/*    */   public void invoke(HttpRequest request, HttpResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
/* 18 */     this.method.invoke(this.instance, new Object[] { request, response });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/HttpAdaperter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */