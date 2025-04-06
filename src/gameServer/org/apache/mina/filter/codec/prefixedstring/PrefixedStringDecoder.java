/*     */ package org.apache.mina.filter.codec.prefixedstring;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
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
/*     */ public class PrefixedStringDecoder
/*     */   extends CumulativeProtocolDecoder
/*     */ {
/*     */   public static final int DEFAULT_PREFIX_LENGTH = 4;
/*     */   public static final int DEFAULT_MAX_DATA_LENGTH = 2048;
/*     */   private final Charset charset;
/*  44 */   private int prefixLength = 4;
/*     */   
/*  46 */   private int maxDataLength = 2048;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrefixedStringDecoder(Charset charset, int prefixLength, int maxDataLength) {
/*  54 */     this.charset = charset;
/*  55 */     this.prefixLength = prefixLength;
/*  56 */     this.maxDataLength = maxDataLength;
/*     */   }
/*     */   
/*     */   public PrefixedStringDecoder(Charset charset, int prefixLength) {
/*  60 */     this(charset, prefixLength, 2048);
/*     */   }
/*     */   
/*     */   public PrefixedStringDecoder(Charset charset) {
/*  64 */     this(charset, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixLength(int prefixLength) {
/*  73 */     this.prefixLength = prefixLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrefixLength() {
/*  82 */     return this.prefixLength;
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
/*     */   public void setMaxDataLength(int maxDataLength) {
/*  97 */     this.maxDataLength = maxDataLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxDataLength() {
/* 106 */     return this.maxDataLength;
/*     */   }
/*     */   
/*     */   protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 110 */     if (in.prefixedDataAvailable(this.prefixLength, this.maxDataLength)) {
/* 111 */       String msg = in.getPrefixedString(this.prefixLength, this.charset.newDecoder());
/* 112 */       out.write(msg);
/* 113 */       return true;
/*     */     } 
/*     */     
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/prefixedstring/PrefixedStringDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */