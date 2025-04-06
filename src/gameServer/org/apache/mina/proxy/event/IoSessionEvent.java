/*     */ package org.apache.mina.proxy.event;
/*     */ 
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public class IoSessionEvent
/*     */ {
/*  35 */   private static final Logger logger = LoggerFactory.getLogger(IoSessionEvent.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IoFilter.NextFilter nextFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IoSession session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IoSessionEventType type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IdleStatus status;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSessionEvent(IoFilter.NextFilter nextFilter, IoSession session, IoSessionEventType type) {
/*  67 */     this.nextFilter = nextFilter;
/*  68 */     this.session = session;
/*  69 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSessionEvent(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) {
/*  81 */     this(nextFilter, session, IoSessionEventType.IDLE);
/*  82 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deliverEvent() {
/*  89 */     logger.debug("Delivering event {}", this);
/*  90 */     deliverEvent(this.nextFilter, this.session, this.type, this.status);
/*     */   }
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
/*     */   private static void deliverEvent(IoFilter.NextFilter nextFilter, IoSession session, IoSessionEventType type, IdleStatus status) {
/* 105 */     switch (type) {
/*     */       case CREATED:
/* 107 */         nextFilter.sessionCreated(session);
/*     */         break;
/*     */       case OPENED:
/* 110 */         nextFilter.sessionOpened(session);
/*     */         break;
/*     */       case IDLE:
/* 113 */         nextFilter.sessionIdle(session, status);
/*     */         break;
/*     */       case CLOSED:
/* 116 */         nextFilter.sessionClosed(session);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     StringBuilder sb = new StringBuilder(IoSessionEvent.class.getSimpleName());
/* 127 */     sb.append('@');
/* 128 */     sb.append(Integer.toHexString(hashCode()));
/* 129 */     sb.append(" - [ ").append(this.session);
/* 130 */     sb.append(", ").append(this.type);
/* 131 */     sb.append(']');
/* 132 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdleStatus getStatus() {
/* 141 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFilter.NextFilter getNextFilter() {
/* 150 */     return this.nextFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSession getSession() {
/* 159 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSessionEventType getType() {
/* 168 */     return this.type;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/event/IoSessionEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */