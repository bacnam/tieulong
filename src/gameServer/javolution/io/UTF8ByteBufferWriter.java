/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public final class UTF8ByteBufferWriter
/*     */   extends Writer
/*     */ {
/*     */   private ByteBuffer _byteBuffer;
/*     */   private char _highSurrogate;
/*     */   
/*     */   public UTF8ByteBufferWriter setOutput(ByteBuffer byteBuffer) {
/*  58 */     if (this._byteBuffer != null)
/*  59 */       throw new IllegalStateException("Writer not closed or reset"); 
/*  60 */     this._byteBuffer = byteBuffer;
/*  61 */     return this;
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
/*  73 */     if (c < '?' || c > '?') {
/*  74 */       write(c);
/*  75 */     } else if (c < '?') {
/*  76 */       this._highSurrogate = c;
/*     */     } else {
/*  78 */       int code = (this._highSurrogate - 55296 << 10) + c - 56320 + 65536;
/*     */       
/*  80 */       write(code);
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
/*  93 */     if ((code & 0xFFFFFF80) == 0) {
/*  94 */       this._byteBuffer.put((byte)code);
/*     */     } else {
/*  96 */       write2(code);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void write2(int c) throws IOException {
/* 101 */     if ((c & 0xFFFFF800) == 0) {
/* 102 */       this._byteBuffer.put((byte)(0xC0 | c >> 6));
/* 103 */       this._byteBuffer.put((byte)(0x80 | c & 0x3F));
/* 104 */     } else if ((c & 0xFFFF0000) == 0) {
/* 105 */       this._byteBuffer.put((byte)(0xE0 | c >> 12));
/* 106 */       this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
/* 107 */       this._byteBuffer.put((byte)(0x80 | c & 0x3F));
/* 108 */     } else if ((c & 0xFF200000) == 0) {
/* 109 */       this._byteBuffer.put((byte)(0xF0 | c >> 18));
/* 110 */       this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
/* 111 */       this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
/* 112 */       this._byteBuffer.put((byte)(0x80 | c & 0x3F));
/* 113 */     } else if ((c & 0xF4000000) == 0) {
/* 114 */       this._byteBuffer.put((byte)(0xF8 | c >> 24));
/* 115 */       this._byteBuffer.put((byte)(0x80 | c >> 18 & 0x3F));
/* 116 */       this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
/* 117 */       this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
/* 118 */       this._byteBuffer.put((byte)(0x80 | c & 0x3F));
/* 119 */     } else if ((c & Integer.MIN_VALUE) == 0) {
/* 120 */       this._byteBuffer.put((byte)(0xFC | c >> 30));
/* 121 */       this._byteBuffer.put((byte)(0x80 | c >> 24 & 0x3F));
/* 122 */       this._byteBuffer.put((byte)(0x80 | c >> 18 & 0x3F));
/* 123 */       this._byteBuffer.put((byte)(0x80 | c >> 12 & 0x3F));
/* 124 */       this._byteBuffer.put((byte)(0x80 | c >> 6 & 0x3F));
/* 125 */       this._byteBuffer.put((byte)(0x80 | c & 0x3F));
/*     */     } else {
/* 127 */       throw new CharConversionException("Illegal character U+" + Integer.toHexString(c));
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
/* 141 */     int off_plus_len = off + len;
/* 142 */     for (int i = off; i < off_plus_len; ) {
/* 143 */       char c = cbuf[i++];
/* 144 */       if (c < '') {
/* 145 */         this._byteBuffer.put((byte)c); continue;
/*     */       } 
/* 147 */       write(c);
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
/* 161 */     int off_plus_len = off + len;
/* 162 */     for (int i = off; i < off_plus_len; ) {
/* 163 */       char c = str.charAt(i++);
/* 164 */       if (c < '') {
/* 165 */         this._byteBuffer.put((byte)c); continue;
/*     */       } 
/* 167 */       write(c);
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
/* 179 */     int length = csq.length();
/* 180 */     for (int i = 0; i < length; ) {
/* 181 */       char c = csq.charAt(i++);
/* 182 */       if (c < '') {
/* 183 */         this._byteBuffer.put((byte)c); continue;
/*     */       } 
/* 185 */       write(c);
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
/*     */   public void flush() throws IOException {
/* 197 */     if (this._byteBuffer == null) throw new IOException("Writer closed");
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 206 */     if (this._byteBuffer != null) {
/* 207 */       reset();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 213 */     this._byteBuffer = null;
/* 214 */     this._highSurrogate = Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UTF8ByteBufferWriter setByteBuffer(ByteBuffer byteBuffer) {
/* 221 */     return setOutput(byteBuffer);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/UTF8ByteBufferWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */