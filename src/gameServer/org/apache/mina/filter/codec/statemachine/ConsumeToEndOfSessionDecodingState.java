/*    */ package org.apache.mina.filter.codec.statemachine;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.filter.codec.ProtocolDecoderException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ConsumeToEndOfSessionDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private IoBuffer buffer;
/*    */   private final int maxLength;
/*    */   
/*    */   public ConsumeToEndOfSessionDecodingState(int maxLength) {
/* 46 */     this.maxLength = maxLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 53 */     if (this.buffer == null) {
/* 54 */       this.buffer = IoBuffer.allocate(256).setAutoExpand(true);
/*    */     }
/*    */     
/* 57 */     if (this.buffer.position() + in.remaining() > this.maxLength) {
/* 58 */       throw new ProtocolDecoderException("Received data exceeds " + this.maxLength + " byte(s).");
/*    */     }
/* 60 */     this.buffer.put(in);
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/*    */     try {
/* 69 */       if (this.buffer == null) {
/* 70 */         this.buffer = IoBuffer.allocate(0);
/*    */       }
/* 72 */       this.buffer.flip();
/* 73 */       return finishDecode(this.buffer, out);
/*    */     } finally {
/* 75 */       this.buffer = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ConsumeToEndOfSessionDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */