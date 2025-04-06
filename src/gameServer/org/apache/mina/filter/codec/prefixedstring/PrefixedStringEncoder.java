/*     */ package org.apache.mina.filter.codec.prefixedstring;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoderOutput;
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
/*     */ public class PrefixedStringEncoder
/*     */   extends ProtocolEncoderAdapter
/*     */ {
/*     */   public static final int DEFAULT_PREFIX_LENGTH = 4;
/*     */   public static final int DEFAULT_MAX_DATA_LENGTH = 2048;
/*     */   private final Charset charset;
/*  44 */   private int prefixLength = 4;
/*     */   
/*  46 */   private int maxDataLength = 2048;
/*     */   
/*     */   public PrefixedStringEncoder(Charset charset, int prefixLength, int maxDataLength) {
/*  49 */     this.charset = charset;
/*  50 */     this.prefixLength = prefixLength;
/*  51 */     this.maxDataLength = maxDataLength;
/*     */   }
/*     */   
/*     */   public PrefixedStringEncoder(Charset charset, int prefixLength) {
/*  55 */     this(charset, prefixLength, 2048);
/*     */   }
/*     */   
/*     */   public PrefixedStringEncoder(Charset charset) {
/*  59 */     this(charset, 4);
/*     */   }
/*     */   
/*     */   public PrefixedStringEncoder() {
/*  63 */     this(Charset.defaultCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefixLength(int prefixLength) {
/*  72 */     if (prefixLength != 1 && prefixLength != 2 && prefixLength != 4) {
/*  73 */       throw new IllegalArgumentException("prefixLength: " + prefixLength);
/*     */     }
/*  75 */     this.prefixLength = prefixLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrefixLength() {
/*  84 */     return this.prefixLength;
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
/*  99 */     this.maxDataLength = maxDataLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxDataLength() {
/* 108 */     return this.maxDataLength;
/*     */   }
/*     */   
/*     */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 112 */     String value = (String)message;
/* 113 */     IoBuffer buffer = IoBuffer.allocate(value.length()).setAutoExpand(true);
/* 114 */     buffer.putPrefixedString(value, this.prefixLength, this.charset.newEncoder());
/* 115 */     if (buffer.position() > this.maxDataLength) {
/* 116 */       throw new IllegalArgumentException("Data length: " + buffer.position());
/*     */     }
/* 118 */     buffer.flip();
/* 119 */     out.write(buffer);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/prefixedstring/PrefixedStringEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */