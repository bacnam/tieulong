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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CrLfDecodingState
/*    */   implements DecodingState
/*    */ {
/*    */   private static final byte CR = 13;
/*    */   private static final byte LF = 10;
/*    */   private boolean hasCR;
/*    */   
/*    */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 54 */     boolean found = false;
/* 55 */     boolean finished = false;
/* 56 */     while (in.hasRemaining()) {
/* 57 */       byte b = in.get();
/* 58 */       if (!this.hasCR) {
/* 59 */         if (b == 13) {
/* 60 */           this.hasCR = true; continue;
/*    */         } 
/* 62 */         if (b == 10) {
/* 63 */           found = true;
/*    */         } else {
/* 65 */           in.position(in.position() - 1);
/* 66 */           found = false;
/*    */         } 
/* 68 */         finished = true;
/*    */         
/*    */         break;
/*    */       } 
/* 72 */       if (b == 10) {
/* 73 */         found = true;
/* 74 */         finished = true;
/*    */         
/*    */         break;
/*    */       } 
/* 78 */       throw new ProtocolDecoderException("Expected LF after CR but was: " + (b & 0xFF));
/*    */     } 
/*    */ 
/*    */     
/* 82 */     if (finished) {
/* 83 */       this.hasCR = false;
/* 84 */       return finishDecode(found, out);
/*    */     } 
/*    */     
/* 87 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/* 94 */     return finishDecode(false, out);
/*    */   }
/*    */   
/*    */   protected abstract DecodingState finishDecode(boolean paramBoolean, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/CrLfDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */