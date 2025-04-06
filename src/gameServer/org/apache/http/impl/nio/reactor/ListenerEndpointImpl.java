/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.ListenerEndpoint;
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
/*     */ @ThreadSafe
/*     */ public class ListenerEndpointImpl
/*     */   implements ListenerEndpoint
/*     */ {
/*     */   private volatile boolean completed;
/*     */   private volatile boolean closed;
/*     */   private volatile SelectionKey key;
/*     */   private volatile SocketAddress address;
/*     */   private volatile IOException exception;
/*     */   private final ListenerEndpointClosedCallback callback;
/*     */   
/*     */   public ListenerEndpointImpl(SocketAddress address, ListenerEndpointClosedCallback callback) {
/*  59 */     Args.notNull(address, "Address");
/*  60 */     this.address = address;
/*  61 */     this.callback = callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress getAddress() {
/*  66 */     return this.address;
/*     */   }
/*     */   
/*     */   public boolean isCompleted() {
/*  70 */     return this.completed;
/*     */   }
/*     */ 
/*     */   
/*     */   public IOException getException() {
/*  75 */     return this.exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public void waitFor() throws InterruptedException {
/*  80 */     if (this.completed) {
/*     */       return;
/*     */     }
/*  83 */     synchronized (this) {
/*  84 */       while (!this.completed) {
/*  85 */         wait();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void completed(SocketAddress address) {
/*  91 */     Args.notNull(address, "Address");
/*  92 */     if (this.completed) {
/*     */       return;
/*     */     }
/*  95 */     this.completed = true;
/*  96 */     synchronized (this) {
/*  97 */       this.address = address;
/*  98 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void failed(IOException exception) {
/* 103 */     if (exception == null) {
/*     */       return;
/*     */     }
/* 106 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 109 */     this.completed = true;
/* 110 */     synchronized (this) {
/* 111 */       this.exception = exception;
/* 112 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 117 */     if (this.completed) {
/*     */       return;
/*     */     }
/* 120 */     this.completed = true;
/* 121 */     this.closed = true;
/* 122 */     synchronized (this) {
/* 123 */       notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setKey(SelectionKey key) {
/* 128 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 133 */     return (this.closed || (this.key != null && !this.key.isValid()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 138 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 141 */     this.completed = true;
/* 142 */     this.closed = true;
/* 143 */     if (this.key != null) {
/* 144 */       this.key.cancel();
/* 145 */       Channel channel = this.key.channel();
/*     */       try {
/* 147 */         channel.close();
/* 148 */       } catch (IOException ignore) {}
/*     */     } 
/* 150 */     if (this.callback != null) {
/* 151 */       this.callback.endpointClosed(this);
/*     */     }
/* 153 */     synchronized (this) {
/* 154 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/ListenerEndpointImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */