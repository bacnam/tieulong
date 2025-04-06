/*     */ package org.apache.mina.filter.buffer;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.util.LazyInitializedCacheMap;
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
/*     */ 
/*     */ 
/*     */ public final class BufferedWriteFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  50 */   private final Logger logger = LoggerFactory.getLogger(BufferedWriteFilter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private int bufferSize = 8192;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final LazyInitializedCacheMap<IoSession, IoBuffer> buffersMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedWriteFilter() {
/*  73 */     this(8192, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedWriteFilter(int bufferSize) {
/*  83 */     this(bufferSize, null);
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
/*     */   public BufferedWriteFilter(int bufferSize, LazyInitializedCacheMap<IoSession, IoBuffer> buffersMap) {
/*  96 */     this.bufferSize = bufferSize;
/*  97 */     if (buffersMap == null) {
/*  98 */       this.buffersMap = new LazyInitializedCacheMap();
/*     */     } else {
/* 100 */       this.buffersMap = buffersMap;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 108 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int bufferSize) {
/* 117 */     this.bufferSize = bufferSize;
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
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 129 */     Object data = writeRequest.getMessage();
/*     */     
/* 131 */     if (data instanceof IoBuffer) {
/* 132 */       write(session, (IoBuffer)data);
/*     */     } else {
/* 134 */       throw new IllegalArgumentException("This filter should only buffer IoBuffer objects");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void write(IoSession session, IoBuffer data) {
/* 145 */     IoBuffer dest = (IoBuffer)this.buffersMap.putIfAbsent(session, new IoBufferLazyInitializer(this.bufferSize));
/*     */     
/* 147 */     write(session, data, dest);
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
/*     */   private void write(IoSession session, IoBuffer data, IoBuffer buf) {
/*     */     try {
/* 162 */       int len = data.remaining();
/* 163 */       if (len >= buf.capacity()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 168 */         IoFilter.NextFilter nextFilter = session.getFilterChain().getNextFilter((IoFilter)this);
/* 169 */         internalFlush(nextFilter, session, buf);
/* 170 */         nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(data));
/*     */         return;
/*     */       } 
/* 173 */       if (len > buf.limit() - buf.position()) {
/* 174 */         internalFlush(session.getFilterChain().getNextFilter((IoFilter)this), session, buf);
/*     */       }
/* 176 */       synchronized (buf) {
/* 177 */         buf.put(data);
/*     */       } 
/* 179 */     } catch (Exception e) {
/* 180 */       session.getFilterChain().fireExceptionCaught(e);
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
/*     */   private void internalFlush(IoFilter.NextFilter nextFilter, IoSession session, IoBuffer buf) throws Exception {
/* 193 */     IoBuffer tmp = null;
/* 194 */     synchronized (buf) {
/* 195 */       buf.flip();
/* 196 */       tmp = buf.duplicate();
/* 197 */       buf.clear();
/*     */     } 
/* 199 */     this.logger.debug("Flushing buffer: {}", tmp);
/* 200 */     nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(tmp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush(IoSession session) {
/*     */     try {
/* 210 */       internalFlush(session.getFilterChain().getNextFilter((IoFilter)this), session, (IoBuffer)this.buffersMap.get(session));
/* 211 */     } catch (Exception e) {
/* 212 */       session.getFilterChain().fireExceptionCaught(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void free(IoSession session) {
/* 223 */     IoBuffer buf = (IoBuffer)this.buffersMap.remove(session);
/* 224 */     if (buf != null) {
/* 225 */       buf.free();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/* 234 */     free(session);
/* 235 */     nextFilter.exceptionCaught(session, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 243 */     free(session);
/* 244 */     nextFilter.sessionClosed(session);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/buffer/BufferedWriteFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */