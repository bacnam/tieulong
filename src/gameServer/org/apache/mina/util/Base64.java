/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.security.InvalidParameterException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64
/*     */ {
/*     */   static final int CHUNK_SIZE = 76;
/*  58 */   static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int BASELENGTH = 255;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int LOOKUPLENGTH = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int EIGHTBIT = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int SIXTEENBIT = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int TWENTYFOURBITGROUP = 24;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int FOURBYTE = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int SIGN = -128;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final byte PAD = 61;
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static byte[] base64Alphabet = new byte[255];
/*     */   
/* 104 */   private static byte[] lookUpBase64Alphabet = new byte[64];
/*     */   
/*     */   static {
/*     */     int i;
/* 108 */     for (i = 0; i < 255; i++) {
/* 109 */       base64Alphabet[i] = -1;
/*     */     }
/* 111 */     for (i = 90; i >= 65; i--) {
/* 112 */       base64Alphabet[i] = (byte)(i - 65);
/*     */     }
/* 114 */     for (i = 122; i >= 97; i--) {
/* 115 */       base64Alphabet[i] = (byte)(i - 97 + 26);
/*     */     }
/* 117 */     for (i = 57; i >= 48; i--) {
/* 118 */       base64Alphabet[i] = (byte)(i - 48 + 52);
/*     */     }
/*     */     
/* 121 */     base64Alphabet[43] = 62;
/* 122 */     base64Alphabet[47] = 63;
/*     */     
/* 124 */     for (i = 0; i <= 25; i++) {
/* 125 */       lookUpBase64Alphabet[i] = (byte)(65 + i);
/*     */     }
/*     */     int j;
/* 128 */     for (i = 26, j = 0; i <= 51; i++, j++) {
/* 129 */       lookUpBase64Alphabet[i] = (byte)(97 + j);
/*     */     }
/*     */     
/* 132 */     for (i = 52, j = 0; i <= 61; i++, j++) {
/* 133 */       lookUpBase64Alphabet[i] = (byte)(48 + j);
/*     */     }
/*     */     
/* 136 */     lookUpBase64Alphabet[62] = 43;
/* 137 */     lookUpBase64Alphabet[63] = 47;
/*     */   }
/*     */   
/*     */   private static boolean isBase64(byte octect) {
/* 141 */     if (octect == 61)
/* 142 */       return true; 
/* 143 */     if (base64Alphabet[octect] == -1) {
/* 144 */       return false;
/*     */     }
/* 146 */     return true;
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
/*     */   public static boolean isArrayByteBase64(byte[] arrayOctect) {
/* 160 */     arrayOctect = discardWhitespace(arrayOctect);
/*     */     
/* 162 */     int length = arrayOctect.length;
/* 163 */     if (length == 0)
/*     */     {
/* 165 */       return true;
/*     */     }
/* 167 */     for (int i = 0; i < length; i++) {
/* 168 */       if (!isBase64(arrayOctect[i])) {
/* 169 */         return false;
/*     */       }
/*     */     } 
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64(byte[] binaryData) {
/* 183 */     return encodeBase64(binaryData, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64Chunked(byte[] binaryData) {
/* 194 */     return encodeBase64(binaryData, true);
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
/*     */   public Object decode(Object pObject) {
/* 210 */     if (!(pObject instanceof byte[])) {
/* 211 */       throw new InvalidParameterException("Parameter supplied to Base64 decode is not a byte[]");
/*     */     }
/* 213 */     return decode((byte[])pObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] pArray) {
/* 224 */     return decodeBase64(pArray);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
/* 237 */     int lengthDataBits = binaryData.length * 8;
/* 238 */     int fewerThan24bits = lengthDataBits % 24;
/* 239 */     int numberTriplets = lengthDataBits / 24;
/* 240 */     byte[] encodedData = null;
/* 241 */     int encodedDataLength = 0;
/* 242 */     int nbrChunks = 0;
/*     */     
/* 244 */     if (fewerThan24bits != 0) {
/*     */       
/* 246 */       encodedDataLength = (numberTriplets + 1) * 4;
/*     */     } else {
/*     */       
/* 249 */       encodedDataLength = numberTriplets * 4;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     if (isChunked) {
/*     */       
/* 257 */       nbrChunks = (CHUNK_SEPARATOR.length == 0) ? 0 : (int)Math.ceil((encodedDataLength / 76.0F));
/* 258 */       encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;
/*     */     } 
/*     */     
/* 261 */     encodedData = new byte[encodedDataLength];
/*     */     
/* 263 */     byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;
/*     */     
/* 265 */     int encodedIndex = 0;
/* 266 */     int dataIndex = 0;
/* 267 */     int i = 0;
/* 268 */     int nextSeparatorIndex = 76;
/* 269 */     int chunksSoFar = 0;
/*     */     
/* 271 */     for (i = 0; i < numberTriplets; i++) {
/* 272 */       dataIndex = i * 3;
/* 273 */       b1 = binaryData[dataIndex];
/* 274 */       b2 = binaryData[dataIndex + 1];
/* 275 */       b3 = binaryData[dataIndex + 2];
/*     */       
/* 277 */       l = (byte)(b2 & 0xF);
/* 278 */       k = (byte)(b1 & 0x3);
/*     */       
/* 280 */       byte val1 = ((b1 & Byte.MIN_VALUE) == 0) ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/* 281 */       byte val2 = ((b2 & Byte.MIN_VALUE) == 0) ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 0xF0);
/* 282 */       byte val3 = ((b3 & Byte.MIN_VALUE) == 0) ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 0xFC);
/*     */       
/* 284 */       encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
/* 285 */       encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
/* 286 */       encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
/* 287 */       encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 0x3F];
/*     */       
/* 289 */       encodedIndex += 4;
/*     */ 
/*     */       
/* 292 */       if (isChunked)
/*     */       {
/* 294 */         if (encodedIndex == nextSeparatorIndex) {
/* 295 */           System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
/* 296 */           chunksSoFar++;
/* 297 */           nextSeparatorIndex = 76 * (chunksSoFar + 1) + chunksSoFar * CHUNK_SEPARATOR.length;
/* 298 */           encodedIndex += CHUNK_SEPARATOR.length;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 304 */     dataIndex = i * 3;
/*     */     
/* 306 */     if (fewerThan24bits == 8) {
/* 307 */       b1 = binaryData[dataIndex];
/* 308 */       k = (byte)(b1 & 0x3);
/* 309 */       byte val1 = ((b1 & Byte.MIN_VALUE) == 0) ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/* 310 */       encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
/* 311 */       encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
/* 312 */       encodedData[encodedIndex + 2] = 61;
/* 313 */       encodedData[encodedIndex + 3] = 61;
/* 314 */     } else if (fewerThan24bits == 16) {
/*     */       
/* 316 */       b1 = binaryData[dataIndex];
/* 317 */       b2 = binaryData[dataIndex + 1];
/* 318 */       l = (byte)(b2 & 0xF);
/* 319 */       k = (byte)(b1 & 0x3);
/*     */       
/* 321 */       byte val1 = ((b1 & Byte.MIN_VALUE) == 0) ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 0xC0);
/* 322 */       byte val2 = ((b2 & Byte.MIN_VALUE) == 0) ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 0xF0);
/*     */       
/* 324 */       encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
/* 325 */       encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
/* 326 */       encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
/* 327 */       encodedData[encodedIndex + 3] = 61;
/*     */     } 
/*     */     
/* 330 */     if (isChunked)
/*     */     {
/* 332 */       if (chunksSoFar < nbrChunks) {
/* 333 */         System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 338 */     return encodedData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(byte[] base64Data) {
/* 349 */     base64Data = discardNonBase64(base64Data);
/*     */ 
/*     */     
/* 352 */     if (base64Data.length == 0) {
/* 353 */       return new byte[0];
/*     */     }
/*     */     
/* 356 */     int numberQuadruple = base64Data.length / 4;
/* 357 */     byte[] decodedData = null;
/* 358 */     byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;
/*     */ 
/*     */ 
/*     */     
/* 362 */     int encodedIndex = 0;
/* 363 */     int dataIndex = 0;
/*     */ 
/*     */     
/* 366 */     int lastData = base64Data.length;
/*     */     
/* 368 */     while (base64Data[lastData - 1] == 61) {
/* 369 */       if (--lastData == 0) {
/* 370 */         return new byte[0];
/*     */       }
/*     */     } 
/* 373 */     decodedData = new byte[lastData - numberQuadruple];
/*     */ 
/*     */     
/* 376 */     for (int i = 0; i < numberQuadruple; i++) {
/* 377 */       dataIndex = i * 4;
/* 378 */       marker0 = base64Data[dataIndex + 2];
/* 379 */       marker1 = base64Data[dataIndex + 3];
/*     */       
/* 381 */       b1 = base64Alphabet[base64Data[dataIndex]];
/* 382 */       b2 = base64Alphabet[base64Data[dataIndex + 1]];
/*     */       
/* 384 */       if (marker0 != 61 && marker1 != 61) {
/*     */         
/* 386 */         b3 = base64Alphabet[marker0];
/* 387 */         b4 = base64Alphabet[marker1];
/*     */         
/* 389 */         decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
/* 390 */         decodedData[encodedIndex + 1] = (byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
/* 391 */         decodedData[encodedIndex + 2] = (byte)(b3 << 6 | b4);
/* 392 */       } else if (marker0 == 61) {
/*     */         
/* 394 */         decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
/* 395 */       } else if (marker1 == 61) {
/*     */         
/* 397 */         b3 = base64Alphabet[marker0];
/*     */         
/* 399 */         decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
/* 400 */         decodedData[encodedIndex + 1] = (byte)((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
/*     */       } 
/* 402 */       encodedIndex += 3;
/*     */     } 
/* 404 */     return decodedData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] discardWhitespace(byte[] data) {
/* 415 */     byte[] groomedData = new byte[data.length];
/* 416 */     int bytesCopied = 0;
/*     */     
/* 418 */     for (int i = 0; i < data.length; i++) {
/* 419 */       switch (data[i]) {
/*     */         case 9:
/*     */         case 10:
/*     */         case 13:
/*     */         case 32:
/*     */           break;
/*     */         default:
/* 426 */           groomedData[bytesCopied++] = data[i];
/*     */           break;
/*     */       } 
/*     */     } 
/* 430 */     byte[] packedData = new byte[bytesCopied];
/*     */     
/* 432 */     System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
/*     */     
/* 434 */     return packedData;
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
/*     */   static byte[] discardNonBase64(byte[] data) {
/* 447 */     byte[] groomedData = new byte[data.length];
/* 448 */     int bytesCopied = 0;
/*     */     
/* 450 */     for (int i = 0; i < data.length; i++) {
/* 451 */       if (isBase64(data[i])) {
/* 452 */         groomedData[bytesCopied++] = data[i];
/*     */       }
/*     */     } 
/*     */     
/* 456 */     byte[] packedData = new byte[bytesCopied];
/*     */     
/* 458 */     System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
/*     */     
/* 460 */     return packedData;
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
/*     */   public Object encode(Object pObject) {
/* 478 */     if (!(pObject instanceof byte[])) {
/* 479 */       throw new InvalidParameterException("Parameter supplied to Base64 encode is not a byte[]");
/*     */     }
/* 481 */     return encode((byte[])pObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] pArray) {
/* 492 */     return encodeBase64(pArray, false);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/Base64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */