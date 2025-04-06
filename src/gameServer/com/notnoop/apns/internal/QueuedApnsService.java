/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import com.notnoop.apns.ApnsNotification;
/*     */ import com.notnoop.apns.ApnsService;
/*     */ import com.notnoop.apns.EnhancedApnsNotification;
/*     */ import com.notnoop.exceptions.NetworkIOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueuedApnsService
/*     */   extends AbstractApnsService
/*     */ {
/*  48 */   private static final Logger logger = LoggerFactory.getLogger(QueuedApnsService.class);
/*     */   
/*     */   private ApnsService service;
/*     */   private BlockingQueue<ApnsNotification> queue;
/*  52 */   private AtomicBoolean started = new AtomicBoolean(false); private Thread thread;
/*     */   
/*     */   public QueuedApnsService(ApnsService service) {
/*  55 */     super(null);
/*  56 */     this.service = service;
/*  57 */     this.queue = new LinkedBlockingQueue<ApnsNotification>();
/*  58 */     this.thread = null;
/*     */   }
/*     */   private volatile boolean shouldContinue;
/*     */   
/*     */   public void push(ApnsNotification msg) {
/*  63 */     if (!this.started.get()) {
/*  64 */       throw new IllegalStateException("service hasn't be started or was closed");
/*     */     }
/*  66 */     this.queue.add(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  73 */     if (this.started.getAndSet(true)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.service.start();
/*  81 */     this.shouldContinue = true;
/*  82 */     this.thread = new Thread() {
/*     */         public void run() {
/*  84 */           while (QueuedApnsService.this.shouldContinue) {
/*     */             try {
/*  86 */               ApnsNotification msg = QueuedApnsService.this.queue.take();
/*  87 */               QueuedApnsService.this.service.push(msg);
/*  88 */             } catch (InterruptedException e) {
/*     */             
/*  90 */             } catch (NetworkIOException e) {
/*     */             
/*  92 */             } catch (Exception e) {
/*     */               
/*  94 */               QueuedApnsService.logger.warn("Unexpected message caught... Shouldn't be here", e);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*  99 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 103 */     this.started.set(false);
/* 104 */     this.shouldContinue = false;
/* 105 */     this.thread.interrupt();
/* 106 */     this.service.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Date> getInactiveDevices() throws NetworkIOException {
/* 111 */     return this.service.getInactiveDevices();
/*     */   }
/*     */   
/*     */   public void testConnection() throws NetworkIOException {
/* 115 */     this.service.testConnection();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/QueuedApnsService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */