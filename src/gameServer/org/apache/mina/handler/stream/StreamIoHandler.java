/*     */ package org.apache.mina.handler.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.service.IoHandlerAdapter;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public abstract class StreamIoHandler
/*     */   extends IoHandlerAdapter
/*     */ {
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(StreamIoHandler.class);
/*     */   
/*  49 */   private static final AttributeKey KEY_IN = new AttributeKey(StreamIoHandler.class, "in");
/*     */   
/*  51 */   private static final AttributeKey KEY_OUT = new AttributeKey(StreamIoHandler.class, "out");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readTimeout;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int writeTimeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void processStreamIo(IoSession paramIoSession, InputStream paramInputStream, OutputStream paramOutputStream);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReadTimeout() {
/*  73 */     return this.readTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/*  81 */     this.readTimeout = readTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWriteTimeout() {
/*  89 */     return this.writeTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteTimeout(int writeTimeout) {
/*  97 */     this.writeTimeout = writeTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoSession session) {
/* 106 */     session.getConfig().setWriteTimeout(this.writeTimeout);
/* 107 */     session.getConfig().setIdleTime(IdleStatus.READER_IDLE, this.readTimeout);
/*     */ 
/*     */     
/* 110 */     InputStream in = new IoSessionInputStream();
/* 111 */     OutputStream out = new IoSessionOutputStream(session);
/* 112 */     session.setAttribute(KEY_IN, in);
/* 113 */     session.setAttribute(KEY_OUT, out);
/* 114 */     processStreamIo(session, in, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoSession session) throws Exception {
/* 122 */     InputStream in = (InputStream)session.getAttribute(KEY_IN);
/* 123 */     OutputStream out = (OutputStream)session.getAttribute(KEY_OUT);
/*     */     try {
/* 125 */       in.close();
/*     */     } finally {
/* 127 */       out.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageReceived(IoSession session, Object buf) {
/* 136 */     IoSessionInputStream in = (IoSessionInputStream)session.getAttribute(KEY_IN);
/* 137 */     in.write((IoBuffer)buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoSession session, Throwable cause) {
/* 145 */     IoSessionInputStream in = (IoSessionInputStream)session.getAttribute(KEY_IN);
/*     */     
/* 147 */     IOException e = null;
/* 148 */     if (cause instanceof StreamIoException) {
/* 149 */       e = (IOException)cause.getCause();
/* 150 */     } else if (cause instanceof IOException) {
/* 151 */       e = (IOException)cause;
/*     */     } 
/*     */     
/* 154 */     if (e != null && in != null) {
/* 155 */       in.throwException(e);
/*     */     } else {
/* 157 */       LOGGER.warn("Unexpected exception.", cause);
/* 158 */       session.close(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoSession session, IdleStatus status) {
/* 167 */     if (status == IdleStatus.READER_IDLE)
/* 168 */       throw new StreamIoException(new SocketTimeoutException("Read timeout")); 
/*     */   }
/*     */   
/*     */   private static class StreamIoException
/*     */     extends RuntimeException {
/*     */     private static final long serialVersionUID = 3976736960742503222L;
/*     */     
/*     */     public StreamIoException(IOException cause) {
/* 176 */       super(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/stream/StreamIoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */