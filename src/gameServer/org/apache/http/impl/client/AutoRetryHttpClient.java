/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.URI;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EntityUtils;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class AutoRetryHttpClient
/*     */   implements HttpClient
/*     */ {
/*     */   private final HttpClient backend;
/*     */   private final ServiceUnavailableRetryStrategy retryStrategy;
/*  66 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy) {
/*  71 */     Args.notNull(client, "HttpClient");
/*  72 */     Args.notNull(retryStrategy, "ServiceUnavailableRetryStrategy");
/*  73 */     this.backend = client;
/*  74 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient() {
/*  83 */     this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config) {
/*  95 */     this(new DefaultHttpClient(), config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoRetryHttpClient(HttpClient client) {
/* 107 */     this(client, new DefaultServiceUnavailableRetryStrategy());
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
/* 112 */     HttpContext defaultContext = null;
/* 113 */     return execute(target, request, defaultContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 118 */     return execute(target, request, responseHandler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 124 */     HttpResponse resp = execute(target, request, context);
/* 125 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request) throws IOException {
/* 129 */     HttpContext context = null;
/* 130 */     return execute(request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
/* 135 */     URI uri = request.getURI();
/* 136 */     HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */     
/* 138 */     return execute(httpHost, (HttpRequest)request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
/* 143 */     return execute(request, responseHandler, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
/* 149 */     HttpResponse resp = execute(request, context);
/* 150 */     return (T)responseHandler.handleResponse(resp);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
/* 155 */     for (int c = 1;; c++) {
/* 156 */       HttpResponse response = this.backend.execute(target, request, context);
/*     */       try {
/* 158 */         if (this.retryStrategy.retryRequest(response, c, context)) {
/* 159 */           EntityUtils.consume(response.getEntity());
/* 160 */           long nextInterval = this.retryStrategy.getRetryInterval();
/*     */           try {
/* 162 */             this.log.trace("Wait for " + nextInterval);
/* 163 */             Thread.sleep(nextInterval);
/* 164 */           } catch (InterruptedException e) {
/* 165 */             Thread.currentThread().interrupt();
/* 166 */             throw new InterruptedIOException();
/*     */           } 
/*     */         } else {
/* 169 */           return response;
/*     */         } 
/* 171 */       } catch (RuntimeException ex) {
/*     */         try {
/* 173 */           EntityUtils.consume(response.getEntity());
/* 174 */         } catch (IOException ioex) {
/* 175 */           this.log.warn("I/O error consuming response content", ioex);
/*     */         } 
/* 177 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 183 */     return this.backend.getConnectionManager();
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/* 187 */     return this.backend.getParams();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/AutoRetryHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */