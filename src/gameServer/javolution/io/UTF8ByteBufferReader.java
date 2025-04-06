/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UTF8ByteBufferReader
/*     */   extends Reader
/*     */ {
/*     */   private ByteBuffer _byteBuffer;
/*     */   private int _code;
/*     */   private int _moreBytes;
/*     */   
/*     */   public UTF8ByteBufferReader setInput(ByteBuffer byteBuffer) {
/*  61 */     if (this._byteBuffer != null)
/*  62 */       throw new IllegalStateException("Reader not closed or reset"); 
/*  63 */     this._byteBuffer = byteBuffer;
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/*  75 */     if (this._byteBuffer != null) {
/*  76 */       return this._byteBuffer.hasRemaining();
/*     */     }
/*  78 */     throw new IOException("Reader closed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  88 */     if (this._byteBuffer != null) {
/*  89 */       reset();
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
/*     */   public int read() throws IOException {
/* 103 */     if (this._byteBuffer != null) {
/* 104 */       if (this._byteBuffer.hasRemaining()) {
/* 105 */         byte b = this._byteBuffer.get();
/* 106 */         return (b >= 0) ? b : read2(b);
/*     */       } 
/* 108 */       return -1;
/*     */     } 
/*     */     
/* 111 */     throw new IOException("Reader closed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int read2(byte b) throws IOException {
/*     */     try {
/* 119 */       if (b >= 0 && this._moreBytes == 0)
/*     */       {
/* 121 */         return b; } 
/* 122 */       if ((b & 0xC0) == 128 && this._moreBytes != 0) {
/*     */         
/* 124 */         this._code = this._code << 6 | b & 0x3F;
/* 125 */         if (--this._moreBytes == 0) {
/* 126 */           return this._code;
/*     */         }
/* 128 */         return read2(this._byteBuffer.get());
/*     */       } 
/* 130 */       if ((b & 0xE0) == 192 && this._moreBytes == 0) {
/*     */         
/* 132 */         this._code = b & 0x1F;
/* 133 */         this._moreBytes = 1;
/* 134 */         return read2(this._byteBuffer.get());
/* 135 */       }  if ((b & 0xF0) == 224 && this._moreBytes == 0) {
/*     */         
/* 137 */         this._code = b & 0xF;
/* 138 */         this._moreBytes = 2;
/* 139 */         return read2(this._byteBuffer.get());
/* 140 */       }  if ((b & 0xF8) == 240 && this._moreBytes == 0) {
/*     */         
/* 142 */         this._code = b & 0x7;
/* 143 */         this._moreBytes = 3;
/* 144 */         return read2(this._byteBuffer.get());
/* 145 */       }  if ((b & 0xFC) == 248 && this._moreBytes == 0) {
/*     */         
/* 147 */         this._code = b & 0x3;
/* 148 */         this._moreBytes = 4;
/* 149 */         return read2(this._byteBuffer.get());
/* 150 */       }  if ((b & 0xFE) == 252 && this._moreBytes == 0) {
/*     */         
/* 152 */         this._code = b & 0x1;
/* 153 */         this._moreBytes = 5;
/* 154 */         return read2(this._byteBuffer.get());
/*     */       } 
/* 156 */       throw new CharConversionException("Invalid UTF-8 Encoding");
/*     */     }
/* 158 */     catch (BufferUnderflowException e) {
/* 159 */       throw new CharConversionException("Incomplete Sequence");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 182 */     if (this._byteBuffer == null)
/* 183 */       throw new IOException("Reader closed"); 
/* 184 */     int off_plus_len = off + len;
/* 185 */     int remaining = this._byteBuffer.remaining();
/* 186 */     if (remaining <= 0)
/* 187 */       return -1; 
/* 188 */     for (int i = off; i < off_plus_len; ) {
/* 189 */       if (remaining-- > 0) {
/* 190 */         byte b = this._byteBuffer.get();
/* 191 */         if (b >= 0) {
/* 192 */           cbuf[i++] = (char)b; continue;
/*     */         } 
/* 194 */         if (i < off_plus_len - 1) {
/* 195 */           int code = read2(b);
/* 196 */           remaining = this._byteBuffer.remaining();
/* 197 */           if (code < 65536) {
/* 198 */             cbuf[i++] = (char)code; continue;
/* 199 */           }  if (code <= 1114111) {
/* 200 */             cbuf[i++] = (char)((code - 65536 >> 10) + 55296);
/* 201 */             cbuf[i++] = (char)((code - 65536 & 0x3FF) + 56320); continue;
/*     */           } 
/* 203 */           throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 209 */         this._byteBuffer.position(this._byteBuffer.position() - 1);
/* 210 */         remaining++;
/* 211 */         return i - off;
/*     */       } 
/*     */ 
/*     */       
/* 215 */       return i - off;
/*     */     } 
/*     */     
/* 218 */     return len;
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
/*     */   public void read(Appendable dest) throws IOException {
/* 232 */     if (this._byteBuffer == null)
/* 233 */       throw new IOException("Reader closed"); 
/* 234 */     while (this._byteBuffer.hasRemaining()) {
/* 235 */       byte b = this._byteBuffer.get();
/* 236 */       if (b >= 0) {
/* 237 */         dest.append((char)b); continue;
/*     */       } 
/* 239 */       int code = read2(b);
/* 240 */       if (code < 65536) {
/* 241 */         dest.append((char)code); continue;
/* 242 */       }  if (code <= 1114111) {
/* 243 */         dest.append((char)((code - 65536 >> 10) + 55296));
/* 244 */         dest.append((char)((code - 65536 & 0x3FF) + 56320)); continue;
/*     */       } 
/* 246 */       throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 255 */     this._byteBuffer = null;
/* 256 */     this._code = 0;
/* 257 */     this._moreBytes = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8ByteBufferReader setByteBuffer(ByteBuffer byteBuffer) {
/* 264 */     return setInput(byteBuffer);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/UTF8ByteBufferReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */