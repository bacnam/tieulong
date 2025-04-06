/*     */ package org.apache.mina.filter.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractStreamWriteFilter<T>
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   public static final int DEFAULT_STREAM_BUFFER_SIZE = 4096;
/*  48 */   protected final AttributeKey CURRENT_STREAM = new AttributeKey(getClass(), "stream");
/*     */   
/*  50 */   protected final AttributeKey WRITE_REQUEST_QUEUE = new AttributeKey(getClass(), "queue");
/*     */   
/*  52 */   protected final AttributeKey CURRENT_WRITE_REQUEST = new AttributeKey(getClass(), "writeRequest");
/*     */   
/*  54 */   private int writeBufferSize = 4096;
/*     */ 
/*     */   
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/*  58 */     Class<? extends IoFilterAdapter> clazz = (Class)getClass();
/*  59 */     if (parent.contains(clazz)) {
/*  60 */       throw new IllegalStateException("Only one " + clazz.getName() + " is permitted.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  67 */     if (session.getAttribute(this.CURRENT_STREAM) != null) {
/*  68 */       Queue<WriteRequest> queue = getWriteRequestQueue(session);
/*  69 */       if (queue == null) {
/*  70 */         queue = new ConcurrentLinkedQueue<WriteRequest>();
/*  71 */         session.setAttribute(this.WRITE_REQUEST_QUEUE, queue);
/*     */       } 
/*  73 */       queue.add(writeRequest);
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     Object message = writeRequest.getMessage();
/*     */     
/*  79 */     if (getMessageClass().isInstance(message)) {
/*     */       
/*  81 */       T stream = getMessageClass().cast(message);
/*     */       
/*  83 */       IoBuffer buffer = getNextBuffer(stream);
/*  84 */       if (buffer == null) {
/*     */         
/*  86 */         writeRequest.getFuture().setWritten();
/*  87 */         nextFilter.messageSent(session, writeRequest);
/*     */       } else {
/*  89 */         session.setAttribute(this.CURRENT_STREAM, message);
/*  90 */         session.setAttribute(this.CURRENT_WRITE_REQUEST, writeRequest);
/*     */         
/*  92 */         nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(buffer));
/*     */       } 
/*     */     } else {
/*     */       
/*  96 */       nextFilter.filterWrite(session, writeRequest);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Class<T> getMessageClass();
/*     */   
/*     */   private Queue<WriteRequest> getWriteRequestQueue(IoSession session) {
/* 104 */     return (Queue<WriteRequest>)session.getAttribute(this.WRITE_REQUEST_QUEUE);
/*     */   }
/*     */ 
/*     */   
/*     */   private Queue<WriteRequest> removeWriteRequestQueue(IoSession session) {
/* 109 */     return (Queue<WriteRequest>)session.removeAttribute(this.WRITE_REQUEST_QUEUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 114 */     T stream = getMessageClass().cast(session.getAttribute(this.CURRENT_STREAM));
/*     */     
/* 116 */     if (stream == null) {
/* 117 */       nextFilter.messageSent(session, writeRequest);
/*     */     } else {
/* 119 */       IoBuffer buffer = getNextBuffer(stream);
/*     */       
/* 121 */       if (buffer == null) {
/*     */         
/* 123 */         session.removeAttribute(this.CURRENT_STREAM);
/* 124 */         WriteRequest currentWriteRequest = (WriteRequest)session.removeAttribute(this.CURRENT_WRITE_REQUEST);
/*     */ 
/*     */         
/* 127 */         Queue<WriteRequest> queue = removeWriteRequestQueue(session);
/* 128 */         if (queue != null) {
/* 129 */           WriteRequest wr = queue.poll();
/* 130 */           while (wr != null) {
/* 131 */             filterWrite(nextFilter, session, wr);
/* 132 */             wr = queue.poll();
/*     */           } 
/*     */         } 
/*     */         
/* 136 */         currentWriteRequest.getFuture().setWritten();
/* 137 */         nextFilter.messageSent(session, currentWriteRequest);
/*     */       } else {
/* 139 */         nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(buffer));
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
/*     */   public int getWriteBufferSize() {
/* 151 */     return this.writeBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteBufferSize(int writeBufferSize) {
/* 161 */     if (writeBufferSize < 1) {
/* 162 */       throw new IllegalArgumentException("writeBufferSize must be at least 1");
/*     */     }
/* 164 */     this.writeBufferSize = writeBufferSize;
/*     */   }
/*     */   
/*     */   protected abstract IoBuffer getNextBuffer(T paramT) throws IOException;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/stream/AbstractStreamWriteFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */