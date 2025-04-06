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
/*    */ public abstract class ShortIntegerDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private int highByte;
/*    */   private int counter;
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 43 */     while (in.hasRemaining()) {
/* 44 */       switch (this.counter) {
/*    */         case 0:
/* 46 */           this.highByte = in.getUnsigned();
/*    */           break;
/*    */         case 1:
/* 49 */           this.counter = 0;
/* 50 */           return finishDecode((short)(this.highByte << 8 | in.getUnsigned()), out);
/*    */         default:
/* 52 */           throw new InternalError();
/*    */       } 
/*    */       
/* 55 */       this.counter++;
/*    */     } 
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 64 */     throw new ProtocolDecoderException("Unexpected end of session while waiting for a short integer.");
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(short paramShort, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ShortIntegerDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */