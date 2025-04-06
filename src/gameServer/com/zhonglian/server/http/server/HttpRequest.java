/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URI;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpRequest
/*    */ {
/*    */   private HttpExchange httpExchange;
/* 19 */   private Map<String, String> params = null;
/* 20 */   private Map<String, List<String>> headMap = new HashMap<>();
/* 21 */   private String requestBody = "";
/*    */   
/*    */   public HttpRequest(HttpExchange httpExchange) {
/* 24 */     this.httpExchange = httpExchange;
/* 25 */     this.headMap.putAll(httpExchange.getRequestHeaders());
/* 26 */     this.params = HttpUtils.abstractHttpParams(getReuestURI().getQuery());
/* 27 */     initRequestBody();
/*    */   }
/*    */   
/*    */   public URI getReuestURI() {
/* 31 */     return this.httpExchange.getRequestURI();
/*    */   }
/*    */   
/*    */   public String getParam(String key) {
/* 35 */     return this.params.get(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHeader(String key) {
/* 44 */     List<String> head = this.httpExchange.getRequestHeaders().get(key);
/* 45 */     if (head == null || head.size() == 0) {
/* 46 */       return null;
/*    */     }
/* 48 */     return head.get(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getHeaderList(String key) {
/* 57 */     return this.httpExchange.getRequestHeaders().get(key);
/*    */   }
/*    */   
/*    */   private void initRequestBody() {
/* 61 */     InputStream in = this.httpExchange.getRequestBody();
/* 62 */     StringBuilder builder = new StringBuilder();
/* 63 */     BufferedReader reader = null;
/*    */     try {
/* 65 */       String line = null;
/* 66 */       reader = new BufferedReader(new InputStreamReader(in));
/* 67 */       while ((line = reader.readLine()) != null) {
/* 68 */         builder.append(line);
/*    */       }
/* 70 */     } catch (IOException e) {
/* 71 */       CommLog.error("[HttpRequest]解析http协议body发生错误");
/*    */     } finally {
/* 73 */       if (reader != null) {
/*    */         try {
/* 75 */           reader.close();
/* 76 */         } catch (IOException e) {
/* 77 */           CommLog.error("[HttpRequest]解析http关闭输入流时错误");
/*    */         } 
/*    */       }
/*    */     } 
/* 81 */     this.requestBody = builder.toString();
/*    */   }
/*    */   
/*    */   public String getRequestBody() {
/* 85 */     return this.requestBody;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/HttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */