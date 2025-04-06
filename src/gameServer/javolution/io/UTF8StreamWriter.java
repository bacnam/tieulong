/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UTF8StreamWriter
/*     */   extends Writer
/*     */ {
/*     */   private OutputStream _outputStream;
/*     */   private final byte[] _bytes;
/*     */   private int _index;
/*     */   private char _highSurrogate;
/*     */   
/*     */   public UTF8StreamWriter() {
/*  57 */     this._bytes = new byte[2048];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8StreamWriter(int capacity) {
/*  66 */     this._bytes = new byte[capacity];
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
/*     */   public UTF8StreamWriter setOutput(OutputStream out) {
/*  83 */     if (this._outputStream != null)
/*  84 */       throw new IllegalStateException("Writer not closed or reset"); 
/*  85 */     this._outputStream = out;
/*  86 */     return this;
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
/*     */   public void write(char c) throws IOException {
/*  98 */     if (c < '?' || c > '?') {
/*  99 */       write(c);
/* 100 */     } else if (c < '?') {
/* 101 */       this._highSurrogate = c;
/*     */     } else {
/* 103 */       int code = (this._highSurrogate - 55296 << 10) + c - 56320 + 65536;
/*     */       
/* 105 */       write(code);
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
/*     */   public void write(int code) throws IOException {
/* 118 */     if ((code & 0xFFFFFF80) == 0) {
/* 119 */       this._bytes[this._index] = (byte)code;
/* 120 */       if (++this._index >= this._bytes.length) {
/* 121 */         flushBuffer();
/*     */       }
/*     */     } else {
/* 124 */       write2(code);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void write2(int c) throws IOException {
/* 129 */     if ((c & 0xFFFFF800) == 0) {
/* 130 */       this._bytes[this._index] = (byte)(0xC0 | c >> 6);
/* 131 */       if (++this._index >= this._bytes.length) {
/* 132 */         flushBuffer();
/*     */       }
/* 134 */       this._bytes[this._index] = (byte)(0x80 | c & 0x3F);
/* 135 */       if (++this._index >= this._bytes.length) {
/* 136 */         flushBuffer();
/*     */       }
/* 138 */     } else if ((c & 0xFFFF0000) == 0) {
/* 139 */       this._bytes[this._index] = (byte)(0xE0 | c >> 12);
/* 140 */       if (++this._index >= this._bytes.length) {
/* 141 */         flushBuffer();
/*     */       }
/* 143 */       this._bytes[this._index] = (byte)(0x80 | c >> 6 & 0x3F);
/* 144 */       if (++this._index >= this._bytes.length) {
/* 145 */         flushBuffer();
/*     */       }
/* 147 */       this._bytes[this._index] = (byte)(0x80 | c & 0x3F);
/* 148 */       if (++this._index >= this._bytes.length) {
/* 149 */         flushBuffer();
/*     */       }
/* 151 */     } else if ((c & 0xFF200000) == 0) {
/* 152 */       this._bytes[this._index] = (byte)(0xF0 | c >> 18);
/* 153 */       if (++this._index >= this._bytes.length) {
/* 154 */         flushBuffer();
/*     */       }
/* 156 */       this._bytes[this._index] = (byte)(0x80 | c >> 12 & 0x3F);
/* 157 */       if (++this._index >= this._bytes.length) {
/* 158 */         flushBuffer();
/*     */       }
/* 160 */       this._bytes[this._index] = (byte)(0x80 | c >> 6 & 0x3F);
/* 161 */       if (++this._index >= this._bytes.length) {
/* 162 */         flushBuffer();
/*     */       }
/* 164 */       this._bytes[this._index] = (byte)(0x80 | c & 0x3F);
/* 165 */       if (++this._index >= this._bytes.length) {
/* 166 */         flushBuffer();
/*     */       }
/* 168 */     } else if ((c & 0xF4000000) == 0) {
/* 169 */       this._bytes[this._index] = (byte)(0xF8 | c >> 24);
/* 170 */       if (++this._index >= this._bytes.length) {
/* 171 */         flushBuffer();
/*     */       }
/* 173 */       this._bytes[this._index] = (byte)(0x80 | c >> 18 & 0x3F);
/* 174 */       if (++this._index >= this._bytes.length) {
/* 175 */         flushBuffer();
/*     */       }
/* 177 */       this._bytes[this._index] = (byte)(0x80 | c >> 12 & 0x3F);
/* 178 */       if (++this._index >= this._bytes.length) {
/* 179 */         flushBuffer();
/*     */       }
/* 181 */       this._bytes[this._index] = (byte)(0x80 | c >> 6 & 0x3F);
/* 182 */       if (++this._index >= this._bytes.length) {
/* 183 */         flushBuffer();
/*     */       }
/* 185 */       this._bytes[this._index] = (byte)(0x80 | c & 0x3F);
/* 186 */       if (++this._index >= this._bytes.length) {
/* 187 */         flushBuffer();
/*     */       }
/* 189 */     } else if ((c & Integer.MIN_VALUE) == 0) {
/* 190 */       this._bytes[this._index] = (byte)(0xFC | c >> 30);
/* 191 */       if (++this._index >= this._bytes.length) {
/* 192 */         flushBuffer();
/*     */       }
/* 194 */       this._bytes[this._index] = (byte)(0x80 | c >> 24 & 0x3F);
/* 195 */       if (++this._index >= this._bytes.length) {
/* 196 */         flushBuffer();
/*     */       }
/* 198 */       this._bytes[this._index] = (byte)(0x80 | c >> 18 & 0x3F);
/* 199 */       if (++this._index >= this._bytes.length) {
/* 200 */         flushBuffer();
/*     */       }
/* 202 */       this._bytes[this._index] = (byte)(0x80 | c >> 12 & 0x3F);
/* 203 */       if (++this._index >= this._bytes.length) {
/* 204 */         flushBuffer();
/*     */       }
/* 206 */       this._bytes[this._index] = (byte)(0x80 | c >> 6 & 0x3F);
/* 207 */       if (++this._index >= this._bytes.length) {
/* 208 */         flushBuffer();
/*     */       }
/* 210 */       this._bytes[this._index] = (byte)(0x80 | c & 0x3F);
/* 211 */       if (++this._index >= this._bytes.length) {
/* 212 */         flushBuffer();
/*     */       }
/*     */     } else {
/* 215 */       throw new CharConversionException("Illegal character U+" + Integer.toHexString(c));
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 229 */     int off_plus_len = off + len;
/* 230 */     for (int i = off; i < off_plus_len; ) {
/* 231 */       char c = cbuf[i++];
/* 232 */       if (c < '') {
/* 233 */         this._bytes[this._index] = (byte)c;
/* 234 */         if (++this._index >= this._bytes.length)
/* 235 */           flushBuffer(); 
/*     */         continue;
/*     */       } 
/* 238 */       write(c);
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
/*     */   public void write(String str, int off, int len) throws IOException {
/* 252 */     int off_plus_len = off + len;
/* 253 */     for (int i = off; i < off_plus_len; ) {
/* 254 */       char c = str.charAt(i++);
/* 255 */       if (c < '') {
/* 256 */         this._bytes[this._index] = (byte)c;
/* 257 */         if (++this._index >= this._bytes.length)
/* 258 */           flushBuffer(); 
/*     */         continue;
/*     */       } 
/* 261 */       write(c);
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
/*     */   public void write(CharSequence csq) throws IOException {
/* 273 */     int length = csq.length();
/* 274 */     for (int i = 0; i < length; ) {
/* 275 */       char c = csq.charAt(i++);
/* 276 */       if (c < '') {
/* 277 */         this._bytes[this._index] = (byte)c;
/* 278 */         if (++this._index >= this._bytes.length)
/* 279 */           flushBuffer(); 
/*     */         continue;
/*     */       } 
/* 282 */       write(c);
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
/*     */   public void flush() throws IOException {
/* 297 */     flushBuffer();
/* 298 */     this._outputStream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 307 */     if (this._outputStream != null) {
/* 308 */       flushBuffer();
/* 309 */       this._outputStream.close();
/* 310 */       reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushBuffer() throws IOException {
/* 320 */     if (this._outputStream == null)
/* 321 */       throw new IOException("Stream closed"); 
/* 322 */     this._outputStream.write(this._bytes, 0, this._index);
/* 323 */     this._index = 0;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 327 */     this._highSurrogate = Character.MIN_VALUE;
/* 328 */     this._index = 0;
/* 329 */     this._outputStream = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8StreamWriter setOutputStream(OutputStream out) {
/* 336 */     return setOutput(out);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/UTF8StreamWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */