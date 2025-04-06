/*     */ package org.apache.mina.core.write;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultWriteRequest
/*     */   implements WriteRequest
/*     */ {
/*  36 */   public static final byte[] EMPTY_MESSAGE = new byte[0];
/*     */ 
/*     */   
/*  39 */   private static final WriteFuture UNUSED_FUTURE = new WriteFuture() {
/*     */       public boolean isWritten() {
/*  41 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public void setWritten() {}
/*     */ 
/*     */       
/*     */       public IoSession getSession() {
/*  49 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void join() {}
/*     */ 
/*     */       
/*     */       public boolean join(long timeoutInMillis) {
/*  57 */         return true;
/*     */       }
/*     */       
/*     */       public boolean isDone() {
/*  61 */         return true;
/*     */       }
/*     */       
/*     */       public WriteFuture addListener(IoFutureListener<?> listener) {
/*  65 */         throw new IllegalStateException("You can't add a listener to a dummy future.");
/*     */       }
/*     */       
/*     */       public WriteFuture removeListener(IoFutureListener<?> listener) {
/*  69 */         throw new IllegalStateException("You can't add a listener to a dummy future.");
/*     */       }
/*     */       
/*     */       public WriteFuture await() throws InterruptedException {
/*  73 */         return this;
/*     */       }
/*     */       
/*     */       public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
/*  77 */         return true;
/*     */       }
/*     */       
/*     */       public boolean await(long timeoutMillis) throws InterruptedException {
/*  81 */         return true;
/*     */       }
/*     */       
/*     */       public WriteFuture awaitUninterruptibly() {
/*  85 */         return this;
/*     */       }
/*     */       
/*     */       public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
/*  89 */         return true;
/*     */       }
/*     */       
/*     */       public boolean awaitUninterruptibly(long timeoutMillis) {
/*  93 */         return true;
/*     */       }
/*     */       
/*     */       public Throwable getException() {
/*  97 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void setException(Throwable cause) {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object message;
/*     */ 
/*     */   
/*     */   private final WriteFuture future;
/*     */ 
/*     */   
/*     */   private final SocketAddress destination;
/*     */ 
/*     */   
/*     */   public DefaultWriteRequest(Object message) {
/* 117 */     this(message, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultWriteRequest(Object message, WriteFuture future) {
/* 124 */     this(message, future, null);
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
/*     */   public DefaultWriteRequest(Object message, WriteFuture future, SocketAddress destination) {
/* 136 */     if (message == null) {
/* 137 */       throw new IllegalArgumentException("message");
/*     */     }
/*     */     
/* 140 */     if (future == null) {
/* 141 */       future = UNUSED_FUTURE;
/*     */     }
/*     */     
/* 144 */     this.message = message;
/* 145 */     this.future = future;
/* 146 */     this.destination = destination;
/*     */   }
/*     */   
/*     */   public WriteFuture getFuture() {
/* 150 */     return this.future;
/*     */   }
/*     */   
/*     */   public Object getMessage() {
/* 154 */     return this.message;
/*     */   }
/*     */   
/*     */   public WriteRequest getOriginalRequest() {
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public SocketAddress getDestination() {
/* 162 */     return this.destination;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 169 */     sb.append("WriteRequest: ");
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (this.message.getClass().getName().equals(Object.class.getName())) {
/* 174 */       sb.append("CLOSE_REQUEST");
/*     */     }
/* 176 */     else if (getDestination() == null) {
/* 177 */       sb.append(this.message);
/*     */     } else {
/* 179 */       sb.append(this.message);
/* 180 */       sb.append(" => ");
/* 181 */       sb.append(getDestination());
/*     */     } 
/*     */ 
/*     */     
/* 185 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean isEncoded() {
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/DefaultWriteRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */