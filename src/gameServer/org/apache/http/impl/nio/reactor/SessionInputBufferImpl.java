/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.MessageConstraintException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.ExpandableBuffer;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.CharsetUtils;
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
/*     */ @NotThreadSafe
/*     */ public class SessionInputBufferImpl
/*     */   extends ExpandableBuffer
/*     */   implements SessionInputBuffer
/*     */ {
/*     */   private final CharsetDecoder chardecoder;
/*     */   private final MessageConstraints constraints;
/*     */   private final int lineBuffersize;
/*     */   private CharBuffer charbuffer;
/*     */   
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize, MessageConstraints constraints, CharsetDecoder chardecoder, ByteBufferAllocator allocator) {
/*  92 */     super(buffersize, (allocator != null) ? allocator : (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*  93 */     this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
/*  94 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/*  95 */     this.chardecoder = chardecoder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize, CharsetDecoder chardecoder, ByteBufferAllocator allocator) {
/* 116 */     this(buffersize, lineBuffersize, (MessageConstraints)null, chardecoder, allocator);
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
/*     */   @Deprecated
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize, ByteBufferAllocator allocator, HttpParams params) {
/* 130 */     super(buffersize, allocator);
/* 131 */     this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
/* 132 */     String charsetName = (String)params.getParameter("http.protocol.element-charset");
/* 133 */     Charset charset = CharsetUtils.lookup(charsetName);
/* 134 */     if (charset != null) {
/* 135 */       this.chardecoder = charset.newDecoder();
/* 136 */       CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
/*     */       
/* 138 */       this.chardecoder.onMalformedInput((a1 != null) ? a1 : CodingErrorAction.REPORT);
/* 139 */       CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
/*     */       
/* 141 */       this.chardecoder.onUnmappableCharacter((a2 != null) ? a2 : CodingErrorAction.REPORT);
/*     */     } else {
/* 143 */       this.chardecoder = null;
/*     */     } 
/* 145 */     this.constraints = MessageConstraints.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SessionInputBufferImpl(int buffersize, int linebuffersize, HttpParams params) {
/* 157 */     this(buffersize, linebuffersize, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize, Charset charset) {
/* 167 */     this(buffersize, lineBuffersize, (MessageConstraints)null, (charset != null) ? charset.newDecoder() : null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
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
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize, MessageConstraints constraints, Charset charset) {
/* 179 */     this(buffersize, lineBuffersize, constraints, (charset != null) ? charset.newDecoder() : null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int buffersize, int lineBuffersize) {
/* 189 */     this(buffersize, lineBuffersize, (MessageConstraints)null, (CharsetDecoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int buffersize) {
/* 196 */     this(buffersize, 256, (MessageConstraints)null, (CharsetDecoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public int fill(ReadableByteChannel channel) throws IOException {
/* 201 */     Args.notNull(channel, "Channel");
/* 202 */     setInputMode();
/* 203 */     if (!this.buffer.hasRemaining()) {
/* 204 */       expand();
/*     */     }
/* 206 */     return channel.read(this.buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() {
/* 211 */     setOutputMode();
/* 212 */     return this.buffer.get() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, int maxLen) {
/* 217 */     if (dst == null) {
/* 218 */       return 0;
/*     */     }
/* 220 */     setOutputMode();
/* 221 */     int len = Math.min(dst.remaining(), maxLen);
/* 222 */     int chunk = Math.min(this.buffer.remaining(), len);
/* 223 */     if (this.buffer.remaining() > chunk) {
/* 224 */       int oldLimit = this.buffer.limit();
/* 225 */       int newLimit = this.buffer.position() + chunk;
/* 226 */       this.buffer.limit(newLimit);
/* 227 */       dst.put(this.buffer);
/* 228 */       this.buffer.limit(oldLimit);
/* 229 */       return len;
/*     */     } 
/* 231 */     dst.put(this.buffer);
/*     */     
/* 233 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) {
/* 238 */     if (dst == null) {
/* 239 */       return 0;
/*     */     }
/* 241 */     return read(dst, dst.remaining());
/*     */   }
/*     */   
/*     */   public int read(WritableByteChannel dst, int maxLen) throws IOException {
/*     */     int bytesRead;
/* 246 */     if (dst == null) {
/* 247 */       return 0;
/*     */     }
/* 249 */     setOutputMode();
/*     */     
/* 251 */     if (this.buffer.remaining() > maxLen) {
/* 252 */       int oldLimit = this.buffer.limit();
/* 253 */       int newLimit = oldLimit - this.buffer.remaining() - maxLen;
/* 254 */       this.buffer.limit(newLimit);
/* 255 */       bytesRead = dst.write(this.buffer);
/* 256 */       this.buffer.limit(oldLimit);
/*     */     } else {
/* 258 */       bytesRead = dst.write(this.buffer);
/*     */     } 
/* 260 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(WritableByteChannel dst) throws IOException {
/* 265 */     if (dst == null) {
/* 266 */       return 0;
/*     */     }
/* 268 */     setOutputMode();
/* 269 */     return dst.write(this.buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readLine(CharArrayBuffer linebuffer, boolean endOfStream) throws CharacterCodingException {
/* 277 */     setOutputMode();
/*     */     
/* 279 */     int pos = -1;
/* 280 */     for (int i = this.buffer.position(); i < this.buffer.limit(); i++) {
/* 281 */       int b = this.buffer.get(i);
/* 282 */       if (b == 10) {
/* 283 */         pos = i + 1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 288 */     int maxLineLen = this.constraints.getMaxLineLength();
/* 289 */     if (maxLineLen > 0) {
/* 290 */       int currentLen = ((pos > 0) ? pos : this.buffer.limit()) - this.buffer.position();
/* 291 */       if (currentLen >= maxLineLen) {
/* 292 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/*     */     } 
/*     */     
/* 296 */     if (pos == -1) {
/* 297 */       if (endOfStream && this.buffer.hasRemaining()) {
/*     */         
/* 299 */         pos = this.buffer.limit();
/*     */       }
/*     */       else {
/*     */         
/* 303 */         return false;
/*     */       } 
/*     */     }
/* 306 */     int origLimit = this.buffer.limit();
/* 307 */     this.buffer.limit(pos);
/*     */     
/* 309 */     int requiredCapacity = this.buffer.limit() - this.buffer.position();
/*     */     
/* 311 */     linebuffer.ensureCapacity(requiredCapacity);
/*     */     
/* 313 */     if (this.chardecoder == null) {
/* 314 */       if (this.buffer.hasArray()) {
/* 315 */         byte[] b = this.buffer.array();
/* 316 */         int off = this.buffer.position();
/* 317 */         int len = this.buffer.remaining();
/* 318 */         linebuffer.append(b, off, len);
/* 319 */         this.buffer.position(off + len);
/*     */       } else {
/* 321 */         while (this.buffer.hasRemaining())
/* 322 */           linebuffer.append((char)(this.buffer.get() & 0xFF)); 
/*     */       } 
/*     */     } else {
/*     */       CoderResult result;
/* 326 */       if (this.charbuffer == null) {
/* 327 */         this.charbuffer = CharBuffer.allocate(this.lineBuffersize);
/*     */       }
/* 329 */       this.chardecoder.reset();
/*     */       
/*     */       do {
/* 332 */         result = this.chardecoder.decode(this.buffer, this.charbuffer, true);
/*     */ 
/*     */ 
/*     */         
/* 336 */         if (result.isError()) {
/* 337 */           result.throwException();
/*     */         }
/* 339 */         if (!result.isOverflow())
/* 340 */           continue;  this.charbuffer.flip();
/* 341 */         linebuffer.append(this.charbuffer.array(), this.charbuffer.position(), this.charbuffer.remaining());
/*     */ 
/*     */ 
/*     */         
/* 345 */         this.charbuffer.clear();
/*     */       }
/* 347 */       while (!result.isUnderflow());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 353 */       this.chardecoder.flush(this.charbuffer);
/* 354 */       this.charbuffer.flip();
/*     */       
/* 356 */       if (this.charbuffer.hasRemaining()) {
/* 357 */         linebuffer.append(this.charbuffer.array(), this.charbuffer.position(), this.charbuffer.remaining());
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     this.buffer.limit(origLimit);
/*     */ 
/*     */     
/* 367 */     int l = linebuffer.length();
/* 368 */     if (l > 0) {
/* 369 */       if (linebuffer.charAt(l - 1) == '\n') {
/* 370 */         l--;
/* 371 */         linebuffer.setLength(l);
/*     */       } 
/*     */       
/* 374 */       if (l > 0 && 
/* 375 */         linebuffer.charAt(l - 1) == '\r') {
/* 376 */         l--;
/* 377 */         linebuffer.setLength(l);
/*     */       } 
/*     */     } 
/*     */     
/* 381 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String readLine(boolean endOfStream) throws CharacterCodingException {
/* 386 */     CharArrayBuffer buffer = new CharArrayBuffer(64);
/* 387 */     boolean found = readLine(buffer, endOfStream);
/* 388 */     if (found) {
/* 389 */       return buffer.toString();
/*     */     }
/* 391 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/SessionInputBufferImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */