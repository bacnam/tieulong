/*    */ package org.apache.mina.handler.stream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.future.WriteFuture;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class IoSessionOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final IoSession session;
/*    */   private WriteFuture lastWriteFuture;
/*    */   
/*    */   public IoSessionOutputStream(IoSession session) {
/* 41 */     this.session = session;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 47 */       flush();
/*    */     } finally {
/* 49 */       this.session.close(true).awaitUninterruptibly();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void checkClosed() throws IOException {
/* 54 */     if (!this.session.isConnected()) {
/* 55 */       throw new IOException("The session has been closed.");
/*    */     }
/*    */   }
/*    */   
/*    */   private synchronized void write(IoBuffer buf) throws IOException {
/* 60 */     checkClosed();
/* 61 */     WriteFuture future = this.session.write(buf);
/* 62 */     this.lastWriteFuture = future;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 67 */     write(IoBuffer.wrap((byte[])b.clone(), off, len));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 72 */     IoBuffer buf = IoBuffer.allocate(1);
/* 73 */     buf.put((byte)b);
/* 74 */     buf.flip();
/* 75 */     write(buf);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void flush() throws IOException {
/* 80 */     if (this.lastWriteFuture == null) {
/*    */       return;
/*    */     }
/*    */     
/* 84 */     this.lastWriteFuture.awaitUninterruptibly();
/* 85 */     if (!this.lastWriteFuture.isWritten())
/* 86 */       throw new IOException("The bytes could not be written to the session"); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/stream/IoSessionOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */