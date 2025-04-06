/*     */ package org.apache.mina.filter.codec;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.future.DefaultWriteFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.DummySession;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtocolCodecSession
/*     */   extends DummySession
/*     */ {
/*  62 */   private final WriteFuture notWrittenFuture = DefaultWriteFuture.newNotWrittenFuture((IoSession)this, new UnsupportedOperationException());
/*     */ 
/*     */   
/*  65 */   private final AbstractProtocolEncoderOutput encoderOutput = new AbstractProtocolEncoderOutput() {
/*     */       public WriteFuture flush() {
/*  67 */         return ProtocolCodecSession.this.notWrittenFuture;
/*     */       }
/*     */     };
/*     */   
/*  71 */   private final AbstractProtocolDecoderOutput decoderOutput = new AbstractProtocolDecoderOutput()
/*     */     {
/*     */       public void flush(IoFilter.NextFilter nextFilter, IoSession session) {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolEncoderOutput getEncoderOutput() {
/*  89 */     return this.encoderOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<Object> getEncoderOutputQueue() {
/*  96 */     return this.encoderOutput.getMessageQueue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolDecoderOutput getDecoderOutput() {
/* 104 */     return this.decoderOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<Object> getDecoderOutputQueue() {
/* 111 */     return this.decoderOutput.getMessageQueue();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/ProtocolCodecSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */