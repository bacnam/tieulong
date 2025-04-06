/*     */ package org.apache.mina.core.session;
/*     */ 
/*     */ import org.apache.mina.core.write.WriteRequest;
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
/*     */ public class IoEvent
/*     */   implements Runnable
/*     */ {
/*     */   private final IoEventType type;
/*     */   private final IoSession session;
/*     */   private final Object parameter;
/*     */   
/*     */   public IoEvent(IoEventType type, IoSession session, Object parameter) {
/*  39 */     if (type == null) {
/*  40 */       throw new IllegalArgumentException("type");
/*     */     }
/*  42 */     if (session == null) {
/*  43 */       throw new IllegalArgumentException("session");
/*     */     }
/*  45 */     this.type = type;
/*  46 */     this.session = session;
/*  47 */     this.parameter = parameter;
/*     */   }
/*     */   
/*     */   public IoEventType getType() {
/*  51 */     return this.type;
/*     */   }
/*     */   
/*     */   public IoSession getSession() {
/*  55 */     return this.session;
/*     */   }
/*     */   
/*     */   public Object getParameter() {
/*  59 */     return this.parameter;
/*     */   }
/*     */   
/*     */   public void run() {
/*  63 */     fire();
/*     */   }
/*     */   
/*     */   public void fire() {
/*  67 */     switch (getType()) {
/*     */       case MESSAGE_RECEIVED:
/*  69 */         getSession().getFilterChain().fireMessageReceived(getParameter());
/*     */         return;
/*     */       case MESSAGE_SENT:
/*  72 */         getSession().getFilterChain().fireMessageSent((WriteRequest)getParameter());
/*     */         return;
/*     */       case WRITE:
/*  75 */         getSession().getFilterChain().fireFilterWrite((WriteRequest)getParameter());
/*     */         return;
/*     */       case CLOSE:
/*  78 */         getSession().getFilterChain().fireFilterClose();
/*     */         return;
/*     */       case EXCEPTION_CAUGHT:
/*  81 */         getSession().getFilterChain().fireExceptionCaught((Throwable)getParameter());
/*     */         return;
/*     */       case SESSION_IDLE:
/*  84 */         getSession().getFilterChain().fireSessionIdle((IdleStatus)getParameter());
/*     */         return;
/*     */       case SESSION_OPENED:
/*  87 */         getSession().getFilterChain().fireSessionOpened();
/*     */         return;
/*     */       case SESSION_CREATED:
/*  90 */         getSession().getFilterChain().fireSessionCreated();
/*     */         return;
/*     */       case SESSION_CLOSED:
/*  93 */         getSession().getFilterChain().fireSessionClosed();
/*     */         return;
/*     */     } 
/*  96 */     throw new IllegalArgumentException("Unknown event type: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     if (getParameter() == null) {
/* 103 */       return "[" + getSession() + "] " + getType().name();
/*     */     }
/*     */     
/* 106 */     return "[" + getSession() + "] " + getType().name() + ": " + getParameter();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/IoEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */