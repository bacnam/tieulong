/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.ResponseHandler;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*     */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ public class DecompressingHttpClient
/*     */   implements HttpClient
/*     */ {
/*     */   private final HttpClient backend;
/*     */   private final HttpRequestInterceptor acceptEncodingInterceptor;
/*     */   private final HttpResponseInterceptor contentEncodingInterceptor;
/*     */   
/*     */   public DecompressingHttpClient() {
/*  89 */     this(new DefaultHttpClient());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecompressingHttpClient(HttpClient backend) {
/*  99 */     this(backend, (HttpRequestInterceptor)new RequestAcceptEncoding(), (HttpResponseInterceptor)new ResponseContentEncoding());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DecompressingHttpClient(HttpClient backend, HttpRequestInterceptor requestInterceptor, HttpResponseInterceptor responseInterceptor) {
/* 105 */     this.backend = backend;
/* 106 */     this.acceptEncodingInterceptor = requestInterceptor;
/* 107 */     this.contentEncodingInterceptor = responseInterceptor;
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/* 111 */     return this.backend.getParams();
/*     */   }
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 115 */     return this.backend.getConnectionManager();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
/* 120 */     return execute(getHttpHost(request), (HttpRequest)request, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getHttpClient() {
/* 129 */     return this.backend;
/*     */   }
/*     */   
/*     */   HttpHost getHttpHost(HttpUriRequest request) {
/* 133 */     URI uri = request.getURI();
/* 134 */     return URIUtils.extractHost(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
/* 139 */     return execute(getHttpHost(request), (HttpRequest)request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
/* 144 */     return execute(target, request, (HttpContext)null);
/*     */   }
/*     */   
/*     */   public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*     */     try {
/*     */       RequestWrapper requestWrapper;
/* 150 */       HttpContext localContext = (context != null) ? context : (HttpContext)new BasicHttpContext();
/*     */       
/* 152 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 153 */         requestWrapper = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*     */       } else {
/* 155 */         requestWrapper = new RequestWrapper(request);
/*     */       } 
/* 157 */       this.acceptEncodingInterceptor.process((HttpRequest)requestWrapper, localContext);
/* 158 */       HttpResponse response = this.backend.execute(target, (HttpRequest)requestWrapper, localContext);
/*     */       try {
/* 160 */         this.contentEncodingInterceptor.process(response, localContext);
/* 161 */         if (Boolean.TRUE.equals(localContext.getAttribute("http.client.response.uncompressed"))) {
/* 162 */           response.removeHeaders("Content-Length");
/* 163 */           response.removeHeaders("Content-Encoding");
/* 164 */           response.removeHeaders("Content-MD5");
/*     */         } 
/* 166 */         return response;
/* 167 */       } catch (HttpException ex) {
/* 168 */         EntityUtils.consume(response.getEntity());
/* 169 */         throw ex;
/* 170 */       } catch (IOException ex) {
/* 171 */         EntityUtils.consume(response.getEntity());
/* 172 */         throw ex;
/* 173 */       } catch (RuntimeException ex) {
/* 174 */         EntityUtils.consume(response.getEntity());
/* 175 */         throw ex;
/*     */       } 
/* 177 */     } catch (HttpException e) {
/* 178 */       throw new ClientProtocolException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 185 */     return execute(getHttpHost(request), (HttpRequest)request, responseHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/* 191 */     return execute(getHttpHost(request), (HttpRequest)request, responseHandler, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
/* 197 */     return execute(target, request, responseHandler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
/* 203 */     HttpResponse response = execute(target, request, context);
/*     */     try {
/* 205 */       return (T)responseHandler.handleResponse(response);
/*     */     } finally {
/* 207 */       HttpEntity entity = response.getEntity();
/* 208 */       if (entity != null)
/* 209 */         EntityUtils.consume(entity); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/DecompressingHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */