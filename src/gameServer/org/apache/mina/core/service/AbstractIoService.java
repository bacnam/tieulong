/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.mina.core.IoUtil;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChain;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
/*     */ import org.apache.mina.core.filterchain.IoFilterChainBuilder;
/*     */ import org.apache.mina.core.future.DefaultIoFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.DefaultIoSessionDataStructureFactory;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionDataStructureFactory;
/*     */ import org.apache.mina.core.session.IoSessionInitializationException;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.util.ExceptionMonitor;
/*     */ import org.apache.mina.util.NamePreservingRunnable;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractIoService
/*     */   implements IoService
/*     */ {
/*  64 */   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIoService.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static final AtomicInteger id = new AtomicInteger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String threadName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Executor executor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean createdExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoHandler handler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final IoSessionConfig sessionConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private final IoServiceListener serviceActivationListener = new IoServiceListener()
/*     */     {
/*     */       public void serviceActivated(IoService service) {
/* 105 */         AbstractIoService s = (AbstractIoService)service;
/* 106 */         IoServiceStatistics _stats = s.getStatistics();
/* 107 */         _stats.setLastReadTime(s.getActivationTime());
/* 108 */         _stats.setLastWriteTime(s.getActivationTime());
/* 109 */         _stats.setLastThroughputCalculationTime(s.getActivationTime());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void serviceDeactivated(IoService service) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void serviceIdle(IoService service, IdleStatus idleStatus) throws Exception {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void sessionCreated(IoSession session) throws Exception {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void sessionClosed(IoSession session) throws Exception {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void sessionDestroyed(IoSession session) throws Exception {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 137 */   private IoFilterChainBuilder filterChainBuilder = (IoFilterChainBuilder)new DefaultIoFilterChainBuilder();
/*     */   
/* 139 */   private IoSessionDataStructureFactory sessionDataStructureFactory = (IoSessionDataStructureFactory)new DefaultIoSessionDataStructureFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IoServiceListenerSupport listeners;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   protected final Object disposalLock = new Object();
/*     */ 
/*     */   
/*     */   private volatile boolean disposing;
/*     */ 
/*     */   
/*     */   private volatile boolean disposed;
/*     */ 
/*     */   
/* 159 */   private IoServiceStatistics stats = new IoServiceStatistics(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractIoService(IoSessionConfig sessionConfig, Executor executor) {
/* 174 */     if (sessionConfig == null) {
/* 175 */       throw new IllegalArgumentException("sessionConfig");
/*     */     }
/*     */     
/* 178 */     if (getTransportMetadata() == null) {
/* 179 */       throw new IllegalArgumentException("TransportMetadata");
/*     */     }
/*     */     
/* 182 */     if (!getTransportMetadata().getSessionConfigType().isAssignableFrom(sessionConfig.getClass())) {
/* 183 */       throw new IllegalArgumentException("sessionConfig type: " + sessionConfig.getClass() + " (expected: " + getTransportMetadata().getSessionConfigType() + ")");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     this.listeners = new IoServiceListenerSupport(this);
/* 190 */     this.listeners.add(this.serviceActivationListener);
/*     */ 
/*     */     
/* 193 */     this.sessionConfig = sessionConfig;
/*     */ 
/*     */ 
/*     */     
/* 197 */     ExceptionMonitor.getInstance();
/*     */     
/* 199 */     if (executor == null) {
/* 200 */       this.executor = Executors.newCachedThreadPool();
/* 201 */       this.createdExecutor = true;
/*     */     } else {
/* 203 */       this.executor = executor;
/* 204 */       this.createdExecutor = false;
/*     */     } 
/*     */     
/* 207 */     this.threadName = getClass().getSimpleName() + '-' + id.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IoFilterChainBuilder getFilterChainBuilder() {
/* 214 */     return this.filterChainBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFilterChainBuilder(IoFilterChainBuilder builder) {
/*     */     DefaultIoFilterChainBuilder defaultIoFilterChainBuilder;
/* 221 */     if (builder == null) {
/* 222 */       defaultIoFilterChainBuilder = new DefaultIoFilterChainBuilder();
/*     */     }
/* 224 */     this.filterChainBuilder = (IoFilterChainBuilder)defaultIoFilterChainBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DefaultIoFilterChainBuilder getFilterChain() {
/* 231 */     if (this.filterChainBuilder instanceof DefaultIoFilterChainBuilder) {
/* 232 */       return (DefaultIoFilterChainBuilder)this.filterChainBuilder;
/*     */     }
/*     */     
/* 235 */     throw new IllegalStateException("Current filter chain builder is not a DefaultIoFilterChainBuilder.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(IoServiceListener listener) {
/* 242 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void removeListener(IoServiceListener listener) {
/* 249 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isActive() {
/* 256 */     return this.listeners.isActive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isDisposing() {
/* 263 */     return this.disposing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isDisposed() {
/* 270 */     return this.disposed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 277 */     dispose(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void dispose(boolean awaitTermination) {
/* 284 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/* 288 */     synchronized (this.disposalLock) {
/* 289 */       if (!this.disposing) {
/* 290 */         this.disposing = true;
/*     */         
/*     */         try {
/* 293 */           dispose0();
/* 294 */         } catch (Exception e) {
/* 295 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     if (this.createdExecutor) {
/* 301 */       ExecutorService e = (ExecutorService)this.executor;
/* 302 */       e.shutdownNow();
/* 303 */       if (awaitTermination) {
/*     */         
/*     */         try {
/* 306 */           LOGGER.debug("awaitTermination on {} called by thread=[{}]", this, Thread.currentThread().getName());
/* 307 */           e.awaitTermination(2147483647L, TimeUnit.SECONDS);
/* 308 */           LOGGER.debug("awaitTermination on {} finished", this);
/* 309 */         } catch (InterruptedException e1) {
/* 310 */           LOGGER.warn("awaitTermination on [{}] was interrupted", this);
/*     */           
/* 312 */           Thread.currentThread().interrupt();
/*     */         } 
/*     */       }
/*     */     } 
/* 316 */     this.disposed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void dispose0() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<Long, IoSession> getManagedSessions() {
/* 329 */     return this.listeners.getManagedSessions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getManagedSessionCount() {
/* 336 */     return this.listeners.getManagedSessionCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IoHandler getHandler() {
/* 343 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHandler(IoHandler handler) {
/* 350 */     if (handler == null) {
/* 351 */       throw new IllegalArgumentException("handler cannot be null");
/*     */     }
/*     */     
/* 354 */     if (isActive()) {
/* 355 */       throw new IllegalStateException("handler cannot be set while the service is active.");
/*     */     }
/*     */     
/* 358 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IoSessionDataStructureFactory getSessionDataStructureFactory() {
/* 365 */     return this.sessionDataStructureFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSessionDataStructureFactory(IoSessionDataStructureFactory sessionDataStructureFactory) {
/* 372 */     if (sessionDataStructureFactory == null) {
/* 373 */       throw new IllegalArgumentException("sessionDataStructureFactory");
/*     */     }
/*     */     
/* 376 */     if (isActive()) {
/* 377 */       throw new IllegalStateException("sessionDataStructureFactory cannot be set while the service is active.");
/*     */     }
/*     */     
/* 380 */     this.sessionDataStructureFactory = sessionDataStructureFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoServiceStatistics getStatistics() {
/* 387 */     return this.stats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getActivationTime() {
/* 394 */     return this.listeners.getActivationTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<WriteFuture> broadcast(Object message) {
/* 404 */     final List<WriteFuture> futures = IoUtil.broadcast(message, getManagedSessions().values());
/* 405 */     return new AbstractSet<WriteFuture>()
/*     */       {
/*     */         public Iterator<WriteFuture> iterator() {
/* 408 */           return futures.iterator();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 413 */           return futures.size();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public final IoServiceListenerSupport getListeners() {
/* 419 */     return this.listeners;
/*     */   }
/*     */   
/*     */   protected final void executeWorker(Runnable worker) {
/* 423 */     executeWorker(worker, null);
/*     */   }
/*     */   
/*     */   protected final void executeWorker(Runnable worker, String suffix) {
/* 427 */     String actualThreadName = this.threadName;
/* 428 */     if (suffix != null) {
/* 429 */       actualThreadName = actualThreadName + '-' + suffix;
/*     */     }
/* 431 */     this.executor.execute((Runnable)new NamePreservingRunnable(worker, actualThreadName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void initSession(IoSession session, IoFuture future, IoSessionInitializer sessionInitializer) {
/* 438 */     if (this.stats.getLastReadTime() == 0L) {
/* 439 */       this.stats.setLastReadTime(getActivationTime());
/*     */     }
/*     */     
/* 442 */     if (this.stats.getLastWriteTime() == 0L) {
/* 443 */       this.stats.setLastWriteTime(getActivationTime());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 451 */       ((AbstractIoSession)session).setAttributeMap(session.getService().getSessionDataStructureFactory().getAttributeMap(session));
/*     */     }
/* 453 */     catch (IoSessionInitializationException e) {
/* 454 */       throw e;
/* 455 */     } catch (Exception e) {
/* 456 */       throw new IoSessionInitializationException("Failed to initialize an attributeMap.", e);
/*     */     } 
/*     */     
/*     */     try {
/* 460 */       ((AbstractIoSession)session).setWriteRequestQueue(session.getService().getSessionDataStructureFactory().getWriteRequestQueue(session));
/*     */     }
/* 462 */     catch (IoSessionInitializationException e) {
/* 463 */       throw e;
/* 464 */     } catch (Exception e) {
/* 465 */       throw new IoSessionInitializationException("Failed to initialize a writeRequestQueue.", e);
/*     */     } 
/*     */     
/* 468 */     if (future != null && future instanceof org.apache.mina.core.future.ConnectFuture)
/*     */     {
/* 470 */       session.setAttribute(DefaultIoFilterChain.SESSION_CREATED_FUTURE, future);
/*     */     }
/*     */     
/* 473 */     if (sessionInitializer != null) {
/* 474 */       sessionInitializer.initializeSession(session, future);
/*     */     }
/*     */     
/* 477 */     finishSessionInitialization0(session, future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finishSessionInitialization0(IoSession session, IoFuture future) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ServiceOperationFuture
/*     */     extends DefaultIoFuture
/*     */   {
/*     */     public ServiceOperationFuture() {
/* 492 */       super(null);
/*     */     }
/*     */     
/*     */     public final boolean isDone() {
/* 496 */       return (getValue() == Boolean.TRUE);
/*     */     }
/*     */     
/*     */     public final void setDone() {
/* 500 */       setValue(Boolean.TRUE);
/*     */     }
/*     */     
/*     */     public final Exception getException() {
/* 504 */       if (getValue() instanceof Exception) {
/* 505 */         return (Exception)getValue();
/*     */       }
/*     */       
/* 508 */       return null;
/*     */     }
/*     */     
/*     */     public final void setException(Exception exception) {
/* 512 */       if (exception == null) {
/* 513 */         throw new IllegalArgumentException("exception");
/*     */       }
/* 515 */       setValue(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScheduledWriteBytes() {
/* 523 */     return this.stats.getScheduledWriteBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScheduledWriteMessages() {
/* 530 */     return this.stats.getScheduledWriteMessages();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/AbstractIoService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */