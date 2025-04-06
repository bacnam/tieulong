/*     */ package com.zhonglian.server.http.server;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponse
/*     */ {
/*     */   private HttpExchange httpExchange;
/*     */   private boolean responsed = false;
/*     */   
/*     */   public HttpResponse(HttpExchange httpExchange) {
/*  18 */     this.httpExchange = httpExchange;
/*     */   }
/*     */   
/*     */   public void response(String result) {
/*  22 */     response(200, result);
/*     */   }
/*     */   
/*     */   public void response(int code, String result) {
/*  26 */     if (this.responsed) {
/*  27 */       CommLog.error("重复response", new Throwable());
/*     */       return;
/*     */     } 
/*     */     try {
/*  31 */       this.httpExchange.sendResponseHeaders(code, (result.getBytes()).length);
/*  32 */       OutputStream out = this.httpExchange.getResponseBody();
/*  33 */       out.write(result.getBytes());
/*  34 */       out.flush();
/*  35 */       this.httpExchange.close();
/*  36 */       this.responsed = true;
/*  37 */     } catch (IOException e) {
/*  38 */       CommLog.error("回写Http数据response时发生错误", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void response(File file) {
/*  43 */     if (this.responsed) {
/*  44 */       CommLog.error("重复response", new Throwable()); return;
/*     */     } 
/*     */     try {
/*  47 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  56 */     catch (IOException e) {
/*  57 */       CommLog.error("回写Http数据response时发生错误", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void error(int code, String format, Object... param) {
/*  62 */     String msg = String.format(format, param);
/*  63 */     String rep = String.format("{\"state\":%d,\"msg\":\"%s\"}", new Object[] { Integer.valueOf(code), encodeString(msg) });
/*  64 */     response(rep);
/*  65 */     CommLog.error("{}请求处理失败,错误码:{},msg:{}", new Object[] { this.httpExchange.getRequestURI(), Integer.valueOf(code), msg });
/*     */   }
/*     */   
/*     */   private String encodeString(String str) {
/*  69 */     StringBuilder sb = new StringBuilder();
/*  70 */     for (int i = 0; i < str.length(); i++) {
/*  71 */       char ch = str.charAt(i);
/*  72 */       switch (ch) {
/*     */         case '\\':
/*  74 */           sb.append("\\\\");
/*     */           break;
/*     */         case '/':
/*  77 */           sb.append("\\/");
/*     */           break;
/*     */         case '"':
/*  80 */           sb.append("\\\"");
/*     */           break;
/*     */         case '\t':
/*  83 */           sb.append("\\t");
/*     */           break;
/*     */         case '\f':
/*  86 */           sb.append("\\f");
/*     */           break;
/*     */         case '\b':
/*  89 */           sb.append("\\b");
/*     */           break;
/*     */         case '\n':
/*  92 */           sb.append("\\n");
/*     */           break;
/*     */         case '\r':
/*  95 */           sb.append("\\r");
/*     */           break;
/*     */         default:
/*  98 */           sb.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 103 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/HttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */