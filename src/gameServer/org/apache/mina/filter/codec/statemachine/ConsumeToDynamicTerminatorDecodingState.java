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
/*    */ public abstract class ConsumeToDynamicTerminatorDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private IoBuffer buffer;
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 39 */     int beginPos = in.position();
/* 40 */     int terminatorPos = -1;
/* 41 */     int limit = in.limit();
/*    */     
/* 43 */     for (int i = beginPos; i < limit; i++) {
/* 44 */       byte b = in.get(i);
/* 45 */       if (isTerminator(b)) {
/* 46 */         terminatorPos = i;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 51 */     if (terminatorPos >= 0) {
/*    */       IoBuffer product;
/*    */       
/* 54 */       if (beginPos < terminatorPos) {
/* 55 */         in.limit(terminatorPos);
/*    */         
/* 57 */         if (this.buffer == null) {
/* 58 */           product = in.slice();
/*    */         } else {
/* 60 */           this.buffer.put(in);
/* 61 */           product = this.buffer.flip();
/* 62 */           this.buffer = null;
/*    */         } 
/*    */         
/* 65 */         in.limit(limit);
/*    */       
/*    */       }
/* 68 */       else if (this.buffer == null) {
/* 69 */         product = IoBuffer.allocate(0);
/*    */       } else {
/* 71 */         product = this.buffer.flip();
/* 72 */         this.buffer = null;
/*    */       } 
/*    */       
/* 75 */       in.position(terminatorPos + 1);
/* 76 */       return finishDecode(product, out);
/*    */     } 
/*    */     
/* 79 */     if (this.buffer == null) {
/* 80 */       this.buffer = IoBuffer.allocate(in.remaining());
/* 81 */       this.buffer.setAutoExpand(true);
/*    */     } 
/* 83 */     this.buffer.put(in);
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/*    */     IoBuffer product;
/* 93 */     if (this.buffer == null) {
/* 94 */       product = IoBuffer.allocate(0);
/*    */     } else {
/* 96 */       product = this.buffer.flip();
/* 97 */       this.buffer = null;
/*    */     } 
/* 99 */     return finishDecode(product, out);
/*    */   }
/*    */   
/*    */   protected abstract boolean isTerminator(byte paramByte);
/*    */   
/*    */   protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ConsumeToDynamicTerminatorDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */