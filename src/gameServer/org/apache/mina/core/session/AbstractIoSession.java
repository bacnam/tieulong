/*      */ package org.apache.mina.core.session;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.util.Iterator;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import org.apache.mina.core.buffer.IoBuffer;
/*      */ import org.apache.mina.core.file.DefaultFileRegion;
/*      */ import org.apache.mina.core.file.FilenameFileRegion;
/*      */ import org.apache.mina.core.filterchain.IoFilterChain;
/*      */ import org.apache.mina.core.future.CloseFuture;
/*      */ import org.apache.mina.core.future.DefaultCloseFuture;
/*      */ import org.apache.mina.core.future.DefaultReadFuture;
/*      */ import org.apache.mina.core.future.DefaultWriteFuture;
/*      */ import org.apache.mina.core.future.IoFuture;
/*      */ import org.apache.mina.core.future.IoFutureListener;
/*      */ import org.apache.mina.core.future.ReadFuture;
/*      */ import org.apache.mina.core.future.WriteFuture;
/*      */ import org.apache.mina.core.service.AbstractIoService;
/*      */ import org.apache.mina.core.service.IoAcceptor;
/*      */ import org.apache.mina.core.service.IoHandler;
/*      */ import org.apache.mina.core.service.IoProcessor;
/*      */ import org.apache.mina.core.service.IoService;
/*      */ import org.apache.mina.core.service.TransportMetadata;
/*      */ import org.apache.mina.core.write.DefaultWriteRequest;
/*      */ import org.apache.mina.core.write.WriteRequest;
/*      */ import org.apache.mina.core.write.WriteRequestQueue;
/*      */ import org.apache.mina.core.write.WriteTimeoutException;
/*      */ import org.apache.mina.core.write.WriteToClosedSessionException;
/*      */ import org.apache.mina.util.ExceptionMonitor;
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
/*      */ public abstract class AbstractIoSession
/*      */   implements IoSession
/*      */ {
/*      */   private final IoHandler handler;
/*      */   protected IoSessionConfig config;
/*      */   private final IoService service;
/*   75 */   private static final AttributeKey READY_READ_FUTURES_KEY = new AttributeKey(AbstractIoSession.class, "readyReadFutures");
/*      */ 
/*      */   
/*   78 */   private static final AttributeKey WAITING_READ_FUTURES_KEY = new AttributeKey(AbstractIoSession.class, "waitingReadFutures");
/*      */ 
/*      */   
/*   81 */   private static final IoFutureListener<CloseFuture> SCHEDULED_COUNTER_RESETTER = new IoFutureListener<CloseFuture>() {
/*      */       public void operationComplete(CloseFuture future) {
/*   83 */         AbstractIoSession session = (AbstractIoSession)future.getSession();
/*   84 */         session.scheduledWriteBytes.set(0);
/*   85 */         session.scheduledWriteMessages.set(0);
/*   86 */         session.readBytesThroughput = 0.0D;
/*   87 */         session.readMessagesThroughput = 0.0D;
/*   88 */         session.writtenBytesThroughput = 0.0D;
/*   89 */         session.writtenMessagesThroughput = 0.0D;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   98 */   private static final WriteRequest CLOSE_REQUEST = (WriteRequest)new DefaultWriteRequest(new Object());
/*      */   
/*  100 */   private final Object lock = new Object();
/*      */ 
/*      */   
/*      */   private IoSessionAttributeMap attributes;
/*      */ 
/*      */   
/*      */   private WriteRequestQueue writeRequestQueue;
/*      */   
/*      */   private WriteRequest currentWriteRequest;
/*      */   
/*      */   private final long creationTime;
/*      */   
/*  112 */   private static AtomicLong idGenerator = new AtomicLong(0L);
/*      */ 
/*      */ 
/*      */   
/*      */   private long sessionId;
/*      */ 
/*      */ 
/*      */   
/*  120 */   private final CloseFuture closeFuture = (CloseFuture)new DefaultCloseFuture(this);
/*      */ 
/*      */   
/*      */   private volatile boolean closing;
/*      */ 
/*      */   
/*      */   private boolean readSuspended = false;
/*      */   
/*      */   private boolean writeSuspended = false;
/*      */   
/*  130 */   private final AtomicBoolean scheduledForFlush = new AtomicBoolean();
/*      */   
/*  132 */   private final AtomicInteger scheduledWriteBytes = new AtomicInteger();
/*      */   
/*  134 */   private final AtomicInteger scheduledWriteMessages = new AtomicInteger();
/*      */   
/*      */   private long readBytes;
/*      */   
/*      */   private long writtenBytes;
/*      */   
/*      */   private long readMessages;
/*      */   
/*      */   private long writtenMessages;
/*      */   
/*      */   private long lastReadTime;
/*      */   
/*      */   private long lastWriteTime;
/*      */   
/*      */   private long lastThroughputCalculationTime;
/*      */   
/*      */   private long lastReadBytes;
/*      */   
/*      */   private long lastWrittenBytes;
/*      */   
/*      */   private long lastReadMessages;
/*      */   
/*      */   private long lastWrittenMessages;
/*      */   
/*      */   private double readBytesThroughput;
/*      */   
/*      */   private double writtenBytesThroughput;
/*      */   
/*      */   private double readMessagesThroughput;
/*      */   
/*      */   private double writtenMessagesThroughput;
/*      */   
/*  166 */   private AtomicInteger idleCountForBoth = new AtomicInteger();
/*      */   
/*  168 */   private AtomicInteger idleCountForRead = new AtomicInteger();
/*      */   
/*  170 */   private AtomicInteger idleCountForWrite = new AtomicInteger();
/*      */ 
/*      */   
/*      */   private long lastIdleTimeForBoth;
/*      */ 
/*      */   
/*      */   private long lastIdleTimeForRead;
/*      */   
/*      */   private long lastIdleTimeForWrite;
/*      */   
/*      */   private boolean deferDecreaseReadBuffer = true;
/*      */ 
/*      */   
/*      */   protected AbstractIoSession(IoService service) {
/*  184 */     this.service = service;
/*  185 */     this.handler = service.getHandler();
/*      */ 
/*      */     
/*  188 */     long currentTime = System.currentTimeMillis();
/*  189 */     this.creationTime = currentTime;
/*  190 */     this.lastThroughputCalculationTime = currentTime;
/*  191 */     this.lastReadTime = currentTime;
/*  192 */     this.lastWriteTime = currentTime;
/*  193 */     this.lastIdleTimeForBoth = currentTime;
/*  194 */     this.lastIdleTimeForRead = currentTime;
/*  195 */     this.lastIdleTimeForWrite = currentTime;
/*      */ 
/*      */     
/*  198 */     this.closeFuture.addListener(SCHEDULED_COUNTER_RESETTER);
/*      */ 
/*      */     
/*  201 */     this.sessionId = idGenerator.incrementAndGet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getId() {
/*  210 */     return this.sessionId;
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
/*      */   public final boolean isConnected() {
/*  222 */     return !this.closeFuture.isClosed();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isClosing() {
/*  229 */     return (this.closing || this.closeFuture.isClosed());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSecured() {
/*  237 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CloseFuture getCloseFuture() {
/*  244 */     return this.closeFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isScheduledForFlush() {
/*  253 */     return this.scheduledForFlush.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void scheduledForFlush() {
/*  260 */     this.scheduledForFlush.set(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void unscheduledForFlush() {
/*  267 */     this.scheduledForFlush.set(false);
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
/*      */   public final boolean setScheduledForFlush(boolean schedule) {
/*  280 */     if (schedule)
/*      */     {
/*      */ 
/*      */       
/*  284 */       return this.scheduledForFlush.compareAndSet(false, schedule);
/*      */     }
/*      */     
/*  287 */     this.scheduledForFlush.set(schedule);
/*  288 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CloseFuture close(boolean rightNow) {
/*  295 */     if (!isClosing()) {
/*  296 */       if (rightNow) {
/*  297 */         return close();
/*      */       }
/*      */       
/*  300 */       return closeOnFlush();
/*      */     } 
/*  302 */     return this.closeFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CloseFuture close() {
/*  310 */     synchronized (this.lock) {
/*  311 */       if (isClosing()) {
/*  312 */         return this.closeFuture;
/*      */       }
/*      */       
/*  315 */       this.closing = true;
/*      */     } 
/*      */     
/*  318 */     getFilterChain().fireFilterClose();
/*  319 */     return this.closeFuture;
/*      */   }
/*      */   
/*      */   private final CloseFuture closeOnFlush() {
/*  323 */     getWriteRequestQueue().offer(this, CLOSE_REQUEST);
/*  324 */     getProcessor().flush(this);
/*  325 */     return this.closeFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoHandler getHandler() {
/*  332 */     return this.handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoSessionConfig getConfig() {
/*  339 */     return this.config;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ReadFuture read() {
/*      */     DefaultReadFuture defaultReadFuture;
/*  346 */     if (!getConfig().isUseReadOperation()) {
/*  347 */       throw new IllegalStateException("useReadOperation is not enabled.");
/*      */     }
/*      */     
/*  350 */     Queue<ReadFuture> readyReadFutures = getReadyReadFutures();
/*      */     
/*  352 */     synchronized (readyReadFutures) {
/*  353 */       ReadFuture future = readyReadFutures.poll();
/*  354 */       if (future != null) {
/*  355 */         if (future.isClosed())
/*      */         {
/*  357 */           readyReadFutures.offer(future);
/*      */         }
/*      */       } else {
/*  360 */         defaultReadFuture = new DefaultReadFuture(this);
/*  361 */         getWaitingReadFutures().offer(defaultReadFuture);
/*      */       } 
/*      */     } 
/*      */     
/*  365 */     return (ReadFuture)defaultReadFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void offerReadFuture(Object message) {
/*  372 */     newReadFuture().setRead(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void offerFailedReadFuture(Throwable exception) {
/*  379 */     newReadFuture().setException(exception);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void offerClosedReadFuture() {
/*  386 */     Queue<ReadFuture> readyReadFutures = getReadyReadFutures();
/*  387 */     synchronized (readyReadFutures) {
/*  388 */       newReadFuture().setClosed();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ReadFuture newReadFuture() {
/*      */     DefaultReadFuture defaultReadFuture;
/*  396 */     Queue<ReadFuture> readyReadFutures = getReadyReadFutures();
/*  397 */     Queue<ReadFuture> waitingReadFutures = getWaitingReadFutures();
/*      */     
/*  399 */     synchronized (readyReadFutures) {
/*  400 */       ReadFuture future = waitingReadFutures.poll();
/*  401 */       if (future == null) {
/*  402 */         defaultReadFuture = new DefaultReadFuture(this);
/*  403 */         readyReadFutures.offer(defaultReadFuture);
/*      */       } 
/*      */     } 
/*  406 */     return (ReadFuture)defaultReadFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Queue<ReadFuture> getReadyReadFutures() {
/*  413 */     Queue<ReadFuture> readyReadFutures = (Queue<ReadFuture>)getAttribute(READY_READ_FUTURES_KEY);
/*  414 */     if (readyReadFutures == null) {
/*  415 */       readyReadFutures = new ConcurrentLinkedQueue<ReadFuture>();
/*      */       
/*  417 */       Queue<ReadFuture> oldReadyReadFutures = (Queue<ReadFuture>)setAttributeIfAbsent(READY_READ_FUTURES_KEY, readyReadFutures);
/*      */       
/*  419 */       if (oldReadyReadFutures != null) {
/*  420 */         readyReadFutures = oldReadyReadFutures;
/*      */       }
/*      */     } 
/*  423 */     return readyReadFutures;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Queue<ReadFuture> getWaitingReadFutures() {
/*  430 */     Queue<ReadFuture> waitingReadyReadFutures = (Queue<ReadFuture>)getAttribute(WAITING_READ_FUTURES_KEY);
/*  431 */     if (waitingReadyReadFutures == null) {
/*  432 */       waitingReadyReadFutures = new ConcurrentLinkedQueue<ReadFuture>();
/*      */       
/*  434 */       Queue<ReadFuture> oldWaitingReadyReadFutures = (Queue<ReadFuture>)setAttributeIfAbsent(WAITING_READ_FUTURES_KEY, waitingReadyReadFutures);
/*      */       
/*  436 */       if (oldWaitingReadyReadFutures != null) {
/*  437 */         waitingReadyReadFutures = oldWaitingReadyReadFutures;
/*      */       }
/*      */     } 
/*  440 */     return waitingReadyReadFutures;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WriteFuture write(Object message) {
/*  447 */     return write(message, (SocketAddress)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WriteFuture write(Object message, SocketAddress remoteAddress) {
/*  454 */     if (message == null) {
/*  455 */       throw new IllegalArgumentException("Trying to write a null message : not allowed");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  460 */     if (!getTransportMetadata().isConnectionless() && remoteAddress != null) {
/*  461 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  467 */     if (isClosing() || !isConnected()) {
/*  468 */       DefaultWriteFuture defaultWriteFuture1 = new DefaultWriteFuture(this);
/*  469 */       DefaultWriteRequest defaultWriteRequest1 = new DefaultWriteRequest(message, (WriteFuture)defaultWriteFuture1, remoteAddress);
/*  470 */       WriteToClosedSessionException writeToClosedSessionException = new WriteToClosedSessionException((WriteRequest)defaultWriteRequest1);
/*  471 */       defaultWriteFuture1.setException((Throwable)writeToClosedSessionException);
/*  472 */       return (WriteFuture)defaultWriteFuture1;
/*      */     } 
/*      */     
/*  475 */     FileChannel openedFileChannel = null;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  480 */       if (message instanceof IoBuffer && !((IoBuffer)message).hasRemaining())
/*      */       {
/*  482 */         throw new IllegalArgumentException("message is empty. Forgot to call flip()?"); } 
/*  483 */       if (message instanceof FileChannel) {
/*  484 */         FileChannel fileChannel = (FileChannel)message;
/*  485 */         message = new DefaultFileRegion(fileChannel, 0L, fileChannel.size());
/*  486 */       } else if (message instanceof File) {
/*  487 */         File file = (File)message;
/*  488 */         openedFileChannel = (new FileInputStream(file)).getChannel();
/*  489 */         message = new FilenameFileRegion(file, openedFileChannel, 0L, openedFileChannel.size());
/*      */       } 
/*  491 */     } catch (IOException e) {
/*  492 */       ExceptionMonitor.getInstance().exceptionCaught(e);
/*  493 */       return DefaultWriteFuture.newNotWrittenFuture(this, e);
/*      */     } 
/*      */ 
/*      */     
/*  497 */     DefaultWriteFuture defaultWriteFuture = new DefaultWriteFuture(this);
/*  498 */     DefaultWriteRequest defaultWriteRequest = new DefaultWriteRequest(message, (WriteFuture)defaultWriteFuture, remoteAddress);
/*      */ 
/*      */     
/*  501 */     IoFilterChain filterChain = getFilterChain();
/*  502 */     filterChain.fireFilterWrite((WriteRequest)defaultWriteRequest);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  507 */     if (openedFileChannel != null) {
/*      */ 
/*      */       
/*  510 */       final FileChannel finalChannel = openedFileChannel;
/*  511 */       defaultWriteFuture.addListener(new IoFutureListener<WriteFuture>() {
/*      */             public void operationComplete(WriteFuture future) {
/*      */               try {
/*  514 */                 finalChannel.close();
/*  515 */               } catch (IOException e) {
/*  516 */                 ExceptionMonitor.getInstance().exceptionCaught(e);
/*      */               } 
/*      */             }
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/*  523 */     return (WriteFuture)defaultWriteFuture;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getAttachment() {
/*  530 */     return getAttribute("");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object setAttachment(Object attachment) {
/*  537 */     return setAttribute("", attachment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getAttribute(Object key) {
/*  544 */     return getAttribute(key, (Object)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getAttribute(Object key, Object defaultValue) {
/*  551 */     return this.attributes.getAttribute(this, key, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object setAttribute(Object key, Object value) {
/*  558 */     return this.attributes.setAttribute(this, key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object setAttribute(Object key) {
/*  565 */     return setAttribute(key, Boolean.TRUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object setAttributeIfAbsent(Object key, Object value) {
/*  572 */     return this.attributes.setAttributeIfAbsent(this, key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object setAttributeIfAbsent(Object key) {
/*  579 */     return setAttributeIfAbsent(key, Boolean.TRUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object removeAttribute(Object key) {
/*  586 */     return this.attributes.removeAttribute(this, key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean removeAttribute(Object key, Object value) {
/*  593 */     return this.attributes.removeAttribute(this, key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
/*  600 */     return this.attributes.replaceAttribute(this, key, oldValue, newValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean containsAttribute(Object key) {
/*  607 */     return this.attributes.containsAttribute(this, key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Set<Object> getAttributeKeys() {
/*  614 */     return this.attributes.getAttributeKeys(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final IoSessionAttributeMap getAttributeMap() {
/*  621 */     return this.attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAttributeMap(IoSessionAttributeMap attributes) {
/*  628 */     this.attributes = attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setWriteRequestQueue(WriteRequestQueue writeRequestQueue) {
/*  638 */     this.writeRequestQueue = new CloseAwareWriteQueue(writeRequestQueue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void suspendRead() {
/*  645 */     this.readSuspended = true;
/*  646 */     if (isClosing() || !isConnected()) {
/*      */       return;
/*      */     }
/*  649 */     getProcessor().updateTrafficControl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void suspendWrite() {
/*  656 */     this.writeSuspended = true;
/*  657 */     if (isClosing() || !isConnected()) {
/*      */       return;
/*      */     }
/*  660 */     getProcessor().updateTrafficControl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void resumeRead() {
/*  668 */     this.readSuspended = false;
/*  669 */     if (isClosing() || !isConnected()) {
/*      */       return;
/*      */     }
/*  672 */     getProcessor().updateTrafficControl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void resumeWrite() {
/*  680 */     this.writeSuspended = false;
/*  681 */     if (isClosing() || !isConnected()) {
/*      */       return;
/*      */     }
/*  684 */     getProcessor().updateTrafficControl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadSuspended() {
/*  691 */     return this.readSuspended;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWriteSuspended() {
/*  698 */     return this.writeSuspended;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getReadBytes() {
/*  705 */     return this.readBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getWrittenBytes() {
/*  712 */     return this.writtenBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getReadMessages() {
/*  719 */     return this.readMessages;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getWrittenMessages() {
/*  726 */     return this.writtenMessages;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getReadBytesThroughput() {
/*  733 */     return this.readBytesThroughput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getWrittenBytesThroughput() {
/*  740 */     return this.writtenBytesThroughput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getReadMessagesThroughput() {
/*  747 */     return this.readMessagesThroughput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getWrittenMessagesThroughput() {
/*  754 */     return this.writtenMessagesThroughput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateThroughput(long currentTime, boolean force) {
/*  761 */     int interval = (int)(currentTime - this.lastThroughputCalculationTime);
/*      */     
/*  763 */     long minInterval = getConfig().getThroughputCalculationIntervalInMillis();
/*      */     
/*  765 */     if ((minInterval == 0L || interval < minInterval) && !force) {
/*      */       return;
/*      */     }
/*      */     
/*  769 */     this.readBytesThroughput = (this.readBytes - this.lastReadBytes) * 1000.0D / interval;
/*  770 */     this.writtenBytesThroughput = (this.writtenBytes - this.lastWrittenBytes) * 1000.0D / interval;
/*  771 */     this.readMessagesThroughput = (this.readMessages - this.lastReadMessages) * 1000.0D / interval;
/*  772 */     this.writtenMessagesThroughput = (this.writtenMessages - this.lastWrittenMessages) * 1000.0D / interval;
/*      */     
/*  774 */     this.lastReadBytes = this.readBytes;
/*  775 */     this.lastWrittenBytes = this.writtenBytes;
/*  776 */     this.lastReadMessages = this.readMessages;
/*  777 */     this.lastWrittenMessages = this.writtenMessages;
/*      */     
/*  779 */     this.lastThroughputCalculationTime = currentTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getScheduledWriteBytes() {
/*  786 */     return this.scheduledWriteBytes.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getScheduledWriteMessages() {
/*  793 */     return this.scheduledWriteMessages.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setScheduledWriteBytes(int byteCount) {
/*  800 */     this.scheduledWriteBytes.set(byteCount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setScheduledWriteMessages(int messages) {
/*  807 */     this.scheduledWriteMessages.set(messages);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseReadBytes(long increment, long currentTime) {
/*  814 */     if (increment <= 0L) {
/*      */       return;
/*      */     }
/*      */     
/*  818 */     this.readBytes += increment;
/*  819 */     this.lastReadTime = currentTime;
/*  820 */     this.idleCountForBoth.set(0);
/*  821 */     this.idleCountForRead.set(0);
/*      */     
/*  823 */     if (getService() instanceof AbstractIoService) {
/*  824 */       ((AbstractIoService)getService()).getStatistics().increaseReadBytes(increment, currentTime);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseReadMessages(long currentTime) {
/*  832 */     this.readMessages++;
/*  833 */     this.lastReadTime = currentTime;
/*  834 */     this.idleCountForBoth.set(0);
/*  835 */     this.idleCountForRead.set(0);
/*      */     
/*  837 */     if (getService() instanceof AbstractIoService) {
/*  838 */       ((AbstractIoService)getService()).getStatistics().increaseReadMessages(currentTime);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseWrittenBytes(int increment, long currentTime) {
/*  846 */     if (increment <= 0) {
/*      */       return;
/*      */     }
/*      */     
/*  850 */     this.writtenBytes += increment;
/*  851 */     this.lastWriteTime = currentTime;
/*  852 */     this.idleCountForBoth.set(0);
/*  853 */     this.idleCountForWrite.set(0);
/*      */     
/*  855 */     if (getService() instanceof AbstractIoService) {
/*  856 */       ((AbstractIoService)getService()).getStatistics().increaseWrittenBytes(increment, currentTime);
/*      */     }
/*      */     
/*  859 */     increaseScheduledWriteBytes(-increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseWrittenMessages(WriteRequest request, long currentTime) {
/*  866 */     Object message = request.getMessage();
/*      */     
/*  868 */     if (message instanceof IoBuffer) {
/*  869 */       IoBuffer b = (IoBuffer)message;
/*      */       
/*  871 */       if (b.hasRemaining()) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  876 */     this.writtenMessages++;
/*  877 */     this.lastWriteTime = currentTime;
/*      */     
/*  879 */     if (getService() instanceof AbstractIoService) {
/*  880 */       ((AbstractIoService)getService()).getStatistics().increaseWrittenMessages(currentTime);
/*      */     }
/*      */     
/*  883 */     decreaseScheduledWriteMessages();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseScheduledWriteBytes(int increment) {
/*  893 */     this.scheduledWriteBytes.addAndGet(increment);
/*  894 */     if (getService() instanceof AbstractIoService) {
/*  895 */       ((AbstractIoService)getService()).getStatistics().increaseScheduledWriteBytes(increment);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseScheduledWriteMessages() {
/*  903 */     this.scheduledWriteMessages.incrementAndGet();
/*  904 */     if (getService() instanceof AbstractIoService) {
/*  905 */       ((AbstractIoService)getService()).getStatistics().increaseScheduledWriteMessages();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void decreaseScheduledWriteMessages() {
/*  913 */     this.scheduledWriteMessages.decrementAndGet();
/*  914 */     if (getService() instanceof AbstractIoService) {
/*  915 */       ((AbstractIoService)getService()).getStatistics().decreaseScheduledWriteMessages();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void decreaseScheduledBytesAndMessages(WriteRequest request) {
/*  923 */     Object message = request.getMessage();
/*  924 */     if (message instanceof IoBuffer) {
/*  925 */       IoBuffer b = (IoBuffer)message;
/*  926 */       if (b.hasRemaining()) {
/*  927 */         increaseScheduledWriteBytes(-((IoBuffer)message).remaining());
/*      */       } else {
/*  929 */         decreaseScheduledWriteMessages();
/*      */       } 
/*      */     } else {
/*  932 */       decreaseScheduledWriteMessages();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final WriteRequestQueue getWriteRequestQueue() {
/*  940 */     if (this.writeRequestQueue == null) {
/*  941 */       throw new IllegalStateException();
/*      */     }
/*  943 */     return this.writeRequestQueue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final WriteRequest getCurrentWriteRequest() {
/*  950 */     return this.currentWriteRequest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getCurrentWriteMessage() {
/*  957 */     WriteRequest req = getCurrentWriteRequest();
/*  958 */     if (req == null) {
/*  959 */       return null;
/*      */     }
/*  961 */     return req.getMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCurrentWriteRequest(WriteRequest currentWriteRequest) {
/*  968 */     this.currentWriteRequest = currentWriteRequest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseReadBufferSize() {
/*  975 */     int newReadBufferSize = getConfig().getReadBufferSize() << 1;
/*  976 */     if (newReadBufferSize <= getConfig().getMaxReadBufferSize()) {
/*  977 */       getConfig().setReadBufferSize(newReadBufferSize);
/*      */     } else {
/*  979 */       getConfig().setReadBufferSize(getConfig().getMaxReadBufferSize());
/*      */     } 
/*      */     
/*  982 */     this.deferDecreaseReadBuffer = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void decreaseReadBufferSize() {
/*  989 */     if (this.deferDecreaseReadBuffer) {
/*  990 */       this.deferDecreaseReadBuffer = false;
/*      */       
/*      */       return;
/*      */     } 
/*  994 */     if (getConfig().getReadBufferSize() > getConfig().getMinReadBufferSize()) {
/*  995 */       getConfig().setReadBufferSize(getConfig().getReadBufferSize() >>> 1);
/*      */     }
/*      */     
/*  998 */     this.deferDecreaseReadBuffer = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCreationTime() {
/* 1005 */     return this.creationTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastIoTime() {
/* 1012 */     return Math.max(this.lastReadTime, this.lastWriteTime);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastReadTime() {
/* 1019 */     return this.lastReadTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastWriteTime() {
/* 1026 */     return this.lastWriteTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isIdle(IdleStatus status) {
/* 1033 */     if (status == IdleStatus.BOTH_IDLE) {
/* 1034 */       return (this.idleCountForBoth.get() > 0);
/*      */     }
/*      */     
/* 1037 */     if (status == IdleStatus.READER_IDLE) {
/* 1038 */       return (this.idleCountForRead.get() > 0);
/*      */     }
/*      */     
/* 1041 */     if (status == IdleStatus.WRITER_IDLE) {
/* 1042 */       return (this.idleCountForWrite.get() > 0);
/*      */     }
/*      */     
/* 1045 */     throw new IllegalArgumentException("Unknown idle status: " + status);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBothIdle() {
/* 1052 */     return isIdle(IdleStatus.BOTH_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isReaderIdle() {
/* 1059 */     return isIdle(IdleStatus.READER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isWriterIdle() {
/* 1066 */     return isIdle(IdleStatus.WRITER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getIdleCount(IdleStatus status) {
/* 1073 */     if (getConfig().getIdleTime(status) == 0) {
/* 1074 */       if (status == IdleStatus.BOTH_IDLE) {
/* 1075 */         this.idleCountForBoth.set(0);
/*      */       }
/*      */       
/* 1078 */       if (status == IdleStatus.READER_IDLE) {
/* 1079 */         this.idleCountForRead.set(0);
/*      */       }
/*      */       
/* 1082 */       if (status == IdleStatus.WRITER_IDLE) {
/* 1083 */         this.idleCountForWrite.set(0);
/*      */       }
/*      */     } 
/*      */     
/* 1087 */     if (status == IdleStatus.BOTH_IDLE) {
/* 1088 */       return this.idleCountForBoth.get();
/*      */     }
/*      */     
/* 1091 */     if (status == IdleStatus.READER_IDLE) {
/* 1092 */       return this.idleCountForRead.get();
/*      */     }
/*      */     
/* 1095 */     if (status == IdleStatus.WRITER_IDLE) {
/* 1096 */       return this.idleCountForWrite.get();
/*      */     }
/*      */     
/* 1099 */     throw new IllegalArgumentException("Unknown idle status: " + status);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastIdleTime(IdleStatus status) {
/* 1106 */     if (status == IdleStatus.BOTH_IDLE) {
/* 1107 */       return this.lastIdleTimeForBoth;
/*      */     }
/*      */     
/* 1110 */     if (status == IdleStatus.READER_IDLE) {
/* 1111 */       return this.lastIdleTimeForRead;
/*      */     }
/*      */     
/* 1114 */     if (status == IdleStatus.WRITER_IDLE) {
/* 1115 */       return this.lastIdleTimeForWrite;
/*      */     }
/*      */     
/* 1118 */     throw new IllegalArgumentException("Unknown idle status: " + status);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void increaseIdleCount(IdleStatus status, long currentTime) {
/* 1125 */     if (status == IdleStatus.BOTH_IDLE) {
/* 1126 */       this.idleCountForBoth.incrementAndGet();
/* 1127 */       this.lastIdleTimeForBoth = currentTime;
/* 1128 */     } else if (status == IdleStatus.READER_IDLE) {
/* 1129 */       this.idleCountForRead.incrementAndGet();
/* 1130 */       this.lastIdleTimeForRead = currentTime;
/* 1131 */     } else if (status == IdleStatus.WRITER_IDLE) {
/* 1132 */       this.idleCountForWrite.incrementAndGet();
/* 1133 */       this.lastIdleTimeForWrite = currentTime;
/*      */     } else {
/* 1135 */       throw new IllegalArgumentException("Unknown idle status: " + status);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getBothIdleCount() {
/* 1143 */     return getIdleCount(IdleStatus.BOTH_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastBothIdleTime() {
/* 1150 */     return getLastIdleTime(IdleStatus.BOTH_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastReaderIdleTime() {
/* 1157 */     return getLastIdleTime(IdleStatus.READER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLastWriterIdleTime() {
/* 1164 */     return getLastIdleTime(IdleStatus.WRITER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getReaderIdleCount() {
/* 1171 */     return getIdleCount(IdleStatus.READER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getWriterIdleCount() {
/* 1178 */     return getIdleCount(IdleStatus.WRITER_IDLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SocketAddress getServiceAddress() {
/* 1185 */     IoService service = getService();
/* 1186 */     if (service instanceof IoAcceptor) {
/* 1187 */       return ((IoAcceptor)service).getLocalAddress();
/*      */     }
/*      */     
/* 1190 */     return getRemoteAddress();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int hashCode() {
/* 1198 */     return super.hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean equals(Object o) {
/* 1207 */     return super.equals(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1215 */     if (isConnected() || isClosing()) {
/* 1216 */       String remote = null;
/* 1217 */       String local = null;
/*      */       
/*      */       try {
/* 1220 */         remote = String.valueOf(getRemoteAddress());
/* 1221 */       } catch (Exception e) {
/* 1222 */         remote = "Cannot get the remote address informations: " + e.getMessage();
/*      */       } 
/*      */       
/*      */       try {
/* 1226 */         local = String.valueOf(getLocalAddress());
/* 1227 */       } catch (Exception e) {}
/*      */ 
/*      */       
/* 1230 */       if (getService() instanceof IoAcceptor) {
/* 1231 */         return "(" + getIdAsString() + ": " + getServiceName() + ", server, " + remote + " => " + local + ')';
/*      */       }
/*      */       
/* 1234 */       return "(" + getIdAsString() + ": " + getServiceName() + ", client, " + local + " => " + remote + ')';
/*      */     } 
/*      */     
/* 1237 */     return "(" + getIdAsString() + ") Session disconnected ...";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getIdAsString() {
/* 1244 */     String id = Long.toHexString(getId()).toUpperCase();
/*      */ 
/*      */ 
/*      */     
/* 1248 */     while (id.length() < 8) {
/* 1249 */       id = '0' + id;
/*      */     }
/* 1251 */     id = "0x" + id;
/*      */     
/* 1253 */     return id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getServiceName() {
/* 1260 */     TransportMetadata tm = getTransportMetadata();
/* 1261 */     if (tm == null) {
/* 1262 */       return "null";
/*      */     }
/*      */     
/* 1265 */     return tm.getProviderName() + ' ' + tm.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IoService getService() {
/* 1272 */     return this.service;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void notifyIdleness(Iterator<? extends IoSession> sessions, long currentTime) {
/* 1283 */     IoSession s = null;
/* 1284 */     while (sessions.hasNext()) {
/* 1285 */       s = sessions.next();
/* 1286 */       notifyIdleSession(s, currentTime);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void notifyIdleSession(IoSession session, long currentTime) {
/* 1298 */     notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.BOTH_IDLE), IdleStatus.BOTH_IDLE, Math.max(session.getLastIoTime(), session.getLastIdleTime(IdleStatus.BOTH_IDLE)));
/*      */ 
/*      */     
/* 1301 */     notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.READER_IDLE), IdleStatus.READER_IDLE, Math.max(session.getLastReadTime(), session.getLastIdleTime(IdleStatus.READER_IDLE)));
/*      */ 
/*      */ 
/*      */     
/* 1305 */     notifyIdleSession0(session, currentTime, session.getConfig().getIdleTimeInMillis(IdleStatus.WRITER_IDLE), IdleStatus.WRITER_IDLE, Math.max(session.getLastWriteTime(), session.getLastIdleTime(IdleStatus.WRITER_IDLE)));
/*      */ 
/*      */ 
/*      */     
/* 1309 */     notifyWriteTimeout(session, currentTime);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void notifyIdleSession0(IoSession session, long currentTime, long idleTime, IdleStatus status, long lastIoTime) {
/* 1314 */     if (idleTime > 0L && lastIoTime != 0L && currentTime - lastIoTime >= idleTime) {
/* 1315 */       session.getFilterChain().fireSessionIdle(status);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void notifyWriteTimeout(IoSession session, long currentTime) {
/* 1321 */     long writeTimeout = session.getConfig().getWriteTimeoutInMillis();
/* 1322 */     if (writeTimeout > 0L && currentTime - session.getLastWriteTime() >= writeTimeout && !session.getWriteRequestQueue().isEmpty(session)) {
/*      */       
/* 1324 */       WriteRequest request = session.getCurrentWriteRequest();
/* 1325 */       if (request != null) {
/* 1326 */         session.setCurrentWriteRequest(null);
/* 1327 */         WriteTimeoutException cause = new WriteTimeoutException(request);
/* 1328 */         request.getFuture().setException((Throwable)cause);
/* 1329 */         session.getFilterChain().fireExceptionCaught((Throwable)cause);
/*      */         
/* 1331 */         session.close(true);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract IoProcessor getProcessor();
/*      */ 
/*      */ 
/*      */   
/*      */   private class CloseAwareWriteQueue
/*      */     implements WriteRequestQueue
/*      */   {
/*      */     private final WriteRequestQueue queue;
/*      */ 
/*      */ 
/*      */     
/*      */     public CloseAwareWriteQueue(WriteRequestQueue queue) {
/* 1350 */       this.queue = queue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public synchronized WriteRequest poll(IoSession session) {
/* 1357 */       WriteRequest answer = this.queue.poll(session);
/*      */       
/* 1359 */       if (answer == AbstractIoSession.CLOSE_REQUEST) {
/* 1360 */         AbstractIoSession.this.close();
/* 1361 */         dispose(session);
/* 1362 */         answer = null;
/*      */       } 
/*      */       
/* 1365 */       return answer;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void offer(IoSession session, WriteRequest e) {
/* 1372 */       this.queue.offer(session, e);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty(IoSession session) {
/* 1379 */       return this.queue.isEmpty(session);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear(IoSession session) {
/* 1386 */       this.queue.clear(session);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void dispose(IoSession session) {
/* 1393 */       this.queue.dispose(session);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/* 1400 */       return this.queue.size();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/AbstractIoSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */