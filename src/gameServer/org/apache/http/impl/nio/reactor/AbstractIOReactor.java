/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOReactor;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.nio.reactor.IOSession;
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
/*     */ @ThreadSafe
/*     */ public abstract class AbstractIOReactor
/*     */   implements IOReactor
/*     */ {
/*     */   private volatile IOReactorStatus status;
/*     */   private final Object statusMutex;
/*     */   private final long selectTimeout;
/*     */   private final boolean interestOpsQueueing;
/*     */   private final Selector selector;
/*     */   private final Set<IOSession> sessions;
/*     */   private final Queue<InterestOpEntry> interestOpsQueue;
/*     */   private final Queue<IOSession> closedSessions;
/*     */   private final Queue<ChannelEntry> newChannels;
/*     */   
/*     */   public AbstractIOReactor(long selectTimeout) throws IOReactorException {
/*  80 */     this(selectTimeout, false);
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
/*     */   public AbstractIOReactor(long selectTimeout, boolean interestOpsQueueing) throws IOReactorException {
/*  95 */     Args.positive(selectTimeout, "Select timeout");
/*  96 */     this.selectTimeout = selectTimeout;
/*  97 */     this.interestOpsQueueing = interestOpsQueueing;
/*  98 */     this.sessions = Collections.synchronizedSet(new HashSet<IOSession>());
/*  99 */     this.interestOpsQueue = new ConcurrentLinkedQueue<InterestOpEntry>();
/* 100 */     this.closedSessions = new ConcurrentLinkedQueue<IOSession>();
/* 101 */     this.newChannels = new ConcurrentLinkedQueue<ChannelEntry>();
/*     */     try {
/* 103 */       this.selector = Selector.open();
/* 104 */     } catch (IOException ex) {
/* 105 */       throw new IOReactorException("Failure opening selector", ex);
/*     */     } 
/* 107 */     this.statusMutex = new Object();
/* 108 */     this.status = IOReactorStatus.INACTIVE;
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
/*     */   protected void sessionCreated(SelectionKey key, IOSession session) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sessionClosed(IOSession session) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sessionTimedOut(IOSession session) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IOSession getSession(SelectionKey key) {
/* 198 */     return (IOSession)key.attachment();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 203 */     return this.status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getInterestOpsQueueing() {
/* 212 */     return this.interestOpsQueueing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChannel(ChannelEntry channelEntry) {
/* 222 */     Args.notNull(channelEntry, "Channel entry");
/* 223 */     this.newChannels.add(channelEntry);
/* 224 */     this.selector.wakeup();
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
/*     */   protected void execute() throws InterruptedIOException, IOReactorException {
/* 250 */     this.status = IOReactorStatus.ACTIVE;
/*     */ 
/*     */     
/*     */     try { while (true) {
/*     */         int readyCount;
/*     */         
/*     */         try {
/* 257 */           readyCount = this.selector.select(this.selectTimeout);
/* 258 */         } catch (InterruptedIOException ex) {
/* 259 */           throw ex;
/* 260 */         } catch (IOException ex) {
/* 261 */           throw new IOReactorException("Unexpected selector failure", ex);
/*     */         } 
/*     */         
/* 264 */         if (this.status == IOReactorStatus.SHUT_DOWN) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 269 */         if (this.status == IOReactorStatus.SHUTTING_DOWN) {
/*     */ 
/*     */           
/* 272 */           closeSessions();
/* 273 */           closeNewChannels();
/*     */         } 
/*     */ 
/*     */         
/* 277 */         if (readyCount > 0) {
/* 278 */           processEvents(this.selector.selectedKeys());
/*     */         }
/*     */ 
/*     */         
/* 282 */         validate(this.selector.keys());
/*     */ 
/*     */         
/* 285 */         processClosedSessions();
/*     */ 
/*     */         
/* 288 */         if (this.status == IOReactorStatus.ACTIVE) {
/* 289 */           processNewChannels();
/*     */         }
/*     */ 
/*     */         
/* 293 */         if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) > 0 && this.sessions.isEmpty()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/* 298 */         if (this.interestOpsQueueing)
/*     */         {
/* 300 */           processPendingInterestOps();
/*     */         }
/*     */       }
/*     */        }
/*     */     
/* 305 */     catch (ClosedSelectorException ignore) {  }
/*     */     finally
/* 307 */     { hardShutdown();
/* 308 */       synchronized (this.statusMutex) {
/* 309 */         this.statusMutex.notifyAll();
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void processEvents(Set<SelectionKey> selectedKeys) {
/* 315 */     for (SelectionKey key : selectedKeys)
/*     */     {
/* 317 */       processEvent(key);
/*     */     }
/*     */     
/* 320 */     selectedKeys.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processEvent(SelectionKey key) {
/* 329 */     IOSessionImpl session = (IOSessionImpl)key.attachment();
/*     */     try {
/* 331 */       if (key.isAcceptable()) {
/* 332 */         acceptable(key);
/*     */       }
/* 334 */       if (key.isConnectable()) {
/* 335 */         connectable(key);
/*     */       }
/* 337 */       if (key.isReadable()) {
/* 338 */         session.resetLastRead();
/* 339 */         readable(key);
/*     */       } 
/* 341 */       if (key.isWritable()) {
/* 342 */         session.resetLastWrite();
/* 343 */         writable(key);
/*     */       } 
/* 345 */     } catch (CancelledKeyException ex) {
/* 346 */       queueClosedSession(session);
/* 347 */       key.attach(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void queueClosedSession(IOSession session) {
/* 357 */     if (session != null) {
/* 358 */       this.closedSessions.add(session);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processNewChannels() throws IOReactorException {
/*     */     ChannelEntry entry;
/* 364 */     while ((entry = this.newChannels.poll()) != null) {
/*     */       SocketChannel channel;
/*     */       SelectionKey key;
/*     */       IOSession session;
/*     */       try {
/* 369 */         channel = entry.getChannel();
/* 370 */         channel.configureBlocking(false);
/* 371 */         key = channel.register(this.selector, 1);
/* 372 */       } catch (ClosedChannelException ex) {
/* 373 */         SessionRequestImpl sessionRequest = entry.getSessionRequest();
/* 374 */         if (sessionRequest != null) {
/* 375 */           sessionRequest.failed(ex);
/*     */         }
/*     */         
/*     */         return;
/* 379 */       } catch (IOException ex) {
/* 380 */         throw new IOReactorException("Failure registering channel with the selector", ex);
/*     */       } 
/*     */ 
/*     */       
/* 384 */       SessionClosedCallback sessionClosedCallback = new SessionClosedCallback()
/*     */         {
/*     */           public void sessionClosed(IOSession session)
/*     */           {
/* 388 */             AbstractIOReactor.this.queueClosedSession(session);
/*     */           }
/*     */         };
/*     */ 
/*     */       
/* 393 */       InterestOpsCallback interestOpsCallback = null;
/* 394 */       if (this.interestOpsQueueing) {
/* 395 */         interestOpsCallback = new InterestOpsCallback()
/*     */           {
/*     */             public void addInterestOps(InterestOpEntry entry)
/*     */             {
/* 399 */               AbstractIOReactor.this.queueInterestOps(entry);
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 407 */         session = new IOSessionImpl(key, interestOpsCallback, sessionClosedCallback);
/* 408 */         int timeout = 0;
/*     */         try {
/* 410 */           timeout = channel.socket().getSoTimeout();
/* 411 */         } catch (IOException ex) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 417 */         session.setAttribute("http.session.attachment", entry.getAttachment());
/* 418 */         session.setSocketTimeout(timeout);
/* 419 */       } catch (CancelledKeyException ex) {
/*     */         continue;
/*     */       } 
/*     */       try {
/* 423 */         this.sessions.add(session);
/* 424 */         SessionRequestImpl sessionRequest = entry.getSessionRequest();
/* 425 */         if (sessionRequest != null) {
/* 426 */           sessionRequest.completed(session);
/*     */         }
/* 428 */         key.attach(session);
/* 429 */         sessionCreated(key, session);
/* 430 */       } catch (CancelledKeyException ex) {
/* 431 */         queueClosedSession(session);
/* 432 */         key.attach(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processClosedSessions() {
/*     */     IOSession session;
/* 439 */     while ((session = this.closedSessions.poll()) != null) {
/* 440 */       if (this.sessions.remove(session)) {
/*     */         try {
/* 442 */           sessionClosed(session);
/* 443 */         } catch (CancelledKeyException ex) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPendingInterestOps() {
/* 452 */     if (!this.interestOpsQueueing) {
/*     */       return;
/*     */     }
/*     */     InterestOpEntry entry;
/* 456 */     while ((entry = this.interestOpsQueue.poll()) != null) {
/*     */       
/* 458 */       SelectionKey key = entry.getSelectionKey();
/* 459 */       int eventMask = entry.getEventMask();
/* 460 */       if (key.isValid()) {
/* 461 */         key.interestOps(eventMask);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean queueInterestOps(InterestOpEntry entry) {
/* 468 */     Asserts.check(this.interestOpsQueueing, "Interest ops queueing not enabled");
/* 469 */     if (entry == null) {
/* 470 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 474 */     this.interestOpsQueue.add(entry);
/*     */     
/* 476 */     return true;
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
/*     */   protected void timeoutCheck(SelectionKey key, long now) {
/* 489 */     IOSessionImpl session = (IOSessionImpl)key.attachment();
/* 490 */     if (session != null) {
/* 491 */       int timeout = session.getSocketTimeout();
/* 492 */       if (timeout > 0 && 
/* 493 */         session.getLastAccessTime() + timeout < now) {
/* 494 */         sessionTimedOut(session);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeSessions() {
/* 504 */     synchronized (this.sessions) {
/* 505 */       for (IOSession session : this.sessions) {
/* 506 */         session.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeNewChannels() throws IOReactorException {
/*     */     ChannelEntry entry;
/* 518 */     while ((entry = this.newChannels.poll()) != null) {
/* 519 */       SessionRequestImpl sessionRequest = entry.getSessionRequest();
/* 520 */       if (sessionRequest != null) {
/* 521 */         sessionRequest.cancel();
/*     */       }
/* 523 */       SocketChannel channel = entry.getChannel();
/*     */       try {
/* 525 */         channel.close();
/* 526 */       } catch (IOException ignore) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeActiveChannels() throws IOReactorException {
/*     */     try {
/* 538 */       Set<SelectionKey> keys = this.selector.keys();
/* 539 */       for (SelectionKey key : keys) {
/* 540 */         IOSession session = getSession(key);
/* 541 */         if (session != null) {
/* 542 */           session.close();
/*     */         }
/*     */       } 
/* 545 */       this.selector.close();
/* 546 */     } catch (IOException ignore) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void gracefulShutdown() {
/* 554 */     synchronized (this.statusMutex) {
/* 555 */       if (this.status != IOReactorStatus.ACTIVE) {
/*     */         return;
/*     */       }
/*     */       
/* 559 */       this.status = IOReactorStatus.SHUTTING_DOWN;
/*     */     } 
/* 561 */     this.selector.wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hardShutdown() throws IOReactorException {
/* 568 */     synchronized (this.statusMutex) {
/* 569 */       if (this.status == IOReactorStatus.SHUT_DOWN) {
/*     */         return;
/*     */       }
/*     */       
/* 573 */       this.status = IOReactorStatus.SHUT_DOWN;
/*     */     } 
/*     */     
/* 576 */     closeNewChannels();
/* 577 */     closeActiveChannels();
/* 578 */     processClosedSessions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void awaitShutdown(long timeout) throws InterruptedException {
/* 589 */     synchronized (this.statusMutex) {
/* 590 */       long deadline = System.currentTimeMillis() + timeout;
/* 591 */       long remaining = timeout;
/* 592 */       while (this.status != IOReactorStatus.SHUT_DOWN) {
/* 593 */         this.statusMutex.wait(remaining);
/* 594 */         if (timeout > 0L) {
/* 595 */           remaining = deadline - System.currentTimeMillis();
/* 596 */           if (remaining <= 0L) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown(long gracePeriod) throws IOReactorException {
/* 606 */     if (this.status != IOReactorStatus.INACTIVE) {
/* 607 */       gracefulShutdown();
/*     */       try {
/* 609 */         awaitShutdown(gracePeriod);
/* 610 */       } catch (InterruptedException ignore) {}
/*     */     } 
/*     */     
/* 613 */     if (this.status != IOReactorStatus.SHUT_DOWN) {
/* 614 */       hardShutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOReactorException {
/* 620 */     shutdown(1000L);
/*     */   }
/*     */   
/*     */   protected abstract void acceptable(SelectionKey paramSelectionKey);
/*     */   
/*     */   protected abstract void connectable(SelectionKey paramSelectionKey);
/*     */   
/*     */   protected abstract void readable(SelectionKey paramSelectionKey);
/*     */   
/*     */   protected abstract void writable(SelectionKey paramSelectionKey);
/*     */   
/*     */   protected abstract void validate(Set<SelectionKey> paramSet);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/AbstractIOReactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */