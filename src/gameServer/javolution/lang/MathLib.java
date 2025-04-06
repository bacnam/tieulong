/*      */ package javolution.lang;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Realtime
/*      */ public final class MathLib
/*      */ {
/*      */   public static int bitLength(int i) {
/*   39 */     if (i < 0)
/*   40 */       i = -++i; 
/*   41 */     return (i < 65536) ? ((i < 256) ? BIT_LENGTH[i] : (BIT_LENGTH[i >>> 8] + 8)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 16) : (BIT_LENGTH[i >>> 24] + 24));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   47 */   private static final byte[] BIT_LENGTH = new byte[] { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long MASK_63 = 9223372036854775807L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long MASK_32 = 4294967295L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int bitLength(long l) {
/*   73 */     int i = (int)(l >> 32L);
/*   74 */     if (i > 0) {
/*   75 */       return (i < 65536) ? ((i < 256) ? (BIT_LENGTH[i] + 32) : (BIT_LENGTH[i >>> 8] + 40)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 48) : (BIT_LENGTH[i >>> 24] + 56));
/*      */     }
/*      */ 
/*      */     
/*   79 */     if (i < 0)
/*   80 */       return bitLength(-++l); 
/*   81 */     i = (int)l;
/*   82 */     return (i < 0) ? 32 : ((i < 65536) ? ((i < 256) ? BIT_LENGTH[i] : (BIT_LENGTH[i >>> 8] + 8)) : ((i < 16777216) ? (BIT_LENGTH[i >>> 16] + 16) : (BIT_LENGTH[i >>> 24] + 24)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int bitCount(long longValue) {
/*   98 */     longValue -= longValue >>> 1L & 0x5555555555555555L;
/*   99 */     longValue = (longValue & 0x3333333333333333L) + (longValue >>> 2L & 0x3333333333333333L);
/*      */     
/*  101 */     longValue = longValue + (longValue >>> 4L) & 0xF0F0F0F0F0F0F0FL;
/*  102 */     longValue += longValue >>> 8L;
/*  103 */     longValue += longValue >>> 16L;
/*  104 */     longValue += longValue >>> 32L;
/*  105 */     return (int)longValue & 0x7F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int numberOfLeadingZeros(long longValue) {
/*  119 */     if (longValue == 0L)
/*  120 */       return 64; 
/*  121 */     int n = 1;
/*  122 */     int x = (int)(longValue >>> 32L);
/*  123 */     if (x == 0) { n += 32; x = (int)longValue; }
/*  124 */      if (x >>> 16 == 0) { n += 16; x <<= 16; }
/*  125 */      if (x >>> 24 == 0) { n += 8; x <<= 8; }
/*  126 */      if (x >>> 28 == 0) { n += 4; x <<= 4; }
/*  127 */      if (x >>> 30 == 0) { n += 2; x <<= 2; }
/*  128 */      n -= x >>> 31;
/*  129 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int numberOfTrailingZeros(long longValue) {
/*      */     int x;
/*  143 */     if (longValue == 0L) return 64; 
/*  144 */     int n = 63;
/*  145 */     int y = (int)longValue; if (y != 0) { n -= 32; x = y; } else { x = (int)(longValue >>> 32L); }
/*  146 */      y = x << 16; if (y != 0) { n -= 16; x = y; }
/*  147 */      y = x << 8; if (y != 0) { n -= 8; x = y; }
/*  148 */      y = x << 4; if (y != 0) { n -= 4; x = y; }
/*  149 */      y = x << 2; if (y != 0) { n -= 2; x = y; }
/*  150 */      return n - (x << 1 >>> 31);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int digitLength(int i) {
/*  162 */     if (i >= 0) {
/*  163 */       return (i >= 100000) ? ((i >= 10000000) ? ((i >= 1000000000) ? 10 : ((i >= 100000000) ? 9 : 8)) : ((i >= 1000000) ? 7 : 6)) : ((i >= 100) ? ((i >= 10000) ? 5 : ((i >= 1000) ? 4 : 3)) : ((i >= 10) ? 2 : 1));
/*      */     }
/*      */ 
/*      */     
/*  167 */     if (i == Integer.MIN_VALUE)
/*  168 */       return 10; 
/*  169 */     return digitLength(-i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int digitLength(long l) {
/*  181 */     if (l >= 0L) {
/*  182 */       return (l <= 2147483647L) ? digitLength((int)l) : ((l >= 100000000000000L) ? ((l >= 10000000000000000L) ? ((l >= 1000000000000000000L) ? 19 : ((l >= 100000000000000000L) ? 18 : 17)) : ((l >= 1000000000000000L) ? 16 : 15)) : ((l >= 100000000000L) ? ((l >= 10000000000000L) ? 14 : ((l >= 1000000000000L) ? 13 : 12)) : ((l >= 10000000000L) ? 11 : 10)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  190 */     if (l == Long.MIN_VALUE)
/*  191 */       return 19; 
/*  192 */     return digitLength(-l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDoublePow2(long m, int n) {
/*  204 */     if (m == 0L)
/*  205 */       return 0.0D; 
/*  206 */     if (m == Long.MIN_VALUE)
/*  207 */       return toDoublePow2(-4611686018427387904L, n + 1); 
/*  208 */     if (m < 0L)
/*  209 */       return -toDoublePow2(-m, n); 
/*  210 */     int bitLength = bitLength(m);
/*  211 */     int shift = bitLength - 53;
/*  212 */     long exp = 1075L + n + shift;
/*  213 */     if (exp >= 2047L)
/*  214 */       return Double.POSITIVE_INFINITY; 
/*  215 */     if (exp <= 0L) {
/*  216 */       if (exp <= -54L)
/*  217 */         return 0.0D; 
/*  218 */       return toDoublePow2(m, n + 54) / 1.8014398509481984E16D;
/*      */     } 
/*      */     
/*  221 */     long bits = (shift > 0) ? ((m >> shift) + (m >> shift - 1 & 0x1L)) : (m << -shift);
/*      */     
/*  223 */     if (bits >> 52L != 1L && ++exp >= 2047L)
/*  224 */       return Double.POSITIVE_INFINITY; 
/*  225 */     bits &= 0xFFFFFFFFFFFFFL;
/*  226 */     bits |= exp << 52L;
/*  227 */     return Double.longBitsToDouble(bits);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDoublePow10(long m, int n) {
/*  241 */     if (m == 0L)
/*  242 */       return 0.0D; 
/*  243 */     if (m == Long.MIN_VALUE)
/*  244 */       return toDoublePow10(-922337203685477580L, n + 1); 
/*  245 */     if (m < 0L)
/*  246 */       return -toDoublePow10(-m, n); 
/*  247 */     if (n >= 0) {
/*  248 */       if (n > 308) {
/*  249 */         return Double.POSITIVE_INFINITY;
/*      */       }
/*  251 */       long l1 = 0L;
/*  252 */       long l2 = 0L;
/*  253 */       long x2 = m & 0xFFFFFFFFL;
/*  254 */       long x3 = m >>> 32L;
/*  255 */       int i = 0;
/*  256 */       while (n != 0) {
/*  257 */         int j = (n >= POW5_INT.length) ? (POW5_INT.length - 1) : n;
/*  258 */         int coef = POW5_INT[j];
/*      */         
/*  260 */         if ((int)l1 != 0)
/*  261 */           l1 *= coef; 
/*  262 */         if ((int)l2 != 0)
/*  263 */           l2 *= coef; 
/*  264 */         x2 *= coef;
/*  265 */         x3 *= coef;
/*      */         
/*  267 */         l2 += l1 >>> 32L;
/*  268 */         l1 &= 0xFFFFFFFFL;
/*      */         
/*  270 */         x2 += l2 >>> 32L;
/*  271 */         l2 &= 0xFFFFFFFFL;
/*      */         
/*  273 */         x3 += x2 >>> 32L;
/*  274 */         x2 &= 0xFFFFFFFFL;
/*      */ 
/*      */         
/*  277 */         i += j;
/*  278 */         n -= j;
/*      */ 
/*      */         
/*  281 */         long carry = x3 >>> 32L;
/*  282 */         if (carry != 0L) {
/*  283 */           l1 = l2;
/*  284 */           l2 = x2;
/*  285 */           x2 = x3 & 0xFFFFFFFFL;
/*  286 */           x3 = carry;
/*  287 */           i += 32;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  292 */       int shift = 31 - bitLength(x3);
/*  293 */       i -= shift;
/*  294 */       long mantissa = (shift < 0) ? (x3 << 31L | x2 >>> 1L) : ((x3 << 32L | x2) << shift | l2 >>> 32 - shift);
/*      */       
/*  296 */       return toDoublePow2(mantissa, i);
/*      */     } 
/*      */     
/*  299 */     if (n < -344) {
/*  300 */       return 0.0D;
/*      */     }
/*      */     
/*  303 */     long x1 = m;
/*  304 */     long x0 = 0L;
/*  305 */     int pow2 = 0;
/*      */ 
/*      */     
/*      */     while (true) {
/*  309 */       int shift = 63 - bitLength(x1);
/*  310 */       x1 <<= shift;
/*  311 */       x1 |= x0 >>> 63 - shift;
/*  312 */       x0 = x0 << shift & Long.MAX_VALUE;
/*  313 */       pow2 -= shift;
/*      */ 
/*      */       
/*  316 */       if (n == 0) {
/*      */         break;
/*      */       }
/*      */       
/*  320 */       int i = (-n >= POW5_INT.length) ? (POW5_INT.length - 1) : -n;
/*  321 */       int divisor = POW5_INT[i];
/*      */ 
/*      */       
/*  324 */       long wh = x1 >>> 32L;
/*  325 */       long qh = wh / divisor;
/*  326 */       long r = wh - qh * divisor;
/*  327 */       long wl = r << 32L | x1 & 0xFFFFFFFFL;
/*  328 */       long ql = wl / divisor;
/*  329 */       r = wl - ql * divisor;
/*  330 */       x1 = qh << 32L | ql;
/*      */       
/*  332 */       wh = r << 31L | x0 >>> 32L;
/*  333 */       qh = wh / divisor;
/*  334 */       r = wh - qh * divisor;
/*  335 */       wl = r << 32L | x0 & 0xFFFFFFFFL;
/*  336 */       ql = wl / divisor;
/*  337 */       x0 = qh << 32L | ql;
/*      */ 
/*      */       
/*  340 */       n += i;
/*  341 */       pow2 -= i;
/*      */     } 
/*  343 */     return toDoublePow2(x1, pow2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  351 */   private static final int[] POW5_INT = new int[] { 1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125 };
/*      */   
/*      */   private static final double LOG2_DIV_LOG10 = 0.3010299956639812D;
/*      */   public static final double E = 2.718281828459045D;
/*      */   public static final double PI = 3.141592653589793D;
/*      */   public static final double HALF_PI = 1.5707963267948966D;
/*      */   public static final double TWO_PI = 6.283185307179586D;
/*      */   public static final double FOUR_PI = 12.566370614359172D;
/*      */   public static final double PI_SQUARE = 9.869604401089358D;
/*      */   public static final double LOG2 = 0.6931471805599453D;
/*      */   public static final double LOG10 = 2.302585092994046D;
/*      */   public static final double SQRT2 = 1.4142135623730951D;
/*      */   public static final double NaN = NaND;
/*      */   public static final double Infinity = InfinityD;
/*      */   
/*      */   public static long toLongPow2(double d, int n) {
/*  367 */     long bits = Double.doubleToLongBits(d);
/*  368 */     boolean isNegative = (bits >> 63L != 0L);
/*  369 */     int exp = (int)(bits >> 52L) & 0x7FF;
/*  370 */     long m = bits & 0xFFFFFFFFFFFFFL;
/*  371 */     if (exp == 2047) {
/*  372 */       throw new ArithmeticException("Cannot convert to long (Infinity or NaN)");
/*      */     }
/*  374 */     if (exp == 0) {
/*  375 */       if (m == 0L)
/*  376 */         return 0L; 
/*  377 */       return toLongPow2(d * 1.8014398509481984E16D, n - 54);
/*      */     } 
/*  379 */     m |= 0x10000000000000L;
/*  380 */     long shift = exp - 1023L - 52L + n;
/*  381 */     if (shift <= -64L)
/*  382 */       return 0L; 
/*  383 */     if (shift >= 11L)
/*  384 */       throw new ArithmeticException("Cannot convert to long (overflow)"); 
/*  385 */     m = (shift >= 0L) ? (m << (int)shift) : ((m >> (int)-shift) + (m >> (int)-(shift + 1L) & 0x1L));
/*      */     
/*  387 */     return isNegative ? -m : m;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long toLongPow10(double d, int n) {
/*  401 */     long bits = Double.doubleToLongBits(d);
/*  402 */     boolean isNegative = (bits >> 63L != 0L);
/*  403 */     int exp = (int)(bits >> 52L) & 0x7FF;
/*  404 */     long m = bits & 0xFFFFFFFFFFFFFL;
/*  405 */     if (exp == 2047) {
/*  406 */       throw new ArithmeticException("Cannot convert to long (Infinity or NaN)");
/*      */     }
/*  408 */     if (exp == 0) {
/*  409 */       if (m == 0L)
/*  410 */         return 0L; 
/*  411 */       return toLongPow10(d * 1.0E16D, n - 16);
/*      */     } 
/*  413 */     m |= 0x10000000000000L;
/*  414 */     int pow2 = exp - 1023 - 52;
/*      */     
/*  416 */     if (n >= 0) {
/*      */       
/*  418 */       long x0 = 0L;
/*  419 */       long x1 = 0L;
/*  420 */       long x2 = m & 0xFFFFFFFFL;
/*  421 */       long x3 = m >>> 32L;
/*  422 */       while (n != 0) {
/*  423 */         int i = (n >= POW5_INT.length) ? (POW5_INT.length - 1) : n;
/*  424 */         int coef = POW5_INT[i];
/*      */         
/*  426 */         if ((int)x0 != 0)
/*  427 */           x0 *= coef; 
/*  428 */         if ((int)x1 != 0)
/*  429 */           x1 *= coef; 
/*  430 */         x2 *= coef;
/*  431 */         x3 *= coef;
/*      */         
/*  433 */         x1 += x0 >>> 32L;
/*  434 */         x0 &= 0xFFFFFFFFL;
/*      */         
/*  436 */         x2 += x1 >>> 32L;
/*  437 */         x1 &= 0xFFFFFFFFL;
/*      */         
/*  439 */         x3 += x2 >>> 32L;
/*  440 */         x2 &= 0xFFFFFFFFL;
/*      */ 
/*      */         
/*  443 */         pow2 += i;
/*  444 */         n -= i;
/*      */ 
/*      */         
/*  447 */         long carry = x3 >>> 32L;
/*  448 */         if (carry != 0L) {
/*  449 */           x0 = x1;
/*  450 */           x1 = x2;
/*  451 */           x2 = x3 & 0xFFFFFFFFL;
/*  452 */           x3 = carry;
/*  453 */           pow2 += 32;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  458 */       int shift = 31 - bitLength(x3);
/*  459 */       pow2 -= shift;
/*  460 */       m = (shift < 0) ? (x3 << 31L | x2 >>> 1L) : ((x3 << 32L | x2) << shift | x1 >>> 32 - shift);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  466 */       long x1 = m;
/*  467 */       long x0 = 0L;
/*      */ 
/*      */       
/*      */       while (true) {
/*  471 */         int shift = 63 - bitLength(x1);
/*  472 */         x1 <<= shift;
/*  473 */         x1 |= x0 >>> 63 - shift;
/*  474 */         x0 = x0 << shift & Long.MAX_VALUE;
/*  475 */         pow2 -= shift;
/*      */ 
/*      */         
/*  478 */         if (n == 0) {
/*      */           break;
/*      */         }
/*      */         
/*  482 */         int i = (-n >= POW5_INT.length) ? (POW5_INT.length - 1) : -n;
/*  483 */         int divisor = POW5_INT[i];
/*      */ 
/*      */         
/*  486 */         long wh = x1 >>> 32L;
/*  487 */         long qh = wh / divisor;
/*  488 */         long r = wh - qh * divisor;
/*  489 */         long wl = r << 32L | x1 & 0xFFFFFFFFL;
/*  490 */         long ql = wl / divisor;
/*  491 */         r = wl - ql * divisor;
/*  492 */         x1 = qh << 32L | ql;
/*      */         
/*  494 */         wh = r << 31L | x0 >>> 32L;
/*  495 */         qh = wh / divisor;
/*  496 */         r = wh - qh * divisor;
/*  497 */         wl = r << 32L | x0 & 0xFFFFFFFFL;
/*  498 */         ql = wl / divisor;
/*  499 */         x0 = qh << 32L | ql;
/*      */ 
/*      */         
/*  502 */         n += i;
/*  503 */         pow2 -= i;
/*      */       } 
/*  505 */       m = x1;
/*      */     } 
/*  507 */     if (pow2 > 0)
/*  508 */       throw new ArithmeticException("Overflow"); 
/*  509 */     if (pow2 < -63)
/*  510 */       return 0L; 
/*  511 */     m = (m >> -pow2) + (m >> -(pow2 + 1) & 0x1L);
/*  512 */     return isNegative ? -m : m;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int floorLog2(double d) {
/*  527 */     if (d <= 0.0D)
/*  528 */       throw new ArithmeticException("Negative number or zero"); 
/*  529 */     long bits = Double.doubleToLongBits(d);
/*  530 */     int exp = (int)(bits >> 52L) & 0x7FF;
/*  531 */     if (exp == 2047)
/*  532 */       throw new ArithmeticException("Infinity or NaN"); 
/*  533 */     if (exp == 0)
/*  534 */       return floorLog2(d * 1.8014398509481984E16D) - 54; 
/*  535 */     return exp - 1023;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int floorLog10(double d) {
/*  550 */     int guess = (int)(0.3010299956639812D * floorLog2(d));
/*  551 */     double pow10 = toDoublePow10(1L, guess);
/*  552 */     if (pow10 <= d && pow10 * 10.0D > d)
/*  553 */       return guess; 
/*  554 */     if (pow10 > d)
/*  555 */       return guess - 1; 
/*  556 */     return guess + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toRadians(double degrees) {
/*  624 */     return degrees * 0.017453292519943295D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double toDegrees(double radians) {
/*  636 */     return radians * 57.29577951308232D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double sqrt(double x) {
/*  648 */     return Math.sqrt(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double rem(double x, double y) {
/*  661 */     double tmp = x / y;
/*  662 */     if (abs(tmp) <= 9.223372036854776E18D) {
/*  663 */       return x - round(tmp) * y;
/*      */     }
/*  665 */     return Double.NaN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double ceil(double x) {
/*  679 */     return Math.ceil(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double floor(double x) {
/*  693 */     return Math.floor(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double sin(double radians) {
/*  705 */     return Math.sin(radians);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double cos(double radians) {
/*  717 */     return Math.cos(radians);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double tan(double radians) {
/*  729 */     return Math.tan(radians);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double asin(double x) {
/*  742 */     if (x < -1.0D || x > 1.0D)
/*  743 */       return Double.NaN; 
/*  744 */     if (x == -1.0D)
/*  745 */       return -1.5707963267948966D; 
/*  746 */     if (x == 1.0D)
/*  747 */       return 1.5707963267948966D; 
/*  748 */     return atan(x / sqrt(1.0D - x * x));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double acos(double x) {
/*  761 */     return 1.5707963267948966D - asin(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double atan(double x) {
/*  776 */     return _atan(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double atan2(double y, double x) {
/*  792 */     if (x > 0.0D) return atan(y / x); 
/*  793 */     if (y >= 0.0D && x < 0.0D) return atan(y / x) + Math.PI; 
/*  794 */     if (y < 0.0D && x < 0.0D) return atan(y / x) - Math.PI; 
/*  795 */     if (y > 0.0D && x == 0.0D) return 1.5707963267948966D; 
/*  796 */     if (y < 0.0D && x == 0.0D) return -1.5707963267948966D; 
/*  797 */     return Double.NaN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double sinh(double x) {
/*  809 */     return (exp(x) - exp(-x)) * 0.5D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double cosh(double x) {
/*  821 */     return (exp(x) + exp(-x)) * 0.5D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double tanh(double x) {
/*  833 */     return (exp(2.0D * x) - 1.0D) / (exp(2.0D * x) + 1.0D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double exp(double x) {
/*  847 */     return _ieee754_exp(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double log(double x) {
/*  860 */     return _ieee754_log(x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double log10(double x) {
/*  872 */     return log(x) * INV_LOG10;
/*      */   }
/*      */   
/*  875 */   private static double INV_LOG10 = 0.4342944819032518D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double pow(double x, double y) {
/*  887 */     if (x < 0.0D && y == (int)y)
/*  888 */       return (((int)y & 0x1) == 0) ? pow(-x, y) : -pow(-x, y); 
/*  889 */     return exp(y * log(x));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int round(float f) {
/*  899 */     return (int)floor((f + 0.5F));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long round(double d) {
/*  912 */     return (long)floor(d + 0.5D);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int abs(int i) {
/*  922 */     return (i < 0) ? -i : i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long abs(long l) {
/*  932 */     return (l < 0L) ? -l : l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float abs(float f) {
/*  942 */     return (f < 0.0F) ? -f : f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double abs(double d) {
/*  952 */     return (d < 0.0D) ? -d : d;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int max(int x, int y) {
/*  965 */     return (x >= y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long max(long x, long y) {
/*  976 */     return (x >= y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float max(float x, float y) {
/*  987 */     return (x >= y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double max(double x, double y) {
/*  998 */     return (x >= y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int min(int x, int y) {
/* 1011 */     return (x < y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long min(long x, long y) {
/* 1022 */     return (x < y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float min(float x, float y) {
/* 1033 */     return (x < y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double min(double x, double y) {
/* 1046 */     return (x < y) ? x : y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1082 */   static final double[] atanhi = new double[] { 0.4636476090008061D, 0.7853981633974483D, 0.982793723247329D, 1.5707963267948966D };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1088 */   static final double[] atanlo = new double[] { 2.2698777452961687E-17D, 3.061616997868383E-17D, 1.3903311031230998E-17D, 6.123233995736766E-17D };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1094 */   static final double[] aT = new double[] { 0.3333333333333293D, -0.19999999999876483D, 0.14285714272503466D, -0.11111110405462356D, 0.09090887133436507D, -0.0769187620504483D, 0.06661073137387531D, -0.058335701337905735D, 0.049768779946159324D, -0.036531572744216916D, 0.016285820115365782D };
/*      */   
/*      */   static final double one = 1.0D;
/*      */   static final double huge = 1.0E300D;
/*      */   static final double ln2_hi = 0.6931471803691238D;
/*      */   static final double ln2_lo = 1.9082149292705877E-10D;
/*      */   static final double two54 = 1.8014398509481984E16D;
/*      */   static final double Lg1 = 0.6666666666666735D;
/*      */   static final double Lg2 = 0.3999999999940942D;
/*      */   static final double Lg3 = 0.2857142874366239D;
/*      */   static final double Lg4 = 0.22222198432149784D;
/*      */   static final double Lg5 = 0.1818357216161805D;
/*      */   static final double Lg6 = 0.15313837699209373D;
/*      */   static final double Lg7 = 0.14798198605116586D;
/*      */   static final double zero = 0.0D;
/*      */   
/*      */   static double _atan(double x) {
/*      */     int id;
/* 1112 */     long xBits = Double.doubleToLongBits(x);
/* 1113 */     int __HIx = (int)(xBits >> 32L);
/* 1114 */     int __LOx = (int)xBits;
/*      */     
/* 1116 */     int hx = __HIx;
/* 1117 */     int ix = hx & Integer.MAX_VALUE;
/* 1118 */     if (ix >= 1141899264) {
/* 1119 */       if (ix > 2146435072 || (ix == 2146435072 && __LOx != 0))
/* 1120 */         return x + x; 
/* 1121 */       if (hx > 0) {
/* 1122 */         return atanhi[3] + atanlo[3];
/*      */       }
/* 1124 */       return -atanhi[3] - atanlo[3];
/*      */     } 
/* 1126 */     if (ix < 1071382528) {
/* 1127 */       if (ix < 1042284544 && 
/* 1128 */         1.0E300D + x > 1.0D)
/* 1129 */         return x; 
/* 1130 */       id = -1;
/*      */     } else {
/* 1132 */       x = abs(x);
/* 1133 */       if (ix < 1072889856) {
/* 1134 */         if (ix < 1072037888) {
/* 1135 */           id = 0;
/* 1136 */           x = (2.0D * x - 1.0D) / (2.0D + x);
/*      */         } else {
/* 1138 */           id = 1;
/* 1139 */           x = (x - 1.0D) / (x + 1.0D);
/*      */         } 
/* 1141 */       } else if (ix < 1073971200) {
/* 1142 */         id = 2;
/* 1143 */         x = (x - 1.5D) / (1.0D + 1.5D * x);
/*      */       } else {
/* 1145 */         id = 3;
/* 1146 */         x = -1.0D / x;
/*      */       } 
/*      */     } 
/*      */     
/* 1150 */     double z = x * x;
/* 1151 */     double w = z * z;
/*      */     
/* 1153 */     double s1 = z * (aT[0] + w * (aT[2] + w * (aT[4] + w * (aT[6] + w * (aT[8] + w * aT[10])))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1158 */     double s2 = w * (aT[1] + w * (aT[3] + w * (aT[5] + w * (aT[7] + w * aT[9]))));
/* 1159 */     if (id < 0) {
/* 1160 */       return x - x * (s1 + s2);
/*      */     }
/* 1162 */     z = atanhi[id] - x * (s1 + s2) - atanlo[id] - x;
/* 1163 */     return (hx < 0) ? -z : z;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static double _ieee754_log(double x) {
/* 1249 */     long xBits = Double.doubleToLongBits(x);
/* 1250 */     int hx = (int)(xBits >> 32L);
/* 1251 */     int lx = (int)xBits;
/*      */     
/* 1253 */     int k = 0;
/* 1254 */     if (hx < 1048576) {
/* 1255 */       if ((hx & Integer.MAX_VALUE | lx) == 0)
/* 1256 */         return Double.NEGATIVE_INFINITY; 
/* 1257 */       if (hx < 0)
/* 1258 */         return (x - x) / 0.0D; 
/* 1259 */       k -= 54;
/* 1260 */       x *= 1.8014398509481984E16D;
/* 1261 */       xBits = Double.doubleToLongBits(x);
/* 1262 */       hx = (int)(xBits >> 32L);
/*      */     } 
/* 1264 */     if (hx >= 2146435072)
/* 1265 */       return x + x; 
/* 1266 */     k += (hx >> 20) - 1023;
/* 1267 */     hx &= 0xFFFFF;
/* 1268 */     int i = hx + 614244 & 0x100000;
/* 1269 */     xBits = Double.doubleToLongBits(x);
/* 1270 */     int HIx = hx | i ^ 0x3FF00000;
/* 1271 */     xBits = (HIx & 0xFFFFFFFFL) << 32L | xBits & 0xFFFFFFFFL;
/* 1272 */     x = Double.longBitsToDouble(xBits);
/* 1273 */     k += i >> 20;
/* 1274 */     double f = x - 1.0D;
/* 1275 */     if ((0xFFFFF & 2 + hx) < 3) {
/* 1276 */       if (f == 0.0D) {
/* 1277 */         if (k == 0) {
/* 1278 */           return 0.0D;
/*      */         }
/* 1280 */         double d = k;
/* 1281 */         return d * 0.6931471803691238D + d * 1.9082149292705877E-10D;
/*      */       } 
/* 1283 */       double d1 = f * f * (0.5D - 0.3333333333333333D * f);
/* 1284 */       if (k == 0) {
/* 1285 */         return f - d1;
/*      */       }
/* 1287 */       double d2 = k;
/* 1288 */       return d2 * 0.6931471803691238D - d1 - d2 * 1.9082149292705877E-10D - f;
/*      */     } 
/*      */     
/* 1291 */     double s = f / (2.0D + f);
/* 1292 */     double dk = k;
/* 1293 */     double z = s * s;
/* 1294 */     i = hx - 398458;
/* 1295 */     double w = z * z;
/* 1296 */     int j = 440401 - hx;
/* 1297 */     double t1 = w * (0.3999999999940942D + w * (0.22222198432149784D + w * 0.15313837699209373D));
/* 1298 */     double t2 = z * (0.6666666666666735D + w * (0.2857142874366239D + w * (0.1818357216161805D + w * 0.14798198605116586D)));
/* 1299 */     i |= j;
/* 1300 */     double R = t2 + t1;
/* 1301 */     if (i > 0) {
/* 1302 */       double hfsq = 0.5D * f * f;
/* 1303 */       if (k == 0) {
/* 1304 */         return f - hfsq - s * (hfsq + R);
/*      */       }
/* 1306 */       return dk * 0.6931471803691238D - hfsq - s * (hfsq + R) + dk * 1.9082149292705877E-10D - f;
/*      */     } 
/* 1308 */     if (k == 0) {
/* 1309 */       return f - s * (f - R);
/*      */     }
/* 1311 */     return dk * 0.6931471803691238D - s * (f - R) - dk * 1.9082149292705877E-10D - f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1389 */   static final double[] halF = new double[] { 0.5D, -0.5D };
/*      */   static final double twom1000 = 9.332636185032189E-302D;
/*      */   static final double o_threshold = 709.782712893384D;
/*      */   static final double u_threshold = -745.1332191019411D;
/* 1393 */   static final double[] ln2HI = new double[] { 0.6931471803691238D, -0.6931471803691238D };
/*      */   
/* 1395 */   static final double[] ln2LO = new double[] { 1.9082149292705877E-10D, -1.9082149292705877E-10D };
/*      */   
/*      */   static final double invln2 = 1.4426950408889634D;
/*      */   
/*      */   static final double P1 = 0.16666666666666602D;
/*      */   static final double P2 = -0.0027777777777015593D;
/*      */   static final double P3 = 6.613756321437934E-5D;
/*      */   static final double P4 = -1.6533902205465252E-6D;
/*      */   static final double P5 = 4.1381367970572385E-8D;
/*      */   
/*      */   static double _ieee754_exp(double x) {
/* 1406 */     double hi = 0.0D, lo = 0.0D;
/* 1407 */     int k = 0;
/*      */     
/* 1409 */     long xBits = Double.doubleToLongBits(x);
/* 1410 */     int __HIx = (int)(xBits >> 32L);
/* 1411 */     int __LOx = (int)xBits;
/*      */     
/* 1413 */     int hx = __HIx;
/* 1414 */     int xsb = hx >> 31 & 0x1;
/* 1415 */     hx &= Integer.MAX_VALUE;
/*      */ 
/*      */     
/* 1418 */     if (hx >= 1082535490) {
/* 1419 */       if (hx >= 2146435072) {
/* 1420 */         if ((hx & 0xFFFFF | __LOx) != 0) {
/* 1421 */           return x + x;
/*      */         }
/* 1423 */         return (xsb == 0) ? x : 0.0D;
/* 1424 */       }  if (x > 709.782712893384D)
/* 1425 */         return Double.POSITIVE_INFINITY; 
/* 1426 */       if (x < -745.1332191019411D) {
/* 1427 */         return 0.0D;
/*      */       }
/*      */     } 
/*      */     
/* 1431 */     if (hx > 1071001154) {
/* 1432 */       if (hx < 1072734898) {
/* 1433 */         hi = x - ln2HI[xsb];
/* 1434 */         lo = ln2LO[xsb];
/* 1435 */         k = 1 - xsb - xsb;
/*      */       } else {
/* 1437 */         k = (int)(1.4426950408889634D * x + halF[xsb]);
/* 1438 */         double d = k;
/* 1439 */         hi = x - d * ln2HI[0];
/* 1440 */         lo = d * ln2LO[0];
/*      */       } 
/* 1442 */       x = hi - lo;
/* 1443 */     } else if (hx < 1043333120) {
/* 1444 */       if (1.0E300D + x > 1.0D)
/* 1445 */         return 1.0D + x; 
/*      */     } else {
/* 1447 */       k = 0;
/*      */     } 
/*      */     
/* 1450 */     double t = x * x;
/* 1451 */     double c = x - t * (0.16666666666666602D + t * (-0.0027777777777015593D + t * (6.613756321437934E-5D + t * (-1.6533902205465252E-6D + t * 4.1381367970572385E-8D))));
/* 1452 */     if (k == 0) {
/* 1453 */       return 1.0D - x * c / (c - 2.0D) - x;
/*      */     }
/* 1455 */     double y = 1.0D - lo - x * c / (2.0D - c) - hi;
/* 1456 */     long yBits = Double.doubleToLongBits(y);
/* 1457 */     int __HIy = (int)(yBits >> 32L);
/* 1458 */     if (k >= -1021) {
/* 1459 */       __HIy += k << 20;
/* 1460 */       yBits = (__HIy & 0xFFFFFFFFL) << 32L | yBits & 0xFFFFFFFFL;
/* 1461 */       y = Double.longBitsToDouble(yBits);
/* 1462 */       return y;
/*      */     } 
/* 1464 */     __HIy += k + 1000 << 20;
/* 1465 */     yBits = (__HIy & 0xFFFFFFFFL) << 32L | yBits & 0xFFFFFFFFL;
/* 1466 */     y = Double.longBitsToDouble(yBits);
/* 1467 */     return y * 9.332636185032189E-302D;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/lang/MathLib.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */