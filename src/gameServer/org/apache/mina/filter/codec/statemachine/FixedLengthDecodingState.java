/*    */ package org.apache.mina.filter.codec.statemachine;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
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
/*    */ public abstract class FixedLengthDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private final int length;
/*    */   private IoBuffer buffer;
/*    */   
/*    */   public FixedLengthDecodingState(int length) {
/* 45 */     this.length = length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 52 */     if (this.buffer == null) {
/* 53 */       if (in.remaining() >= this.length) {
/* 54 */         int limit = in.limit();
/* 55 */         in.limit(in.position() + this.length);
/* 56 */         IoBuffer product = in.slice();
/* 57 */         in.position(in.position() + this.length);
/* 58 */         in.limit(limit);
/* 59 */         return finishDecode(product, out);
/*    */       } 
/*    */       
/* 62 */       this.buffer = IoBuffer.allocate(this.length);
/* 63 */       this.buffer.put(in);
/* 64 */       return this;
/*    */     } 
/*    */     
/* 67 */     if (in.remaining() >= this.length - this.buffer.position()) {
/* 68 */       int limit = in.limit();
/* 69 */       in.limit(in.position() + this.length - this.buffer.position());
/* 70 */       this.buffer.put(in);
/* 71 */       in.limit(limit);
/* 72 */       IoBuffer product = this.buffer;
/* 73 */       this.buffer = null;
/* 74 */       return finishDecode(product.flip(), out);
/*    */     } 
/*    */     
/* 77 */     this.buffer.put(in);
/* 78 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/*    */     IoBuffer readData;
/* 86 */     if (this.buffer == null) {
/* 87 */       readData = IoBuffer.allocate(0);
/*    */     } else {
/* 89 */       readData = this.buffer.flip();
/* 90 */       this.buffer = null;
/*    */     } 
/* 92 */     return finishDecode(readData, out);
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/FixedLengthDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */