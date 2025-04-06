/*     */ package javolution.text;
/*     */ 
/*     */ import javolution.util.function.Equalities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharArray
/*     */   implements CharSequence, Comparable<CharSequence>
/*     */ {
/*     */   private char[] _array;
/*     */   private int _offset;
/*     */   private int _length;
/*     */   
/*     */   public CharArray() {
/*  57 */     this._array = NO_CHAR;
/*     */   }
/*     */   
/*  60 */   private static final char[] NO_CHAR = new char[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArray(int capacity) {
/*  68 */     this._array = new char[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArray(String string) {
/*  77 */     this._array = string.toCharArray();
/*  78 */     this._length = string.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] array() {
/*  87 */     return this._array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  96 */     return this._length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int offset() {
/* 105 */     return this._offset;
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
/*     */   public CharArray setArray(char[] array, int offset, int length) {
/* 117 */     this._array = array;
/* 118 */     this._offset = offset;
/* 119 */     this._length = length;
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
/*     */   public final int indexOf(CharSequence csq) {
/* 133 */     char c = csq.charAt(0);
/* 134 */     int csqLength = csq.length();
/* 135 */     for (int i = this._offset, end = this._offset + this._length - csqLength + 1; i < end; i++) {
/* 136 */       if (this._array[i] == c) {
/* 137 */         boolean match = true;
/* 138 */         for (int j = 1; j < csqLength; j++) {
/* 139 */           if (this._array[i + j] != csq.charAt(j)) {
/* 140 */             match = false;
/*     */             break;
/*     */           } 
/*     */         } 
/* 144 */         if (match) return i - this._offset; 
/*     */       } 
/*     */     } 
/* 147 */     return -1;
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
/*     */   public final int indexOf(char c) {
/* 160 */     for (int i = this._offset, end = this._offset + this._length; i < end; i++) {
/* 161 */       if (this._array[i] == c)
/* 162 */         return i - this._offset; 
/*     */     } 
/* 164 */     return -1;
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
/*     */   public String toString() {
/* 176 */     return new String(this._array, this._offset, this._length);
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
/*     */   public int hashCode() {
/* 188 */     int h = 0;
/* 189 */     for (int i = 0, j = this._offset; i < this._length; i++) {
/* 190 */       h = 31 * h + this._array[j++];
/*     */     }
/* 192 */     return h;
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
/*     */   public boolean equals(Object that) {
/* 205 */     if (that instanceof String)
/* 206 */       return equals((String)that); 
/* 207 */     if (that instanceof CharArray)
/* 208 */       return equals((CharArray)that); 
/* 209 */     if (that instanceof CharSequence) {
/* 210 */       return equals((CharSequence)that);
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean equals(CharSequence chars) {
/* 218 */     if (chars == null)
/* 219 */       return false; 
/* 220 */     if (this._length != chars.length())
/* 221 */       return false; 
/* 222 */     for (int i = this._length, j = this._offset + this._length; --i >= 0;) {
/* 223 */       if (this._array[--j] != chars.charAt(i))
/* 224 */         return false; 
/*     */     } 
/* 226 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(CharArray that) {
/* 237 */     if (this == that)
/* 238 */       return true; 
/* 239 */     if (that == null)
/* 240 */       return false; 
/* 241 */     if (this._length != that._length)
/* 242 */       return false; 
/* 243 */     char[] thatArray = that._array;
/* 244 */     for (int i = that._offset + this._length, j = this._offset + this._length; --j >= this._offset;) {
/* 245 */       if (this._array[j] != thatArray[--i])
/* 246 */         return false; 
/*     */     } 
/* 248 */     return true;
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
/*     */   public boolean equals(String str) {
/* 261 */     if (str == null)
/* 262 */       return false; 
/* 263 */     if (this._length != str.length())
/* 264 */       return false; 
/* 265 */     for (int i = this._length, j = this._offset + this._length; --i >= 0;) {
/* 266 */       if (this._array[--j] != str.charAt(i))
/* 267 */         return false; 
/*     */     } 
/* 269 */     return true;
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
/*     */   public int compareTo(CharSequence seq) {
/* 282 */     return Equalities.LEXICAL.compare(this, seq);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean toBoolean() {
/* 293 */     return TypeFormat.parseBoolean(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int toInt() {
/* 304 */     return TypeFormat.parseInt(this);
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
/*     */   public int toInt(int radix) {
/* 317 */     return TypeFormat.parseInt(this, radix);
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
/*     */   public long toLong() {
/* 329 */     return TypeFormat.parseLong(this);
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
/*     */   public long toLong(int radix) {
/* 342 */     return TypeFormat.parseLong(this, radix);
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
/*     */   public float toFloat() {
/* 354 */     return TypeFormat.parseFloat(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double toDouble() {
/* 365 */     return TypeFormat.parseDouble(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 370 */     if (index < 0 || index >= this._length)
/* 371 */       throw new IndexOutOfBoundsException("index: " + index); 
/* 372 */     return this._array[this._offset + index];
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 377 */     if (start < 0 || end < 0 || start > end || end > length())
/* 378 */       throw new IndexOutOfBoundsException(); 
/* 379 */     CharArray chars = new CharArray();
/* 380 */     chars._array = this._array;
/* 381 */     this._offset += start;
/* 382 */     chars._length = end - start;
/* 383 */     return chars;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getChars(int start, int end, char[] dest, int destPos) {
/* 388 */     if (start < 0 || end < 0 || start > end || end > this._length)
/* 389 */       throw new IndexOutOfBoundsException(); 
/* 390 */     System.arraycopy(this._array, start + this._offset, dest, destPos, end - start);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/CharArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */