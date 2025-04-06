/*     */ package org.apache.mina.core.session;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.mina.core.future.CloseFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.util.ConcurrentHashSet;
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
/*     */ public class IdleStatusChecker
/*     */ {
/*  42 */   private final Set<AbstractIoSession> sessions = (Set<AbstractIoSession>)new ConcurrentHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private final NotifyingTask notifyingTask = new NotifyingTask();
/*     */   
/*  51 */   private final IoFutureListener<IoFuture> sessionCloseListener = new SessionCloseListener();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSession(AbstractIoSession session) {
/*  62 */     this.sessions.add(session);
/*  63 */     CloseFuture closeFuture = session.getCloseFuture();
/*     */ 
/*     */     
/*  66 */     closeFuture.addListener(this.sessionCloseListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeSession(AbstractIoSession session) {
/*  74 */     this.sessions.remove(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NotifyingTask getNotifyingTask() {
/*  82 */     return this.notifyingTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class NotifyingTask
/*     */     implements Runnable
/*     */   {
/*     */     private volatile boolean cancelled;
/*     */ 
/*     */ 
/*     */     
/*     */     private volatile Thread thread;
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 100 */       this.thread = Thread.currentThread();
/*     */       try {
/* 102 */         while (!this.cancelled)
/*     */         {
/* 104 */           long currentTime = System.currentTimeMillis();
/*     */           
/* 106 */           notifySessions(currentTime);
/*     */           
/*     */           try {
/* 109 */             Thread.sleep(1000L);
/* 110 */           } catch (InterruptedException e) {}
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 115 */         this.thread = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 123 */       this.cancelled = true;
/* 124 */       Thread thread = this.thread;
/* 125 */       if (thread != null) {
/* 126 */         thread.interrupt();
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifySessions(long currentTime) {
/* 131 */       Iterator<AbstractIoSession> it = IdleStatusChecker.this.sessions.iterator();
/* 132 */       while (it.hasNext()) {
/* 133 */         AbstractIoSession session = it.next();
/* 134 */         if (session.isConnected()) {
/* 135 */           AbstractIoSession.notifyIdleSession(session, currentTime);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SessionCloseListener
/*     */     implements IoFutureListener<IoFuture>
/*     */   {
/*     */     public void operationComplete(IoFuture future) {
/* 150 */       IdleStatusChecker.this.removeSession((AbstractIoSession)future.getSession());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IdleStatusChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */