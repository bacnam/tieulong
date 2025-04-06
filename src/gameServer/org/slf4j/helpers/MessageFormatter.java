/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg) {
/* 124 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */   public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
/* 151 */     return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   static final Throwable getThrowableCandidate(Object[] argArray) {
/* 155 */     if (argArray == null || argArray.length == 0) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     Object lastEntry = argArray[argArray.length - 1];
/* 160 */     if (lastEntry instanceof Throwable) {
/* 161 */       return (Throwable)lastEntry;
/*     */     }
/* 163 */     return null;
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
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
/* 180 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/*     */     
/* 182 */     if (messagePattern == null) {
/* 183 */       return new FormattingTuple(null, argArray, throwableCandidate);
/*     */     }
/*     */     
/* 186 */     if (argArray == null) {
/* 187 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 190 */     int i = 0;
/*     */ 
/*     */     
/* 193 */     StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
/*     */     
/*     */     int L;
/* 196 */     for (L = 0; L < argArray.length; L++) {
/*     */       
/* 198 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 200 */       if (j == -1) {
/*     */         
/* 202 */         if (i == 0) {
/* 203 */           return new FormattingTuple(messagePattern, argArray, throwableCandidate);
/*     */         }
/*     */         
/* 206 */         sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 207 */         return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */       } 
/*     */       
/* 210 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 211 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 212 */           L--;
/* 213 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 214 */           sbuf.append('{');
/* 215 */           i = j + 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 220 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 221 */           deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<Object, Object>());
/* 222 */           i = j + 2;
/*     */         } 
/*     */       } else {
/*     */         
/* 226 */         sbuf.append(messagePattern.substring(i, j));
/* 227 */         deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<Object, Object>());
/* 228 */         i = j + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 233 */     sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 234 */     if (L < argArray.length - 1) {
/* 235 */       return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */     }
/* 237 */     return new FormattingTuple(sbuf.toString(), argArray, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
/* 243 */     if (delimeterStartIndex == 0) {
/* 244 */       return false;
/*     */     }
/* 246 */     char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
/* 247 */     if (potentialEscape == '\\') {
/* 248 */       return true;
/*     */     }
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
/* 255 */     if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
/* 256 */       return true;
/*     */     }
/* 258 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
/* 264 */     if (o == null) {
/* 265 */       sbuf.append("null");
/*     */       return;
/*     */     } 
/* 268 */     if (!o.getClass().isArray()) {
/* 269 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */     
/*     */     }
/* 273 */     else if (o instanceof boolean[]) {
/* 274 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 275 */     } else if (o instanceof byte[]) {
/* 276 */       byteArrayAppend(sbuf, (byte[])o);
/* 277 */     } else if (o instanceof char[]) {
/* 278 */       charArrayAppend(sbuf, (char[])o);
/* 279 */     } else if (o instanceof short[]) {
/* 280 */       shortArrayAppend(sbuf, (short[])o);
/* 281 */     } else if (o instanceof int[]) {
/* 282 */       intArrayAppend(sbuf, (int[])o);
/* 283 */     } else if (o instanceof long[]) {
/* 284 */       longArrayAppend(sbuf, (long[])o);
/* 285 */     } else if (o instanceof float[]) {
/* 286 */       floatArrayAppend(sbuf, (float[])o);
/* 287 */     } else if (o instanceof double[]) {
/* 288 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 290 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void safeObjectAppend(StringBuilder sbuf, Object o) {
/*     */     try {
/* 297 */       String oAsString = o.toString();
/* 298 */       sbuf.append(oAsString);
/* 299 */     } catch (Throwable t) {
/* 300 */       System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
/* 301 */       t.printStackTrace();
/* 302 */       sbuf.append("[FAILED toString()]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
/* 308 */     sbuf.append('[');
/* 309 */     if (!seenMap.containsKey(a)) {
/* 310 */       seenMap.put(a, null);
/* 311 */       int len = a.length;
/* 312 */       for (int i = 0; i < len; i++) {
/* 313 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 314 */         if (i != len - 1) {
/* 315 */           sbuf.append(", ");
/*     */         }
/*     */       } 
/* 318 */       seenMap.remove(a);
/*     */     } else {
/* 320 */       sbuf.append("...");
/*     */     } 
/* 322 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
/* 326 */     sbuf.append('[');
/* 327 */     int len = a.length;
/* 328 */     for (int i = 0; i < len; i++) {
/* 329 */       sbuf.append(a[i]);
/* 330 */       if (i != len - 1)
/* 331 */         sbuf.append(", "); 
/*     */     } 
/* 333 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
/* 337 */     sbuf.append('[');
/* 338 */     int len = a.length;
/* 339 */     for (int i = 0; i < len; i++) {
/* 340 */       sbuf.append(a[i]);
/* 341 */       if (i != len - 1)
/* 342 */         sbuf.append(", "); 
/*     */     } 
/* 344 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuilder sbuf, char[] a) {
/* 348 */     sbuf.append('[');
/* 349 */     int len = a.length;
/* 350 */     for (int i = 0; i < len; i++) {
/* 351 */       sbuf.append(a[i]);
/* 352 */       if (i != len - 1)
/* 353 */         sbuf.append(", "); 
/*     */     } 
/* 355 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
/* 359 */     sbuf.append('[');
/* 360 */     int len = a.length;
/* 361 */     for (int i = 0; i < len; i++) {
/* 362 */       sbuf.append(a[i]);
/* 363 */       if (i != len - 1)
/* 364 */         sbuf.append(", "); 
/*     */     } 
/* 366 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuilder sbuf, int[] a) {
/* 370 */     sbuf.append('[');
/* 371 */     int len = a.length;
/* 372 */     for (int i = 0; i < len; i++) {
/* 373 */       sbuf.append(a[i]);
/* 374 */       if (i != len - 1)
/* 375 */         sbuf.append(", "); 
/*     */     } 
/* 377 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuilder sbuf, long[] a) {
/* 381 */     sbuf.append('[');
/* 382 */     int len = a.length;
/* 383 */     for (int i = 0; i < len; i++) {
/* 384 */       sbuf.append(a[i]);
/* 385 */       if (i != len - 1)
/* 386 */         sbuf.append(", "); 
/*     */     } 
/* 388 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
/* 392 */     sbuf.append('[');
/* 393 */     int len = a.length;
/* 394 */     for (int i = 0; i < len; i++) {
/* 395 */       sbuf.append(a[i]);
/* 396 */       if (i != len - 1)
/* 397 */         sbuf.append(", "); 
/*     */     } 
/* 399 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
/* 403 */     sbuf.append('[');
/* 404 */     int len = a.length;
/* 405 */     for (int i = 0; i < len; i++) {
/* 406 */       sbuf.append(a[i]);
/* 407 */       if (i != len - 1)
/* 408 */         sbuf.append(", "); 
/*     */     } 
/* 410 */     sbuf.append(']');
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/MessageFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */