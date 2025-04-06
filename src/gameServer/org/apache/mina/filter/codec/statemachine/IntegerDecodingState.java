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
/*    */ public abstract class IntegerDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private int firstByte;
/*    */   private int secondByte;
/*    */   private int thirdByte;
/*    */   private int counter;
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 46 */     while (in.hasRemaining()) {
/* 47 */       switch (this.counter) {
/*    */         case 0:
/* 49 */           this.firstByte = in.getUnsigned();
/*    */           break;
/*    */         case 1:
/* 52 */           this.secondByte = in.getUnsigned();
/*    */           break;
/*    */         case 2:
/* 55 */           this.thirdByte = in.getUnsigned();
/*    */           break;
/*    */         case 3:
/* 58 */           this.counter = 0;
/* 59 */           return finishDecode(this.firstByte << 24 | this.secondByte << 16 | this.thirdByte << 8 | in.getUnsigned(), out);
/*    */         default:
/* 61 */           throw new InternalError();
/*    */       } 
/* 63 */       this.counter++;
/*    */     } 
/*    */     
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 73 */     throw new ProtocolDecoderException("Unexpected end of session while waiting for an integer.");
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(int paramInt, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/IntegerDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */