/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorExceptionHandler;
/*     */ import org.apache.http.nio.reactor.IOSession;
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
/*     */ @ThreadSafe
/*     */ public class BaseIOReactor
/*     */   extends AbstractIOReactor
/*     */ {
/*     */   private final long timeoutCheckInterval;
/*     */   private final Set<IOSession> bufferingSessions;
/*     */   private long lastTimeoutCheck;
/*  62 */   private IOReactorExceptionHandler exceptionHandler = null;
/*  63 */   private IOEventDispatch eventDispatch = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseIOReactor(long selectTimeout) throws IOReactorException {
/*  72 */     this(selectTimeout, false);
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
/*     */   public BaseIOReactor(long selectTimeout, boolean interestOpsQueueing) throws IOReactorException {
/*  87 */     super(selectTimeout, interestOpsQueueing);
/*  88 */     this.bufferingSessions = new HashSet<IOSession>();
/*  89 */     this.timeoutCheckInterval = selectTimeout;
/*  90 */     this.lastTimeoutCheck = System.currentTimeMillis();
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
/*     */   public void execute(IOEventDispatch eventDispatch) throws InterruptedIOException, IOReactorException {
/* 104 */     Args.notNull(eventDispatch, "Event dispatcher");
/* 105 */     this.eventDispatch = eventDispatch;
/* 106 */     execute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionHandler(IOReactorExceptionHandler exceptionHandler) {
/* 115 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleRuntimeException(RuntimeException ex) {
/* 126 */     if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex)) {
/* 127 */       throw ex;
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
/*     */   protected void acceptable(SelectionKey key) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void connectable(SelectionKey key) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readable(SelectionKey key) {
/* 158 */     IOSession session = getSession(key);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 163 */       for (int i = 0; i < 5; i++) {
/* 164 */         this.eventDispatch.inputReady(session);
/* 165 */         if (!session.hasBufferedInput() || (session.getEventMask() & 0x1) == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 170 */       if (session.hasBufferedInput()) {
/* 171 */         this.bufferingSessions.add(session);
/*     */       }
/* 173 */     } catch (CancelledKeyException ex) {
/* 174 */       queueClosedSession(session);
/* 175 */       key.attach(null);
/* 176 */     } catch (RuntimeException ex) {
/* 177 */       handleRuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writable(SelectionKey key) {
/* 188 */     IOSession session = getSession(key);
/*     */     try {
/* 190 */       this.eventDispatch.outputReady(session);
/* 191 */     } catch (CancelledKeyException ex) {
/* 192 */       queueClosedSession(session);
/* 193 */       key.attach(null);
/* 194 */     } catch (RuntimeException ex) {
/* 195 */       handleRuntimeException(ex);
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
/*     */ 
/*     */   
/*     */   protected void validate(Set<SelectionKey> keys) {
/* 210 */     long currentTime = System.currentTimeMillis();
/* 211 */     if (currentTime - this.lastTimeoutCheck >= this.timeoutCheckInterval) {
/* 212 */       this.lastTimeoutCheck = currentTime;
/* 213 */       if (keys != null) {
/* 214 */         for (SelectionKey key : keys) {
/* 215 */           timeoutCheck(key, currentTime);
/*     */         }
/*     */       }
/*     */     } 
/* 219 */     if (!this.bufferingSessions.isEmpty()) {
/* 220 */       for (Iterator<IOSession> it = this.bufferingSessions.iterator(); it.hasNext(); ) {
/* 221 */         IOSession session = it.next();
/* 222 */         if (!session.hasBufferedInput()) {
/* 223 */           it.remove();
/*     */           continue;
/*     */         } 
/*     */         try {
/* 227 */           if ((session.getEventMask() & 0x1) > 0) {
/* 228 */             this.eventDispatch.inputReady(session);
/* 229 */             if (!session.hasBufferedInput()) {
/* 230 */               it.remove();
/*     */             }
/*     */           } 
/* 233 */         } catch (CancelledKeyException ex) {
/* 234 */           it.remove();
/* 235 */           queueClosedSession(session);
/* 236 */         } catch (RuntimeException ex) {
/* 237 */           handleRuntimeException(ex);
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
/*     */   protected void sessionCreated(SelectionKey key, IOSession session) {
/*     */     try {
/* 250 */       this.eventDispatch.connected(session);
/* 251 */     } catch (CancelledKeyException ex) {
/* 252 */       queueClosedSession(session);
/* 253 */     } catch (RuntimeException ex) {
/* 254 */       handleRuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sessionTimedOut(IOSession session) {
/*     */     try {
/* 265 */       this.eventDispatch.timeout(session);
/* 266 */     } catch (CancelledKeyException ex) {
/* 267 */       queueClosedSession(session);
/* 268 */     } catch (RuntimeException ex) {
/* 269 */       handleRuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sessionClosed(IOSession session) {
/*     */     try {
/* 281 */       this.eventDispatch.disconnected(session);
/* 282 */     } catch (CancelledKeyException ex) {
/*     */     
/* 284 */     } catch (RuntimeException ex) {
/* 285 */       handleRuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/BaseIOReactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */