/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOReactor;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorExceptionHandler;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.params.BasicHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public abstract class AbstractMultiworkerIOReactor
/*     */   implements IOReactor
/*     */ {
/*     */   protected volatile IOReactorStatus status;
/*     */   @Deprecated
/*     */   protected final HttpParams params;
/*     */   protected final IOReactorConfig config;
/*     */   protected final Selector selector;
/*     */   protected final long selectTimeout;
/*     */   protected final boolean interestOpsQueueing;
/*     */   private final int workerCount;
/*     */   private final ThreadFactory threadFactory;
/*     */   private final BaseIOReactor[] dispatchers;
/*     */   private final Worker[] workers;
/*     */   private final Thread[] threads;
/*     */   private final Object statusLock;
/*     */   protected IOReactorExceptionHandler exceptionHandler;
/*     */   protected List<ExceptionEvent> auditLog;
/* 125 */   private int currentWorker = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMultiworkerIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
/* 141 */     this.config = (config != null) ? config : IOReactorConfig.DEFAULT;
/* 142 */     this.params = (HttpParams)new BasicHttpParams();
/*     */     try {
/* 144 */       this.selector = Selector.open();
/* 145 */     } catch (IOException ex) {
/* 146 */       throw new IOReactorException("Failure opening selector", ex);
/*     */     } 
/* 148 */     this.selectTimeout = this.config.getSelectInterval();
/* 149 */     this.interestOpsQueueing = this.config.isInterestOpQueued();
/* 150 */     this.statusLock = new Object();
/* 151 */     if (threadFactory != null) {
/* 152 */       this.threadFactory = threadFactory;
/*     */     } else {
/* 154 */       this.threadFactory = new DefaultThreadFactory();
/*     */     } 
/* 156 */     this.auditLog = new ArrayList<ExceptionEvent>();
/* 157 */     this.workerCount = this.config.getIoThreadCount();
/* 158 */     this.dispatchers = new BaseIOReactor[this.workerCount];
/* 159 */     this.workers = new Worker[this.workerCount];
/* 160 */     this.threads = new Thread[this.workerCount];
/* 161 */     this.status = IOReactorStatus.INACTIVE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMultiworkerIOReactor() throws IOReactorException {
/* 172 */     this(null, null);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   static IOReactorConfig convert(int workerCount, HttpParams params) {
/* 177 */     Args.notNull(params, "HTTP parameters");
/* 178 */     return IOReactorConfig.custom().setSelectInterval(params.getLongParameter("http.nio.select-interval", 1000L)).setShutdownGracePeriod(params.getLongParameter("http.nio.grace-period", 500L)).setInterestOpQueued(params.getBooleanParameter("http.nio.select-interval", false)).setIoThreadCount(workerCount).setSoTimeout(params.getIntParameter("http.socket.timeout", 0)).setConnectTimeout(params.getIntParameter("http.connection.timeout", 0)).setSoTimeout(params.getIntParameter("http.socket.timeout", 0)).setSoReuseAddress(params.getBooleanParameter("http.socket.reuseaddr", false)).setSoKeepAlive(params.getBooleanParameter("http.socket.keepalive", false)).setSoLinger(params.getIntParameter("http.socket.linger", -1)).setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true)).build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AbstractMultiworkerIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
/* 209 */     this(convert(workerCount, params), threadFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 214 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExceptionEvent> getAuditLog() {
/* 224 */     synchronized (this.auditLog) {
/* 225 */       return new ArrayList<ExceptionEvent>(this.auditLog);
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
/*     */   protected synchronized void addExceptionEvent(Throwable ex, Date timestamp) {
/* 238 */     if (ex == null) {
/*     */       return;
/*     */     }
/* 241 */     synchronized (this.auditLog) {
/* 242 */       this.auditLog.add(new ExceptionEvent(ex, (timestamp != null) ? timestamp : new Date()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addExceptionEvent(Throwable ex) {
/* 252 */     addExceptionEvent(ex, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(IOReactorExceptionHandler exceptionHandler) {
/* 261 */     this.exceptionHandler = exceptionHandler;
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
/*     */   protected abstract void processEvents(int paramInt) throws IOReactorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void cancelRequests() throws IOReactorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(IOEventDispatch eventDispatch) throws InterruptedIOException, IOReactorException {
/* 308 */     Args.notNull(eventDispatch, "Event dispatcher");
/* 309 */     synchronized (this.statusLock) {
/* 310 */       if (this.status.compareTo((Enum)IOReactorStatus.SHUTDOWN_REQUEST) >= 0) {
/* 311 */         this.status = IOReactorStatus.SHUT_DOWN;
/* 312 */         this.statusLock.notifyAll();
/*     */         return;
/*     */       } 
/* 315 */       Asserts.check((this.status.compareTo((Enum)IOReactorStatus.INACTIVE) == 0), "Illegal state %s", this.status);
/*     */       
/* 317 */       this.status = IOReactorStatus.ACTIVE;
/*     */       int i;
/* 319 */       for (i = 0; i < this.dispatchers.length; i++) {
/* 320 */         BaseIOReactor dispatcher = new BaseIOReactor(this.selectTimeout, this.interestOpsQueueing);
/* 321 */         dispatcher.setExceptionHandler(this.exceptionHandler);
/* 322 */         this.dispatchers[i] = dispatcher;
/*     */       } 
/* 324 */       for (i = 0; i < this.workerCount; i++) {
/* 325 */         BaseIOReactor dispatcher = this.dispatchers[i];
/* 326 */         this.workers[i] = new Worker(dispatcher, eventDispatch);
/* 327 */         this.threads[i] = this.threadFactory.newThread(this.workers[i]);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 332 */       for (int i = 0; i < this.workerCount; i++) {
/* 333 */         if (this.status != IOReactorStatus.ACTIVE) {
/*     */           return;
/*     */         }
/* 336 */         this.threads[i].start();
/*     */       } 
/*     */       
/*     */       do {
/*     */         int readyCount;
/*     */         try {
/* 342 */           readyCount = this.selector.select(this.selectTimeout);
/* 343 */         } catch (InterruptedIOException ex) {
/* 344 */           throw ex;
/* 345 */         } catch (IOException ex) {
/* 346 */           throw new IOReactorException("Unexpected selector failure", ex);
/*     */         } 
/*     */         
/* 349 */         if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) == 0) {
/* 350 */           processEvents(readyCount);
/*     */         }
/*     */ 
/*     */         
/* 354 */         for (int j = 0; j < this.workerCount; j++) {
/* 355 */           Worker worker = this.workers[j];
/* 356 */           Exception ex = worker.getException();
/* 357 */           if (ex != null) {
/* 358 */             throw new IOReactorException("I/O dispatch worker terminated abnormally", ex);
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 363 */       while (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 368 */     catch (ClosedSelectorException ex) {
/* 369 */       addExceptionEvent(ex);
/* 370 */     } catch (IOReactorException ex) {
/* 371 */       if (ex.getCause() != null) {
/* 372 */         addExceptionEvent(ex.getCause());
/*     */       }
/* 374 */       throw ex;
/*     */     } finally {
/* 376 */       doShutdown();
/* 377 */       synchronized (this.statusLock) {
/* 378 */         this.status = IOReactorStatus.SHUT_DOWN;
/* 379 */         this.statusLock.notifyAll();
/*     */       } 
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
/*     */ 
/*     */   
/*     */   protected void doShutdown() throws InterruptedIOException {
/* 395 */     synchronized (this.statusLock) {
/* 396 */       if (this.status.compareTo((Enum)IOReactorStatus.SHUTTING_DOWN) >= 0) {
/*     */         return;
/*     */       }
/* 399 */       this.status = IOReactorStatus.SHUTTING_DOWN;
/*     */     } 
/*     */     try {
/* 402 */       cancelRequests();
/* 403 */     } catch (IOReactorException ex) {
/* 404 */       if (ex.getCause() != null) {
/* 405 */         addExceptionEvent(ex.getCause());
/*     */       }
/*     */     } 
/* 408 */     this.selector.wakeup();
/*     */ 
/*     */     
/* 411 */     if (this.selector.isOpen()) {
/* 412 */       for (SelectionKey key : this.selector.keys()) {
/*     */         try {
/* 414 */           Channel channel = key.channel();
/* 415 */           if (channel != null) {
/* 416 */             channel.close();
/*     */           }
/* 418 */         } catch (IOException ex) {
/* 419 */           addExceptionEvent(ex);
/*     */         } 
/*     */       } 
/*     */       
/*     */       try {
/* 424 */         this.selector.close();
/* 425 */       } catch (IOException ex) {
/* 426 */         addExceptionEvent(ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 431 */     for (int i = 0; i < this.workerCount; i++) {
/* 432 */       BaseIOReactor dispatcher = this.dispatchers[i];
/* 433 */       dispatcher.gracefulShutdown();
/*     */     } 
/*     */     
/* 436 */     long gracePeriod = this.config.getShutdownGracePeriod();
/*     */     
/*     */     try {
/*     */       int j;
/*     */       
/* 441 */       for (j = 0; j < this.workerCount; j++) {
/* 442 */         BaseIOReactor dispatcher = this.dispatchers[j];
/* 443 */         if (dispatcher.getStatus() != IOReactorStatus.INACTIVE) {
/* 444 */           dispatcher.awaitShutdown(gracePeriod);
/*     */         }
/* 446 */         if (dispatcher.getStatus() != IOReactorStatus.SHUT_DOWN) {
/*     */           try {
/* 448 */             dispatcher.hardShutdown();
/* 449 */           } catch (IOReactorException ex) {
/* 450 */             if (ex.getCause() != null) {
/* 451 */               addExceptionEvent(ex.getCause());
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 457 */       for (j = 0; j < this.workerCount; j++) {
/* 458 */         Thread t = this.threads[j];
/* 459 */         if (t != null) {
/* 460 */           t.join(gracePeriod);
/*     */         }
/*     */       } 
/* 463 */     } catch (InterruptedException ex) {
/* 464 */       throw new InterruptedIOException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addChannel(ChannelEntry entry) {
/* 475 */     int i = Math.abs(this.currentWorker++ % this.workerCount);
/* 476 */     this.dispatchers[i].addChannel(entry);
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
/*     */   protected SelectionKey registerChannel(SelectableChannel channel, int ops) throws ClosedChannelException {
/* 489 */     return channel.register(this.selector, ops);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareSocket(Socket socket) throws IOException {
/* 499 */     socket.setTcpNoDelay(this.config.isTcpNoDelay());
/* 500 */     socket.setKeepAlive(this.config.isSoKeepalive());
/* 501 */     if (this.config.getSoTimeout() > 0) {
/* 502 */       socket.setSoTimeout(this.config.getSoTimeout());
/*     */     }
/* 504 */     if (this.config.getSndBufSize() > 0) {
/* 505 */       socket.setSendBufferSize(this.config.getSndBufSize());
/*     */     }
/* 507 */     if (this.config.getRcvBufSize() > 0) {
/* 508 */       socket.setReceiveBufferSize(this.config.getRcvBufSize());
/*     */     }
/* 510 */     int linger = this.config.getSoLinger();
/* 511 */     if (linger >= 0) {
/* 512 */       socket.setSoLinger(true, linger);
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
/*     */   
/*     */   protected void awaitShutdown(long timeout) throws InterruptedException {
/* 526 */     synchronized (this.statusLock) {
/* 527 */       long deadline = System.currentTimeMillis() + timeout;
/* 528 */       long remaining = timeout;
/* 529 */       while (this.status != IOReactorStatus.SHUT_DOWN) {
/* 530 */         this.statusLock.wait(remaining);
/* 531 */         if (timeout > 0L) {
/* 532 */           remaining = deadline - System.currentTimeMillis();
/* 533 */           if (remaining <= 0L) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 543 */     shutdown(2000L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown(long waitMs) throws IOException {
/* 548 */     synchronized (this.statusLock) {
/* 549 */       if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) > 0) {
/*     */         return;
/*     */       }
/* 552 */       if (this.status.compareTo((Enum)IOReactorStatus.INACTIVE) == 0) {
/* 553 */         this.status = IOReactorStatus.SHUT_DOWN;
/* 554 */         cancelRequests();
/* 555 */         this.selector.close();
/*     */         return;
/*     */       } 
/* 558 */       this.status = IOReactorStatus.SHUTDOWN_REQUEST;
/*     */     } 
/* 560 */     this.selector.wakeup();
/*     */     try {
/* 562 */       awaitShutdown(waitMs);
/* 563 */     } catch (InterruptedException ignore) {}
/*     */   }
/*     */ 
/*     */   
/*     */   static void closeChannel(Channel channel) {
/*     */     try {
/* 569 */       channel.close();
/* 570 */     } catch (IOException ignore) {}
/*     */   }
/*     */ 
/*     */   
/*     */   static class Worker
/*     */     implements Runnable
/*     */   {
/*     */     final BaseIOReactor dispatcher;
/*     */     
/*     */     final IOEventDispatch eventDispatch;
/*     */     private volatile Exception exception;
/*     */     
/*     */     public Worker(BaseIOReactor dispatcher, IOEventDispatch eventDispatch) {
/* 583 */       this.dispatcher = dispatcher;
/* 584 */       this.eventDispatch = eventDispatch;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 590 */         this.dispatcher.execute(this.eventDispatch);
/* 591 */       } catch (Exception ex) {
/* 592 */         this.exception = ex;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Exception getException() {
/* 597 */       return this.exception;
/*     */     }
/*     */   }
/*     */   
/*     */   static class DefaultThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/* 604 */     private static final AtomicLong COUNT = new AtomicLong(1L);
/*     */ 
/*     */     
/*     */     public Thread newThread(Runnable r) {
/* 608 */       return new Thread(r, "I/O dispatcher " + COUNT.getAndIncrement());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/AbstractMultiworkerIOReactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */