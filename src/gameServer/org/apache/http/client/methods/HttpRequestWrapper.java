/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ @NotThreadSafe
/*     */ public class HttpRequestWrapper
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest
/*     */ {
/*     */   private final HttpRequest original;
/*     */   private final HttpHost target;
/*     */   private final String method;
/*     */   private ProtocolVersion version;
/*     */   private URI uri;
/*     */   
/*     */   private HttpRequestWrapper(HttpRequest request, HttpHost target) {
/*  64 */     this.original = (HttpRequest)Args.notNull(request, "HTTP request");
/*  65 */     this.target = target;
/*  66 */     this.version = this.original.getRequestLine().getProtocolVersion();
/*  67 */     this.method = this.original.getRequestLine().getMethod();
/*  68 */     if (request instanceof HttpUriRequest) {
/*  69 */       this.uri = ((HttpUriRequest)request).getURI();
/*     */     } else {
/*  71 */       this.uri = null;
/*     */     } 
/*  73 */     setHeaders(request.getAllHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  78 */     return (this.version != null) ? this.version : this.original.getProtocolVersion();
/*     */   }
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/*  82 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  87 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/*  91 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethod() {
/*  96 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public void abort() throws UnsupportedOperationException {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAborted() {
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 111 */     String requestUri = null;
/* 112 */     if (this.uri != null) {
/* 113 */       requestUri = this.uri.toASCIIString();
/*     */     } else {
/* 115 */       requestUri = this.original.getRequestLine().getUri();
/*     */     } 
/* 117 */     if (requestUri == null || requestUri.isEmpty()) {
/* 118 */       requestUri = "/";
/*     */     }
/* 120 */     return (RequestLine)new BasicRequestLine(this.method, requestUri, getProtocolVersion());
/*     */   }
/*     */   
/*     */   public HttpRequest getOriginal() {
/* 124 */     return this.original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost getTarget() {
/* 131 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return getRequestLine() + " " + this.headergroup;
/*     */   }
/*     */   
/*     */   static class HttpEntityEnclosingRequestWrapper
/*     */     extends HttpRequestWrapper
/*     */     implements HttpEntityEnclosingRequest {
/*     */     private HttpEntity entity;
/*     */     
/*     */     HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request, HttpHost target) {
/* 145 */       super((HttpRequest)request, target);
/* 146 */       this.entity = request.getEntity();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpEntity getEntity() {
/* 151 */       return this.entity;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setEntity(HttpEntity entity) {
/* 156 */       this.entity = entity;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean expectContinue() {
/* 161 */       Header expect = getFirstHeader("Expect");
/* 162 */       return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRequestWrapper wrap(HttpRequest request) {
/* 174 */     return wrap(request, (HttpHost)null);
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
/*     */   
/*     */   public static HttpRequestWrapper wrap(HttpRequest request, HttpHost target) {
/* 187 */     Args.notNull(request, "HTTP request");
/* 188 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 189 */       return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request, target);
/*     */     }
/* 191 */     return new HttpRequestWrapper(request, target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getParams() {
/* 202 */     if (this.params == null) {
/* 203 */       this.params = this.original.getParams().copy();
/*     */     }
/* 205 */     return this.params;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/methods/HttpRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */