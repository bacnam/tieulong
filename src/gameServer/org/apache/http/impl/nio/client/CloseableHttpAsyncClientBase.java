/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CloseableHttpAsyncClientBase
/*     */   extends CloseableHttpAsyncClient
/*     */ {
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   private final Thread reactorThread;
/*     */   private final AtomicReference<Status> status;
/*  40 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  42 */   enum Status { INACTIVE, ACTIVE, STOPPED; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CloseableHttpAsyncClientBase(NHttpClientConnectionManager connmgr, ThreadFactory threadFactory) {
/*  53 */     this.connmgr = connmgr;
/*  54 */     this.reactorThread = threadFactory.newThread(new Runnable()
/*     */         {
/*     */           public void run() {
/*  57 */             CloseableHttpAsyncClientBase.this.doExecute();
/*     */           }
/*     */         });
/*     */     
/*  61 */     this.status = new AtomicReference<Status>(Status.INACTIVE);
/*     */   }
/*     */   
/*     */   private void doExecute() {
/*     */     try {
/*  66 */       InternalIODispatch internalIODispatch = new InternalIODispatch();
/*  67 */       this.connmgr.execute((IOEventDispatch)internalIODispatch);
/*  68 */     } catch (Exception ex) {
/*  69 */       this.log.error("I/O reactor terminated abnormally", ex);
/*     */     } finally {
/*  71 */       this.status.set(Status.STOPPED);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  77 */     if (this.status.compareAndSet(Status.INACTIVE, Status.ACTIVE)) {
/*  78 */       this.reactorThread.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() {
/*  83 */     if (this.status.compareAndSet(Status.ACTIVE, Status.STOPPED)) {
/*     */       try {
/*  85 */         this.connmgr.shutdown();
/*  86 */       } catch (IOException ex) {
/*  87 */         this.log.error("I/O error shutting down connection manager", ex);
/*     */       } 
/*     */       try {
/*  90 */         this.reactorThread.join();
/*  91 */       } catch (InterruptedException ex) {
/*  92 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/*  98 */     shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 103 */     return (getStatus() == Status.ACTIVE);
/*     */   }
/*     */   
/*     */   Status getStatus() {
/* 107 */     return this.status.get();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/CloseableHttpAsyncClientBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */