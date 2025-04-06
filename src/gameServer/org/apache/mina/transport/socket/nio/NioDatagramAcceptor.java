/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
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
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.service.AbstractIoAcceptor;
/*     */ import org.apache.mina.core.service.AbstractIoService;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.ExpiringSessionRecycler;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionRecycler;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestQueue;
/*     */ import org.apache.mina.transport.socket.DatagramAcceptor;
/*     */ import org.apache.mina.transport.socket.DatagramSessionConfig;
/*     */ import org.apache.mina.transport.socket.DefaultDatagramSessionConfig;
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
/*     */ public final class NioDatagramAcceptor
/*     */   extends AbstractIoAcceptor
/*     */   implements DatagramAcceptor, IoProcessor<NioSession>
/*     */ {
/*  72 */   private static final IoSessionRecycler DEFAULT_RECYCLER = (IoSessionRecycler)new ExpiringSessionRecycler();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long SELECT_TIMEOUT = 1000L;
/*     */ 
/*     */ 
/*     */   
/*  81 */   private final Semaphore lock = new Semaphore(1);
/*     */ 
/*     */   
/*  84 */   private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> registerQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();
/*     */   
/*  86 */   private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> cancelQueue = new ConcurrentLinkedQueue<AbstractIoAcceptor.AcceptorOperationFuture>();
/*     */   
/*  88 */   private final Queue<NioSession> flushingSessions = new ConcurrentLinkedQueue<NioSession>();
/*     */   
/*  90 */   private final Map<SocketAddress, DatagramChannel> boundHandles = Collections.synchronizedMap(new HashMap<SocketAddress, DatagramChannel>());
/*     */ 
/*     */   
/*  93 */   private IoSessionRecycler sessionRecycler = DEFAULT_RECYCLER;
/*     */   
/*  95 */   private final AbstractIoService.ServiceOperationFuture disposalFuture = new AbstractIoService.ServiceOperationFuture();
/*     */ 
/*     */   
/*     */   private volatile boolean selectable;
/*     */ 
/*     */   
/*     */   private Acceptor acceptor;
/*     */ 
/*     */   
/*     */   private long lastIdleCheckTime;
/*     */ 
/*     */   
/*     */   private volatile Selector selector;
/*     */ 
/*     */   
/*     */   public NioDatagramAcceptor() {
/* 111 */     this((IoSessionConfig)new DefaultDatagramSessionConfig(), (Executor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramAcceptor(Executor executor) {
/* 118 */     this((IoSessionConfig)new DefaultDatagramSessionConfig(), executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NioDatagramAcceptor(IoSessionConfig sessionConfig, Executor executor) {
/* 125 */     super(sessionConfig, executor);
/*     */     
/*     */     try {
/* 128 */       init();
/* 129 */       this.selectable = true;
/* 130 */     } catch (RuntimeException e) {
/* 131 */       throw e;
/* 132 */     } catch (Exception e) {
/* 133 */       throw new RuntimeIoException("Failed to initialize.", e);
/*     */     } finally {
/* 135 */       if (!this.selectable) {
/*     */         try {
/* 137 */           destroy();
/* 138 */         } catch (Exception e) {
/* 139 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private class Acceptor
/*     */     implements Runnable
/*     */   {
/*     */     private Acceptor() {}
/*     */     
/*     */     public void run() {
/* 152 */       int nHandles = 0;
/* 153 */       NioDatagramAcceptor.this.lastIdleCheckTime = System.currentTimeMillis();
/*     */ 
/*     */       
/* 156 */       NioDatagramAcceptor.this.lock.release();
/*     */       
/* 158 */       while (NioDatagramAcceptor.this.selectable) {
/*     */         try {
/* 160 */           int selected = NioDatagramAcceptor.this.select(1000L);
/*     */           
/* 162 */           nHandles += NioDatagramAcceptor.this.registerHandles();
/*     */           
/* 164 */           if (nHandles == 0) {
/*     */             
/* 166 */             try { NioDatagramAcceptor.this.lock.acquire();
/*     */               
/* 168 */               if (NioDatagramAcceptor.this.registerQueue.isEmpty() && NioDatagramAcceptor.this.cancelQueue.isEmpty())
/* 169 */               { NioDatagramAcceptor.this.acceptor = null;
/*     */ 
/*     */ 
/*     */                 
/* 173 */                 NioDatagramAcceptor.this.lock.release(); break; }  } finally { NioDatagramAcceptor.this.lock.release(); }
/*     */           
/*     */           }
/*     */           
/* 177 */           if (selected > 0) {
/* 178 */             NioDatagramAcceptor.this.processReadySessions(NioDatagramAcceptor.this.selectedHandles());
/*     */           }
/*     */           
/* 181 */           long currentTime = System.currentTimeMillis();
/* 182 */           NioDatagramAcceptor.this.flushSessions(currentTime);
/* 183 */           nHandles -= NioDatagramAcceptor.this.unregisterHandles();
/*     */           
/* 185 */           NioDatagramAcceptor.this.notifyIdleSessions(currentTime);
/* 186 */         } catch (ClosedSelectorException cse) {
/*     */           
/* 188 */           ExceptionMonitor.getInstance().exceptionCaught(cse);
/*     */           break;
/* 190 */         } catch (Exception e) {
/* 191 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */           
/*     */           try {
/* 194 */             Thread.sleep(1000L);
/* 195 */           } catch (InterruptedException e1) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 200 */       if (NioDatagramAcceptor.this.selectable && NioDatagramAcceptor.this.isDisposing()) {
/* 201 */         NioDatagramAcceptor.this.selectable = false;
/*     */         try {
/* 203 */           NioDatagramAcceptor.this.destroy();
/* 204 */         } catch (Exception e) {
/* 205 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } finally {
/* 207 */           NioDatagramAcceptor.this.disposalFuture.setValue(Boolean.valueOf(true));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private int registerHandles() {
/*     */     while (true) {
/* 215 */       AbstractIoAcceptor.AcceptorOperationFuture req = this.registerQueue.poll();
/*     */       
/* 217 */       if (req == null) {
/*     */         break;
/*     */       }
/*     */       
/* 221 */       Map<SocketAddress, DatagramChannel> newHandles = new HashMap<SocketAddress, DatagramChannel>();
/* 222 */       List<SocketAddress> localAddresses = req.getLocalAddresses();
/*     */       
/*     */       try {
/* 225 */         for (SocketAddress socketAddress : localAddresses) {
/* 226 */           DatagramChannel handle = open(socketAddress);
/* 227 */           newHandles.put(localAddress(handle), handle);
/*     */         } 
/*     */         
/* 230 */         this.boundHandles.putAll(newHandles);
/*     */         
/* 232 */         getListeners().fireServiceActivated();
/* 233 */         req.setDone();
/*     */         
/* 235 */         return newHandles.size();
/* 236 */       } catch (Exception e) {
/* 237 */         req.setException(e);
/*     */       } finally {
/*     */         
/* 240 */         if (req.getException() != null) {
/* 241 */           for (DatagramChannel handle : newHandles.values()) {
/*     */             try {
/* 243 */               close(handle);
/* 244 */             } catch (Exception e) {
/* 245 */               ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */             } 
/*     */           } 
/*     */           
/* 249 */           wakeup();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 254 */     return 0;
/*     */   }
/*     */   
/*     */   private void processReadySessions(Set<SelectionKey> handles) {
/* 258 */     Iterator<SelectionKey> iterator = handles.iterator();
/*     */     
/* 260 */     while (iterator.hasNext()) {
/* 261 */       SelectionKey key = iterator.next();
/* 262 */       DatagramChannel handle = (DatagramChannel)key.channel();
/* 263 */       iterator.remove();
/*     */       
/*     */       try {
/* 266 */         if (key != null && key.isValid() && key.isReadable()) {
/* 267 */           readHandle(handle);
/*     */         }
/*     */         
/* 270 */         if (key != null && key.isValid() && key.isWritable()) {
/* 271 */           for (IoSession session : getManagedSessions().values()) {
/* 272 */             scheduleFlush((NioSession)session);
/*     */           }
/*     */         }
/* 275 */       } catch (Exception e) {
/* 276 */         ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean scheduleFlush(NioSession session) {
/* 285 */     if (session.setScheduledForFlush(true)) {
/* 286 */       this.flushingSessions.add(session);
/* 287 */       return true;
/*     */     } 
/* 289 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readHandle(DatagramChannel handle) throws Exception {
/* 294 */     IoBuffer readBuf = IoBuffer.allocate(getSessionConfig().getReadBufferSize());
/*     */     
/* 296 */     SocketAddress remoteAddress = receive(handle, readBuf);
/*     */     
/* 298 */     if (remoteAddress != null) {
/* 299 */       IoSession session = newSessionWithoutLock(remoteAddress, localAddress(handle));
/*     */       
/* 301 */       readBuf.flip();
/*     */       
/* 303 */       session.getFilterChain().fireMessageReceived(readBuf);
/*     */     } 
/*     */   }
/*     */   private IoSession newSessionWithoutLock(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*     */     NioSession nioSession;
/* 308 */     DatagramChannel handle = this.boundHandles.get(localAddress);
/*     */     
/* 310 */     if (handle == null) {
/* 311 */       throw new IllegalArgumentException("Unknown local address: " + localAddress);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 316 */     synchronized (this.sessionRecycler) {
/* 317 */       IoSession session = this.sessionRecycler.recycle(remoteAddress);
/*     */       
/* 319 */       if (session != null) {
/* 320 */         return session;
/*     */       }
/*     */ 
/*     */       
/* 324 */       NioSession newSession = newSession(this, handle, remoteAddress);
/* 325 */       getSessionRecycler().put((IoSession)newSession);
/* 326 */       nioSession = newSession;
/*     */     } 
/*     */     
/* 329 */     initSession((IoSession)nioSession, null, null);
/*     */     
/*     */     try {
/* 332 */       getFilterChainBuilder().buildFilterChain(nioSession.getFilterChain());
/* 333 */       getListeners().fireSessionCreated((IoSession)nioSession);
/* 334 */     } catch (Exception e) {
/* 335 */       ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */     } 
/*     */     
/* 338 */     return (IoSession)nioSession;
/*     */   }
/*     */   
/*     */   private void flushSessions(long currentTime) {
/*     */     while (true) {
/* 343 */       NioSession session = this.flushingSessions.poll();
/*     */       
/* 345 */       if (session == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 351 */       session.unscheduledForFlush();
/*     */       
/*     */       try {
/* 354 */         boolean flushedAll = flush(session, currentTime);
/*     */         
/* 356 */         if (flushedAll && !session.getWriteRequestQueue().isEmpty((IoSession)session) && !session.isScheduledForFlush()) {
/* 357 */           scheduleFlush(session);
/*     */         }
/* 359 */       } catch (Exception e) {
/* 360 */         session.getFilterChain().fireExceptionCaught(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean flush(NioSession session, long currentTime) throws Exception {
/* 366 */     WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/* 367 */     int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);
/*     */ 
/*     */     
/* 370 */     int writtenBytes = 0;
/*     */     
/*     */     try {
/*     */       while (true) {
/* 374 */         WriteRequest req = session.getCurrentWriteRequest();
/*     */         
/* 376 */         if (req == null) {
/* 377 */           req = writeRequestQueue.poll((IoSession)session);
/*     */           
/* 379 */           if (req == null) {
/* 380 */             setInterestedInWrite(session, false);
/*     */             
/*     */             break;
/*     */           } 
/* 384 */           session.setCurrentWriteRequest(req);
/*     */         } 
/*     */         
/* 387 */         IoBuffer buf = (IoBuffer)req.getMessage();
/*     */         
/* 389 */         if (buf.remaining() == 0) {
/*     */           
/* 391 */           session.setCurrentWriteRequest(null);
/* 392 */           buf.reset();
/* 393 */           session.getFilterChain().fireMessageSent(req);
/*     */           
/*     */           continue;
/*     */         } 
/* 397 */         SocketAddress destination = req.getDestination();
/*     */         
/* 399 */         if (destination == null) {
/* 400 */           destination = session.getRemoteAddress();
/*     */         }
/*     */         
/* 403 */         int localWrittenBytes = send(session, buf, destination);
/*     */         
/* 405 */         if (localWrittenBytes == 0 || writtenBytes >= maxWrittenBytes) {
/*     */           
/* 407 */           setInterestedInWrite(session, true);
/*     */           
/* 409 */           return false;
/*     */         } 
/* 411 */         setInterestedInWrite(session, false);
/*     */ 
/*     */         
/* 414 */         session.setCurrentWriteRequest(null);
/* 415 */         writtenBytes += localWrittenBytes;
/* 416 */         buf.reset();
/* 417 */         session.getFilterChain().fireMessageSent(req);
/*     */       } 
/*     */     } finally {
/*     */       
/* 421 */       session.increaseWrittenBytes(writtenBytes, currentTime);
/*     */     } 
/*     */     
/* 424 */     return true;
/*     */   }
/*     */   
/*     */   private int unregisterHandles() {
/* 428 */     int nHandles = 0;
/*     */     
/*     */     while (true) {
/* 431 */       AbstractIoAcceptor.AcceptorOperationFuture request = this.cancelQueue.poll();
/* 432 */       if (request == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 437 */       for (SocketAddress socketAddress : request.getLocalAddresses()) {
/* 438 */         DatagramChannel handle = this.boundHandles.remove(socketAddress);
/*     */         
/* 440 */         if (handle == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         try {
/* 445 */           close(handle);
/* 446 */           wakeup();
/* 447 */         } catch (Exception e) {
/* 448 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } finally {
/* 450 */           nHandles++;
/*     */         } 
/*     */       } 
/*     */       
/* 454 */       request.setDone();
/*     */     } 
/*     */     
/* 457 */     return nHandles;
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyIdleSessions(long currentTime) {
/* 462 */     if (currentTime - this.lastIdleCheckTime >= 1000L) {
/* 463 */       this.lastIdleCheckTime = currentTime;
/* 464 */       AbstractIoSession.notifyIdleness(getListeners().getManagedSessions().values().iterator(), currentTime);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startupAcceptor() throws InterruptedException {
/* 472 */     if (!this.selectable) {
/* 473 */       this.registerQueue.clear();
/* 474 */       this.cancelQueue.clear();
/* 475 */       this.flushingSessions.clear();
/*     */     } 
/*     */     
/* 478 */     this.lock.acquire();
/*     */     
/* 480 */     if (this.acceptor == null) {
/* 481 */       this.acceptor = new Acceptor();
/* 482 */       executeWorker(this.acceptor);
/*     */     } else {
/* 484 */       this.lock.release();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void init() throws Exception {
/* 489 */     this.selector = Selector.open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(NioSession session) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws Exception {
/* 506 */     AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);
/*     */ 
/*     */ 
/*     */     
/* 510 */     this.registerQueue.add(request);
/*     */ 
/*     */ 
/*     */     
/* 514 */     startupAcceptor();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 520 */       this.lock.acquire();
/*     */ 
/*     */       
/* 523 */       Thread.sleep(10L);
/* 524 */       wakeup();
/*     */     } finally {
/* 526 */       this.lock.release();
/*     */     } 
/*     */ 
/*     */     
/* 530 */     request.awaitUninterruptibly();
/*     */     
/* 532 */     if (request.getException() != null) {
/* 533 */       throw request.getException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 539 */     Set<SocketAddress> newLocalAddresses = new HashSet<SocketAddress>();
/*     */     
/* 541 */     for (DatagramChannel handle : this.boundHandles.values()) {
/* 542 */       newLocalAddresses.add(localAddress(handle));
/*     */     }
/*     */     
/* 545 */     return newLocalAddresses;
/*     */   }
/*     */   
/*     */   protected void close(DatagramChannel handle) throws Exception {
/* 549 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 551 */     if (key != null) {
/* 552 */       key.cancel();
/*     */     }
/*     */     
/* 555 */     handle.disconnect();
/* 556 */     handle.close();
/*     */   }
/*     */   
/*     */   protected void destroy() throws Exception {
/* 560 */     if (this.selector != null) {
/* 561 */       this.selector.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispose0() throws Exception {
/* 570 */     unbind();
/* 571 */     startupAcceptor();
/* 572 */     wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush(NioSession session) {
/* 579 */     if (scheduleFlush(session)) {
/* 580 */       wakeup();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getDefaultLocalAddress() {
/* 586 */     return (InetSocketAddress)super.getDefaultLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getLocalAddress() {
/* 591 */     return (InetSocketAddress)super.getLocalAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramSessionConfig getSessionConfig() {
/* 598 */     return (DatagramSessionConfig)this.sessionConfig;
/*     */   }
/*     */   
/*     */   public final IoSessionRecycler getSessionRecycler() {
/* 602 */     return this.sessionRecycler;
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 606 */     return NioDatagramSession.METADATA;
/*     */   }
/*     */   
/*     */   protected boolean isReadable(DatagramChannel handle) {
/* 610 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 612 */     if (key == null || !key.isValid()) {
/* 613 */       return false;
/*     */     }
/*     */     
/* 616 */     return key.isReadable();
/*     */   }
/*     */   
/*     */   protected boolean isWritable(DatagramChannel handle) {
/* 620 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 622 */     if (key == null || !key.isValid()) {
/* 623 */       return false;
/*     */     }
/*     */     
/* 626 */     return key.isWritable();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress(DatagramChannel handle) throws Exception {
/* 630 */     InetSocketAddress inetSocketAddress = (InetSocketAddress)handle.socket().getLocalSocketAddress();
/* 631 */     InetAddress inetAddress = inetSocketAddress.getAddress();
/*     */     
/* 633 */     if (inetAddress instanceof Inet6Address && ((Inet6Address)inetAddress).isIPv4CompatibleAddress()) {
/*     */ 
/*     */ 
/*     */       
/* 637 */       byte[] ipV6Address = ((Inet6Address)inetAddress).getAddress();
/* 638 */       byte[] ipV4Address = new byte[4];
/*     */       
/* 640 */       for (int i = 0; i < 4; i++) {
/* 641 */         ipV4Address[i] = ipV6Address[12 + i];
/*     */       }
/*     */       
/* 644 */       InetAddress inet4Adress = Inet4Address.getByAddress(ipV4Address);
/* 645 */       return new InetSocketAddress(inet4Adress, inetSocketAddress.getPort());
/*     */     } 
/* 647 */     return inetSocketAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NioSession newSession(IoProcessor<NioSession> processor, DatagramChannel handle, SocketAddress remoteAddress) {
/* 653 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 655 */     if (key == null || !key.isValid()) {
/* 656 */       return null;
/*     */     }
/*     */     
/* 659 */     NioDatagramSession newSession = new NioDatagramSession((IoService)this, handle, processor, remoteAddress);
/* 660 */     newSession.setSelectionKey(key);
/*     */     
/* 662 */     return newSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 669 */     if (isDisposing()) {
/* 670 */       throw new IllegalStateException("Already disposed.");
/*     */     }
/*     */     
/* 673 */     if (remoteAddress == null) {
/* 674 */       throw new IllegalArgumentException("remoteAddress");
/*     */     }
/*     */     
/* 677 */     synchronized (this.bindLock) {
/* 678 */       if (!isActive()) {
/* 679 */         throw new IllegalStateException("Can't create a session from a unbound service.");
/*     */       }
/*     */       
/*     */       try {
/* 683 */         return newSessionWithoutLock(remoteAddress, localAddress);
/* 684 */       } catch (RuntimeException e) {
/* 685 */         throw e;
/* 686 */       } catch (Error e) {
/* 687 */         throw e;
/* 688 */       } catch (Exception e) {
/* 689 */         throw new RuntimeIoException("Failed to create a session.", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected DatagramChannel open(SocketAddress localAddress) throws Exception {
/* 695 */     DatagramChannel ch = DatagramChannel.open();
/* 696 */     boolean success = false;
/*     */     try {
/* 698 */       (new NioDatagramSessionConfig(ch)).setAll((IoSessionConfig)getSessionConfig());
/* 699 */       ch.configureBlocking(false);
/*     */       
/*     */       try {
/* 702 */         ch.socket().bind(localAddress);
/* 703 */       } catch (IOException ioe) {
/*     */ 
/*     */         
/* 706 */         String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();
/*     */         
/* 708 */         Exception e = new IOException(newMessage);
/* 709 */         e.initCause(ioe.getCause());
/*     */ 
/*     */         
/* 712 */         ch.close();
/*     */         
/* 714 */         throw e;
/*     */       } 
/*     */       
/* 717 */       ch.register(this.selector, 1);
/* 718 */       success = true;
/*     */     } finally {
/* 720 */       if (!success) {
/* 721 */         close(ch);
/*     */       }
/*     */     } 
/*     */     
/* 725 */     return ch;
/*     */   }
/*     */   
/*     */   protected SocketAddress receive(DatagramChannel handle, IoBuffer buffer) throws Exception {
/* 729 */     return handle.receive(buffer.buf());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(NioSession session) {
/* 736 */     getSessionRecycler().remove((IoSession)session);
/* 737 */     getListeners().fireSessionDestroyed((IoSession)session);
/*     */   }
/*     */   
/*     */   protected int select() throws Exception {
/* 741 */     return this.selector.select();
/*     */   }
/*     */   
/*     */   protected int select(long timeout) throws Exception {
/* 745 */     return this.selector.select(timeout);
/*     */   }
/*     */   
/*     */   protected Set<SelectionKey> selectedHandles() {
/* 749 */     return this.selector.selectedKeys();
/*     */   }
/*     */   
/*     */   protected int send(NioSession session, IoBuffer buffer, SocketAddress remoteAddress) throws Exception {
/* 753 */     return ((DatagramChannel)session.getChannel()).send(buffer.buf(), remoteAddress);
/*     */   }
/*     */   
/*     */   public void setDefaultLocalAddress(InetSocketAddress localAddress) {
/* 757 */     setDefaultLocalAddress(localAddress);
/*     */   }
/*     */   
/*     */   protected void setInterestedInWrite(NioSession session, boolean isInterested) throws Exception {
/* 761 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 763 */     if (key == null) {
/*     */       return;
/*     */     }
/*     */     
/* 767 */     int newInterestOps = key.interestOps();
/*     */     
/* 769 */     if (isInterested) {
/* 770 */       newInterestOps |= 0x4;
/*     */     } else {
/* 772 */       newInterestOps &= 0xFFFFFFFB;
/*     */     } 
/*     */     
/* 775 */     key.interestOps(newInterestOps);
/*     */   }
/*     */   
/*     */   public final void setSessionRecycler(IoSessionRecycler sessionRecycler) {
/* 779 */     synchronized (this.bindLock) {
/* 780 */       if (isActive()) {
/* 781 */         throw new IllegalStateException("sessionRecycler can't be set while the acceptor is bound.");
/*     */       }
/*     */       
/* 784 */       if (sessionRecycler == null) {
/* 785 */         sessionRecycler = DEFAULT_RECYCLER;
/*     */       }
/*     */       
/* 788 */       this.sessionRecycler = sessionRecycler;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void unbind0(List<? extends SocketAddress> localAddresses) throws Exception {
/* 797 */     AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);
/*     */     
/* 799 */     this.cancelQueue.add(request);
/* 800 */     startupAcceptor();
/* 801 */     wakeup();
/*     */     
/* 803 */     request.awaitUninterruptibly();
/*     */     
/* 805 */     if (request.getException() != null) {
/* 806 */       throw request.getException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTrafficControl(NioSession session) {
/* 814 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void wakeup() {
/* 818 */     this.selector.wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(NioSession session, WriteRequest writeRequest) {
/* 826 */     long currentTime = System.currentTimeMillis();
/* 827 */     WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/* 828 */     int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);
/*     */ 
/*     */     
/* 831 */     int writtenBytes = 0;
/*     */ 
/*     */ 
/*     */     
/* 835 */     IoBuffer buf = (IoBuffer)writeRequest.getMessage();
/*     */     
/* 837 */     if (buf.remaining() == 0) {
/*     */       
/* 839 */       session.setCurrentWriteRequest(null);
/* 840 */       buf.reset();
/* 841 */       session.getFilterChain().fireMessageSent(writeRequest);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*     */       while (true) {
/* 848 */         if (writeRequest == null) {
/* 849 */           writeRequest = writeRequestQueue.poll((IoSession)session);
/*     */           
/* 851 */           if (writeRequest == null) {
/* 852 */             setInterestedInWrite(session, false);
/*     */             
/*     */             break;
/*     */           } 
/* 856 */           session.setCurrentWriteRequest(writeRequest);
/*     */         } 
/*     */         
/* 859 */         buf = (IoBuffer)writeRequest.getMessage();
/*     */         
/* 861 */         if (buf.remaining() == 0) {
/*     */           
/* 863 */           session.setCurrentWriteRequest(null);
/* 864 */           buf.reset();
/* 865 */           session.getFilterChain().fireMessageSent(writeRequest);
/*     */           
/*     */           continue;
/*     */         } 
/* 869 */         SocketAddress destination = writeRequest.getDestination();
/*     */         
/* 871 */         if (destination == null) {
/* 872 */           destination = session.getRemoteAddress();
/*     */         }
/*     */         
/* 875 */         int localWrittenBytes = send(session, buf, destination);
/*     */         
/* 877 */         if (localWrittenBytes == 0 || writtenBytes >= maxWrittenBytes) {
/*     */           
/* 879 */           setInterestedInWrite(session, true);
/*     */           
/* 881 */           session.getWriteRequestQueue().offer((IoSession)session, writeRequest);
/* 882 */           scheduleFlush(session); continue;
/*     */         } 
/* 884 */         setInterestedInWrite(session, false);
/*     */ 
/*     */         
/* 887 */         session.setCurrentWriteRequest(null);
/* 888 */         writtenBytes += localWrittenBytes;
/* 889 */         buf.reset();
/* 890 */         session.getFilterChain().fireMessageSent(writeRequest);
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/* 895 */     } catch (Exception e) {
/* 896 */       session.getFilterChain().fireExceptionCaught(e);
/*     */     } finally {
/* 898 */       session.increaseWrittenBytes(writtenBytes, currentTime);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioDatagramAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */