/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MinimalClientExec
/*     */   implements InternalClientExec
/*     */ {
/*  57 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   private final ConnectionReuseStrategy connReuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepaliveStrategy;
/*     */ 
/*     */   
/*     */   public MinimalClientExec(NHttpClientConnectionManager connmgr, HttpProcessor httpProcessor, ConnectionReuseStrategy connReuseStrategy, ConnectionKeepAliveStrategy keepaliveStrategy) {
/*  70 */     this.connmgr = connmgr;
/*  71 */     this.httpProcessor = httpProcessor;
/*  72 */     this.connReuseStrategy = connReuseStrategy;
/*  73 */     this.keepaliveStrategy = keepaliveStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(InternalState state, HttpHost target, HttpRequest original) throws HttpException, IOException {
/*  80 */     if (this.log.isDebugEnabled()) {
/*  81 */       this.log.debug("[exchange: " + state.getId() + "] start execution");
/*     */     }
/*     */     
/*  84 */     HttpClientContext localContext = state.getLocalContext();
/*     */     
/*  86 */     if (original instanceof Configurable) {
/*  87 */       RequestConfig config = ((Configurable)original).getConfig();
/*  88 */       if (config != null) {
/*  89 */         localContext.setRequestConfig(config);
/*     */       }
/*     */     } 
/*     */     
/*  93 */     HttpRequestWrapper request = HttpRequestWrapper.wrap(original);
/*  94 */     HttpRoute route = new HttpRoute(target);
/*  95 */     state.setRoute(route);
/*  96 */     state.setMainRequest(request);
/*  97 */     state.setCurrentRequest(request);
/*     */     
/*  99 */     localContext.setAttribute("http.request", request);
/* 100 */     localContext.setAttribute("http.target_host", target);
/* 101 */     localContext.setAttribute("http.route", route);
/* 102 */     this.httpProcessor.process((HttpRequest)request, (HttpContext)localContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest generateRequest(InternalState state, InternalConnManager connManager) throws IOException, HttpException {
/* 108 */     HttpClientContext localContext = state.getLocalContext();
/* 109 */     HttpRoute route = state.getRoute();
/* 110 */     NHttpClientConnection localConn = connManager.getConnection();
/* 111 */     if (!this.connmgr.isRouteComplete(localConn)) {
/* 112 */       this.connmgr.startRoute(localConn, route, (HttpContext)localContext);
/* 113 */       this.connmgr.routeComplete(localConn, route, (HttpContext)localContext);
/*     */     } 
/* 115 */     localContext.setAttribute("http.connection", localConn);
/* 116 */     RequestConfig config = localContext.getRequestConfig();
/* 117 */     if (config.getSocketTimeout() > 0) {
/* 118 */       localConn.setSocketTimeout(config.getSocketTimeout());
/*     */     }
/* 120 */     return (HttpRequest)state.getCurrentRequest();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(InternalState state, ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 127 */     if (this.log.isDebugEnabled()) {
/* 128 */       this.log.debug("[exchange: " + state.getId() + "] produce content");
/*     */     }
/* 130 */     HttpAsyncRequestProducer requestProducer = state.getRequestProducer();
/* 131 */     state.setRequestContentProduced();
/* 132 */     requestProducer.produceContent(encoder, ioctrl);
/* 133 */     if (encoder.isCompleted()) {
/* 134 */       requestProducer.resetRequest();
/*     */     }
/*     */   }
/*     */   
/*     */   public void requestCompleted(InternalState state) {
/* 139 */     if (this.log.isDebugEnabled()) {
/* 140 */       this.log.debug("[exchange: " + state.getId() + "] Request completed");
/*     */     }
/* 142 */     HttpClientContext localContext = state.getLocalContext();
/* 143 */     HttpAsyncRequestProducer requestProducer = state.getRequestProducer();
/* 144 */     requestProducer.requestCompleted((HttpContext)localContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseReceived(InternalState state, HttpResponse response) throws IOException, HttpException {
/* 150 */     if (this.log.isDebugEnabled()) {
/* 151 */       this.log.debug("[exchange: " + state.getId() + "] Response received " + response.getStatusLine());
/*     */     }
/* 153 */     HttpClientContext localContext = state.getLocalContext();
/* 154 */     localContext.setAttribute("http.response", response);
/* 155 */     this.httpProcessor.process(response, (HttpContext)localContext);
/* 156 */     state.setFinalResponse(response);
/*     */     
/* 158 */     HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 159 */     responseConsumer.responseReceived(response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeContent(InternalState state, ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 166 */     if (this.log.isDebugEnabled()) {
/* 167 */       this.log.debug("[exchange: " + state.getId() + "] Consume content");
/*     */     }
/* 169 */     HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 170 */     responseConsumer.consumeContent(decoder, ioctrl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseCompleted(InternalState state, InternalConnManager connManager) throws IOException, HttpException {
/* 176 */     HttpClientContext localContext = state.getLocalContext();
/* 177 */     HttpResponse response = state.getFinalResponse();
/* 178 */     if (this.connReuseStrategy.keepAlive(response, (HttpContext)localContext)) {
/* 179 */       long validDuration = this.keepaliveStrategy.getKeepAliveDuration(response, (HttpContext)localContext);
/*     */       
/* 181 */       if (this.log.isDebugEnabled()) {
/*     */         String s;
/* 183 */         if (validDuration > 0L) {
/* 184 */           s = "for " + validDuration + " " + TimeUnit.MILLISECONDS;
/*     */         } else {
/* 186 */           s = "indefinitely";
/*     */         } 
/* 188 */         this.log.debug("[exchange: " + state.getId() + "] Connection can be kept alive " + s);
/*     */       } 
/* 190 */       state.setValidDuration(validDuration);
/* 191 */       state.setReusable();
/*     */     } else {
/* 193 */       if (this.log.isDebugEnabled()) {
/* 194 */         this.log.debug("[exchange: " + state.getId() + "] Connection cannot be kept alive");
/*     */       }
/* 196 */       state.setNonReusable();
/*     */     } 
/* 198 */     HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 199 */     responseConsumer.responseCompleted((HttpContext)localContext);
/* 200 */     if (this.log.isDebugEnabled()) {
/* 201 */       this.log.debug("[exchange: " + state.getId() + "] Response processed");
/*     */     }
/* 203 */     connManager.releaseConnection();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/MinimalClientExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */