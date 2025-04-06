/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import com.sun.net.httpserver.HttpHandler;
/*    */ import com.zhonglian.server.http.annotation.RequestMapping;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class HttpDispather
/*    */   implements HttpHandler
/*    */ {
/* 19 */   private Map<HttpMethod, MethodAdapater> methodAdapaters = new HashMap<>();
/*    */   
/*    */   public void init(String pack) throws Exception {
/* 22 */     Set<Class<?>> dealers = CommClass.getClasses(pack);
/*    */     
/* 24 */     for (Class<?> cs : dealers) {
/* 25 */       Object instance = null; byte b; int i; Method[] arrayOfMethod;
/* 26 */       for (i = (arrayOfMethod = cs.getMethods()).length, b = 0; b < i; ) { Method method = arrayOfMethod[b];
/* 27 */         RequestMapping mapping = method.<RequestMapping>getAnnotation(RequestMapping.class);
/* 28 */         if (mapping != null) {
/*    */ 
/*    */           
/* 31 */           Class[] params = method.getParameterTypes();
/* 32 */           if (params.length != 2) {
/* 33 */             throw new IllegalArgumentException("[" + method.getName() + "]不是固定2个参数");
/*    */           }
/* 35 */           if (params[0] != HttpRequest.class) {
/* 36 */             throw new IllegalArgumentException("[" + method.getName() + "]第一个参数不是HttpRequest");
/*    */           }
/* 38 */           if (params[1] != HttpResponse.class) {
/* 39 */             throw new IllegalArgumentException("[" + method.getName() + "]第二个参数不是HttpResponse");
/*    */           }
/* 41 */           if (instance == null)
/* 42 */             instance = CommClass.forName(cs.getName()).newInstance();  byte b1; int j;
/*    */           HttpMethod[] arrayOfHttpMethod;
/* 44 */           for (j = (arrayOfHttpMethod = mapping.method()).length, b1 = 0; b1 < j; ) { HttpMethod httpMethod = arrayOfHttpMethod[b1];
/* 45 */             if (this.methodAdapaters.get(httpMethod) == null) {
/* 46 */               this.methodAdapaters.put(httpMethod, new MethodAdapater());
/*    */             }
/* 48 */             ((MethodAdapater)this.methodAdapaters.get(httpMethod)).addAdapter(mapping.uri(), new HttpAdaperter(instance, method));
/*    */             b1++; }
/*    */         
/*    */         } 
/*    */         b++; }
/*    */     
/*    */     } 
/*    */   } public void handle(HttpExchange exchange) throws IOException {
/* 56 */     HttpResponse response = new HttpResponse(exchange);
/* 57 */     SyncTaskManager.task(() -> {
/*    */           try {
/*    */             HttpMethod method = null;
/*    */             try {
/*    */               String methodname = paramHttpExchange.getRequestMethod().trim().toUpperCase();
/*    */               method = HttpMethod.valueOf(methodname);
/* 63 */             } catch (Exception e) {
/*    */               method = HttpMethod.GET;
/*    */             } 
/*    */             MethodAdapater methodAdapater = this.methodAdapaters.get(method);
/*    */             if (methodAdapater == null) {
/*    */               paramHttpResponse.response(404, "File Not Found");
/*    */               return;
/*    */             } 
/*    */             HttpAdaperter adaperter = methodAdapater.getAdaperter(paramHttpExchange.getRequestURI().getPath());
/*    */             if (adaperter == null) {
/*    */               paramHttpResponse.response(404, "File Not Found");
/*    */               return;
/*    */             } 
/*    */             HttpRequest request = new HttpRequest(paramHttpExchange);
/*    */             CommLog.info("[HttpRequest]url:{},Body:{}", request.getReuestURI(), request.getRequestBody());
/*    */             adaperter.invoke(request, paramHttpResponse);
/* 79 */           } catch (Exception e) {
/*    */             Throwable cause = e.getCause();
/*    */             if (cause != null && cause instanceof RequestException) {
/*    */               RequestException re = (RequestException)cause;
/*    */               paramHttpResponse.error(re.getCode(), re.getMessage(), new Object[0]);
/*    */             } else {
/*    */               CommLog.error("{}请求处理失败,异常：", paramHttpExchange.getRequestURI().getPath(), cause);
/*    */               paramHttpResponse.error(300001, "服务器发生未知错误，错误信息：%s", new Object[] { e.getMessage() });
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/HttpDispather.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */