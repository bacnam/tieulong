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
/*     */ 
/*     */ 
/*     */ public class DefaultReadFuture
/*     */   extends DefaultIoFuture
/*     */   implements ReadFuture
/*     */ {
/*  34 */   private static final Object CLOSED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultReadFuture(IoSession session) {
/*  40 */     super(session);
/*     */   }
/*     */   
/*     */   public Object getMessage() {
/*  44 */     if (isDone()) {
/*  45 */       Object v = getValue();
/*  46 */       if (v == CLOSED) {
/*  47 */         return null;
/*     */       }
/*     */       
/*  50 */       if (v instanceof ExceptionHolder) {
/*  51 */         v = ((ExceptionHolder)v).exception;
/*  52 */         if (v instanceof RuntimeException) {
/*  53 */           throw (RuntimeException)v;
/*     */         }
/*  55 */         if (v instanceof Error) {
/*  56 */           throw (Error)v;
/*     */         }
/*  58 */         if (v instanceof java.io.IOException || v instanceof Exception) {
/*  59 */           throw new RuntimeIoException((Exception)v);
/*     */         }
/*     */       } 
/*     */       
/*  63 */       return v;
/*     */     } 
/*     */     
/*  66 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isRead() {
/*  70 */     if (isDone()) {
/*  71 */       Object v = getValue();
/*  72 */       return (v != CLOSED && !(v instanceof ExceptionHolder));
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/*  78 */     if (isDone()) {
/*  79 */       return (getValue() == CLOSED);
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public Throwable getException() {
/*  85 */     if (isDone()) {
/*  86 */       Object v = getValue();
/*  87 */       if (v instanceof ExceptionHolder) {
/*  88 */         return ((ExceptionHolder)v).exception;
/*     */       }
/*     */     } 
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public void setClosed() {
/*  95 */     setValue(CLOSED);
/*     */   }
/*     */   
/*     */   public void setRead(Object message) {
/*  99 */     if (message == null) {
/* 100 */       throw new IllegalArgumentException("message");
/*     */     }
/* 102 */     setValue(message);
/*     */   }
/*     */   
/*     */   public void setException(Throwable exception) {
/* 106 */     if (exception == null) {
/* 107 */       throw new IllegalArgumentException("exception");
/*     */     }
/*     */     
/* 110 */     setValue(new ExceptionHolder(exception));
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadFuture await() throws InterruptedException {
/* 115 */     return (ReadFuture)super.await();
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadFuture awaitUninterruptibly() {
/* 120 */     return (ReadFuture)super.awaitUninterruptibly();
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadFuture addListener(IoFutureListener<?> listener) {
/* 125 */     return (ReadFuture)super.addListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadFuture removeListener(IoFutureListener<?> listener) {
/* 130 */     return (ReadFuture)super.removeListener(listener);
/*     */   }
/*     */   
/*     */   private static class ExceptionHolder {
/*     */     private final Throwable exception;
/*     */     
/*     */     private ExceptionHolder(Throwable exception) {
/* 137 */       this.exception = exception;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/DefaultReadFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */