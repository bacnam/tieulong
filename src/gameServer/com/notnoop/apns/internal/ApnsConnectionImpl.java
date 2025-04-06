/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import com.notnoop.apns.ApnsDelegate;
/*     */ import com.notnoop.apns.ApnsNotification;
/*     */ import com.notnoop.apns.DeliveryError;
/*     */ import com.notnoop.apns.ReconnectPolicy;
/*     */ import com.notnoop.apns.SimpleApnsNotification;
/*     */ import com.notnoop.exceptions.ApnsDeliveryErrorException;
/*     */ import com.notnoop.exceptions.NetworkIOException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
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
/*     */ public class ApnsConnectionImpl
/*     */   implements ApnsConnection
/*     */ {
/*  58 */   private static final Logger logger = LoggerFactory.getLogger(ApnsConnectionImpl.class); private final SocketFactory factory; private final String host; private final int port; private final Proxy proxy;
/*     */   private final ReconnectPolicy reconnectPolicy;
/*     */   private final ApnsDelegate delegate;
/*     */   private int cacheLength;
/*     */   private final boolean errorDetection;
/*     */   private final boolean autoAdjustCacheLength;
/*     */   private final ConcurrentLinkedQueue<ApnsNotification> cachedNotifications;
/*     */   private final ConcurrentLinkedQueue<ApnsNotification> notificationsBuffer;
/*     */   private Socket socket;
/*     */   int DELAY_IN_MS;
/*     */   private static final int RETRIES = 3;
/*     */   
/*     */   public ApnsConnectionImpl(SocketFactory factory, String host, int port) {
/*  71 */     this(factory, host, port, new ReconnectPolicies.Never(), ApnsDelegate.EMPTY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsConnectionImpl(SocketFactory factory, String host, int port, ReconnectPolicy reconnectPolicy, ApnsDelegate delegate) {
/*  77 */     this(factory, host, port, null, reconnectPolicy, delegate, false, 100, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ApnsConnectionImpl(SocketFactory factory, String host, int port, Proxy proxy, ReconnectPolicy reconnectPolicy, ApnsDelegate delegate, boolean errorDetection, int cacheLength, boolean autoAdjustCacheLength) {
/* 226 */     this.DELAY_IN_MS = 1000; this.factory = factory; this.host = host; this.port = port; this.reconnectPolicy = reconnectPolicy; this.delegate = (delegate == null) ? ApnsDelegate.EMPTY : delegate; this.proxy = proxy; this.errorDetection = errorDetection; this.cacheLength = cacheLength;
/*     */     this.autoAdjustCacheLength = autoAdjustCacheLength;
/*     */     this.cachedNotifications = new ConcurrentLinkedQueue<ApnsNotification>();
/*     */     this.notificationsBuffer = new ConcurrentLinkedQueue<ApnsNotification>();
/* 230 */   } public synchronized void close() { Utilities.close(this.socket); } public synchronized void sendMessage(ApnsNotification m) throws NetworkIOException { sendMessage(m, false); }
/*     */   private void monitorSocket(final Socket socket) { class MonitoringThread extends Thread {
/*     */       public void run() { try { InputStream in = socket.getInputStream(); int expectedSize = 6; byte[] bytes = new byte[6]; while (in.read(bytes) == 6) { int command = bytes[0] & 0xFF; if (command != 8)
/*     */               throw new IOException("Unexpected command byte " + command);  int statusCode = bytes[1] & 0xFF; DeliveryError e = DeliveryError.ofCode(statusCode); int id = Utilities.parseBytes(bytes[2], bytes[3], bytes[4], bytes[5]); Queue<ApnsNotification> tempCache = new LinkedList<ApnsNotification>(); ApnsNotification notification = null; boolean foundNotification = false; while (!ApnsConnectionImpl.this.cachedNotifications.isEmpty()) { notification = ApnsConnectionImpl.this.cachedNotifications.poll(); if (notification.getIdentifier() == id) { foundNotification = true; break; }  tempCache.add(notification); }  if (foundNotification) { ApnsConnectionImpl.this.delegate.messageSendFailed(notification, (Throwable)new ApnsDeliveryErrorException(e)); } else { ApnsConnectionImpl.this.cachedNotifications.addAll(tempCache); int i = tempCache.size(); ApnsConnectionImpl.logger.warn("Received error for message that wasn't in the cache..."); if (ApnsConnectionImpl.this.autoAdjustCacheLength) { ApnsConnectionImpl.this.cacheLength = ApnsConnectionImpl.this.cacheLength + i / 2; ApnsConnectionImpl.this.delegate.cacheLengthExceeded(ApnsConnectionImpl.this.cacheLength); }  ApnsConnectionImpl.this.delegate.messageSendFailed(null, (Throwable)new ApnsDeliveryErrorException(e)); }  int resendSize = 0; while (!ApnsConnectionImpl.this.cachedNotifications.isEmpty()) { resendSize++; ApnsConnectionImpl.this.notificationsBuffer.add(ApnsConnectionImpl.this.cachedNotifications.poll()); }  ApnsConnectionImpl.this.delegate.notificationsResent(resendSize); ApnsConnectionImpl.this.delegate.connectionClosed(e, id); ApnsConnectionImpl.this.drainBuffer(); }  } catch (Exception e) { ApnsConnectionImpl.logger.info("Exception while waiting for error code", e); ApnsConnectionImpl.this.delegate.connectionClosed(DeliveryError.UNKNOWN, -1); } finally { ApnsConnectionImpl.this.close(); }
/*     */          } }; Thread t = new MonitoringThread(); t.setDaemon(true); t.start(); }
/* 235 */   public synchronized void sendMessage(ApnsNotification m, boolean fromBuffer) throws NetworkIOException { int attempts = 0;
/*     */     while (true)
/*     */     { 
/* 238 */       try { attempts++;
/* 239 */         Socket socket = socket();
/* 240 */         socket.getOutputStream().write(m.marshall());
/* 241 */         socket.getOutputStream().flush();
/* 242 */         cacheNotification(m);
/*     */         
/* 244 */         this.delegate.messageSent(m, fromBuffer);
/*     */         
/* 246 */         logger.debug("Message \"{}\" sent", m);
/*     */         
/* 248 */         attempts = 0;
/* 249 */         drainBuffer();
/*     */         break; }
/* 251 */       catch (Exception e)
/* 252 */       { Utilities.close(this.socket);
/* 253 */         this.socket = null;
/* 254 */         if (attempts >= 3) {
/* 255 */           logger.error("Couldn't send message after 3 retries." + m, e);
/* 256 */           this.delegate.messageSendFailed(m, e);
/* 257 */           Utilities.wrapAndThrowAsRuntimeException(e);
/*     */         } 
/*     */ 
/*     */         
/* 261 */         if (attempts != 1)
/*     */         
/*     */         { 
/* 264 */           logger.info("Failed to send message " + m + "... trying again after delay", e);
/* 265 */           Utilities.sleep(this.DELAY_IN_MS); }  }  }  }
/*     */   private synchronized Socket socket() throws NetworkIOException { if (this.reconnectPolicy.shouldReconnect()) { Utilities.close(this.socket); this.socket = null; }  if (this.socket == null || this.socket.isClosed())
/*     */       try { if (this.proxy == null) { this.socket = this.factory.createSocket(this.host, this.port); } else if (this.proxy.type() == Proxy.Type.HTTP) { TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder(); this.socket = tunnelBuilder.build((SSLSocketFactory)this.factory, this.proxy, this.host, this.port); } else { boolean success = false; Socket proxySocket = null; try { proxySocket = new Socket(this.proxy); proxySocket.connect(new InetSocketAddress(this.host, this.port)); this.socket = ((SSLSocketFactory)this.factory).createSocket(proxySocket, this.host, this.port, false); success = true; } finally { if (!success)
/*     */               Utilities.close(proxySocket);  }  }
/*     */          if (this.errorDetection)
/*     */           monitorSocket(this.socket);  this.reconnectPolicy.reconnected(); logger.debug("Made a new connection to APNS"); }
/*     */       catch (IOException e) { logger.error("Couldn't connect to APNS server", e); throw new NetworkIOException(e); }
/* 272 */         return this.socket; } private void drainBuffer() { if (!this.notificationsBuffer.isEmpty()) {
/* 273 */       sendMessage(this.notificationsBuffer.poll(), true);
/*     */     } }
/*     */ 
/*     */   
/*     */   private void cacheNotification(ApnsNotification notification) {
/* 278 */     this.cachedNotifications.add(notification);
/* 279 */     while (this.cachedNotifications.size() > this.cacheLength) {
/* 280 */       this.cachedNotifications.poll();
/* 281 */       logger.debug("Removing notification from cache " + notification);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ApnsConnectionImpl copy() {
/* 286 */     return new ApnsConnectionImpl(this.factory, this.host, this.port, this.proxy, this.reconnectPolicy.copy(), this.delegate, this.errorDetection, this.cacheLength, this.autoAdjustCacheLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public void testConnection() throws NetworkIOException {
/* 291 */     ApnsConnectionImpl testConnection = null;
/*     */     try {
/* 293 */       testConnection = new ApnsConnectionImpl(this.factory, this.host, this.port, this.reconnectPolicy.copy(), ApnsDelegate.EMPTY);
/* 294 */       testConnection.sendMessage((ApnsNotification)new SimpleApnsNotification(new byte[] { 0 }, new byte[] { 0 }));
/*     */     } finally {
/* 296 */       if (testConnection != null) {
/* 297 */         testConnection.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCacheLength(int cacheLength) {
/* 303 */     this.cacheLength = cacheLength;
/*     */   }
/*     */   
/*     */   public int getCacheLength() {
/* 307 */     return this.cacheLength;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/ApnsConnectionImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */