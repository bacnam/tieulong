/*     */ package org.apache.mina.core.future;
/*     */ 
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
/*     */ public class DefaultWriteFuture
/*     */   extends DefaultIoFuture
/*     */   implements WriteFuture
/*     */ {
/*     */   public static WriteFuture newWrittenFuture(IoSession session) {
/*  34 */     DefaultWriteFuture unwrittenFuture = new DefaultWriteFuture(session);
/*  35 */     unwrittenFuture.setWritten();
/*  36 */     return unwrittenFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WriteFuture newNotWrittenFuture(IoSession session, Throwable cause) {
/*  43 */     DefaultWriteFuture unwrittenFuture = new DefaultWriteFuture(session);
/*  44 */     unwrittenFuture.setException(cause);
/*  45 */     return unwrittenFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultWriteFuture(IoSession session) {
/*  52 */     super(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritten() {
/*  59 */     if (isDone()) {
/*  60 */       Object v = getValue();
/*  61 */       if (v instanceof Boolean) {
/*  62 */         return ((Boolean)v).booleanValue();
/*     */       }
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getException() {
/*  72 */     if (isDone()) {
/*  73 */       Object v = getValue();
/*  74 */       if (v instanceof Throwable) {
/*  75 */         return (Throwable)v;
/*     */       }
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWritten() {
/*  85 */     setValue(Boolean.TRUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setException(Throwable exception) {
/*  92 */     if (exception == null) {
/*  93 */       throw new IllegalArgumentException("exception");
/*     */     }
/*     */     
/*  96 */     setValue(exception);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteFuture await() throws InterruptedException {
/* 104 */     return (WriteFuture)super.await();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteFuture awaitUninterruptibly() {
/* 112 */     return (WriteFuture)super.awaitUninterruptibly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteFuture addListener(IoFutureListener<?> listener) {
/* 120 */     return (WriteFuture)super.addListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WriteFuture removeListener(IoFutureListener<?> listener) {
/* 128 */     return (WriteFuture)super.removeListener(listener);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/DefaultWriteFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */