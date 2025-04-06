/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionRequest;
/*     */ import org.apache.http.nio.reactor.SessionRequestCallback;
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
/*     */ @ThreadSafe
/*     */ public class SessionRequestImpl
/*     */   implements SessionRequest
/*     */ {
/*     */   private volatile boolean completed;
/*     */   private volatile SelectionKey key;
/*     */   private final SocketAddress remoteAddress;
/*     */   private final SocketAddress localAddress;
/*     */   private final Object attachment;
/*     */   private final SessionRequestCallback callback;
/*     */   private volatile int connectTimeout;
/*  58 */   private volatile IOSession session = null;
/*  59 */   private volatile IOException exception = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionRequestImpl(SocketAddress remoteAddress, SocketAddress localAddress, Object attachment, SessionRequestCallback callback) {
/*  67 */     Args.notNull(remoteAddress, "Remote address");
/*  68 */     this.remoteAddress = remoteAddress;
/*  69 */     this.localAddress = localAddress;
/*  70 */     this.attachment = attachment;
/*  71 */     this.callback = callback;
/*  72 */     this.connectTimeout = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/*  77 */     return this.remoteAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/*  82 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttachment() {
/*  87 */     return this.attachment;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  92 */     return this.completed;
/*     */   }
/*     */   
/*     */   protected void setKey(SelectionKey key) {
/*  96 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public void waitFor() throws InterruptedException {
/* 101 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 104 */     synchronized (this) {
/* 105 */       while (!this.completed) {
/* 106 */         wait();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession getSession() {
/* 113 */     synchronized (this) {
/* 114 */       return this.session;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IOException getException() {
/* 120 */     synchronized (this) {
/* 121 */       return this.exception;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void completed(IOSession session) {
/* 126 */     Args.notNull(session, "Session");
/* 127 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 130 */     this.completed = true;
/* 131 */     synchronized (this) {
/* 132 */       this.session = session;
/* 133 */       if (this.callback != null) {
/* 134 */         this.callback.completed(this);
/*     */       }
/* 136 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void failed(IOException exception) {
/* 141 */     if (exception == null) {
/*     */       return;
/*     */     }
/* 144 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 147 */     this.completed = true;
/* 148 */     SelectionKey key = this.key;
/* 149 */     if (key != null) {
/* 150 */       key.cancel();
/* 151 */       Channel channel = key.channel();
/*     */       try {
/* 153 */         channel.close();
/* 154 */       } catch (IOException ignore) {}
/*     */     } 
/* 156 */     synchronized (this) {
/* 157 */       this.exception = exception;
/* 158 */       if (this.callback != null) {
/* 159 */         this.callback.failed(this);
/*     */       }
/* 161 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void timeout() {
/* 166 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 169 */     this.completed = true;
/* 170 */     SelectionKey key = this.key;
/* 171 */     if (key != null) {
/* 172 */       key.cancel();
/* 173 */       Channel channel = key.channel();
/* 174 */       if (channel.isOpen()) {
/*     */         try {
/* 176 */           channel.close();
/* 177 */         } catch (IOException ignore) {}
/*     */       }
/*     */     } 
/* 180 */     synchronized (this) {
/* 181 */       if (this.callback != null) {
/* 182 */         this.callback.timeout(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConnectTimeout() {
/* 189 */     return this.connectTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int timeout) {
/* 194 */     if (this.connectTimeout != timeout) {
/* 195 */       this.connectTimeout = timeout;
/* 196 */       SelectionKey key = this.key;
/* 197 */       if (key != null) {
/* 198 */         key.selector().wakeup();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 205 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 208 */     this.completed = true;
/* 209 */     SelectionKey key = this.key;
/* 210 */     if (key != null) {
/* 211 */       key.cancel();
/* 212 */       Channel channel = key.channel();
/* 213 */       if (channel.isOpen()) {
/*     */         try {
/* 215 */           channel.close();
/* 216 */         } catch (IOException ignore) {}
/*     */       }
/*     */     } 
/* 219 */     synchronized (this) {
/* 220 */       if (this.callback != null) {
/* 221 */         this.callback.cancelled(this);
/*     */       }
/* 223 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/SessionRequestImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */