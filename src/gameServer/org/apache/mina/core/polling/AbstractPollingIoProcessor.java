/*      */ package org.apache.mina.core.polling;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.channels.ClosedSelectorException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import org.apache.mina.core.buffer.IoBuffer;
/*      */ import org.apache.mina.core.file.FileRegion;
/*      */ import org.apache.mina.core.filterchain.IoFilterChain;
/*      */ import org.apache.mina.core.filterchain.IoFilterChainBuilder;
/*      */ import org.apache.mina.core.future.DefaultIoFuture;
/*      */ import org.apache.mina.core.service.AbstractIoService;
/*      */ import org.apache.mina.core.service.IoProcessor;
/*      */ import org.apache.mina.core.service.IoServiceListenerSupport;
/*      */ import org.apache.mina.core.session.AbstractIoSession;
/*      */ import org.apache.mina.core.session.IoSession;
/*      */ import org.apache.mina.core.session.IoSessionConfig;
/*      */ import org.apache.mina.core.session.SessionState;
/*      */ import org.apache.mina.core.write.WriteRequest;
/*      */ import org.apache.mina.core.write.WriteRequestQueue;
/*      */ import org.apache.mina.core.write.WriteToClosedSessionException;
/*      */ import org.apache.mina.transport.socket.AbstractDatagramSessionConfig;
/*      */ import org.apache.mina.util.ExceptionMonitor;
/*      */ import org.apache.mina.util.NamePreservingRunnable;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractPollingIoProcessor<S extends AbstractIoSession>
/*      */   implements IoProcessor<S>
/*      */ {
/*   69 */   private static final Logger LOG = LoggerFactory.getLogger(IoProcessor.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int WRITE_SPIN_COUNT = 256;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long SELECT_TIMEOUT = 1000L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   private static final ConcurrentHashMap<Class<?>, AtomicInteger> threadIds = new ConcurrentHashMap<Class<?>, AtomicInteger>();
/*      */ 
/*      */   
/*      */   private final String threadName;
/*      */ 
/*      */   
/*      */   private final Executor executor;
/*      */ 
/*      */   
/*   95 */   private final Queue<S> newSessions = new ConcurrentLinkedQueue<S>();
/*      */ 
/*      */   
/*   98 */   private final Queue<S> removingSessions = new ConcurrentLinkedQueue<S>();
/*      */ 
/*      */   
/*  101 */   private final Queue<S> flushingSessions = new ConcurrentLinkedQueue<S>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   private final Queue<S> trafficControllingSessions = new ConcurrentLinkedQueue<S>();
/*      */ 
/*      */   
/*  110 */   private final AtomicReference<Processor> processorRef = new AtomicReference<Processor>();
/*      */   
/*      */   private long lastIdleCheckTime;
/*      */   
/*  114 */   private final Object disposalLock = new Object();
/*      */   
/*      */   private volatile boolean disposing;
/*      */   
/*      */   private volatile boolean disposed;
/*      */   
/*  120 */   private final DefaultIoFuture disposalFuture = new DefaultIoFuture(null);
/*      */   
/*  122 */   protected AtomicBoolean wakeupCalled = new AtomicBoolean(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractPollingIoProcessor(Executor executor) {
/*  132 */     if (executor == null) {
/*  133 */       throw new IllegalArgumentException("executor");
/*      */     }
/*      */     
/*  136 */     this.threadName = nextThreadName();
/*  137 */     this.executor = executor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String nextThreadName() {
/*      */     int newThreadId;
/*  149 */     Class<?> cls = getClass();
/*      */ 
/*      */     
/*  152 */     AtomicInteger threadId = threadIds.putIfAbsent(cls, new AtomicInteger(1));
/*      */     
/*  154 */     if (threadId == null) {
/*  155 */       newThreadId = 1;
/*      */     } else {
/*      */       
/*  158 */       newThreadId = threadId.incrementAndGet();
/*      */     } 
/*      */ 
/*      */     
/*  162 */     return cls.getSimpleName() + '-' + newThreadId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDisposing() {
/*  169 */     return this.disposing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isDisposed() {
/*  176 */     return this.disposed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void dispose() {
/*  183 */     if (this.disposed || this.disposing) {
/*      */       return;
/*      */     }
/*      */     
/*  187 */     synchronized (this.disposalLock) {
/*  188 */       this.disposing = true;
/*  189 */       startupProcessor();
/*      */     } 
/*      */     
/*  192 */     this.disposalFuture.awaitUninterruptibly();
/*  193 */     this.disposed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void add(S session) {
/*  389 */     if (this.disposed || this.disposing) {
/*  390 */       throw new IllegalStateException("Already disposed.");
/*      */     }
/*      */ 
/*      */     
/*  394 */     this.newSessions.add(session);
/*  395 */     startupProcessor();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void remove(S session) {
/*  402 */     scheduleRemove(session);
/*  403 */     startupProcessor();
/*      */   }
/*      */   
/*      */   private void scheduleRemove(S session) {
/*  407 */     this.removingSessions.add(session);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(S session, WriteRequest writeRequest) {
/*  414 */     WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/*      */     
/*  416 */     writeRequestQueue.offer((IoSession)session, writeRequest);
/*      */     
/*  418 */     if (!session.isWriteSuspended()) {
/*  419 */       flush(session);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void flush(S session) {
/*  429 */     if (session.setScheduledForFlush(true)) {
/*  430 */       this.flushingSessions.add(session);
/*  431 */       wakeup();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void scheduleFlush(S session) {
/*  438 */     if (session.setScheduledForFlush(true)) {
/*  439 */       this.flushingSessions.add(session);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateTrafficMask(S session) {
/*  450 */     this.trafficControllingSessions.add(session);
/*  451 */     wakeup();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void startupProcessor() {
/*  459 */     Processor processor = this.processorRef.get();
/*      */     
/*  461 */     if (processor == null) {
/*  462 */       processor = new Processor();
/*      */       
/*  464 */       if (this.processorRef.compareAndSet(null, processor)) {
/*  465 */         this.executor.execute((Runnable)new NamePreservingRunnable(processor, this.threadName));
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  471 */     wakeup();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int handleNewSessions() {
/*  502 */     int addedSessions = 0;
/*      */     
/*  504 */     for (AbstractIoSession abstractIoSession = (AbstractIoSession)this.newSessions.poll(); abstractIoSession != null; abstractIoSession = (AbstractIoSession)this.newSessions.poll()) {
/*  505 */       if (addNow((S)abstractIoSession))
/*      */       {
/*  507 */         addedSessions++;
/*      */       }
/*      */     } 
/*      */     
/*  511 */     return addedSessions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addNow(S session) {
/*  524 */     boolean registered = false;
/*      */     
/*      */     try {
/*  527 */       init(session);
/*  528 */       registered = true;
/*      */ 
/*      */       
/*  531 */       IoFilterChainBuilder chainBuilder = session.getService().getFilterChainBuilder();
/*  532 */       chainBuilder.buildFilterChain(session.getFilterChain());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  537 */       IoServiceListenerSupport listeners = ((AbstractIoService)session.getService()).getListeners();
/*  538 */       listeners.fireSessionCreated((IoSession)session);
/*  539 */     } catch (Exception e) {
/*  540 */       ExceptionMonitor.getInstance().exceptionCaught(e);
/*      */       
/*      */       try {
/*  543 */         destroy(session);
/*  544 */       } catch (Exception e1) {
/*  545 */         ExceptionMonitor.getInstance().exceptionCaught(e1);
/*      */       } finally {
/*  547 */         registered = false;
/*      */       } 
/*      */     } 
/*      */     
/*  551 */     return registered;
/*      */   }
/*      */   
/*      */   private int removeSessions() {
/*  555 */     int removedSessions = 0;
/*      */     
/*  557 */     for (AbstractIoSession abstractIoSession = (AbstractIoSession)this.removingSessions.poll(); abstractIoSession != null; abstractIoSession = (AbstractIoSession)this.removingSessions.poll()) {
/*  558 */       SessionState state = getState((S)abstractIoSession);
/*      */ 
/*      */       
/*  561 */       switch (state) {
/*      */         
/*      */         case OPENED:
/*  564 */           if (removeNow((S)abstractIoSession)) {
/*  565 */             removedSessions++;
/*      */           }
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case CLOSING:
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case OPENING:
/*  577 */           this.newSessions.remove(abstractIoSession);
/*      */           
/*  579 */           if (removeNow((S)abstractIoSession)) {
/*  580 */             removedSessions++;
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         default:
/*  586 */           throw new IllegalStateException(String.valueOf(state));
/*      */       } 
/*      */     
/*      */     } 
/*  590 */     return removedSessions;
/*      */   }
/*      */   
/*      */   private boolean removeNow(S session) {
/*  594 */     clearWriteRequestQueue(session);
/*      */     
/*      */     try {
/*  597 */       destroy(session);
/*  598 */       return true;
/*  599 */     } catch (Exception e) {
/*  600 */       IoFilterChain filterChain = session.getFilterChain();
/*  601 */       filterChain.fireExceptionCaught(e);
/*      */     } finally {
/*  603 */       clearWriteRequestQueue(session);
/*  604 */       ((AbstractIoService)session.getService()).getListeners().fireSessionDestroyed((IoSession)session);
/*      */     } 
/*  606 */     return false;
/*      */   }
/*      */   
/*      */   private void clearWriteRequestQueue(S session) {
/*  610 */     WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/*      */ 
/*      */     
/*  613 */     List<WriteRequest> failedRequests = new ArrayList<WriteRequest>();
/*      */     WriteRequest req;
/*  615 */     if ((req = writeRequestQueue.poll((IoSession)session)) != null) {
/*  616 */       Object message = req.getMessage();
/*      */       
/*  618 */       if (message instanceof IoBuffer) {
/*  619 */         IoBuffer buf = (IoBuffer)message;
/*      */ 
/*      */ 
/*      */         
/*  623 */         if (buf.hasRemaining()) {
/*  624 */           buf.reset();
/*  625 */           failedRequests.add(req);
/*      */         } else {
/*  627 */           IoFilterChain filterChain = session.getFilterChain();
/*  628 */           filterChain.fireMessageSent(req);
/*      */         } 
/*      */       } else {
/*  631 */         failedRequests.add(req);
/*      */       } 
/*      */ 
/*      */       
/*  635 */       while ((req = writeRequestQueue.poll((IoSession)session)) != null) {
/*  636 */         failedRequests.add(req);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  641 */     if (!failedRequests.isEmpty()) {
/*  642 */       WriteToClosedSessionException cause = new WriteToClosedSessionException(failedRequests);
/*      */       
/*  644 */       for (WriteRequest r : failedRequests) {
/*  645 */         session.decreaseScheduledBytesAndMessages(r);
/*  646 */         r.getFuture().setException((Throwable)cause);
/*      */       } 
/*      */       
/*  649 */       IoFilterChain filterChain = session.getFilterChain();
/*  650 */       filterChain.fireExceptionCaught((Throwable)cause);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void process() throws Exception {
/*  655 */     for (Iterator<S> i = selectedSessions(); i.hasNext(); ) {
/*  656 */       AbstractIoSession abstractIoSession = (AbstractIoSession)i.next();
/*  657 */       process((S)abstractIoSession);
/*  658 */       i.remove();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void process(S session) {
/*  667 */     if (isReadable(session) && !session.isReadSuspended()) {
/*  668 */       read(session);
/*      */     }
/*      */ 
/*      */     
/*  672 */     if (isWritable(session) && !session.isWriteSuspended())
/*      */     {
/*  674 */       if (session.setScheduledForFlush(true)) {
/*  675 */         this.flushingSessions.add(session);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void read(S session) {
/*  681 */     IoSessionConfig config = session.getConfig();
/*  682 */     int bufferSize = config.getReadBufferSize();
/*  683 */     IoBuffer buf = IoBuffer.allocate(bufferSize);
/*      */     
/*  685 */     boolean hasFragmentation = session.getTransportMetadata().hasFragmentation();
/*      */     
/*      */     try {
/*  688 */       int ret, readBytes = 0;
/*      */ 
/*      */       
/*      */       try {
/*  692 */         if (hasFragmentation) {
/*      */           
/*  694 */           while ((ret = read(session, buf)) > 0) {
/*  695 */             readBytes += ret;
/*      */             
/*  697 */             if (!buf.hasRemaining()) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */         } else {
/*  702 */           ret = read(session, buf);
/*      */           
/*  704 */           if (ret > 0) {
/*  705 */             readBytes = ret;
/*      */           }
/*      */         } 
/*      */       } finally {
/*  709 */         buf.flip();
/*      */       } 
/*      */       
/*  712 */       if (readBytes > 0) {
/*  713 */         IoFilterChain filterChain = session.getFilterChain();
/*  714 */         filterChain.fireMessageReceived(buf);
/*  715 */         buf = null;
/*      */         
/*  717 */         if (hasFragmentation) {
/*  718 */           if (readBytes << 1 < config.getReadBufferSize()) {
/*  719 */             session.decreaseReadBufferSize();
/*  720 */           } else if (readBytes == config.getReadBufferSize()) {
/*  721 */             session.increaseReadBufferSize();
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  726 */       if (ret < 0) {
/*      */         
/*  728 */         IoFilterChain filterChain = session.getFilterChain();
/*  729 */         filterChain.fireInputClosed();
/*      */       } 
/*  731 */     } catch (Exception e) {
/*  732 */       if (e instanceof IOException && (
/*  733 */         !(e instanceof java.net.PortUnreachableException) || !AbstractDatagramSessionConfig.class.isAssignableFrom(config.getClass()) || ((AbstractDatagramSessionConfig)config).isCloseOnPortUnreachable()))
/*      */       {
/*      */         
/*  736 */         scheduleRemove(session);
/*      */       }
/*      */ 
/*      */       
/*  740 */       IoFilterChain filterChain = session.getFilterChain();
/*  741 */       filterChain.fireExceptionCaught(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void notifyIdleSessions(long currentTime) throws Exception {
/*  747 */     if (currentTime - this.lastIdleCheckTime >= 1000L) {
/*  748 */       this.lastIdleCheckTime = currentTime;
/*  749 */       AbstractIoSession.notifyIdleness(allSessions(), currentTime);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void flush(long currentTime) {
/*  757 */     if (this.flushingSessions.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*      */     do {
/*  762 */       AbstractIoSession abstractIoSession = (AbstractIoSession)this.flushingSessions.poll();
/*      */       
/*  764 */       if (abstractIoSession == null) {
/*      */         break;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  771 */       abstractIoSession.unscheduledForFlush();
/*      */       
/*  773 */       SessionState state = getState((S)abstractIoSession);
/*      */       
/*  775 */       switch (state) {
/*      */         case OPENED:
/*      */           try {
/*  778 */             boolean flushedAll = flushNow((S)abstractIoSession, currentTime);
/*      */             
/*  780 */             if (flushedAll && !abstractIoSession.getWriteRequestQueue().isEmpty((IoSession)abstractIoSession) && !abstractIoSession.isScheduledForFlush())
/*      */             {
/*  782 */               scheduleFlush((S)abstractIoSession);
/*      */             }
/*  784 */           } catch (Exception e) {
/*  785 */             scheduleRemove((S)abstractIoSession);
/*  786 */             IoFilterChain filterChain = abstractIoSession.getFilterChain();
/*  787 */             filterChain.fireExceptionCaught(e);
/*      */           } 
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case CLOSING:
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case OPENING:
/*  800 */           scheduleFlush((S)abstractIoSession);
/*      */           return;
/*      */         
/*      */         default:
/*  804 */           throw new IllegalStateException(String.valueOf(state));
/*      */       } 
/*      */     
/*  807 */     } while (!this.flushingSessions.isEmpty());
/*      */   }
/*      */   
/*      */   private boolean flushNow(S session, long currentTime) {
/*  811 */     if (!session.isConnected()) {
/*  812 */       scheduleRemove(session);
/*  813 */       return false;
/*      */     } 
/*      */     
/*  816 */     boolean hasFragmentation = session.getTransportMetadata().hasFragmentation();
/*      */     
/*  818 */     WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);
/*      */     
/*  825 */     int writtenBytes = 0;
/*  826 */     WriteRequest req = null;
/*      */ 
/*      */     
/*      */     try {
/*  830 */       setInterestedInWrite(session, false);
/*      */ 
/*      */       
/*      */       do {
/*  834 */         req = session.getCurrentWriteRequest();
/*      */         
/*  836 */         if (req == null) {
/*  837 */           req = writeRequestQueue.poll((IoSession)session);
/*      */           
/*  839 */           if (req == null) {
/*      */             break;
/*      */           }
/*      */           
/*  843 */           session.setCurrentWriteRequest(req);
/*      */         } 
/*      */         
/*  846 */         int localWrittenBytes = 0;
/*  847 */         Object message = req.getMessage();
/*      */         
/*  849 */         if (message instanceof IoBuffer) {
/*  850 */           localWrittenBytes = writeBuffer(session, req, hasFragmentation, maxWrittenBytes - writtenBytes, currentTime);
/*      */ 
/*      */           
/*  853 */           if (localWrittenBytes > 0 && ((IoBuffer)message).hasRemaining()) {
/*      */             
/*  855 */             writtenBytes += localWrittenBytes;
/*  856 */             setInterestedInWrite(session, true);
/*  857 */             return false;
/*      */           } 
/*  859 */         } else if (message instanceof FileRegion) {
/*  860 */           localWrittenBytes = writeFile(session, req, hasFragmentation, maxWrittenBytes - writtenBytes, currentTime);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  868 */           if (localWrittenBytes > 0 && ((FileRegion)message).getRemainingBytes() > 0L) {
/*  869 */             writtenBytes += localWrittenBytes;
/*  870 */             setInterestedInWrite(session, true);
/*  871 */             return false;
/*      */           } 
/*      */         } else {
/*  874 */           throw new IllegalStateException("Don't know how to handle message of type '" + message.getClass().getName() + "'.  Are you missing a protocol encoder?");
/*      */         } 
/*      */ 
/*      */         
/*  878 */         if (localWrittenBytes == 0) {
/*      */           
/*  880 */           setInterestedInWrite(session, true);
/*  881 */           return false;
/*      */         } 
/*      */         
/*  884 */         writtenBytes += localWrittenBytes;
/*      */         
/*  886 */         if (writtenBytes >= maxWrittenBytes) {
/*      */           
/*  888 */           scheduleFlush(session);
/*  889 */           return false;
/*      */         } 
/*      */         
/*  892 */         if (!(message instanceof IoBuffer))
/*  893 */           continue;  ((IoBuffer)message).free();
/*      */       }
/*  895 */       while (writtenBytes < maxWrittenBytes);
/*  896 */     } catch (Exception e) {
/*  897 */       if (req != null) {
/*  898 */         req.getFuture().setException(e);
/*      */       }
/*      */       
/*  901 */       IoFilterChain filterChain = session.getFilterChain();
/*  902 */       filterChain.fireExceptionCaught(e);
/*  903 */       return false;
/*      */     } 
/*      */     
/*  906 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private int writeBuffer(S session, WriteRequest req, boolean hasFragmentation, int maxLength, long currentTime) throws Exception {
/*  911 */     IoBuffer buf = (IoBuffer)req.getMessage();
/*  912 */     int localWrittenBytes = 0;
/*      */     
/*  914 */     if (buf.hasRemaining()) {
/*      */       int length;
/*      */       
/*  917 */       if (hasFragmentation) {
/*  918 */         length = Math.min(buf.remaining(), maxLength);
/*      */       } else {
/*  920 */         length = buf.remaining();
/*      */       } 
/*      */       
/*      */       try {
/*  924 */         localWrittenBytes = write(session, buf, length);
/*  925 */       } catch (IOException ioe) {
/*      */ 
/*      */         
/*  928 */         buf.free();
/*  929 */         session.close(true);
/*  930 */         destroy(session);
/*      */         
/*  932 */         return 0;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  937 */     session.increaseWrittenBytes(localWrittenBytes, currentTime);
/*      */     
/*  939 */     if (!buf.hasRemaining() || (!hasFragmentation && localWrittenBytes != 0)) {
/*      */       
/*  941 */       int pos = buf.position();
/*  942 */       buf.reset();
/*      */       
/*  944 */       fireMessageSent(session, req);
/*      */ 
/*      */       
/*  947 */       buf.position(pos);
/*      */     } 
/*      */     
/*  950 */     return localWrittenBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   private int writeFile(S session, WriteRequest req, boolean hasFragmentation, int maxLength, long currentTime) throws Exception {
/*      */     int localWrittenBytes;
/*  956 */     FileRegion region = (FileRegion)req.getMessage();
/*      */     
/*  958 */     if (region.getRemainingBytes() > 0L) {
/*      */       int length;
/*      */       
/*  961 */       if (hasFragmentation) {
/*  962 */         length = (int)Math.min(region.getRemainingBytes(), maxLength);
/*      */       } else {
/*  964 */         length = (int)Math.min(2147483647L, region.getRemainingBytes());
/*      */       } 
/*      */       
/*  967 */       localWrittenBytes = transferFile(session, region, length);
/*  968 */       region.update(localWrittenBytes);
/*      */     } else {
/*  970 */       localWrittenBytes = 0;
/*      */     } 
/*      */     
/*  973 */     session.increaseWrittenBytes(localWrittenBytes, currentTime);
/*      */     
/*  975 */     if (region.getRemainingBytes() <= 0L || (!hasFragmentation && localWrittenBytes != 0)) {
/*  976 */       fireMessageSent(session, req);
/*      */     }
/*      */     
/*  979 */     return localWrittenBytes;
/*      */   }
/*      */   
/*      */   private void fireMessageSent(S session, WriteRequest req) {
/*  983 */     session.setCurrentWriteRequest(null);
/*  984 */     IoFilterChain filterChain = session.getFilterChain();
/*  985 */     filterChain.fireMessageSent(req);
/*      */   } protected abstract void doDispose() throws Exception; protected abstract int select(long paramLong) throws Exception;
/*      */   protected abstract int select() throws Exception;
/*      */   protected abstract boolean isSelectorEmpty();
/*      */   protected abstract void wakeup();
/*      */   protected abstract Iterator<S> allSessions();
/*      */   private void updateTrafficMask() {
/*  992 */     int queueSize = this.trafficControllingSessions.size();
/*      */     
/*  994 */     while (queueSize > 0) {
/*  995 */       AbstractIoSession abstractIoSession = (AbstractIoSession)this.trafficControllingSessions.poll();
/*      */       
/*  997 */       if (abstractIoSession == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1002 */       SessionState state = getState((S)abstractIoSession);
/*      */       
/* 1004 */       switch (state) {
/*      */         case OPENED:
/* 1006 */           updateTrafficControl((S)abstractIoSession);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case CLOSING:
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case OPENING:
/* 1018 */           this.trafficControllingSessions.add((S)abstractIoSession);
/*      */           break;
/*      */         
/*      */         default:
/* 1022 */           throw new IllegalStateException(String.valueOf(state));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1029 */       queueSize--;
/*      */     } 
/*      */   } protected abstract Iterator<S> selectedSessions(); protected abstract SessionState getState(S paramS); protected abstract boolean isWritable(S paramS); protected abstract boolean isReadable(S paramS); protected abstract void setInterestedInWrite(S paramS, boolean paramBoolean) throws Exception;
/*      */   protected abstract void setInterestedInRead(S paramS, boolean paramBoolean) throws Exception;
/*      */   protected abstract boolean isInterestedInRead(S paramS);
/*      */   protected abstract boolean isInterestedInWrite(S paramS);
/*      */   protected abstract void init(S paramS) throws Exception;
/*      */   protected abstract void destroy(S paramS) throws Exception;
/*      */   public void updateTrafficControl(S session) {
/*      */     try {
/* 1039 */       setInterestedInRead(session, !session.isReadSuspended());
/* 1040 */     } catch (Exception e) {
/* 1041 */       IoFilterChain filterChain = session.getFilterChain();
/* 1042 */       filterChain.fireExceptionCaught(e);
/*      */     } 
/*      */     
/*      */     try {
/* 1046 */       setInterestedInWrite(session, (!session.getWriteRequestQueue().isEmpty((IoSession)session) && !session.isWriteSuspended()));
/*      */     }
/* 1048 */     catch (Exception e) {
/* 1049 */       IoFilterChain filterChain = session.getFilterChain();
/* 1050 */       filterChain.fireExceptionCaught(e);
/*      */     } 
/*      */   }
/*      */   protected abstract int read(S paramS, IoBuffer paramIoBuffer) throws Exception;
/*      */   protected abstract int write(S paramS, IoBuffer paramIoBuffer, int paramInt) throws Exception;
/*      */   protected abstract int transferFile(S paramS, FileRegion paramFileRegion, int paramInt) throws Exception;
/*      */   
/*      */   protected abstract void registerNewSelector() throws IOException;
/*      */   
/*      */   protected abstract boolean isBrokenConnection() throws IOException;
/*      */   
/*      */   private class Processor implements Runnable { public void run() {
/* 1062 */       assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() == this;
/*      */       
/* 1064 */       int nSessions = 0;
/* 1065 */       AbstractPollingIoProcessor.this.lastIdleCheckTime = System.currentTimeMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*      */         try {
/* 1073 */           long t0 = System.currentTimeMillis();
/* 1074 */           int selected = AbstractPollingIoProcessor.this.select(1000L);
/* 1075 */           long t1 = System.currentTimeMillis();
/* 1076 */           long delta = t1 - t0;
/*      */           
/* 1078 */           if (selected == 0 && !AbstractPollingIoProcessor.this.wakeupCalled.get() && delta < 100L) {
/*      */ 
/*      */             
/* 1081 */             if (AbstractPollingIoProcessor.this.isBrokenConnection()) {
/* 1082 */               AbstractPollingIoProcessor.LOG.warn("Broken connection");
/*      */ 
/*      */ 
/*      */               
/* 1086 */               AbstractPollingIoProcessor.this.wakeupCalled.getAndSet(false);
/*      */               
/*      */               continue;
/*      */             } 
/* 1090 */             AbstractPollingIoProcessor.LOG.warn("Create a new selector. Selected is 0, delta = " + (t1 - t0));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1102 */             AbstractPollingIoProcessor.this.registerNewSelector();
/*      */ 
/*      */ 
/*      */             
/* 1106 */             AbstractPollingIoProcessor.this.wakeupCalled.getAndSet(false);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */ 
/*      */           
/* 1113 */           nSessions += AbstractPollingIoProcessor.this.handleNewSessions();
/*      */           
/* 1115 */           AbstractPollingIoProcessor.this.updateTrafficMask();
/*      */ 
/*      */ 
/*      */           
/* 1119 */           if (selected > 0)
/*      */           {
/* 1121 */             AbstractPollingIoProcessor.this.process();
/*      */           }
/*      */ 
/*      */           
/* 1125 */           long currentTime = System.currentTimeMillis();
/* 1126 */           AbstractPollingIoProcessor.this.flush(currentTime);
/*      */ 
/*      */           
/* 1129 */           nSessions -= AbstractPollingIoProcessor.this.removeSessions();
/*      */ 
/*      */           
/* 1132 */           AbstractPollingIoProcessor.this.notifyIdleSessions(currentTime);
/*      */ 
/*      */ 
/*      */           
/* 1136 */           if (nSessions == 0) {
/* 1137 */             AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).set(null);
/*      */             
/* 1139 */             if (AbstractPollingIoProcessor.this.newSessions.isEmpty() && AbstractPollingIoProcessor.this.isSelectorEmpty()) {
/*      */               
/* 1141 */               assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;
/*      */               
/*      */               break;
/*      */             } 
/* 1145 */             assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;
/*      */             
/* 1147 */             if (!AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).compareAndSet(null, this)) {
/*      */               
/* 1149 */               assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() != this;
/*      */               
/*      */               break;
/*      */             } 
/* 1153 */             assert AbstractPollingIoProcessor.access$100(AbstractPollingIoProcessor.this).get() == this;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1158 */           if (AbstractPollingIoProcessor.this.isDisposing()) {
/* 1159 */             for (Iterator<S> i = AbstractPollingIoProcessor.this.allSessions(); i.hasNext();) {
/* 1160 */               AbstractPollingIoProcessor.this.scheduleRemove(i.next());
/*      */             }
/*      */             
/* 1163 */             AbstractPollingIoProcessor.this.wakeup();
/*      */           } 
/* 1165 */         } catch (ClosedSelectorException cse) {
/*      */ 
/*      */           
/* 1168 */           ExceptionMonitor.getInstance().exceptionCaught(cse);
/*      */           break;
/* 1170 */         } catch (Exception e) {
/* 1171 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*      */           
/*      */           try {
/* 1174 */             Thread.sleep(1000L);
/* 1175 */           } catch (InterruptedException e1) {
/* 1176 */             ExceptionMonitor.getInstance().exceptionCaught(e1);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*      */       try {
/* 1182 */         synchronized (AbstractPollingIoProcessor.this.disposalLock) {
/* 1183 */           if (AbstractPollingIoProcessor.this.disposing) {
/* 1184 */             AbstractPollingIoProcessor.this.doDispose();
/*      */           }
/*      */         } 
/* 1187 */       } catch (Exception e) {
/* 1188 */         ExceptionMonitor.getInstance().exceptionCaught(e);
/*      */       } finally {
/* 1190 */         AbstractPollingIoProcessor.this.disposalFuture.setValue(Boolean.valueOf(true));
/*      */       } 
/*      */     }
/*      */     
/*      */     private Processor() {} }
/*      */ 
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/polling/AbstractPollingIoProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */