/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.nio.reactor.SessionRequest;
/*     */ import org.apache.http.nio.reactor.SessionRequestCallback;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @ThreadSafe
/*     */ public class DefaultConnectingIOReactor
/*     */   extends AbstractMultiworkerIOReactor
/*     */   implements ConnectingIOReactor
/*     */ {
/*     */   private final Queue<SessionRequestImpl> requestQueue;
/*     */   private long lastTimeoutCheck;
/*     */   
/*     */   public DefaultConnectingIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
/*  81 */     super(config, threadFactory);
/*  82 */     this.requestQueue = new ConcurrentLinkedQueue<SessionRequestImpl>();
/*  83 */     this.lastTimeoutCheck = System.currentTimeMillis();
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
/*     */   public DefaultConnectingIOReactor(IOReactorConfig config) throws IOReactorException {
/*  96 */     this(config, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectingIOReactor() throws IOReactorException {
/* 107 */     this((IOReactorConfig)null, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultConnectingIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
/* 118 */     this(convert(workerCount, params), threadFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultConnectingIOReactor(int workerCount, HttpParams params) throws IOReactorException {
/* 128 */     this(convert(workerCount, params), (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRequests() throws IOReactorException {
/*     */     SessionRequestImpl request;
/* 134 */     while ((request = this.requestQueue.poll()) != null) {
/* 135 */       request.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processEvents(int readyCount) throws IOReactorException {
/* 141 */     processSessionRequests();
/*     */     
/* 143 */     if (readyCount > 0) {
/* 144 */       Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
/* 145 */       for (SelectionKey key : selectedKeys)
/*     */       {
/* 147 */         processEvent(key);
/*     */       }
/*     */       
/* 150 */       selectedKeys.clear();
/*     */     } 
/*     */     
/* 153 */     long currentTime = System.currentTimeMillis();
/* 154 */     if (currentTime - this.lastTimeoutCheck >= this.selectTimeout) {
/* 155 */       this.lastTimeoutCheck = currentTime;
/* 156 */       Set<SelectionKey> keys = this.selector.keys();
/* 157 */       processTimeouts(keys);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processEvent(SelectionKey key) {
/*     */     try {
/* 164 */       if (key.isConnectable())
/*     */       {
/* 166 */         SocketChannel channel = (SocketChannel)key.channel();
/*     */         
/* 168 */         SessionRequestHandle requestHandle = (SessionRequestHandle)key.attachment();
/* 169 */         SessionRequestImpl sessionRequest = requestHandle.getSessionRequest();
/*     */ 
/*     */         
/*     */         try {
/* 173 */           channel.finishConnect();
/* 174 */         } catch (IOException ex) {
/* 175 */           sessionRequest.failed(ex);
/*     */         } 
/* 177 */         key.cancel();
/* 178 */         key.attach(null);
/* 179 */         if (!sessionRequest.isCompleted()) {
/* 180 */           addChannel(new ChannelEntry(channel, sessionRequest));
/*     */         } else {
/*     */           try {
/* 183 */             channel.close();
/* 184 */           } catch (IOException ignore) {}
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 189 */     } catch (CancelledKeyException ex) {
/* 190 */       SessionRequestHandle requestHandle = (SessionRequestHandle)key.attachment();
/* 191 */       key.attach(null);
/* 192 */       if (requestHandle != null) {
/* 193 */         SessionRequestImpl sessionRequest = requestHandle.getSessionRequest();
/* 194 */         if (sessionRequest != null) {
/* 195 */           sessionRequest.cancel();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processTimeouts(Set<SelectionKey> keys) {
/* 202 */     long now = System.currentTimeMillis();
/* 203 */     for (SelectionKey key : keys) {
/* 204 */       Object attachment = key.attachment();
/*     */       
/* 206 */       if (attachment instanceof SessionRequestHandle) {
/* 207 */         SessionRequestHandle handle = (SessionRequestHandle)key.attachment();
/* 208 */         SessionRequestImpl sessionRequest = handle.getSessionRequest();
/* 209 */         int timeout = sessionRequest.getConnectTimeout();
/* 210 */         if (timeout > 0 && 
/* 211 */           handle.getRequestTime() + timeout < now) {
/* 212 */           sessionRequest.timeout();
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
/*     */   public SessionRequest connect(SocketAddress remoteAddress, SocketAddress localAddress, Object attachment, SessionRequestCallback callback) {
/* 226 */     Asserts.check((this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0), "I/O reactor has been shut down");
/*     */     
/* 228 */     SessionRequestImpl sessionRequest = new SessionRequestImpl(remoteAddress, localAddress, attachment, callback);
/*     */     
/* 230 */     sessionRequest.setConnectTimeout(this.config.getConnectTimeout());
/*     */     
/* 232 */     this.requestQueue.add(sessionRequest);
/* 233 */     this.selector.wakeup();
/*     */     
/* 235 */     return sessionRequest;
/*     */   }
/*     */   
/*     */   private void validateAddress(SocketAddress address) throws UnknownHostException {
/* 239 */     if (address == null) {
/*     */       return;
/*     */     }
/* 242 */     if (address instanceof InetSocketAddress) {
/* 243 */       InetSocketAddress endpoint = (InetSocketAddress)address;
/* 244 */       if (endpoint.isUnresolved()) {
/* 245 */         throw new UnknownHostException(endpoint.getHostName());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processSessionRequests() throws IOReactorException {
/*     */     SessionRequestImpl request;
/* 252 */     while ((request = this.requestQueue.poll()) != null) {
/* 253 */       SocketChannel socketChannel; if (request.isCompleted()) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 258 */         socketChannel = SocketChannel.open();
/* 259 */       } catch (IOException ex) {
/* 260 */         throw new IOReactorException("Failure opening socket", ex);
/*     */       } 
/*     */       try {
/* 263 */         validateAddress(request.getLocalAddress());
/* 264 */         validateAddress(request.getRemoteAddress());
/*     */         
/* 266 */         socketChannel.configureBlocking(false);
/* 267 */         prepareSocket(socketChannel.socket());
/*     */         
/* 269 */         if (request.getLocalAddress() != null) {
/* 270 */           Socket sock = socketChannel.socket();
/* 271 */           sock.setReuseAddress(this.config.isSoReuseAddress());
/* 272 */           sock.bind(request.getLocalAddress());
/*     */         } 
/* 274 */         boolean connected = socketChannel.connect(request.getRemoteAddress());
/* 275 */         if (connected) {
/* 276 */           ChannelEntry entry = new ChannelEntry(socketChannel, request);
/* 277 */           addChannel(entry);
/*     */           continue;
/*     */         } 
/* 280 */       } catch (IOException ex) {
/* 281 */         closeChannel(socketChannel);
/* 282 */         request.failed(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 286 */       SessionRequestHandle requestHandle = new SessionRequestHandle(request);
/*     */       try {
/* 288 */         SelectionKey key = socketChannel.register(this.selector, 8, requestHandle);
/*     */         
/* 290 */         request.setKey(key);
/* 291 */       } catch (IOException ex) {
/* 292 */         closeChannel(socketChannel);
/* 293 */         throw new IOReactorException("Failure registering channel with the selector", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/DefaultConnectingIOReactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */