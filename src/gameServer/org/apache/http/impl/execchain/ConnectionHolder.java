/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ class ConnectionHolder
/*     */   implements ConnectionReleaseTrigger, Cancellable, Closeable
/*     */ {
/*     */   private final Log log;
/*     */   private final HttpClientConnectionManager manager;
/*     */   private final HttpClientConnection managedConn;
/*     */   private volatile boolean reusable;
/*     */   private volatile Object state;
/*     */   private volatile long validDuration;
/*     */   private volatile TimeUnit tunit;
/*     */   private volatile boolean released;
/*     */   
/*     */   public ConnectionHolder(Log log, HttpClientConnectionManager manager, HttpClientConnection managedConn) {
/*  65 */     this.log = log;
/*  66 */     this.manager = manager;
/*  67 */     this.managedConn = managedConn;
/*     */   }
/*     */   
/*     */   public boolean isReusable() {
/*  71 */     return this.reusable;
/*     */   }
/*     */   
/*     */   public void markReusable() {
/*  75 */     this.reusable = true;
/*     */   }
/*     */   
/*     */   public void markNonReusable() {
/*  79 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/*  83 */     this.state = state;
/*     */   }
/*     */   
/*     */   public void setValidFor(long duration, TimeUnit tunit) {
/*  87 */     synchronized (this.managedConn) {
/*  88 */       this.validDuration = duration;
/*  89 */       this.tunit = tunit;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseConnection() {
/*  95 */     synchronized (this.managedConn) {
/*  96 */       if (this.released) {
/*     */         return;
/*     */       }
/*  99 */       this.released = true;
/* 100 */       if (this.reusable) {
/* 101 */         this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.tunit);
/*     */       } else {
/*     */         
/*     */         try {
/* 105 */           this.managedConn.close();
/* 106 */           this.log.debug("Connection discarded");
/* 107 */         } catch (IOException ex) {
/* 108 */           if (this.log.isDebugEnabled()) {
/* 109 */             this.log.debug(ex.getMessage(), ex);
/*     */           }
/*     */         } finally {
/* 112 */           this.manager.releaseConnection(this.managedConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void abortConnection() {
/* 121 */     synchronized (this.managedConn) {
/* 122 */       if (this.released) {
/*     */         return;
/*     */       }
/* 125 */       this.released = true;
/*     */       try {
/* 127 */         this.managedConn.shutdown();
/* 128 */         this.log.debug("Connection discarded");
/* 129 */       } catch (IOException ex) {
/* 130 */         if (this.log.isDebugEnabled()) {
/* 131 */           this.log.debug(ex.getMessage(), ex);
/*     */         }
/*     */       } finally {
/* 134 */         this.manager.releaseConnection(this.managedConn, null, 0L, TimeUnit.MILLISECONDS);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 142 */     boolean alreadyReleased = this.released;
/* 143 */     this.log.debug("Cancelling request execution");
/* 144 */     abortConnection();
/* 145 */     return !alreadyReleased;
/*     */   }
/*     */   
/*     */   public boolean isReleased() {
/* 149 */     return this.released;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 154 */     abortConnection();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/execchain/ConnectionHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */