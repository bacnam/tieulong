/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.nio.reactor.ListenerEndpoint;
/*     */ import org.apache.http.nio.reactor.ListeningIOReactor;
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
/*     */ public class DefaultListeningIOReactor
/*     */   extends AbstractMultiworkerIOReactor
/*     */   implements ListeningIOReactor
/*     */ {
/*     */   private final Queue<ListenerEndpointImpl> requestQueue;
/*     */   private final Set<ListenerEndpointImpl> endpoints;
/*     */   private final Set<SocketAddress> pausedEndpoints;
/*     */   private volatile boolean paused;
/*     */   
/*     */   public DefaultListeningIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
/*  84 */     super(config, threadFactory);
/*  85 */     this.requestQueue = new ConcurrentLinkedQueue<ListenerEndpointImpl>();
/*  86 */     this.endpoints = Collections.synchronizedSet(new HashSet<ListenerEndpointImpl>());
/*  87 */     this.pausedEndpoints = new HashSet<SocketAddress>();
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
/*     */   public DefaultListeningIOReactor(IOReactorConfig config) throws IOReactorException {
/* 100 */     this(config, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultListeningIOReactor() throws IOReactorException {
/* 111 */     this((IOReactorConfig)null, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultListeningIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
/* 122 */     this(convert(workerCount, params), threadFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultListeningIOReactor(int workerCount, HttpParams params) throws IOReactorException {
/* 132 */     this(convert(workerCount, params), (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRequests() throws IOReactorException {
/*     */     ListenerEndpointImpl request;
/* 138 */     while ((request = this.requestQueue.poll()) != null) {
/* 139 */       request.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processEvents(int readyCount) throws IOReactorException {
/* 145 */     if (!this.paused) {
/* 146 */       processSessionRequests();
/*     */     }
/*     */     
/* 149 */     if (readyCount > 0) {
/* 150 */       Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
/* 151 */       for (SelectionKey key : selectedKeys)
/*     */       {
/* 153 */         processEvent(key);
/*     */       }
/*     */       
/* 156 */       selectedKeys.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processEvent(SelectionKey key) throws IOReactorException {
/*     */     try {
/* 164 */       if (key.isAcceptable()) {
/*     */         
/* 166 */         ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
/*     */         while (true) {
/* 168 */           SocketChannel socketChannel = null;
/*     */           try {
/* 170 */             socketChannel = serverChannel.accept();
/* 171 */           } catch (IOException ex) {
/* 172 */             if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex))
/*     */             {
/* 174 */               throw new IOReactorException("Failure accepting connection", ex);
/*     */             }
/*     */           } 
/*     */           
/* 178 */           if (socketChannel == null) {
/*     */             break;
/*     */           }
/*     */           try {
/* 182 */             prepareSocket(socketChannel.socket());
/* 183 */           } catch (IOException ex) {
/* 184 */             if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex))
/*     */             {
/* 186 */               throw new IOReactorException("Failure initalizing socket", ex);
/*     */             }
/*     */           } 
/*     */           
/* 190 */           ChannelEntry entry = new ChannelEntry(socketChannel);
/* 191 */           addChannel(entry);
/*     */         }
/*     */       
/*     */       } 
/* 195 */     } catch (CancelledKeyException ex) {
/* 196 */       ListenerEndpoint endpoint = (ListenerEndpoint)key.attachment();
/* 197 */       this.endpoints.remove(endpoint);
/* 198 */       key.attach(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ListenerEndpointImpl createEndpoint(SocketAddress address) {
/* 203 */     return new ListenerEndpointImpl(address, new ListenerEndpointClosedCallback()
/*     */         {
/*     */ 
/*     */           
/*     */           public void endpointClosed(ListenerEndpoint endpoint)
/*     */           {
/* 209 */             DefaultListeningIOReactor.this.endpoints.remove(endpoint);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenerEndpoint listen(SocketAddress address) {
/* 217 */     Asserts.check((this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0), "I/O reactor has been shut down");
/*     */     
/* 219 */     ListenerEndpointImpl request = createEndpoint(address);
/* 220 */     this.requestQueue.add(request);
/* 221 */     this.selector.wakeup();
/* 222 */     return request;
/*     */   }
/*     */   
/*     */   private void processSessionRequests() throws IOReactorException {
/*     */     ListenerEndpointImpl request;
/* 227 */     while ((request = this.requestQueue.poll()) != null) {
/* 228 */       ServerSocketChannel serverChannel; SocketAddress address = request.getAddress();
/*     */       
/*     */       try {
/* 231 */         serverChannel = ServerSocketChannel.open();
/* 232 */       } catch (IOException ex) {
/* 233 */         throw new IOReactorException("Failure opening server socket", ex);
/*     */       } 
/*     */       try {
/* 236 */         ServerSocket socket = serverChannel.socket();
/* 237 */         socket.setReuseAddress(this.config.isSoReuseAddress());
/* 238 */         if (this.config.getSoTimeout() > 0) {
/* 239 */           socket.setSoTimeout(this.config.getSoTimeout());
/*     */         }
/* 241 */         if (this.config.getRcvBufSize() > 0) {
/* 242 */           socket.setReceiveBufferSize(this.config.getRcvBufSize());
/*     */         }
/* 244 */         serverChannel.configureBlocking(false);
/* 245 */         socket.bind(address, this.config.getBacklogSize());
/* 246 */       } catch (IOException ex) {
/* 247 */         closeChannel(serverChannel);
/* 248 */         request.failed(ex);
/* 249 */         if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex)) {
/* 250 */           throw new IOReactorException("Failure binding socket to address " + address, ex);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 257 */         SelectionKey key = serverChannel.register(this.selector, 16);
/* 258 */         key.attach(request);
/* 259 */         request.setKey(key);
/* 260 */       } catch (IOException ex) {
/* 261 */         closeChannel(serverChannel);
/* 262 */         throw new IOReactorException("Failure registering channel with the selector", ex);
/*     */       } 
/*     */ 
/*     */       
/* 266 */       this.endpoints.add(request);
/* 267 */       request.completed(serverChannel.socket().getLocalSocketAddress());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ListenerEndpoint> getEndpoints() {
/* 273 */     Set<ListenerEndpoint> set = new HashSet<ListenerEndpoint>();
/* 274 */     synchronized (this.endpoints) {
/* 275 */       Iterator<ListenerEndpointImpl> it = this.endpoints.iterator();
/* 276 */       while (it.hasNext()) {
/* 277 */         ListenerEndpoint endpoint = it.next();
/* 278 */         if (!endpoint.isClosed()) {
/* 279 */           set.add(endpoint); continue;
/*     */         } 
/* 281 */         it.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause() throws IOException {
/* 290 */     if (this.paused) {
/*     */       return;
/*     */     }
/* 293 */     this.paused = true;
/* 294 */     synchronized (this.endpoints) {
/* 295 */       for (ListenerEndpointImpl endpoint : this.endpoints) {
/* 296 */         if (!endpoint.isClosed()) {
/* 297 */           endpoint.close();
/* 298 */           this.pausedEndpoints.add(endpoint.getAddress());
/*     */         } 
/*     */       } 
/* 301 */       this.endpoints.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume() throws IOException {
/* 307 */     if (!this.paused) {
/*     */       return;
/*     */     }
/* 310 */     this.paused = false;
/* 311 */     for (SocketAddress address : this.pausedEndpoints) {
/* 312 */       ListenerEndpointImpl request = createEndpoint(address);
/* 313 */       this.requestQueue.add(request);
/*     */     } 
/* 315 */     this.pausedEndpoints.clear();
/* 316 */     this.selector.wakeup();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/DefaultListeningIOReactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */