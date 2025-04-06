/*     */ package com.notnoop.apns.internal;
/*     */ 
/*     */ import com.notnoop.exceptions.InvalidSSLConfig;
/*     */ import com.notnoop.exceptions.NetworkIOException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.Socket;
/*     */ import java.security.KeyStore;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Utilities
/*     */ {
/*  57 */   private static Logger logger = LoggerFactory.getLogger(Utilities.class);
/*     */   
/*     */   public static final String SANDBOX_GATEWAY_HOST = "gateway.sandbox.push.apple.com";
/*     */   
/*     */   public static final int SANDBOX_GATEWAY_PORT = 2195;
/*     */   
/*     */   public static final String SANDBOX_FEEDBACK_HOST = "feedback.sandbox.push.apple.com";
/*     */   
/*     */   public static final int SANDBOX_FEEDBACK_PORT = 2196;
/*     */   public static final String PRODUCTION_GATEWAY_HOST = "gateway.push.apple.com";
/*     */   public static final int PRODUCTION_GATEWAY_PORT = 2195;
/*     */   public static final String PRODUCTION_FEEDBACK_HOST = "feedback.push.apple.com";
/*     */   public static final int PRODUCTION_FEEDBACK_PORT = 2196;
/*     */   public static final int MAX_PAYLOAD_LENGTH = 256;
/*     */   
/*     */   private Utilities() {
/*  73 */     throw new AssertionError("Uninstantiable class");
/*     */   }
/*     */   
/*     */   public static SSLSocketFactory newSSLSocketFactory(InputStream cert, String password, String ksType, String ksAlgorithm) throws InvalidSSLConfig {
/*  77 */     SSLContext context = newSSLContext(cert, password, ksType, ksAlgorithm);
/*  78 */     return context.getSocketFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public static SSLContext newSSLContext(InputStream cert, String password, String ksType, String ksAlgorithm) throws InvalidSSLConfig {
/*     */     try {
/*  84 */       KeyStore ks = KeyStore.getInstance(ksType);
/*  85 */       ks.load(cert, password.toCharArray());
/*  86 */       return newSSLContext(ks, password, ksAlgorithm);
/*  87 */     } catch (Exception e) {
/*  88 */       throw new InvalidSSLConfig(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static SSLContext newSSLContext(KeyStore ks, String password, String ksAlgorithm) throws InvalidSSLConfig {
/*     */     try {
/*  96 */       KeyManagerFactory kmf = KeyManagerFactory.getInstance(ksAlgorithm);
/*  97 */       kmf.init(ks, password.toCharArray());
/*     */ 
/*     */ 
/*     */       
/* 101 */       TrustManagerFactory tmf = TrustManagerFactory.getInstance(ksAlgorithm);
/* 102 */       tmf.init((KeyStore)null);
/*     */ 
/*     */       
/* 105 */       SSLContext sslc = SSLContext.getInstance("TLS");
/* 106 */       sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
/* 107 */       return sslc;
/* 108 */     } catch (Exception e) {
/* 109 */       throw new InvalidSSLConfig(e);
/*     */     } 
/*     */   }
/*     */   
/* 113 */   private static final Pattern pattern = Pattern.compile("[ -]");
/*     */   public static byte[] decodeHex(String deviceToken) {
/* 115 */     String hex = pattern.matcher(deviceToken).replaceAll("");
/*     */     
/* 117 */     byte[] bts = new byte[hex.length() / 2];
/* 118 */     for (int i = 0; i < bts.length; i++) {
/* 119 */       bts[i] = (byte)(charval(hex.charAt(2 * i)) * 16 + charval(hex.charAt(2 * i + 1)));
/*     */     }
/* 121 */     return bts;
/*     */   }
/*     */   
/*     */   private static int charval(char a) {
/* 125 */     if ('0' <= a && a <= '9')
/* 126 */       return a - 48; 
/* 127 */     if ('a' <= a && a <= 'f')
/* 128 */       return a - 97 + 10; 
/* 129 */     if ('A' <= a && a <= 'F') {
/* 130 */       return a - 65 + 10;
/*     */     }
/* 132 */     throw new RuntimeException("Invalid hex character: " + a);
/*     */   }
/*     */ 
/*     */   
/* 136 */   private static final char[] base = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */   
/*     */   public static String encodeHex(byte[] bytes) {
/* 139 */     char[] chars = new char[bytes.length * 2];
/*     */     
/* 141 */     for (int i = 0; i < bytes.length; i++) {
/* 142 */       int b = bytes[i] & 0xFF;
/* 143 */       chars[2 * i] = base[b >>> 4];
/* 144 */       chars[2 * i + 1] = base[b & 0xF];
/*     */     } 
/*     */     
/* 147 */     return new String(chars);
/*     */   }
/*     */   
/*     */   public static byte[] toUTF8Bytes(String s) {
/*     */     try {
/* 152 */       return s.getBytes("UTF-8");
/* 153 */     } catch (UnsupportedEncodingException e) {
/* 154 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static byte[] marshall(byte command, byte[] deviceToken, byte[] payload) {
/* 159 */     ByteArrayOutputStream boas = new ByteArrayOutputStream();
/* 160 */     DataOutputStream dos = new DataOutputStream(boas);
/*     */     
/*     */     try {
/* 163 */       dos.writeByte(command);
/* 164 */       dos.writeShort(deviceToken.length);
/* 165 */       dos.write(deviceToken);
/* 166 */       dos.writeShort(payload.length);
/* 167 */       dos.write(payload);
/* 168 */       return boas.toByteArray();
/* 169 */     } catch (IOException e) {
/* 170 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] marshallEnhanced(byte command, int identifier, int expiryTime, byte[] deviceToken, byte[] payload) {
/* 176 */     ByteArrayOutputStream boas = new ByteArrayOutputStream();
/* 177 */     DataOutputStream dos = new DataOutputStream(boas);
/*     */     
/*     */     try {
/* 180 */       dos.writeByte(command);
/* 181 */       dos.writeInt(identifier);
/* 182 */       dos.writeInt(expiryTime);
/* 183 */       dos.writeShort(deviceToken.length);
/* 184 */       dos.write(deviceToken);
/* 185 */       dos.writeShort(payload.length);
/* 186 */       dos.write(payload);
/* 187 */       return boas.toByteArray();
/* 188 */     } catch (IOException e) {
/* 189 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Map<byte[], Integer> parseFeedbackStreamRaw(InputStream in) {
/* 194 */     Map<byte[], Integer> result = (Map)new HashMap<byte, Integer>();
/*     */     
/* 196 */     DataInputStream data = new DataInputStream(in);
/*     */     
/*     */     try {
/*     */       while (true)
/* 200 */       { int time = data.readInt();
/* 201 */         int dtLength = data.readUnsignedShort();
/* 202 */         byte[] deviceToken = new byte[dtLength];
/* 203 */         data.readFully(deviceToken);
/*     */         
/* 205 */         result.put(deviceToken, Integer.valueOf(time)); } 
/* 206 */     } catch (EOFException e) {
/*     */     
/* 208 */     } catch (IOException e) {
/* 209 */       throw new RuntimeException(e);
/*     */     } 
/*     */ 
/*     */     
/* 213 */     return result;
/*     */   }
/*     */   
/*     */   public static Map<String, Date> parseFeedbackStream(InputStream in) {
/* 217 */     Map<String, Date> result = new HashMap<String, Date>();
/*     */     
/* 219 */     Map<byte[], Integer> raw = parseFeedbackStreamRaw(in);
/* 220 */     for (Map.Entry<byte[], Integer> entry : raw.entrySet()) {
/* 221 */       byte[] dtArray = entry.getKey();
/* 222 */       int time = ((Integer)entry.getValue()).intValue();
/*     */       
/* 224 */       Date date = new Date(time * 1000L);
/* 225 */       String dtString = encodeHex(dtArray);
/* 226 */       result.put(dtString, date);
/*     */     } 
/*     */     
/* 229 */     return result;
/*     */   }
/*     */   
/*     */   public static void close(Closeable closeable) {
/*     */     try {
/* 234 */       if (closeable != null) {
/* 235 */         closeable.close();
/*     */       }
/* 237 */     } catch (IOException e) {
/* 238 */       logger.debug("error while closing resource", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void close(Socket closeable) {
/*     */     try {
/* 244 */       if (closeable != null) {
/* 245 */         closeable.close();
/*     */       }
/* 247 */     } catch (IOException e) {
/* 248 */       logger.debug("error while closing socket", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void sleep(int delay) {
/*     */     try {
/* 254 */       Thread.sleep(delay);
/* 255 */     } catch (InterruptedException e1) {}
/*     */   }
/*     */   
/*     */   public static byte[] copyOf(byte[] bytes) {
/* 259 */     byte[] copy = new byte[bytes.length];
/* 260 */     System.arraycopy(bytes, 0, copy, 0, bytes.length);
/* 261 */     return copy;
/*     */   }
/*     */   
/*     */   public static byte[] copyOfRange(byte[] original, int from, int to) {
/* 265 */     int newLength = to - from;
/* 266 */     if (newLength < 0) {
/* 267 */       throw new IllegalArgumentException(from + " > " + to);
/*     */     }
/* 269 */     byte[] copy = new byte[newLength];
/* 270 */     System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
/*     */     
/* 272 */     return copy;
/*     */   }
/*     */   
/*     */   public static void wrapAndThrowAsRuntimeException(Exception e) throws NetworkIOException {
/* 276 */     if (e instanceof IOException)
/* 277 */       throw new NetworkIOException((IOException)e); 
/* 278 */     if (e instanceof NetworkIOException)
/* 279 */       throw (NetworkIOException)e; 
/* 280 */     if (e instanceof RuntimeException) {
/* 281 */       throw (RuntimeException)e;
/*     */     }
/* 283 */     throw new RuntimeException(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseBytes(int b1, int b2, int b3, int b4) {
/* 288 */     return b1 << 24 & 0xFF000000 | b2 << 16 & 0xFF0000 | b3 << 8 & 0xFF00 | b4 << 0 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String truncateWhenUTF8(String s, int maxBytes) {
/* 296 */     int b = 0;
/* 297 */     for (int i = 0; i < s.length(); i++) {
/* 298 */       int more; char c = s.charAt(i);
/*     */ 
/*     */       
/* 301 */       int skip = 0;
/*     */       
/* 303 */       if (c <= '') {
/* 304 */         more = 1;
/*     */       }
/* 306 */       else if (c <= '߿') {
/* 307 */         more = 2;
/* 308 */       } else if (c <= '퟿') {
/* 309 */         more = 3;
/* 310 */       } else if (c <= '?') {
/*     */         
/* 312 */         more = 4;
/* 313 */         skip = 1;
/*     */       } else {
/* 315 */         more = 3;
/*     */       } 
/*     */       
/* 318 */       if (b + more > maxBytes) {
/* 319 */         return s.substring(0, i);
/*     */       }
/* 321 */       b += more;
/* 322 */       i += skip;
/*     */     } 
/* 324 */     return s;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/apns/internal/Utilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */