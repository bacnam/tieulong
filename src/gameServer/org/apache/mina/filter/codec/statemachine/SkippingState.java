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
/*    */ public abstract class SkippingState
/*    */   implements DecodingState
/*    */ {
/*    */   private int skippedBytes;
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 39 */     int beginPos = in.position();
/* 40 */     int limit = in.limit();
/* 41 */     for (int i = beginPos; i < limit; i++) {
/* 42 */       byte b = in.get(i);
/* 43 */       if (!canSkip(b)) {
/* 44 */         in.position(i);
/* 45 */         int answer = this.skippedBytes;
/* 46 */         this.skippedBytes = 0;
/* 47 */         return finishDecode(answer);
/*    */       } 
/*    */       
/* 50 */       this.skippedBytes++;
/*    */     } 
/*    */     
/* 53 */     in.position(limit);
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 61 */     return finishDecode(this.skippedBytes);
/*    */   }
/*    */   
/*    */   protected abstract boolean canSkip(byte paramByte);
/*    */   
/*    */   protected abstract DecodingState finishDecode(int paramInt) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/SkippingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */