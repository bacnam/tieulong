/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionBufferStatus;
/*     */ import org.apache.http.nio.reactor.SocketAccessor;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class IOSessionImpl
/*     */   implements IOSession, SocketAccessor
/*     */ {
/*     */   private final SelectionKey key;
/*     */   private final ByteChannel channel;
/*     */   private final Map<String, Object> attributes;
/*     */   private final InterestOpsCallback interestOpsCallback;
/*     */   private final SessionClosedCallback sessionClosedCallback;
/*     */   private volatile int status;
/*     */   private volatile int currentEventMask;
/*     */   private volatile SessionBufferStatus bufferStatus;
/*     */   private volatile int socketTimeout;
/*     */   private final long startedTime;
/*     */   private volatile long lastReadTime;
/*     */   private volatile long lastWriteTime;
/*     */   private volatile long lastAccessTime;
/*     */   
/*     */   public IOSessionImpl(SelectionKey key, InterestOpsCallback interestOpsCallback, SessionClosedCallback sessionClosedCallback) {
/*  86 */     Args.notNull(key, "Selection key");
/*  87 */     this.key = key;
/*  88 */     this.channel = (ByteChannel)this.key.channel();
/*  89 */     this.interestOpsCallback = interestOpsCallback;
/*  90 */     this.sessionClosedCallback = sessionClosedCallback;
/*  91 */     this.attributes = Collections.synchronizedMap(new HashMap<String, Object>());
/*  92 */     this.currentEventMask = key.interestOps();
/*  93 */     this.socketTimeout = 0;
/*  94 */     this.status = 0;
/*  95 */     long now = System.currentTimeMillis();
/*  96 */     this.startedTime = now;
/*  97 */     this.lastReadTime = now;
/*  98 */     this.lastWriteTime = now;
/*  99 */     this.lastAccessTime = now;
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
/*     */   public IOSessionImpl(SelectionKey key, SessionClosedCallback sessionClosedCallback) {
/* 111 */     this(key, null, sessionClosedCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteChannel channel() {
/* 116 */     return this.channel;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 121 */     if (this.channel instanceof SocketChannel) {
/* 122 */       return ((SocketChannel)this.channel).socket().getLocalSocketAddress();
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 130 */     if (this.channel instanceof SocketChannel) {
/* 131 */       return ((SocketChannel)this.channel).socket().getRemoteSocketAddress();
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEventMask() {
/* 139 */     return (this.interestOpsCallback != null) ? this.currentEventMask : this.key.interestOps();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setEventMask(int ops) {
/* 144 */     if (this.status == Integer.MAX_VALUE) {
/*     */       return;
/*     */     }
/* 147 */     if (this.interestOpsCallback != null) {
/*     */       
/* 149 */       this.currentEventMask = ops;
/*     */ 
/*     */       
/* 152 */       InterestOpEntry entry = new InterestOpEntry(this.key, this.currentEventMask);
/*     */ 
/*     */       
/* 155 */       this.interestOpsCallback.addInterestOps(entry);
/*     */     } else {
/* 157 */       this.key.interestOps(ops);
/*     */     } 
/* 159 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setEvent(int op) {
/* 164 */     if (this.status == Integer.MAX_VALUE) {
/*     */       return;
/*     */     }
/* 167 */     if (this.interestOpsCallback != null) {
/*     */       
/* 169 */       this.currentEventMask |= op;
/*     */ 
/*     */       
/* 172 */       InterestOpEntry entry = new InterestOpEntry(this.key, this.currentEventMask);
/*     */ 
/*     */       
/* 175 */       this.interestOpsCallback.addInterestOps(entry);
/*     */     } else {
/* 177 */       int ops = this.key.interestOps();
/* 178 */       this.key.interestOps(ops | op);
/*     */     } 
/* 180 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clearEvent(int op) {
/* 185 */     if (this.status == Integer.MAX_VALUE) {
/*     */       return;
/*     */     }
/* 188 */     if (this.interestOpsCallback != null) {
/*     */       
/* 190 */       this.currentEventMask &= op ^ 0xFFFFFFFF;
/*     */ 
/*     */       
/* 193 */       InterestOpEntry entry = new InterestOpEntry(this.key, this.currentEventMask);
/*     */ 
/*     */       
/* 196 */       this.interestOpsCallback.addInterestOps(entry);
/*     */     } else {
/* 198 */       int ops = this.key.interestOps();
/* 199 */       this.key.interestOps(ops & (op ^ 0xFFFFFFFF));
/*     */     } 
/* 201 */     this.key.selector().wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 206 */     return this.socketTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 211 */     this.socketTimeout = timeout;
/* 212 */     this.lastAccessTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 217 */     synchronized (this) {
/* 218 */       if (this.status == Integer.MAX_VALUE) {
/*     */         return;
/*     */       }
/* 221 */       this.status = Integer.MAX_VALUE;
/*     */     } 
/* 223 */     synchronized (this.key) {
/* 224 */       this.key.cancel();
/*     */       try {
/* 226 */         this.key.channel().close();
/* 227 */       } catch (IOException ex) {}
/*     */ 
/*     */ 
/*     */       
/* 231 */       if (this.sessionClosedCallback != null) {
/* 232 */         this.sessionClosedCallback.sessionClosed(this);
/*     */       }
/* 234 */       if (this.key.selector().isOpen()) {
/* 235 */         this.key.selector().wakeup();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatus() {
/* 242 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 247 */     return (this.status == Integer.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 254 */     close();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBufferedInput() {
/* 259 */     SessionBufferStatus buffStatus = this.bufferStatus;
/* 260 */     return (buffStatus != null && buffStatus.hasBufferedInput());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBufferedOutput() {
/* 265 */     SessionBufferStatus buffStatus = this.bufferStatus;
/* 266 */     return (buffStatus != null && buffStatus.hasBufferedOutput());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBufferStatus(SessionBufferStatus bufferStatus) {
/* 271 */     this.bufferStatus = bufferStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String name) {
/* 276 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String name) {
/* 281 */     return this.attributes.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object obj) {
/* 286 */     this.attributes.put(name, obj);
/*     */   }
/*     */   
/*     */   public long getStartedTime() {
/* 290 */     return this.startedTime;
/*     */   }
/*     */   
/*     */   public long getLastReadTime() {
/* 294 */     return this.lastReadTime;
/*     */   }
/*     */   
/*     */   public long getLastWriteTime() {
/* 298 */     return this.lastWriteTime;
/*     */   }
/*     */   
/*     */   public long getLastAccessTime() {
/* 302 */     return this.lastAccessTime;
/*     */   }
/*     */   
/*     */   void resetLastRead() {
/* 306 */     long now = System.currentTimeMillis();
/* 307 */     this.lastReadTime = now;
/* 308 */     this.lastAccessTime = now;
/*     */   }
/*     */   
/*     */   void resetLastWrite() {
/* 312 */     long now = System.currentTimeMillis();
/* 313 */     this.lastWriteTime = now;
/* 314 */     this.lastAccessTime = now;
/*     */   }
/*     */   
/*     */   private static void formatOps(StringBuilder buffer, int ops) {
/* 318 */     if ((ops & 0x1) > 0) {
/* 319 */       buffer.append('r');
/*     */     }
/* 321 */     if ((ops & 0x4) > 0) {
/* 322 */       buffer.append('w');
/*     */     }
/* 324 */     if ((ops & 0x10) > 0) {
/* 325 */       buffer.append('a');
/*     */     }
/* 327 */     if ((ops & 0x8) > 0) {
/* 328 */       buffer.append('c');
/*     */     }
/*     */   }
/*     */   
/*     */   private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 333 */     if (socketAddress instanceof InetSocketAddress) {
/* 334 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 335 */       buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 340 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 346 */     StringBuilder buffer = new StringBuilder();
/* 347 */     synchronized (this.key) {
/* 348 */       SocketAddress remoteAddress = getRemoteAddress();
/* 349 */       SocketAddress localAddress = getLocalAddress();
/* 350 */       if (remoteAddress != null && localAddress != null) {
/* 351 */         formatAddress(buffer, localAddress);
/* 352 */         buffer.append("<->");
/* 353 */         formatAddress(buffer, remoteAddress);
/*     */       } 
/* 355 */       buffer.append('[');
/* 356 */       switch (this.status) {
/*     */         case 0:
/* 358 */           buffer.append("ACTIVE");
/*     */           break;
/*     */         case 1:
/* 361 */           buffer.append("CLOSING");
/*     */           break;
/*     */         case 2147483647:
/* 364 */           buffer.append("CLOSED");
/*     */           break;
/*     */       } 
/* 367 */       buffer.append("][");
/* 368 */       if (this.key.isValid()) {
/* 369 */         formatOps(buffer, (this.interestOpsCallback != null) ? this.currentEventMask : this.key.interestOps());
/*     */         
/* 371 */         buffer.append(':');
/* 372 */         formatOps(buffer, this.key.readyOps());
/*     */       } 
/*     */     } 
/* 375 */     buffer.append(']');
/* 376 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 381 */     if (this.channel instanceof SocketChannel) {
/* 382 */       return ((SocketChannel)this.channel).socket();
/*     */     }
/* 384 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/IOSessionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */