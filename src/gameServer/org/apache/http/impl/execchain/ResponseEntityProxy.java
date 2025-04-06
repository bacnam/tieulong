/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.EofSensorInputStream;
/*     */ import org.apache.http.conn.EofSensorWatcher;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class ResponseEntityProxy
/*     */   extends HttpEntityWrapper
/*     */   implements EofSensorWatcher
/*     */ {
/*     */   private final ConnectionHolder connHolder;
/*     */   
/*     */   public static void enchance(HttpResponse response, ConnectionHolder connHolder) {
/*  53 */     HttpEntity entity = response.getEntity();
/*  54 */     if (entity != null && entity.isStreaming() && connHolder != null) {
/*  55 */       response.setEntity((HttpEntity)new ResponseEntityProxy(entity, connHolder));
/*     */     }
/*     */   }
/*     */   
/*     */   ResponseEntityProxy(HttpEntity entity, ConnectionHolder connHolder) {
/*  60 */     super(entity);
/*  61 */     this.connHolder = connHolder;
/*     */   }
/*     */   
/*     */   private void cleanup() {
/*  65 */     if (this.connHolder != null) {
/*  66 */       this.connHolder.abortConnection();
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseConnection() throws IOException {
/*  71 */     if (this.connHolder != null) {
/*     */       try {
/*  73 */         if (this.connHolder.isReusable()) {
/*  74 */           this.connHolder.releaseConnection();
/*     */         }
/*     */       } finally {
/*  77 */         cleanup();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  89 */     return (InputStream)new EofSensorInputStream(this.wrappedEntity.getContent(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/*  95 */     releaseConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*     */     try {
/* 101 */       this.wrappedEntity.writeTo(outstream);
/* 102 */       releaseConnection();
/*     */     } finally {
/* 104 */       cleanup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eofDetected(InputStream wrapped) throws IOException {
/*     */     try {
/* 113 */       wrapped.close();
/* 114 */       releaseConnection();
/*     */     } finally {
/* 116 */       cleanup();
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamClosed(InputStream wrapped) throws IOException {
/*     */     try {
/* 124 */       boolean open = (this.connHolder != null && !this.connHolder.isReleased());
/*     */ 
/*     */       
/*     */       try {
/* 128 */         wrapped.close();
/* 129 */         releaseConnection();
/* 130 */       } catch (SocketException ex) {
/* 131 */         if (open) {
/* 132 */           throw ex;
/*     */         }
/*     */       } 
/*     */     } finally {
/* 136 */       cleanup();
/*     */     } 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamAbort(InputStream wrapped) throws IOException {
/* 143 */     cleanup();
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
/* 150 */     sb.append(this.wrappedEntity);
/* 151 */     sb.append('}');
/* 152 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/execchain/ResponseEntityProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */