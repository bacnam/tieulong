/*     */ package javolution.text;
/*     */ 
/*     */ import javolution.lang.Immutable;
/*     */ import javolution.lang.MathLib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharSet
/*     */   implements Immutable<CharSet>
/*     */ {
/*  40 */   public static final CharSet EMPTY = new CharSet(new long[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final CharSet WHITESPACES = valueOf(new char[] { '\t', '\n', '\013', '\f', '\r', '\034', '\035', '\036', '\037', ' ', ' ', '᠎', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '​', ' ', ' ', ' ', '　' });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final CharSet SPACES = valueOf(new char[] { ' ', ' ', ' ', '᠎', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '​', ' ', ' ', ' ', ' ', '　' });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final CharSet ISO_CONTROLS = valueOf(new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '' });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long[] _mapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharSet(long[] mapping) {
/*  83 */     this._mapping = mapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet valueOf(char... chars) {
/*  93 */     int maxChar = 0;
/*  94 */     for (int i = chars.length; --i >= 0;) {
/*  95 */       if (chars[i] > maxChar) {
/*  96 */         maxChar = chars[i];
/*     */       }
/*     */     } 
/*  99 */     CharSet charSet = new CharSet(new long[(maxChar >> 6) + 1]);
/* 100 */     for (int j = chars.length; --j >= 0; ) {
/* 101 */       char c = chars[j];
/* 102 */       charSet._mapping[c >> 6] = charSet._mapping[c >> 6] | 1L << (c & 0x3F);
/*     */     } 
/* 104 */     return charSet;
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
/*     */   public static CharSet rangeOf(char first, char last) {
/* 117 */     if (first > last) {
/* 118 */       throw new IllegalArgumentException("first should be less or equal to last");
/*     */     }
/* 120 */     CharSet charSet = new CharSet(new long[(last >> 6) + 1]);
/* 121 */     for (char c = first; c <= last; c = (char)(c + 1)) {
/* 122 */       charSet._mapping[c >> 6] = charSet._mapping[c >> 6] | 1L << (c & 0x3F);
/*     */     }
/* 124 */     return charSet;
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
/*     */   public boolean contains(char c) {
/* 136 */     int i = c >> 6;
/* 137 */     return (i < this._mapping.length) ? (((this._mapping[i] & 1L << (c & 0x3F)) != 0L)) : false;
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
/*     */   public int indexIn(CharSequence csq) {
/* 150 */     return indexIn(csq, 0);
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
/*     */   public int indexIn(CharSequence csq, int fromIndex) {
/* 163 */     for (int i = fromIndex, n = csq.length(); i < n; i++) {
/* 164 */       if (contains(csq.charAt(i)))
/* 165 */         return i; 
/*     */     } 
/* 167 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexIn(char[] chars) {
/* 177 */     return indexIn(chars, 0);
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
/*     */   public int indexIn(char[] chars, int fromIndex) {
/* 190 */     for (int i = fromIndex, n = chars.length; i < n; i++) {
/* 191 */       if (contains(chars[i]))
/* 192 */         return i; 
/*     */     } 
/* 194 */     return -1;
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
/*     */   public int lastIndexIn(CharSequence csq) {
/* 207 */     return lastIndexIn(csq, csq.length() - 1);
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
/*     */   public int lastIndexIn(CharSequence csq, int fromIndex) {
/* 220 */     for (int i = fromIndex; i >= 0; i--) {
/* 221 */       if (contains(csq.charAt(i)))
/* 222 */         return i; 
/*     */     } 
/* 224 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexIn(char[] chars) {
/* 235 */     return lastIndexIn(chars, chars.length - 1);
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
/*     */   public int lastIndexIn(char[] chars, int fromIndex) {
/* 248 */     for (int i = fromIndex; i >= 0; i--) {
/* 249 */       if (contains(chars[i]))
/* 250 */         return i; 
/*     */     } 
/* 252 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSet plus(CharSet that) {
/* 263 */     if (that._mapping.length > this._mapping.length)
/* 264 */       return that.plus(this); 
/* 265 */     CharSet result = copy();
/* 266 */     for (int i = that._mapping.length; --i >= 0;) {
/* 267 */       result._mapping[i] = result._mapping[i] | that._mapping[i];
/*     */     }
/* 269 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSet minus(CharSet that) {
/* 280 */     CharSet result = copy();
/* 281 */     for (int i = MathLib.min(this._mapping.length, that._mapping.length); --i >= 0;) {
/* 282 */       result._mapping[i] = result._mapping[i] & (that._mapping[i] ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/* 284 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 294 */     TextBuilder tb = new TextBuilder();
/* 295 */     tb.append('{');
/* 296 */     int length = this._mapping.length << 6;
/* 297 */     for (int i = 0; i < length; i++) {
/* 298 */       if (contains((char)i)) {
/* 299 */         if (tb.length() > 1) {
/* 300 */           tb.append(',');
/* 301 */           tb.append(' ');
/*     */         } 
/* 303 */         tb.append('\'');
/* 304 */         tb.append((char)i);
/* 305 */         tb.append('\'');
/*     */       } 
/*     */     } 
/* 308 */     tb.append('}');
/* 309 */     return tb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharSet copy() {
/* 316 */     CharSet charSet = new CharSet(new long[this._mapping.length]);
/* 317 */     for (int i = this._mapping.length; --i >= 0;) {
/* 318 */       charSet._mapping[i] = this._mapping[i];
/*     */     }
/* 320 */     return charSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSet value() {
/* 325 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/CharSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */