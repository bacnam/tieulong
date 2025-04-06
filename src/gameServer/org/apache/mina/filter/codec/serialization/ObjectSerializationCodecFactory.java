/*     */ package org.apache.mina.filter.codec.serialization;
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
/*     */ 
/*     */ 
/*     */ public class ObjectSerializationCodecFactory
/*     */   implements ProtocolCodecFactory
/*     */ {
/*     */   private final ObjectSerializationEncoder encoder;
/*     */   private final ObjectSerializationDecoder decoder;
/*     */   
/*     */   public ObjectSerializationCodecFactory() {
/*  45 */     this(Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectSerializationCodecFactory(ClassLoader classLoader) {
/*  52 */     this.encoder = new ObjectSerializationEncoder();
/*  53 */     this.decoder = new ObjectSerializationDecoder(classLoader);
/*     */   }
/*     */   
/*     */   public ProtocolEncoder getEncoder(IoSession session) {
/*  57 */     return (ProtocolEncoder)this.encoder;
/*     */   }
/*     */   
/*     */   public ProtocolDecoder getDecoder(IoSession session) {
/*  61 */     return (ProtocolDecoder)this.decoder;
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
/*     */   public int getEncoderMaxObjectSize() {
/*  73 */     return this.encoder.getMaxObjectSize();
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
/*     */   public void setEncoderMaxObjectSize(int maxObjectSize) {
/*  85 */     this.encoder.setMaxObjectSize(maxObjectSize);
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
/*     */   public int getDecoderMaxObjectSize() {
/*  97 */     return this.decoder.getMaxObjectSize();
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
/*     */   public void setDecoderMaxObjectSize(int maxObjectSize) {
/* 109 */     this.decoder.setMaxObjectSize(maxObjectSize);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/serialization/ObjectSerializationCodecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */