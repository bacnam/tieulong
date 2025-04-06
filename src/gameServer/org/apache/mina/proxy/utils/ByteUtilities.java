/*     */ package org.apache.mina.proxy.utils;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteUtilities
/*     */ {
/*     */   public static int networkByteOrderToInt(byte[] buf, int start, int count) {
/*  41 */     if (count > 4) {
/*  42 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */     
/*  45 */     int result = 0;
/*     */     
/*  47 */     for (int i = 0; i < count; i++) {
/*  48 */       result <<= 8;
/*  49 */       result |= buf[start + i] & 0xFF;
/*     */     } 
/*     */     
/*  52 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] intToNetworkByteOrder(int num, int count) {
/*  63 */     byte[] buf = new byte[count];
/*  64 */     intToNetworkByteOrder(num, buf, 0, count);
/*     */     
/*  66 */     return buf;
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
/*     */   public static void intToNetworkByteOrder(int num, byte[] buf, int start, int count) {
/*  80 */     if (count > 4) {
/*  81 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */     
/*  84 */     for (int i = count - 1; i >= 0; i--) {
/*  85 */       buf[start + i] = (byte)(num & 0xFF);
/*  86 */       num >>>= 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] writeShort(short v) {
/*  96 */     return writeShort(v, new byte[2], 0);
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
/*     */   public static final byte[] writeShort(short v, byte[] b, int offset) {
/* 108 */     b[offset] = (byte)v;
/* 109 */     b[offset + 1] = (byte)(v >> 8);
/*     */     
/* 111 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] writeInt(int v) {
/* 120 */     return writeInt(v, new byte[4], 0);
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
/*     */   public static final byte[] writeInt(int v, byte[] b, int offset) {
/* 132 */     b[offset] = (byte)v;
/* 133 */     b[offset + 1] = (byte)(v >> 8);
/* 134 */     b[offset + 2] = (byte)(v >> 16);
/* 135 */     b[offset + 3] = (byte)(v >> 24);
/*     */     
/* 137 */     return b;
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
/*     */   public static final void changeWordEndianess(byte[] b, int offset, int length) {
/* 153 */     for (int i = offset; i < offset + length; i += 4) {
/* 154 */       byte tmp = b[i];
/* 155 */       b[i] = b[i + 3];
/* 156 */       b[i + 3] = tmp;
/* 157 */       tmp = b[i + 1];
/* 158 */       b[i + 1] = b[i + 2];
/* 159 */       b[i + 2] = tmp;
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
/*     */ 
/*     */   
/*     */   public static final void changeByteEndianess(byte[] b, int offset, int length) {
/* 176 */     for (int i = offset; i < offset + length; i += 2) {
/* 177 */       byte tmp = b[i];
/* 178 */       b[i] = b[i + 1];
/* 179 */       b[i + 1] = tmp;
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
/*     */   public static final byte[] getOEMStringAsByteArray(String s) throws UnsupportedEncodingException {
/* 192 */     return s.getBytes("ASCII");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] getUTFStringAsByteArray(String s) throws UnsupportedEncodingException {
/* 203 */     return s.getBytes("UTF-16LE");
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
/*     */   public static final byte[] encodeString(String s, boolean useUnicode) throws UnsupportedEncodingException {
/* 217 */     if (useUnicode) {
/* 218 */       return getUTFStringAsByteArray(s);
/*     */     }
/*     */     
/* 221 */     return getOEMStringAsByteArray(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String asHex(byte[] bytes) {
/* 231 */     return asHex(bytes, null);
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
/*     */   public static String asHex(byte[] bytes, String separator) {
/* 243 */     StringBuilder sb = new StringBuilder();
/* 244 */     for (int i = 0; i < bytes.length; i++) {
/* 245 */       String code = Integer.toHexString(bytes[i] & 0xFF);
/* 246 */       if ((bytes[i] & 0xFF) < 16) {
/* 247 */         sb.append('0');
/*     */       }
/*     */       
/* 250 */       sb.append(code);
/*     */       
/* 252 */       if (separator != null && i < bytes.length - 1) {
/* 253 */         sb.append(separator);
/*     */       }
/*     */     } 
/*     */     
/* 257 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] asByteArray(String hex) {
/* 267 */     byte[] bts = new byte[hex.length() / 2];
/* 268 */     for (int i = 0; i < bts.length; i++) {
/* 269 */       bts[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
/*     */     }
/*     */     
/* 272 */     return bts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int makeIntFromByte4(byte[] b) {
/* 282 */     return makeIntFromByte4(b, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int makeIntFromByte4(byte[] b, int offset) {
/* 293 */     return b[offset] << 24 | (b[offset + 1] & 0xFF) << 16 | (b[offset + 2] & 0xFF) << 8 | b[offset + 3] & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int makeIntFromByte2(byte[] b) {
/* 303 */     return makeIntFromByte2(b, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int makeIntFromByte2(byte[] b, int offset) {
/* 314 */     return (b[offset] & 0xFF) << 8 | b[offset + 1] & 0xFF;
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
/*     */   public static final boolean isFlagSet(int flagSet, int testFlag) {
/* 326 */     return ((flagSet & testFlag) > 0);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/utils/ByteUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */