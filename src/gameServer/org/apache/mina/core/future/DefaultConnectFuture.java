/*     */ package org.apache.mina.core.future;
/*     */ 
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConnectFuture
/*     */   extends DefaultIoFuture
/*     */   implements ConnectFuture
/*     */ {
/*  32 */   private static final Object CANCELED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConnectFuture newFailedFuture(Throwable exception) {
/*  38 */     DefaultConnectFuture failedFuture = new DefaultConnectFuture();
/*  39 */     failedFuture.setException(exception);
/*  40 */     return failedFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectFuture() {
/*  47 */     super(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSession getSession() {
/*  52 */     Object v = getValue();
/*  53 */     if (v instanceof RuntimeException)
/*  54 */       throw (RuntimeException)v; 
/*  55 */     if (v instanceof Error)
/*  56 */       throw (Error)v; 
/*  57 */     if (v instanceof Throwable)
/*  58 */       throw (RuntimeIoException)(new RuntimeIoException("Failed to get the session.")).initCause((Throwable)v); 
/*  59 */     if (v instanceof IoSession) {
/*  60 */       return (IoSession)v;
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getException() {
/*  67 */     Object v = getValue();
/*  68 */     if (v instanceof Throwable) {
/*  69 */       return (Throwable)v;
/*     */     }
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/*  76 */     return getValue() instanceof IoSession;
/*     */   }
/*     */   
/*     */   public boolean isCanceled() {
/*  80 */     return (getValue() == CANCELED);
/*     */   }
/*     */   
/*     */   public void setSession(IoSession session) {
/*  84 */     if (session == null) {
/*  85 */       throw new IllegalArgumentException("session");
/*     */     }
/*  87 */     setValue(session);
/*     */   }
/*     */   
/*     */   public void setException(Throwable exception) {
/*  91 */     if (exception == null) {
/*  92 */       throw new IllegalArgumentException("exception");
/*     */     }
/*  94 */     setValue(exception);
/*     */   }
/*     */   
/*     */   public void cancel() {
/*  98 */     setValue(CANCELED);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectFuture await() throws InterruptedException {
/* 103 */     return (ConnectFuture)super.await();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectFuture awaitUninterruptibly() {
/* 108 */     return (ConnectFuture)super.awaitUninterruptibly();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectFuture addListener(IoFutureListener<?> listener) {
/* 113 */     return (ConnectFuture)super.addListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectFuture removeListener(IoFutureListener<?> listener) {
/* 118 */     return (ConnectFuture)super.removeListener(listener);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/DefaultConnectFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */