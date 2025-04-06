/*    */ package org.apache.mina.filter.codec;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
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
/*    */ public class SynchronizedProtocolDecoder
/*    */   implements ProtocolDecoder
/*    */ {
/*    */   private final ProtocolDecoder decoder;
/*    */   
/*    */   public SynchronizedProtocolDecoder(ProtocolDecoder decoder) {
/* 43 */     if (decoder == null) {
/* 44 */       throw new IllegalArgumentException("decoder");
/*    */     }
/* 46 */     this.decoder = decoder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtocolDecoder getDecoder() {
/* 53 */     return this.decoder;
/*    */   }
/*    */   
/*    */   public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 57 */     synchronized (this.decoder) {
/* 58 */       this.decoder.decode(session, in, out);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
/* 63 */     synchronized (this.decoder) {
/* 64 */       this.decoder.finishDecode(session, out);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void dispose(IoSession session) throws Exception {
/* 69 */     synchronized (this.decoder) {
/* 70 */       this.decoder.dispose(session);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/SynchronizedProtocolDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */