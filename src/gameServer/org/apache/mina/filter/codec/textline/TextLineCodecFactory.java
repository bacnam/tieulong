/*     */ package org.apache.mina.filter.codec.textline;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolCodecFactory;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoder;
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
/*     */ public class TextLineCodecFactory
/*     */   implements ProtocolCodecFactory
/*     */ {
/*     */   private final TextLineEncoder encoder;
/*     */   private final TextLineDecoder decoder;
/*     */   
/*     */   public TextLineCodecFactory() {
/*  47 */     this(Charset.defaultCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineCodecFactory(Charset charset) {
/*  59 */     this.encoder = new TextLineEncoder(charset, LineDelimiter.UNIX);
/*  60 */     this.decoder = new TextLineDecoder(charset, LineDelimiter.AUTO);
/*     */   }
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
/*     */   public TextLineCodecFactory(Charset charset, String encodingDelimiter, String decodingDelimiter) {
/*  75 */     this.encoder = new TextLineEncoder(charset, encodingDelimiter);
/*  76 */     this.decoder = new TextLineDecoder(charset, decodingDelimiter);
/*     */   }
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
/*     */   public TextLineCodecFactory(Charset charset, LineDelimiter encodingDelimiter, LineDelimiter decodingDelimiter) {
/*  91 */     this.encoder = new TextLineEncoder(charset, encodingDelimiter);
/*  92 */     this.decoder = new TextLineDecoder(charset, decodingDelimiter);
/*     */   }
/*     */   
/*     */   public ProtocolEncoder getEncoder(IoSession session) {
/*  96 */     return (ProtocolEncoder)this.encoder;
/*     */   }
/*     */   
/*     */   public ProtocolDecoder getDecoder(IoSession session) {
/* 100 */     return this.decoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEncoderMaxLineLength() {
/* 112 */     return this.encoder.getMaxLineLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoderMaxLineLength(int maxLineLength) {
/* 124 */     this.encoder.setMaxLineLength(maxLineLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDecoderMaxLineLength() {
/* 136 */     return this.decoder.getMaxLineLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDecoderMaxLineLength(int maxLineLength) {
/* 148 */     this.decoder.setMaxLineLength(maxLineLength);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/textline/TextLineCodecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */