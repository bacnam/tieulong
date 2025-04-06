/*     */ package org.apache.mina.proxy.handlers.http.ntlm;
/*     */ 
/*     */ import java.security.Key;
/*     */ import java.security.MessageDigest;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NTLMResponses
/*     */ {
/*  46 */   public static final byte[] LM_HASH_MAGIC_CONSTANT = new byte[] { 75, 71, 83, 33, 64, 35, 36, 37 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getLMResponse(String password, byte[] challenge) throws Exception {
/*  59 */     byte[] lmHash = lmHash(password);
/*  60 */     return lmResponse(lmHash, challenge);
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
/*     */   public static byte[] getNTLMResponse(String password, byte[] challenge) throws Exception {
/*  73 */     byte[] ntlmHash = ntlmHash(password);
/*  74 */     return lmResponse(ntlmHash, challenge);
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
/*     */   public static byte[] getNTLMv2Response(String target, String user, String password, byte[] targetInformation, byte[] challenge, byte[] clientNonce) throws Exception {
/*  95 */     return getNTLMv2Response(target, user, password, targetInformation, challenge, clientNonce, System.currentTimeMillis());
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
/*     */   public static byte[] getNTLMv2Response(String target, String user, String password, byte[] targetInformation, byte[] challenge, byte[] clientNonce, long time) throws Exception {
/* 117 */     byte[] ntlmv2Hash = ntlmv2Hash(target, user, password);
/* 118 */     byte[] blob = createBlob(targetInformation, clientNonce, time);
/* 119 */     return lmv2Response(ntlmv2Hash, blob, challenge);
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
/*     */   public static byte[] getLMv2Response(String target, String user, String password, byte[] challenge, byte[] clientNonce) throws Exception {
/* 137 */     byte[] ntlmv2Hash = ntlmv2Hash(target, user, password);
/* 138 */     return lmv2Response(ntlmv2Hash, clientNonce, challenge);
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
/*     */   public static byte[] getNTLM2SessionResponse(String password, byte[] challenge, byte[] clientNonce) throws Exception {
/* 155 */     byte[] ntlmHash = ntlmHash(password);
/* 156 */     MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 157 */     md5.update(challenge);
/* 158 */     md5.update(clientNonce);
/* 159 */     byte[] sessionHash = new byte[8];
/* 160 */     System.arraycopy(md5.digest(), 0, sessionHash, 0, 8);
/* 161 */     return lmResponse(ntlmHash, sessionHash);
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
/*     */   private static byte[] lmHash(String password) throws Exception {
/* 173 */     byte[] oemPassword = password.toUpperCase().getBytes("US-ASCII");
/* 174 */     int length = Math.min(oemPassword.length, 14);
/* 175 */     byte[] keyBytes = new byte[14];
/* 176 */     System.arraycopy(oemPassword, 0, keyBytes, 0, length);
/* 177 */     Key lowKey = createDESKey(keyBytes, 0);
/* 178 */     Key highKey = createDESKey(keyBytes, 7);
/* 179 */     Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/* 180 */     des.init(1, lowKey);
/* 181 */     byte[] lowHash = des.doFinal(LM_HASH_MAGIC_CONSTANT);
/* 182 */     des.init(1, highKey);
/* 183 */     byte[] highHash = des.doFinal(LM_HASH_MAGIC_CONSTANT);
/* 184 */     byte[] lmHash = new byte[16];
/* 185 */     System.arraycopy(lowHash, 0, lmHash, 0, 8);
/* 186 */     System.arraycopy(highHash, 0, lmHash, 8, 8);
/* 187 */     return lmHash;
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
/*     */   private static byte[] ntlmHash(String password) throws Exception {
/* 199 */     byte[] unicodePassword = password.getBytes("UnicodeLittleUnmarked");
/* 200 */     MessageDigest md4 = MessageDigest.getInstance("MD4");
/* 201 */     return md4.digest(unicodePassword);
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
/*     */   private static byte[] ntlmv2Hash(String target, String user, String password) throws Exception {
/* 215 */     byte[] ntlmHash = ntlmHash(password);
/* 216 */     String identity = user.toUpperCase() + target;
/* 217 */     return hmacMD5(identity.getBytes("UnicodeLittleUnmarked"), ntlmHash);
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
/*     */   private static byte[] lmResponse(byte[] hash, byte[] challenge) throws Exception {
/* 230 */     byte[] keyBytes = new byte[21];
/* 231 */     System.arraycopy(hash, 0, keyBytes, 0, 16);
/* 232 */     Key lowKey = createDESKey(keyBytes, 0);
/* 233 */     Key middleKey = createDESKey(keyBytes, 7);
/* 234 */     Key highKey = createDESKey(keyBytes, 14);
/* 235 */     Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/* 236 */     des.init(1, lowKey);
/* 237 */     byte[] lowResponse = des.doFinal(challenge);
/* 238 */     des.init(1, middleKey);
/* 239 */     byte[] middleResponse = des.doFinal(challenge);
/* 240 */     des.init(1, highKey);
/* 241 */     byte[] highResponse = des.doFinal(challenge);
/* 242 */     byte[] lmResponse = new byte[24];
/* 243 */     System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
/* 244 */     System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
/* 245 */     System.arraycopy(highResponse, 0, lmResponse, 16, 8);
/* 246 */     return lmResponse;
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
/*     */   private static byte[] lmv2Response(byte[] hash, byte[] clientData, byte[] challenge) throws Exception {
/* 261 */     byte[] data = new byte[challenge.length + clientData.length];
/* 262 */     System.arraycopy(challenge, 0, data, 0, challenge.length);
/* 263 */     System.arraycopy(clientData, 0, data, challenge.length, clientData.length);
/* 264 */     byte[] mac = hmacMD5(data, hash);
/* 265 */     byte[] lmv2Response = new byte[mac.length + clientData.length];
/* 266 */     System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
/* 267 */     System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
/* 268 */     return lmv2Response;
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
/*     */   private static byte[] createBlob(byte[] targetInformation, byte[] clientNonce, long time) {
/* 283 */     byte[] blobSignature = { 1, 1, 0, 0 };
/* 284 */     byte[] reserved = { 0, 0, 0, 0 };
/* 285 */     byte[] unknown1 = { 0, 0, 0, 0 };
/* 286 */     byte[] unknown2 = { 0, 0, 0, 0 };
/* 287 */     time += 11644473600000L;
/* 288 */     time *= 10000L;
/*     */     
/* 290 */     byte[] timestamp = new byte[8];
/* 291 */     for (int i = 0; i < 8; i++) {
/* 292 */       timestamp[i] = (byte)(int)time;
/* 293 */       time >>>= 8L;
/*     */     } 
/* 295 */     byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + clientNonce.length + unknown1.length + targetInformation.length + unknown2.length];
/*     */     
/* 297 */     int offset = 0;
/* 298 */     System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
/* 299 */     offset += blobSignature.length;
/* 300 */     System.arraycopy(reserved, 0, blob, offset, reserved.length);
/* 301 */     offset += reserved.length;
/* 302 */     System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
/* 303 */     offset += timestamp.length;
/* 304 */     System.arraycopy(clientNonce, 0, blob, offset, clientNonce.length);
/* 305 */     offset += clientNonce.length;
/* 306 */     System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
/* 307 */     offset += unknown1.length;
/* 308 */     System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
/* 309 */     offset += targetInformation.length;
/* 310 */     System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
/* 311 */     return blob;
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
/*     */   public static byte[] hmacMD5(byte[] data, byte[] key) throws Exception {
/* 324 */     byte[] ipad = new byte[64];
/* 325 */     byte[] opad = new byte[64];
/*     */ 
/*     */     
/* 328 */     for (int i = 0; i < 64; i++) {
/* 329 */       if (i < key.length) {
/* 330 */         ipad[i] = (byte)(key[i] ^ 0x36);
/* 331 */         opad[i] = (byte)(key[i] ^ 0x5C);
/*     */       } else {
/* 333 */         ipad[i] = 54;
/* 334 */         opad[i] = 92;
/*     */       } 
/*     */     } 
/*     */     
/* 338 */     byte[] content = new byte[data.length + 64];
/* 339 */     System.arraycopy(ipad, 0, content, 0, 64);
/* 340 */     System.arraycopy(data, 0, content, 64, data.length);
/* 341 */     MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 342 */     data = md5.digest(content);
/* 343 */     content = new byte[data.length + 64];
/* 344 */     System.arraycopy(opad, 0, content, 0, 64);
/* 345 */     System.arraycopy(data, 0, content, 64, data.length);
/* 346 */     return md5.digest(content);
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
/*     */   private static Key createDESKey(byte[] bytes, int offset) {
/* 360 */     byte[] keyBytes = new byte[7];
/* 361 */     System.arraycopy(bytes, offset, keyBytes, 0, 7);
/* 362 */     byte[] material = new byte[8];
/* 363 */     material[0] = keyBytes[0];
/* 364 */     material[1] = (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1);
/* 365 */     material[2] = (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2);
/* 366 */     material[3] = (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3);
/* 367 */     material[4] = (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4);
/* 368 */     material[5] = (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5);
/* 369 */     material[6] = (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6);
/* 370 */     material[7] = (byte)(keyBytes[6] << 1);
/* 371 */     oddParity(material);
/* 372 */     return new SecretKeySpec(material, "DES");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void oddParity(byte[] bytes) {
/* 382 */     for (int i = 0; i < bytes.length; i++) {
/* 383 */       byte b = bytes[i];
/* 384 */       boolean needsParity = (((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0);
/* 385 */       if (needsParity) {
/* 386 */         bytes[i] = (byte)(bytes[i] | 0x1);
/*     */       } else {
/* 388 */         bytes[i] = (byte)(bytes[i] & 0xFFFFFFFE);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/ntlm/NTLMResponses.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */