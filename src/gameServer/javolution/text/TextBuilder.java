/*     */ package javolution.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class TextBuilder
/*     */   implements Appendable, CharSequence, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private static final int B0 = 5;
/*     */   private static final int C0 = 32;
/*     */   private static final int B1 = 10;
/*     */   private static final int C1 = 1024;
/*     */   private static final int M1 = 1023;
/*  40 */   private char[] _low = new char[32];
/*     */   
/*  42 */   private char[][] _high = new char[1][];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int _length;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private int _capacity = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextBuilder() {
/*  58 */     this._high[0] = this._low;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextBuilder(String str) {
/*  68 */     this();
/*  69 */     append(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextBuilder(int capacity) {
/*  80 */     this();
/*  81 */     while (capacity > this._capacity) {
/*  82 */       increaseCapacity();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int length() {
/*  92 */     return this._length;
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
/*     */   public final char charAt(int index) {
/* 104 */     if (index >= this._length)
/* 105 */       throw new IndexOutOfBoundsException(); 
/* 106 */     return (index < 1024) ? this._low[index] : this._high[index >> 10][index & 0x3FF];
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
/*     */   public final void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
/* 123 */     if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > this._length)
/* 124 */       throw new IndexOutOfBoundsException();  int j;
/* 125 */     for (int i = srcBegin; i < srcEnd; ) {
/* 126 */       char[] chars0 = this._high[i >> 10];
/* 127 */       int i0 = i & 0x3FF;
/* 128 */       int length = MathLib.min(1024 - i0, srcEnd - i);
/* 129 */       System.arraycopy(chars0, i0, dst, j, length);
/* 130 */       i += length;
/* 131 */       j += length;
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
/*     */   public final void setCharAt(int index, char c) {
/* 144 */     if (index < 0 || index >= this._length)
/* 145 */       throw new IndexOutOfBoundsException(); 
/* 146 */     this._high[index >> 10][index & 0x3FF] = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setLength(int newLength) {
/* 157 */     setLength(newLength, false);
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
/*     */   public final void setLength(int newLength, char fillChar) {
/* 170 */     if (newLength < 0)
/* 171 */       throw new IndexOutOfBoundsException(); 
/* 172 */     if (newLength <= this._length) {
/* 173 */       this._length = newLength;
/*     */     } else {
/* 175 */       for (int i = this._length; i++ < newLength;) {
/* 176 */         append(fillChar);
/*     */       }
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
/*     */   public final CharSequence subSequence(int start, int end) {
/* 191 */     if (start < 0 || end < 0 || start > end || end > this._length)
/* 192 */       throw new IndexOutOfBoundsException(); 
/* 193 */     return Text.valueOf(this, start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(char c) {
/* 203 */     if (this._length >= this._capacity)
/* 204 */       increaseCapacity(); 
/* 205 */     this._high[this._length >> 10][this._length & 0x3FF] = c;
/* 206 */     this._length++;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(Object obj) {
/* 216 */     if (obj == null) return append("null"); 
/* 217 */     TextFormat<Object> textFormat = TextContext.getFormat(obj.getClass());
/* 218 */     if (textFormat == null) return append(obj.toString()); 
/* 219 */     return textFormat.format(obj, this);
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
/*     */   public final TextBuilder append(CharSequence csq) {
/* 231 */     return (csq == null) ? append("null") : append(csq, 0, csq.length());
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
/*     */   public final TextBuilder append(CharSequence csq, int start, int end) {
/* 248 */     if (csq == null)
/* 249 */       return append("null"); 
/* 250 */     if (start < 0 || end < 0 || start > end || end > csq.length())
/* 251 */       throw new IndexOutOfBoundsException(); 
/* 252 */     for (int i = start; i < end;) {
/* 253 */       append(csq.charAt(i++));
/*     */     }
/* 255 */     return this;
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
/*     */   public final TextBuilder append(String str) {
/* 267 */     return (str == null) ? append("null") : append(str, 0, str.length());
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
/*     */   public final TextBuilder append(String str, int start, int end) {
/* 283 */     if (str == null)
/* 284 */       return append("null"); 
/* 285 */     if (start < 0 || end < 0 || start > end || end > str.length()) {
/* 286 */       throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", str.length(): " + str.length());
/*     */     }
/* 288 */     int newLength = this._length + end - start;
/* 289 */     while (this._capacity < newLength)
/* 290 */       increaseCapacity(); 
/*     */     int j;
/* 292 */     for (int i = start; i < end; ) {
/* 293 */       char[] chars = this._high[j >> 10];
/* 294 */       int dstBegin = j & 0x3FF;
/* 295 */       int inc = MathLib.min(1024 - dstBegin, end - i);
/* 296 */       str.getChars(i, i += inc, chars, dstBegin);
/* 297 */       j += inc;
/*     */     } 
/* 299 */     this._length = newLength;
/* 300 */     return this;
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
/*     */   public final TextBuilder append(Text txt) {
/* 312 */     return (txt == null) ? append("null") : append(txt, 0, txt.length());
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
/*     */   public final TextBuilder append(Text txt, int start, int end) {
/* 328 */     if (txt == null)
/* 329 */       return append("null"); 
/* 330 */     if (start < 0 || end < 0 || start > end || end > txt.length())
/* 331 */       throw new IndexOutOfBoundsException(); 
/* 332 */     int newLength = this._length + end - start;
/* 333 */     while (this._capacity < newLength)
/* 334 */       increaseCapacity(); 
/*     */     int j;
/* 336 */     for (int i = start; i < end; ) {
/* 337 */       char[] chars = this._high[j >> 10];
/* 338 */       int dstBegin = j & 0x3FF;
/* 339 */       int inc = MathLib.min(1024 - dstBegin, end - i);
/* 340 */       txt.getChars(i, i += inc, chars, dstBegin);
/* 341 */       j += inc;
/*     */     } 
/* 343 */     this._length = newLength;
/* 344 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(char[] chars) {
/* 354 */     append(chars, 0, chars.length);
/* 355 */     return this;
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
/*     */   public final TextBuilder append(char[] chars, int offset, int length) {
/* 369 */     int end = offset + length;
/* 370 */     if (offset < 0 || length < 0 || end > chars.length)
/* 371 */       throw new IndexOutOfBoundsException(); 
/* 372 */     int newLength = this._length + length;
/* 373 */     while (this._capacity < newLength)
/* 374 */       increaseCapacity(); 
/*     */     int j;
/* 376 */     for (int i = offset; i < end; ) {
/* 377 */       char[] dstChars = this._high[j >> 10];
/* 378 */       int dstBegin = j & 0x3FF;
/* 379 */       int inc = MathLib.min(1024 - dstBegin, end - i);
/* 380 */       System.arraycopy(chars, i, dstChars, dstBegin, inc);
/* 381 */       i += inc;
/* 382 */       j += inc;
/*     */     } 
/* 384 */     this._length = newLength;
/* 385 */     return this;
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
/*     */   public final TextBuilder append(boolean b) {
/* 397 */     return b ? append("true") : append("false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(int i) {
/* 408 */     if (i <= 0) {
/* 409 */       if (i == 0)
/* 410 */         return append("0"); 
/* 411 */       if (i == Integer.MIN_VALUE)
/* 412 */         return append("-2147483648"); 
/* 413 */       append('-');
/* 414 */       i = -i;
/*     */     } 
/* 416 */     int digits = MathLib.digitLength(i);
/* 417 */     if (this._capacity < this._length + digits)
/* 418 */       increaseCapacity(); 
/* 419 */     this._length += digits;
/* 420 */     for (int index = this._length - 1;; index--) {
/* 421 */       int j = i / 10;
/* 422 */       this._high[index >> 10][index & 0x3FF] = (char)(48 + i - j * 10);
/* 423 */       if (j == 0)
/* 424 */         return this; 
/* 425 */       i = j;
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
/*     */   public final TextBuilder append(int i, int radix) {
/* 438 */     if (radix == 10)
/* 439 */       return append(i); 
/* 440 */     if (radix < 2 || radix > 36)
/* 441 */       throw new IllegalArgumentException("radix: " + radix); 
/* 442 */     if (i < 0) {
/* 443 */       append('-');
/* 444 */       if (i == Integer.MIN_VALUE) {
/* 445 */         appendPositive(-(i / radix), radix);
/* 446 */         return append(DIGIT_TO_CHAR[-(i % radix)]);
/*     */       } 
/* 448 */       i = -i;
/*     */     } 
/* 450 */     appendPositive(i, radix);
/* 451 */     return this;
/*     */   }
/*     */   
/*     */   private void appendPositive(int l1, int radix) {
/* 455 */     if (l1 >= radix) {
/* 456 */       int l2 = l1 / radix;
/*     */       
/* 458 */       if (l2 >= radix) {
/* 459 */         int l3 = l2 / radix;
/*     */         
/* 461 */         if (l3 >= radix) {
/* 462 */           int l4 = l3 / radix;
/* 463 */           appendPositive(l4, radix);
/* 464 */           append(DIGIT_TO_CHAR[l3 - l4 * radix]);
/*     */         } else {
/* 466 */           append(DIGIT_TO_CHAR[l3]);
/* 467 */         }  append(DIGIT_TO_CHAR[l2 - l3 * radix]);
/*     */       } else {
/* 469 */         append(DIGIT_TO_CHAR[l2]);
/* 470 */       }  append(DIGIT_TO_CHAR[l1 - l2 * radix]);
/*     */     } else {
/* 472 */       append(DIGIT_TO_CHAR[l1]);
/*     */     } 
/*     */   }
/* 475 */   private static final char[] DIGIT_TO_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(long l) {
/* 488 */     if (l <= 0L) {
/* 489 */       if (l == 0L)
/* 490 */         return append("0"); 
/* 491 */       if (l == Long.MIN_VALUE)
/* 492 */         return append("-9223372036854775808"); 
/* 493 */       append('-');
/* 494 */       l = -l;
/*     */     } 
/* 496 */     if (l <= 2147483647L)
/* 497 */       return append((int)l); 
/* 498 */     append(l / 1000000000L);
/* 499 */     int i = (int)(l % 1000000000L);
/* 500 */     int digits = MathLib.digitLength(i);
/* 501 */     append("000000000", 0, 9 - digits);
/* 502 */     return append(i);
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
/*     */   public final TextBuilder append(long l, int radix) {
/* 514 */     if (radix == 10)
/* 515 */       return append(l); 
/* 516 */     if (radix < 2 || radix > 36)
/* 517 */       throw new IllegalArgumentException("radix: " + radix); 
/* 518 */     if (l < 0L) {
/* 519 */       append('-');
/* 520 */       if (l == Long.MIN_VALUE) {
/* 521 */         appendPositive(-(l / radix), radix);
/* 522 */         return append(DIGIT_TO_CHAR[(int)-(l % radix)]);
/*     */       } 
/* 524 */       l = -l;
/*     */     } 
/* 526 */     appendPositive(l, radix);
/* 527 */     return this;
/*     */   }
/*     */   
/*     */   private void appendPositive(long l1, int radix) {
/* 531 */     if (l1 >= radix) {
/* 532 */       long l2 = l1 / radix;
/*     */       
/* 534 */       if (l2 >= radix) {
/* 535 */         long l3 = l2 / radix;
/*     */         
/* 537 */         if (l3 >= radix) {
/* 538 */           long l4 = l3 / radix;
/* 539 */           appendPositive(l4, radix);
/* 540 */           append(DIGIT_TO_CHAR[(int)(l3 - l4 * radix)]);
/*     */         } else {
/* 542 */           append(DIGIT_TO_CHAR[(int)l3]);
/* 543 */         }  append(DIGIT_TO_CHAR[(int)(l2 - l3 * radix)]);
/*     */       } else {
/* 545 */         append(DIGIT_TO_CHAR[(int)l2]);
/* 546 */       }  append(DIGIT_TO_CHAR[(int)(l1 - l2 * radix)]);
/*     */     } else {
/* 548 */       append(DIGIT_TO_CHAR[(int)l1]);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder append(float f) {
/* 558 */     return append(f, 10, (MathLib.abs(f) >= 1.0E7D || MathLib.abs(f) < 0.001D), false);
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
/*     */   public final TextBuilder append(double d) {
/* 573 */     return append(d, -1, (MathLib.abs(d) >= 1.0E7D || MathLib.abs(d) < 0.001D), false);
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
/*     */   public final TextBuilder append(double d, int digits, boolean scientific, boolean showZero) {
/*     */     long m;
/* 594 */     if (digits > 19)
/* 595 */       throw new IllegalArgumentException("digits: " + digits); 
/* 596 */     if (d != d)
/* 597 */       return append("NaN"); 
/* 598 */     if (d == Double.POSITIVE_INFINITY)
/* 599 */       return append("Infinity"); 
/* 600 */     if (d == Double.NEGATIVE_INFINITY)
/* 601 */       return append("-Infinity"); 
/* 602 */     if (d == 0.0D) {
/* 603 */       if (digits < 0)
/* 604 */         return append("0.0"); 
/* 605 */       append('0');
/* 606 */       if (showZero) {
/* 607 */         append('.');
/* 608 */         for (int j = 1; j < digits; j++) {
/* 609 */           append('0');
/*     */         }
/*     */       } 
/* 612 */       return this;
/*     */     } 
/* 614 */     if (d < 0.0D) {
/* 615 */       d = -d;
/* 616 */       append('-');
/*     */     } 
/*     */ 
/*     */     
/* 620 */     int e = MathLib.floorLog10(d);
/*     */ 
/*     */     
/* 623 */     if (digits < 0) {
/*     */       
/* 625 */       long m17 = MathLib.toLongPow10(d, 16 - e);
/*     */       
/* 627 */       long m16 = m17 / 10L;
/* 628 */       double dd = MathLib.toDoublePow10(m16, e - 16 + 1);
/* 629 */       if (dd == d) {
/* 630 */         digits = 16;
/* 631 */         m = m16;
/*     */       } else {
/* 633 */         digits = 17;
/* 634 */         m = m17;
/*     */       } 
/*     */     } else {
/*     */       
/* 638 */       m = MathLib.toLongPow10(d, digits - 1 - e);
/*     */     } 
/*     */     
/* 641 */     if (scientific || e >= digits) {
/*     */       
/* 643 */       long pow10 = POW10_LONG[digits - 1];
/* 644 */       int k = (int)(m / pow10);
/* 645 */       append((char)(48 + k));
/* 646 */       m -= pow10 * k;
/* 647 */       appendFraction(m, digits - 1, showZero);
/* 648 */       append('E');
/* 649 */       append(e);
/*     */     } else {
/* 651 */       int exp = digits - e - 1;
/* 652 */       if (exp < POW10_LONG.length) {
/* 653 */         long pow10 = POW10_LONG[exp];
/* 654 */         long l = m / pow10;
/* 655 */         append(l);
/* 656 */         m -= pow10 * l;
/*     */       } else {
/* 658 */         append('0');
/* 659 */       }  appendFraction(m, exp, showZero);
/*     */     } 
/* 661 */     return this;
/*     */   }
/*     */   
/*     */   private void appendFraction(long l, int digits, boolean showZero) {
/* 665 */     append('.');
/* 666 */     if (l == 0L)
/* 667 */     { if (showZero) {
/* 668 */         for (int i = 0; i < digits; i++) {
/* 669 */           append('0');
/*     */         }
/*     */       } else {
/* 672 */         append('0');
/*     */       }  }
/* 674 */     else { int length = MathLib.digitLength(l);
/* 675 */       for (int j = length; j < digits; j++) {
/* 676 */         append('0');
/*     */       }
/* 678 */       if (!showZero)
/* 679 */         while (l % 10L == 0L) {
/* 680 */           l /= 10L;
/*     */         } 
/* 682 */       append(l); }
/*     */   
/*     */   }
/*     */   
/* 686 */   private static final long[] POW10_LONG = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder insert(int index, CharSequence csq) {
/* 702 */     if (index < 0 || index > this._length)
/* 703 */       throw new IndexOutOfBoundsException("index: " + index); 
/* 704 */     int shift = csq.length();
/* 705 */     int newLength = this._length + shift;
/* 706 */     while (newLength >= this._capacity) {
/* 707 */       increaseCapacity();
/*     */     }
/* 709 */     this._length = newLength; int i;
/* 710 */     for (i = this._length - shift; --i >= index;) {
/* 711 */       setCharAt(i + shift, charAt(i));
/*     */     }
/* 713 */     for (i = csq.length(); --i >= 0;) {
/* 714 */       setCharAt(index + i, csq.charAt(i));
/*     */     }
/* 716 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder clear() {
/* 726 */     this._length = 0;
/* 727 */     return this;
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
/*     */   public final TextBuilder delete(int start, int end) {
/* 740 */     if (start < 0 || end < 0 || start > end || end > length())
/* 741 */       throw new IndexOutOfBoundsException(); 
/* 742 */     for (int i = end, j = start; i < this._length;) {
/* 743 */       setCharAt(j++, charAt(i++));
/*     */     }
/* 745 */     this._length -= end - start;
/* 746 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TextBuilder reverse() {
/* 755 */     int n = this._length - 1;
/* 756 */     for (int j = n - 1 >> 1; j >= 0; ) {
/* 757 */       char c = charAt(j);
/* 758 */       setCharAt(j, charAt(n - j));
/* 759 */       setCharAt(n - j--, c);
/*     */     } 
/* 761 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Text toText() {
/* 770 */     return Text.valueOf(this, 0, this._length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 781 */     return (this._length < 1024) ? new String(this._low, 0, this._length) : toLargeString();
/*     */   }
/*     */   
/*     */   private String toLargeString() {
/* 785 */     char[] data = new char[this._length];
/* 786 */     getChars(0, this._length, data, 0);
/* 787 */     return new String(data, 0, this._length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CharArray toCharArray() {
/*     */     char[] data;
/* 797 */     CharArray cArray = new CharArray();
/*     */     
/* 799 */     if (this._length < 1024) {
/* 800 */       data = this._low;
/*     */     } else {
/* 802 */       data = new char[this._length];
/* 803 */       getChars(0, this._length, data, 0);
/*     */     } 
/* 805 */     cArray.setArray(data, 0, this._length);
/* 806 */     return cArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 816 */     int h = 0;
/* 817 */     for (int i = 0; i < this._length;) {
/* 818 */       h = 31 * h + charAt(i++);
/*     */     }
/* 820 */     return h;
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
/*     */   public final boolean equals(Object obj) {
/* 834 */     if (this == obj)
/* 835 */       return true; 
/* 836 */     if (!(obj instanceof TextBuilder))
/* 837 */       return false; 
/* 838 */     TextBuilder that = (TextBuilder)obj;
/* 839 */     if (this._length != that._length)
/* 840 */       return false; 
/* 841 */     for (int i = 0; i < this._length;) {
/* 842 */       if (charAt(i) != that.charAt(i++))
/* 843 */         return false; 
/*     */     } 
/* 845 */     return true;
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
/*     */   public final boolean contentEquals(CharSequence csq) {
/* 857 */     if (csq.length() != this._length)
/* 858 */       return false; 
/* 859 */     for (int i = 0; i < this._length; ) {
/* 860 */       char c = this._high[i >> 10][i & 0x3FF];
/* 861 */       if (csq.charAt(i++) != c)
/* 862 */         return false; 
/*     */     } 
/* 864 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void increaseCapacity() {
/* 871 */     if (this._capacity < 1024) {
/* 872 */       this._capacity <<= 1;
/* 873 */       char[] tmp = new char[this._capacity];
/* 874 */       System.arraycopy(this._low, 0, tmp, 0, this._length);
/* 875 */       this._low = tmp;
/* 876 */       this._high[0] = tmp;
/*     */     } else {
/* 878 */       int j = this._capacity >> 10;
/* 879 */       if (j >= this._high.length) {
/* 880 */         char[][] tmp = new char[this._high.length * 2][];
/* 881 */         System.arraycopy(this._high, 0, tmp, 0, this._high.length);
/* 882 */         this._high = tmp;
/*     */       } 
/* 884 */       this._high[j] = new char[1024];
/* 885 */       this._capacity += 1024;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/TextBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */