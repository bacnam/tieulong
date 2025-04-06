/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UTF8StreamReader
/*     */   extends Reader
/*     */ {
/*     */   private InputStream _inputStream;
/*     */   private int _start;
/*     */   private int _end;
/*     */   private final byte[] _bytes;
/*     */   private int _code;
/*     */   private int _moreBytes;
/*     */   
/*     */   public UTF8StreamReader() {
/*  70 */     this._bytes = new byte[2048];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8StreamReader(int capacity) {
/*  79 */     this._bytes = new byte[capacity];
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
/*     */   public UTF8StreamReader setInput(InputStream inStream) {
/*  96 */     if (this._inputStream != null)
/*  97 */       throw new IllegalStateException("Reader not closed or reset"); 
/*  98 */     this._inputStream = inStream;
/*  99 */     return this;
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
/* 110 */     if (this._inputStream == null)
/* 111 */       throw new IOException("Stream closed"); 
/* 112 */     return (this._end - this._start > 0 || this._inputStream.available() != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 121 */     if (this._inputStream != null) {
/* 122 */       this._inputStream.close();
/* 123 */       reset();
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
/*     */   public int read() throws IOException {
/* 136 */     byte b = this._bytes[this._start];
/* 137 */     return (b >= 0 && this._start++ < this._end) ? b : read2();
/*     */   }
/*     */ 
/*     */   
/*     */   private int read2() throws IOException {
/* 142 */     if (this._start < this._end) {
/* 143 */       byte b = this._bytes[this._start++];
/*     */ 
/*     */       
/* 146 */       if (b >= 0 && this._moreBytes == 0)
/*     */       {
/* 148 */         return b; } 
/* 149 */       if ((b & 0xC0) == 128 && this._moreBytes != 0) {
/*     */         
/* 151 */         this._code = this._code << 6 | b & 0x3F;
/* 152 */         if (--this._moreBytes == 0) {
/* 153 */           return this._code;
/*     */         }
/* 155 */         return read2();
/*     */       } 
/* 157 */       if ((b & 0xE0) == 192 && this._moreBytes == 0) {
/*     */         
/* 159 */         this._code = b & 0x1F;
/* 160 */         this._moreBytes = 1;
/* 161 */         return read2();
/* 162 */       }  if ((b & 0xF0) == 224 && this._moreBytes == 0) {
/*     */         
/* 164 */         this._code = b & 0xF;
/* 165 */         this._moreBytes = 2;
/* 166 */         return read2();
/* 167 */       }  if ((b & 0xF8) == 240 && this._moreBytes == 0) {
/*     */         
/* 169 */         this._code = b & 0x7;
/* 170 */         this._moreBytes = 3;
/* 171 */         return read2();
/* 172 */       }  if ((b & 0xFC) == 248 && this._moreBytes == 0) {
/*     */         
/* 174 */         this._code = b & 0x3;
/* 175 */         this._moreBytes = 4;
/* 176 */         return read2();
/* 177 */       }  if ((b & 0xFE) == 252 && this._moreBytes == 0) {
/*     */         
/* 179 */         this._code = b & 0x1;
/* 180 */         this._moreBytes = 5;
/* 181 */         return read2();
/*     */       } 
/* 183 */       throw new CharConversionException("Invalid UTF-8 Encoding");
/*     */     } 
/*     */     
/* 186 */     if (this._inputStream == null)
/* 187 */       throw new IOException("No input stream or stream closed"); 
/* 188 */     this._start = 0;
/* 189 */     this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
/* 190 */     if (this._end > 0) {
/* 191 */       return read2();
/*     */     }
/* 193 */     if (this._moreBytes == 0) {
/* 194 */       return -1;
/*     */     }
/* 196 */     throw new CharConversionException("Unexpected end of stream");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 223 */     if (this._inputStream == null)
/* 224 */       throw new IOException("No input stream or stream closed"); 
/* 225 */     if (this._start >= this._end) {
/* 226 */       this._start = 0;
/* 227 */       this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
/* 228 */       if (this._end <= 0) {
/* 229 */         return this._end;
/*     */       }
/*     */     } 
/* 232 */     int off_plus_len = off + len;
/* 233 */     for (int i = off; i < off_plus_len; ) {
/*     */       
/* 235 */       byte b = this._bytes[this._start];
/* 236 */       if (b >= 0 && ++this._start < this._end) {
/* 237 */         cbuf[i++] = (char)b; continue;
/* 238 */       }  if (b < 0) {
/* 239 */         if (i < off_plus_len - 1) {
/* 240 */           int code = read2();
/* 241 */           if (code < 65536) {
/* 242 */             cbuf[i++] = (char)code;
/* 243 */           } else if (code <= 1114111) {
/* 244 */             cbuf[i++] = (char)((code - 65536 >> 10) + 55296);
/* 245 */             cbuf[i++] = (char)((code - 65536 & 0x3FF) + 56320);
/*     */           } else {
/* 247 */             throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
/*     */           } 
/*     */ 
/*     */           
/* 251 */           if (this._start < this._end) {
/*     */             continue;
/*     */           }
/*     */         } 
/* 255 */         return i - off;
/*     */       } 
/* 257 */       cbuf[i++] = (char)b;
/* 258 */       return i - off;
/*     */     } 
/*     */     
/* 261 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(Appendable dest) throws IOException {
/* 272 */     if (this._inputStream == null)
/* 273 */       throw new IOException("No input stream or stream closed"); 
/*     */     while (true) {
/* 275 */       if (this._start >= this._end) {
/* 276 */         this._start = 0;
/* 277 */         this._end = this._inputStream.read(this._bytes, 0, this._bytes.length);
/* 278 */         if (this._end <= 0) {
/*     */           break;
/*     */         }
/*     */       } 
/* 282 */       byte b = this._bytes[this._start];
/* 283 */       if (b >= 0) {
/* 284 */         dest.append((char)b);
/* 285 */         this._start++; continue;
/*     */       } 
/* 287 */       int code = read2();
/* 288 */       if (code < 65536) {
/* 289 */         dest.append((char)code); continue;
/* 290 */       }  if (code <= 1114111) {
/* 291 */         dest.append((char)((code - 65536 >> 10) + 55296));
/* 292 */         dest.append((char)((code - 65536 & 0x3FF) + 56320)); continue;
/*     */       } 
/* 294 */       throw new CharConversionException("Cannot convert U+" + Integer.toHexString(code) + " to char (code greater than U+10FFFF)");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 303 */     this._code = 0;
/* 304 */     this._end = 0;
/* 305 */     this._inputStream = null;
/* 306 */     this._moreBytes = 0;
/* 307 */     this._start = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8StreamReader setInputStream(InputStream inStream) {
/* 314 */     return setInput(inStream);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/UTF8StreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */