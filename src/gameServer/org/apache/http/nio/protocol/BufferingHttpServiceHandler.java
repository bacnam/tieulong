/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
/*     */ import org.apache.http.nio.entity.BufferingNHttpEntity;
/*     */ import org.apache.http.nio.entity.ConsumingNHttpEntity;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpExpectationVerifier;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpRequestHandler;
/*     */ import org.apache.http.protocol.HttpRequestHandlerResolver;
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
/*     */ @Immutable
/*     */ public class BufferingHttpServiceHandler
/*     */   implements NHttpServiceHandler
/*     */ {
/*     */   private final AsyncNHttpServiceHandler asyncHandler;
/*     */   private HttpRequestHandlerResolver handlerResolver;
/*     */   
/*     */   public BufferingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
/*  89 */     this.asyncHandler = new AsyncNHttpServiceHandler(httpProcessor, responseFactory, connStrategy, allocator, params);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.asyncHandler.setHandlerResolver(new RequestHandlerResolverAdaptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, HttpParams params) {
/* 103 */     this(httpProcessor, responseFactory, connStrategy, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEventListener(EventListener eventListener) {
/* 108 */     this.asyncHandler.setEventListener(eventListener);
/*     */   }
/*     */   
/*     */   public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 112 */     this.asyncHandler.setExpectationVerifier(expectationVerifier);
/*     */   }
/*     */   
/*     */   public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
/* 116 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */   
/*     */   public void connected(NHttpServerConnection conn) {
/* 120 */     this.asyncHandler.connected(conn);
/*     */   }
/*     */   
/*     */   public void closed(NHttpServerConnection conn) {
/* 124 */     this.asyncHandler.closed(conn);
/*     */   }
/*     */   
/*     */   public void requestReceived(NHttpServerConnection conn) {
/* 128 */     this.asyncHandler.requestReceived(conn);
/*     */   }
/*     */   
/*     */   public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) {
/* 132 */     this.asyncHandler.inputReady(conn, decoder);
/*     */   }
/*     */   
/*     */   public void responseReady(NHttpServerConnection conn) {
/* 136 */     this.asyncHandler.responseReady(conn);
/*     */   }
/*     */   
/*     */   public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) {
/* 140 */     this.asyncHandler.outputReady(conn, encoder);
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, HttpException httpex) {
/* 144 */     this.asyncHandler.exception(conn, httpex);
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, IOException ioex) {
/* 148 */     this.asyncHandler.exception(conn, ioex);
/*     */   }
/*     */   
/*     */   public void timeout(NHttpServerConnection conn) {
/* 152 */     this.asyncHandler.timeout(conn);
/*     */   }
/*     */   
/*     */   class RequestHandlerResolverAdaptor
/*     */     implements NHttpRequestHandlerResolver {
/*     */     public NHttpRequestHandler lookup(String requestURI) {
/* 158 */       HttpRequestHandler handler = BufferingHttpServiceHandler.this.handlerResolver.lookup(requestURI);
/* 159 */       if (handler != null) {
/* 160 */         return new BufferingHttpServiceHandler.RequestHandlerAdaptor(handler);
/*     */       }
/* 162 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RequestHandlerAdaptor
/*     */     extends SimpleNHttpRequestHandler
/*     */   {
/*     */     private final HttpRequestHandler requestHandler;
/*     */ 
/*     */     
/*     */     public RequestHandlerAdaptor(HttpRequestHandler requestHandler) {
/* 174 */       this.requestHandler = requestHandler;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ConsumingNHttpEntity entityRequest(HttpEntityEnclosingRequest request, HttpContext context) throws HttpException, IOException {
/* 180 */       return (ConsumingNHttpEntity)new BufferingNHttpEntity(request.getEntity(), (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 190 */       this.requestHandler.handle(request, response, context);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/BufferingHttpServiceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */