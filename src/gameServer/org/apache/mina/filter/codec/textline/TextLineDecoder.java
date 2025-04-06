/*     */ package org.apache.mina.filter.codec.textline;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderException;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
/*     */ import org.apache.mina.filter.codec.RecoverableProtocolDecoderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextLineDecoder
/*     */   implements ProtocolDecoder
/*     */ {
/*  43 */   private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */   
/*     */   private final LineDelimiter delimiter;
/*     */ 
/*     */   
/*     */   private IoBuffer delimBuf;
/*     */   
/*  54 */   private int maxLineLength = 1024;
/*     */ 
/*     */   
/*  57 */   private int bufferLength = 128;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder() {
/*  64 */     this(LineDelimiter.AUTO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder(String delimiter) {
/*  72 */     this(new LineDelimiter(delimiter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder(LineDelimiter delimiter) {
/*  80 */     this(Charset.defaultCharset(), delimiter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder(Charset charset) {
/*  88 */     this(charset, LineDelimiter.AUTO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder(Charset charset, String delimiter) {
/*  96 */     this(charset, new LineDelimiter(delimiter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextLineDecoder(Charset charset, LineDelimiter delimiter) {
/* 104 */     if (charset == null) {
/* 105 */       throw new IllegalArgumentException("charset parameter shuld not be null");
/*     */     }
/*     */     
/* 108 */     if (delimiter == null) {
/* 109 */       throw new IllegalArgumentException("delimiter parameter should not be null");
/*     */     }
/*     */     
/* 112 */     this.charset = charset;
/* 113 */     this.delimiter = delimiter;
/*     */ 
/*     */     
/* 116 */     if (this.delimBuf == null) {
/* 117 */       IoBuffer tmp = IoBuffer.allocate(2).setAutoExpand(true);
/*     */       
/*     */       try {
/* 120 */         tmp.putString(delimiter.getValue(), charset.newEncoder());
/* 121 */       } catch (CharacterCodingException cce) {}
/*     */ 
/*     */ 
/*     */       
/* 125 */       tmp.flip();
/* 126 */       this.delimBuf = tmp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLineLength() {
/* 137 */     return this.maxLineLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLineLength(int maxLineLength) {
/* 147 */     if (maxLineLength <= 0) {
/* 148 */       throw new IllegalArgumentException("maxLineLength (" + maxLineLength + ") should be a positive value");
/*     */     }
/*     */     
/* 151 */     this.maxLineLength = maxLineLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferLength(int bufferLength) {
/* 161 */     if (bufferLength <= 0) {
/* 162 */       throw new IllegalArgumentException("bufferLength (" + this.maxLineLength + ") should be a positive value");
/*     */     }
/*     */ 
/*     */     
/* 166 */     this.bufferLength = bufferLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferLength() {
/* 174 */     return this.bufferLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 181 */     Context ctx = getContext(session);
/*     */     
/* 183 */     if (LineDelimiter.AUTO.equals(this.delimiter)) {
/* 184 */       decodeAuto(ctx, session, in, out);
/*     */     } else {
/* 186 */       decodeNormal(ctx, session, in, out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Context getContext(IoSession session) {
/* 195 */     Context ctx = (Context)session.getAttribute(this.CONTEXT);
/*     */     
/* 197 */     if (ctx == null) {
/* 198 */       ctx = new Context(this.bufferLength);
/* 199 */       session.setAttribute(this.CONTEXT, ctx);
/*     */     } 
/*     */     
/* 202 */     return ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose(IoSession session) throws Exception {
/* 216 */     Context ctx = (Context)session.getAttribute(this.CONTEXT);
/*     */     
/* 218 */     if (ctx != null) {
/* 219 */       session.removeAttribute(this.CONTEXT);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decodeAuto(Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException, ProtocolDecoderException {
/* 228 */     int matchCount = ctx.getMatchCount();
/*     */ 
/*     */     
/* 231 */     int oldPos = in.position();
/* 232 */     int oldLimit = in.limit();
/*     */     
/* 234 */     while (in.hasRemaining()) {
/* 235 */       byte b = in.get();
/* 236 */       boolean matched = false;
/*     */       
/* 238 */       switch (b) {
/*     */ 
/*     */         
/*     */         case 13:
/* 242 */           matchCount++;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 10:
/* 247 */           matchCount++;
/* 248 */           matched = true;
/*     */           break;
/*     */         
/*     */         default:
/* 252 */           matchCount = 0;
/*     */           break;
/*     */       } 
/* 255 */       if (matched) {
/*     */         
/* 257 */         int pos = in.position();
/* 258 */         in.limit(pos);
/* 259 */         in.position(oldPos);
/*     */         
/* 261 */         ctx.append(in);
/*     */         
/* 263 */         in.limit(oldLimit);
/* 264 */         in.position(pos);
/*     */         
/* 266 */         if (ctx.getOverflowPosition() == 0) {
/* 267 */           IoBuffer buf = ctx.getBuffer();
/* 268 */           buf.flip();
/* 269 */           buf.limit(buf.limit() - matchCount);
/*     */           
/*     */           try {
/* 272 */             byte[] data = new byte[buf.limit()];
/* 273 */             buf.get(data);
/* 274 */             CharsetDecoder decoder = ctx.getDecoder();
/*     */             
/* 276 */             CharBuffer buffer = decoder.decode(ByteBuffer.wrap(data));
/* 277 */             String str = buffer.toString();
/* 278 */             writeText(session, str, out);
/*     */           } finally {
/* 280 */             buf.clear();
/*     */           } 
/*     */         } else {
/* 283 */           int overflowPosition = ctx.getOverflowPosition();
/* 284 */           ctx.reset();
/* 285 */           throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
/*     */         } 
/*     */         
/* 288 */         oldPos = pos;
/* 289 */         matchCount = 0;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 294 */     in.position(oldPos);
/* 295 */     ctx.append(in);
/*     */     
/* 297 */     ctx.setMatchCount(matchCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decodeNormal(Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException, ProtocolDecoderException {
/* 305 */     int matchCount = ctx.getMatchCount();
/*     */ 
/*     */     
/* 308 */     int oldPos = in.position();
/* 309 */     int oldLimit = in.limit();
/*     */     
/* 311 */     while (in.hasRemaining()) {
/* 312 */       byte b = in.get();
/*     */       
/* 314 */       if (this.delimBuf.get(matchCount) == b) {
/* 315 */         matchCount++;
/*     */         
/* 317 */         if (matchCount == this.delimBuf.limit()) {
/*     */           
/* 319 */           int pos = in.position();
/* 320 */           in.limit(pos);
/* 321 */           in.position(oldPos);
/*     */           
/* 323 */           ctx.append(in);
/*     */           
/* 325 */           in.limit(oldLimit);
/* 326 */           in.position(pos);
/*     */           
/* 328 */           if (ctx.getOverflowPosition() == 0) {
/* 329 */             IoBuffer buf = ctx.getBuffer();
/* 330 */             buf.flip();
/* 331 */             buf.limit(buf.limit() - matchCount);
/*     */             
/*     */             try {
/* 334 */               writeText(session, buf.getString(ctx.getDecoder()), out);
/*     */             } finally {
/* 336 */               buf.clear();
/*     */             } 
/*     */           } else {
/* 339 */             int overflowPosition = ctx.getOverflowPosition();
/* 340 */             ctx.reset();
/* 341 */             throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
/*     */           } 
/*     */           
/* 344 */           oldPos = pos;
/* 345 */           matchCount = 0;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 349 */       in.position(Math.max(0, in.position() - matchCount));
/* 350 */       matchCount = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 355 */     in.position(oldPos);
/* 356 */     ctx.append(in);
/*     */     
/* 358 */     ctx.setMatchCount(matchCount);
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
/*     */   protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
/* 371 */     out.write(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Context
/*     */   {
/*     */     private final CharsetDecoder decoder;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final IoBuffer buf;
/*     */ 
/*     */ 
/*     */     
/* 389 */     private int matchCount = 0;
/*     */ 
/*     */     
/* 392 */     private int overflowPosition = 0;
/*     */ 
/*     */     
/*     */     private Context(int bufferLength) {
/* 396 */       this.decoder = TextLineDecoder.this.charset.newDecoder();
/* 397 */       this.buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
/*     */     }
/*     */     
/*     */     public CharsetDecoder getDecoder() {
/* 401 */       return this.decoder;
/*     */     }
/*     */     
/*     */     public IoBuffer getBuffer() {
/* 405 */       return this.buf;
/*     */     }
/*     */     
/*     */     public int getOverflowPosition() {
/* 409 */       return this.overflowPosition;
/*     */     }
/*     */     
/*     */     public int getMatchCount() {
/* 413 */       return this.matchCount;
/*     */     }
/*     */     
/*     */     public void setMatchCount(int matchCount) {
/* 417 */       this.matchCount = matchCount;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 421 */       this.overflowPosition = 0;
/* 422 */       this.matchCount = 0;
/* 423 */       this.decoder.reset();
/*     */     }
/*     */     
/*     */     public void append(IoBuffer in) {
/* 427 */       if (this.overflowPosition != 0) {
/* 428 */         discard(in);
/* 429 */       } else if (this.buf.position() > TextLineDecoder.this.maxLineLength - in.remaining()) {
/* 430 */         this.overflowPosition = this.buf.position();
/* 431 */         this.buf.clear();
/* 432 */         discard(in);
/*     */       } else {
/* 434 */         getBuffer().put(in);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void discard(IoBuffer in) {
/* 439 */       if (Integer.MAX_VALUE - in.remaining() < this.overflowPosition) {
/* 440 */         this.overflowPosition = Integer.MAX_VALUE;
/*     */       } else {
/* 442 */         this.overflowPosition += in.remaining();
/*     */       } 
/*     */       
/* 445 */       in.position(in.limit());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/textline/TextLineDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */