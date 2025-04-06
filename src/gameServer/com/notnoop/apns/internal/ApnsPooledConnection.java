/*    */ package com.notnoop.apns.internal;
/*    */ 
/*    */ import com.notnoop.apns.ApnsNotification;
/*    */ import com.notnoop.exceptions.NetworkIOException;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class ApnsPooledConnection
/*    */   implements ApnsConnection
/*    */ {
/* 15 */   private static final Logger logger = LoggerFactory.getLogger(ApnsPooledConnection.class);
/*    */   
/*    */   private final ApnsConnection prototype;
/*    */   private final int max;
/*    */   private final ExecutorService executors;
/*    */   private final ConcurrentLinkedQueue<ApnsConnection> prototypes;
/*    */   private final ThreadLocal<ApnsConnection> uniquePrototype;
/*    */   
/*    */   public ApnsPooledConnection(ApnsConnection prototype, int max) {
/* 24 */     this(prototype, max, Executors.newFixedThreadPool(max));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ApnsPooledConnection(ApnsConnection prototype, int max, ExecutorService executors) {
/* 35 */     this.uniquePrototype = new ThreadLocal<ApnsConnection>()
/*    */       {
/*    */         protected ApnsConnection initialValue() {
/* 38 */           ApnsConnection newCopy = ApnsPooledConnection.this.prototype.copy();
/* 39 */           ApnsPooledConnection.this.prototypes.add(newCopy);
/* 40 */           return newCopy; }
/*    */       };
/*    */     this.prototype = prototype;
/*    */     this.max = max;
/*    */     this.executors = executors;
/* 45 */     this.prototypes = new ConcurrentLinkedQueue<ApnsConnection>(); } public void sendMessage(final ApnsNotification m) throws NetworkIOException { this.executors.execute(new Runnable() {
/*    */           public void run() {
/* 47 */             ((ApnsConnection)ApnsPooledConnection.this.uniquePrototype.get()).sendMessage(m);
/*    */           }
/*    */         }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ApnsConnection copy() {
/* 55 */     return new ApnsPooledConnection(this.prototype, this.max);
/*    */   }
/*    */   
/*    */   public void close() {
/* 59 */     this.executors.shutdown();
/*    */     try {
/* 61 */       this.executors.awaitTermination(10L, TimeUnit.SECONDS);
/* 62 */     } catch (InterruptedException e) {
/* 63 */       logger.warn("pool termination interrupted", e);
/*    */     } 
/* 65 */     for (ApnsConnection conn : this.prototypes) {
/* 66 */       Utilities.close(conn);
/*    */     }
/* 68 */     Utilities.close(this.prototype);
/*    */   }
/*    */   
/*    */   public void testConnection() {
/* 72 */     this.prototype.testConnection();
/*    */   }
/*    */   
/*    */   public synchronized void setCacheLength(int cacheLength) {
/* 76 */     for (ApnsConnection conn : this.prototypes) {
/* 77 */       conn.setCacheLength(cacheLength);
/*    */     }
/*    */   }
/*    */   
/*    */   public int getCacheLength() {
/* 82 */     return ((ApnsConnection)this.prototypes.peek()).getCacheLength();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ApnsPooledConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */