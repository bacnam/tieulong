/*     */ package org.apache.mina.core.polling;
/*     */ 
/*     */ import java.net.ConnectException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.core.future.DefaultConnectFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.service.AbstractIoConnector;
/*     */ import org.apache.mina.core.service.AbstractIoService;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.SimpleIoProcessorPool;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.util.ExceptionMonitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPollingIoConnector<T extends AbstractIoSession, H>
/*     */   extends AbstractIoConnector
/*     */ {
/*  67 */   private final Queue<ConnectionRequest> connectQueue = new ConcurrentLinkedQueue<ConnectionRequest>();
/*     */   
/*  69 */   private final Queue<ConnectionRequest> cancelQueue = new ConcurrentLinkedQueue<ConnectionRequest>();
/*     */   
/*     */   private final IoProcessor<T> processor;
/*     */   
/*     */   private final boolean createdProcessor;
/*     */   
/*  75 */   private final AbstractIoService.ServiceOperationFuture disposalFuture = new AbstractIoService.ServiceOperationFuture();
/*     */ 
/*     */   
/*     */   private volatile boolean selectable;
/*     */   
/*  80 */   private final AtomicReference<Connector> connectorRef = new AtomicReference<Connector>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Class<? extends IoProcessor<T>> processorClass) {
/*  96 */     this(sessionConfig, (Executor)null, (IoProcessor<T>)new SimpleIoProcessorPool(processorClass), true);
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
/*     */   protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Class<? extends IoProcessor<T>> processorClass, int processorCount) {
/* 115 */     this(sessionConfig, (Executor)null, (IoProcessor<T>)new SimpleIoProcessorPool(processorClass, processorCount), true);
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
/*     */   protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, IoProcessor<T> processor) {
/* 134 */     this(sessionConfig, (Executor)null, processor, false);
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
/*     */   protected AbstractPollingIoConnector(IoSessionConfig sessionConfig, Executor executor, IoProcessor<T> processor) {
/* 157 */     this(sessionConfig, executor, processor, false);
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
/*     */   private AbstractPollingIoConnector(IoSessionConfig sessionConfig, Executor executor, IoProcessor<T> processor, boolean createdProcessor) {
/* 184 */     super(sessionConfig, executor);
/*     */     
/* 186 */     if (processor == null) {
/* 187 */       throw new IllegalArgumentException("processor");
/*     */     }
/*     */     
/* 190 */     this.processor = processor;
/* 191 */     this.createdProcessor = createdProcessor;
/*     */     
/*     */     try {
/* 194 */       init();
/* 195 */       this.selectable = true;
/* 196 */     } catch (RuntimeException e) {
/* 197 */       throw e;
/* 198 */     } catch (Exception e) {
/* 199 */       throw new RuntimeIoException("Failed to initialize.", e);
/*     */     } finally {
/* 201 */       if (!this.selectable) {
/*     */         try {
/* 203 */           destroy();
/* 204 */         } catch (Exception e) {
/* 205 */           ExceptionMonitor.getInstance().exceptionCaught(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void dispose0() throws Exception {
/* 324 */     startupWorker();
/* 325 */     wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/* 335 */     H handle = null;
/* 336 */     boolean success = false;
/*     */     try {
/* 338 */       handle = newHandle(localAddress);
/* 339 */       if (connect(handle, remoteAddress)) {
/* 340 */         DefaultConnectFuture defaultConnectFuture = new DefaultConnectFuture();
/* 341 */         T session = newSession(this.processor, handle);
/* 342 */         initSession((IoSession)session, (IoFuture)defaultConnectFuture, sessionInitializer);
/*     */         
/* 344 */         session.getProcessor().add((IoSession)session);
/* 345 */         success = true;
/* 346 */         return (ConnectFuture)defaultConnectFuture;
/*     */       } 
/*     */       
/* 349 */       success = true;
/* 350 */     } catch (Exception e) {
/* 351 */       return DefaultConnectFuture.newFailedFuture(e);
/*     */     } finally {
/* 353 */       if (!success && handle != null) {
/*     */         try {
/* 355 */           close(handle);
/* 356 */         } catch (Exception e) {
/* 357 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 362 */     ConnectionRequest request = new ConnectionRequest(handle, sessionInitializer);
/* 363 */     this.connectQueue.add(request);
/* 364 */     startupWorker();
/* 365 */     wakeup();
/*     */     
/* 367 */     return (ConnectFuture)request;
/*     */   }
/*     */   
/*     */   private void startupWorker() {
/* 371 */     if (!this.selectable) {
/* 372 */       this.connectQueue.clear();
/* 373 */       this.cancelQueue.clear();
/*     */     } 
/*     */     
/* 376 */     Connector connector = this.connectorRef.get();
/*     */     
/* 378 */     if (connector == null) {
/* 379 */       connector = new Connector();
/*     */       
/* 381 */       if (this.connectorRef.compareAndSet(null, connector)) {
/* 382 */         executeWorker(connector);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int registerNew() {
/* 388 */     int nHandles = 0;
/*     */     while (true) {
/* 390 */       ConnectionRequest req = this.connectQueue.poll();
/* 391 */       if (req == null) {
/*     */         break;
/*     */       }
/*     */       
/* 395 */       H handle = req.handle;
/*     */       try {
/* 397 */         register(handle, req);
/* 398 */         nHandles++;
/* 399 */       } catch (Exception e) {
/* 400 */         req.setException(e);
/*     */         try {
/* 402 */           close(handle);
/* 403 */         } catch (Exception e2) {
/* 404 */           ExceptionMonitor.getInstance().exceptionCaught(e2);
/*     */         } 
/*     */       } 
/*     */     } 
/* 408 */     return nHandles;
/*     */   }
/*     */   
/*     */   private int cancelKeys() {
/* 412 */     int nHandles = 0;
/*     */     
/*     */     while (true) {
/* 415 */       ConnectionRequest req = this.cancelQueue.poll();
/*     */       
/* 417 */       if (req == null) {
/*     */         break;
/*     */       }
/*     */       
/* 421 */       H handle = req.handle;
/*     */       
/*     */       try {
/* 424 */         close(handle);
/* 425 */       } catch (Exception e) {
/* 426 */         ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */       } finally {
/* 428 */         nHandles++;
/*     */       } 
/*     */     } 
/*     */     
/* 432 */     if (nHandles > 0) {
/* 433 */       wakeup();
/*     */     }
/*     */     
/* 436 */     return nHandles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int processConnections(Iterator<H> handlers) {
/* 444 */     int nHandles = 0;
/*     */ 
/*     */     
/* 447 */     while (handlers.hasNext()) {
/* 448 */       H handle = handlers.next();
/* 449 */       handlers.remove();
/*     */       
/* 451 */       ConnectionRequest connectionRequest = getConnectionRequest(handle);
/*     */       
/* 453 */       if (connectionRequest == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 457 */       boolean success = false;
/*     */       try {
/* 459 */         if (finishConnect(handle)) {
/* 460 */           T session = newSession(this.processor, handle);
/* 461 */           initSession((IoSession)session, (IoFuture)connectionRequest, connectionRequest.getSessionInitializer());
/*     */           
/* 463 */           session.getProcessor().add((IoSession)session);
/* 464 */           nHandles++;
/*     */         } 
/* 466 */         success = true;
/* 467 */       } catch (Exception e) {
/* 468 */         connectionRequest.setException(e);
/*     */       } finally {
/* 470 */         if (!success)
/*     */         {
/* 472 */           this.cancelQueue.offer(connectionRequest);
/*     */         }
/*     */       } 
/*     */     } 
/* 476 */     return nHandles;
/*     */   } protected abstract void init() throws Exception; protected abstract void destroy() throws Exception; protected abstract H newHandle(SocketAddress paramSocketAddress) throws Exception; protected abstract boolean connect(H paramH, SocketAddress paramSocketAddress) throws Exception; protected abstract boolean finishConnect(H paramH) throws Exception; protected abstract T newSession(IoProcessor<T> paramIoProcessor, H paramH) throws Exception;
/*     */   protected abstract void close(H paramH) throws Exception;
/*     */   private void processTimedOutSessions(Iterator<H> handles) {
/* 480 */     long currentTime = System.currentTimeMillis();
/*     */     
/* 482 */     while (handles.hasNext()) {
/* 483 */       H handle = handles.next();
/* 484 */       ConnectionRequest connectionRequest = getConnectionRequest(handle);
/*     */       
/* 486 */       if (connectionRequest != null && currentTime >= connectionRequest.deadline) {
/* 487 */         connectionRequest.setException(new ConnectException("Connection timed out."));
/* 488 */         this.cancelQueue.offer(connectionRequest);
/*     */       } 
/*     */     } 
/*     */   } protected abstract void wakeup(); protected abstract int select(int paramInt) throws Exception; protected abstract Iterator<H> selectedHandles();
/*     */   protected abstract Iterator<H> allHandles();
/*     */   protected abstract void register(H paramH, ConnectionRequest paramConnectionRequest) throws Exception;
/*     */   protected abstract ConnectionRequest getConnectionRequest(H paramH);
/*     */   private class Connector implements Runnable { public void run() {
/* 496 */       assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() == this;
/*     */       
/* 498 */       int nHandles = 0;
/*     */       
/* 500 */       while (AbstractPollingIoConnector.this.selectable) {
/*     */ 
/*     */         
/*     */         try {
/* 504 */           int timeout = (int)Math.min(AbstractPollingIoConnector.this.getConnectTimeoutMillis(), 1000L);
/* 505 */           int selected = AbstractPollingIoConnector.this.select(timeout);
/*     */           
/* 507 */           nHandles += AbstractPollingIoConnector.this.registerNew();
/*     */ 
/*     */           
/* 510 */           if (nHandles == 0) {
/* 511 */             AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).set(null);
/*     */             
/* 513 */             if (AbstractPollingIoConnector.access$600(AbstractPollingIoConnector.this).isEmpty()) {
/* 514 */               assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() != this;
/*     */               
/*     */               break;
/*     */             } 
/* 518 */             if (!AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).compareAndSet(null, this)) {
/* 519 */               assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() != this;
/*     */               
/*     */               break;
/*     */             } 
/* 523 */             assert AbstractPollingIoConnector.access$300(AbstractPollingIoConnector.this).get() == this;
/*     */           } 
/*     */           
/* 526 */           if (selected > 0) {
/* 527 */             nHandles -= AbstractPollingIoConnector.this.processConnections(AbstractPollingIoConnector.this.selectedHandles());
/*     */           }
/*     */           
/* 530 */           AbstractPollingIoConnector.this.processTimedOutSessions(AbstractPollingIoConnector.this.allHandles());
/*     */           
/* 532 */           nHandles -= AbstractPollingIoConnector.this.cancelKeys();
/* 533 */         } catch (ClosedSelectorException cse) {
/*     */           
/* 535 */           ExceptionMonitor.getInstance().exceptionCaught(cse);
/*     */           break;
/* 537 */         } catch (Exception e) {
/* 538 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */           
/*     */           try {
/* 541 */             Thread.sleep(1000L);
/* 542 */           } catch (InterruptedException e1) {
/* 543 */             ExceptionMonitor.getInstance().exceptionCaught(e1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 548 */       if (AbstractPollingIoConnector.this.selectable && AbstractPollingIoConnector.this.isDisposing()) {
/* 549 */         AbstractPollingIoConnector.this.selectable = false;
/*     */         try {
/* 551 */           if (AbstractPollingIoConnector.this.createdProcessor) {
/* 552 */             AbstractPollingIoConnector.this.processor.dispose();
/*     */           }
/*     */         } finally {
/*     */           try {
/* 556 */             synchronized (AbstractPollingIoConnector.this.disposalLock) {
/* 557 */               if (AbstractPollingIoConnector.this.isDisposing()) {
/* 558 */                 AbstractPollingIoConnector.this.destroy();
/*     */               }
/*     */             } 
/* 561 */           } catch (Exception e) {
/* 562 */             ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */           } finally {
/* 564 */             AbstractPollingIoConnector.this.disposalFuture.setDone();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private Connector() {} }
/*     */ 
/*     */   
/*     */   public final class ConnectionRequest extends DefaultConnectFuture {
/*     */     private final H handle;
/*     */     private final long deadline;
/*     */     private final IoSessionInitializer<? extends ConnectFuture> sessionInitializer;
/*     */     
/*     */     public ConnectionRequest(H handle, IoSessionInitializer<? extends ConnectFuture> callback) {
/* 579 */       this.handle = handle;
/* 580 */       long timeout = AbstractPollingIoConnector.this.getConnectTimeoutMillis();
/* 581 */       if (timeout <= 0L) {
/* 582 */         this.deadline = Long.MAX_VALUE;
/*     */       } else {
/* 584 */         this.deadline = System.currentTimeMillis() + timeout;
/*     */       } 
/* 586 */       this.sessionInitializer = callback;
/*     */     }
/*     */     
/*     */     public H getHandle() {
/* 590 */       return this.handle;
/*     */     }
/*     */     
/*     */     public long getDeadline() {
/* 594 */       return this.deadline;
/*     */     }
/*     */     
/*     */     public IoSessionInitializer<? extends ConnectFuture> getSessionInitializer() {
/* 598 */       return this.sessionInitializer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 603 */       if (!isDone()) {
/* 604 */         super.cancel();
/* 605 */         AbstractPollingIoConnector.access$1400(AbstractPollingIoConnector.this).add(this);
/* 606 */         AbstractPollingIoConnector.this.startupWorker();
/* 607 */         AbstractPollingIoConnector.this.wakeup();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/polling/AbstractPollingIoConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */