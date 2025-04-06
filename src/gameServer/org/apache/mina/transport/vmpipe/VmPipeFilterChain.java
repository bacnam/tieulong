/*     */ package org.apache.mina.transport.vmpipe;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChain;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoEvent;
/*     */ import org.apache.mina.core.session.IoEventType;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestQueue;
/*     */ import org.apache.mina.core.write.WriteToClosedSessionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VmPipeFilterChain
/*     */   extends DefaultIoFilterChain
/*     */ {
/*  45 */   private final Queue<IoEvent> eventQueue = new ConcurrentLinkedQueue<IoEvent>();
/*     */   
/*  47 */   private final IoProcessor<VmPipeSession> processor = new VmPipeIoProcessor();
/*     */   
/*     */   private volatile boolean flushEnabled;
/*     */   
/*     */   private volatile boolean sessionOpened;
/*     */   
/*     */   VmPipeFilterChain(AbstractIoSession session) {
/*  54 */     super(session);
/*     */   }
/*     */   
/*     */   IoProcessor<VmPipeSession> getProcessor() {
/*  58 */     return this.processor;
/*     */   }
/*     */   
/*     */   public void start() {
/*  62 */     this.flushEnabled = true;
/*  63 */     flushEvents();
/*  64 */     flushPendingDataQueues((VmPipeSession)getSession());
/*     */   }
/*     */   
/*     */   private void pushEvent(IoEvent e) {
/*  68 */     pushEvent(e, this.flushEnabled);
/*     */   }
/*     */   
/*     */   private void pushEvent(IoEvent e, boolean flushNow) {
/*  72 */     this.eventQueue.add(e);
/*  73 */     if (flushNow) {
/*  74 */       flushEvents();
/*     */     }
/*     */   }
/*     */   
/*     */   private void flushEvents() {
/*     */     IoEvent e;
/*  80 */     while ((e = this.eventQueue.poll()) != null) {
/*  81 */       fireEvent(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireEvent(IoEvent e) {
/*  86 */     VmPipeSession session = (VmPipeSession)getSession();
/*  87 */     IoEventType type = e.getType();
/*  88 */     Object data = e.getParameter();
/*     */     
/*  90 */     if (type == IoEventType.MESSAGE_RECEIVED) {
/*  91 */       if (this.sessionOpened && !session.isReadSuspended() && session.getLock().tryLock()) {
/*     */         try {
/*  93 */           if (session.isReadSuspended()) {
/*  94 */             session.receivedMessageQueue.add(data);
/*     */           } else {
/*  96 */             super.fireMessageReceived(data);
/*     */           } 
/*     */         } finally {
/*  99 */           session.getLock().unlock();
/*     */         } 
/*     */       } else {
/* 102 */         session.receivedMessageQueue.add(data);
/*     */       } 
/* 104 */     } else if (type == IoEventType.WRITE) {
/* 105 */       super.fireFilterWrite((WriteRequest)data);
/* 106 */     } else if (type == IoEventType.MESSAGE_SENT) {
/* 107 */       super.fireMessageSent((WriteRequest)data);
/* 108 */     } else if (type == IoEventType.EXCEPTION_CAUGHT) {
/* 109 */       super.fireExceptionCaught((Throwable)data);
/* 110 */     } else if (type == IoEventType.SESSION_IDLE) {
/* 111 */       super.fireSessionIdle((IdleStatus)data);
/* 112 */     } else if (type == IoEventType.SESSION_OPENED) {
/* 113 */       super.fireSessionOpened();
/* 114 */       this.sessionOpened = true;
/* 115 */     } else if (type == IoEventType.SESSION_CREATED) {
/* 116 */       session.getLock().lock();
/*     */       try {
/* 118 */         super.fireSessionCreated();
/*     */       } finally {
/* 120 */         session.getLock().unlock();
/*     */       } 
/* 122 */     } else if (type == IoEventType.SESSION_CLOSED) {
/* 123 */       flushPendingDataQueues(session);
/* 124 */       super.fireSessionClosed();
/* 125 */     } else if (type == IoEventType.CLOSE) {
/* 126 */       super.fireFilterClose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void flushPendingDataQueues(VmPipeSession s) {
/* 131 */     s.getProcessor().updateTrafficControl((IoSession)s);
/* 132 */     s.getRemoteSession().getProcessor().updateTrafficControl((IoSession)s);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireFilterClose() {
/* 137 */     pushEvent(new IoEvent(IoEventType.CLOSE, getSession(), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireFilterWrite(WriteRequest writeRequest) {
/* 142 */     pushEvent(new IoEvent(IoEventType.WRITE, getSession(), writeRequest));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireExceptionCaught(Throwable cause) {
/* 147 */     pushEvent(new IoEvent(IoEventType.EXCEPTION_CAUGHT, getSession(), cause));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireMessageSent(WriteRequest request) {
/* 152 */     pushEvent(new IoEvent(IoEventType.MESSAGE_SENT, getSession(), request));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireSessionClosed() {
/* 157 */     pushEvent(new IoEvent(IoEventType.SESSION_CLOSED, getSession(), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireSessionCreated() {
/* 162 */     pushEvent(new IoEvent(IoEventType.SESSION_CREATED, getSession(), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireSessionIdle(IdleStatus status) {
/* 167 */     pushEvent(new IoEvent(IoEventType.SESSION_IDLE, getSession(), status));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireSessionOpened() {
/* 172 */     pushEvent(new IoEvent(IoEventType.SESSION_OPENED, getSession(), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireMessageReceived(Object message) {
/* 177 */     pushEvent(new IoEvent(IoEventType.MESSAGE_RECEIVED, getSession(), message));
/*     */   }
/*     */   
/*     */   private class VmPipeIoProcessor implements IoProcessor<VmPipeSession> {
/*     */     public void flush(VmPipeSession session) {
/* 182 */       WriteRequestQueue queue = session.getWriteRequestQueue0();
/* 183 */       if (!session.isClosing()) {
/* 184 */         session.getLock().lock();
/*     */         try {
/* 186 */           if (queue.isEmpty((IoSession)session)) {
/*     */             return;
/*     */           }
/*     */           
/* 190 */           long currentTime = System.currentTimeMillis(); WriteRequest req;
/* 191 */           while ((req = queue.poll((IoSession)session)) != null) {
/* 192 */             Object m = req.getMessage();
/* 193 */             VmPipeFilterChain.this.pushEvent(new IoEvent(IoEventType.MESSAGE_SENT, (IoSession)session, req), false);
/* 194 */             session.getRemoteSession().getFilterChain().fireMessageReceived(getMessageCopy(m));
/* 195 */             if (m instanceof IoBuffer) {
/* 196 */               session.increaseWrittenBytes0(((IoBuffer)m).remaining(), currentTime);
/*     */             }
/*     */           } 
/*     */         } finally {
/* 200 */           if (VmPipeFilterChain.this.flushEnabled) {
/* 201 */             VmPipeFilterChain.this.flushEvents();
/*     */           }
/* 203 */           session.getLock().unlock();
/*     */         } 
/*     */         
/* 206 */         VmPipeFilterChain.flushPendingDataQueues(session);
/*     */       } else {
/* 208 */         List<WriteRequest> failedRequests = new ArrayList<WriteRequest>();
/*     */         WriteRequest req;
/* 210 */         while ((req = queue.poll((IoSession)session)) != null) {
/* 211 */           failedRequests.add(req);
/*     */         }
/*     */         
/* 214 */         if (!failedRequests.isEmpty()) {
/* 215 */           WriteToClosedSessionException cause = new WriteToClosedSessionException(failedRequests);
/* 216 */           for (WriteRequest r : failedRequests) {
/* 217 */             r.getFuture().setException((Throwable)cause);
/*     */           }
/* 219 */           session.getFilterChain().fireExceptionCaught((Throwable)cause);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private VmPipeIoProcessor() {}
/*     */     
/*     */     public void write(VmPipeSession session, WriteRequest writeRequest) {
/* 228 */       WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/*     */       
/* 230 */       writeRequestQueue.offer((IoSession)session, writeRequest);
/*     */       
/* 232 */       if (!session.isWriteSuspended()) {
/* 233 */         flush(session);
/*     */       }
/*     */     }
/*     */     
/*     */     private Object getMessageCopy(Object message) {
/* 238 */       Object messageCopy = message;
/* 239 */       if (message instanceof IoBuffer) {
/* 240 */         IoBuffer rb = (IoBuffer)message;
/* 241 */         rb.mark();
/* 242 */         IoBuffer wb = IoBuffer.allocate(rb.remaining());
/* 243 */         wb.put(rb);
/* 244 */         wb.flip();
/* 245 */         rb.reset();
/* 246 */         messageCopy = wb;
/*     */       } 
/* 248 */       return messageCopy;
/*     */     }
/*     */     
/*     */     public void remove(VmPipeSession session) {
/*     */       try {
/* 253 */         session.getLock().lock();
/* 254 */         if (!session.getCloseFuture().isClosed()) {
/* 255 */           session.getServiceListeners().fireSessionDestroyed((IoSession)session);
/* 256 */           session.getRemoteSession().close(true);
/*     */         } 
/*     */       } finally {
/* 259 */         session.getLock().unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(VmPipeSession session) {}
/*     */ 
/*     */     
/*     */     public void updateTrafficControl(VmPipeSession session) {
/* 268 */       if (!session.isReadSuspended()) {
/* 269 */         List<Object> data = new ArrayList();
/* 270 */         session.receivedMessageQueue.drainTo(data);
/* 271 */         for (Object aData : data) {
/* 272 */           VmPipeFilterChain.this.fireMessageReceived(aData);
/*     */         }
/*     */       } 
/*     */       
/* 276 */       if (!session.isWriteSuspended()) {
/* 277 */         flush(session);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {}
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 286 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isDisposing() {
/* 290 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipeFilterChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */