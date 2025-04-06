/*     */ package org.apache.mina.proxy.utils;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IoBufferDecoder
/*     */ {
/*     */   public class DecodingContext
/*     */   {
/*     */     private IoBuffer decodedBuffer;
/*     */     private IoBuffer delimiter;
/*  55 */     private int matchCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     private int contentLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reset() {
/*  67 */       this.contentLength = -1;
/*  68 */       this.matchCount = 0;
/*  69 */       this.decodedBuffer = null;
/*     */     }
/*     */     
/*     */     public int getContentLength() {
/*  73 */       return this.contentLength;
/*     */     }
/*     */     
/*     */     public void setContentLength(int contentLength) {
/*  77 */       this.contentLength = contentLength;
/*     */     }
/*     */     
/*     */     public int getMatchCount() {
/*  81 */       return this.matchCount;
/*     */     }
/*     */     
/*     */     public void setMatchCount(int matchCount) {
/*  85 */       this.matchCount = matchCount;
/*     */     }
/*     */     
/*     */     public IoBuffer getDecodedBuffer() {
/*  89 */       return this.decodedBuffer;
/*     */     }
/*     */     
/*     */     public void setDecodedBuffer(IoBuffer decodedBuffer) {
/*  93 */       this.decodedBuffer = decodedBuffer;
/*     */     }
/*     */     
/*     */     public IoBuffer getDelimiter() {
/*  97 */       return this.delimiter;
/*     */     }
/*     */     
/*     */     public void setDelimiter(IoBuffer delimiter) {
/* 101 */       this.delimiter = delimiter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private DecodingContext ctx = new DecodingContext();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoBufferDecoder(byte[] delimiter) {
/* 117 */     setDelimiter(delimiter, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoBufferDecoder(int contentLength) {
/* 126 */     setContentLength(contentLength, false);
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
/*     */   public void setContentLength(int contentLength, boolean resetMatchCount) {
/* 139 */     if (contentLength <= 0) {
/* 140 */       throw new IllegalArgumentException("contentLength: " + contentLength);
/*     */     }
/*     */     
/* 143 */     this.ctx.setContentLength(contentLength);
/* 144 */     if (resetMatchCount) {
/* 145 */       this.ctx.setMatchCount(0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimiter(byte[] delim, boolean resetMatchCount) {
/* 161 */     if (delim == null) {
/* 162 */       throw new IllegalArgumentException("Null delimiter not allowed");
/*     */     }
/*     */ 
/*     */     
/* 166 */     IoBuffer delimiter = IoBuffer.allocate(delim.length);
/* 167 */     delimiter.put(delim);
/* 168 */     delimiter.flip();
/*     */     
/* 170 */     this.ctx.setDelimiter(delimiter);
/* 171 */     this.ctx.setContentLength(-1);
/* 172 */     if (resetMatchCount) {
/* 173 */       this.ctx.setMatchCount(0);
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
/*     */   
/*     */   public IoBuffer decodeFully(IoBuffer in) {
/* 186 */     int contentLength = this.ctx.getContentLength();
/* 187 */     IoBuffer decodedBuffer = this.ctx.getDecodedBuffer();
/*     */     
/* 189 */     int oldLimit = in.limit();
/*     */ 
/*     */     
/* 192 */     if (contentLength > -1) {
/* 193 */       if (decodedBuffer == null) {
/* 194 */         decodedBuffer = IoBuffer.allocate(contentLength).setAutoExpand(true);
/*     */       }
/*     */ 
/*     */       
/* 198 */       if (in.remaining() < contentLength) {
/* 199 */         int readBytes = in.remaining();
/* 200 */         decodedBuffer.put(in);
/* 201 */         this.ctx.setDecodedBuffer(decodedBuffer);
/* 202 */         this.ctx.setContentLength(contentLength - readBytes);
/* 203 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 207 */       int newLimit = in.position() + contentLength;
/* 208 */       in.limit(newLimit);
/* 209 */       decodedBuffer.put(in);
/* 210 */       decodedBuffer.flip();
/* 211 */       in.limit(oldLimit);
/* 212 */       this.ctx.reset();
/*     */       
/* 214 */       return decodedBuffer;
/*     */     } 
/*     */ 
/*     */     
/* 218 */     int oldPos = in.position();
/* 219 */     int matchCount = this.ctx.getMatchCount();
/* 220 */     IoBuffer delimiter = this.ctx.getDelimiter();
/*     */     
/* 222 */     while (in.hasRemaining()) {
/* 223 */       byte b = in.get();
/* 224 */       if (delimiter.get(matchCount) == b) {
/* 225 */         matchCount++;
/* 226 */         if (matchCount == delimiter.limit()) {
/*     */           
/* 228 */           int pos = in.position();
/* 229 */           in.position(oldPos);
/*     */           
/* 231 */           in.limit(pos);
/*     */           
/* 233 */           if (decodedBuffer == null) {
/* 234 */             decodedBuffer = IoBuffer.allocate(in.remaining()).setAutoExpand(true);
/*     */           }
/*     */           
/* 237 */           decodedBuffer.put(in);
/* 238 */           decodedBuffer.flip();
/*     */           
/* 240 */           in.limit(oldLimit);
/* 241 */           this.ctx.reset();
/*     */           
/* 243 */           return decodedBuffer;
/*     */         }  continue;
/*     */       } 
/* 246 */       in.position(Math.max(0, in.position() - matchCount));
/* 247 */       matchCount = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 252 */     if (in.remaining() > 0) {
/* 253 */       in.position(oldPos);
/* 254 */       decodedBuffer.put(in);
/* 255 */       in.position(in.limit());
/*     */     } 
/*     */ 
/*     */     
/* 259 */     this.ctx.setMatchCount(matchCount);
/* 260 */     this.ctx.setDecodedBuffer(decodedBuffer);
/*     */     
/* 262 */     return decodedBuffer;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/utils/IoBufferDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */