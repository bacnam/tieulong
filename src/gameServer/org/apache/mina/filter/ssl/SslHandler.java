/*     */ package org.apache.mina.filter.ssl;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterEvent;
/*     */ import org.apache.mina.core.future.DefaultWriteFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SslHandler
/*     */ {
/*  65 */   private static final Logger LOGGER = LoggerFactory.getLogger(SslHandler.class);
/*     */ 
/*     */   
/*     */   private final SslFilter sslFilter;
/*     */ 
/*     */   
/*     */   private final IoSession session;
/*     */   
/*  73 */   private final Queue<IoFilterEvent> preHandshakeEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();
/*     */   
/*  75 */   private final Queue<IoFilterEvent> filterWriteEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();
/*     */ 
/*     */   
/*  78 */   private final Queue<IoFilterEvent> messageReceivedEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLEngine sslEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoBuffer inNetBuffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private IoBuffer outNetBuffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private IoBuffer appBuffer;
/*     */ 
/*     */ 
/*     */   
/* 100 */   private final IoBuffer emptyBuffer = IoBuffer.allocate(0);
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLEngineResult.HandshakeStatus handshakeStatus;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean firstSSLNegociation;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handshakeComplete;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writingEncryptedData;
/*     */ 
/*     */   
/* 119 */   private Lock sslLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SslHandler(SslFilter sslFilter, IoSession session) throws SSLException {
/* 128 */     this.sslFilter = sslFilter;
/* 129 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void init() throws SSLException {
/* 138 */     if (this.sslEngine != null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 143 */     LOGGER.debug("{} Initializing the SSL Handler", this.sslFilter.getSessionInfo(this.session));
/*     */     
/* 145 */     InetSocketAddress peer = (InetSocketAddress)this.session.getAttribute(SslFilter.PEER_ADDRESS);
/*     */ 
/*     */     
/* 148 */     if (peer == null) {
/* 149 */       this.sslEngine = this.sslFilter.sslContext.createSSLEngine();
/*     */     } else {
/* 151 */       this.sslEngine = this.sslFilter.sslContext.createSSLEngine(peer.getHostName(), peer.getPort());
/*     */     } 
/*     */ 
/*     */     
/* 155 */     this.sslEngine.setUseClientMode(this.sslFilter.isUseClientMode());
/*     */ 
/*     */     
/* 158 */     if (!this.sslEngine.getUseClientMode()) {
/*     */       
/* 160 */       if (this.sslFilter.isWantClientAuth()) {
/* 161 */         this.sslEngine.setWantClientAuth(true);
/*     */       }
/*     */       
/* 164 */       if (this.sslFilter.isNeedClientAuth()) {
/* 165 */         this.sslEngine.setNeedClientAuth(true);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 170 */     if (this.sslFilter.getEnabledCipherSuites() != null) {
/* 171 */       this.sslEngine.setEnabledCipherSuites(this.sslFilter.getEnabledCipherSuites());
/*     */     }
/*     */ 
/*     */     
/* 175 */     if (this.sslFilter.getEnabledProtocols() != null) {
/* 176 */       this.sslEngine.setEnabledProtocols(this.sslFilter.getEnabledProtocols());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 181 */     this.sslEngine.beginHandshake();
/*     */     
/* 183 */     this.handshakeStatus = this.sslEngine.getHandshakeStatus();
/*     */ 
/*     */     
/* 186 */     this.writingEncryptedData = false;
/*     */ 
/*     */ 
/*     */     
/* 190 */     this.firstSSLNegociation = true;
/* 191 */     this.handshakeComplete = false;
/*     */     
/* 193 */     if (LOGGER.isDebugEnabled()) {
/* 194 */       LOGGER.debug("{} SSL Handler Initialization done.", this.sslFilter.getSessionInfo(this.session));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void destroy() {
/* 202 */     if (this.sslEngine == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 208 */       this.sslEngine.closeInbound();
/* 209 */     } catch (SSLException e) {
/* 210 */       LOGGER.debug("Unexpected exception from SSLEngine.closeInbound().", e);
/*     */     } 
/*     */     
/* 213 */     if (this.outNetBuffer != null) {
/* 214 */       this.outNetBuffer.capacity(this.sslEngine.getSession().getPacketBufferSize());
/*     */     } else {
/* 216 */       createOutNetBuffer(0);
/*     */     } 
/*     */     try {
/*     */       do {
/* 220 */         this.outNetBuffer.clear();
/* 221 */       } while (this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf()).bytesProduced() > 0);
/* 222 */     } catch (SSLException e) {
/*     */     
/*     */     } finally {
/* 225 */       this.outNetBuffer.free();
/* 226 */       this.outNetBuffer = null;
/*     */     } 
/*     */     
/* 229 */     this.sslEngine.closeOutbound();
/* 230 */     this.sslEngine = null;
/*     */     
/* 232 */     this.preHandshakeEventQueue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SslFilter getSslFilter() {
/* 239 */     return this.sslFilter;
/*     */   }
/*     */   
/*     */   IoSession getSession() {
/* 243 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isWritingEncryptedData() {
/* 250 */     return this.writingEncryptedData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHandshakeComplete() {
/* 257 */     return this.handshakeComplete;
/*     */   }
/*     */   
/*     */   boolean isInboundDone() {
/* 261 */     return (this.sslEngine == null || this.sslEngine.isInboundDone());
/*     */   }
/*     */   
/*     */   boolean isOutboundDone() {
/* 265 */     return (this.sslEngine == null || this.sslEngine.isOutboundDone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean needToCompleteHandshake() {
/* 272 */     return (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP && !isInboundDone());
/*     */   }
/*     */   
/*     */   void schedulePreHandshakeWriteRequest(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
/* 276 */     this.preHandshakeEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.WRITE, this.session, writeRequest));
/*     */   }
/*     */ 
/*     */   
/*     */   void flushPreHandshakeEvents() throws SSLException {
/*     */     IoFilterEvent scheduledWrite;
/* 282 */     while ((scheduledWrite = this.preHandshakeEventQueue.poll()) != null) {
/* 283 */       this.sslFilter.filterWrite(scheduledWrite.getNextFilter(), this.session, (WriteRequest)scheduledWrite.getParameter());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void scheduleFilterWrite(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
/* 289 */     this.filterWriteEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.WRITE, this.session, writeRequest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void scheduleMessageReceived(IoFilter.NextFilter nextFilter, Object message) {
/* 300 */     this.messageReceivedEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.MESSAGE_RECEIVED, this.session, message));
/*     */   }
/*     */ 
/*     */   
/*     */   void flushScheduledEvents() {
/* 305 */     if (Thread.holdsLock(this)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     this.sslLock.lock();
/*     */     try {
/*     */       IoFilterEvent event;
/* 316 */       while ((event = this.filterWriteEventQueue.poll()) != null) {
/* 317 */         IoFilter.NextFilter nextFilter = event.getNextFilter();
/* 318 */         nextFilter.filterWrite(this.session, (WriteRequest)event.getParameter());
/*     */       } 
/*     */       
/* 321 */       while ((event = this.messageReceivedEventQueue.poll()) != null) {
/* 322 */         IoFilter.NextFilter nextFilter = event.getNextFilter();
/* 323 */         nextFilter.messageReceived(this.session, event.getParameter());
/*     */       } 
/*     */     } finally {
/* 326 */       this.sslLock.unlock();
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
/*     */   void messageReceived(IoFilter.NextFilter nextFilter, ByteBuffer buf) throws SSLException {
/* 339 */     if (LOGGER.isDebugEnabled()) {
/* 340 */       if (!isOutboundDone()) {
/* 341 */         LOGGER.debug("{} Processing the received message", this.sslFilter.getSessionInfo(this.session));
/*     */       } else {
/* 343 */         LOGGER.debug("{} Processing the received message", this.sslFilter.getSessionInfo(this.session));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 348 */     if (this.inNetBuffer == null) {
/* 349 */       this.inNetBuffer = IoBuffer.allocate(buf.remaining()).setAutoExpand(true);
/*     */     }
/*     */     
/* 352 */     this.inNetBuffer.put(buf);
/*     */     
/* 354 */     if (!this.handshakeComplete) {
/* 355 */       handshake(nextFilter);
/*     */     } else {
/*     */       
/* 358 */       this.inNetBuffer.flip();
/*     */       
/* 360 */       if (!this.inNetBuffer.hasRemaining()) {
/*     */         return;
/*     */       }
/*     */       
/* 364 */       SSLEngineResult res = unwrap();
/*     */ 
/*     */       
/* 367 */       if (this.inNetBuffer.hasRemaining()) {
/* 368 */         this.inNetBuffer.compact();
/*     */       } else {
/* 370 */         this.inNetBuffer.free();
/* 371 */         this.inNetBuffer = null;
/*     */       } 
/*     */       
/* 374 */       checkStatus(res);
/*     */       
/* 376 */       renegotiateIfNeeded(nextFilter, res);
/*     */     } 
/*     */     
/* 379 */     if (isInboundDone()) {
/*     */ 
/*     */       
/* 382 */       int inNetBufferPosition = (this.inNetBuffer == null) ? 0 : this.inNetBuffer.position();
/* 383 */       buf.position(buf.position() - inNetBufferPosition);
/*     */       
/* 385 */       if (this.inNetBuffer != null) {
/* 386 */         this.inNetBuffer.free();
/* 387 */         this.inNetBuffer = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IoBuffer fetchAppBuffer() {
/* 398 */     if (this.appBuffer == null) {
/* 399 */       return IoBuffer.allocate(0);
/*     */     }
/* 401 */     IoBuffer appBuffer = this.appBuffer.flip();
/* 402 */     this.appBuffer = null;
/*     */     
/* 404 */     return appBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IoBuffer fetchOutNetBuffer() {
/* 414 */     IoBuffer answer = this.outNetBuffer;
/* 415 */     if (answer == null) {
/* 416 */       return this.emptyBuffer;
/*     */     }
/*     */     
/* 419 */     this.outNetBuffer = null;
/* 420 */     return answer.shrink();
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
/*     */   void encrypt(ByteBuffer src) throws SSLException {
/* 432 */     if (!this.handshakeComplete) {
/* 433 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 436 */     if (!src.hasRemaining()) {
/* 437 */       if (this.outNetBuffer == null) {
/* 438 */         this.outNetBuffer = this.emptyBuffer;
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 443 */     createOutNetBuffer(src.remaining());
/*     */ 
/*     */     
/* 446 */     while (src.hasRemaining()) {
/*     */       
/* 448 */       SSLEngineResult result = this.sslEngine.wrap(src, this.outNetBuffer.buf());
/* 449 */       if (result.getStatus() == SSLEngineResult.Status.OK) {
/* 450 */         if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK)
/* 451 */           doTasks();  continue;
/*     */       } 
/* 453 */       if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 454 */         this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
/* 455 */         this.outNetBuffer.limit(this.outNetBuffer.capacity()); continue;
/*     */       } 
/* 457 */       throw new SSLException("SSLEngine error during encrypt: " + result.getStatus() + " src: " + src + "outNetBuffer: " + this.outNetBuffer);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 462 */     this.outNetBuffer.flip();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean closeOutbound() throws SSLException {
/*     */     SSLEngineResult result;
/* 474 */     if (this.sslEngine == null || this.sslEngine.isOutboundDone()) {
/* 475 */       return false;
/*     */     }
/*     */     
/* 478 */     this.sslEngine.closeOutbound();
/*     */     
/* 480 */     createOutNetBuffer(0);
/*     */ 
/*     */     
/*     */     while (true) {
/* 484 */       result = this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf());
/* 485 */       if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 486 */         this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
/* 487 */         this.outNetBuffer.limit(this.outNetBuffer.capacity());
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 493 */     if (result.getStatus() != SSLEngineResult.Status.CLOSED) {
/* 494 */       throw new SSLException("Improper close state: " + result);
/*     */     }
/*     */     
/* 497 */     this.outNetBuffer.flip();
/*     */     
/* 499 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkStatus(SSLEngineResult res) throws SSLException {
/* 508 */     SSLEngineResult.Status status = res.getStatus();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     if (status == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 519 */       throw new SSLException("SSLEngine error during decrypt: " + status + " inNetBuffer: " + this.inNetBuffer + "appBuffer: " + this.appBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void handshake(IoFilter.NextFilter nextFilter) throws SSLException {
/*     */     while (true) {
/*     */       SSLEngineResult.Status status;
/*     */       SSLEngineResult result;
/* 529 */       switch (this.handshakeStatus) {
/*     */         case FINISHED:
/*     */         case NOT_HANDSHAKING:
/* 532 */           if (LOGGER.isDebugEnabled()) {
/* 533 */             LOGGER.debug("{} processing the FINISHED state", this.sslFilter.getSessionInfo(this.session));
/*     */           }
/*     */           
/* 536 */           this.session.setAttribute(SslFilter.SSL_SESSION, this.sslEngine.getSession());
/* 537 */           this.handshakeComplete = true;
/*     */ 
/*     */           
/* 540 */           if (this.firstSSLNegociation && this.session.containsAttribute(SslFilter.USE_NOTIFICATION)) {
/*     */             
/* 542 */             this.firstSSLNegociation = false;
/* 543 */             scheduleMessageReceived(nextFilter, SslFilter.SESSION_SECURED);
/*     */           } 
/*     */           
/* 546 */           if (LOGGER.isDebugEnabled()) {
/* 547 */             if (!isOutboundDone()) {
/* 548 */               LOGGER.debug("{} is now secured", this.sslFilter.getSessionInfo(this.session));
/*     */             } else {
/* 550 */               LOGGER.debug("{} is not secured yet", this.sslFilter.getSessionInfo(this.session));
/*     */             } 
/*     */           }
/*     */           return;
/*     */ 
/*     */         
/*     */         case NEED_TASK:
/* 557 */           if (LOGGER.isDebugEnabled()) {
/* 558 */             LOGGER.debug("{} processing the NEED_TASK state", this.sslFilter.getSessionInfo(this.session));
/*     */           }
/*     */           
/* 561 */           this.handshakeStatus = doTasks();
/*     */           continue;
/*     */         
/*     */         case NEED_UNWRAP:
/* 565 */           if (LOGGER.isDebugEnabled()) {
/* 566 */             LOGGER.debug("{} processing the NEED_UNWRAP state", this.sslFilter.getSessionInfo(this.session));
/*     */           }
/*     */           
/* 569 */           status = unwrapHandshake(nextFilter);
/*     */           
/* 571 */           if ((status == SSLEngineResult.Status.BUFFER_UNDERFLOW && this.handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED) || isInboundDone()) {
/*     */             return;
/*     */           }
/*     */           continue;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case NEED_WRAP:
/* 580 */           if (LOGGER.isDebugEnabled()) {
/* 581 */             LOGGER.debug("{} processing the NEED_WRAP state", this.sslFilter.getSessionInfo(this.session));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 587 */           if (this.outNetBuffer != null && this.outNetBuffer.hasRemaining()) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 592 */           createOutNetBuffer(0);
/*     */           
/*     */           while (true) {
/* 595 */             result = this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf());
/* 596 */             if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 597 */               this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
/* 598 */               this.outNetBuffer.limit(this.outNetBuffer.capacity());
/*     */               
/*     */               continue;
/*     */             } 
/*     */             break;
/*     */           } 
/* 604 */           this.outNetBuffer.flip();
/* 605 */           this.handshakeStatus = result.getHandshakeStatus();
/* 606 */           writeNetBuffer(nextFilter); continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 610 */     String msg = "Invalid Handshaking State" + this.handshakeStatus + " while processing the Handshake for session " + this.session.getId();
/*     */     
/* 612 */     LOGGER.error(msg);
/* 613 */     throw new IllegalStateException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createOutNetBuffer(int expectedRemaining) {
/* 621 */     int capacity = Math.max(expectedRemaining, this.sslEngine.getSession().getPacketBufferSize());
/*     */     
/* 623 */     if (this.outNetBuffer != null) {
/* 624 */       this.outNetBuffer.capacity(capacity);
/*     */     } else {
/* 626 */       this.outNetBuffer = IoBuffer.allocate(capacity).minimumCapacity(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   WriteFuture writeNetBuffer(IoFilter.NextFilter nextFilter) throws SSLException {
/*     */     DefaultWriteFuture defaultWriteFuture;
/* 632 */     if (this.outNetBuffer == null || !this.outNetBuffer.hasRemaining())
/*     */     {
/* 634 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 639 */     this.writingEncryptedData = true;
/*     */ 
/*     */     
/* 642 */     WriteFuture writeFuture = null;
/*     */     
/*     */     try {
/* 645 */       IoBuffer writeBuffer = fetchOutNetBuffer();
/* 646 */       defaultWriteFuture = new DefaultWriteFuture(this.session);
/* 647 */       this.sslFilter.filterWrite(nextFilter, this.session, (WriteRequest)new DefaultWriteRequest(writeBuffer, (WriteFuture)defaultWriteFuture));
/*     */ 
/*     */       
/* 650 */       while (needToCompleteHandshake()) {
/*     */         try {
/* 652 */           handshake(nextFilter);
/* 653 */         } catch (SSLException ssle) {
/* 654 */           SSLException newSsle = new SSLHandshakeException("SSL handshake failed.");
/* 655 */           newSsle.initCause(ssle);
/* 656 */           throw newSsle;
/*     */         } 
/*     */         
/* 659 */         IoBuffer outNetBuffer = fetchOutNetBuffer();
/* 660 */         if (outNetBuffer != null && outNetBuffer.hasRemaining()) {
/* 661 */           defaultWriteFuture = new DefaultWriteFuture(this.session);
/* 662 */           this.sslFilter.filterWrite(nextFilter, this.session, (WriteRequest)new DefaultWriteRequest(outNetBuffer, (WriteFuture)defaultWriteFuture));
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 666 */       this.writingEncryptedData = false;
/*     */     } 
/*     */     
/* 669 */     return (WriteFuture)defaultWriteFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLEngineResult.Status unwrapHandshake(IoFilter.NextFilter nextFilter) throws SSLException {
/* 674 */     if (this.inNetBuffer != null) {
/* 675 */       this.inNetBuffer.flip();
/*     */     }
/*     */     
/* 678 */     if (this.inNetBuffer == null || !this.inNetBuffer.hasRemaining())
/*     */     {
/* 680 */       return SSLEngineResult.Status.BUFFER_UNDERFLOW;
/*     */     }
/*     */     
/* 683 */     SSLEngineResult res = unwrap();
/* 684 */     this.handshakeStatus = res.getHandshakeStatus();
/*     */     
/* 686 */     checkStatus(res);
/*     */ 
/*     */ 
/*     */     
/* 690 */     if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED && res.getStatus() == SSLEngineResult.Status.OK && this.inNetBuffer.hasRemaining()) {
/*     */ 
/*     */       
/* 693 */       res = unwrap();
/*     */ 
/*     */       
/* 696 */       if (this.inNetBuffer.hasRemaining()) {
/* 697 */         this.inNetBuffer.compact();
/*     */       } else {
/* 699 */         this.inNetBuffer.free();
/* 700 */         this.inNetBuffer = null;
/*     */       } 
/*     */       
/* 703 */       renegotiateIfNeeded(nextFilter, res);
/*     */     
/*     */     }
/* 706 */     else if (this.inNetBuffer.hasRemaining()) {
/* 707 */       this.inNetBuffer.compact();
/*     */     } else {
/* 709 */       this.inNetBuffer.free();
/* 710 */       this.inNetBuffer = null;
/*     */     } 
/*     */ 
/*     */     
/* 714 */     return res.getStatus();
/*     */   }
/*     */   
/*     */   private void renegotiateIfNeeded(IoFilter.NextFilter nextFilter, SSLEngineResult res) throws SSLException {
/* 718 */     if (res.getStatus() != SSLEngineResult.Status.CLOSED && res.getStatus() != SSLEngineResult.Status.BUFFER_UNDERFLOW && res.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
/*     */ 
/*     */ 
/*     */       
/* 722 */       this.handshakeComplete = false;
/* 723 */       this.handshakeStatus = res.getHandshakeStatus();
/* 724 */       handshake(nextFilter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLEngineResult unwrap() throws SSLException {
/*     */     SSLEngineResult res;
/* 734 */     if (this.appBuffer == null) {
/* 735 */       this.appBuffer = IoBuffer.allocate(this.inNetBuffer.remaining());
/*     */     } else {
/*     */       
/* 738 */       this.appBuffer.expand(this.inNetBuffer.remaining());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 743 */     SSLEngineResult.Status status = null;
/* 744 */     SSLEngineResult.HandshakeStatus handshakeStatus = null;
/*     */ 
/*     */     
/*     */     do {
/* 748 */       res = this.sslEngine.unwrap(this.inNetBuffer.buf(), this.appBuffer.buf());
/* 749 */       status = res.getStatus();
/*     */ 
/*     */       
/* 752 */       handshakeStatus = res.getHandshakeStatus();
/*     */       
/* 754 */       if (status != SSLEngineResult.Status.BUFFER_OVERFLOW) {
/*     */         continue;
/*     */       }
/* 757 */       this.appBuffer.capacity(this.appBuffer.capacity() << 1);
/* 758 */       this.appBuffer.limit(this.appBuffer.capacity());
/*     */     
/*     */     }
/* 761 */     while ((status == SSLEngineResult.Status.OK || status == SSLEngineResult.Status.BUFFER_OVERFLOW) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING || handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP));
/*     */ 
/*     */     
/* 764 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLEngineResult.HandshakeStatus doTasks() {
/*     */     Runnable runnable;
/* 776 */     while ((runnable = this.sslEngine.getDelegatedTask()) != null)
/*     */     {
/*     */       
/* 779 */       runnable.run();
/*     */     }
/* 781 */     return this.sslEngine.getHandshakeStatus();
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
/*     */   static IoBuffer copy(ByteBuffer src) {
/* 793 */     IoBuffer copy = IoBuffer.allocate(src.remaining());
/* 794 */     copy.put(src);
/* 795 */     copy.flip();
/* 796 */     return copy;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 800 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 802 */     sb.append("SSLStatus <");
/*     */     
/* 804 */     if (this.handshakeComplete) {
/* 805 */       sb.append("SSL established");
/*     */     } else {
/* 807 */       sb.append("Processing Handshake").append("; ");
/* 808 */       sb.append("Status : ").append(this.handshakeStatus).append("; ");
/*     */     } 
/*     */     
/* 811 */     sb.append(", ");
/* 812 */     sb.append("HandshakeComplete :").append(this.handshakeComplete).append(", ");
/* 813 */     sb.append(">");
/*     */     
/* 815 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void release() {
/* 822 */     if (this.inNetBuffer != null) {
/* 823 */       this.inNetBuffer.free();
/* 824 */       this.inNetBuffer = null;
/*     */     } 
/*     */     
/* 827 */     if (this.outNetBuffer != null) {
/* 828 */       this.outNetBuffer.free();
/* 829 */       this.outNetBuffer = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/ssl/SslHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */