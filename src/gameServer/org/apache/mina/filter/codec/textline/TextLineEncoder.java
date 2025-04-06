/*     */ package org.apache.mina.filter.codec.textline;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.AttributeKey;
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
/*     */ public class TextLineEncoder
/*     */   extends ProtocolEncoderAdapter
/*     */ {
/*  39 */   private static final AttributeKey ENCODER = new AttributeKey(TextLineEncoder.class, "encoder");
/*     */   
/*     */   private final Charset charset;
/*     */   
/*     */   private final LineDelimiter delimiter;
/*     */   
/*  45 */   private int maxLineLength = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder() {
/*  52 */     this(Charset.defaultCharset(), LineDelimiter.UNIX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder(String delimiter) {
/*  60 */     this(new LineDelimiter(delimiter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder(LineDelimiter delimiter) {
/*  68 */     this(Charset.defaultCharset(), delimiter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder(Charset charset) {
/*  76 */     this(charset, LineDelimiter.UNIX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder(Charset charset, String delimiter) {
/*  84 */     this(charset, new LineDelimiter(delimiter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineEncoder(Charset charset, LineDelimiter delimiter) {
/*  92 */     if (charset == null) {
/*  93 */       throw new IllegalArgumentException("charset");
/*     */     }
/*  95 */     if (delimiter == null) {
/*  96 */       throw new IllegalArgumentException("delimiter");
/*     */     }
/*  98 */     if (LineDelimiter.AUTO.equals(delimiter)) {
/*  99 */       throw new IllegalArgumentException("AUTO delimiter is not allowed for encoder.");
/*     */     }
/*     */     
/* 102 */     this.charset = charset;
/* 103 */     this.delimiter = delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLineLength() {
/* 113 */     return this.maxLineLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLineLength(int maxLineLength) {
/* 123 */     if (maxLineLength <= 0) {
/* 124 */       throw new IllegalArgumentException("maxLineLength: " + maxLineLength);
/*     */     }
/*     */     
/* 127 */     this.maxLineLength = maxLineLength;
/*     */   }
/*     */   
/*     */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 131 */     CharsetEncoder encoder = (CharsetEncoder)session.getAttribute(ENCODER);
/*     */     
/* 133 */     if (encoder == null) {
/* 134 */       encoder = this.charset.newEncoder();
/* 135 */       session.setAttribute(ENCODER, encoder);
/*     */     } 
/*     */     
/* 138 */     String value = (message == null) ? "" : message.toString();
/* 139 */     IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
/* 140 */     buf.putString(value, encoder);
/*     */     
/* 142 */     if (buf.position() > this.maxLineLength) {
/* 143 */       throw new IllegalArgumentException("Line length: " + buf.position());
/*     */     }
/*     */     
/* 146 */     buf.putString(this.delimiter.getValue(), encoder);
/* 147 */     buf.flip();
/* 148 */     out.write(buf);
/*     */   }
/*     */   
/*     */   public void dispose() throws Exception {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/textline/TextLineEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */