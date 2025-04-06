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
/*     */ public abstract class ConsumeToCrLfDecodingState
/*     */   implements DecodingState
/*     */ {
/*     */   private static final byte CR = 13;
/*     */   private static final byte LF = 10;
/*     */   private boolean lastIsCR;
/*     */   private IoBuffer buffer;
/*     */   
/*     */   public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/*  55 */     int beginPos = in.position();
/*  56 */     int limit = in.limit();
/*  57 */     int terminatorPos = -1;
/*     */     
/*  59 */     for (int i = beginPos; i < limit; i++) {
/*  60 */       byte b = in.get(i);
/*  61 */       if (b == 13) {
/*  62 */         this.lastIsCR = true;
/*     */       } else {
/*  64 */         if (b == 10 && this.lastIsCR) {
/*  65 */           terminatorPos = i;
/*     */           break;
/*     */         } 
/*  68 */         this.lastIsCR = false;
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     if (terminatorPos >= 0) {
/*     */       IoBuffer product;
/*     */       
/*  75 */       int endPos = terminatorPos - 1;
/*     */       
/*  77 */       if (beginPos < endPos) {
/*  78 */         in.limit(endPos);
/*     */         
/*  80 */         if (this.buffer == null) {
/*  81 */           product = in.slice();
/*     */         } else {
/*  83 */           this.buffer.put(in);
/*  84 */           product = this.buffer.flip();
/*  85 */           this.buffer = null;
/*     */         } 
/*     */         
/*  88 */         in.limit(limit);
/*     */       
/*     */       }
/*  91 */       else if (this.buffer == null) {
/*  92 */         product = IoBuffer.allocate(0);
/*     */       } else {
/*  94 */         product = this.buffer.flip();
/*  95 */         this.buffer = null;
/*     */       } 
/*     */       
/*  98 */       in.position(terminatorPos + 1);
/*  99 */       return finishDecode(product, out);
/*     */     } 
/*     */     
/* 102 */     in.position(beginPos);
/*     */     
/* 104 */     if (this.buffer == null) {
/* 105 */       this.buffer = IoBuffer.allocate(in.remaining());
/* 106 */       this.buffer.setAutoExpand(true);
/*     */     } 
/*     */     
/* 109 */     this.buffer.put(in);
/*     */     
/* 111 */     if (this.lastIsCR) {
/* 112 */       this.buffer.position(this.buffer.position() - 1);
/*     */     }
/*     */     
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
/*     */     IoBuffer product;
/* 124 */     if (this.buffer == null) {
/* 125 */       product = IoBuffer.allocate(0);
/*     */     } else {
/* 127 */       product = this.buffer.flip();
/* 128 */       this.buffer = null;
/*     */     } 
/* 130 */     return finishDecode(product, out);
/*     */   }
/*     */   
/*     */   protected abstract DecodingState finishDecode(IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/statemachine/ConsumeToCrLfDecodingState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */