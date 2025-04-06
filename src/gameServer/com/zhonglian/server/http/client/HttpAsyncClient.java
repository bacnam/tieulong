/*    */ package com.zhonglian.server.http.client;
/*    */ 
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.client.config.RequestConfig;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpPost;
/*    */ import org.apache.http.client.methods.HttpRequestBase;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.concurrent.FutureCallback;
/*    */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*    */ import org.apache.http.impl.nio.client.HttpAsyncClients;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpAsyncClient
/*    */ {
/* 19 */   static CloseableHttpAsyncClient httpclient = null;
/*    */   
/*    */   public static void init() {
/* 22 */     if (httpclient == null) {
/* 23 */       RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
/* 24 */       httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
/* 25 */       httpclient.start();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void start(HttpRequestBase request, FutureCallback<HttpResponse> response) {
/* 30 */     init();
/* 31 */     httpclient.execute((HttpUriRequest)request, response);
/*    */   }
/*    */   
/*    */   public static void startHttpGet(String request, FutureCallback<HttpResponse> response) {
/* 35 */     init();
/* 36 */     httpclient.execute((HttpUriRequest)new HttpGet(request), response);
/*    */   }
/*    */   
/*    */   public static void startHttpGet(String request, IResponseHandler response) {
/* 40 */     init();
/* 41 */     httpclient.execute((HttpUriRequest)new HttpGet(request), response);
/*    */   }
/*    */   
/*    */   public static void startHttpPost(HttpPost httpRequest, IResponseHandler response) {
/* 45 */     init();
/* 46 */     httpclient.execute((HttpUriRequest)httpRequest, response);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/client/HttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */