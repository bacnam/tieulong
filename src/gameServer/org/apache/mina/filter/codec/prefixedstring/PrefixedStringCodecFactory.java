/*     */ package org.apache.mina.filter.codec.prefixedstring;
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
/*     */ public class PrefixedStringCodecFactory
/*     */   implements ProtocolCodecFactory
/*     */ {
/*     */   private final PrefixedStringEncoder encoder;
/*     */   private final PrefixedStringDecoder decoder;
/*     */   
/*     */   public PrefixedStringCodecFactory(Charset charset) {
/*  43 */     this.encoder = new PrefixedStringEncoder(charset);
/*  44 */     this.decoder = new PrefixedStringDecoder(charset);
/*     */   }
/*     */   
/*     */   public PrefixedStringCodecFactory() {
/*  48 */     this(Charset.defaultCharset());
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
/*     */   public int getEncoderMaxDataLength() {
/*  62 */     return this.encoder.getMaxDataLength();
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
/*     */   public void setEncoderMaxDataLength(int maxDataLength) {
/*  76 */     this.encoder.setMaxDataLength(maxDataLength);
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
/*     */   public int getDecoderMaxDataLength() {
/*  89 */     return this.decoder.getMaxDataLength();
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
/*     */ 
/*     */   
/*     */   public void setDecoderMaxDataLength(int maxDataLength) {
/* 106 */     this.decoder.setMaxDataLength(maxDataLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDecoderPrefixLength(int prefixLength) {
/* 115 */     this.decoder.setPrefixLength(prefixLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDecoderPrefixLength() {
/* 124 */     return this.decoder.getPrefixLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoderPrefixLength(int prefixLength) {
/* 133 */     this.encoder.setPrefixLength(prefixLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEncoderPrefixLength() {
/* 142 */     return this.encoder.getPrefixLength();
/*     */   }
/*     */   
/*     */   public ProtocolEncoder getEncoder(IoSession session) throws Exception {
/* 146 */     return (ProtocolEncoder)this.encoder;
/*     */   }
/*     */   
/*     */   public ProtocolDecoder getDecoder(IoSession session) throws Exception {
/* 150 */     return (ProtocolDecoder)this.decoder;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/prefixedstring/PrefixedStringCodecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */