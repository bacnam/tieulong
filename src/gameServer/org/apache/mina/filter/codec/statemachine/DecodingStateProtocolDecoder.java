/*    */ package org.apache.mina.filter.codec.statemachine;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*    */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DecodingStateProtocolDecoder
/*    */   implements ProtocolDecoder
/*    */ {
/*    */   private final DecodingState state;
/* 43 */   private final Queue<IoBuffer> undecodedBuffers = new ConcurrentLinkedQueue<IoBuffer>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private IoSession session;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingStateProtocolDecoder(DecodingState state) {
/* 55 */     if (state == null) {
/* 56 */       throw new IllegalArgumentException("state");
/*    */     }
/* 58 */     this.state = state;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 65 */     if (this.session == null) {
/* 66 */       this.session = session;
/* 67 */     } else if (this.session != session) {
/* 68 */       throw new IllegalStateException(getClass().getSimpleName() + " is a stateful decoder.  " + "You have to create one per session.");
/*    */     } 
/*    */ 
/*    */     
/* 72 */     this.undecodedBuffers.offer(in);
/*    */     while (true) {
/* 74 */       IoBuffer b = this.undecodedBuffers.peek();
/* 75 */       if (b == null) {
/*    */         break;
/*    */       }
/*    */       
/* 79 */       int oldRemaining = b.remaining();
/* 80 */       this.state.decode(b, out);
/* 81 */       int newRemaining = b.remaining();
/* 82 */       if (newRemaining != 0) {
/* 83 */         if (oldRemaining == newRemaining) {
/* 84 */           throw new IllegalStateException(DecodingState.class.getSimpleName() + " must " + "consume at least one byte per decode().");
/*    */         }
/*    */         continue;
/*    */       } 
/* 88 */       this.undecodedBuffers.poll();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
/* 97 */     this.state.finishDecode(out);
/*    */   }
/*    */   
/*    */   public void dispose(IoSession session) throws Exception {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/DecodingStateProtocolDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */