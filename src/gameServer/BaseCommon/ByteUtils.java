/*     */ package BaseCommon;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteUtils
/*     */ {
/*     */   public static final short UNSIGNED_MAX_VALUE = 255;
/*     */   
/*     */   public static short toUnsigned(byte b) {
/*  52 */     return (short)((b < 0) ? (256 + b) : b);
/*     */   }
/*     */   
/*     */   public static String toHexAscii(byte b) {
/*  56 */     StringWriter sw = new StringWriter(2);
/*  57 */     addHexAscii(b, sw);
/*  58 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public static String toHexAscii(byte[] bytes) {
/*  62 */     int len = bytes.length;
/*  63 */     StringWriter sw = new StringWriter(len * 2);
/*  64 */     for (int i = 0; i < len; i++)
/*  65 */       addHexAscii(bytes[i], sw); 
/*  66 */     return sw.toString();
/*     */   }
/*     */   
/*     */   public static byte[] fromHexAscii(String s) throws NumberFormatException {
/*     */     try {
/*  71 */       int len = s.length();
/*  72 */       if (len % 2 != 0) {
/*  73 */         throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
/*     */       }
/*  75 */       int out_len = len / 2;
/*  76 */       byte[] out = new byte[out_len];
/*  77 */       int i = 0;
/*  78 */       StringReader sr = new StringReader(s);
/*  79 */       while (i < out_len) {
/*  80 */         int val = 16 * fromHexDigit(sr.read()) + fromHexDigit(sr.read());
/*  81 */         out[i++] = (byte)val;
/*     */       } 
/*  83 */       return out;
/*  84 */     } catch (IOException e) {
/*  85 */       throw new InternalError("IOException reading from StringReader?!?!");
/*     */     } 
/*     */   }
/*     */   
/*     */   static void addHexAscii(byte b, StringWriter sw) {
/*  90 */     short ub = toUnsigned(b);
/*  91 */     int h1 = ub / 16;
/*  92 */     int h2 = ub % 16;
/*  93 */     sw.write(toHexDigit(h1));
/*  94 */     sw.write(toHexDigit(h2));
/*     */   }
/*     */   
/*     */   private static int fromHexDigit(int c) throws NumberFormatException {
/*  98 */     if (c >= 48 && c < 58)
/*  99 */       return c - 48; 
/* 100 */     if (c >= 65 && c < 71)
/* 101 */       return c - 55; 
/* 102 */     if (c >= 97 && c < 103) {
/* 103 */       return c - 87;
/*     */     }
/* 105 */     throw new NumberFormatException(String.valueOf(39 + c) + "' is not a valid hexadecimal digit.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char toHexDigit(int h) {
/*     */     char out;
/* 113 */     if (h <= 9) {
/* 114 */       out = (char)(h + 48);
/*     */     } else {
/* 116 */       out = (char)(h + 55);
/*     */     } 
/* 118 */     return out;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseCommon/ByteUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */