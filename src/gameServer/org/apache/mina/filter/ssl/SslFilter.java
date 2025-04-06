/*     */ package org.apache.mina.filter.ssl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.future.DefaultWriteFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestWrapper;
/*     */ import org.apache.mina.core.write.WriteToClosedSessionException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SslFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  91 */   private static final Logger LOGGER = LoggerFactory.getLogger(SslFilter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final AttributeKey SSL_SESSION = new AttributeKey(SslFilter.class, "session");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final AttributeKey DISABLE_ENCRYPTION_ONCE = new AttributeKey(SslFilter.class, "disableOnce");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final AttributeKey USE_NOTIFICATION = new AttributeKey(SslFilter.class, "useNotification");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public static final AttributeKey PEER_ADDRESS = new AttributeKey(SslFilter.class, "peerAddress");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public static final SslFilterMessage SESSION_SECURED = new SslFilterMessage("SESSION_SECURED");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final SslFilterMessage SESSION_UNSECURED = new SslFilterMessage("SESSION_UNSECURED");
/*     */   
/* 149 */   private static final AttributeKey NEXT_FILTER = new AttributeKey(SslFilter.class, "nextFilter");
/*     */   
/* 151 */   private static final AttributeKey SSL_HANDLER = new AttributeKey(SslFilter.class, "handler");
/*     */ 
/*     */   
/*     */   final SSLContext sslContext;
/*     */ 
/*     */   
/*     */   private final boolean autoStart;
/*     */ 
/*     */   
/*     */   private static final boolean START_HANDSHAKE = true;
/*     */ 
/*     */   
/*     */   private boolean client;
/*     */ 
/*     */   
/*     */   private boolean needClientAuth;
/*     */ 
/*     */   
/*     */   private boolean wantClientAuth;
/*     */   
/*     */   private String[] enabledCipherSuites;
/*     */   
/*     */   private String[] enabledProtocols;
/*     */ 
/*     */   
/*     */   public SslFilter(SSLContext sslContext) {
/* 177 */     this(sslContext, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslFilter(SSLContext sslContext, boolean autoStart) {
/* 186 */     if (sslContext == null) {
/* 187 */       throw new IllegalArgumentException("sslContext");
/*     */     }
/*     */     
/* 190 */     this.sslContext = sslContext;
/* 191 */     this.autoStart = autoStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSession getSslSession(IoSession session) {
/* 200 */     return (SSLSession)session.getAttribute(SSL_SESSION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startSsl(IoSession session) throws SSLException {
/*     */     boolean started;
/* 212 */     SslHandler sslHandler = getSslSessionHandler(session);
/*     */ 
/*     */     
/*     */     try {
/* 216 */       synchronized (sslHandler) {
/* 217 */         if (sslHandler.isOutboundDone()) {
/* 218 */           IoFilter.NextFilter nextFilter = (IoFilter.NextFilter)session.getAttribute(NEXT_FILTER);
/* 219 */           sslHandler.destroy();
/* 220 */           sslHandler.init();
/* 221 */           sslHandler.handshake(nextFilter);
/* 222 */           started = true;
/*     */         } else {
/* 224 */           started = false;
/*     */         } 
/*     */       } 
/*     */       
/* 228 */       sslHandler.flushScheduledEvents();
/* 229 */     } catch (SSLException se) {
/* 230 */       sslHandler.release();
/* 231 */       throw se;
/*     */     } 
/*     */     
/* 234 */     return started;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getSessionInfo(IoSession session) {
/* 243 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 245 */     if (session.getService() instanceof org.apache.mina.core.service.IoAcceptor) {
/* 246 */       sb.append("Session Server");
/*     */     } else {
/*     */       
/* 249 */       sb.append("Session Client");
/*     */     } 
/*     */     
/* 252 */     sb.append('[').append(session.getId()).append(']');
/*     */     
/* 254 */     SslHandler sslHandler = (SslHandler)session.getAttribute(SSL_HANDLER);
/*     */     
/* 256 */     if (sslHandler == null) {
/* 257 */       sb.append("(no sslEngine)");
/* 258 */     } else if (isSslStarted(session)) {
/* 259 */       if (sslHandler.isHandshakeComplete()) {
/* 260 */         sb.append("(SSL)");
/*     */       } else {
/* 262 */         sb.append("(ssl...)");
/*     */       } 
/*     */     } 
/*     */     
/* 266 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSslStarted(IoSession session) {
/* 276 */     SslHandler sslHandler = (SslHandler)session.getAttribute(SSL_HANDLER);
/*     */     
/* 278 */     if (sslHandler == null) {
/* 279 */       return false;
/*     */     }
/*     */     
/* 282 */     synchronized (sslHandler) {
/* 283 */       return !sslHandler.isOutboundDone();
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
/*     */   public WriteFuture stopSsl(IoSession session) throws SSLException {
/*     */     WriteFuture future;
/* 296 */     SslHandler sslHandler = getSslSessionHandler(session);
/* 297 */     IoFilter.NextFilter nextFilter = (IoFilter.NextFilter)session.getAttribute(NEXT_FILTER);
/*     */ 
/*     */     
/*     */     try {
/* 301 */       synchronized (sslHandler) {
/* 302 */         future = initiateClosure(nextFilter, session);
/*     */       } 
/*     */       
/* 305 */       sslHandler.flushScheduledEvents();
/* 306 */     } catch (SSLException se) {
/* 307 */       sslHandler.release();
/* 308 */       throw se;
/*     */     } 
/*     */     
/* 311 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseClientMode() {
/* 319 */     return this.client;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseClientMode(boolean clientMode) {
/* 326 */     this.client = clientMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNeedClientAuth() {
/* 334 */     return this.needClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNeedClientAuth(boolean needClientAuth) {
/* 342 */     this.needClientAuth = needClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWantClientAuth() {
/* 350 */     return this.wantClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWantClientAuth(boolean wantClientAuth) {
/* 358 */     this.wantClientAuth = wantClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledCipherSuites() {
/* 368 */     return this.enabledCipherSuites;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] cipherSuites) {
/* 378 */     this.enabledCipherSuites = cipherSuites;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledProtocols() {
/* 388 */     return this.enabledProtocols;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] protocols) {
/* 398 */     this.enabledProtocols = protocols;
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
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws SSLException {
/* 413 */     if (parent.contains(SslFilter.class)) {
/* 414 */       String msg = "Only one SSL filter is permitted in a chain.";
/* 415 */       LOGGER.error(msg);
/* 416 */       throw new IllegalStateException(msg);
/*     */     } 
/*     */     
/* 419 */     LOGGER.debug("Adding the SSL Filter {} to the chain", name);
/*     */     
/* 421 */     IoSession session = parent.getSession();
/* 422 */     session.setAttribute(NEXT_FILTER, nextFilter);
/*     */ 
/*     */     
/* 425 */     SslHandler sslHandler = new SslHandler(this, session);
/* 426 */     sslHandler.init();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     String[] ciphers = this.sslContext.getServerSocketFactory().getSupportedCipherSuites();
/* 432 */     setEnabledCipherSuites(ciphers);
/* 433 */     session.setAttribute(SSL_HANDLER, sslHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPostAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws SSLException {
/* 438 */     if (this.autoStart == true) {
/* 439 */       initiateHandshake(nextFilter, parent.getSession());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPreRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws SSLException {
/* 445 */     IoSession session = parent.getSession();
/* 446 */     stopSsl(session);
/* 447 */     session.removeAttribute(NEXT_FILTER);
/* 448 */     session.removeAttribute(SSL_HANDLER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws SSLException {
/* 454 */     SslHandler sslHandler = getSslSessionHandler(session);
/*     */     
/*     */     try {
/* 457 */       synchronized (sslHandler) {
/*     */         
/* 459 */         sslHandler.destroy();
/*     */       } 
/*     */       
/* 462 */       sslHandler.flushScheduledEvents();
/*     */     } finally {
/*     */       
/* 465 */       nextFilter.sessionClosed(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws SSLException {
/* 471 */     if (LOGGER.isDebugEnabled()) {
/* 472 */       LOGGER.debug("{}: Message received : {}", getSessionInfo(session), message);
/*     */     }
/*     */     
/* 475 */     SslHandler sslHandler = getSslSessionHandler(session);
/*     */     
/* 477 */     synchronized (sslHandler) {
/* 478 */       if (!isSslStarted(session) && sslHandler.isInboundDone()) {
/*     */ 
/*     */ 
/*     */         
/* 482 */         sslHandler.scheduleMessageReceived(nextFilter, message);
/*     */       } else {
/* 484 */         IoBuffer buf = (IoBuffer)message;
/*     */ 
/*     */         
/*     */         try {
/* 488 */           sslHandler.messageReceived(nextFilter, buf.buf());
/*     */ 
/*     */           
/* 491 */           handleSslData(nextFilter, sslHandler);
/*     */           
/* 493 */           if (sslHandler.isInboundDone()) {
/* 494 */             if (sslHandler.isOutboundDone()) {
/* 495 */               sslHandler.destroy();
/*     */             } else {
/* 497 */               initiateClosure(nextFilter, session);
/*     */             } 
/*     */             
/* 500 */             if (buf.hasRemaining())
/*     */             {
/* 502 */               sslHandler.scheduleMessageReceived(nextFilter, buf);
/*     */             }
/*     */           } 
/* 505 */         } catch (SSLException ssle) {
/* 506 */           if (!sslHandler.isHandshakeComplete()) {
/* 507 */             SSLException newSsle = new SSLHandshakeException("SSL handshake failed.");
/* 508 */             newSsle.initCause(ssle);
/* 509 */             ssle = newSsle;
/*     */           } else {
/*     */             
/* 512 */             sslHandler.release();
/*     */           } 
/*     */           
/* 515 */           throw ssle;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 520 */     sslHandler.flushScheduledEvents();
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
/* 525 */     if (writeRequest instanceof EncryptedWriteRequest) {
/* 526 */       EncryptedWriteRequest wrappedRequest = (EncryptedWriteRequest)writeRequest;
/* 527 */       nextFilter.messageSent(session, wrappedRequest.getParentRequest());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/*     */     WriteToClosedSessionException writeToClosedSessionException;
/* 536 */     if (cause instanceof WriteToClosedSessionException) {
/*     */ 
/*     */       
/* 539 */       WriteToClosedSessionException e = (WriteToClosedSessionException)cause;
/* 540 */       List<WriteRequest> failedRequests = e.getRequests();
/* 541 */       boolean containsCloseNotify = false;
/*     */       
/* 543 */       for (WriteRequest r : failedRequests) {
/* 544 */         if (isCloseNotify(r.getMessage())) {
/* 545 */           containsCloseNotify = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 550 */       if (containsCloseNotify) {
/* 551 */         if (failedRequests.size() == 1) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 556 */         List<WriteRequest> newFailedRequests = new ArrayList<WriteRequest>(failedRequests.size() - 1);
/*     */         
/* 558 */         for (WriteRequest r : failedRequests) {
/* 559 */           if (!isCloseNotify(r.getMessage())) {
/* 560 */             newFailedRequests.add(r);
/*     */           }
/*     */         } 
/*     */         
/* 564 */         if (newFailedRequests.isEmpty()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 569 */         writeToClosedSessionException = new WriteToClosedSessionException(newFailedRequests, cause.getMessage(), cause.getCause());
/*     */       } 
/*     */     } 
/*     */     
/* 573 */     nextFilter.exceptionCaught(session, (Throwable)writeToClosedSessionException);
/*     */   }
/*     */   
/*     */   private boolean isCloseNotify(Object message) {
/* 577 */     if (!(message instanceof IoBuffer)) {
/* 578 */       return false;
/*     */     }
/*     */     
/* 581 */     IoBuffer buf = (IoBuffer)message;
/* 582 */     int offset = buf.position();
/*     */     
/* 584 */     return (buf.get(offset + 0) == 21 && buf.get(offset + 1) == 3 && (buf.get(offset + 2) == 0 || buf.get(offset + 2) == 1 || buf.get(offset + 2) == 2 || buf.get(offset + 2) == 3) && buf.get(offset + 3) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws SSLException {
/* 595 */     if (LOGGER.isDebugEnabled()) {
/* 596 */       LOGGER.debug("{}: Writing Message : {}", getSessionInfo(session), writeRequest);
/*     */     }
/*     */     
/* 599 */     boolean needsFlush = true;
/* 600 */     SslHandler sslHandler = getSslSessionHandler(session);
/*     */     
/*     */     try {
/* 603 */       synchronized (sslHandler) {
/* 604 */         if (!isSslStarted(session)) {
/* 605 */           sslHandler.scheduleFilterWrite(nextFilter, writeRequest);
/*     */         
/*     */         }
/* 608 */         else if (session.containsAttribute(DISABLE_ENCRYPTION_ONCE)) {
/*     */           
/* 610 */           session.removeAttribute(DISABLE_ENCRYPTION_ONCE);
/* 611 */           sslHandler.scheduleFilterWrite(nextFilter, writeRequest);
/*     */         } else {
/*     */           
/* 614 */           IoBuffer buf = (IoBuffer)writeRequest.getMessage();
/*     */           
/* 616 */           if (sslHandler.isWritingEncryptedData()) {
/*     */             
/* 618 */             sslHandler.scheduleFilterWrite(nextFilter, writeRequest);
/* 619 */           } else if (sslHandler.isHandshakeComplete()) {
/*     */             
/* 621 */             int pos = buf.position();
/* 622 */             sslHandler.encrypt(buf.buf());
/* 623 */             buf.position(pos);
/* 624 */             IoBuffer encryptedBuffer = sslHandler.fetchOutNetBuffer();
/* 625 */             sslHandler.scheduleFilterWrite(nextFilter, (WriteRequest)new EncryptedWriteRequest(writeRequest, encryptedBuffer));
/*     */           } else {
/*     */             
/* 628 */             if (session.isConnected())
/*     */             {
/* 630 */               sslHandler.schedulePreHandshakeWriteRequest(nextFilter, writeRequest);
/*     */             }
/*     */             
/* 633 */             needsFlush = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 638 */       if (needsFlush) {
/* 639 */         sslHandler.flushScheduledEvents();
/*     */       }
/* 641 */     } catch (SSLException se) {
/* 642 */       sslHandler.release();
/* 643 */       throw se;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void filterClose(final IoFilter.NextFilter nextFilter, final IoSession session) throws SSLException {
/* 649 */     SslHandler sslHandler = (SslHandler)session.getAttribute(SSL_HANDLER);
/*     */     
/* 651 */     if (sslHandler == null) {
/*     */ 
/*     */       
/* 654 */       nextFilter.filterClose(session);
/*     */       
/*     */       return;
/*     */     } 
/* 658 */     WriteFuture future = null;
/*     */     
/*     */     try {
/* 661 */       synchronized (sslHandler) {
/* 662 */         if (isSslStarted(session)) {
/* 663 */           future = initiateClosure(nextFilter, session);
/* 664 */           future.addListener(new IoFutureListener<IoFuture>() {
/*     */                 public void operationComplete(IoFuture future) {
/* 666 */                   nextFilter.filterClose(session);
/*     */                 }
/*     */               });
/*     */         } 
/*     */       } 
/*     */       
/* 672 */       sslHandler.flushScheduledEvents();
/* 673 */     } catch (SSLException se) {
/* 674 */       sslHandler.release();
/* 675 */       throw se;
/*     */     } finally {
/* 677 */       if (future == null) {
/* 678 */         nextFilter.filterClose(session);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initiateHandshake(IoFilter.NextFilter nextFilter, IoSession session) throws SSLException {
/* 684 */     LOGGER.debug("{} : Starting the first handshake", getSessionInfo(session));
/* 685 */     SslHandler sslHandler = getSslSessionHandler(session);
/*     */     
/*     */     try {
/* 688 */       synchronized (sslHandler) {
/* 689 */         sslHandler.handshake(nextFilter);
/*     */       } 
/*     */       
/* 692 */       sslHandler.flushScheduledEvents();
/* 693 */     } catch (SSLException se) {
/* 694 */       sslHandler.release();
/* 695 */       throw se;
/*     */     } 
/*     */   }
/*     */   
/*     */   private WriteFuture initiateClosure(IoFilter.NextFilter nextFilter, IoSession session) throws SSLException {
/* 700 */     SslHandler sslHandler = getSslSessionHandler(session);
/* 701 */     WriteFuture future = null;
/*     */ 
/*     */     
/*     */     try {
/* 705 */       if (!sslHandler.closeOutbound()) {
/* 706 */         return DefaultWriteFuture.newNotWrittenFuture(session, new IllegalStateException("SSL session is shut down already."));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 711 */       future = sslHandler.writeNetBuffer(nextFilter);
/*     */       
/* 713 */       if (future == null) {
/* 714 */         future = DefaultWriteFuture.newWrittenFuture(session);
/*     */       }
/*     */       
/* 717 */       if (sslHandler.isInboundDone()) {
/* 718 */         sslHandler.destroy();
/*     */       }
/*     */       
/* 721 */       if (session.containsAttribute(USE_NOTIFICATION)) {
/* 722 */         sslHandler.scheduleMessageReceived(nextFilter, SESSION_UNSECURED);
/*     */       }
/* 724 */     } catch (SSLException se) {
/* 725 */       sslHandler.release();
/* 726 */       throw se;
/*     */     } 
/*     */     
/* 729 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleSslData(IoFilter.NextFilter nextFilter, SslHandler sslHandler) throws SSLException {
/* 734 */     if (LOGGER.isDebugEnabled()) {
/* 735 */       LOGGER.debug("{}: Processing the SSL Data ", getSessionInfo(sslHandler.getSession()));
/*     */     }
/*     */ 
/*     */     
/* 739 */     if (sslHandler.isHandshakeComplete()) {
/* 740 */       sslHandler.flushPreHandshakeEvents();
/*     */     }
/*     */ 
/*     */     
/* 744 */     sslHandler.writeNetBuffer(nextFilter);
/*     */ 
/*     */     
/* 747 */     handleAppDataRead(nextFilter, sslHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleAppDataRead(IoFilter.NextFilter nextFilter, SslHandler sslHandler) {
/* 752 */     IoBuffer readBuffer = sslHandler.fetchAppBuffer();
/*     */     
/* 754 */     if (readBuffer.hasRemaining()) {
/* 755 */       sslHandler.scheduleMessageReceived(nextFilter, readBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private SslHandler getSslSessionHandler(IoSession session) {
/* 760 */     SslHandler sslHandler = (SslHandler)session.getAttribute(SSL_HANDLER);
/*     */     
/* 762 */     if (sslHandler == null) {
/* 763 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 766 */     if (sslHandler.getSslFilter() != this) {
/* 767 */       throw new IllegalArgumentException("Not managed by this filter.");
/*     */     }
/*     */     
/* 770 */     return sslHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SslFilterMessage
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private SslFilterMessage(String name) {
/* 783 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 788 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EncryptedWriteRequest extends WriteRequestWrapper {
/*     */     private final IoBuffer encryptedMessage;
/*     */     
/*     */     private EncryptedWriteRequest(WriteRequest writeRequest, IoBuffer encryptedMessage) {
/* 796 */       super(writeRequest);
/* 797 */       this.encryptedMessage = encryptedMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getMessage() {
/* 802 */       return this.encryptedMessage;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/ssl/SslFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */