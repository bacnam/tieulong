/*     */ package org.apache.mina.core.polling;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.service.AbstractIoAcceptor;
/*     */ import org.apache.mina.core.service.AbstractIoService;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.SimpleIoProcessorPool;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
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
/*     */ public abstract class AbstractPollingIoAcceptor<S extends AbstractIoSession, H>
/*     */   extends AbstractIoAcceptor
/*     */ {
/*  73 */   private final Semaphore lock = new Semaphore(1);
/*     */   
/*     */   private final IoProcessor<S> processor;
/*     */   
/*     */   private final boolean createdProcessor;
/*     */   
/*  79 */   private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> registerQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();
/*     */   
/*  81 */   private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> cancelQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();
/*     */   
/*  83 */   private final Map<SocketAddress, H> boundHandles = Collections.synchronizedMap(new HashMap<SocketAddress, H>());
/*     */   
/*  85 */   private final AbstractIoService.ServiceOperationFuture disposalFuture = new AbstractIoService.ServiceOperationFuture();
/*     */ 
/*     */   
/*     */   private volatile boolean selectable;
/*     */ 
/*     */   
/*  91 */   private AtomicReference<Acceptor> acceptorRef = new AtomicReference<Acceptor>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean reuseAddress = false;
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected int backlog = 50;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass) {
/* 115 */     this(sessionConfig, (Executor)null, (IoProcessor<S>)new SimpleIoProcessorPool(processorClass), true, (SelectorProvider)null);
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
/*     */   protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount) {
/* 134 */     this(sessionConfig, (Executor)null, (IoProcessor<S>)new SimpleIoProcessorPool(processorClass, processorCount), true, (SelectorProvider)null);
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
/*     */   protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount, SelectorProvider selectorProvider) {
/* 154 */     this(sessionConfig, (Executor)null, (IoProcessor<S>)new SimpleIoProcessorPool(processorClass, processorCount, selectorProvider), true, selectorProvider);
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
/*     */   protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, IoProcessor<S> processor) {
/* 170 */     this(sessionConfig, (Executor)null, processor, false, (SelectorProvider)null);
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
/*     */   protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor) {
/* 193 */     this(sessionConfig, executor, processor, false, (SelectorProvider)null);
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
/*     */   private AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor, boolean createdProcessor, SelectorProvider selectorProvider) {
/* 220 */     super(sessionConfig, executor);
/*     */     
/* 222 */     if (processor == null) {
/* 223 */       throw new IllegalArgumentException("processor");
/*     */     }
/*     */     
/* 226 */     this.processor = processor;
/* 227 */     this.createdProcessor = createdProcessor;
/*     */ 
/*     */     
/*     */     try {
/* 231 */       init(selectorProvider);
/*     */ 
/*     */ 
/*     */       
/* 235 */       this.selectable = true;
/* 236 */     } catch (RuntimeException e) {
/* 237 */       throw e;
/* 238 */     } catch (Exception e) {
/* 239 */       throw new RuntimeIoException("Failed to initialize.", e);
/*     */     } finally {
/* 241 */       if (!this.selectable) {
/*     */         try {
/* 243 */           destroy();
/* 244 */         } catch (Exception e) {
/* 245 */           ExceptionMonitor.getInstance().exceptionCaught(e);
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
/*     */   protected void dispose0() throws Exception {
/* 328 */     unbind();
/*     */     
/* 330 */     startupAcceptor();
/* 331 */     wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws Exception {
/* 341 */     AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);
/*     */ 
/*     */ 
/*     */     
/* 345 */     this.registerQueue.add(request);
/*     */ 
/*     */ 
/*     */     
/* 349 */     startupAcceptor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 355 */       this.lock.acquire();
/*     */ 
/*     */       
/* 358 */       Thread.sleep(10L);
/* 359 */       wakeup();
/*     */     } finally {
/* 361 */       this.lock.release();
/*     */     } 
/*     */ 
/*     */     
/* 365 */     request.awaitUninterruptibly();
/*     */     
/* 367 */     if (request.getException() != null) {
/* 368 */       throw request.getException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     Set<SocketAddress> newLocalAddresses = new HashSet<SocketAddress>();
/*     */     
/* 376 */     for (H handle : this.boundHandles.values()) {
/* 377 */       newLocalAddresses.add(localAddress(handle));
/*     */     }
/*     */     
/* 380 */     return newLocalAddresses;
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
/*     */   private void startupAcceptor() throws InterruptedException {
/* 394 */     if (!this.selectable) {
/* 395 */       this.registerQueue.clear();
/* 396 */       this.cancelQueue.clear();
/*     */     } 
/*     */ 
/*     */     
/* 400 */     Acceptor acceptor = this.acceptorRef.get();
/*     */     
/* 402 */     if (acceptor == null) {
/* 403 */       this.lock.acquire();
/* 404 */       acceptor = new Acceptor();
/*     */       
/* 406 */       if (this.acceptorRef.compareAndSet(null, acceptor)) {
/* 407 */         executeWorker(acceptor);
/*     */       } else {
/* 409 */         this.lock.release();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void unbind0(List<? extends SocketAddress> localAddresses) throws Exception {
/* 419 */     AbstractIoAcceptor.AcceptorOperationFuture future = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);
/*     */     
/* 421 */     this.cancelQueue.add(future);
/* 422 */     startupAcceptor();
/* 423 */     wakeup();
/*     */     
/* 425 */     future.awaitUninterruptibly();
/* 426 */     if (future.getException() != null) {
/* 427 */       throw future.getException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class Acceptor
/*     */     implements Runnable
/*     */   {
/*     */     private Acceptor() {}
/*     */ 
/*     */     
/*     */     public void run() {
/* 439 */       assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() == this;
/*     */       
/* 441 */       int nHandles = 0;
/*     */ 
/*     */       
/* 444 */       AbstractPollingIoAcceptor.this.lock.release();
/*     */       
/* 446 */       while (AbstractPollingIoAcceptor.this.selectable) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 452 */           int selected = AbstractPollingIoAcceptor.this.select();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 457 */           nHandles += AbstractPollingIoAcceptor.this.registerHandles();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 462 */           if (nHandles == 0) {
/* 463 */             AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).set(null);
/*     */             
/* 465 */             if (AbstractPollingIoAcceptor.this.registerQueue.isEmpty() && AbstractPollingIoAcceptor.this.cancelQueue.isEmpty()) {
/* 466 */               assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() != this;
/*     */               
/*     */               break;
/*     */             } 
/* 470 */             if (!AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).compareAndSet(null, this)) {
/* 471 */               assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() != this;
/*     */               
/*     */               break;
/*     */             } 
/* 475 */             assert AbstractPollingIoAcceptor.access$100(AbstractPollingIoAcceptor.this).get() == this;
/*     */           } 
/*     */           
/* 478 */           if (selected > 0)
/*     */           {
/*     */             
/* 481 */             processHandles(AbstractPollingIoAcceptor.this.selectedHandles());
/*     */           }
/*     */ 
/*     */           
/* 485 */           nHandles -= AbstractPollingIoAcceptor.this.unregisterHandles();
/* 486 */         } catch (ClosedSelectorException cse) {
/*     */           
/* 488 */           ExceptionMonitor.getInstance().exceptionCaught(cse);
/*     */           break;
/* 490 */         } catch (Exception e) {
/* 491 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */           
/*     */           try {
/* 494 */             Thread.sleep(1000L);
/* 495 */           } catch (InterruptedException e1) {
/* 496 */             ExceptionMonitor.getInstance().exceptionCaught(e1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 502 */       if (AbstractPollingIoAcceptor.this.selectable && AbstractPollingIoAcceptor.this.isDisposing()) {
/* 503 */         AbstractPollingIoAcceptor.this.selectable = false;
/*     */         try {
/* 505 */           if (AbstractPollingIoAcceptor.this.createdProcessor) {
/* 506 */             AbstractPollingIoAcceptor.this.processor.dispose();
/*     */           }
/*     */         } finally {
/*     */           try {
/* 510 */             synchronized (AbstractPollingIoAcceptor.this.disposalLock) {
/* 511 */               if (AbstractPollingIoAcceptor.this.isDisposing()) {
/* 512 */                 AbstractPollingIoAcceptor.this.destroy();
/*     */               }
/*     */             } 
/* 515 */           } catch (Exception e) {
/* 516 */             ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */           } finally {
/* 518 */             AbstractPollingIoAcceptor.this.disposalFuture.setDone();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void processHandles(Iterator<H> handles) throws Exception {
/* 535 */       while (handles.hasNext()) {
/* 536 */         H handle = handles.next();
/* 537 */         handles.remove();
/*     */ 
/*     */ 
/*     */         
/* 541 */         S session = AbstractPollingIoAcceptor.this.accept(AbstractPollingIoAcceptor.this.processor, handle);
/*     */         
/* 543 */         if (session == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 547 */         AbstractPollingIoAcceptor.this.initSession((IoSession)session, null, null);
/*     */ 
/*     */         
/* 550 */         session.getProcessor().add((IoSession)session);
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
/*     */   private int registerHandles() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield registerQueue : Ljava/util/Queue;
/*     */     //   4: invokeinterface poll : ()Ljava/lang/Object;
/*     */     //   9: checkcast org/apache/mina/core/service/AbstractIoAcceptor$AcceptorOperationFuture
/*     */     //   12: astore_1
/*     */     //   13: aload_1
/*     */     //   14: ifnonnull -> 19
/*     */     //   17: iconst_0
/*     */     //   18: ireturn
/*     */     //   19: new java/util/concurrent/ConcurrentHashMap
/*     */     //   22: dup
/*     */     //   23: invokespecial <init> : ()V
/*     */     //   26: astore_2
/*     */     //   27: aload_1
/*     */     //   28: invokevirtual getLocalAddresses : ()Ljava/util/List;
/*     */     //   31: astore_3
/*     */     //   32: aload_3
/*     */     //   33: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   38: astore #4
/*     */     //   40: aload #4
/*     */     //   42: invokeinterface hasNext : ()Z
/*     */     //   47: ifeq -> 88
/*     */     //   50: aload #4
/*     */     //   52: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   57: checkcast java/net/SocketAddress
/*     */     //   60: astore #5
/*     */     //   62: aload_0
/*     */     //   63: aload #5
/*     */     //   65: invokevirtual open : (Ljava/net/SocketAddress;)Ljava/lang/Object;
/*     */     //   68: astore #6
/*     */     //   70: aload_2
/*     */     //   71: aload_0
/*     */     //   72: aload #6
/*     */     //   74: invokevirtual localAddress : (Ljava/lang/Object;)Ljava/net/SocketAddress;
/*     */     //   77: aload #6
/*     */     //   79: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   84: pop
/*     */     //   85: goto -> 40
/*     */     //   88: aload_0
/*     */     //   89: getfield boundHandles : Ljava/util/Map;
/*     */     //   92: aload_2
/*     */     //   93: invokeinterface putAll : (Ljava/util/Map;)V
/*     */     //   98: aload_1
/*     */     //   99: invokevirtual setDone : ()V
/*     */     //   102: aload_2
/*     */     //   103: invokeinterface size : ()I
/*     */     //   108: istore #4
/*     */     //   110: aload_1
/*     */     //   111: invokevirtual getException : ()Ljava/lang/Exception;
/*     */     //   114: ifnull -> 175
/*     */     //   117: aload_2
/*     */     //   118: invokeinterface values : ()Ljava/util/Collection;
/*     */     //   123: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   128: astore #5
/*     */     //   130: aload #5
/*     */     //   132: invokeinterface hasNext : ()Z
/*     */     //   137: ifeq -> 171
/*     */     //   140: aload #5
/*     */     //   142: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   147: astore #6
/*     */     //   149: aload_0
/*     */     //   150: aload #6
/*     */     //   152: invokevirtual close : (Ljava/lang/Object;)V
/*     */     //   155: goto -> 168
/*     */     //   158: astore #7
/*     */     //   160: invokestatic getInstance : ()Lorg/apache/mina/util/ExceptionMonitor;
/*     */     //   163: aload #7
/*     */     //   165: invokevirtual exceptionCaught : (Ljava/lang/Throwable;)V
/*     */     //   168: goto -> 130
/*     */     //   171: aload_0
/*     */     //   172: invokevirtual wakeup : ()V
/*     */     //   175: iload #4
/*     */     //   177: ireturn
/*     */     //   178: astore #4
/*     */     //   180: aload_1
/*     */     //   181: aload #4
/*     */     //   183: invokevirtual setException : (Ljava/lang/Exception;)V
/*     */     //   186: aload_1
/*     */     //   187: invokevirtual getException : ()Ljava/lang/Exception;
/*     */     //   190: ifnull -> 324
/*     */     //   193: aload_2
/*     */     //   194: invokeinterface values : ()Ljava/util/Collection;
/*     */     //   199: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   204: astore #4
/*     */     //   206: aload #4
/*     */     //   208: invokeinterface hasNext : ()Z
/*     */     //   213: ifeq -> 247
/*     */     //   216: aload #4
/*     */     //   218: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   223: astore #5
/*     */     //   225: aload_0
/*     */     //   226: aload #5
/*     */     //   228: invokevirtual close : (Ljava/lang/Object;)V
/*     */     //   231: goto -> 244
/*     */     //   234: astore #6
/*     */     //   236: invokestatic getInstance : ()Lorg/apache/mina/util/ExceptionMonitor;
/*     */     //   239: aload #6
/*     */     //   241: invokevirtual exceptionCaught : (Ljava/lang/Throwable;)V
/*     */     //   244: goto -> 206
/*     */     //   247: aload_0
/*     */     //   248: invokevirtual wakeup : ()V
/*     */     //   251: goto -> 324
/*     */     //   254: astore #8
/*     */     //   256: aload_1
/*     */     //   257: invokevirtual getException : ()Ljava/lang/Exception;
/*     */     //   260: ifnull -> 321
/*     */     //   263: aload_2
/*     */     //   264: invokeinterface values : ()Ljava/util/Collection;
/*     */     //   269: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   274: astore #9
/*     */     //   276: aload #9
/*     */     //   278: invokeinterface hasNext : ()Z
/*     */     //   283: ifeq -> 317
/*     */     //   286: aload #9
/*     */     //   288: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   293: astore #10
/*     */     //   295: aload_0
/*     */     //   296: aload #10
/*     */     //   298: invokevirtual close : (Ljava/lang/Object;)V
/*     */     //   301: goto -> 314
/*     */     //   304: astore #11
/*     */     //   306: invokestatic getInstance : ()Lorg/apache/mina/util/ExceptionMonitor;
/*     */     //   309: aload #11
/*     */     //   311: invokevirtual exceptionCaught : (Ljava/lang/Throwable;)V
/*     */     //   314: goto -> 276
/*     */     //   317: aload_0
/*     */     //   318: invokevirtual wakeup : ()V
/*     */     //   321: aload #8
/*     */     //   323: athrow
/*     */     //   324: goto -> 0
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #568	-> 0
/*     */     //   #570	-> 13
/*     */     //   #571	-> 17
/*     */     //   #577	-> 19
/*     */     //   #578	-> 27
/*     */     //   #582	-> 32
/*     */     //   #583	-> 62
/*     */     //   #584	-> 70
/*     */     //   #585	-> 85
/*     */     //   #589	-> 88
/*     */     //   #592	-> 98
/*     */     //   #593	-> 102
/*     */     //   #599	-> 110
/*     */     //   #600	-> 117
/*     */     //   #602	-> 149
/*     */     //   #605	-> 155
/*     */     //   #603	-> 158
/*     */     //   #604	-> 160
/*     */     //   #606	-> 168
/*     */     //   #609	-> 171
/*     */     //   #594	-> 178
/*     */     //   #596	-> 180
/*     */     //   #599	-> 186
/*     */     //   #600	-> 193
/*     */     //   #602	-> 225
/*     */     //   #605	-> 231
/*     */     //   #603	-> 234
/*     */     //   #604	-> 236
/*     */     //   #606	-> 244
/*     */     //   #609	-> 247
/*     */     //   #599	-> 254
/*     */     //   #600	-> 263
/*     */     //   #602	-> 295
/*     */     //   #605	-> 301
/*     */     //   #603	-> 304
/*     */     //   #604	-> 306
/*     */     //   #606	-> 314
/*     */     //   #609	-> 317
/*     */     //   #612	-> 324
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   70	15	6	handle	Ljava/lang/Object;
/*     */     //   62	23	5	a	Ljava/net/SocketAddress;
/*     */     //   40	48	4	i$	Ljava/util/Iterator;
/*     */     //   160	8	7	e	Ljava/lang/Exception;
/*     */     //   149	19	6	handle	Ljava/lang/Object;
/*     */     //   130	41	5	i$	Ljava/util/Iterator;
/*     */     //   180	6	4	e	Ljava/lang/Exception;
/*     */     //   236	8	6	e	Ljava/lang/Exception;
/*     */     //   225	19	5	handle	Ljava/lang/Object;
/*     */     //   206	41	4	i$	Ljava/util/Iterator;
/*     */     //   306	8	11	e	Ljava/lang/Exception;
/*     */     //   295	19	10	handle	Ljava/lang/Object;
/*     */     //   276	41	9	i$	Ljava/util/Iterator;
/*     */     //   13	311	1	future	Lorg/apache/mina/core/service/AbstractIoAcceptor$AcceptorOperationFuture;
/*     */     //   27	297	2	newHandles	Ljava/util/Map;
/*     */     //   32	292	3	localAddresses	Ljava/util/List;
/*     */     //   0	327	0	this	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   70	15	6	handle	TH;
/*     */     //   149	19	6	handle	TH;
/*     */     //   225	19	5	handle	TH;
/*     */     //   295	19	10	handle	TH;
/*     */     //   27	297	2	newHandles	Ljava/util/Map<Ljava/net/SocketAddress;TH;>;
/*     */     //   32	292	3	localAddresses	Ljava/util/List<Ljava/net/SocketAddress;>;
/*     */     //   0	327	0	this	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor<TS;TH;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   32	110	178	java/lang/Exception
/*     */     //   32	110	254	finally
/*     */     //   149	155	158	java/lang/Exception
/*     */     //   178	186	254	finally
/*     */     //   225	231	234	java/lang/Exception
/*     */     //   254	256	254	finally
/*     */     //   295	301	304	java/lang/Exception
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
/*     */   private int unregisterHandles() {
/* 622 */     int cancelledHandles = 0;
/*     */     while (true) {
/* 624 */       AbstractIoAcceptor.AcceptorOperationFuture future = this.cancelQueue.poll();
/* 625 */       if (future == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 630 */       for (SocketAddress a : future.getLocalAddresses()) {
/* 631 */         H handle = this.boundHandles.remove(a);
/*     */         
/* 633 */         if (handle == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         try {
/* 638 */           close(handle);
/* 639 */           wakeup();
/* 640 */         } catch (Exception e) {
/* 641 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } finally {
/* 643 */           cancelledHandles++;
/*     */         } 
/*     */       } 
/*     */       
/* 647 */       future.setDone();
/*     */     } 
/*     */     
/* 650 */     return cancelledHandles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 657 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBacklog() {
/* 664 */     return this.backlog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBacklog(int backlog) {
/* 674 */     synchronized (this.bindLock) {
/* 675 */       if (isActive()) {
/* 676 */         throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
/*     */       }
/*     */       
/* 679 */       this.backlog = backlog;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/* 687 */     return this.reuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReuseAddress(boolean reuseAddress) {
/* 697 */     synchronized (this.bindLock) {
/* 698 */       if (isActive()) {
/* 699 */         throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
/*     */       }
/*     */       
/* 702 */       this.reuseAddress = reuseAddress;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketSessionConfig getSessionConfig() {
/* 710 */     return (SocketSessionConfig)this.sessionConfig;
/*     */   }
/*     */   
/*     */   protected abstract void init() throws Exception;
/*     */   
/*     */   protected abstract void init(SelectorProvider paramSelectorProvider) throws Exception;
/*     */   
/*     */   protected abstract void destroy() throws Exception;
/*     */   
/*     */   protected abstract int select() throws Exception;
/*     */   
/*     */   protected abstract void wakeup();
/*     */   
/*     */   protected abstract Iterator<H> selectedHandles();
/*     */   
/*     */   protected abstract H open(SocketAddress paramSocketAddress) throws Exception;
/*     */   
/*     */   protected abstract SocketAddress localAddress(H paramH) throws Exception;
/*     */   
/*     */   protected abstract S accept(IoProcessor<S> paramIoProcessor, H paramH) throws Exception;
/*     */   
/*     */   protected abstract void close(H paramH) throws Exception;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/polling/AbstractPollingIoAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */