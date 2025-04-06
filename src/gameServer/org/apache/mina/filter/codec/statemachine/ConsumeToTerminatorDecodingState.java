/*     */ package org.apache.mina.filter.codec.statemachine;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
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
/*     */ public abstract class ConsumeToTerminatorDecodingState
/*     */   implements DecodingState
/*     */ {
/*     */   private final byte terminator;
/*     */   private IoBuffer buffer;
/*     */   
/*     */   public ConsumeToTerminatorDecodingState(byte terminator) {
/*  43 */     this.terminator = terminator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/*  50 */     int terminatorPos = in.indexOf(this.terminator);
/*     */     
/*  52 */     if (terminatorPos >= 0) {
/*  53 */       IoBuffer product; int limit = in.limit();
/*     */ 
/*     */       
/*  56 */       if (in.position() < terminatorPos) {
/*  57 */         in.limit(terminatorPos);
/*     */         
/*  59 */         if (this.buffer == null) {
/*  60 */           product = in.slice();
/*     */         } else {
/*  62 */           this.buffer.put(in);
/*  63 */           product = this.buffer.flip();
/*  64 */           this.buffer = null;
/*     */         } 
/*     */         
/*  67 */         in.limit(limit);
/*     */       
/*     */       }
/*  70 */       else if (this.buffer == null) {
/*  71 */         product = IoBuffer.allocate(0);
/*     */       } else {
/*  73 */         product = this.buffer.flip();
/*  74 */         this.buffer = null;
/*     */       } 
/*     */       
/*  77 */       in.position(terminatorPos + 1);
/*  78 */       return finishDecode(product, out);
/*     */     } 
/*     */     
/*  81 */     if (this.buffer == null) {
/*  82 */       this.buffer = IoBuffer.allocate(in.remaining());
/*  83 */       this.buffer.setAutoExpand(true);
/*     */     } 
/*     */     
/*  86 */     this.buffer.put(in);
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/*     */     IoBuffer product;
/*  96 */     if (this.buffer == null) {
/*  97 */       product = IoBuffer.allocate(0);
/*     */     } else {
/*  99 */       product = this.buffer.flip();
/* 100 */       this.buffer = null;
/*     */     } 
/* 102 */     return finishDecode(product, out);
/*     */   }
/*     */   
/*     */   protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ConsumeToTerminatorDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */