/*    */ package org.apache.mina.filter.codec;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
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
/*    */ public abstract class AbstractProtocolEncoderOutput
/*    */   implements ProtocolEncoderOutput
/*    */ {
/* 33 */   private final Queue<Object> messageQueue = new ConcurrentLinkedQueue();
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean buffersOnly = true;
/*    */ 
/*    */ 
/*    */   
/*    */   public Queue<Object> getMessageQueue() {
/* 42 */     return this.messageQueue;
/*    */   }
/*    */   
/*    */   public void write(Object encodedMessage) {
/* 46 */     if (encodedMessage instanceof IoBuffer) {
/* 47 */       IoBuffer buf = (IoBuffer)encodedMessage;
/* 48 */       if (buf.hasRemaining()) {
/* 49 */         this.messageQueue.offer(buf);
/*    */       } else {
/* 51 */         throw new IllegalArgumentException("buf is empty. Forgot to call flip()?");
/*    */       } 
/*    */     } else {
/* 54 */       this.messageQueue.offer(encodedMessage);
/* 55 */       this.buffersOnly = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void mergeAll() {
/* 60 */     if (!this.buffersOnly) {
/* 61 */       throw new IllegalStateException("the encoded message list contains a non-buffer.");
/*    */     }
/*    */     
/* 64 */     int size = this.messageQueue.size();
/*    */     
/* 66 */     if (size < 2) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 72 */     int sum = 0;
/* 73 */     for (Object b : this.messageQueue) {
/* 74 */       sum += ((IoBuffer)b).remaining();
/*    */     }
/*    */ 
/*    */     
/* 78 */     IoBuffer newBuf = IoBuffer.allocate(sum);
/*    */ 
/*    */     
/*    */     while (true) {
/* 82 */       IoBuffer buf = (IoBuffer)this.messageQueue.poll();
/* 83 */       if (buf == null) {
/*    */         break;
/*    */       }
/*    */       
/* 87 */       newBuf.put(buf);
/*    */     } 
/*    */ 
/*    */     
/* 91 */     newBuf.flip();
/* 92 */     this.messageQueue.add(newBuf);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/AbstractProtocolEncoderOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */