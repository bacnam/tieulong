/*     */ package org.apache.mina.proxy.handlers.http.ntlm;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.mina.proxy.utils.ByteUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NTLMUtilities
/*     */   implements NTLMConstants
/*     */ {
/*     */   public static final byte[] writeSecurityBuffer(short length, int bufferOffset) {
/*  43 */     byte[] b = new byte[8];
/*  44 */     writeSecurityBuffer(length, length, bufferOffset, b, 0);
/*     */     
/*  46 */     return b;
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
/*     */   public static final void writeSecurityBuffer(short length, short allocated, int bufferOffset, byte[] b, int offset) {
/*  65 */     ByteUtilities.writeShort(length, b, offset);
/*  66 */     ByteUtilities.writeShort(allocated, b, offset + 2);
/*  67 */     ByteUtilities.writeInt(bufferOffset, b, offset + 4);
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
/*     */   public static final void writeOSVersion(byte majorVersion, byte minorVersion, short buildNumber, byte[] b, int offset) {
/*  83 */     b[offset] = majorVersion;
/*  84 */     b[offset + 1] = minorVersion;
/*  85 */     b[offset + 2] = (byte)buildNumber;
/*  86 */     b[offset + 3] = (byte)(buildNumber >> 8);
/*  87 */     b[offset + 4] = 0;
/*  88 */     b[offset + 5] = 0;
/*  89 */     b[offset + 6] = 0;
/*  90 */     b[offset + 7] = 15;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] getOsVersion() {
/* 101 */     String os = System.getProperty("os.name");
/*     */     
/* 103 */     if (os == null || !os.toUpperCase().contains("WINDOWS")) {
/* 104 */       return DEFAULT_OS_VERSION;
/*     */     }
/*     */     
/* 107 */     byte[] osVer = new byte[8];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 113 */       Process pr = Runtime.getRuntime().exec("cmd /C ver");
/* 114 */       BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
/* 115 */       pr.waitFor();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 121 */         line = reader.readLine();
/* 122 */       } while (line != null && line.length() != 0);
/*     */       
/* 124 */       reader.close();
/*     */ 
/*     */       
/* 127 */       if (line == null)
/*     */       {
/* 129 */         throw new Exception();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 134 */       int pos = line.toLowerCase().indexOf("version");
/*     */       
/* 136 */       if (pos == -1)
/*     */       {
/* 138 */         throw new Exception();
/*     */       }
/*     */       
/* 141 */       pos += 8;
/* 142 */       String line = line.substring(pos, line.indexOf(']'));
/* 143 */       StringTokenizer tk = new StringTokenizer(line, ".");
/*     */       
/* 145 */       if (tk.countTokens() != 3)
/*     */       {
/* 147 */         throw new Exception();
/*     */       }
/*     */       
/* 150 */       writeOSVersion(Byte.parseByte(tk.nextToken()), Byte.parseByte(tk.nextToken()), Short.parseShort(tk.nextToken()), osVer, 0);
/*     */     }
/* 152 */     catch (Exception ex) {
/*     */       try {
/* 154 */         String version = System.getProperty("os.version");
/* 155 */         writeOSVersion(Byte.parseByte(version.substring(0, 1)), Byte.parseByte(version.substring(2, 3)), (short)0, osVer, 0);
/*     */       }
/* 157 */       catch (Exception ex2) {
/* 158 */         return DEFAULT_OS_VERSION;
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     return osVer;
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
/*     */   public static final byte[] createType1Message(String workStation, String domain, Integer customFlags, byte[] osVersion) {
/* 178 */     byte[] msg = null;
/*     */     
/* 180 */     if (osVersion != null && osVersion.length != 8) {
/* 181 */       throw new IllegalArgumentException("osVersion parameter should be a 8 byte wide array");
/*     */     }
/*     */     
/* 184 */     if (workStation == null || domain == null) {
/* 185 */       throw new IllegalArgumentException("workStation and domain must be non null");
/*     */     }
/*     */     
/* 188 */     int flags = (customFlags != null) ? (customFlags.intValue() | 0x2000 | 0x1000) : 12291;
/*     */ 
/*     */     
/* 191 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     
/*     */     try {
/* 194 */       baos.write(NTLM_SIGNATURE);
/* 195 */       baos.write(ByteUtilities.writeInt(1));
/* 196 */       baos.write(ByteUtilities.writeInt(flags));
/*     */       
/* 198 */       byte[] domainData = ByteUtilities.getOEMStringAsByteArray(domain);
/* 199 */       byte[] workStationData = ByteUtilities.getOEMStringAsByteArray(workStation);
/*     */       
/* 201 */       int pos = (osVersion != null) ? 40 : 32;
/* 202 */       baos.write(writeSecurityBuffer((short)domainData.length, pos + workStationData.length));
/* 203 */       baos.write(writeSecurityBuffer((short)workStationData.length, pos));
/*     */       
/* 205 */       if (osVersion != null) {
/* 206 */         baos.write(osVersion);
/*     */       }
/*     */ 
/*     */       
/* 210 */       baos.write(workStationData);
/* 211 */       baos.write(domainData);
/*     */       
/* 213 */       msg = baos.toByteArray();
/* 214 */       baos.close();
/* 215 */     } catch (IOException e) {
/* 216 */       return null;
/*     */     } 
/*     */     
/* 219 */     return msg;
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
/*     */   public static final int writeSecurityBufferAndUpdatePointer(ByteArrayOutputStream baos, short len, int pointer) throws IOException {
/* 234 */     baos.write(writeSecurityBuffer(len, pointer));
/*     */     
/* 236 */     return pointer + len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] extractChallengeFromType2Message(byte[] msg) {
/* 246 */     byte[] challenge = new byte[8];
/* 247 */     System.arraycopy(msg, 24, challenge, 0, 8);
/*     */     
/* 249 */     return challenge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int extractFlagsFromType2Message(byte[] msg) {
/* 259 */     byte[] flagsBytes = new byte[4];
/*     */     
/* 261 */     System.arraycopy(msg, 20, flagsBytes, 0, 4);
/* 262 */     ByteUtilities.changeWordEndianess(flagsBytes, 0, 4);
/*     */     
/* 264 */     return ByteUtilities.makeIntFromByte4(flagsBytes);
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
/*     */   public static final byte[] readSecurityBufferTarget(byte[] msg, int securityBufferOffset) {
/* 276 */     byte[] securityBuffer = new byte[8];
/*     */     
/* 278 */     System.arraycopy(msg, securityBufferOffset, securityBuffer, 0, 8);
/* 279 */     ByteUtilities.changeWordEndianess(securityBuffer, 0, 8);
/* 280 */     int length = ByteUtilities.makeIntFromByte2(securityBuffer);
/* 281 */     int offset = ByteUtilities.makeIntFromByte4(securityBuffer, 4);
/*     */     
/* 283 */     byte[] secBufValue = new byte[length];
/* 284 */     System.arraycopy(msg, offset, secBufValue, 0, length);
/*     */     
/* 286 */     return secBufValue;
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
/*     */   public static final String extractTargetNameFromType2Message(byte[] msg, Integer msgFlags) throws UnsupportedEncodingException {
/* 303 */     byte[] targetName = readSecurityBufferTarget(msg, 12);
/*     */ 
/*     */     
/* 306 */     int flags = (msgFlags == null) ? extractFlagsFromType2Message(msg) : msgFlags.intValue();
/*     */     
/* 308 */     if (ByteUtilities.isFlagSet(flags, 1)) {
/* 309 */       return new String(targetName, "UTF-16LE");
/*     */     }
/*     */     
/* 312 */     return new String(targetName, "ASCII");
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
/*     */   public static final byte[] extractTargetInfoFromType2Message(byte[] msg, Integer msgFlags) {
/* 324 */     int flags = (msgFlags == null) ? extractFlagsFromType2Message(msg) : msgFlags.intValue();
/*     */     
/* 326 */     if (!ByteUtilities.isFlagSet(flags, 8388608)) {
/* 327 */       return null;
/*     */     }
/*     */     
/* 330 */     int pos = 40;
/*     */     
/* 332 */     return readSecurityBufferTarget(msg, pos);
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
/*     */   public static final void printTargetInformationBlockFromType2Message(byte[] msg, Integer msgFlags, PrintWriter out) throws UnsupportedEncodingException {
/* 348 */     int flags = (msgFlags == null) ? extractFlagsFromType2Message(msg) : msgFlags.intValue();
/*     */     
/* 350 */     byte[] infoBlock = extractTargetInfoFromType2Message(msg, Integer.valueOf(flags));
/*     */     
/* 352 */     if (infoBlock == null) {
/* 353 */       out.println("No target information block found !");
/*     */     } else {
/* 355 */       int pos = 0;
/*     */       
/* 357 */       while (infoBlock[pos] != 0) {
/* 358 */         out.print("---\nType " + infoBlock[pos] + ": ");
/*     */         
/* 360 */         switch (infoBlock[pos]) {
/*     */           case 1:
/* 362 */             out.println("Server name");
/*     */             break;
/*     */           case 2:
/* 365 */             out.println("Domain name");
/*     */             break;
/*     */           case 3:
/* 368 */             out.println("Fully qualified DNS hostname");
/*     */             break;
/*     */           case 4:
/* 371 */             out.println("DNS domain name");
/*     */             break;
/*     */           case 5:
/* 374 */             out.println("Parent DNS domain name");
/*     */             break;
/*     */         } 
/*     */         
/* 378 */         byte[] len = new byte[2];
/* 379 */         System.arraycopy(infoBlock, pos + 2, len, 0, 2);
/* 380 */         ByteUtilities.changeByteEndianess(len, 0, 2);
/*     */         
/* 382 */         int length = ByteUtilities.makeIntFromByte2(len, 0);
/* 383 */         out.println("Length: " + length + " bytes");
/* 384 */         out.print("Data: ");
/*     */         
/* 386 */         if (ByteUtilities.isFlagSet(flags, 1)) {
/* 387 */           out.println(new String(infoBlock, pos + 4, length, "UTF-16LE"));
/*     */         } else {
/* 389 */           out.println(new String(infoBlock, pos + 4, length, "ASCII"));
/*     */         } 
/*     */         
/* 392 */         pos += 4 + length;
/* 393 */         out.flush();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] createType3Message(String user, String password, byte[] challenge, String target, String workstation, Integer serverFlags, byte[] osVersion) {
/* 421 */     byte[] msg = null;
/*     */     
/* 423 */     if (challenge == null || challenge.length != 8) {
/* 424 */       throw new IllegalArgumentException("challenge[] should be a 8 byte wide array");
/*     */     }
/*     */     
/* 427 */     if (osVersion != null && osVersion.length != 8) {
/* 428 */       throw new IllegalArgumentException("osVersion should be a 8 byte wide array");
/*     */     }
/*     */     
/* 431 */     int flags = (serverFlags != null) ? serverFlags.intValue() : 12291;
/*     */     
/* 433 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     
/*     */     try {
/* 436 */       baos.write(NTLM_SIGNATURE);
/* 437 */       baos.write(ByteUtilities.writeInt(3));
/*     */       
/* 439 */       byte[] dataLMResponse = NTLMResponses.getLMResponse(password, challenge);
/* 440 */       byte[] dataNTLMResponse = NTLMResponses.getNTLMResponse(password, challenge);
/*     */       
/* 442 */       boolean useUnicode = ByteUtilities.isFlagSet(flags, 1);
/* 443 */       byte[] targetName = ByteUtilities.encodeString(target, useUnicode);
/* 444 */       byte[] userName = ByteUtilities.encodeString(user, useUnicode);
/* 445 */       byte[] workstationName = ByteUtilities.encodeString(workstation, useUnicode);
/*     */       
/* 447 */       int pos = (osVersion != null) ? 72 : 64;
/* 448 */       int responsePos = pos + targetName.length + userName.length + workstationName.length;
/* 449 */       responsePos = writeSecurityBufferAndUpdatePointer(baos, (short)dataLMResponse.length, responsePos);
/* 450 */       writeSecurityBufferAndUpdatePointer(baos, (short)dataNTLMResponse.length, responsePos);
/* 451 */       pos = writeSecurityBufferAndUpdatePointer(baos, (short)targetName.length, pos);
/* 452 */       pos = writeSecurityBufferAndUpdatePointer(baos, (short)userName.length, pos);
/* 453 */       writeSecurityBufferAndUpdatePointer(baos, (short)workstationName.length, pos);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 467 */       baos.write(new byte[] { 0, 0, 0, 0, -102, 0, 0, 0 });
/*     */       
/* 469 */       baos.write(ByteUtilities.writeInt(flags));
/*     */       
/* 471 */       if (osVersion != null) {
/* 472 */         baos.write(osVersion);
/*     */       }
/*     */ 
/*     */       
/* 476 */       baos.write(targetName);
/* 477 */       baos.write(userName);
/* 478 */       baos.write(workstationName);
/*     */       
/* 480 */       baos.write(dataLMResponse);
/* 481 */       baos.write(dataNTLMResponse);
/*     */       
/* 483 */       msg = baos.toByteArray();
/* 484 */       baos.close();
/* 485 */     } catch (Exception e) {
/* 486 */       e.printStackTrace();
/* 487 */       return null;
/*     */     } 
/*     */     
/* 490 */     return msg;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/ntlm/NTLMUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */