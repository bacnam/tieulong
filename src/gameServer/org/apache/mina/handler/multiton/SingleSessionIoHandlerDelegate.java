/*     */ package org.apache.mina.handler.multiton;
/*     */ 
/*     */ import org.apache.mina.core.service.IoHandler;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IdleStatus;
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
/*     */ @Deprecated
/*     */ public class SingleSessionIoHandlerDelegate
/*     */   implements IoHandler
/*     */ {
/*  46 */   public static final AttributeKey HANDLER = new AttributeKey(SingleSessionIoHandlerDelegate.class, "handler");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SingleSessionIoHandlerFactory factory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleSessionIoHandlerDelegate(SingleSessionIoHandlerFactory factory) {
/*  62 */     if (factory == null) {
/*  63 */       throw new IllegalArgumentException("factory");
/*     */     }
/*  65 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingleSessionIoHandlerFactory getFactory() {
/*  73 */     return this.factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoSession session) throws Exception {
/*  84 */     SingleSessionIoHandler handler = this.factory.getHandler(session);
/*  85 */     session.setAttribute(HANDLER, handler);
/*  86 */     handler.sessionCreated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoSession session) throws Exception {
/*  95 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/*  96 */     handler.sessionOpened();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoSession session) throws Exception {
/* 105 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 106 */     handler.sessionClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
/* 115 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 116 */     handler.sessionIdle(status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
/* 125 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 126 */     handler.exceptionCaught(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageReceived(IoSession session, Object message) throws Exception {
/* 135 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 136 */     handler.messageReceived(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageSent(IoSession session, Object message) throws Exception {
/* 145 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 146 */     handler.messageSent(message);
/*     */   }
/*     */   
/*     */   public void inputClosed(IoSession session) throws Exception {
/* 150 */     SingleSessionIoHandler handler = (SingleSessionIoHandler)session.getAttribute(HANDLER);
/* 151 */     handler.inputClosed(session);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/multiton/SingleSessionIoHandlerDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */