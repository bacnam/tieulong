/*     */ package org.apache.mina.filter.keepalive;
/*     */ 
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
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
/*     */ public class KeepAliveFilter
/*     */   extends IoFilterAdapter
/*     */ {
/* 142 */   private final AttributeKey WAITING_FOR_RESPONSE = new AttributeKey(getClass(), "waitingForResponse");
/*     */   
/* 144 */   private final AttributeKey IGNORE_READER_IDLE_ONCE = new AttributeKey(getClass(), "ignoreReaderIdleOnce");
/*     */ 
/*     */ 
/*     */   
/*     */   private final KeepAliveMessageFactory messageFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IdleStatus interestedIdleStatus;
/*     */ 
/*     */   
/*     */   private volatile KeepAliveRequestTimeoutHandler requestTimeoutHandler;
/*     */ 
/*     */   
/*     */   private volatile int requestInterval;
/*     */ 
/*     */   
/*     */   private volatile int requestTimeout;
/*     */ 
/*     */   
/*     */   private volatile boolean forwardEvent;
/*     */ 
/*     */ 
/*     */   
/*     */   public KeepAliveFilter(KeepAliveMessageFactory messageFactory) {
/* 169 */     this(messageFactory, IdleStatus.READER_IDLE, KeepAliveRequestTimeoutHandler.CLOSE);
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
/*     */   public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus) {
/* 182 */     this(messageFactory, interestedIdleStatus, KeepAliveRequestTimeoutHandler.CLOSE, 60, 30);
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
/*     */   public KeepAliveFilter(KeepAliveMessageFactory messageFactory, KeepAliveRequestTimeoutHandler policy) {
/* 195 */     this(messageFactory, IdleStatus.READER_IDLE, policy, 60, 30);
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
/*     */   public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus, KeepAliveRequestTimeoutHandler policy) {
/* 208 */     this(messageFactory, interestedIdleStatus, policy, 60, 30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus, KeepAliveRequestTimeoutHandler policy, int keepAliveRequestInterval, int keepAliveRequestTimeout) {
/* 216 */     if (messageFactory == null) {
/* 217 */       throw new IllegalArgumentException("messageFactory");
/*     */     }
/* 219 */     if (interestedIdleStatus == null) {
/* 220 */       throw new IllegalArgumentException("interestedIdleStatus");
/*     */     }
/* 222 */     if (policy == null) {
/* 223 */       throw new IllegalArgumentException("policy");
/*     */     }
/*     */     
/* 226 */     this.messageFactory = messageFactory;
/* 227 */     this.interestedIdleStatus = interestedIdleStatus;
/* 228 */     this.requestTimeoutHandler = policy;
/*     */     
/* 230 */     setRequestInterval(keepAliveRequestInterval);
/* 231 */     setRequestTimeout(keepAliveRequestTimeout);
/*     */   }
/*     */   
/*     */   public IdleStatus getInterestedIdleStatus() {
/* 235 */     return this.interestedIdleStatus;
/*     */   }
/*     */   
/*     */   public KeepAliveRequestTimeoutHandler getRequestTimeoutHandler() {
/* 239 */     return this.requestTimeoutHandler;
/*     */   }
/*     */   
/*     */   public void setRequestTimeoutHandler(KeepAliveRequestTimeoutHandler timeoutHandler) {
/* 243 */     if (timeoutHandler == null) {
/* 244 */       throw new IllegalArgumentException("timeoutHandler");
/*     */     }
/* 246 */     this.requestTimeoutHandler = timeoutHandler;
/*     */   }
/*     */   
/*     */   public int getRequestInterval() {
/* 250 */     return this.requestInterval;
/*     */   }
/*     */   
/*     */   public void setRequestInterval(int keepAliveRequestInterval) {
/* 254 */     if (keepAliveRequestInterval <= 0) {
/* 255 */       throw new IllegalArgumentException("keepAliveRequestInterval must be a positive integer: " + keepAliveRequestInterval);
/*     */     }
/*     */     
/* 258 */     this.requestInterval = keepAliveRequestInterval;
/*     */   }
/*     */   
/*     */   public int getRequestTimeout() {
/* 262 */     return this.requestTimeout;
/*     */   }
/*     */   
/*     */   public void setRequestTimeout(int keepAliveRequestTimeout) {
/* 266 */     if (keepAliveRequestTimeout <= 0) {
/* 267 */       throw new IllegalArgumentException("keepAliveRequestTimeout must be a positive integer: " + keepAliveRequestTimeout);
/*     */     }
/*     */     
/* 270 */     this.requestTimeout = keepAliveRequestTimeout;
/*     */   }
/*     */   
/*     */   public KeepAliveMessageFactory getMessageFactory() {
/* 274 */     return this.messageFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForwardEvent() {
/* 283 */     return this.forwardEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwardEvent(boolean forwardEvent) {
/* 292 */     this.forwardEvent = forwardEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 297 */     if (parent.contains((IoFilter)this)) {
/* 298 */       throw new IllegalArgumentException("You can't add the same filter instance more than once. Create another instance and add it.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPostAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 305 */     resetStatus(parent.getSession());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 310 */     resetStatus(parent.getSession());
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/*     */     try {
/* 316 */       if (this.messageFactory.isRequest(session, message)) {
/* 317 */         Object pongMessage = this.messageFactory.getResponse(session, message);
/*     */         
/* 319 */         if (pongMessage != null) {
/* 320 */           nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(pongMessage));
/*     */         }
/*     */       } 
/*     */       
/* 324 */       if (this.messageFactory.isResponse(session, message)) {
/* 325 */         resetStatus(session);
/*     */       }
/*     */     } finally {
/* 328 */       if (!isKeepAliveMessage(session, message)) {
/* 329 */         nextFilter.messageReceived(session, message);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 336 */     Object message = writeRequest.getMessage();
/* 337 */     if (!isKeepAliveMessage(session, message)) {
/* 338 */       nextFilter.messageSent(session, writeRequest);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 344 */     if (status == this.interestedIdleStatus) {
/* 345 */       if (!session.containsAttribute(this.WAITING_FOR_RESPONSE)) {
/* 346 */         Object pingMessage = this.messageFactory.getRequest(session);
/* 347 */         if (pingMessage != null) {
/* 348 */           nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(pingMessage));
/*     */ 
/*     */ 
/*     */           
/* 352 */           if (getRequestTimeoutHandler() != KeepAliveRequestTimeoutHandler.DEAF_SPEAKER) {
/* 353 */             markStatus(session);
/* 354 */             if (this.interestedIdleStatus == IdleStatus.BOTH_IDLE) {
/* 355 */               session.setAttribute(this.IGNORE_READER_IDLE_ONCE);
/*     */             }
/*     */           } else {
/* 358 */             resetStatus(session);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 362 */         handlePingTimeout(session);
/*     */       } 
/* 364 */     } else if (status == IdleStatus.READER_IDLE && 
/* 365 */       session.removeAttribute(this.IGNORE_READER_IDLE_ONCE) == null && 
/* 366 */       session.containsAttribute(this.WAITING_FOR_RESPONSE)) {
/* 367 */       handlePingTimeout(session);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 372 */     if (this.forwardEvent) {
/* 373 */       nextFilter.sessionIdle(session, status);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handlePingTimeout(IoSession session) throws Exception {
/* 378 */     resetStatus(session);
/* 379 */     KeepAliveRequestTimeoutHandler handler = getRequestTimeoutHandler();
/* 380 */     if (handler == KeepAliveRequestTimeoutHandler.DEAF_SPEAKER) {
/*     */       return;
/*     */     }
/*     */     
/* 384 */     handler.keepAliveRequestTimedOut(this, session);
/*     */   }
/*     */   
/*     */   private void markStatus(IoSession session) {
/* 388 */     session.getConfig().setIdleTime(this.interestedIdleStatus, 0);
/* 389 */     session.getConfig().setReaderIdleTime(getRequestTimeout());
/* 390 */     session.setAttribute(this.WAITING_FOR_RESPONSE);
/*     */   }
/*     */   
/*     */   private void resetStatus(IoSession session) {
/* 394 */     session.getConfig().setReaderIdleTime(0);
/* 395 */     session.getConfig().setWriterIdleTime(0);
/* 396 */     session.getConfig().setIdleTime(this.interestedIdleStatus, getRequestInterval());
/* 397 */     session.removeAttribute(this.WAITING_FOR_RESPONSE);
/*     */   }
/*     */   
/*     */   private boolean isKeepAliveMessage(IoSession session, Object message) {
/* 401 */     return (this.messageFactory.isRequest(session, message) || this.messageFactory.isResponse(session, message));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/keepalive/KeepAliveFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */