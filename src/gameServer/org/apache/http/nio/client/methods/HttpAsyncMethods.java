/*     */ package org.apache.http.nio.client.methods;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpDelete;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.entity.HttpAsyncContentProducer;
/*     */ import org.apache.http.nio.entity.NByteArrayEntity;
/*     */ import org.apache.http.nio.entity.NStringEntity;
/*     */ import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpAsyncMethods
/*     */ {
/*     */   public static HttpAsyncRequestProducer create(HttpHost target, HttpRequest request) {
/*  71 */     Args.notNull(target, "HTTP host");
/*  72 */     Args.notNull(request, "HTTP request");
/*  73 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer create(HttpUriRequest request) {
/*  83 */     Args.notNull(request, "HTTP request");
/*  84 */     HttpHost target = URIUtils.extractHost(request.getURI());
/*  85 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpRequest)request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createGet(URI requestURI) {
/*  95 */     return create((HttpUriRequest)new HttpGet(requestURI));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createGet(String requestURI) {
/* 105 */     return create((HttpUriRequest)new HttpGet(URI.create(requestURI)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createHead(URI requestURI) {
/* 115 */     return create((HttpUriRequest)new HttpGet(requestURI));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createHead(String requestURI) {
/* 125 */     return create((HttpUriRequest)new HttpGet(URI.create(requestURI)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createDelete(URI requestURI) {
/* 135 */     return create((HttpUriRequest)new HttpDelete(requestURI));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createDelete(String requestURI) {
/* 145 */     return create((HttpUriRequest)new HttpDelete(URI.create(requestURI)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createOptions(URI requestURI) {
/* 155 */     return create((HttpUriRequest)new HttpOptions(requestURI));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createOptions(String requestURI) {
/* 165 */     return create((HttpUriRequest)new HttpOptions(URI.create(requestURI)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createTrace(URI requestURI) {
/* 175 */     return create((HttpUriRequest)new HttpTrace(requestURI));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createTrace(String requestURI) {
/* 185 */     return create((HttpUriRequest)new HttpTrace(URI.create(requestURI)));
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPost(URI requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
/* 200 */     HttpPost httppost = new HttpPost(requestURI);
/* 201 */     NStringEntity entity = new NStringEntity(content, contentType);
/* 202 */     httppost.setEntity((HttpEntity)entity);
/* 203 */     HttpHost target = URIUtils.extractHost(requestURI);
/* 204 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httppost, (HttpAsyncContentProducer)entity);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPost(String requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
/* 219 */     return createPost(URI.create(requestURI), content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPost(URI requestURI, byte[] content, ContentType contentType) {
/* 234 */     HttpPost httppost = new HttpPost(requestURI);
/* 235 */     NByteArrayEntity entity = new NByteArrayEntity(content, contentType);
/* 236 */     HttpHost target = URIUtils.extractHost(requestURI);
/* 237 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httppost, (HttpAsyncContentProducer)entity);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPost(String requestURI, byte[] content, ContentType contentType) {
/* 252 */     return createPost(URI.create(requestURI), content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPut(URI requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
/* 267 */     HttpPut httpput = new HttpPut(requestURI);
/* 268 */     NStringEntity entity = new NStringEntity(content, contentType);
/* 269 */     httpput.setEntity((HttpEntity)entity);
/* 270 */     HttpHost target = URIUtils.extractHost(requestURI);
/* 271 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httpput, (HttpAsyncContentProducer)entity);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPut(String requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
/* 286 */     return createPut(URI.create(requestURI), content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPut(URI requestURI, byte[] content, ContentType contentType) {
/* 301 */     HttpPut httpput = new HttpPut(requestURI);
/* 302 */     NByteArrayEntity entity = new NByteArrayEntity(content, contentType);
/* 303 */     HttpHost target = URIUtils.extractHost(requestURI);
/* 304 */     return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httpput, (HttpAsyncContentProducer)entity);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createPut(String requestURI, byte[] content, ContentType contentType) {
/* 319 */     return createPut(URI.create(requestURI), content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createZeroCopyPost(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 334 */     return new ZeroCopyPost(requestURI, content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createZeroCopyPost(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 349 */     return new ZeroCopyPost(URI.create(requestURI), content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createZeroCopyPut(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 364 */     return new ZeroCopyPut(requestURI, content, contentType);
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
/*     */ 
/*     */   
/*     */   public static HttpAsyncRequestProducer createZeroCopyPut(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
/* 379 */     return new ZeroCopyPut(URI.create(requestURI), content, contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncResponseConsumer<HttpResponse> createConsumer() {
/* 387 */     return (HttpAsyncResponseConsumer<HttpResponse>)new BasicAsyncResponseConsumer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpAsyncResponseConsumer<HttpResponse> createZeroCopyConsumer(File file) throws FileNotFoundException {
/* 397 */     return (HttpAsyncResponseConsumer<HttpResponse>)new ZeroCopyConsumer<HttpResponse>(file)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         protected HttpResponse process(HttpResponse response, File file, ContentType contentType)
/*     */         {
/* 404 */           return response;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class RequestProducerImpl
/*     */     extends BasicAsyncRequestProducer
/*     */   {
/*     */     protected RequestProducerImpl(HttpHost target, HttpEntityEnclosingRequest request, HttpAsyncContentProducer producer) {
/* 416 */       super(target, request, producer);
/*     */     }
/*     */     
/*     */     public RequestProducerImpl(HttpHost target, HttpRequest request) {
/* 420 */       super(target, request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/HttpAsyncMethods.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */