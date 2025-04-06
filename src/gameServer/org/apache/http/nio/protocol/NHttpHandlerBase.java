/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpProcessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public abstract class NHttpHandlerBase
/*     */ {
/*     */   protected static final String CONN_STATE = "http.nio.conn-state";
/*     */   protected final HttpProcessor httpProcessor;
/*     */   protected final ConnectionReuseStrategy connStrategy;
/*     */   protected final ByteBufferAllocator allocator;
/*     */   protected final HttpParams params;
/*     */   protected EventListener eventListener;
/*     */   
/*     */   public NHttpHandlerBase(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
/*  67 */     Args.notNull(httpProcessor, "HTTP processor");
/*  68 */     Args.notNull(connStrategy, "Connection reuse strategy");
/*  69 */     Args.notNull(allocator, "ByteBuffer allocator");
/*  70 */     Args.notNull(params, "HTTP parameters");
/*  71 */     this.httpProcessor = httpProcessor;
/*  72 */     this.connStrategy = connStrategy;
/*  73 */     this.allocator = allocator;
/*  74 */     this.params = params;
/*     */   }
/*     */   
/*     */   public HttpParams getParams() {
/*  78 */     return this.params;
/*     */   }
/*     */   
/*     */   public void setEventListener(EventListener eventListener) {
/*  82 */     this.eventListener = eventListener;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void closeConnection(NHttpConnection conn, Throwable cause) {
/*     */     try {
/*  88 */       conn.close();
/*  89 */     } catch (IOException ex) {
/*     */       
/*     */       try {
/*  92 */         conn.shutdown();
/*  93 */       } catch (IOException ignore) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void shutdownConnection(NHttpConnection conn, Throwable cause) {
/*     */     try {
/* 100 */       conn.shutdown();
/* 101 */     } catch (IOException ignore) {}
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleTimeout(NHttpConnection conn) {
/*     */     try {
/* 107 */       if (conn.getStatus() == 0) {
/* 108 */         conn.close();
/* 109 */         if (conn.getStatus() == 1)
/*     */         {
/*     */           
/* 112 */           conn.setSocketTimeout(250);
/*     */         }
/* 114 */         if (this.eventListener != null) {
/* 115 */           this.eventListener.connectionTimeout(conn);
/*     */         }
/*     */       } else {
/* 118 */         conn.shutdown();
/*     */       } 
/* 120 */     } catch (IOException ignore) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/* 127 */     if (request != null && "HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     int status = response.getStatusLine().getStatusCode();
/* 132 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/NHttpHandlerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */