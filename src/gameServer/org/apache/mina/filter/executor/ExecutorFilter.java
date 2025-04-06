/*     */ package org.apache.mina.filter.executor;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.filterchain.IoFilterEvent;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public class ExecutorFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   private EnumSet<IoEventType> eventTypes;
/*     */   private Executor executor;
/*     */   private boolean manageableExecutor;
/*     */   private static final int DEFAULT_MAX_POOL_SIZE = 16;
/*     */   private static final int BASE_THREAD_NUMBER = 0;
/*     */   private static final long DEFAULT_KEEPALIVE_TIME = 30L;
/*     */   private static final boolean MANAGEABLE_EXECUTOR = true;
/*     */   private static final boolean NOT_MANAGEABLE_EXECUTOR = false;
/* 141 */   private static IoEventType[] DEFAULT_EVENT_SET = new IoEventType[] { IoEventType.EXCEPTION_CAUGHT, IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT, IoEventType.SESSION_CLOSED, IoEventType.SESSION_IDLE, IoEventType.SESSION_OPENED };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorFilter() {
/* 153 */     Executor executor = createDefaultExecutor(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 157 */     init(executor, true, new IoEventType[0]);
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
/*     */   public ExecutorFilter(int maximumPoolSize) {
/* 170 */     Executor executor = createDefaultExecutor(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 174 */     init(executor, true, new IoEventType[0]);
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
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize) {
/* 188 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 192 */     init(executor, true, new IoEventType[0]);
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
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
/* 206 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 210 */     init(executor, true, new IoEventType[0]);
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
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler) {
/* 226 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);
/*     */ 
/*     */ 
/*     */     
/* 230 */     init(executor, true, new IoEventType[0]);
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
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
/* 246 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, null);
/*     */ 
/*     */ 
/*     */     
/* 250 */     init(executor, true, new IoEventType[0]);
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
/*     */ 
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
/* 267 */     Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);
/*     */ 
/*     */ 
/*     */     
/* 271 */     init(executor, true, new IoEventType[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorFilter(IoEventType... eventTypes) {
/* 282 */     Executor executor = createDefaultExecutor(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 286 */     init(executor, true, eventTypes);
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
/*     */   public ExecutorFilter(int maximumPoolSize, IoEventType... eventTypes) {
/* 298 */     Executor executor = createDefaultExecutor(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 302 */     init(executor, true, eventTypes);
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
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, IoEventType... eventTypes) {
/* 315 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 319 */     init(executor, true, eventTypes);
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
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventType... eventTypes) {
/* 335 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), null);
/*     */ 
/*     */ 
/*     */     
/* 339 */     init(executor, true, eventTypes);
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
/*     */ 
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler queueHandler, IoEventType... eventTypes) {
/* 356 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), queueHandler);
/*     */ 
/*     */ 
/*     */     
/* 360 */     init(executor, true, eventTypes);
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
/*     */ 
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventType... eventTypes) {
/* 377 */     Executor executor = createDefaultExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, null);
/*     */ 
/*     */ 
/*     */     
/* 381 */     init(executor, true, eventTypes);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorFilter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler, IoEventType... eventTypes) {
/* 399 */     Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);
/*     */ 
/*     */ 
/*     */     
/* 403 */     init(executor, true, eventTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorFilter(Executor executor) {
/* 413 */     init(executor, false, new IoEventType[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutorFilter(Executor executor, IoEventType... eventTypes) {
/* 424 */     init(executor, false, eventTypes);
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
/*     */ 
/*     */   
/*     */   private Executor createDefaultExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler queueHandler) {
/* 441 */     Executor executor = new OrderedThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, queueHandler);
/*     */ 
/*     */     
/* 444 */     return executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initEventTypes(IoEventType... eventTypes) {
/* 454 */     if (eventTypes == null || eventTypes.length == 0) {
/* 455 */       eventTypes = DEFAULT_EVENT_SET;
/*     */     }
/*     */ 
/*     */     
/* 459 */     this.eventTypes = EnumSet.of(eventTypes[0], eventTypes);
/*     */ 
/*     */     
/* 462 */     if (this.eventTypes.contains(IoEventType.SESSION_CREATED)) {
/* 463 */       this.eventTypes = null;
/* 464 */       throw new IllegalArgumentException(IoEventType.SESSION_CREATED + " is not allowed.");
/*     */     } 
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
/*     */   private void init(Executor executor, boolean manageableExecutor, IoEventType... eventTypes) {
/* 477 */     if (executor == null) {
/* 478 */       throw new IllegalArgumentException("executor");
/*     */     }
/*     */     
/* 481 */     initEventTypes(eventTypes);
/* 482 */     this.executor = executor;
/* 483 */     this.manageableExecutor = manageableExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 492 */     if (this.manageableExecutor) {
/* 493 */       ((ExecutorService)this.executor).shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Executor getExecutor() {
/* 503 */     return this.executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireEvent(IoFilterEvent event) {
/* 512 */     this.executor.execute((Runnable)event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/* 520 */     if (parent.contains((IoFilter)this)) {
/* 521 */       throw new IllegalArgumentException("You can't add the same filter instance more than once.  Create another instance and add it.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) {
/* 531 */     if (this.eventTypes.contains(IoEventType.SESSION_OPENED)) {
/* 532 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_OPENED, session, null);
/* 533 */       fireEvent(event);
/*     */     } else {
/* 535 */       nextFilter.sessionOpened(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) {
/* 544 */     if (this.eventTypes.contains(IoEventType.SESSION_CLOSED)) {
/* 545 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_CLOSED, session, null);
/* 546 */       fireEvent(event);
/*     */     } else {
/* 548 */       nextFilter.sessionClosed(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) {
/* 557 */     if (this.eventTypes.contains(IoEventType.SESSION_IDLE)) {
/* 558 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.SESSION_IDLE, session, status);
/* 559 */       fireEvent(event);
/*     */     } else {
/* 561 */       nextFilter.sessionIdle(session, status);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) {
/* 570 */     if (this.eventTypes.contains(IoEventType.EXCEPTION_CAUGHT)) {
/* 571 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.EXCEPTION_CAUGHT, session, cause);
/* 572 */       fireEvent(event);
/*     */     } else {
/* 574 */       nextFilter.exceptionCaught(session, cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) {
/* 583 */     if (this.eventTypes.contains(IoEventType.MESSAGE_RECEIVED)) {
/* 584 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.MESSAGE_RECEIVED, session, message);
/* 585 */       fireEvent(event);
/*     */     } else {
/* 587 */       nextFilter.messageReceived(session, message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
/* 596 */     if (this.eventTypes.contains(IoEventType.MESSAGE_SENT)) {
/* 597 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.MESSAGE_SENT, session, writeRequest);
/* 598 */       fireEvent(event);
/*     */     } else {
/* 600 */       nextFilter.messageSent(session, writeRequest);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
/* 609 */     if (this.eventTypes.contains(IoEventType.WRITE)) {
/* 610 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.WRITE, session, writeRequest);
/* 611 */       fireEvent(event);
/*     */     } else {
/* 613 */       nextFilter.filterWrite(session, writeRequest);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 622 */     if (this.eventTypes.contains(IoEventType.CLOSE)) {
/* 623 */       IoFilterEvent event = new IoFilterEvent(nextFilter, IoEventType.CLOSE, session, null);
/* 624 */       fireEvent(event);
/*     */     } else {
/* 626 */       nextFilter.filterClose(session);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/executor/ExecutorFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */