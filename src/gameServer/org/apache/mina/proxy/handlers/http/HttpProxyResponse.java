/*     */ package org.apache.mina.proxy.handlers.http;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class HttpProxyResponse
/*     */ {
/*     */   private final String httpVersion;
/*     */   private final String statusLine;
/*     */   private final int statusCode;
/*     */   private final Map<String, List<String>> headers;
/*     */   private String body;
/*     */   
/*     */   protected HttpProxyResponse(String httpVersion, String statusLine, Map<String, List<String>> headers) {
/*  66 */     this.httpVersion = httpVersion;
/*  67 */     this.statusLine = statusLine;
/*     */ 
/*     */     
/*  70 */     this.statusCode = (statusLine.charAt(0) == ' ') ? Integer.parseInt(statusLine.substring(1, 4)) : Integer.parseInt(statusLine.substring(0, 3));
/*     */ 
/*     */     
/*  73 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getHttpVersion() {
/*  80 */     return this.httpVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getStatusCode() {
/*  87 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getStatusLine() {
/*  94 */     return this.statusLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBody() {
/* 101 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBody(String body) {
/* 108 */     this.body = body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<String, List<String>> getHeaders() {
/* 115 */     return this.headers;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/HttpProxyResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */