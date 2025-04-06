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
/*    */ public abstract class SingleByteDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 34 */     if (in.hasRemaining()) {
/* 35 */       return finishDecode(in.get(), out);
/*    */     }
/*    */     
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 45 */     throw new ProtocolDecoderException("Unexpected end of session while waiting for a single byte.");
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(byte paramByte, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/SingleByteDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */