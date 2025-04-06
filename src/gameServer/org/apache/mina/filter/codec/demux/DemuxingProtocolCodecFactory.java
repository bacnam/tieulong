/*     */ package org.apache.mina.filter.codec.demux;
/*     */ 
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
/*     */ public class DemuxingProtocolCodecFactory
/*     */   implements ProtocolCodecFactory
/*     */ {
/*  39 */   private final DemuxingProtocolEncoder encoder = new DemuxingProtocolEncoder();
/*     */   
/*  41 */   private final DemuxingProtocolDecoder decoder = new DemuxingProtocolDecoder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolEncoder getEncoder(IoSession session) throws Exception {
/*  51 */     return this.encoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolDecoder getDecoder(IoSession session) throws Exception {
/*  58 */     return (ProtocolDecoder)this.decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMessageEncoder(Class<?> messageType, Class<? extends MessageEncoder> encoderClass) {
/*  63 */     this.encoder.addMessageEncoder(messageType, encoderClass);
/*     */   }
/*     */   
/*     */   public <T> void addMessageEncoder(Class<T> messageType, MessageEncoder<? super T> encoder) {
/*  67 */     this.encoder.addMessageEncoder(messageType, encoder);
/*     */   }
/*     */   
/*     */   public <T> void addMessageEncoder(Class<T> messageType, MessageEncoderFactory<? super T> factory) {
/*  71 */     this.encoder.addMessageEncoder(messageType, factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMessageEncoder(Iterable<Class<?>> messageTypes, Class<? extends MessageEncoder> encoderClass) {
/*  76 */     for (Class<?> messageType : messageTypes) {
/*  77 */       addMessageEncoder(messageType, encoderClass);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoder<? super T> encoder) {
/*  82 */     for (Class<? extends T> messageType : messageTypes) {
/*  83 */       addMessageEncoder(messageType, encoder);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void addMessageEncoder(Iterable<Class<? extends T>> messageTypes, MessageEncoderFactory<? super T> factory) {
/*  89 */     for (Class<? extends T> messageType : messageTypes) {
/*  90 */       addMessageEncoder(messageType, factory);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addMessageDecoder(Class<? extends MessageDecoder> decoderClass) {
/*  95 */     this.decoder.addMessageDecoder(decoderClass);
/*     */   }
/*     */   
/*     */   public void addMessageDecoder(MessageDecoder decoder) {
/*  99 */     this.decoder.addMessageDecoder(decoder);
/*     */   }
/*     */   
/*     */   public void addMessageDecoder(MessageDecoderFactory factory) {
/* 103 */     this.decoder.addMessageDecoder(factory);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/DemuxingProtocolCodecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */