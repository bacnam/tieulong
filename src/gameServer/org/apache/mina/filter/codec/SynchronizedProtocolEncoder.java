/*    */ package org.apache.mina.filter.codec;
/*    */ 
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
/*    */ public class SynchronizedProtocolEncoder
/*    */   implements ProtocolEncoder
/*    */ {
/*    */   private final ProtocolEncoder encoder;
/*    */   
/*    */   public SynchronizedProtocolEncoder(ProtocolEncoder encoder) {
/* 41 */     if (encoder == null) {
/* 42 */       throw new IllegalArgumentException("encoder");
/*    */     }
/* 44 */     this.encoder = encoder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtocolEncoder getEncoder() {
/* 51 */     return this.encoder;
/*    */   }
/*    */   
/*    */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 55 */     synchronized (this.encoder) {
/* 56 */       this.encoder.encode(session, message, out);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void dispose(IoSession session) throws Exception {
/* 61 */     synchronized (this.encoder) {
/* 62 */       this.encoder.dispose(session);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/SynchronizedProtocolEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */