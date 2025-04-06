/*     */ package com.zhonglian.server.http.client;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpUtil
/*     */ {
/*     */   public static String sendHttpGet2Web(int connTimeOutMs, int readTimeOutMs, String strGetUrl, String strPostParam, String exceptionRes) {
/*  26 */     String strRet = exceptionRes;
/*     */     
/*  28 */     HttpURLConnection connection = null;
/*  29 */     BufferedReader br = null;
/*     */     
/*     */     try {
/*     */       String strWholeUrl;
/*  33 */       if (strPostParam != null) {
/*  34 */         strWholeUrl = String.valueOf(strGetUrl) + "?" + strPostParam;
/*     */       } else {
/*  36 */         strWholeUrl = strGetUrl;
/*     */       } 
/*  38 */       URL url = new URL(strWholeUrl);
/*  39 */       connection = (HttpURLConnection)url.openConnection();
/*  40 */       connection.setConnectTimeout(connTimeOutMs);
/*  41 */       connection.setReadTimeout(readTimeOutMs);
/*  42 */       connection.setRequestMethod("GET");
/*  43 */       connection.connect();
/*  44 */       br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
/*  45 */       strRet = br.readLine();
/*     */       
/*  47 */       if (strRet != null) {
/*  48 */         strRet = _normReturn(strRet);
/*     */       }
/*     */ 
/*     */       
/*  52 */       br.close();
/*  53 */       connection.disconnect();
/*  54 */     } catch (Exception e) {
/*  55 */       CommLog.error(e.getMessage(), e);
/*     */       
/*     */       try {
/*  58 */         if (br != null)
/*  59 */           br.close(); 
/*  60 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/*  63 */         if (connection != null)
/*  64 */           connection.disconnect(); 
/*  65 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/*  69 */     return strRet;
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
/*     */   public static String sendHttpPost2Web(int connTimeOutMs, int readTimeOutMs, String _strPostUrl, String _strPostParam, String exceptionRes) {
/*  83 */     String strRet = exceptionRes;
/*     */     
/*  85 */     HttpURLConnection connection = null;
/*  86 */     BufferedReader br = null;
/*     */     try {
/*  88 */       URL url = new URL(_strPostUrl);
/*  89 */       connection = (HttpURLConnection)url.openConnection();
/*  90 */       connection.setDoOutput(true);
/*  91 */       connection.setDoInput(true);
/*  92 */       connection.setRequestMethod("POST");
/*  93 */       connection.setUseCaches(false);
/*  94 */       connection.setInstanceFollowRedirects(true);
/*  95 */       connection.setRequestProperty("Content-Type", "application/json");
/*  96 */       connection.setConnectTimeout(connTimeOutMs);
/*  97 */       connection.setReadTimeout(readTimeOutMs);
/*  98 */       connection.connect();
/*  99 */       DataOutputStream out = new DataOutputStream(connection.getOutputStream());
/*     */       
/* 101 */       if (_strPostParam != null) {
/* 102 */         out.writeBytes(_strPostParam);
/* 103 */         out.flush();
/*     */       } 
/* 105 */       out.close();
/*     */ 
/*     */       
/* 108 */       InputStream isRet = connection.getInputStream();
/* 109 */       br = new BufferedReader(new InputStreamReader(isRet, "UTF-8"));
/*     */       
/* 111 */       strRet = "";
/* 112 */       String strRetTemp = null;
/* 113 */       while ((strRetTemp = br.readLine()) != null) {
/* 114 */         strRet = String.valueOf(strRet) + strRetTemp;
/* 115 */         strRetTemp = null;
/*     */       } 
/*     */       
/* 118 */       br.close();
/* 119 */       connection.disconnect();
/* 120 */     } catch (SocketTimeoutException e) {
/* 121 */       CommLog.error("HttpPost time out for: [{} ?{}]", _strPostUrl, _strPostParam);
/*     */       try {
/* 123 */         if (br != null)
/* 124 */           br.close(); 
/* 125 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 128 */         if (connection != null)
/* 129 */           connection.disconnect(); 
/* 130 */       } catch (Exception exception) {}
/*     */     }
/* 132 */     catch (Exception e) {
/* 133 */       CommLog.error("HttpPost exception for: [{}?{}]", new Object[] { _strPostUrl, _strPostParam, e });
/*     */       
/*     */       try {
/* 136 */         if (br != null)
/* 137 */           br.close(); 
/* 138 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 141 */         if (connection != null)
/* 142 */           connection.disconnect(); 
/* 143 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 147 */     return strRet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String _normReturn(String strRetOrgi) {
/* 157 */     String strRet = strRetOrgi;
/* 158 */     int iStartPos = 0;
/* 159 */     for (int i = 0; i < strRetOrgi.length(); i++) {
/* 160 */       char ch = strRetOrgi.charAt(i);
/* 161 */       if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
/*     */         break;
/*     */       }
/* 164 */       iStartPos++;
/*     */     } 
/* 166 */     if (iStartPos != 0) {
/* 167 */       strRet = strRetOrgi.substring(iStartPos);
/*     */     }
/* 169 */     return strRet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] GetAll(String strGetUrl) {
/* 180 */     return GetAll(15000, 15000, strGetUrl, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] GetAll(String strGetUrl, String strPostParam) {
/* 191 */     return GetAll(15000, 15000, strGetUrl, strPostParam);
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
/*     */   public static byte[] GetAll(int connTimeOutMs, int readTimeOutMs, String strGetUrl, String strPostParam) {
/* 204 */     HttpURLConnection connection = null;
/* 205 */     InputStream br = null;
/*     */     
/*     */     try {
/*     */       String strWholeUrl;
/* 209 */       if (strPostParam != null && !strPostParam.trim().isEmpty()) {
/* 210 */         strWholeUrl = String.valueOf(strGetUrl) + "?" + strPostParam;
/*     */       } else {
/* 212 */         strWholeUrl = strGetUrl;
/*     */       } 
/* 214 */       URL url = new URL(strWholeUrl);
/* 215 */       connection = (HttpURLConnection)url.openConnection();
/* 216 */       connection.setConnectTimeout(connTimeOutMs);
/* 217 */       connection.setReadTimeout(readTimeOutMs);
/* 218 */       connection.setRequestMethod("GET");
/* 219 */       connection.connect();
/* 220 */       br = connection.getInputStream();
/*     */       
/* 222 */       ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
/* 223 */       byte[] buff = new byte[512];
/* 224 */       int rc = 0;
/* 225 */       while ((rc = br.read(buff, 0, 512)) > 0) {
/* 226 */         swapStream.write(buff, 0, rc);
/*     */       }
/* 228 */       br.close();
/*     */ 
/*     */       
/* 231 */       br.close();
/* 232 */       connection.disconnect();
/* 233 */       return swapStream.toByteArray();
/* 234 */     } catch (Exception e) {
/* 235 */       CommLog.error(e.getMessage(), e);
/*     */       try {
/* 237 */         if (br != null)
/* 238 */           br.close(); 
/* 239 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 242 */         if (connection != null)
/* 243 */           connection.disconnect(); 
/* 244 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 247 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String chinaToUnicode(String str) {
/* 257 */     String result = "";
/* 258 */     for (int i = 0; i < str.length(); i++) {
/* 259 */       int chr1 = str.charAt(i);
/* 260 */       if (chr1 >= 19968 && chr1 <= 171941) {
/* 261 */         result = String.valueOf(result) + "\\u" + Integer.toHexString(chr1);
/*     */       } else {
/* 263 */         result = String.valueOf(result) + str.charAt(i);
/*     */       } 
/*     */     } 
/* 266 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String decodeUnicode(String theString) {
/* 277 */     int len = theString.length();
/* 278 */     StringBuffer outBuffer = new StringBuffer(len);
/* 279 */     for (int x = 0; x < len; ) {
/* 280 */       char aChar = theString.charAt(x++);
/* 281 */       if (aChar == '\\') {
/* 282 */         aChar = theString.charAt(x++);
/* 283 */         if (aChar == 'u') {
/* 284 */           int value = 0;
/* 285 */           for (int i = 0; i < 4; i++) {
/* 286 */             aChar = theString.charAt(x++);
/* 287 */             switch (aChar) {
/*     */               case '0':
/*     */               case '1':
/*     */               case '2':
/*     */               case '3':
/*     */               case '4':
/*     */               case '5':
/*     */               case '6':
/*     */               case '7':
/*     */               case '8':
/*     */               case '9':
/* 298 */                 value = (value << 4) + aChar - 48;
/*     */                 break;
/*     */               case 'a':
/*     */               case 'b':
/*     */               case 'c':
/*     */               case 'd':
/*     */               case 'e':
/*     */               case 'f':
/* 306 */                 value = (value << 4) + 10 + aChar - 97;
/*     */                 break;
/*     */               case 'A':
/*     */               case 'B':
/*     */               case 'C':
/*     */               case 'D':
/*     */               case 'E':
/*     */               case 'F':
/* 314 */                 value = (value << 4) + 10 + aChar - 65;
/*     */                 break;
/*     */               default:
/* 317 */                 throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
/*     */             } 
/*     */           } 
/* 320 */           outBuffer.append((char)value); continue;
/*     */         } 
/* 322 */         if (aChar == 't') {
/* 323 */           aChar = '\t';
/* 324 */         } else if (aChar == 'r') {
/* 325 */           aChar = '\r';
/* 326 */         } else if (aChar == 'n') {
/* 327 */           aChar = '\n';
/* 328 */         } else if (aChar == 'f') {
/* 329 */           aChar = '\f';
/* 330 */         }  outBuffer.append(aChar);
/*     */         continue;
/*     */       } 
/* 333 */       outBuffer.append(aChar);
/*     */     } 
/* 335 */     return outBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/client/HttpUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */