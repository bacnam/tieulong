/*     */ package org.apache.http.nio.reactor.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionBufferStatus;
/*     */ import org.apache.http.nio.reactor.SocketAccessor;
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
/*     */ @ThreadSafe
/*     */ public class SSLIOSession
/*     */   implements IOSession, SessionBufferStatus, SocketAccessor
/*     */ {
/*     */   public static final String SESSION_KEY = "http.session.ssl";
/*  80 */   private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
/*     */ 
/*     */   
/*     */   private final IOSession session;
/*     */ 
/*     */   
/*     */   private final SSLEngine sslEngine;
/*     */ 
/*     */   
/*     */   private final SSLBuffer inEncrypted;
/*     */ 
/*     */   
/*     */   private final SSLBuffer outEncrypted;
/*     */ 
/*     */   
/*     */   private final SSLBuffer inPlain;
/*     */   
/*     */   private final SSLBuffer outPlain;
/*     */   
/*     */   private final InternalByteChannel channel;
/*     */   
/*     */   private final SSLSetupHandler handler;
/*     */   
/*     */   private int appEventMask;
/*     */   
/*     */   private SessionBufferStatus appBufferStatus;
/*     */   
/*     */   private boolean endOfStream;
/*     */   
/*     */   private volatile SSLMode sslMode;
/*     */   
/*     */   private volatile int status;
/*     */   
/*     */   private volatile boolean initialized;
/*     */ 
/*     */   
/*     */   public SSLIOSession(IOSession session, SSLMode sslMode, HttpHost host, SSLContext sslContext, SSLSetupHandler handler) {
/* 117 */     this(session, sslMode, host, sslContext, handler, new PermanentSSLBufferManagementStrategy());
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
/*     */   public SSLIOSession(IOSession session, SSLMode sslMode, HttpHost host, SSLContext sslContext, SSLSetupHandler handler, SSLBufferManagementStrategy bufferManagementStrategy) {
/* 138 */     Args.notNull(session, "IO session");
/* 139 */     Args.notNull(sslContext, "SSL context");
/* 140 */     Args.notNull(bufferManagementStrategy, "Buffer management strategy");
/* 141 */     this.session = session;
/* 142 */     this.sslMode = sslMode;
/* 143 */     this.appEventMask = session.getEventMask();
/* 144 */     this.channel = new InternalByteChannel();
/* 145 */     this.handler = handler;
/*     */ 
/*     */     
/* 148 */     this.session.setBufferStatus(this);
/*     */     
/* 150 */     if (this.sslMode == SSLMode.CLIENT && host != null) {
/* 151 */       this.sslEngine = sslContext.createSSLEngine(host.getHostName(), host.getPort());
/*     */     } else {
/* 153 */       this.sslEngine = sslContext.createSSLEngine();
/*     */     } 
/*     */ 
/*     */     
/* 157 */     int netBuffersize = this.sslEngine.getSession().getPacketBufferSize();
/* 158 */     this.inEncrypted = bufferManagementStrategy.constructBuffer(netBuffersize);
/* 159 */     this.outEncrypted = bufferManagementStrategy.constructBuffer(netBuffersize);
/*     */ 
/*     */     
/* 162 */     int appBuffersize = this.sslEngine.getSession().getApplicationBufferSize();
/* 163 */     this.inPlain = bufferManagementStrategy.constructBuffer(appBuffersize);
/* 164 */     this.outPlain = bufferManagementStrategy.constructBuffer(appBuffersize);
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
/*     */   public SSLIOSession(IOSession session, SSLMode sslMode, SSLContext sslContext, SSLSetupHandler handler) {
/* 180 */     this(session, sslMode, null, sslContext, handler);
/*     */   }
/*     */   
/*     */   protected SSLSetupHandler getSSLSetupHandler() {
/* 184 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 192 */     return this.initialized;
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
/*     */   @Deprecated
/*     */   public synchronized void initialize(SSLMode sslMode) throws SSLException {
/* 205 */     this.sslMode = sslMode;
/* 206 */     initialize();
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
/*     */   public synchronized void initialize() throws SSLException {
/* 218 */     Asserts.check(!this.initialized, "SSL I/O session already initialized");
/* 219 */     if (this.status >= 1) {
/*     */       return;
/*     */     }
/* 222 */     switch (this.sslMode) {
/*     */       case NEED_WRAP:
/* 224 */         this.sslEngine.setUseClientMode(true);
/*     */         break;
/*     */       case NEED_UNWRAP:
/* 227 */         this.sslEngine.setUseClientMode(false);
/*     */         break;
/*     */     } 
/* 230 */     if (this.handler != null) {
/* 231 */       this.handler.initalize(this.sslEngine);
/*     */     }
/* 233 */     this.initialized = true;
/* 234 */     this.sslEngine.beginHandshake();
/*     */     
/* 236 */     this.inEncrypted.release();
/* 237 */     this.outEncrypted.release();
/* 238 */     this.inPlain.release();
/* 239 */     this.outPlain.release();
/*     */     
/* 241 */     doHandshake();
/*     */   }
/*     */   
/*     */   public synchronized SSLSession getSSLSession() {
/* 245 */     return this.sslEngine.getSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SSLException convert(RuntimeException ex) {
/* 254 */     Throwable cause = ex.getCause();
/* 255 */     if (cause == null) {
/* 256 */       cause = ex;
/*     */     }
/* 258 */     return new SSLException(cause);
/*     */   }
/*     */   
/*     */   private SSLEngineResult doWrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*     */     try {
/* 263 */       return this.sslEngine.wrap(src, dst);
/* 264 */     } catch (RuntimeException ex) {
/* 265 */       throw convert(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLEngineResult doUnwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/*     */     try {
/* 271 */       return this.sslEngine.unwrap(src, dst);
/* 272 */     } catch (RuntimeException ex) {
/* 273 */       throw convert(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doRunTask() throws SSLException {
/*     */     try {
/* 279 */       Runnable r = this.sslEngine.getDelegatedTask();
/* 280 */       if (r != null) {
/* 281 */         r.run();
/*     */       }
/* 283 */     } catch (RuntimeException ex) {
/* 284 */       throw convert(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doHandshake() throws SSLException {
/* 289 */     boolean handshaking = true;
/*     */     
/* 291 */     SSLEngineResult result = null;
/* 292 */     while (handshaking) {
/* 293 */       ByteBuffer outPlainBuf; ByteBuffer outEncryptedBuf; ByteBuffer inEncryptedBuf; ByteBuffer inPlainBuf; switch (this.sslEngine.getHandshakeStatus()) {
/*     */ 
/*     */ 
/*     */         
/*     */         case NEED_WRAP:
/* 298 */           outPlainBuf = this.outPlain.acquire();
/* 299 */           outEncryptedBuf = this.outEncrypted.acquire();
/*     */ 
/*     */           
/* 302 */           outPlainBuf.flip();
/* 303 */           result = doWrap(outPlainBuf, outEncryptedBuf);
/* 304 */           outPlainBuf.compact();
/*     */ 
/*     */           
/* 307 */           if (outPlainBuf.position() == 0) {
/* 308 */             this.outPlain.release();
/* 309 */             outPlainBuf = null;
/*     */           } 
/*     */ 
/*     */           
/* 313 */           if (result.getStatus() != SSLEngineResult.Status.OK) {
/* 314 */             handshaking = false;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case NEED_UNWRAP:
/* 321 */           inEncryptedBuf = this.inEncrypted.acquire();
/* 322 */           inPlainBuf = this.inPlain.acquire();
/*     */ 
/*     */           
/* 325 */           inEncryptedBuf.flip();
/* 326 */           result = doUnwrap(inEncryptedBuf, inPlainBuf);
/* 327 */           inEncryptedBuf.compact();
/*     */ 
/*     */           
/*     */           try {
/* 331 */             if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/* 332 */               throw new SSLException("Input buffer is full");
/*     */             }
/*     */           } finally {
/*     */             
/* 336 */             if (inEncryptedBuf.position() == 0) {
/* 337 */               this.inEncrypted.release();
/* 338 */               inEncryptedBuf = null;
/*     */             } 
/*     */           } 
/*     */           
/* 342 */           if (this.status >= 1) {
/* 343 */             this.inPlain.release();
/* 344 */             inPlainBuf = null;
/*     */           } 
/* 346 */           if (result.getStatus() != SSLEngineResult.Status.OK) {
/* 347 */             handshaking = false;
/*     */           }
/*     */         
/*     */         case NEED_TASK:
/* 351 */           doRunTask();
/*     */         
/*     */         case NOT_HANDSHAKING:
/* 354 */           handshaking = false;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 364 */     if (result != null && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED && 
/* 365 */       this.handler != null) {
/* 366 */       this.handler.verify(this.session, this.sslEngine.getSession());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateEventMask() {
/* 373 */     if (this.status == 1 && this.sslEngine.isOutboundDone() && (this.endOfStream || this.sslEngine.isInboundDone()))
/*     */     {
/* 375 */       this.status = Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 378 */     if (this.status == 0 && this.endOfStream && this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
/*     */     {
/* 380 */       this.status = Integer.MAX_VALUE;
/*     */     }
/* 382 */     if (this.status == Integer.MAX_VALUE) {
/* 383 */       this.session.close();
/*     */       
/*     */       return;
/*     */     } 
/* 387 */     int oldMask = this.session.getEventMask();
/* 388 */     int newMask = oldMask;
/* 389 */     switch (this.sslEngine.getHandshakeStatus()) {
/*     */       case NEED_WRAP:
/* 391 */         newMask = 5;
/*     */         break;
/*     */       case NEED_UNWRAP:
/* 394 */         newMask = 1;
/*     */         break;
/*     */       case NOT_HANDSHAKING:
/* 397 */         newMask = this.appEventMask;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 406 */     if (this.outEncrypted.hasData()) {
/* 407 */       newMask |= 0x4;
/*     */     }
/*     */ 
/*     */     
/* 411 */     if (oldMask != newMask) {
/* 412 */       this.session.setEventMask(newMask);
/*     */     }
/*     */   }
/*     */   
/*     */   private int sendEncryptedData() throws IOException {
/* 417 */     if (!this.outEncrypted.hasData())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 422 */       return this.session.channel().write(EMPTY_BUFFER);
/*     */     }
/*     */ 
/*     */     
/* 426 */     ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/*     */ 
/*     */     
/* 429 */     outEncryptedBuf.flip();
/* 430 */     int bytesWritten = this.session.channel().write(outEncryptedBuf);
/* 431 */     outEncryptedBuf.compact();
/*     */ 
/*     */     
/* 434 */     if (outEncryptedBuf.position() == 0) {
/* 435 */       this.outEncrypted.release();
/*     */     }
/* 437 */     return bytesWritten;
/*     */   }
/*     */   
/*     */   private int receiveEncryptedData() throws IOException {
/* 441 */     if (this.endOfStream) {
/* 442 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 446 */     ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();
/*     */ 
/*     */     
/* 449 */     int ret = this.session.channel().read(inEncryptedBuf);
/*     */ 
/*     */     
/* 452 */     if (inEncryptedBuf.position() == 0) {
/* 453 */       this.inEncrypted.release();
/*     */     }
/* 455 */     return ret;
/*     */   }
/*     */   
/*     */   private boolean decryptData() throws SSLException {
/* 459 */     boolean decrypted = false;
/* 460 */     while (this.inEncrypted.hasData()) {
/*     */       
/* 462 */       ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();
/* 463 */       ByteBuffer inPlainBuf = this.inPlain.acquire();
/*     */ 
/*     */       
/* 466 */       inEncryptedBuf.flip();
/* 467 */       SSLEngineResult result = doUnwrap(inEncryptedBuf, inPlainBuf);
/* 468 */       inEncryptedBuf.compact();
/*     */ 
/*     */       
/* 471 */       try { if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
/* 472 */           throw new SSLException("Input buffer is full");
/*     */         }
/* 474 */         if (result.getStatus() == SSLEngineResult.Status.OK)
/* 475 */         { decrypted = true;
/*     */ 
/*     */ 
/*     */           
/*     */            }
/*     */         
/*     */         else
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */           
/* 487 */           if (this.inEncrypted.acquire().position() == 0)
/* 488 */             this.inEncrypted.release();  break; }  if (result.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();  break; }  if (this.endOfStream) { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();  break; }  } finally { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();
/*     */          }
/*     */     
/*     */     } 
/* 492 */     return decrypted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isAppInputReady() throws IOException {
/*     */     do {
/* 503 */       int bytesRead = receiveEncryptedData();
/* 504 */       if (bytesRead == -1) {
/* 505 */         this.endOfStream = true;
/*     */       }
/* 507 */       doHandshake();
/* 508 */       SSLEngineResult.HandshakeStatus status = this.sslEngine.getHandshakeStatus();
/* 509 */       if (status != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && status != SSLEngineResult.HandshakeStatus.FINISHED)
/* 510 */         continue;  decryptData();
/*     */     }
/* 512 */     while (this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK);
/*     */     
/* 514 */     return ((this.appEventMask & 0x1) > 0 && (this.inPlain.hasData() || (this.appBufferStatus != null && this.appBufferStatus.hasBufferedInput()) || (this.endOfStream && this.status == 0)));
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
/*     */   public synchronized boolean isAppOutputReady() throws IOException {
/* 527 */     return ((this.appEventMask & 0x4) > 0 && this.status == 0 && this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void inboundTransport() throws IOException {
/* 538 */     updateEventMask();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void outboundTransport() throws IOException {
/* 547 */     sendEncryptedData();
/* 548 */     doHandshake();
/* 549 */     updateEventMask();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isInboundDone() {
/* 556 */     return this.sslEngine.isInboundDone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isOutboundDone() {
/* 563 */     return this.sslEngine.isOutboundDone();
/*     */   }
/*     */   
/*     */   private synchronized int writePlain(ByteBuffer src) throws SSLException {
/* 567 */     Args.notNull(src, "Byte buffer");
/* 568 */     if (this.status != 0) {
/* 569 */       return -1;
/*     */     }
/* 571 */     if (this.outPlain.hasData()) {
/*     */       
/* 573 */       ByteBuffer outPlainBuf = this.outPlain.acquire();
/* 574 */       ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/*     */ 
/*     */       
/* 577 */       outPlainBuf.flip();
/* 578 */       doWrap(outPlainBuf, outEncryptedBuf);
/* 579 */       outPlainBuf.compact();
/*     */ 
/*     */       
/* 582 */       if (outPlainBuf.position() == 0) {
/* 583 */         this.outPlain.release();
/* 584 */         outPlainBuf = null;
/*     */       } 
/*     */     } 
/* 587 */     if (!this.outPlain.hasData()) {
/* 588 */       ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
/* 589 */       SSLEngineResult result = doWrap(src, outEncryptedBuf);
/* 590 */       if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
/* 591 */         this.status = Integer.MAX_VALUE;
/*     */       }
/* 593 */       return result.bytesConsumed();
/*     */     } 
/* 595 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized int readPlain(ByteBuffer dst) {
/* 600 */     Args.notNull(dst, "Byte buffer");
/* 601 */     if (this.inPlain.hasData()) {
/*     */       
/* 603 */       ByteBuffer inPlainBuf = this.inPlain.acquire();
/*     */ 
/*     */       
/* 606 */       inPlainBuf.flip();
/* 607 */       int n = Math.min(inPlainBuf.remaining(), dst.remaining());
/* 608 */       for (int i = 0; i < n; i++) {
/* 609 */         dst.put(inPlainBuf.get());
/*     */       }
/* 611 */       inPlainBuf.compact();
/*     */ 
/*     */       
/* 614 */       if (inPlainBuf.position() == 0) {
/* 615 */         this.inPlain.release();
/* 616 */         inPlainBuf = null;
/*     */       } 
/* 618 */       return n;
/*     */     } 
/* 620 */     if (this.endOfStream) {
/* 621 */       return -1;
/*     */     }
/* 623 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 630 */     if (this.status >= 1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 636 */     this.status = 1;
/* 637 */     this.sslEngine.closeOutbound();
/* 638 */     updateEventMask();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 643 */     if (this.status == Integer.MAX_VALUE) {
/*     */       return;
/*     */     }
/*     */     
/* 647 */     this.inEncrypted.release();
/* 648 */     this.outEncrypted.release();
/* 649 */     this.inPlain.release();
/* 650 */     this.outPlain.release();
/*     */     
/* 652 */     this.status = Integer.MAX_VALUE;
/* 653 */     this.session.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatus() {
/* 658 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 663 */     return (this.status >= 1 || this.session.isClosed());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/* 668 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 673 */     return this.session.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 678 */     return this.session.getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getEventMask() {
/* 683 */     return this.appEventMask;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setEventMask(int ops) {
/* 688 */     this.appEventMask = ops;
/* 689 */     updateEventMask();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setEvent(int op) {
/* 694 */     this.appEventMask |= op;
/* 695 */     updateEventMask();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clearEvent(int op) {
/* 700 */     this.appEventMask &= op ^ 0xFFFFFFFF;
/* 701 */     updateEventMask();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 706 */     return this.session.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 711 */     this.session.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean hasBufferedInput() {
/* 716 */     return ((this.appBufferStatus != null && this.appBufferStatus.hasBufferedInput()) || this.inEncrypted.hasData() || this.inPlain.hasData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean hasBufferedOutput() {
/* 723 */     return ((this.appBufferStatus != null && this.appBufferStatus.hasBufferedOutput()) || this.outEncrypted.hasData() || this.outPlain.hasData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setBufferStatus(SessionBufferStatus status) {
/* 730 */     this.appBufferStatus = status;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name) {
/* 735 */     return this.session.getAttribute(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String name) {
/* 740 */     return this.session.removeAttribute(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object obj) {
/* 745 */     this.session.setAttribute(name, obj);
/*     */   }
/*     */   
/*     */   private static void formatOps(StringBuilder buffer, int ops) {
/* 749 */     if ((ops & 0x1) > 0) {
/* 750 */       buffer.append('r');
/*     */     }
/* 752 */     if ((ops & 0x4) > 0) {
/* 753 */       buffer.append('w');
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 759 */     StringBuilder buffer = new StringBuilder();
/* 760 */     buffer.append(this.session);
/* 761 */     buffer.append("[");
/* 762 */     switch (this.status) {
/*     */       case 0:
/* 764 */         buffer.append("ACTIVE");
/*     */         break;
/*     */       case 1:
/* 767 */         buffer.append("CLOSING");
/*     */         break;
/*     */       case 2147483647:
/* 770 */         buffer.append("CLOSED");
/*     */         break;
/*     */     } 
/* 773 */     buffer.append("][");
/* 774 */     formatOps(buffer, this.appEventMask);
/* 775 */     buffer.append("][");
/* 776 */     buffer.append(this.sslEngine.getHandshakeStatus());
/* 777 */     if (this.sslEngine.isInboundDone()) {
/* 778 */       buffer.append("][inbound done][");
/*     */     }
/* 780 */     if (this.sslEngine.isOutboundDone()) {
/* 781 */       buffer.append("][outbound done][");
/*     */     }
/* 783 */     if (this.endOfStream) {
/* 784 */       buffer.append("][EOF][");
/*     */     }
/* 786 */     buffer.append("][");
/* 787 */     buffer.append(!this.inEncrypted.hasData() ? 0 : this.inEncrypted.acquire().position());
/* 788 */     buffer.append("][");
/* 789 */     buffer.append(!this.inPlain.hasData() ? 0 : this.inPlain.acquire().position());
/* 790 */     buffer.append("][");
/* 791 */     buffer.append(!this.outEncrypted.hasData() ? 0 : this.outEncrypted.acquire().position());
/* 792 */     buffer.append("][");
/* 793 */     buffer.append(!this.outPlain.hasData() ? 0 : this.outPlain.acquire().position());
/* 794 */     buffer.append("]");
/* 795 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 800 */     if (this.session instanceof SocketAccessor) {
/* 801 */       return ((SocketAccessor)this.session).getSocket();
/*     */     }
/* 803 */     return null;
/*     */   }
/*     */   
/*     */   private class InternalByteChannel
/*     */     implements ByteChannel {
/*     */     private InternalByteChannel() {}
/*     */     
/*     */     public int write(ByteBuffer src) throws IOException {
/* 811 */       return SSLIOSession.this.writePlain(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(ByteBuffer dst) throws IOException {
/* 816 */       return SSLIOSession.this.readPlain(dst);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 821 */       SSLIOSession.this.close();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 826 */       return !SSLIOSession.this.isClosed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/reactor/ssl/SSLIOSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */