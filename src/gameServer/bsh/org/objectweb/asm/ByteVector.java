/*     */ package bsh.org.objectweb.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ByteVector
/*     */ {
/*     */   byte[] data;
/*     */   int length;
/*     */   
/*     */   public ByteVector() {
/*  51 */     this.data = new byte[64];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteVector(int initialSize) {
/*  61 */     this.data = new byte[initialSize];
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
/*     */   public ByteVector put1(int b) {
/*  73 */     int length = this.length;
/*  74 */     if (length + 1 > this.data.length) {
/*  75 */       enlarge(1);
/*     */     }
/*  77 */     this.data[length++] = (byte)b;
/*  78 */     this.length = length;
/*  79 */     return this;
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
/*     */   public ByteVector put11(int b1, int b2) {
/*  92 */     int length = this.length;
/*  93 */     if (length + 2 > this.data.length) {
/*  94 */       enlarge(2);
/*     */     }
/*  96 */     byte[] data = this.data;
/*  97 */     data[length++] = (byte)b1;
/*  98 */     data[length++] = (byte)b2;
/*  99 */     this.length = length;
/* 100 */     return this;
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
/*     */   public ByteVector put2(int s) {
/* 112 */     int length = this.length;
/* 113 */     if (length + 2 > this.data.length) {
/* 114 */       enlarge(2);
/*     */     }
/* 116 */     byte[] data = this.data;
/* 117 */     data[length++] = (byte)(s >>> 8);
/* 118 */     data[length++] = (byte)s;
/* 119 */     this.length = length;
/* 120 */     return this;
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
/*     */   public ByteVector put12(int b, int s) {
/* 133 */     int length = this.length;
/* 134 */     if (length + 3 > this.data.length) {
/* 135 */       enlarge(3);
/*     */     }
/* 137 */     byte[] data = this.data;
/* 138 */     data[length++] = (byte)b;
/* 139 */     data[length++] = (byte)(s >>> 8);
/* 140 */     data[length++] = (byte)s;
/* 141 */     this.length = length;
/* 142 */     return this;
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
/*     */   public ByteVector put4(int i) {
/* 154 */     int length = this.length;
/* 155 */     if (length + 4 > this.data.length) {
/* 156 */       enlarge(4);
/*     */     }
/* 158 */     byte[] data = this.data;
/* 159 */     data[length++] = (byte)(i >>> 24);
/* 160 */     data[length++] = (byte)(i >>> 16);
/* 161 */     data[length++] = (byte)(i >>> 8);
/* 162 */     data[length++] = (byte)i;
/* 163 */     this.length = length;
/* 164 */     return this;
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
/*     */   public ByteVector put8(long l) {
/* 176 */     int length = this.length;
/* 177 */     if (length + 8 > this.data.length) {
/* 178 */       enlarge(8);
/*     */     }
/* 180 */     byte[] data = this.data;
/* 181 */     int i = (int)(l >>> 32L);
/* 182 */     data[length++] = (byte)(i >>> 24);
/* 183 */     data[length++] = (byte)(i >>> 16);
/* 184 */     data[length++] = (byte)(i >>> 8);
/* 185 */     data[length++] = (byte)i;
/* 186 */     i = (int)l;
/* 187 */     data[length++] = (byte)(i >>> 24);
/* 188 */     data[length++] = (byte)(i >>> 16);
/* 189 */     data[length++] = (byte)(i >>> 8);
/* 190 */     data[length++] = (byte)i;
/* 191 */     this.length = length;
/* 192 */     return this;
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
/*     */   public ByteVector putUTF(String s) {
/* 204 */     int charLength = s.length();
/* 205 */     int byteLength = 0;
/* 206 */     for (int i = 0; i < charLength; i++) {
/* 207 */       char c = s.charAt(i);
/* 208 */       if (c >= '\001' && c <= '') {
/* 209 */         byteLength++;
/* 210 */       } else if (c > '߿') {
/* 211 */         byteLength += 3;
/*     */       } else {
/* 213 */         byteLength += 2;
/*     */       } 
/*     */     } 
/* 216 */     if (byteLength > 65535) {
/* 217 */       throw new IllegalArgumentException();
/*     */     }
/* 219 */     int length = this.length;
/* 220 */     if (length + 2 + byteLength > this.data.length) {
/* 221 */       enlarge(2 + byteLength);
/*     */     }
/* 223 */     byte[] data = this.data;
/* 224 */     data[length++] = (byte)(byteLength >>> 8);
/* 225 */     data[length++] = (byte)byteLength;
/* 226 */     for (int j = 0; j < charLength; j++) {
/* 227 */       char c = s.charAt(j);
/* 228 */       if (c >= '\001' && c <= '') {
/* 229 */         data[length++] = (byte)c;
/* 230 */       } else if (c > '߿') {
/* 231 */         data[length++] = (byte)(0xE0 | c >> 12 & 0xF);
/* 232 */         data[length++] = (byte)(0x80 | c >> 6 & 0x3F);
/* 233 */         data[length++] = (byte)(0x80 | c & 0x3F);
/*     */       } else {
/* 235 */         data[length++] = (byte)(0xC0 | c >> 6 & 0x1F);
/* 236 */         data[length++] = (byte)(0x80 | c & 0x3F);
/*     */       } 
/*     */     } 
/* 239 */     this.length = length;
/* 240 */     return this;
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
/*     */   public ByteVector putByteArray(byte[] b, int off, int len) {
/* 259 */     if (this.length + len > this.data.length) {
/* 260 */       enlarge(len);
/*     */     }
/* 262 */     if (b != null) {
/* 263 */       System.arraycopy(b, off, this.data, this.length, len);
/*     */     }
/* 265 */     this.length += len;
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void enlarge(int size) {
/* 277 */     byte[] newData = new byte[Math.max(2 * this.data.length, this.length + size)];
/* 278 */     System.arraycopy(this.data, 0, newData, 0, this.length);
/* 279 */     this.data = newData;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/org/objectweb/asm/ByteVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */