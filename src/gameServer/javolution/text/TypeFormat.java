/*     */ package javolution.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.lang.Realtime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Realtime
/*     */ public final class TypeFormat
/*     */ {
/*     */   private static final int INT_MAX_DIV10 = 214748364;
/*     */   private static final long LONG_MAX_DIV10 = 922337203685477580L;
/*     */   
/*     */   public static boolean parseBoolean(CharSequence csq, Cursor cursor) {
/*  66 */     int start = cursor.getIndex();
/*  67 */     int end = csq.length();
/*  68 */     if (end >= start + 5 && (csq.charAt(start) == 'f' || csq.charAt(start) == 'F')) {
/*     */       
/*  70 */       if ((csq.charAt(++start) == 'a' || csq.charAt(start) == 'A') && (csq.charAt(++start) == 'l' || csq.charAt(start) == 'L') && (csq.charAt(++start) == 's' || csq.charAt(start) == 'S') && (csq.charAt(++start) == 'e' || csq.charAt(start) == 'E')) {
/*     */ 
/*     */ 
/*     */         
/*  74 */         cursor.increment(5);
/*  75 */         return false;
/*     */       } 
/*  77 */     } else if (end >= start + 4 && (csq.charAt(start) == 't' || csq.charAt(start) == 'T')) {
/*     */       
/*  79 */       if ((csq.charAt(++start) == 'r' || csq.charAt(start) == 'R') && (csq.charAt(++start) == 'u' || csq.charAt(start) == 'U') && (csq.charAt(++start) == 'e' || csq.charAt(start) == 'E')) {
/*     */ 
/*     */         
/*  82 */         cursor.increment(4);
/*  83 */         return true;
/*     */       } 
/*  85 */     }  throw new IllegalArgumentException("Invalid boolean representation");
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
/*     */   public static boolean parseBoolean(CharSequence csq) {
/*  97 */     Cursor cursor = new Cursor();
/*  98 */     boolean result = parseBoolean(csq, cursor);
/*  99 */     if (!cursor.atEnd(csq)) {
/* 100 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 102 */     return result;
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
/*     */   public static byte parseByte(CharSequence csq, int radix, Cursor cursor) {
/* 117 */     int i = parseInt(csq, radix, cursor);
/* 118 */     if (i < -128 || i > 127)
/* 119 */       throw new NumberFormatException("Overflow"); 
/* 120 */     return (byte)i;
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
/*     */   public static byte parseByte(CharSequence csq, int radix) {
/* 135 */     Cursor cursor = new Cursor();
/* 136 */     byte result = parseByte(csq, radix, cursor);
/* 137 */     if (!cursor.atEnd(csq)) {
/* 138 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 140 */     return result;
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
/*     */   public static byte parseByte(CharSequence csq, Cursor cursor) {
/* 154 */     return parseByte(csq, 10, cursor);
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
/*     */   public static byte parseByte(CharSequence csq) {
/* 168 */     return parseByte(csq, 10);
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
/*     */   public static short parseShort(CharSequence csq, int radix, Cursor cursor) {
/* 183 */     int i = parseInt(csq, radix, cursor);
/* 184 */     if (i < -32768 || i > 32767)
/* 185 */       throw new NumberFormatException("Overflow"); 
/* 186 */     return (short)i;
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
/*     */   public static short parseShort(CharSequence csq, int radix) {
/* 201 */     Cursor cursor = new Cursor();
/* 202 */     short result = parseShort(csq, radix, cursor);
/* 203 */     if (!cursor.atEnd(csq)) {
/* 204 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 206 */     return result;
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
/*     */   public static short parseShort(CharSequence csq, Cursor cursor) {
/* 220 */     return parseShort(csq, 10, cursor);
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
/*     */   public static short parseShort(CharSequence csq) {
/* 234 */     return parseShort(csq, 10);
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
/*     */   public static int parseInt(CharSequence csq, int radix, Cursor cursor) {
/* 249 */     int start = cursor.getIndex();
/* 250 */     int end = csq.length();
/* 251 */     boolean isNegative = false;
/* 252 */     int result = 0;
/* 253 */     int i = start;
/* 254 */     for (; i < end; i++) {
/* 255 */       char c = csq.charAt(i);
/* 256 */       int digit = (c <= '9') ? (c - 48) : ((c <= 'Z' && c >= 'A') ? (c - 65 + 10) : ((c <= 'z' && c >= 'a') ? (c - 97 + 10) : -1));
/*     */ 
/*     */       
/* 259 */       if (digit >= 0 && digit < radix) {
/* 260 */         int newResult = result * radix - digit;
/* 261 */         if (newResult > result) {
/* 262 */           throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
/*     */         }
/* 264 */         result = newResult; continue;
/* 265 */       }  if (c == '-' && i == start) {
/* 266 */         isNegative = true; continue;
/* 267 */       }  if (c == '+' && i == start) {
/*     */         continue;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 273 */     if (result == 0 && (end == 0 || csq.charAt(i - 1) != '0')) {
/* 274 */       throw new NumberFormatException("Invalid integer representation for " + csq.subSequence(start, end));
/*     */     }
/*     */     
/* 277 */     if (result == Integer.MIN_VALUE && !isNegative) {
/* 278 */       throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
/*     */     }
/* 280 */     cursor.increment(i - start);
/* 281 */     return isNegative ? result : -result;
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
/*     */   public static int parseInt(CharSequence csq, int radix) {
/* 296 */     Cursor cursor = new Cursor();
/* 297 */     int result = parseInt(csq, radix, cursor);
/* 298 */     if (!cursor.atEnd(csq)) {
/* 299 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 301 */     return result;
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
/*     */   public static int parseInt(CharSequence csq, Cursor cursor) {
/* 315 */     return parseInt(csq, 10, cursor);
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
/*     */   public static int parseInt(CharSequence csq) {
/* 329 */     return parseInt(csq, 10);
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
/*     */   public static long parseLong(CharSequence csq, int radix, Cursor cursor) {
/* 344 */     int start = cursor.getIndex();
/* 345 */     int end = csq.length();
/* 346 */     boolean isNegative = false;
/* 347 */     long result = 0L;
/* 348 */     int i = start;
/* 349 */     for (; i < end; i++) {
/* 350 */       char c = csq.charAt(i);
/* 351 */       int digit = (c <= '9') ? (c - 48) : ((c <= 'Z' && c >= 'A') ? (c - 65 + 10) : ((c <= 'z' && c >= 'a') ? (c - 97 + 10) : -1));
/*     */ 
/*     */       
/* 354 */       if (digit >= 0 && digit < radix) {
/* 355 */         long newResult = result * radix - digit;
/* 356 */         if (newResult > result) {
/* 357 */           throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
/*     */         }
/* 359 */         result = newResult; continue;
/* 360 */       }  if (c == '-' && i == start) {
/* 361 */         isNegative = true; continue;
/* 362 */       }  if (c == '+' && i == start) {
/*     */         continue;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 368 */     if (result == 0L && (end == 0 || csq.charAt(i - 1) != '0')) {
/* 369 */       throw new NumberFormatException("Invalid integer representation for " + csq.subSequence(start, end));
/*     */     }
/*     */     
/* 372 */     if (result == Long.MIN_VALUE && !isNegative) {
/* 373 */       throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
/*     */     }
/* 375 */     cursor.increment(i - start);
/* 376 */     return isNegative ? result : -result;
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
/*     */   public static long parseLong(CharSequence csq, int radix) {
/* 391 */     Cursor cursor = new Cursor();
/* 392 */     long result = parseLong(csq, radix, cursor);
/* 393 */     if (!cursor.atEnd(csq)) {
/* 394 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 396 */     return result;
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
/*     */   public static long parseLong(CharSequence csq, Cursor cursor) {
/* 410 */     return parseLong(csq, 10, cursor);
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
/*     */   public static long parseLong(CharSequence csq) {
/* 424 */     return parseLong(csq, 10);
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
/*     */   public static float parseFloat(CharSequence csq, Cursor cursor) {
/* 437 */     return (float)parseDouble(csq, cursor);
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
/*     */   public static float parseFloat(CharSequence csq) {
/* 450 */     return (float)parseDouble(csq);
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
/*     */   public static double parseDouble(CharSequence csq, Cursor cursor) throws NumberFormatException {
/* 466 */     int start = cursor.getIndex();
/* 467 */     int end = csq.length();
/* 468 */     int i = start;
/* 469 */     char c = csq.charAt(i);
/*     */ 
/*     */     
/* 472 */     if (c == 'N' && match("NaN", csq, i, end)) {
/* 473 */       cursor.increment(3);
/* 474 */       return Double.NaN;
/*     */     } 
/*     */ 
/*     */     
/* 478 */     boolean isNegative = (c == '-');
/* 479 */     if ((isNegative || c == '+') && ++i < end) {
/* 480 */       c = csq.charAt(i);
/*     */     }
/*     */     
/* 483 */     if (c == 'I' && match("Infinity", csq, i, end)) {
/* 484 */       cursor.increment(i + 8 - start);
/* 485 */       return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 490 */     if ((c < '0' || c > '9') && c != '.') {
/* 491 */       throw new NumberFormatException("Digit or '.' required");
/*     */     }
/*     */     
/* 494 */     long decimal = 0L;
/* 495 */     int decimalPoint = -1;
/*     */     while (true) {
/* 497 */       int digit = c - 48;
/* 498 */       if (digit >= 0 && digit < 10) {
/* 499 */         long tmp = decimal * 10L + digit;
/* 500 */         if (decimal > 922337203685477580L || tmp < decimal) {
/* 501 */           throw new NumberFormatException("Too many digits - Overflow");
/*     */         }
/* 503 */         decimal = tmp;
/* 504 */       } else if (c == '.' && decimalPoint < 0) {
/* 505 */         decimalPoint = i;
/*     */       } else {
/*     */         break;
/* 508 */       }  if (++i >= end)
/*     */         break; 
/* 510 */       c = csq.charAt(i);
/*     */     } 
/* 512 */     if (isNegative)
/* 513 */       decimal = -decimal; 
/* 514 */     int fractionLength = (decimalPoint >= 0) ? (i - decimalPoint - 1) : 0;
/*     */ 
/*     */     
/* 517 */     int exp = 0;
/* 518 */     if (i < end && (c == 'E' || c == 'e')) {
/* 519 */       c = csq.charAt(++i);
/* 520 */       boolean isNegativeExp = (c == '-');
/* 521 */       if ((isNegativeExp || c == '+') && ++i < end)
/* 522 */         c = csq.charAt(i); 
/* 523 */       if (c < '0' || c > '9')
/* 524 */         throw new NumberFormatException("Invalid exponent"); 
/*     */       while (true) {
/* 526 */         int digit = c - 48;
/* 527 */         if (digit >= 0 && digit < 10) {
/* 528 */           int tmp = exp * 10 + digit;
/* 529 */           if (exp > 214748364 || tmp < exp)
/* 530 */             throw new NumberFormatException("Exponent Overflow"); 
/* 531 */           exp = tmp;
/*     */ 
/*     */           
/* 534 */           if (++i >= end)
/*     */             break; 
/* 536 */           c = csq.charAt(i); continue;
/*     */         }  break;
/* 538 */       }  if (isNegativeExp)
/* 539 */         exp = -exp; 
/*     */     } 
/* 541 */     cursor.increment(i - start);
/* 542 */     return MathLib.toDoublePow10(decimal, exp - fractionLength);
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
/*     */   public static double parseDouble(CharSequence csq) throws NumberFormatException {
/* 561 */     Cursor cursor = new Cursor();
/* 562 */     double result = parseDouble(csq, cursor);
/* 563 */     if (!cursor.atEnd(csq)) {
/* 564 */       throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
/*     */     }
/* 566 */     return result;
/*     */   }
/*     */   
/*     */   static boolean match(String str, CharSequence csq, int start, int length) {
/* 570 */     for (int i = 0; i < str.length(); i++) {
/* 571 */       if (start + i >= length || csq.charAt(start + i) != str.charAt(i))
/* 572 */         return false; 
/*     */     } 
/* 574 */     return true;
/*     */   }
/*     */   
/*     */   static boolean match(String str, String csq, int start, int length) {
/* 578 */     for (int i = 0; i < str.length(); i++) {
/* 579 */       if (start + i >= length || csq.charAt(start + i) != str.charAt(i))
/* 580 */         return false; 
/*     */     } 
/* 582 */     return true;
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
/*     */   public static Appendable format(boolean b, Appendable a) throws IOException {
/* 598 */     return b ? a.append("true") : a.append("false");
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
/*     */   public static Appendable format(int i, Appendable a) throws IOException {
/* 612 */     if (a instanceof TextBuilder)
/* 613 */       return ((TextBuilder)a).append(i); 
/* 614 */     TextBuilder tb = new TextBuilder();
/* 615 */     tb.append(i);
/* 616 */     return a.append(tb);
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
/*     */   public static Appendable format(int i, int radix, Appendable a) throws IOException {
/* 632 */     if (a instanceof TextBuilder)
/* 633 */       return ((TextBuilder)a).append(i, radix); 
/* 634 */     TextBuilder tb = new TextBuilder();
/* 635 */     tb.append(i, radix);
/* 636 */     return a.append(tb);
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
/*     */   public static Appendable format(long l, Appendable a) throws IOException {
/* 650 */     if (a instanceof TextBuilder)
/* 651 */       return ((TextBuilder)a).append(l); 
/* 652 */     TextBuilder tb = new TextBuilder();
/* 653 */     tb.append(l);
/* 654 */     return a.append(tb);
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
/*     */   public static Appendable format(long l, int radix, Appendable a) throws IOException {
/* 671 */     if (a instanceof TextBuilder)
/* 672 */       return ((TextBuilder)a).append(l, radix); 
/* 673 */     TextBuilder tb = new TextBuilder();
/* 674 */     tb.append(l, radix);
/* 675 */     return a.append(tb);
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
/*     */   public static Appendable format(float f, Appendable a) throws IOException {
/* 687 */     return format(f, 10, (MathLib.abs(f) >= 1.0E7D || MathLib.abs(f) < 0.001D), false, a);
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
/*     */   public static Appendable format(double d, Appendable a) throws IOException {
/* 701 */     return format(d, -1, (MathLib.abs(d) >= 1.0E7D || MathLib.abs(d) < 0.001D), false, a);
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
/*     */   public static Appendable format(double d, int digits, boolean scientific, boolean showZero, Appendable a) throws IOException {
/* 725 */     if (a instanceof TextBuilder)
/* 726 */       return ((TextBuilder)a).append(d, digits, scientific, showZero); 
/* 727 */     TextBuilder tb = new TextBuilder();
/* 728 */     tb.append(d, digits, scientific, showZero);
/* 729 */     return a.append(tb);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/TypeFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */