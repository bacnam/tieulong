/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
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
/*     */ @NotThreadSafe
/*     */ public class SessionOutputBufferImpl
/*     */   extends ExpandableBuffer
/*     */   implements SessionOutputBuffer
/*     */ {
/*  63 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharsetEncoder charencoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int lineBuffersize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharBuffer charbuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int buffersize, int lineBuffersize, CharsetEncoder charencoder, ByteBufferAllocator allocator) {
/*  88 */     super(buffersize, (allocator != null) ? allocator : (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*  89 */     this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
/*  90 */     this.charencoder = charencoder;
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
/*     */   public SessionOutputBufferImpl(int buffersize, int lineBuffersize, ByteBufferAllocator allocator, HttpParams params) {
/* 104 */     super(buffersize, allocator);
/* 105 */     this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
/* 106 */     String charsetName = (String)params.getParameter("http.protocol.element-charset");
/* 107 */     Charset charset = CharsetUtils.lookup(charsetName);
/* 108 */     if (charset != null) {
/* 109 */       this.charencoder = charset.newEncoder();
/* 110 */       CodingErrorAction a1 = (CodingErrorAction)params.getParameter("http.malformed.input.action");
/*     */       
/* 112 */       this.charencoder.onMalformedInput((a1 != null) ? a1 : CodingErrorAction.REPORT);
/* 113 */       CodingErrorAction a2 = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
/*     */       
/* 115 */       this.charencoder.onUnmappableCharacter((a2 != null) ? a2 : CodingErrorAction.REPORT);
/*     */     } else {
/* 117 */       this.charencoder = null;
/*     */     } 
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
/*     */   public SessionOutputBufferImpl(int buffersize, int linebuffersize, HttpParams params) {
/* 130 */     this(buffersize, linebuffersize, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int buffersize) {
/* 137 */     this(buffersize, 256, (CharsetEncoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int buffersize, int linebuffersize, Charset charset) {
/* 147 */     this(buffersize, linebuffersize, (charset != null) ? charset.newEncoder() : null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(int buffersize, int linebuffersize) {
/* 157 */     this(buffersize, linebuffersize, (CharsetEncoder)null, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE);
/*     */   }
/*     */   
/*     */   public void reset(HttpParams params) {
/* 161 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int flush(WritableByteChannel channel) throws IOException {
/* 166 */     Args.notNull(channel, "Channel");
/* 167 */     setOutputMode();
/* 168 */     return channel.write(this.buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ByteBuffer src) {
/* 173 */     if (src == null) {
/*     */       return;
/*     */     }
/* 176 */     setInputMode();
/* 177 */     int requiredCapacity = this.buffer.position() + src.remaining();
/* 178 */     ensureCapacity(requiredCapacity);
/* 179 */     this.buffer.put(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ReadableByteChannel src) throws IOException {
/* 184 */     if (src == null) {
/*     */       return;
/*     */     }
/* 187 */     setInputMode();
/* 188 */     src.read(this.buffer);
/*     */   }
/*     */   
/*     */   private void write(byte[] b) {
/* 192 */     if (b == null) {
/*     */       return;
/*     */     }
/* 195 */     setInputMode();
/* 196 */     int off = 0;
/* 197 */     int len = b.length;
/* 198 */     int requiredCapacity = this.buffer.position() + len;
/* 199 */     ensureCapacity(requiredCapacity);
/* 200 */     this.buffer.put(b, 0, len);
/*     */   }
/*     */   
/*     */   private void writeCRLF() {
/* 204 */     write(CRLF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLine(CharArrayBuffer linebuffer) throws CharacterCodingException {
/* 209 */     if (linebuffer == null) {
/*     */       return;
/*     */     }
/* 212 */     setInputMode();
/*     */     
/* 214 */     if (linebuffer.length() > 0) {
/* 215 */       if (this.charencoder == null) {
/* 216 */         int requiredCapacity = this.buffer.position() + linebuffer.length();
/* 217 */         ensureCapacity(requiredCapacity);
/* 218 */         if (this.buffer.hasArray()) {
/* 219 */           byte[] b = this.buffer.array();
/* 220 */           int len = linebuffer.length();
/* 221 */           int off = this.buffer.position();
/* 222 */           for (int i = 0; i < len; i++) {
/* 223 */             b[off + i] = (byte)linebuffer.charAt(i);
/*     */           }
/* 225 */           this.buffer.position(off + len);
/*     */         } else {
/* 227 */           for (int i = 0; i < linebuffer.length(); i++) {
/* 228 */             this.buffer.put((byte)linebuffer.charAt(i));
/*     */           }
/*     */         } 
/*     */       } else {
/* 232 */         if (this.charbuffer == null) {
/* 233 */           this.charbuffer = CharBuffer.allocate(this.lineBuffersize);
/*     */         }
/* 235 */         this.charencoder.reset();
/*     */         
/* 237 */         int remaining = linebuffer.length();
/* 238 */         int offset = 0;
/* 239 */         while (remaining > 0) {
/* 240 */           int l = this.charbuffer.remaining();
/* 241 */           boolean eol = false;
/* 242 */           if (remaining <= l) {
/* 243 */             l = remaining;
/*     */             
/* 245 */             eol = true;
/*     */           } 
/* 247 */           this.charbuffer.put(linebuffer.buffer(), offset, l);
/* 248 */           this.charbuffer.flip();
/*     */           
/* 250 */           boolean bool1 = true;
/* 251 */           while (bool1) {
/* 252 */             CoderResult result = this.charencoder.encode(this.charbuffer, this.buffer, eol);
/* 253 */             if (result.isError()) {
/* 254 */               result.throwException();
/*     */             }
/* 256 */             if (result.isOverflow()) {
/* 257 */               expand();
/*     */             }
/* 259 */             bool1 = !result.isUnderflow();
/*     */           } 
/* 261 */           this.charbuffer.compact();
/* 262 */           offset += l;
/* 263 */           remaining -= l;
/*     */         } 
/*     */         
/* 266 */         boolean retry = true;
/* 267 */         while (retry) {
/* 268 */           CoderResult result = this.charencoder.flush(this.buffer);
/* 269 */           if (result.isError()) {
/* 270 */             result.throwException();
/*     */           }
/* 272 */           if (result.isOverflow()) {
/* 273 */             expand();
/*     */           }
/* 275 */           retry = !result.isUnderflow();
/*     */         } 
/*     */       } 
/*     */     }
/* 279 */     writeCRLF();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLine(String s) throws IOException {
/* 284 */     if (s == null) {
/*     */       return;
/*     */     }
/* 287 */     if (s.length() > 0) {
/* 288 */       CharArrayBuffer tmp = new CharArrayBuffer(s.length());
/* 289 */       tmp.append(s);
/* 290 */       writeLine(tmp);
/*     */     } else {
/* 292 */       write(CRLF);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/SessionOutputBufferImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */