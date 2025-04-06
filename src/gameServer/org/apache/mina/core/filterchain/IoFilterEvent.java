/*     */ package org.apache.mina.core.filterchain;
/*     */ 
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
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
/*     */ public class IoFilterEvent
/*     */   extends IoEvent
/*     */ {
/*  40 */   private static Logger LOGGER = LoggerFactory.getLogger(IoFilterEvent.class);
/*     */ 
/*     */   
/*  43 */   private static boolean DEBUG = LOGGER.isDebugEnabled();
/*     */   
/*     */   private final IoFilter.NextFilter nextFilter;
/*     */   
/*     */   public IoFilterEvent(IoFilter.NextFilter nextFilter, IoEventType type, IoSession session, Object parameter) {
/*  48 */     super(type, session, parameter);
/*     */     
/*  50 */     if (nextFilter == null) {
/*  51 */       throw new IllegalArgumentException("nextFilter must not be null");
/*     */     }
/*     */     
/*  54 */     this.nextFilter = nextFilter;
/*     */   }
/*     */   
/*     */   public IoFilter.NextFilter getNextFilter() {
/*  58 */     return this.nextFilter;
/*     */   } public void fire() {
/*     */     Object parameter;
/*     */     WriteRequest writeRequest;
/*     */     Throwable throwable;
/*  63 */     IoSession session = getSession();
/*  64 */     IoFilter.NextFilter nextFilter = getNextFilter();
/*  65 */     IoEventType type = getType();
/*     */     
/*  67 */     if (DEBUG) {
/*  68 */       LOGGER.debug("Firing a {} event for session {}", type, Long.valueOf(session.getId()));
/*     */     }
/*     */     
/*  71 */     switch (type) {
/*     */       case MESSAGE_RECEIVED:
/*  73 */         parameter = getParameter();
/*  74 */         nextFilter.messageReceived(session, parameter);
/*     */         break;
/*     */       
/*     */       case MESSAGE_SENT:
/*  78 */         writeRequest = (WriteRequest)getParameter();
/*  79 */         nextFilter.messageSent(session, writeRequest);
/*     */         break;
/*     */       
/*     */       case WRITE:
/*  83 */         writeRequest = (WriteRequest)getParameter();
/*  84 */         nextFilter.filterWrite(session, writeRequest);
/*     */         break;
/*     */       
/*     */       case CLOSE:
/*  88 */         nextFilter.filterClose(session);
/*     */         break;
/*     */       
/*     */       case EXCEPTION_CAUGHT:
/*  92 */         throwable = (Throwable)getParameter();
/*  93 */         nextFilter.exceptionCaught(session, throwable);
/*     */         break;
/*     */       
/*     */       case SESSION_IDLE:
/*  97 */         nextFilter.sessionIdle(session, (IdleStatus)getParameter());
/*     */         break;
/*     */       
/*     */       case SESSION_OPENED:
/* 101 */         nextFilter.sessionOpened(session);
/*     */         break;
/*     */       
/*     */       case SESSION_CREATED:
/* 105 */         nextFilter.sessionCreated(session);
/*     */         break;
/*     */       
/*     */       case SESSION_CLOSED:
/* 109 */         nextFilter.sessionClosed(session);
/*     */         break;
/*     */       
/*     */       default:
/* 113 */         throw new IllegalArgumentException("Unknown event type: " + type);
/*     */     } 
/*     */     
/* 116 */     if (DEBUG)
/* 117 */       LOGGER.debug("Event {} has been fired for session {}", type, Long.valueOf(session.getId())); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/IoFilterEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */